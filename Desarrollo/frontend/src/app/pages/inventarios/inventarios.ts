
import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpParams } from '@angular/common/http';

interface Producto {
  id: number;
  codigo: string;
  nombre: string;
}

interface Filial {
  id: number;
  nombre: string;
  pais: string;
}

interface Inventario {
  id: number;
  producto: Producto;
  filial: Filial;
  cantidadDisponible: number;
  stockMinimo: number;
  estado: boolean;
  fechaActualizacion: string;
}

interface OpcionProducto {
  id: number;
  codigo: string;
  nombre: string;
}

interface OpcionFilial {
  id: number;
  nombre: string;
  pais: string;
}

@Component({
  selector: 'app-inventarios',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './inventarios.html',
  styleUrl: './inventarios.css'
})
export class Inventarios implements OnInit {

  private readonly http = inject(HttpClient);

  private readonly apiUrl = 'http://localhost:8080/api/inventarios';
  private readonly productosUrl = 'http://localhost:8080/api/productos';
  private readonly filialesUrl = 'http://localhost:8080/api/filiales';

  inventarios: Inventario[] = [];
  productos: OpcionProducto[] = [];
  filiales: OpcionFilial[] = [];

  inventarioSeleccionado: Inventario | null = null;

  mostrarFormulario = false;
  mostrarFormularioMovimiento = false;

  tipoMovimiento: 'entrada' | 'salida' = 'entrada';

  nuevoInventario = {
    productoId: 0,
    filialId: 0,
    cantidadInicial: 0,
    stockMinimo: 5
  };

  cantidadMovimiento = 0;

  terminoBusqueda = '';
  filtroFilial = '';
  filtroStock = 'todos';

  cargando = false;
  mensaje = '';
  tipoMensaje: 'exito' | 'error' = 'exito';

  ngOnInit(): void {
    this.listarInventarios();
    this.listarProductos();
    this.listarFiliales();
  }

  listarInventarios(): void {
    this.cargando = true;

    this.http.get<Inventario[]>(this.apiUrl).subscribe({
      next: (datos) => {
        this.inventarios = datos;
        this.cargando = false;
      },
      error: () => {
        this.cargando = false;
        this.mostrarMensaje(
          'No se pudieron cargar los inventarios. Verifica que el backend esté funcionando.',
          'error'
        );
      }
    });
  }

  listarProductos(): void {
    this.http.get<OpcionProducto[]>(this.productosUrl).subscribe({
      next: (datos) => {
        this.productos = datos;
      },
      error: () => {
        this.mostrarMensaje(
          'No se pudieron cargar los productos.',
          'error'
        );
      }
    });
  }

  listarFiliales(): void {
    this.http.get<OpcionFilial[]>(this.filialesUrl).subscribe({
      next: (datos) => {
        this.filiales = datos;
      },
      error: () => {
        this.mostrarMensaje(
          'No se pudieron cargar las filiales.',
          'error'
        );
      }
    });
  }

  abrirFormulario(): void {
    this.nuevoInventario = {
      productoId: 0,
      filialId: 0,
      cantidadInicial: 0,
      stockMinimo: 5
    };

    this.mostrarFormulario = true;
    this.mensaje = '';
  }

  cerrarFormulario(): void {
    this.mostrarFormulario = false;
  }

  registrarInventario(): void {
    if (
      this.nuevoInventario.productoId <= 0 ||
      this.nuevoInventario.filialId <= 0 ||
      this.nuevoInventario.cantidadInicial < 0 ||
      this.nuevoInventario.stockMinimo < 0
    ) {
      this.mostrarMensaje(
        'Completa los campos correctamente.',
        'error'
      );
      return;
    }

    const params = new HttpParams()
      .set('productoId', this.nuevoInventario.productoId)
      .set('filialId', this.nuevoInventario.filialId)
      .set('cantidadInicial', this.nuevoInventario.cantidadInicial)
      .set('stockMinimo', this.nuevoInventario.stockMinimo);

    this.http.post<Inventario>(this.apiUrl, null, { params }).subscribe({
      next: () => {
        this.mostrarFormulario = false;
        this.listarInventarios();

        this.mostrarMensaje(
          'Inventario registrado correctamente.',
          'exito'
        );
      },
      error: (error) => {
        const mensajeError =
          error.error?.message ||
          'No se pudo registrar el inventario. Verifica los datos.';

        this.mostrarMensaje(mensajeError, 'error');
      }
    });
  }

  abrirMovimiento(
    inventario: Inventario,
    tipo: 'entrada' | 'salida'
  ): void {
    this.inventarioSeleccionado = inventario;
    this.tipoMovimiento = tipo;
    this.cantidadMovimiento = 0;
    this.mostrarFormularioMovimiento = true;
    this.mensaje = '';
  }

  cerrarFormularioMovimiento(): void {
    this.mostrarFormularioMovimiento = false;
    this.inventarioSeleccionado = null;
    this.cantidadMovimiento = 0;
  }

  registrarMovimiento(): void {
    if (
      !this.inventarioSeleccionado ||
      this.cantidadMovimiento <= 0
    ) {
      this.mostrarMensaje(
        'Ingresa una cantidad válida para el movimiento.',
        'error'
      );
      return;
    }

    const id = this.inventarioSeleccionado.id;

    const url = `${this.apiUrl}/${id}/${this.tipoMovimiento}`;

    const params = new HttpParams()
      .set('cantidad', this.cantidadMovimiento);

    this.http.patch<Inventario>(url, null, { params }).subscribe({
      next: () => {
        this.cerrarFormularioMovimiento();
        this.listarInventarios();

        this.mostrarMensaje(
          this.tipoMovimiento === 'entrada'
            ? 'Entrada de productos registrada correctamente.'
            : 'Salida de productos registrada correctamente.',
          'exito'
        );
      },
      error: (error) => {
        const mensajeError =
          error.error?.message ||
          'No se pudo registrar el movimiento. Verifica la cantidad disponible.';

        this.mostrarMensaje(mensajeError, 'error');
      }
    });
  }

  get inventariosFiltrados(): Inventario[] {
    return this.inventarios.filter((inventario) => {
      const busqueda = this.terminoBusqueda.toLowerCase().trim();

      const coincideBusqueda =
        inventario.producto?.nombre?.toLowerCase().includes(busqueda) ||
        inventario.producto?.codigo?.toLowerCase().includes(busqueda) ||
        inventario.filial?.nombre?.toLowerCase().includes(busqueda);

      const coincideFilial =
        !this.filtroFilial ||
        inventario.filial?.id === Number(this.filtroFilial);

      const coincideStock =
        this.filtroStock === 'todos' ||
        (this.filtroStock === 'bajo' &&
          inventario.cantidadDisponible <= inventario.stockMinimo) ||
        (this.filtroStock === 'normal' &&
          inventario.cantidadDisponible > inventario.stockMinimo);

      return coincideBusqueda && coincideFilial && coincideStock;
    });
  }

  get totalInventarios(): number {
    return this.inventarios.length;
  }

  get totalStock(): number {
    return this.inventarios.reduce(
      (total, inventario) => total + inventario.cantidadDisponible,
      0
    );
  }

  get inventariosStockBajo(): number {
    return this.inventarios.filter(
      (inventario) =>
        inventario.cantidadDisponible <= inventario.stockMinimo
    ).length;
  }

  get inventariosStockNormal(): number {
    return this.inventarios.filter(
      (inventario) =>
        inventario.cantidadDisponible > inventario.stockMinimo
    ).length;
  }

  mostrarMensaje(
    mensaje: string,
    tipo: 'exito' | 'error'
  ): void {
    this.mensaje = mensaje;
    this.tipoMensaje = tipo;
  }
}
