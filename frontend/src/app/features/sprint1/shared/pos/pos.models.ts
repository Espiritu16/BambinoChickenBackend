export type MetodoPago = 'EFECTIVO' | 'YAPE' | 'PLIN' | 'TARJETA';
export type TipoComprobante = 'BOLETA' | 'FACTURA';
export type EstadoVenta = 'REGISTRADA' | 'ANULADA';
export type EstadoCaja = 'ABIERTA' | 'CERRADA';
export type TipoCierre = 'CIEGO' | 'CLASICO';

export interface ApiResponse<T> {
  message: string;
  data: T;
}

export type ApiErrorType = 'VALIDATION' | 'CONFLICT' | 'SERVER' | 'NETWORK' | 'FORBIDDEN' | 'UNAUTHORIZED' | 'UNKNOWN';

export interface ApiError {
  type: ApiErrorType;
  message: string;
  statusCode?: number;
}

export interface Venta {
  idVenta: number;
  idLocal: number;
  idCaja: number;
  idUsuario: number;
  tipoComprobante: TipoComprobante;
  serieComprobante: string | null;
  numeroComprobante: string | null;
  metodoPago: MetodoPago;
  subtotal: number;
  porcentajeIgv: number;
  igv: number;
  total: number;
  estado: EstadoVenta;
  motivoAnulacion: string | null;
  fechaVenta: string;
  fechaAnulacion: string | null;
}

export interface RegistrarVentaRequest {
  idLocal: number;
  idCaja: number;
  tipoComprobante: TipoComprobante;
  serieComprobante?: string;
  numeroComprobante?: string;
  metodoPago: MetodoPago;
  subtotal: number;
  porcentajeIgv?: number;
  igv: number;
  total: number;
}

export interface AnularVentaRequest {
  motivo: string;
}

export interface VentaResumenDiarioResponse {
  fecha: string;
  idLocal?: number;
  cantidadVentas: number;
  montoTotal: number;
}

export interface Caja {
  idCaja: number;
  idLocal: number;
  idUsuario: number;
  montoInicial: number;
  montoFinal: number | null;
  estado: EstadoCaja;
  tipoCierre: TipoCierre | null;
  fechaApertura: string;
  fechaCierre: string | null;
}

export interface AbrirCajaRequest {
  idLocal: number;
  montoInicial: number;
}

export interface CerrarCajaRequest {
  montoFinal: number;
  tipoCierre: TipoCierre;
}
