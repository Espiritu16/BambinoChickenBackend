export type Rol = 'ADMINISTRADOR' | 'CAJERO' | 'MOZO';

export interface AuthUser {
  id: number;
  correo: string;
  nombre: string;
  rol: Rol;
  idLocal: number;
  localNombre: string;
  accessToken: string;
}

export interface LoginRequest {
  correo: string;
  contrasena: string;
}
