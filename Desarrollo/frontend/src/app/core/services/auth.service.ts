
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

export interface LoginRequest {
  correo: string;
  contrasena: string;
}

export interface LoginResponse {
  token: string;
  tipo: string;
  id: number;
  nombre: string;
  correo: string;
  rol: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly http = inject(HttpClient);

  private readonly apiUrl = 'http://localhost:8080/api/auth';

  iniciarSesion(datos: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(
      `${this.apiUrl}/login`,
      datos
    ).pipe(
      tap(respuesta => {
        localStorage.setItem('token', respuesta.token);

        localStorage.setItem('usuario', JSON.stringify({
          id: respuesta.id,
          nombre: respuesta.nombre,
          correo: respuesta.correo,
          rol: respuesta.rol
        }));
      })
    );
  }

  cerrarSesion(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('usuario');
  }

  obtenerToken(): string | null {
    return localStorage.getItem('token');
  }

  obtenerUsuario(): LoginResponse | null {
    const usuario = localStorage.getItem('usuario');
    return usuario ? JSON.parse(usuario) : null;
  }

  estaAutenticado(): boolean {
    return !!this.obtenerToken();
  }
}
