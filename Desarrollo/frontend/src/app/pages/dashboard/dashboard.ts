
import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    RouterLinkActive
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard {

  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  usuario = this.authService.obtenerUsuario();

  cerrarSesion(): void {
    this.authService.cerrarSesion();
    this.router.navigate(['/login']);
  }
}
