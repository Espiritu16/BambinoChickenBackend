import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import {
  AnularVentaRequest,
  ApiResponse,
  RegistrarVentaRequest,
  Venta,
  VentaResumenDiarioResponse
} from './pos.models';
import { apiUrl, buildAuthHeaders } from './pos-api.utils';
import { AuthService } from '../../../../core/auth/auth.service';

@Injectable({ providedIn: 'root' })
export class VentasApiService {
  private readonly http = inject(HttpClient);
  private readonly authService = inject(AuthService);

  listVentas(): Promise<Venta[]> {
    return firstValueFrom(
      this.http.get<ApiResponse<Venta[]>>(apiUrl('/ventas'), {
        headers: buildAuthHeaders(this.authService)
      })
    ).then((res) => res.data);
  }

  registrarVenta(payload: RegistrarVentaRequest): Promise<Venta> {
    return firstValueFrom(
      this.http.post<ApiResponse<Venta>>(apiUrl('/ventas'), payload, {
        headers: buildAuthHeaders(this.authService)
      })
    ).then((res) => res.data);
  }

  anularVenta(idVenta: number, payload: AnularVentaRequest): Promise<void> {
    return firstValueFrom(
      this.http.post<ApiResponse<unknown>>(apiUrl(`/ventas/${idVenta}/anulacion`), payload, {
        headers: buildAuthHeaders(this.authService)
      })
    ).then(() => undefined);
  }

  getResumenDiario(idLocal?: number): Promise<VentaResumenDiarioResponse> {
    const query = idLocal ? `?idLocal=${idLocal}` : '';
    return firstValueFrom(
      this.http.get<ApiResponse<VentaResumenDiarioResponse>>(apiUrl(`/ventas/resumen-diario${query}`), {
        headers: buildAuthHeaders(this.authService)
      })
    ).then((res) => res.data);
  }
}
