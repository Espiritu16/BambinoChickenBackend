import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { adminRoleGuard } from './core/guards/admin-role.guard';
import { LoginPageComponent } from './features/auth/pages/login-page/login-page.component';
import { HomePageComponent } from './features/home/pages/home-page/home-page.component';
import { AdminUsuariosPageComponent } from './features/admin/usuarios/pages/admin-usuarios-page/admin-usuarios-page.component';

export const routes: Routes = [
  { path: 'login', component: LoginPageComponent },
  {
    path: 'admin/usuarios',
    component: AdminUsuariosPageComponent,
    canActivate: [authGuard, adminRoleGuard]
  },
  {
    path: '',
    component: HomePageComponent,
    canActivate: [authGuard]
  },
  { path: '**', redirectTo: '' }
];
