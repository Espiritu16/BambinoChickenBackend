import { Injectable, computed, signal } from '@angular/core';
import { AuthUser, LoginRequest, Rol } from './auth.models';

interface MockAccount {
  id: number;
  correo: string;
  contrasena: string;
  nombre: string;
  rol: Rol;
  idLocal: number;
  localNombre: string;
}

const MOCK_ACCOUNTS: MockAccount[] = [
  {
    id: 1,
    correo: 'admin@bambino.com',
    contrasena: '123456',
    nombre: 'Administrador',
    rol: 'ADMINISTRADOR',
    idLocal: 1,
    localNombre: 'Local Principal'
  },
  {
    id: 2,
    correo: 'cajero@bambino.com',
    contrasena: '123456',
    nombre: 'Cajero',
    rol: 'CAJERO',
    idLocal: 1,
    localNombre: 'Local Principal'
  },
  {
    id: 3,
    correo: 'mozo@bambino.com',
    contrasena: '123456',
    nombre: 'Mozo',
    rol: 'MOZO',
    idLocal: 1,
    localNombre: 'Local Principal'
  }
];

const SESSION_KEY = 'bambino_auth';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly userSignal = signal<AuthUser | null>(this.restoreSession());

  readonly user = computed(() => this.userSignal());
  readonly isAuthenticated = computed(() => !!this.userSignal());

  async login(request: LoginRequest): Promise<AuthUser> {
    await new Promise((resolve) => setTimeout(resolve, 400));

    const correo = request.correo.trim().toLowerCase();
    const account = MOCK_ACCOUNTS.find(
      (item) => item.correo.toLowerCase() === correo && item.contrasena === request.contrasena
    );

    if (!account) {
      throw new Error('Credenciales invalidas');
    }

    const session: AuthUser = {
      id: account.id,
      correo: account.correo,
      nombre: account.nombre,
      rol: account.rol,
      idLocal: account.idLocal,
      localNombre: account.localNombre,
      accessToken: `mock_${Math.random().toString(36).slice(2)}`
    };

    this.userSignal.set(session);
    sessionStorage.setItem(SESSION_KEY, JSON.stringify(session));
    return session;
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
}
