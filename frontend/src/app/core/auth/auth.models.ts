export type Rol = 'ADMINISTRADOR' | 'CAJERO' | 'MOZO';

export interface AuthUser {
  id: number;
  correo: string;
  nombre: string;
  rol: Rol;
  idLocal: number;
  localNombre: string;
  accessToken: string;
  refreshToken: string;
  tokenType: string;
  accessTokenExpiresAt: string;
}

export interface LoginRequest {
  correo: string;
  contrasena: string;
}

export interface LoginApiResponse {
  message: string;
  data: {
    idUsuario: number;
    nombre: string;
    correo: string;
    rol: Rol;
    idLocal: number;
    localNombre: string;
    accessToken: string;
    refreshToken: string;
    tokenType: string;
    accessTokenExpiresAt: string;
  };
}
