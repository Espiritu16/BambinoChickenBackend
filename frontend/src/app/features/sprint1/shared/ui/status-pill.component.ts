import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-status-pill',
  standalone: true,
  imports: [CommonModule],
  template: `<span class="pill" [ngClass]="variant">{{ label }}</span>`,
  styles: [
    `
      .pill {
        display: inline-flex;
        align-items: center;
        border-radius: 999px;
        padding: 6px 10px;
        font-size: 12px;
        font-weight: 700;
        letter-spacing: 0.02em;
      }

      .success {
        background: #def7ec;
        color: #046c4e;
      }

      .danger {
        background: #fde8e8;
        color: #9b1c1c;
      }

      .warning {
        background: #fdf6b2;
        color: #8e4b10;
      }

      .neutral {
        background: #e8eef6;
        color: #31445f;
      }
    `
  ]
})
export class StatusPillComponent {
  @Input({ required: true }) label = '';
  @Input() variant: 'success' | 'danger' | 'warning' | 'neutral' = 'neutral';
}
