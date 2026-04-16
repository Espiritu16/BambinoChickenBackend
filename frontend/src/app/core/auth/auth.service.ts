import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { AuthUser, LoginApiResponse, LoginRequest, Rol } from './auth.models';

const SESSION_KEY = 'bambino_auth';
const AUTH_BASE_URL = 'http://localhost:8080/api/v1/auth';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly userSignal = signal<AuthUser | null>(this.restoreSession());

  readonly user = computed(() => this.userSignal());
  readonly isAuthenticated = computed(() => !!this.userSignal());

  async login(request: LoginRequest): Promise<AuthUser> {
    try {
      const response = await firstValueFrom(
        this.http.post<LoginApiResponse>(`${AUTH_BASE_URL}/login`, request)
      );

      const session: AuthUser = {
        id: response.data.idUsuario,
        correo: response.data.correo,
        nombre: response.data.nombre,
        rol: response.data.rol,
        idLocal: response.data.idLocal,
        localNombre: response.data.localNombre,
        accessToken: response.data.accessToken,
        refreshToken: response.data.refreshToken,
        tokenType: response.data.tokenType,
        accessTokenExpiresAt: response.data.accessTokenExpiresAt
      };

      this.userSignal.set(session);
      sessionStorage.setItem(SESSION_KEY, JSON.stringify(session));
      return session;
    } catch (error) {
      throw new Error(this.mapLoginError(error));
    }
  }

  logout(): void {
    this.userSignal.set(null);
    sessionStorage.removeItem(SESSION_KEY);
  }

  hasAnyRole(roles: Rol[]): boolean {
    const user = this.userSignal();
    return !!user && roles.includes(user.rol);
  }

  private restoreSession(): AuthUser | null {
    const raw = sessionStorage.getItem(SESSION_KEY);
    if (!raw) {
      return null;
    }

    try {
      return JSON.parse(raw) as AuthUser;
    } catch {
      sessionStorage.removeItem(SESSION_KEY);
      return null;
    }
  }

  private mapLoginError(error: unknown): string {
    if (error instanceof HttpErrorResponse) {
      const backendMessage = typeof error.error?.message === 'string'
        ? error.error.message
        : 'No se pudo iniciar sesion';

      if (error.status === 0) {
        return 'No hay conexión con backend';
      }
      return backendMessage;
    }

    if (error instanceof Error) {
      return error.message;
    }

    return 'No se pudo iniciar sesion';
  }
}
