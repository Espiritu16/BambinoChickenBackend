import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { AuthService } from '../../../../core/auth/auth.service';
import { AdminRole, AdminUser, AdminUserUpsertRequest, ApiResponse } from './admin-usuarios.models';

const BASE_URL = 'http://localhost:8080/api/v1/admin';

@Injectable({ providedIn: 'root' })
export class AdminUsuariosApiService {
  private readonly http = inject(HttpClient);
  private readonly authService = inject(AuthService);

  async listUsers(query: string): Promise<AdminUser[]> {
    const q = query.trim();
    const response = await firstValueFrom(
      this.http.get<ApiResponse<AdminUser[]>>(`${BASE_URL}/usuarios`, {
        params: q ? { q } : {},
        headers: this.authHeaders()
      })
    );
    return response.data;
  }

  async listRoles(): Promise<AdminRole[]> {
    const response = await firstValueFrom(
      this.http.get<ApiResponse<AdminRole[]>>(`${BASE_URL}/roles`, {
        headers: this.authHeaders()
      })
    );
    return response.data;
  }

  async createUser(payload: AdminUserUpsertRequest): Promise<AdminUser> {
    const response = await firstValueFrom(
      this.http.post<ApiResponse<AdminUser>>(`${BASE_URL}/usuarios`, payload, {
        headers: this.authHeaders()
      })
    );
    return response.data;
  }

  async updateUser(idUsuario: number, payload: AdminUserUpsertRequest): Promise<AdminUser> {
    const response = await firstValueFrom(
      this.http.put<ApiResponse<AdminUser>>(`${BASE_URL}/usuarios/${idUsuario}`, payload, {
        headers: this.authHeaders()
      })
    );
    return response.data;
  }

  async inactivateUser(idUsuario: number): Promise<AdminUser> {
    const response = await firstValueFrom(
      this.http.patch<ApiResponse<AdminUser>>(`${BASE_URL}/usuarios/${idUsuario}/inactivar`, {}, {
        headers: this.authHeaders()
      })
    );
    return response.data;
  }

  private authHeaders(): HttpHeaders {
    const token = this.authService.user()?.accessToken;
    return token ? new HttpHeaders({ Authorization: `Bearer ${token}` }) : new HttpHeaders();
  }
}
