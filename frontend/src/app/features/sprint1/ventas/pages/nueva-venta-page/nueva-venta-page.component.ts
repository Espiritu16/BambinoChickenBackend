import { CommonModule, CurrencyPipe } from '@angular/common';
import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { PosStoreService, PRODUCTOS_POS } from '../../../shared/pos/pos-store.service';
import { MetodoPago, TipoComprobante } from '../../../shared/pos/pos.models';
import { ErrorBannerComponent } from '../../../shared/ui/error-banner.component';

@Component({
  selector: 'app-nueva-venta-page',
  standalone: true,
  imports: [CommonModule, FormsModule, CurrencyPipe, ErrorBannerComponent],
  templateUrl: './nueva-venta-page.component.html',
  styleUrl: './nueva-venta-page.component.css'
})
export class NuevaVentaPageComponent {
  readonly posStore = inject(PosStoreService);
  readonly productos = PRODUCTOS_POS;
  readonly metodosPago: MetodoPago[] = ['EFECTIVO', 'YAPE', 'PLIN', 'TARJETA'];
  readonly tiposComprobante: TipoComprobante[] = ['BOLETA', 'FACTURA'];

  readonly selectedProductId = signal<number>(this.productos[0]?.idProducto ?? 1);
  readonly quantity = signal(1);
  readonly filter = signal('');
  readonly activeCategory = signal(this.productos[0]?.categoria ?? 'Pollos');
  readonly carrito = signal<{ idProducto: number; nombre: string; cantidad: number; precioUnitario: number; subtotal: number }[]>([]);
  readonly tipoComprobante = signal<TipoComprobante>('BOLETA');
  readonly metodoPago = signal<MetodoPago>('EFECTIVO');
  readonly serieComprobante = signal('');
  readonly numeroComprobante = signal('');
  readonly successMessage = signal('');

  readonly subtotal = computed(() => this.carrito().reduce((sum, item) => sum + item.subtotal, 0));
  readonly igv = computed(() => Number((this.subtotal() * 0.18).toFixed(2)));
  readonly total = computed(() => Number((this.subtotal() + this.igv()).toFixed(2)));
  readonly productosFiltrados = computed(() => {
    const filter = this.filter().trim().toLowerCase();
    if (!filter) {
      return this.productos.filter((item) => item.categoria === this.activeCategory());
    }
    return this.productos.filter((item) => item.nombre.toLowerCase().includes(filter));
  });
  readonly categorias = computed(() => [...new Set(this.productos.map((item) => item.categoria))]);

  addItem(): void {
    const product = this.productos.find((item) => item.idProducto === this.selectedProductId());
    if (!product || this.quantity() <= 0) {
      return;
    }

    const existing = this.carrito().find((item) => item.idProducto === product.idProducto);
    if (existing) {
      const updated = this.carrito().map((item) =>
        item.idProducto === product.idProducto
          ? {
              ...item,
              cantidad: item.cantidad + this.quantity(),
              subtotal: Number(((item.cantidad + this.quantity()) * item.precioUnitario).toFixed(2))
            }
          : item
      );
      this.carrito.set(updated);
    } else {
      this.carrito.set([
        ...this.carrito(),
        {
          idProducto: product.idProducto,
          nombre: product.nombre,
          cantidad: this.quantity(),
          precioUnitario: product.precio,
          subtotal: Number((product.precio * this.quantity()).toFixed(2))
        }
      ]);
    }

    this.quantity.set(1);
    this.successMessage.set('');
    this.posStore.clearError();
  }

  removeItem(idProducto: number): void {
    this.carrito.set(this.carrito().filter((item) => item.idProducto !== idProducto));
  }

  updateQty(idProducto: number, delta: number): void {
    this.carrito.set(
      this.carrito()
        .map((item) =>
          item.idProducto === idProducto
            ? {
                ...item,
                cantidad: item.cantidad + delta,
                subtotal: Number(((item.cantidad + delta) * item.precioUnitario).toFixed(2))
              }
            : item
        )
        .filter((item) => item.cantidad > 0)
    );
  }

  getCartItem(productId: number) {
    return this.carrito().find((item) => item.idProducto === productId);
  }

  async submitVenta(): Promise<void> {
    const result = await this.posStore.registrarVenta({
      tipoComprobante: this.tipoComprobante(),
      serieComprobante: this.serieComprobante().trim() || undefined,
      numeroComprobante: this.numeroComprobante().trim() || undefined,
      metodoPago: this.metodoPago(),
      items: this.carrito()
    });

    if (result.success) {
      this.successMessage.set('Venta registrada correctamente.');
      this.carrito.set([]);
      this.serieComprobante.set('');
      this.numeroComprobante.set('');
    } else {
      this.successMessage.set('');
    }
  }
}
