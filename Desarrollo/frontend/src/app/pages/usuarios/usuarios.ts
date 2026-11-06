
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

interface Usuario {
  id?: number;
  nombre: string;
  correo: string;
  rol: string;
  estado: boolean;
}

interface UsuarioFormulario {
  nombre: string;
  correo: string;
  contrasena: string;
  rol: string;
  estado: boolean;
}

@Component({
  selector: 'app-usuarios',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './usuarios.html',
  styleUrl: './usuarios.css'
})
export class Usuarios implements OnInit {

  private readonly API_URL = 'http://localhost:8080/api/usuarios';

  usuarios: Usuario[] = [];

  cargando = false;
  guardando = false;

  mostrarFormulario = false;
  modoEdicion = false;

  usuarioSeleccionado: number | null = null;

  terminoBusqueda = '';
  filtroRol = '';
  filtroEstado = '';

  mensaje = '';
  tipoMensaje: 'exito' | 'error' = 'exito';

  formulario: UsuarioFormulario = this.formularioInicial();

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.listarUsuarios();
  }

  private formularioInicial(): UsuarioFormulario {
    return {
      nombre: '',
      correo: '',
      contrasena: '',
      rol: 'ANALISTA',
      estado: true
    };
  }

  // LISTAR USUARIOS

  listarUsuarios(): void {
    this.cargando = true;

    this.http.get<Usuario[]>(this.API_URL).subscribe({
      next: (data) => {
        this.usuarios = data || [];
        this.cargando = false;
      },
      error: (error) => {
        console.error('Error al listar usuarios:', error);
        this.mostrarMensaje(
          'No se pudieron cargar los usuarios.',
          'error'
        );
        this.cargando = false;
      }
    });
  }

  // FILTRAR USUARIOS

  get usuariosFiltrados(): Usuario[] {
    return this.usuarios.filter(usuario => {

      const busqueda = this.terminoBusqueda
        .toLowerCase()
        .trim();

      const coincideBusqueda =
        !busqueda ||
        usuario.nombre.toLowerCase().includes(busqueda) ||
        usuario.correo.toLowerCase().includes(busqueda);

      const coincideRol =
        !this.filtroRol ||
        usuario.rol === this.filtroRol;

      const coincideEstado =
        !this.filtroEstado ||
        (this.filtroEstado === 'ACTIVO' && usuario.estado) ||
        (this.filtroEstado === 'INACTIVO' && !usuario.estado);

      return coincideBusqueda &&
        coincideRol &&
        coincideEstado;
    });
  }

  // INDICADORES

  get totalUsuarios(): number {
    return this.usuarios.length;
  }

  get usuariosActivos(): number {
    return this.usuarios.filter(u => u.estado).length;
  }

  get usuariosInactivos(): number {
    return this.usuarios.filter(u => !u.estado).length;
  }

  get totalAdministradores(): number {
    return this.usuarios.filter(
      u => u.rol === 'ADMINISTRADOR'
    ).length;
  }

  // ABRIR FORMULARIO

  abrirFormulario(): void {
    this.modoEdicion = false;
    this.usuarioSeleccionado = null;
    this.formulario = this.formularioInicial();
    this.mostrarFormulario = true;
    this.mensaje = '';
  }

  // CERRAR FORMULARIO

  cerrarFormulario(): void {
    this.mostrarFormulario = false;
    this.modoEdicion = false;
    this.usuarioSeleccionado = null;
    this.formulario = this.formularioInicial();
  }

  // EDITAR USUARIO

  editarUsuario(usuario: Usuario): void {
    this.modoEdicion = true;
    this.usuarioSeleccionado = usuario.id ?? null;

    this.formulario = {
      nombre: usuario.nombre,
      correo: usuario.correo,
      contrasena: '',
      rol: usuario.rol,
      estado: usuario.estado
    };

    this.mostrarFormulario = true;
    this.mensaje = '';
  }

  // GUARDAR O ACTUALIZAR

  guardarUsuario(): void {

    if (
      !this.formulario.nombre.trim() ||
      !this.formulario.correo.trim() ||
      !this.formulario.rol
    ) {
      this.mostrarMensaje(
        'Completa todos los campos obligatorios.',
        'error'
      );
      return;
    }

    const correoValido =
      /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(
        this.formulario.correo.trim()
      );

    if (!correoValido) {
      this.mostrarMensaje(
        'Ingresa un correo electrónico válido.',
        'error'
      );
      return;
    }

    if (
      !this.modoEdicion &&
      !this.formulario.contrasena.trim()
    ) {
      this.mostrarMensaje(
        'La contraseña es obligatoria para registrar un usuario.',
        'error'
      );
      return;
    }

    const usuario: Record<string, unknown> = {
      nombre: this.formulario.nombre.trim(),
      correo: this.formulario.correo.trim(),
      rol: this.formulario.rol,
      estado: this.formulario.estado
    };

    if (this.formulario.contrasena.trim()) {
      usuario['contrasena'] = this.formulario.contrasena;
    }

    this.guardando = true;

    if (this.modoEdicion && this.usuarioSeleccionado !== null) {

      const id = this.usuarioSeleccionado;

      this.http.put<Usuario>(
        `${this.API_URL}/${id}`,
        usuario
      ).subscribe({
        next: () => {
          this.guardando = false;
          this.cerrarFormulario();
          this.mostrarMensaje(
            'Usuario actualizado correctamente.',
            'exito'
          );
          this.listarUsuarios();
        },
        error: (error) => {
          console.error('Error al actualizar usuario:', error);
          this.guardando = false;
          this.mostrarMensaje(
            'No se pudo actualizar el usuario.',
            'error'
          );
        }
      });

    } else {

      this.http.post<Usuario>(
        this.API_URL,
        usuario
      ).subscribe({
        next: () => {
          this.guardando = false;
          this.cerrarFormulario();
          this.mostrarMensaje(
            'Usuario registrado correctamente.',
            'exito'
          );
          this.listarUsuarios();
        },
        error: (error) => {
          console.error('Error al registrar usuario:', error);
          this.guardando = false;
          this.mostrarMensaje(
            'No se pudo registrar el usuario. Verifica que el correo no esté registrado.',
            'error'
          );
        }
      });

    }
  }

  // CAMBIAR ESTADO

  cambiarEstado(usuario: Usuario): void {

    if (!usuario.id) {
      return;
    }

    const usuarioActualizado = {
      nombre: usuario.nombre,
      correo: usuario.correo,
      rol: usuario.rol,
      estado: !usuario.estado
    };

    this.http.put<Usuario>(
      `${this.API_URL}/${usuario.id}`,
      usuarioActualizado
    ).subscribe({
      next: () => {
        this.mostrarMensaje(
          usuarioActualizado.estado
            ? 'Usuario activado correctamente.'
            : 'Usuario desactivado correctamente.',
          'exito'
        );
        this.listarUsuarios();
      },
      error: (error) => {
        console.error('Error al cambiar estado:', error);
        this.mostrarMensaje(
          'No se pudo cambiar el estado del usuario.',
          'error'
        );
      }
    });
  }

  // ELIMINAR USUARIO

  eliminarUsuario(usuario: Usuario): void {

    if (!usuario.id) {
      return;
    }

    const confirmar = confirm(
      `¿Estás seguro de eliminar al usuario "${usuario.nombre}"?`
    );

    if (!confirmar) {
      return;
    }

    this.http.delete<void>(
      `${this.API_URL}/${usuario.id}`
    ).subscribe({
      next: () => {
        this.mostrarMensaje(
          'Usuario eliminado correctamente.',
          'exito'
        );
        this.listarUsuarios();
      },
      error: (error) => {
        console.error('Error al eliminar usuario:', error);
        this.mostrarMensaje(
          'No se pudo eliminar el usuario.',
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
