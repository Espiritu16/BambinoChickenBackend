import { HttpErrorResponse } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { AuthService } from '../../../../core/auth/auth.service';
import { CajaApiService } from './caja-api.service';
import { PosStoreService } from './pos-store.service';
import { VentasApiService } from './ventas-api.service';

describe('PosStoreService', () => {
  let service: PosStoreService;
  let ventasApi: jasmine.SpyObj<VentasApiService>;
  let cajaApi: jasmine.SpyObj<CajaApiService>;

  const authStub = {
    user: () => ({
      id: 1,
      correo: 'cajero@bambino.com',
      nombre: 'Cajero',
      rol: 'CAJERO',
      idLocal: 1,
      localNombre: 'Local Principal',
      accessToken: 'mock_token'
    })
  };

  beforeEach(() => {
    ventasApi = jasmine.createSpyObj<VentasApiService>('VentasApiService', [
      'listVentas',
      'registrarVenta',
      'anularVenta',
      'getResumenDiario'
    ]);
    cajaApi = jasmine.createSpyObj<CajaApiService>('CajaApiService', [
      'listCajas',
      'getCajaAbierta',
      'abrirCaja',
      'cerrarCaja'
    ]);

    ventasApi.listVentas.and.resolveTo([]);
    ventasApi.getResumenDiario.and.resolveTo({
      fecha: '2026-04-09',
      idLocal: 1,
      cantidadVentas: 0,
      montoTotal: 0
    });
    cajaApi.getCajaAbierta.and.rejectWith(new Error('No abierta'));

    TestBed.configureTestingModule({
      providers: [
        PosStoreService,
        { provide: VentasApiService, useValue: ventasApi },
        { provide: CajaApiService, useValue: cajaApi },
        { provide: AuthService, useValue: authStub }
      ]
    });

    service = TestBed.inject(PosStoreService);
  });

  it('debe bloquear registrar venta sin caja abierta', async () => {
    const result = await service.registrarVenta({
      tipoComprobante: 'BOLETA',
      metodoPago: 'EFECTIVO',
      items: [{ idProducto: 1, nombre: '1/4 Pollo', cantidad: 1, precioUnitario: 18, subtotal: 18 }]
    });

    expect(result.success).toBeFalse();
    expect(result.error?.type).toBe('CONFLICT');
    expect(ventasApi.registrarVenta).not.toHaveBeenCalled();
  });

  it('debe registrar venta válida cuando hay caja abierta', async () => {
    cajaApi.abrirCaja.and.resolveTo({
      idCaja: 10,
      idLocal: 1,
      idUsuario: 1,
      montoInicial: 200,
      montoFinal: null,
      estado: 'ABIERTA',
      tipoCierre: null,
      fechaApertura: '2026-04-09T08:00:00',
      fechaCierre: null
    });
    ventasApi.registrarVenta.and.resolveTo({
      idVenta: 500,
      idLocal: 1,
      idCaja: 10,
      idUsuario: 1,
      tipoComprobante: 'BOLETA',
      serieComprobante: 'B001',
      numeroComprobante: '00000015',
      metodoPago: 'EFECTIVO',
      subtotal: 10,
      porcentajeIgv: 18,
      igv: 1.8,
      total: 11.8,
      estado: 'REGISTRADA',
      motivoAnulacion: null,
      fechaVenta: '2026-04-09T09:10:00',
      fechaAnulacion: null
    });

    await service.abrirCaja(200);
    const result = await service.registrarVenta({
      tipoComprobante: 'BOLETA',
      metodoPago: 'EFECTIVO',
      items: [{ idProducto: 1, nombre: '1/4 Pollo', cantidad: 1, precioUnitario: 10, subtotal: 10 }]
    });

    expect(result.success).toBeTrue();
    expect(ventasApi.registrarVenta).toHaveBeenCalled();
    expect(service.ventas().length).toBe(1);
  });

  it('debe rechazar anulación sin motivo', async () => {
    const result = await service.anularVenta(10, ' ');
    expect(result.success).toBeFalse();
    expect(result.error?.type).toBe('VALIDATION');
    expect(ventasApi.anularVenta).not.toHaveBeenCalled();
  });

  it('debe validar cierre con monto final menor al inicial', async () => {
    cajaApi.abrirCaja.and.resolveTo({
      idCaja: 7,
      idLocal: 1,
      idUsuario: 1,
      montoInicial: 150,
      montoFinal: null,
      estado: 'ABIERTA',
      tipoCierre: null,
      fechaApertura: '2026-04-09T08:00:00',
      fechaCierre: null
    });
    await service.abrirCaja(150);
    const result = await service.cerrarCaja(120, 'CLASICO');
    expect(result.success).toBeFalse();
    expect(result.error?.type).toBe('VALIDATION');
    expect(cajaApi.cerrarCaja).not.toHaveBeenCalled();
  });

  it('debe mapear 422 como error de validación', async () => {
    cajaApi.abrirCaja.and.resolveTo({
      idCaja: 11,
      idLocal: 1,
      idUsuario: 1,
      montoInicial: 200,
      montoFinal: null,
      estado: 'ABIERTA',
      tipoCierre: null,
      fechaApertura: '2026-04-09T08:00:00',
      fechaCierre: null
    });
    ventasApi.registrarVenta.and.rejectWith(
      new HttpErrorResponse({
        status: 422,
        error: { message: 'Los datos enviados no son válidos' }
      })
    );

    await service.abrirCaja(200);
    const result = await service.registrarVenta({
      tipoComprobante: 'BOLETA',
      metodoPago: 'EFECTIVO',
      items: [{ idProducto: 1, nombre: '1/4 Pollo', cantidad: 1, precioUnitario: 10, subtotal: 10 }]
    });

    expect(result.success).toBeFalse();
    expect(result.error?.type).toBe('VALIDATION');
  });
});
