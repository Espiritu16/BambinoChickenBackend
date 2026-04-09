import { CommonModule, CurrencyPipe } from '@angular/common';
import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../../../core/auth/auth.service';
import { PosStoreService } from '../../../shared/pos/pos-store.service';
import { ErrorBannerComponent } from '../../../shared/ui/error-banner.component';
import { MetricCardComponent } from '../../../shared/ui/metric-card.component';

@Component({
  selector: 'app-resumen-diario-page',
  standalone: true,
  imports: [CommonModule, FormsModule, CurrencyPipe, ErrorBannerComponent, MetricCardComponent],
  templateUrl: './resumen-diario-page.component.html',
  styleUrl: './resumen-diario-page.component.css'
})
export class ResumenDiarioPageComponent {
  readonly authService = inject(AuthService);
  readonly posStore = inject(PosStoreService);
  readonly modo = signal<'local' | 'general'>('local');
  readonly fechaFiltro = signal('');

  readonly metodos = computed(() => {
    const metodo = this.posStore.ventasPorMetodo();
    const total = this.posStore.totalVendido();
    return [
      { label: 'Efectivo', value: metodo.EFECTIVO, pct: total > 0 ? (metodo.EFECTIVO / total) * 100 : 0 },
      { label: 'Yape', value: metodo.YAPE, pct: total > 0 ? (metodo.YAPE / total) * 100 : 0 },
      { label: 'Plin', value: metodo.PLIN, pct: total > 0 ? (metodo.PLIN / total) * 100 : 0 },
      { label: 'Tarjeta', value: metodo.TARJETA, pct: total > 0 ? (metodo.TARJETA / total) * 100 : 0 }
    ];
  });
}
