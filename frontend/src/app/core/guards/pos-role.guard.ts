import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../auth/auth.service';

export const posRoleGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (!authService.isAuthenticated()) {
    return router.createUrlTree(['/login']);
  }

  if (authService.hasAnyRole(['ADMINISTRADOR', 'CAJERO'])) {
    return true;
  }

  return router.createUrlTree(['/']);
};
