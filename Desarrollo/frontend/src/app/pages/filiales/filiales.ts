
import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

interface Filial {
  id?: number;
  nombre: string;
  pais: string;
  ciudad: string;
  direccion: string;
  estado: boolean;
}

@Component({
  selector: 'app-filiales',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './filiales.html',
  styleUrl: './filiales.css'
})
export class Filiales implements OnInit {

  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/filiales';

  filiales: Filial[] = [];

  filialSeleccionada: Filial = this.nuevaFilial();

  mostrarFormulario = false;
  modoEdicion = false;
  cargando = false;
  mensaje = '';
  tipoMensaje: 'exito' | 'error' = 'exito';
  terminoBusqueda = '';

  ngOnInit(): void {
    this.listarFiliales();
  }

  nuevaFilial(): Filial {
    return {
      nombre: '',
      pais: '',
      ciudad: '',
      direccion: '',
      estado: true
    };
  }

  listarFiliales(): void {
    this.cargando = true;
    this.http.get<Filial[]>(this.apiUrl).subscribe({
      next: (datos) => {
        this.filiales = datos;
        this.cargando = false;
      },
      error: () => {
        this.mostrarMensaje(
          'No se pudieron cargar las filiales. Verifica que el backend esté funcionando.',
          'error'
        );
        this.cargando = false;
      }
    });
  }

  abrirFormulario(): void {
    this.filialSeleccionada = this.nuevaFilial();
    this.modoEdicion = false;
    this.mostrarFormulario = true;
    this.mensaje = '';
  }

  editarFilial(filial: Filial): void {
    this.filialSeleccionada = { ...filial };
    this.modoEdicion = true;
    this.mostrarFormulario = true;
    this.mensaje = '';
  }

  guardarFilial(): void {
    const datos = this.filialSeleccionada;

    if (
      !datos.nombre.trim() ||
      !datos.pais.trim() ||
      !datos.ciudad.trim() ||
      !datos.direccion.trim()
    ) {
      this.mostrarMensaje(
        'Completa todos los campos obligatorios.',
        'error'
      );
      return;
    }

    this.cargando = true;

    const solicitud = this.modoEdicion
      ? this.http.put<Filial>(
          `${this.apiUrl}/${datos.id}`,
          datos
        )
      : this.http.post<Filial>(this.apiUrl, datos);

    solicitud.subscribe({
      next: () => {
        this.mostrarMensaje(
          this.modoEdicion
            ? 'Filial actualizada correctamente.'
            : 'Filial registrada correctamente.',
          'exito'
        );
        this.cerrarFormulario();
        this.listarFiliales();
      },
      error: (error) => {
        this.cargando = false;
        this.mostrarMensaje(
          error.error?.mensaje ||
          'No se pudo guardar la filial. Verifica los datos.',
          'error'
        );
      }
    });
  }

  cambiarEstado(filial: Filial): void {
    if (!filial.id) return;

    const nuevoEstado = !filial.estado;

    this.http.patch<Filial>(
      `${this.apiUrl}/${filial.id}/estado`,
      { estado: nuevoEstado }
    ).subscribe({
      next: () => {
        this.mostrarMensaje(
          'Estado de la filial actualizado.',
          'exito'
        );
        this.listarFiliales();
      },
      error: () => {
        this.mostrarMensaje(
          'No se pudo cambiar el estado. Esta operación requiere un endpoint de actualización de estado en el backend.',
          'error'
        );
      }
    });
  }

  cerrarFormulario(): void {
    this.mostrarFormulario = false;
    this.filialSeleccionada = this.nuevaFilial();
    this.cargando = false;
  }

  eliminarFilial(filial: Filial): void {
    if (!filial.id) return;

    const confirmar = confirm(
      `¿Estás seguro de eliminar la filial ${filial.nombre}?`
    );

    if (!confirmar) return;

    this.http.delete(`${this.apiUrl}/${filial.id}`).subscribe({
      next: () => {
        this.mostrarMensaje(
          'Filial eliminada correctamente.',
          'exito'
        );
        this.listarFiliales();
      },
      error: () => {
        this.mostrarMensaje(
          'No se pudo eliminar la filial. Comprueba si tiene registros relacionados.',
          'error'
        );
      }
    });
  }

  get filialesFiltradas(): Filial[] {
    const termino = this.terminoBusqueda.toLowerCase().trim();

    if (!termino) return this.filiales;

    return this.filiales.filter(f =>
      f.nombre.toLowerCase().includes(termino) ||
      f.pais.toLowerCase().includes(termino) ||
      f.ciudad.toLowerCase().includes(termino)
    );
  }

  private mostrarMensaje(
    mensaje: string,
    tipo: 'exito' | 'error'
  ): void {
    this.mensaje = mensaje;
    this.tipoMensaje = tipo;
  }
}
