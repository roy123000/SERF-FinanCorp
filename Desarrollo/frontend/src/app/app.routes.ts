
import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () =>
      import('./pages/login/login').then(m => m.Login)
  },
  {
    path: 'dashboard',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./pages/dashboard/dashboard').then(m => m.Dashboard)
  },
  {
    path: 'filiales',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./pages/filiales/filiales').then(m => m.Filiales)
  },
  {
    path: 'productos',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./pages/productos/productos').then(m => m.Productos)
  },
  {
    path: 'inventarios',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./pages/inventarios/inventarios').then(m => m.Inventarios)
  },
  {
    path: 'ventas',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./pages/ventas/ventas').then(m => m.Ventas)
  },
  {
    path: 'reportes',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./pages/reportes/reportes').then(m => m.Reportes)
  },
  {
    path: 'plantillas',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./pages/plantillas/plantillas').then(m => m.Plantillas)
  },
  {
    path: 'usuarios',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./pages/usuarios/usuarios').then(m => m.Usuarios)
  },
  {
    path: 'configuracion',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./pages/configuracion/configuracion').then(m => m.Configuracion)
  },
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },
  {
    path: '**',
    redirectTo: 'login'
  }
];
