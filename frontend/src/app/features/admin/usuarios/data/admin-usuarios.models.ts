export interface ApiResponse<T> {
  message: string;
  data: T;
}

export interface AdminUser {
  idUsuario: number;
  nombres: string;
  apellidos: string | null;
  correo: string;
  estado: number;
  idRol: number;
  rol: string;
  creadoEn: string;
  actualizadoEn: string;
}

export interface AdminRole {
  idRol: number;
  nombre: string;
}

export interface AdminUserUpsertRequest {
  nombres: string;
  apellidos: string;
  correo: string;
  contrasena: string;
  idRol: number;
}
