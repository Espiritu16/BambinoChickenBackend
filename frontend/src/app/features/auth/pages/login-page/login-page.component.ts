import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../../core/auth/auth.service';

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.css'
})
export class LoginPageComponent {
  protected readonly loading = signal(false);
  protected readonly showPassword = signal(false);
  protected readonly shaking = signal(false);
  protected readonly errorMessage = signal('');
  protected readonly testUsers = [
    { correo: 'admin@bambino.com', contrasena: '123456', descripcion: 'Administrador' },
    { correo: 'cajero@bambino.com', contrasena: '123456', descripcion: 'Cajero' },
    { correo: 'mozo@bambino.com', contrasena: '123456', descripcion: 'Mozo, sin acceso a POS' }
  ] as const;
  protected readonly form;

  constructor(
    private readonly fb: FormBuilder,
    private readonly authService: AuthService,
    private readonly router: Router
  ) {
    this.form = this.fb.nonNullable.group({
      correo: ['admin@bambino.com', [Validators.required, Validators.email]],
      contrasena: ['123456', [Validators.required]]
    });
  }

  async submit(): Promise<void> {
    if (this.form.invalid || this.loading()) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading.set(true);
    this.errorMessage.set('');

    try {
      await this.authService.login(this.form.getRawValue());
      await this.router.navigateByUrl('/');
    } catch (error) {
      const message = error instanceof Error ? error.message : 'No se pudo iniciar sesion';
      this.errorMessage.set(message);
      this.shaking.set(true);
      setTimeout(() => this.shaking.set(false), 450);
    } finally {
      this.loading.set(false);
    }
  }

  togglePasswordVisibility(): void {
    this.showPassword.set(!this.showPassword());
  }

  useTestUser(correo: string, contrasena: string): void {
    this.form.setValue({ correo, contrasena });
    this.errorMessage.set('');
  }
}
