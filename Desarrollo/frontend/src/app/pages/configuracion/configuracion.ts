
import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

interface ConfiguracionSistema {
  monedaBase: string;
  idioma: string;
  formatoFecha: string;
  tasaImpuesto: number;
}

@Component({
  selector: 'app-configuracion',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './configuracion.html',
  styleUrl: './configuracion.css'
})
export class Configuracion implements OnInit {

  private http = inject(HttpClient);

  private apiUrl = 'http://localhost:8080/api/configuracion';

  configuracion: ConfiguracionSistema = {
    monedaBase: 'PEN',
    idioma: 'Español',
    formatoFecha: 'dd/MM/yyyy',
    tasaImpuesto: 18
  };

  cargando = false;
  guardando = false;
  mensaje = '';
  tipoMensaje: 'exito' | 'error' | '' = '';

  ngOnInit(): void {
    this.obtenerConfiguracion();
  }

  obtenerConfiguracion(): void {
    this.cargando = true;

    this.http.get<ConfiguracionSistema>(this.apiUrl).subscribe({
      next: (respuesta) => {
        this.configuracion = respuesta;
        this.cargando = false;
      },
      error: (error) => {
        console.error('Error al obtener la configuración:', error);
        this.mostrarMensaje(
          'No se pudo cargar la configuración del sistema.',
          'error'
        );
        this.cargando = false;
      }
    });
  }

  guardarConfiguracion(): void {

    if (
      !this.configuracion.monedaBase ||
      !this.configuracion.idioma ||
      !this.configuracion.formatoFecha ||
      this.configuracion.tasaImpuesto === null ||
      this.configuracion.tasaImpuesto === undefined ||
      this.configuracion.tasaImpuesto < 0 ||
      this.configuracion.tasaImpuesto > 100
    ) {
      this.mostrarMensaje(
        'Verifica los campos. La tasa debe estar entre 0 y 100.',
        'error'
      );
      return;
    }

    this.guardando = true;
    this.mensaje = '';

    this.http.put<ConfiguracionSistema>(
      this.apiUrl,
      this.configuracion
    ).subscribe({
      next: (respuesta) => {
        this.configuracion = respuesta;
        this.guardando = false;
        this.mostrarMensaje(
          'La configuración se actualizó correctamente.',
          'exito'
        );
      },
      error: (error) => {
        console.error('Error al actualizar la configuración:', error);
        this.guardando = false;
        this.mostrarMensaje(
          'No se pudo actualizar la configuración.',
          'error'
        );
      }
    });
  }

  restablecerConfiguracion(): void {
    this.obtenerConfiguracion();
    this.mensaje = '';
  }

  private mostrarMensaje(
    mensaje: string,
    tipo: 'exito' | 'error'
  ): void {
    this.mensaje = mensaje;
    this.tipoMensaje = tipo;
  }
}
