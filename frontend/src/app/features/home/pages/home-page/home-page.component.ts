import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../../../core/auth/auth.service';

@Component({
  selector: 'app-home-page',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './home-page.component.html',
  styleUrl: './home-page.component.css'
})
export class HomePageComponent {
  constructor(
    public readonly authService: AuthService,
    private readonly router: Router
  ) {}

  logout(): void {
    this.authService.logout();
    void this.router.navigateByUrl('/login');
  }
}
