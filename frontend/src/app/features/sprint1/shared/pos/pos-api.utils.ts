import { HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { AuthService } from '../../../../core/auth/auth.service';
import { ApiError } from './pos.models';

const DEFAULT_BASE_URL = 'http://localhost:8080/api/v1';

export function apiUrl(path: string): string {
  return `${DEFAULT_BASE_URL}${path}`;
}

export function buildAuthHeaders(authService: AuthService): HttpHeaders {
  const token = authService.user()?.accessToken;
  return token ? new HttpHeaders({ Authorization: `Bearer ${token}` }) : new HttpHeaders();
}

export function mapApiError(error: unknown): ApiError {
  if (!(error instanceof HttpErrorResponse)) {
    return { type: 'UNKNOWN', message: 'Error inesperado en la operación.' };
  }

  const backendMessage =
    typeof error.error?.message === 'string'
      ? error.error.message
      : typeof error.error?.data?.message === 'string'
        ? error.error.data.message
        : typeof error.message === 'string'
          ? error.message
          : 'Error no identificado';

  if (error.status === 0) {
    return { type: 'NETWORK', message: 'Sin conexión con backend. Verifica que el servidor esté activo.', statusCode: 0 };
  }
  if (error.status === 401) {
    return { type: 'UNAUTHORIZED', message: backendMessage || 'Sesión expirada. Inicia sesión nuevamente.', statusCode: 401 };
  }
  if (error.status === 403) {
    return { type: 'FORBIDDEN', message: backendMessage || 'No tienes permisos para esta acción.', statusCode: 403 };
  }
  if (error.status === 409) {
    return { type: 'CONFLICT', message: backendMessage || 'Conflicto de negocio. Revisa el estado e intenta otra vez.', statusCode: 409 };
  }
  if (error.status === 422 || error.status === 400) {
    return { type: 'VALIDATION', message: backendMessage || 'Datos inválidos para la operación.', statusCode: error.status };
  }
  if (error.status >= 500) {
    return { type: 'SERVER', message: backendMessage || 'Error interno del servidor.', statusCode: error.status };
  }

  return { type: 'UNKNOWN', message: backendMessage, statusCode: error.status };
}
