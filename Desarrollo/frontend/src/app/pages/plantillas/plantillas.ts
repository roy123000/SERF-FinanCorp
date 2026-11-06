
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

interface Plantilla {
  id?: number;
  nombre: string;
  descripcion: string;
  pais: string;
  tipoReporte: string;
  fechaCreacion?: string;
  version: number;
  estado: boolean;
  secciones: string[];
}

interface PlantillaFormulario {
  nombre: string;
  descripcion: string;
  pais: string;
  tipoReporte: string;
  estado: boolean;
  seccionesTexto: string;
}

@Component({
  selector: 'app-plantillas',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './plantillas.html',
  styleUrl: './plantillas.css'
})
export class Plantillas implements OnInit {

  private readonly API_URL = 'http://localhost:8080/api/plantillas';

  plantillas: Plantilla[] = [];

  cargando = false;
  guardando = false;

  mostrarFormulario = false;
  modoEdicion = false;

  plantillaSeleccionada: number | null = null;

  terminoBusqueda = '';
  filtroPais = '';
  filtroTipo = '';
  filtroEstado = '';

  mensaje = '';
  tipoMensaje: 'exito' | 'error' = 'exito';

  formulario: PlantillaFormulario = this.formularioInicial();

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.listarPlantillas();
  }

  private formularioInicial(): PlantillaFormulario {
    return {
      nombre: '',
      descripcion: '',
      pais: '',
      tipoReporte: 'FINANCIERO',
      estado: true,
      seccionesTexto: ''
    };
  }

  // LISTAR PLANTILLAS

  listarPlantillas(): void {
    this.cargando = true;

    this.http.get<Plantilla[]>(this.API_URL).subscribe({
      next: (data) => {
        this.plantillas = data || [];
        this.cargando = false;
      },
      error: (error) => {
        console.error('Error al listar plantillas:', error);
        this.mostrarMensaje('No se pudieron cargar las plantillas.', 'error');
        this.cargando = false;
      }
    });
  }

  // LISTAR SOLAMENTE PLANTILLAS ACTIVAS

  listarActivas(): void {
    this.cargando = true;

    this.http.get<Plantilla[]>(`${this.API_URL}/activas`).subscribe({
      next: (data) => {
        this.plantillas = data || [];
        this.cargando = false;
      },
      error: (error) => {
        console.error('Error al consultar plantillas activas:', error);
        this.mostrarMensaje('No se pudieron cargar las plantillas activas.', 'error');
        this.cargando = false;
      }
    });
  }

  // FILTROS

  get plantillasFiltradas(): Plantilla[] {
    return this.plantillas.filter((plantilla) => {

      const busqueda = this.terminoBusqueda.toLowerCase().trim();

      const coincideBusqueda =
        !busqueda ||
        plantilla.nombre.toLowerCase().includes(busqueda) ||
        (plantilla.descripcion || '').toLowerCase().includes(busqueda) ||
        (plantilla.pais || '').toLowerCase().includes(busqueda);

      const coincidePais =
        !this.filtroPais ||
        plantilla.pais === this.filtroPais;

      const coincideTipo =
        !this.filtroTipo ||
        plantilla.tipoReporte === this.filtroTipo;

      const coincideEstado =
        !this.filtroEstado ||
        (this.filtroEstado === 'ACTIVA' && plantilla.estado) ||
        (this.filtroEstado === 'INACTIVA' && !plantilla.estado);

      return coincideBusqueda &&
        coincidePais &&
        coincideTipo &&
        coincideEstado;
    });
  }

  get totalPlantillas(): number {
    return this.plantillas.length;
  }

  get plantillasActivas(): number {
    return this.plantillas.filter(p => p.estado).length;
  }

  get plantillasInactivas(): number {
    return this.plantillas.filter(p => !p.estado).length;
  }

  get paisesRegistrados(): number {
    return new Set(
      this.plantillas
        .map(p => p.pais)
        .filter(pais => pais && pais.trim() !== '')
    ).size;
  }

  get paisesDisponibles(): string[] {
    return [...new Set(
      this.plantillas
        .map(p => p.pais)
        .filter(pais => pais && pais.trim() !== '')
    )].sort();
  }

  // ABRIR FORMULARIO

  abrirFormulario(): void {
    this.modoEdicion = false;
    this.plantillaSeleccionada = null;
    this.formulario = this.formularioInicial();
    this.mostrarFormulario = true;
    this.mensaje = '';
  }

  // CERRAR FORMULARIO

  cerrarFormulario(): void {
    this.mostrarFormulario = false;
    this.plantillaSeleccionada = null;
    this.modoEdicion = false;
    this.formulario = this.formularioInicial();
  }

  // EDITAR PLANTILLA

  editarPlantilla(plantilla: Plantilla): void {
    this.modoEdicion = true;
    this.plantillaSeleccionada = plantilla.id ?? null;

    this.formulario = {
      nombre: plantilla.nombre,
      descripcion: plantilla.descripcion || '',
      pais: plantilla.pais || '',
      tipoReporte: plantilla.tipoReporte || 'FINANCIERO',
      estado: plantilla.estado,
      seccionesTexto: (plantilla.secciones || []).join('\n')
    };

    this.mostrarFormulario = true;
    this.mensaje = '';
  }

  // GUARDAR O ACTUALIZAR

  guardarPlantilla(): void {

    if (
      !this.formulario.nombre.trim() ||
      !this.formulario.pais.trim() ||
      !this.formulario.tipoReporte.trim()
    ) {
      this.mostrarMensaje(
        'Completa el nombre, país y tipo de reporte.',
        'error'
      );
      return;
    }

    const secciones = this.formulario.seccionesTexto
      .split('\n')
      .map(seccion => seccion.trim())
      .filter(seccion => seccion.length > 0);

    const plantilla: Plantilla = {
      nombre: this.formulario.nombre.trim(),
      descripcion: this.formulario.descripcion.trim(),
      pais: this.formulario.pais.trim(),
      tipoReporte: this.formulario.tipoReporte,
      version: 1,
      estado: this.formulario.estado,
      secciones: secciones
    };

    this.guardando = true;

    if (this.modoEdicion && this.plantillaSeleccionada !== null) {

      const id = this.plantillaSeleccionada;

      this.http.put<Plantilla>(
        `${this.API_URL}/${id}`,
        plantilla
      ).subscribe({
        next: () => {
          this.guardando = false;
          this.cerrarFormulario();
          this.mostrarMensaje(
            'Plantilla actualizada correctamente.',
            'exito'
          );
          this.listarPlantillas();
        },
        error: (error) => {
          console.error('Error al actualizar plantilla:', error);
          this.guardando = false;
          this.mostrarMensaje(
            'No se pudo actualizar la plantilla.',
            'error'
          );
        }
      });

    } else {

      this.http.post<Plantilla>(
        this.API_URL,
        plantilla
      ).subscribe({
        next: () => {
          this.guardando = false;
          this.cerrarFormulario();
          this.mostrarMensaje(
            'Plantilla registrada correctamente.',
            'exito'
          );
          this.listarPlantillas();
        },
        error: (error) => {
          console.error('Error al registrar plantilla:', error);
          this.guardando = false;
          this.mostrarMensaje(
            'No se pudo registrar la plantilla.',
            'error'
          );
        }
      });

    }
  }

  // CLONAR PLANTILLA

  clonarPlantilla(plantilla: Plantilla): void {

    if (!plantilla.id) {
      return;
    }

    const confirmar = confirm(
      `¿Deseas clonar la plantilla "${plantilla.nombre}"?`
    );

    if (!confirmar) {
      return;
    }

    this.http.post<Plantilla>(
      `${this.API_URL}/${plantilla.id}/clonar`,
      {}
    ).subscribe({
      next: () => {
        this.mostrarMensaje(
          'Plantilla clonada correctamente.',
          'exito'
        );
        this.listarPlantillas();
      },
      error: (error) => {
        console.error('Error al clonar plantilla:', error);
        this.mostrarMensaje(
          'No se pudo clonar la plantilla.',
          'error'
        );
      }
    });
  }

  // CAMBIAR ESTADO

  cambiarEstado(plantilla: Plantilla): void {

    if (!plantilla.id) {
      return;
    }

    const plantillaActualizada: Plantilla = {
      ...plantilla,
      estado: !plantilla.estado
    };

    this.http.put<Plantilla>(
      `${this.API_URL}/${plantilla.id}`,
      plantillaActualizada
    ).subscribe({
      next: () => {
        this.mostrarMensaje(
          plantillaActualizada.estado
            ? 'Plantilla activada correctamente.'
            : 'Plantilla desactivada correctamente.',
          'exito'
        );
        this.listarPlantillas();
      },
      error: (error) => {
        console.error('Error al cambiar estado:', error);
        this.mostrarMensaje(
          'No se pudo cambiar el estado de la plantilla.',
          'error'
        );
      }
    });
  }

  // ELIMINAR PLANTILLA

  eliminarPlantilla(plantilla: Plantilla): void {

    if (!plantilla.id) {
      return;
    }

    const confirmar = confirm(
      `¿Estás seguro de eliminar la plantilla "${plantilla.nombre}"?`
    );

    if (!confirmar) {
      return;
    }

    this.http.delete<void>(
      `${this.API_URL}/${plantilla.id}`
    ).subscribe({
      next: () => {
        this.mostrarMensaje(
          'Plantilla eliminada correctamente.',
          'exito'
        );
        this.listarPlantillas();
      },
      error: (error) => {
        console.error('Error al eliminar plantilla:', error);
        this.mostrarMensaje(
          'No se pudo eliminar la plantilla.',
          'error'
        );
      }
    });
  }

  // MENSAJES

  private mostrarMensaje(
    mensaje: string,
    tipo: 'exito' | 'error'
  ): void {
    this.mensaje = mensaje;
    this.tipoMensaje = tipo;
  }

}
