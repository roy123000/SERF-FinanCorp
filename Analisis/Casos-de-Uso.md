
# Casos de uso del sistema SERF

## 1. Introducción

Los casos de uso permiten identificar las interacciones entre los usuarios y el Sistema Empresarial de Gestión de Reportes Financieros (SERF).

Mediante estos casos se describen las principales operaciones que estarán disponibles en la aplicación.

## 2. Actores del sistema

### 2.1. Administrador

Responsable de administrar los usuarios, registrar las filiales y mantener la configuración general del sistema.

### 2.2. Encargado de filial

Responsable de registrar productos, controlar inventarios y gestionar las ventas correspondientes a su filial.

### 2.3. Gerencia corporativa

Responsable de consultar información financiera consolidada y supervisar los resultados de las diferentes filiales.

### 2.4. Analista financiero

Responsable de elaborar, personalizar y generar reportes financieros corporativos.

## 3. Casos de uso por actor

### 3.1. Administrador

- Iniciar sesión.
- Gestionar usuarios.
- Registrar filiales.
- Modificar información de filiales.
- Administrar configuraciones generales.

### 3.2. Encargado de filial

- Iniciar sesión.
- Registrar productos.
- Consultar productos.
- Registrar entradas de inventario.
- Registrar salidas de inventario.
- Registrar ventas.
- Consultar ventas de su filial.

### 3.3. Gerencia corporativa

- Iniciar sesión.
- Consultar ventas consolidadas.
- Consultar inventarios por filial.
- Consultar indicadores financieros.
- Consultar reportes corporativos.

### 3.4. Analista financiero

- Iniciar sesión.
- Seleccionar plantillas de reportes.
- Crear reportes financieros.
- Agregar secciones y subsecciones.
- Incorporar tablas y gráficos.
- Personalizar reportes.
- Generar reportes.
- Exportar reportes en PDF.

## 4. Relaciones entre casos de uso

### 4.1. Registro de ventas

El registro de ventas incluye la validación de disponibilidad del inventario.

### 4.2. Generación de reportes consolidados

La generación de reportes consolidados incluye la recopilación de información de las diferentes filiales.

### 4.3. Exportación de reportes

La exportación requiere que previamente se haya generado el reporte financiero.

### 4.4. Personalización de reportes

La personalización podrá extender la generación de reportes mediante la incorporación de elementos adicionales.

## 5. Reglas generales de acceso

- El administrador podrá gestionar las configuraciones generales.
- El encargado de filial solo podrá administrar las operaciones autorizadas de su filial.
- La gerencia corporativa podrá consultar información consolidada.
- El analista financiero podrá crear y generar reportes según sus permisos.

## 6. Resultado esperado

La identificación de los actores y casos de uso permitirá diseñar una aplicación organizada, con funcionalidades definidas según las responsabilidades de cada usuario.
