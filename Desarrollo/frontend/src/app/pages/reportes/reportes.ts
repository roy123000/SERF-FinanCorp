
import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

interface Reporte {
  id: number;
  titulo: string;
  tipo: string;
  pais: string;
  fechaGeneracion: string;
  periodoInicio: string;
  periodoFin: string;
  moneda: string;
  estado: string;
  secciones: string[];
  ingresosTotales: number;
  costosTotales: number;
  utilidadBruta: number;
}

interface ReporteFormulario {
  titulo: string;
  tipo: string;
  pais: string;
  periodoInicio: string;
  periodoFin: string;
  moneda: string;
  estado: string;
  seccionesTexto: string;
  ingresosTotales: number;
  costosTotales: number;
}

@Component({
  selector: 'app-reportes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reportes.html',
  styleUrl: './reportes.css'
})
export class Reportes implements OnInit {

  private readonly http = inject(HttpClient);

  private readonly apiUrl = 'http://localhost:8080/api/reportes';

  reportes: Reporte[] = [];

  reporteSeleccionado: Reporte | null = null;

  formulario: ReporteFormulario = this.nuevoFormulario();

  mostrarFormulario = false;
  modoEdicion = false;
  cargando = false;
  guardando = false;

  mensaje = '';
  tipoMensaje: 'exito' | 'error' = 'exito';

  terminoBusqueda = '';
  filtroPais = '';
  filtroTipo = '';
  filtroEstado = '';

  ngOnInit(): void {
    this.listarReportes();
  }

  nuevoFormulario(): ReporteFormulario {
    return {
      titulo: '',
      tipo: 'FINANCIERO',
      pais: '',
      periodoInicio: '',
      periodoFin: '',
      moneda: 'PEN',
      estado: 'BORRADOR',
      seccionesTexto: '',
      ingresosTotales: 0,
      costosTotales: 0
    };
  }

  listarReportes(): void {
    this.cargando = true;

    this.http.get<Reporte[]>(this.apiUrl).subscribe({
      next: (datos) => {
        this.reportes = datos;
        this.cargando = false;
      },
      error: () => {
        this.cargando = false;

        this.mostrarMensaje(
          'No se pudieron cargar los reportes. Verifica que el backend esté funcionando.',
          'error'
        );
      }
    });
  }

  abrirFormulario(): void {
    this.formulario = this.nuevoFormulario();
    this.reporteSeleccionado = null;
    this.modoEdicion = false;
    this.mostrarFormulario = true;
    this.mensaje = '';
  }

  editarReporte(reporte: Reporte): void {
    this.reporteSeleccionado = reporte;

    this.formulario = {
      titulo: reporte.titulo,
      tipo: reporte.tipo,
      pais: reporte.pais,
      periodoInicio: reporte.periodoInicio,
      periodoFin: reporte.periodoFin,
      moneda: reporte.moneda,
      estado: reporte.estado,
      seccionesTexto: (reporte.secciones || []).join('\n'),
      ingresosTotales: Number(reporte.ingresosTotales || 0),
      costosTotales: Number(reporte.costosTotales || 0)
    };

    this.modoEdicion = true;
    this.mostrarFormulario = true;
    this.mensaje = '';
  }

  cerrarFormulario(): void {
    this.mostrarFormulario = false;
    this.reporteSeleccionado = null;
  }

  guardarReporte(): void {

    if (
      !this.formulario.titulo.trim() ||
      !this.formulario.tipo.trim() ||
      !this.formulario.pais.trim() ||
      !this.formulario.periodoInicio ||
      !this.formulario.periodoFin
    ) {
      this.mostrarMensaje(
        'Completa todos los campos obligatorios.',
        'error'
      );
      return;
    }

    if (this.formulario.periodoInicio > this.formulario.periodoFin) {
      this.mostrarMensaje(
        'La fecha inicial no puede ser posterior a la fecha final.',
        'error'
      );
      return;
    }

    if (
      this.formulario.ingresosTotales < 0 ||
      this.formulario.costosTotales < 0
    ) {
      this.mostrarMensaje(
        'Los ingresos y costos no pueden ser negativos.',
        'error'
      );
      return;
    }

    const secciones = this.formulario.seccionesTexto
      .split('\n')
      .map(seccion => seccion.trim())
      .filter(seccion => seccion.length > 0);

    const datosReporte = {
      titulo: this.formulario.titulo.trim(),
      tipo: this.formulario.tipo,
      pais: this.formulario.pais.trim(),
      periodoInicio: this.formulario.periodoInicio,
      periodoFin: this.formulario.periodoFin,
      moneda: this.formulario.moneda,
      estado: this.formulario.estado,
      secciones,
      ingresosTotales: Number(this.formulario.ingresosTotales),
      costosTotales: Number(this.formulario.costosTotales)
    };

    this.guardando = true;

    if (this.modoEdicion && this.reporteSeleccionado) {

      this.http.put<Reporte>(
        `${this.apiUrl}/${this.reporteSeleccionado.id}`,
        datosReporte
      ).subscribe({

        next: () => {
          this.guardando = false;
          this.cerrarFormulario();
          this.listarReportes();

          this.mostrarMensaje(
            'Reporte actualizado correctamente.',
            'exito'
          );
        },

        error: (error) => {
          this.guardando = false;

          this.mostrarMensaje(
            error.error?.message ||
            'No se pudo actualizar el reporte.',
            'error'
          );
        }

      });

    } else {

      this.http.post<Reporte>(
        this.apiUrl,
        datosReporte
      ).subscribe({

        next: () => {
          this.guardando = false;
          this.cerrarFormulario();
          this.listarReportes();

          this.mostrarMensaje(
            'Reporte registrado correctamente.',
            'exito'
          );
        },

        error: (error) => {
          this.guardando = false;

          this.mostrarMensaje(
            error.error?.message ||
            'No se pudo registrar el reporte.',
            'error'
          );
        }

      });

    }
  }

  eliminarReporte(reporte: Reporte): void {

    const confirmar = confirm(
      `¿Estás seguro de eliminar el reporte "${reporte.titulo}"?`
    );

    if (!confirmar) {
      return;
    }

    this.http.delete<void>(
      `${this.apiUrl}/${reporte.id}`
    ).subscribe({

      next: () => {
        this.listarReportes();

        this.mostrarMensaje(
          'Reporte eliminado correctamente.',
          'exito'
        );
      },

      error: (error) => {
        this.mostrarMensaje(
          error.error?.message ||
          'No se pudo eliminar el reporte.',
          'error'
        );
      }

    });
  }

  get reportesFiltrados(): Reporte[] {

    return this.reportes.filter(reporte => {

      const busqueda = this.terminoBusqueda
        .toLowerCase()
        .trim();

      const coincideBusqueda =
        reporte.titulo?.toLowerCase().includes(busqueda) ||
        reporte.pais?.toLowerCase().includes(busqueda) ||
        reporte.tipo?.toLowerCase().includes(busqueda);

      const coincidePais =
        !this.filtroPais ||
        reporte.pais === this.filtroPais;

      const coincideTipo =
        !this.filtroTipo ||
        reporte.tipo === this.filtroTipo;

      const coincideEstado =
        !this.filtroEstado ||
        reporte.estado === this.filtroEstado;

      return coincideBusqueda &&
        coincidePais &&
        coincideTipo &&
        coincideEstado;
    });
  }

  get totalReportes(): number {
    return this.reportes.length;
  }

  get reportesGenerados(): number {
    return this.reportes.filter(
      reporte => reporte.estado === 'GENERADO'
    ).length;
  }

  get reportesBorrador(): number {
    return this.reportes.filter(
      reporte => reporte.estado === 'BORRADOR'
    ).length;
  }

  get totalIngresos(): number {
    return this.reportes.reduce(
      (total, reporte) =>
        total + Number(reporte.ingresosTotales || 0),
      0
    );
  }

  get totalCostos(): number {
    return this.reportes.reduce(
      (total, reporte) =>
        total + Number(reporte.costosTotales || 0),
      0
    );
  }

  calcularUtilidad(reporte: Reporte): number {
    return Number(reporte.ingresosTotales || 0) -
      Number(reporte.costosTotales || 0);
  }

  mostrarMensaje(
    mensaje: string,
    tipo: 'exito' | 'error'
  ): void {
    this.mensaje = mensaje;
    this.tipoMensaje = tipo;
  }

}
