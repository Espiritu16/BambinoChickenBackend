import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-confirm-dialog',
  standalone: true,
  imports: [CommonModule],
  template: `
    <section *ngIf="open" class="overlay">
      <article class="dialog">
        <h3>{{ title }}</h3>
        <p>{{ message }}</p>
        <div class="actions">
          <button class="ghost" type="button" (click)="cancel.emit()">{{ cancelText }}</button>
          <button class="confirm" type="button" (click)="confirm.emit()">{{ confirmText }}</button>
        </div>
      </article>
    </section>
  `,
  styles: [
    `
      .overlay {
        position: fixed;
        inset: 0;
        background: rgba(15, 23, 42, 0.46);
        display: grid;
        place-items: center;
        z-index: 200;
        padding: 16px;
      }

      .dialog {
        width: min(100%, 460px);
        background: white;
        border-radius: 16px;
        padding: 20px;
        border: 1px solid #e5e7eb;
        box-shadow: 0 16px 30px rgba(15, 23, 42, 0.18);
      }

      h3 {
        margin: 0;
        color: #daa520;
      }

      p {
        margin: 8px 0 0;
        color: #4b5563;
      }

      .actions {
        margin-top: 18px;
        display: flex;
        justify-content: flex-end;
        gap: 10px;
      }

      button {
        border: 0;
        border-radius: 10px;
        padding: 10px 12px;
        cursor: pointer;
        font-weight: 700;
      }

      .ghost {
        border: 1px solid #e5e7eb;
        background: #ffffff;
        color: #6b7280;
      }

      .confirm {
        background: #4caf50;
        color: white;
      }

      .confirm:hover {
        background: #43a047;
      }
    `
  ]
})
export class ConfirmDialogComponent {
  @Input() open = false;
  @Input() title = '';
  @Input() message = '';
  @Input() confirmText = 'Confirmar';
  @Input() cancelText = 'Cancelar';
  @Output() cancel = new EventEmitter<void>();
  @Output() confirm = new EventEmitter<void>();
}
