import { CommonModule, CurrencyPipe } from '@angular/common';
import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { PosStoreService } from '../../../shared/pos/pos-store.service';
import { ErrorBannerComponent } from '../../../shared/ui/error-banner.component';
import { LoadingSkeletonComponent } from '../../../shared/ui/loading-skeleton.component';
import { MetricCardComponent } from '../../../shared/ui/metric-card.component';
import { StatusPillComponent } from '../../../shared/ui/status-pill.component';

@Component({
  selector: 'app-dashboard-page',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    CurrencyPipe,
    MetricCardComponent,
    ErrorBannerComponent,
    StatusPillComponent,
    LoadingSkeletonComponent
  ],
  templateUrl: './dashboard-page.component.html',
  styleUrl: './dashboard-page.component.css'
})
export class DashboardPageComponent {
  readonly posStore = inject(PosStoreService);

  get ventasRecientes() {
    return this.posStore
      .ventas()
      .slice()
      .sort((a, b) => new Date(b.fechaVenta).getTime() - new Date(a.fechaVenta).getTime())
      .slice(0, 5);
  }
}
