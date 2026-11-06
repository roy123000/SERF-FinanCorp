
import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

interface Producto {
  id: number;
  codigo: string;
  nombre: string;
  precioVenta: number;
  estado: boolean;
}

interface Filial {
  id: number;
  nombre: string;
  pais: string;
  estado?: boolean;
}

interface DetalleVenta {
  id?: number;
  producto: Producto;
  cantidad: number;
  precioUnitario: number;
  descuento: number;
  subtotal: number;
}

interface Venta {
  id: number;
  numeroVenta: string;
  filial: Filial;
  moneda: string;
  subtotal: number;
  tasaImpuesto: number;
  impuesto: number;
  total: number;
  estado: string;
  detalles: DetalleVenta[];
  fechaRegistro?: string;
}

interface DetalleFormulario {
  productoId: number;
  cantidad: number;
  descuento: number;
}

@Component({
  selector: 'app-ventas',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './ventas.html',
  styleUrl: './ventas.css'
})
export class Ventas implements OnInit {

  private readonly http = inject(HttpClient);

  private readonly apiUrl = 'http://localhost:8080/api/ventas';
  private readonly productosUrl = 'http://localhost:8080/api/productos';
  private readonly filialesUrl = 'http://localhost:8080/api/filiales';

  ventas: Venta[] = [];
  productos: Producto[] = [];
  filiales: Filial[] = [];

  mostrarFormulario = false;
  cargando = false;
  guardando = false;

  mensaje = '';
  tipoMensaje: 'exito' | 'error' = 'exito';

  terminoBusqueda = '';
  filtroEstado = 'todos';
  filtroFilial = '';

  numeroVenta = '';
  filialId = 0;
  moneda = 'PEN';
  tasaImpuesto = 18;

  detallesFormulario: DetalleFormulario[] = [
    {
      productoId: 0,
      cantidad: 1,
      descuento: 0
    }
  ];

  ngOnInit(): void {
    this.listarVentas();
    this.listarProductos();
    this.listarFiliales();
  }

  listarVentas(): void {
    this.cargando = true;

    this.http.get<Venta[]>(this.apiUrl).subscribe({
      next: (datos) => {
        this.ventas = datos;
        this.cargando = false;
      },
      error: () => {
        this.cargando = false;
        this.mostrarMensaje(
          'No se pudieron cargar las ventas. Verifica el backend.',
          'error'
        );
      }
    });
  }

  listarProductos(): void {
    this.http.get<Producto[]>(this.productosUrl).subscribe({
      next: (datos) => {
        this.productos = datos.filter(
          producto => producto.estado !== false
        );
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
    this.http.get<Filial[]>(this.filialesUrl).subscribe({
      next: (datos) => {
        this.filiales = datos.filter(
          filial => filial.estado !== false
        );
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
    this.numeroVenta = this.generarNumeroVenta();
    this.filialId = 0;
    this.moneda = 'PEN';
    this.tasaImpuesto = 18;

    this.detallesFormulario = [
      {
        productoId: 0,
        cantidad: 1,
        descuento: 0
      }
    ];

    this.mensaje = '';
    this.mostrarFormulario = true;
  }

  cerrarFormulario(): void {
    this.mostrarFormulario = false;
  }

  generarNumeroVenta(): string {
    const fecha = new Date();
    const marcaTiempo = fecha.getTime().toString().slice(-6);

    return `VTA-${marcaTiempo}`;
  }

  agregarDetalle(): void {
    this.detallesFormulario.push({
      productoId: 0,
      cantidad: 1,
      descuento: 0
    });
  }

  eliminarDetalle(indice: number): void {
    if (this.detallesFormulario.length > 1) {
      this.detallesFormulario.splice(indice, 1);
    }
  }

  obtenerProducto(productoId: number): Producto | undefined {
    return this.productos.find(
      producto => producto.id === Number(productoId)
    );
  }

  obtenerPrecio(productoId: number): number {
    return this.obtenerProducto(productoId)?.precioVenta ?? 0;
  }

  calcularSubtotalDetalle(detalle: DetalleFormulario): number {
    const precio = this.obtenerPrecio(detalle.productoId);
    const descuento = Math.max(
      0,
      Number(detalle.descuento) || 0
    );
    const cantidad = Math.max(
      0,
      Number(detalle.cantidad) || 0
    );

    return Math.max(
      0,
      (precio * cantidad) - descuento
    );
  }

  get subtotalVenta(): number {
    return this.detallesFormulario.reduce(
      (total, detalle) =>
        total + this.calcularSubtotalDetalle(detalle),
      0
    );
  }

  get impuestoVenta(): number {
    return this.subtotalVenta *
      (Number(this.tasaImpuesto) || 0) / 100;
  }

  get totalVenta(): number {
    return this.subtotalVenta + this.impuestoVenta;
  }

  registrarVenta(): void {

    if (!this.numeroVenta.trim() || this.filialId <= 0) {
      this.mostrarMensaje(
        'Ingresa el número de venta y selecciona una filial.',
        'error'
      );
      return;
    }

    if (this.detallesFormulario.length === 0) {
      this.mostrarMensaje(
        'Agrega al menos un producto.',
        'error'
      );
      return;
    }

    for (const detalle of this.detallesFormulario) {

      if (
        detalle.productoId <= 0 ||
        detalle.cantidad <= 0 ||
        detalle.descuento < 0
      ) {
        this.mostrarMensaje(
          'Revisa los productos, cantidades y descuentos.',
          'error'
        );
        return;
      }

      if (!this.obtenerProducto(detalle.productoId)) {
        this.mostrarMensaje(
          'Selecciona un producto válido.',
          'error'
        );
        return;
      }

      if (detalle.descuento > this.obtenerPrecio(detalle.productoId) * detalle.cantidad) {
        this.mostrarMensaje(
          'El descuento no puede superar el importe del producto.',
          'error'
        );
        return;
      }
    }

    const detalles = this.detallesFormulario.map(detalle => {

      const producto = this.obtenerProducto(
        detalle.productoId
      )!;

      const precioUnitario = Number(producto.precioVenta);

      const subtotal = this.calcularSubtotalDetalle(detalle);

      return {
        producto: {
          id: producto.id
        },
        cantidad: Number(detalle.cantidad),
        precioUnitario,
        descuento: Number(detalle.descuento),
        subtotal
      };
    });

    const nuevaVenta = {
      numeroVenta: this.numeroVenta.trim(),
      filial: {
        id: Number(this.filialId)
      },
      moneda: this.moneda,
      tasaImpuesto: Number(this.tasaImpuesto),
      detalles
    };

    this.guardando = true;

    this.http.post<Venta>(
      this.apiUrl,
      nuevaVenta
    ).subscribe({

      next: () => {
        this.guardando = false;
        this.mostrarFormulario = false;

        this.listarVentas();

        this.mostrarMensaje(
          'Venta registrada correctamente.',
          'exito'
        );
      },

      error: (error) => {
        this.guardando = false;

        const mensajeError =
          error.error?.message ||
          'No se pudo registrar la venta. Verifica el stock disponible y los datos ingresados.';

        this.mostrarMensaje(
          mensajeError,
          'error'
        );
      }

    });
  }

  anularVenta(venta: Venta): void {

    if (venta.estado === 'ANULADA') {
      this.mostrarMensaje(
        'Esta venta ya se encuentra anulada.',
        'error'
      );
      return;
    }

    const confirmar = confirm(
      `¿Estás seguro de anular la venta ${venta.numeroVenta}? Las unidades regresarán al inventario.`
    );

    if (!confirmar) {
      return;
    }

    this.http.patch(
      `${this.apiUrl}/${venta.id}/anular`,
      {}
    ).subscribe({

      next: () => {
        this.listarVentas();

        this.mostrarMensaje(
          'Venta anulada correctamente. El inventario ha sido restablecido.',
          'exito'
        );
      },

      error: (error) => {
        const mensajeError =
          error.error?.message ||
          'No se pudo anular la venta.';

        this.mostrarMensaje(
          mensajeError,
          'error'
        );
      }

    });
  }

  get ventasFiltradas(): Venta[] {

    return this.ventas.filter(venta => {

      const busqueda = this.terminoBusqueda
        .toLowerCase()
        .trim();

      const coincideBusqueda =
        venta.numeroVenta?.toLowerCase().includes(busqueda) ||
        venta.filial?.nombre?.toLowerCase().includes(busqueda);

      const coincideEstado =
        this.filtroEstado === 'todos' ||
        venta.estado === this.filtroEstado;

      const coincideFilial =
        !this.filtroFilial ||
        venta.filial?.id === Number(this.filtroFilial);

      return coincideBusqueda &&
        coincideEstado &&
        coincideFilial;
    });
  }

  get totalVentas(): number {
    return this.ventas.length;
  }

  get ventasRegistradas(): number {
    return this.ventas.filter(
      venta => venta.estado === 'REGISTRADA'
    ).length;
  }

  get ventasAnuladas(): number {
    return this.ventas.filter(
      venta => venta.estado === 'ANULADA'
    ).length;
  }

  get importeVentasRegistradas(): number {
    return this.ventas
      .filter(venta => venta.estado === 'REGISTRADA')
      .reduce(
        (total, venta) => total + Number(venta.total || 0),
        0
      );
  }

  mostrarMensaje(
    mensaje: string,
    tipo: 'exito' | 'error'
  ): void {
    this.mensaje = mensaje;
    this.tipoMensaje = tipo;
  }

}
