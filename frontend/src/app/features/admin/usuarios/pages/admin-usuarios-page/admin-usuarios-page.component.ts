import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../../../core/auth/auth.service';
import { AdminUsuariosApiService } from '../../data/admin-usuarios-api.service';
import { AdminRole, AdminUser, AdminUserUpsertRequest } from '../../data/admin-usuarios.models';
import { ConfirmDialogComponent } from '../../../../sprint1/shared/ui/confirm-dialog.component';

@Component({
  selector: 'app-admin-usuarios-page',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, ConfirmDialogComponent],
  templateUrl: './admin-usuarios-page.component.html',
  styleUrl: './admin-usuarios-page.component.css'
})
export class AdminUsuariosPageComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);
  readonly authService = inject(AuthService);
  private readonly api = inject(AdminUsuariosApiService);

  readonly loading = signal(false);
  readonly saving = signal(false);
  readonly error = signal('');
  readonly users = signal<AdminUser[]>([]);
  readonly roles = signal<AdminRole[]>([]);
  readonly selectedUserId = signal<number | null>(null);
  readonly search = signal('');
  readonly confirmDialogOpen = signal(false);
  readonly userPendingInactivation = signal<AdminUser | null>(null);
  readonly saveConfirmOpen = signal(false);
  readonly pendingSavePayload = signal<AdminUserUpsertRequest | null>(null);
  readonly pendingSaveUserId = signal<number | null>(null);

  readonly form = this.fb.nonNullable.group({
    nombres: ['', [Validators.required, Validators.maxLength(100)]],
    apellidos: ['', [Validators.maxLength(100)]],
    correo: ['', [Validators.required, Validators.email, Validators.maxLength(120)]],
    contrasena: ['', [Validators.maxLength(120)]],
    idRol: [1, [Validators.required]]
  });

  async ngOnInit(): Promise<void> {
    if (!this.authService.hasAnyRole(['ADMINISTRADOR'])) {
      await this.router.navigateByUrl('/');
      return;
    }

    await Promise.all([this.loadRoles(), this.loadUsers()]);
  }

  async loadUsers(): Promise<void> {
    this.loading.set(true);
    this.error.set('');
    try {
      this.users.set(await this.api.listUsers(this.search()));
    } catch (error) {
      this.error.set(this.errorMessage(error));
    } finally {
      this.loading.set(false);
    }
  }

  async onSearchInput(event: Event): Promise<void> {
    const input = event.target as HTMLInputElement;
    this.search.set(input.value ?? '');
    await this.loadUsers();
  }

  startCreate(): void {
    this.selectedUserId.set(null);
    this.form.reset({
      nombres: '',
      apellidos: '',
      correo: '',
      contrasena: '',
      idRol: this.roles()[0]?.idRol ?? 1
    });
    this.error.set('');
  }

  startEdit(user: AdminUser): void {
    this.selectedUserId.set(user.idUsuario);
    this.form.reset({
      nombres: user.nombres,
      apellidos: user.apellidos ?? '',
      correo: user.correo,
      contrasena: '',
      idRol: user.idRol
    });
    this.error.set('');
  }

  async saveUser(): Promise<void> {
    if (this.form.invalid || this.saving()) {
      this.form.markAllAsTouched();
      return;
    }

    try {
      const payload = this.form.getRawValue();
      const selectedId = this.selectedUserId();

      if (selectedId == null && payload.contrasena.trim().length < 6) {
        this.error.set('La contraseña debe tener al menos 6 caracteres');
        return;
      }
      this.pendingSavePayload.set(payload);
      this.pendingSaveUserId.set(selectedId);
      this.saveConfirmOpen.set(true);
      this.error.set('');
    } catch (error) {
      this.error.set(this.errorMessage(error));
    }
  }

  requestInactivate(user: AdminUser): void {
    this.userPendingInactivation.set(user);
    this.confirmDialogOpen.set(true);
  }

  cancelInactivation(): void {
    this.confirmDialogOpen.set(false);
    this.userPendingInactivation.set(null);
  }

  async confirmInactivation(): Promise<void> {
    const user = this.userPendingInactivation();
    if (!user) {
      return;
    }

    this.error.set('');
    try {
      await this.api.inactivateUser(user.idUsuario);
      await this.loadUsers();
      if (this.selectedUserId() === user.idUsuario) {
        this.startCreate();
      }
      this.cancelInactivation();
    } catch (error) {
      this.error.set(this.errorMessage(error));
    }
  }

  cancelSaveConfirmation(): void {
    this.saveConfirmOpen.set(false);
    this.pendingSavePayload.set(null);
    this.pendingSaveUserId.set(null);
  }

  async confirmSaveUser(): Promise<void> {
    const payload = this.pendingSavePayload();
    if (!payload || this.saving()) {
      return;
    }

    const selectedId = this.pendingSaveUserId();
    this.saveConfirmOpen.set(false);
    this.saving.set(true);
    this.error.set('');

    try {
      if (selectedId == null) {
        await this.api.createUser(payload);
      } else {
        await this.api.updateUser(selectedId, payload);
      }

      await this.loadUsers();
      this.startCreate();
      this.pendingSavePayload.set(null);
      this.pendingSaveUserId.set(null);
    } catch (error) {
      this.error.set(this.errorMessage(error));
    } finally {
      this.saving.set(false);
    }
  }

  isActive(user: AdminUser): boolean {
    return user.estado === 1;
  }

  isEditing(user: AdminUser): boolean {
    return this.selectedUserId() === user.idUsuario;
  }

  async goHome(): Promise<void> {
    await this.router.navigateByUrl('/');
  }

  logout(): void {
    this.authService.logout();
    void this.router.navigateByUrl('/login');
  }

  private async loadRoles(): Promise<void> {
    try {
      const roles = await this.api.listRoles();
      this.roles.set(roles);
      if (!this.selectedUserId()) {
        this.form.patchValue({ idRol: roles[0]?.idRol ?? 1 });
      }
    } catch (error) {
      this.error.set(this.errorMessage(error));
    }
  }

  private errorMessage(error: unknown): string {
    if (typeof error === 'object' && error != null) {
      const maybeError = error as { error?: { message?: string }; message?: string };
      if (typeof maybeError.error?.message === 'string') {
        return maybeError.error.message;
      }
      if (typeof maybeError.message === 'string') {
        return maybeError.message;
      }
    }
    return 'No se pudo completar la operación';
  }
}
