import { CommonModule, DatePipe } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { PosStoreService } from '../../../shared/pos/pos-store.service';
import { TipoCierre } from '../../../shared/pos/pos.models';
import { ErrorBannerComponent } from '../../../shared/ui/error-banner.component';
import { MetricCardComponent } from '../../../shared/ui/metric-card.component';
import { StatusPillComponent } from '../../../shared/ui/status-pill.component';

@Component({
  selector: 'app-caja-page',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    DatePipe,
    ErrorBannerComponent,
    MetricCardComponent,
    StatusPillComponent
  ],
  templateUrl: './caja-page.component.html',
  styleUrl: './caja-page.component.css'
})
export class CajaPageComponent {
  readonly posStore = inject(PosStoreService);
  readonly montoInicial = signal(200);
  readonly montoFinal = signal(0);
  readonly tipoCierre = signal<TipoCierre>('CLASICO');
}
