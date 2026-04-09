import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ApiError } from '../pos/pos.models';

@Component({
  selector: 'app-error-banner',
  standalone: true,
  imports: [CommonModule],
  template: `
    <section *ngIf="error" class="error-banner">
      <div>
        <p class="title">Error de operación</p>
        <p class="message">{{ error.message }}</p>
      </div>
      <button type="button" (click)="dismiss.emit()">Cerrar</button>
    </section>
  `,
  styles: [
    `
      .error-banner {
        border: 1px solid #ef4444;
        border-radius: 14px;
        padding: 12px 14px;
        background: #fff1f2;
        color: #9f1239;
        display: flex;
        justify-content: space-between;
        align-items: center;
        gap: 12px;
      }

      .title {
        margin: 0;
        font-size: 13px;
        font-weight: 800;
        text-transform: uppercase;
      }

      .message {
        margin: 4px 0 0;
        font-size: 14px;
      }

      button {
        border: 0;
        border-radius: 10px;
        padding: 8px 12px;
        background: #9f1239;
        color: white;
        cursor: pointer;
        font-weight: 600;
      }
    `
  ]
})
export class ErrorBannerComponent {
  @Input() error: ApiError | null = null;
  @Output() dismiss = new EventEmitter<void>();
}
