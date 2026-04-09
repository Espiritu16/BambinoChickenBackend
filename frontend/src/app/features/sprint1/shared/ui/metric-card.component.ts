import { CommonModule, CurrencyPipe } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-metric-card',
  standalone: true,
  imports: [CommonModule, CurrencyPipe],
  template: `
    <article class="metric-card">
      <p class="label">{{ label }}</p>
      <p class="value" *ngIf="isCurrency; else regularValue">{{ value | currency: 'PEN' : 'symbol' : '1.2-2' }}</p>
      <ng-template #regularValue>
        <p class="value">{{ value }}</p>
      </ng-template>
    </article>
  `,
  styles: [
    `
      .metric-card {
        border: 1px solid #e6ecf2;
        border-radius: 16px;
        padding: 16px;
        background: linear-gradient(160deg, #ffffff 0%, #f9fcff 100%);
      }

      .label {
        margin: 0;
        color: #56657a;
        font-size: 13px;
        font-weight: 600;
      }

      .value {
        margin: 8px 0 0;
        color: #0f2239;
        font-size: 24px;
        font-weight: 800;
      }
    `
  ]
})
export class MetricCardComponent {
  @Input({ required: true }) label = '';
  @Input({ required: true }) value: string | number = '';
  @Input() isCurrency = false;
}
