import { CommonModule, CurrencyPipe, DatePipe } from '@angular/common';
import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { PosStoreService } from '../../../shared/pos/pos-store.service';
import { ErrorBannerComponent } from '../../../shared/ui/error-banner.component';
import { StatusPillComponent } from '../../../shared/ui/status-pill.component';
import { ConfirmDialogComponent } from '../../../shared/ui/confirm-dialog.component';
import { LoadingSkeletonComponent } from '../../../shared/ui/loading-skeleton.component';

@Component({
  selector: 'app-listado-ventas-page',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    CurrencyPipe,
    DatePipe,
    ErrorBannerComponent,
    StatusPillComponent,
    ConfirmDialogComponent,
    LoadingSkeletonComponent
  ],
  templateUrl: './listado-ventas-page.component.html',
  styleUrl: './listado-ventas-page.component.css'
})
export class ListadoVentasPageComponent {
  readonly posStore = inject(PosStoreService);
  readonly search = signal('');
  readonly filtroEstado = signal<'TODOS' | 'REGISTRADA' | 'ANULADA'>('TODOS');
  readonly selectedVentaId = signal<number | null>(null);
  readonly motivoAnulacion = signal('');

  readonly ventasFiltradas = computed(() => {
    const term = this.search().trim().toLowerCase();
    if (!term) {
      return this.posStore
        .ventas()
        .filter((item) => this.filtroEstado() === 'TODOS' || item.estado === this.filtroEstado());
    }

    return this.posStore
      .ventas()
      .filter((item) => this.filtroEstado() === 'TODOS' || item.estado === this.filtroEstado())
      .filter((item) =>
        [item.serieComprobante, item.numeroComprobante, item.metodoPago, item.tipoComprobante]
          .filter((value): value is string => Boolean(value))
          .some((value) => value.toLowerCase().includes(term))
      );
  });

  openAnulacion(idVenta: number): void {
    this.selectedVentaId.set(idVenta);
    this.motivoAnulacion.set('');
  }

  closeAnulacion(): void {
    this.selectedVentaId.set(null);
    this.motivoAnulacion.set('');
  }

  async confirmAnulacion(): Promise<void> {
    const idVenta = this.selectedVentaId();
    if (!idVenta) {
      return;
    }

    const result = await this.posStore.anularVenta(idVenta, this.motivoAnulacion());
    if (result.success) {
      this.closeAnulacion();
    }
  }
}
