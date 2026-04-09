import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { LoginPageComponent } from './features/auth/pages/login-page/login-page.component';
import { HomePageComponent } from './features/home/pages/home-page/home-page.component';

export const routes: Routes = [
  { path: 'login', component: LoginPageComponent },
  {
    path: '',
    component: HomePageComponent,
    canActivate: [authGuard]
  },
  { path: '**', redirectTo: '' }
];
