
import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

interface Producto {
  id?: number;
  codigo: string;
  nombre: string;
  categoria: string;
  descripcion: string;
  paisOrigen: string;
  precioCompra: number;
  precioVenta: number;
  estado: boolean;
}

@Component({
  selector: 'app-productos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './productos.html',
  styleUrl: './productos.css'
})
export class Productos implements OnInit {

  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/productos';

  productos: Producto[] = [];
  productoSeleccionado: Producto = this.nuevoProducto();

  mostrarFormulario = false;
  modoEdicion = false;
  cargando = false;
  mensaje = '';
  tipoMensaje: 'exito' | 'error' = 'exito';
  terminoBusqueda = '';

  ngOnInit(): void {
    this.listarProductos();
  }

  nuevoProducto(): Producto {
    return {
      codigo: '',
      nombre: '',
      categoria: '',
      descripcion: '',
      paisOrigen: '',
      precioCompra: 0,
      precioVenta: 0,
      estado: true
    };
  }

  listarProductos(): void {
    this.cargando = true;

    this.http.get<Producto[]>(this.apiUrl).subscribe({
      next: (datos) => {
        this.productos = datos;
        this.cargando = false;
      },
      error: () => {
        this.cargando = false;
        this.mostrarMensaje(
          'No se pudieron cargar los productos. Verifica el backend.',
          'error'
        );
      }
    });
  }

  abrirFormulario(): void {
    this.productoSeleccionado = this.nuevoProducto();
    this.modoEdicion = false;
    this.mostrarFormulario = true;
    this.mensaje = '';
  }

  editarProducto(producto: Producto): void {
    this.productoSeleccionado = { ...producto };
    this.modoEdicion = true;
    this.mostrarFormulario = true;
    this.mensaje = '';
  }

  guardarProducto(): void {
    const datos = this.productoSeleccionado;

    if (
      !datos.codigo.trim() ||
      !datos.nombre.trim() ||
      !datos.categoria.trim() ||
      !datos.paisOrigen.trim() ||
      datos.precioCompra <= 0 ||
      datos.precioVenta <= 0
    ) {
      this.mostrarMensaje(
        'Completa los campos obligatorios y verifica los precios.',
        'error'
      );
      return;
    }

    if (datos.precioVenta < datos.precioCompra) {
      this.mostrarMensaje(
        'El precio de venta no debe ser inferior al precio de compra.',
        'error'
      );
      return;
    }

    this.cargando = true;

    const solicitud = this.modoEdicion
      ? this.http.put<Producto>(
          `${this.apiUrl}/${datos.id}`,
          datos
        )
      : this.http.post<Producto>(this.apiUrl, datos);

    solicitud.subscribe({
      next: () => {
        this.mostrarMensaje(
          this.modoEdicion
            ? 'Producto actualizado correctamente.'
            : 'Producto registrado correctamente.',
          'exito'
        );

        this.cerrarFormulario();
        this.listarProductos();
      },
      error: (error) => {
        this.cargando = false;
        this.mostrarMensaje(
          error.error?.mensaje ||
          'No se pudo guardar el producto. Verifica los datos.',
          'error'
        );
      }
    });
  }

  cambiarEstado(producto: Producto): void {
    if (!producto.id) return;

    const productoActualizado = {
      ...producto,
      estado: !producto.estado
    };

    this.http.put<Producto>(
      `${this.apiUrl}/${producto.id}`,
      productoActualizado
    ).subscribe({
      next: () => {
        this.mostrarMensaje(
          'Estado del producto actualizado correctamente.',
          'exito'
        );
        this.listarProductos();
      },
      error: () => {
        this.mostrarMensaje(
          'No se pudo actualizar el estado del producto.',
          'error'
        );
      }
    });
  }

  eliminarProducto(producto: Producto): void {
    if (!producto.id) return;

    const confirmar = confirm(
      `¿Estás seguro de eliminar el producto ${producto.nombre}?`
    );

    if (!confirmar) return;

    this.http.delete(`${this.apiUrl}/${producto.id}`).subscribe({
      next: () => {
        this.mostrarMensaje(
          'Producto eliminado correctamente.',
          'exito'
        );
        this.listarProductos();
      },
      error: () => {
        this.mostrarMensaje(
          'No se pudo eliminar el producto. Puede tener movimientos o ventas relacionados.',
          'error'
        );
      }
    });
  }

  get productosFiltrados(): Producto[] {
    const termino = this.terminoBusqueda.toLowerCase().trim();

    if (!termino) return this.productos;

    return this.productos.filter(producto =>
      producto.codigo.toLowerCase().includes(termino) ||
      producto.nombre.toLowerCase().includes(termino) ||
      producto.categoria.toLowerCase().includes(termino) ||
      producto.paisOrigen.toLowerCase().includes(termino)
    );
  }

  private mostrarMensaje(
    mensaje: string,
    tipo: 'exito' | 'error'
  ): void {
    this.mensaje = mensaje;
    this.tipoMensaje = tipo;
  }

  cerrarFormulario(): void {
    this.mostrarFormulario = false;
    this.productoSeleccionado = this.nuevoProducto();
    this.cargando = false;
  }
}
