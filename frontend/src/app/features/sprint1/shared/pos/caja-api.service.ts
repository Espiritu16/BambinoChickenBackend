import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { AbrirCajaRequest, ApiResponse, Caja, CerrarCajaRequest } from './pos.models';
import { apiUrl, buildAuthHeaders } from './pos-api.utils';
import { AuthService } from '../../../../core/auth/auth.service';

@Injectable({ providedIn: 'root' })
export class CajaApiService {
  private readonly http = inject(HttpClient);
  private readonly authService = inject(AuthService);

  listCajas(): Promise<Caja[]> {
    return firstValueFrom(
      this.http.get<ApiResponse<Caja[]>>(apiUrl('/caja'), {
        headers: buildAuthHeaders(this.authService)
      })
    ).then((res) => res.data);
  }

  getCajaAbierta(idLocal: number): Promise<Caja> {
    return firstValueFrom(
      this.http.get<ApiResponse<Caja>>(apiUrl(`/caja/abierta?idLocal=${idLocal}`), {
        headers: buildAuthHeaders(this.authService)
      })
    ).then((res) => res.data);
  }

  abrirCaja(payload: AbrirCajaRequest): Promise<Caja> {
    return firstValueFrom(
      this.http.post<ApiResponse<Caja>>(apiUrl('/caja/aperturas'), payload, {
        headers: buildAuthHeaders(this.authService)
      })
    ).then((res) => res.data);
  }

  cerrarCaja(idCaja: number, payload: CerrarCajaRequest): Promise<Caja> {
    return firstValueFrom(
      this.http.post<ApiResponse<Caja>>(apiUrl(`/caja/${idCaja}/cierres`), payload, {
        headers: buildAuthHeaders(this.authService)
      })
    ).then((res) => res.data);
  }
}
