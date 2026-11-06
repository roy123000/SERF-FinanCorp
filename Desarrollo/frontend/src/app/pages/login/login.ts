
import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {

  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  correo = '';
  contrasena = '';

  cargando = false;
  mostrarContrasena = false;
  mensajeError = '';

  iniciarSesion(): void {

    this.mensajeError = '';

    if (!this.correo.trim() || !this.contrasena) {
      this.mensajeError = 'Ingresa tu correo y contraseña.';
      return;
    }

    this.cargando = true;

    this.authService.iniciarSesion({
      correo: this.correo.trim(),
      contrasena: this.contrasena
    }).subscribe({
      next: () => {
        this.cargando = false;
        this.router.navigate(['/dashboard']);
      },
      error: (error) => {
        this.cargando = false;

        this.mensajeError =
          error.error?.mensaje ||
          'No se pudo iniciar sesión. Verifica tus credenciales.';
      }
    });
  }
}
