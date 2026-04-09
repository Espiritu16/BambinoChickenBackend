import { Injectable, computed, inject, signal } from '@angular/core';
import { AuthService } from '../../../../core/auth/auth.service';
import { mapApiError } from './pos-api.utils';
import {
  ApiError,
  Caja,
  MetodoPago,
  RegistrarVentaRequest,
  TipoCierre,
  TipoComprobante,
  Venta,
  VentaResumenDiarioResponse
} from './pos.models';
import { CajaApiService } from './caja-api.service';
import { VentasApiService } from './ventas-api.service';

export interface ProductoCatalogo {
  idProducto: number;
  nombre: string;
  precio: number;
  categoria: string;
}

export interface DetalleVentaUi {
  idProducto: number;
  nombre: string;
  cantidad: number;
  precioUnitario: number;
  subtotal: number;
}

export const PRODUCTOS_POS: ProductoCatalogo[] = [
  { idProducto: 1, nombre: '1/4 Pollo a la Brasa', precio: 18, categoria: 'Pollos' },
  { idProducto: 2, nombre: '1/2 Pollo a la Brasa', precio: 32, categoria: 'Pollos' },
  { idProducto: 3, nombre: 'Pollo Entero a la Brasa', precio: 60, categoria: 'Pollos' },
  { idProducto: 4, nombre: 'Pollo Broaster 6 pzas', precio: 25, categoria: 'Pollos' },
  { idProducto: 5, nombre: 'Papas Fritas', precio: 8, categoria: 'Acompañamientos' },
  { idProducto: 6, nombre: 'Ensalada', precio: 10, categoria: 'Acompañamientos' },
  { idProducto: 7, nombre: 'Arroz Chaufa', precio: 12, categoria: 'Acompañamientos' },
  { idProducto: 8, nombre: 'Inca Kola 1L', precio: 7, categoria: 'Bebidas' },
  { idProducto: 9, nombre: 'Chicha Morada 1L', precio: 6, categoria: 'Bebidas' },
  { idProducto: 10, nombre: 'Agua 500ml', precio: 3, categoria: 'Bebidas' }
];

const IGV_PERCENTAGE = 18;

@Injectable({ providedIn: 'root' })
export class PosStoreService {
  private readonly ventasApi = inject(VentasApiService);
  private readonly cajaApi = inject(CajaApiService);
  private readonly authService = inject(AuthService);

  private readonly ventasSignal = signal<Venta[]>([]);
  private readonly cajaSignal = signal<Caja | null>(null);
  private readonly resumenSignal = signal<VentaResumenDiarioResponse | null>(null);
  private readonly initialLoadingSignal = signal(false);
  private readonly actionLoadingSignal = signal(false);
  private readonly errorSignal = signal<ApiError | null>(null);

  readonly ventas = computed(() => this.ventasSignal());
  readonly caja = computed(() => this.cajaSignal());
  readonly resumenDiario = computed(() => this.resumenSignal());
  readonly initialLoading = computed(() => this.initialLoadingSignal());
  readonly actionLoading = computed(() => this.actionLoadingSignal());
  readonly error = computed(() => this.errorSignal());
  readonly hasCajaAbierta = computed(() => this.cajaSignal()?.estado === 'ABIERTA');
  readonly ventasRegistradas = computed(() => this.ventasSignal().filter((item) => item.estado === 'REGISTRADA'));
  readonly ventasAnuladas = computed(() => this.ventasSignal().filter((item) => item.estado === 'ANULADA').length);
  readonly totalVendido = computed(() =>
    this.ventasRegistradas().reduce((sum, item) => sum + Number(item.total), 0)
  );

  readonly ventasPorMetodo = computed<Record<MetodoPago, number>>(() => {
    const base: Record<MetodoPago, number> = {
      EFECTIVO: 0,
      YAPE: 0,
      PLIN: 0,
      TARJETA: 0
    };
    for (const venta of this.ventasRegistradas()) {
      base[venta.metodoPago] += Number(venta.total);
    }
    return base;
  });

  clearError(): void {
    this.errorSignal.set(null);
  }

  async loadInitialData(): Promise<void> {
    const user = this.authService.user();
    if (!user) {
      return;
    }

    this.initialLoadingSignal.set(true);
    this.errorSignal.set(null);

    try {
      const ventasPromise = this.ventasApi.listVentas();
      const resumenPromise = this.ventasApi.getResumenDiario(user.idLocal);
      const [ventas, resumen] = await Promise.all([ventasPromise, resumenPromise]);
      this.ventasSignal.set(ventas);
      this.resumenSignal.set(resumen);
      await this.refreshCaja();
    } catch (error) {
      this.errorSignal.set(mapApiError(error));
    } finally {
      this.initialLoadingSignal.set(false);
    }
  }

  async refreshCaja(): Promise<void> {
    const user = this.authService.user();
    if (!user) {
      return;
    }

    try {
      const cajaAbierta = await this.cajaApi.getCajaAbierta(user.idLocal);
      this.cajaSignal.set(cajaAbierta);
    } catch {
      this.cajaSignal.set(null);
    }
  }

  async registrarVenta(input: {
    tipoComprobante: TipoComprobante;
    serieComprobante?: string;
    numeroComprobante?: string;
    metodoPago: MetodoPago;
    items: DetalleVentaUi[];
  }): Promise<{ success: boolean; error?: ApiError }> {
    const user = this.authService.user();
    const caja = this.cajaSignal();
    if (!user) {
      const error: ApiError = { type: 'UNAUTHORIZED', message: 'Debes iniciar sesión para registrar ventas.' };
      this.errorSignal.set(error);
      return { success: false, error };
    }
    if (!caja || caja.estado !== 'ABIERTA') {
      const error: ApiError = {
        type: 'CONFLICT',
        message: 'No hay caja abierta. Abre caja antes de registrar una venta.',
        statusCode: 409
      };
      this.errorSignal.set(error);
      return { success: false, error };
    }
    if (input.items.length === 0) {
      const error: ApiError = {
        type: 'VALIDATION',
        message: 'Debes seleccionar al menos un producto.',
        statusCode: 422
      };
      this.errorSignal.set(error);
      return { success: false, error };
    }
    if ((input.serieComprobante && !input.numeroComprobante) || (!input.serieComprobante && input.numeroComprobante)) {
      const error: ApiError = {
        type: 'VALIDATION',
        message: 'Serie y número deben enviarse juntos.',
        statusCode: 422
      };
      this.errorSignal.set(error);
      return { success: false, error };
    }

    const subtotal = input.items.reduce((sum, item) => sum + item.subtotal, 0);
    const igv = Number((subtotal * (IGV_PERCENTAGE / 100)).toFixed(2));
    const total = Number((subtotal + igv).toFixed(2));

    const payload: RegistrarVentaRequest = {
      idLocal: user.idLocal,
      idCaja: caja.idCaja,
      tipoComprobante: input.tipoComprobante,
      serieComprobante: input.serieComprobante || undefined,
      numeroComprobante: input.numeroComprobante || undefined,
      metodoPago: input.metodoPago,
      subtotal: Number(subtotal.toFixed(2)),
      porcentajeIgv: IGV_PERCENTAGE,
      igv,
      total
    };

    this.actionLoadingSignal.set(true);
    this.errorSignal.set(null);

    try {
      const venta = await this.ventasApi.registrarVenta(payload);
      this.ventasSignal.set([venta, ...this.ventasSignal()]);
      await this.reloadResumen();
      return { success: true };
    } catch (error) {
      const mapped = mapApiError(error);
      this.errorSignal.set(mapped);
      return { success: false, error: mapped };
    } finally {
      this.actionLoadingSignal.set(false);
    }
  }

  async anularVenta(idVenta: number, motivo: string): Promise<{ success: boolean; error?: ApiError }> {
    if (!motivo.trim()) {
      const error: ApiError = {
        type: 'VALIDATION',
        message: 'El motivo de anulación es obligatorio.',
        statusCode: 422
      };
      this.errorSignal.set(error);
      return { success: false, error };
    }

    this.actionLoadingSignal.set(true);
    this.errorSignal.set(null);

    try {
      await this.ventasApi.anularVenta(idVenta, { motivo: motivo.trim() });
      this.ventasSignal.set(
        this.ventasSignal().map((item) =>
          item.idVenta === idVenta
            ? { ...item, estado: 'ANULADA', motivoAnulacion: motivo.trim(), fechaAnulacion: new Date().toISOString() }
            : item
        )
      );
      await this.reloadResumen();
      return { success: true };
    } catch (error) {
      const mapped = mapApiError(error);
      this.errorSignal.set(mapped);
      return { success: false, error: mapped };
    } finally {
      this.actionLoadingSignal.set(false);
    }
  }

  async abrirCaja(montoInicial: number): Promise<{ success: boolean; error?: ApiError }> {
    const user = this.authService.user();
    if (!user) {
      const error: ApiError = { type: 'UNAUTHORIZED', message: 'Debes iniciar sesión para abrir caja.' };
      this.errorSignal.set(error);
      return { success: false, error };
    }
    if (montoInicial < 0) {
      const error: ApiError = {
        type: 'VALIDATION',
        message: 'El monto inicial no puede ser negativo.',
        statusCode: 422
      };
      this.errorSignal.set(error);
      return { success: false, error };
    }

    this.actionLoadingSignal.set(true);
    this.errorSignal.set(null);
    try {
      const caja = await this.cajaApi.abrirCaja({
        idLocal: user.idLocal,
        montoInicial
      });
      this.cajaSignal.set(caja);
      return { success: true };
    } catch (error) {
      const mapped = mapApiError(error);
      this.errorSignal.set(mapped);
      return { success: false, error: mapped };
    } finally {
      this.actionLoadingSignal.set(false);
    }
  }

  async cerrarCaja(montoFinal: number, tipoCierre: TipoCierre): Promise<{ success: boolean; error?: ApiError }> {
    const caja = this.cajaSignal();
    if (!caja) {
      const error: ApiError = {
        type: 'CONFLICT',
        message: 'No existe caja abierta para cerrar.',
        statusCode: 409
      };
      this.errorSignal.set(error);
      return { success: false, error };
    }
    if (montoFinal < caja.montoInicial) {
      const error: ApiError = {
        type: 'VALIDATION',
        message: `El monto final no puede ser menor al inicial (S/ ${caja.montoInicial.toFixed(2)}).`,
        statusCode: 422
      };
      this.errorSignal.set(error);
      return { success: false, error };
    }

    this.actionLoadingSignal.set(true);
    this.errorSignal.set(null);

    try {
      const updated = await this.cajaApi.cerrarCaja(caja.idCaja, { montoFinal, tipoCierre });
      this.cajaSignal.set(updated);
      return { success: true };
    } catch (error) {
      const mapped = mapApiError(error);
      this.errorSignal.set(mapped);
      return { success: false, error: mapped };
    } finally {
      this.actionLoadingSignal.set(false);
    }
  }

  async reloadResumen(): Promise<void> {
    const user = this.authService.user();
    if (!user) {
      return;
    }

    try {
      const resumen = await this.ventasApi.getResumenDiario(user.idLocal);
      this.resumenSignal.set(resumen);
    } catch {
      this.resumenSignal.set(null);
    }
  }
}
