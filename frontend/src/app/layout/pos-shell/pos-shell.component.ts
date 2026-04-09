import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthService } from '../../core/auth/auth.service';
import { PosStoreService } from '../../features/sprint1/shared/pos/pos-store.service';

@Component({
  selector: 'app-pos-shell',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive, RouterOutlet],
  templateUrl: './pos-shell.component.html',
  styleUrl: './pos-shell.component.css'
})
export class PosShellComponent implements OnInit {
  private readonly router = inject(Router);
  readonly authService = inject(AuthService);
  readonly posStore = inject(PosStoreService);
  sidebarOpen = false;

  readonly navItems = [
    { to: '/pos', label: 'Dashboard', icon: 'DB', exact: true },
    { to: '/pos/ventas/nueva', label: 'Nueva Venta', icon: 'NV' },
    { to: '/pos/ventas', label: 'Ventas', icon: 'VT' },
    { to: '/pos/resumen', label: 'Resumen Diario', icon: 'RD' },
    { to: '/pos/caja', label: 'Caja', icon: 'CJ' }
  ];

  async ngOnInit(): Promise<void> {
    await this.posStore.loadInitialData();
  }

  logout(): void {
    this.authService.logout();
    void this.router.navigateByUrl('/login');
  }

  closeSidebar(): void {
    this.sidebarOpen = false;
  }
}
