import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-loading-skeleton',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="skeleton-list">
      <div *ngFor="let _ of blocks" class="block"></div>
    </div>
  `,
  styles: [
    `
      .skeleton-list {
        display: grid;
        gap: 10px;
      }

      .block {
        height: 72px;
        border-radius: 14px;
        background: linear-gradient(90deg, #ebf1f6 25%, #f8fbff 50%, #ebf1f6 75%);
        background-size: 200% 100%;
        animation: shine 1.2s linear infinite;
      }

      @keyframes shine {
        from {
          background-position: 200% 0;
        }
        to {
          background-position: -200% 0;
        }
      }
    `
  ]
})
export class LoadingSkeletonComponent {
  @Input() count = 3;

  get blocks(): number[] {
    return Array.from({ length: this.count }, (_, index) => index + 1);
  }
}
