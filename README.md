# SERF – Sistema Empresarial de Gestión de Reportes Financieros

Sistema web empresarial desarrollado para **FinanCorp S.A.**, orientado a centralizar la información de sus filiales y facilitar la gestión de productos, inventarios, ventas y reportes financieros corporativos.

## 1. Descripción del proyecto

FinanCorp S.A. es una corporación multinacional dedicada a la importación y comercialización de productos tecnológicos, con presencia en Latinoamérica y Europa.

SERF surge como una propuesta para solucionar las dificultades de integración de información entre sus diferentes filiales, permitiendo centralizar los registros empresariales y facilitar la elaboración de reportes financieros individuales y consolidados.

## 2. Objetivo general

Desarrollar una aplicación web que permita integrar y administrar la información de ventas e inventarios de las diferentes filiales de FinanCorp S.A., automatizando la generación de reportes financieros corporativos mediante la aplicación de principios SOLID y patrones de diseño creacionales y estructurales.

## 3. Funcionalidades del sistema

El alcance funcional contempla:

- Autenticación de usuarios.
- Administración de usuarios y filiales.
- Gestión de productos importados.
- Control de inventarios.
- Registro y consulta de ventas.
- Consolidación de información empresarial.
- Generación y organización de reportes financieros.
- Administración de plantillas de reportes.
- Configuración centralizada del sistema.
- Personalización de reportes mediante componentes reutilizables.

Las funcionalidades descritas corresponden al alcance definido en los requerimientos del proyecto. Su disponibilidad efectiva depende del estado de implementación de cada módulo.

## 4. Tecnologías utilizadas

### Backend
- Java 21.
- Spring Boot 4.1.2.
- Spring Data JPA.
- Spring Security.
- JSON Web Token (JWT).
- Maven.
- MySQL 8.

### Frontend
- Angular 21.
- TypeScript.
- RxJS.
- HTML y CSS.
- npm.

### Herramientas de desarrollo
- Visual Studio Code.
- Git y GitHub.
- StarUML.
- Postman.

## 5. Estructura del proyecto

```text
SERF-FinanCorp/
│
├── Analisis/
│   ├── Casos-de-Uso.md
│   ├── Descripcion-del-proyecto.md
│   ├── Patrones-de-Diseno.md
│   ├── Principios-SOLID.md
│   ├── Requerimientos-Funcionales.md
│   └── Requerimientos-No-Funcionales.md
│
├── Desarrollo/
│   ├── Backend/
│   │   └── serf/
│   │       ├── src/
│   │       │   ├── main/
│   │       │   │   ├── java/
│   │       │   │   └── resources/
│   │       │   └── test/
│   │       ├── pom.xml
│   │       └── mvnw
│   │
│   └── frontend/
│       ├── public/
│       ├── src/
│       ├── angular.json
│       └── package.json
│
├── Diagramas-UML/
│   └── SERF-Diagramas.mdj.mdj
│
├── .gitignore
└── README.md
```

## 6. Requisitos previos

Antes de ejecutar el proyecto, se requiere:

- Java JDK 21.
- Node.js compatible con Angular 21.
- npm 11.9.0.
- MySQL Server 8.
- Git.

## 7. Configuración de la base de datos

Crear la base de datos en MySQL:

```sql
CREATE DATABASE serf_db;
```

El backend utiliza las siguientes variables de entorno:

| Variable | Descripción |
|---|---|
| DB_PASSWORD | Contraseña del usuario de MySQL |
| JWT_SECRET | Clave secreta para la firma de tokens JWT |
| SERF_ADMIN_PASSWORD | Contraseña del administrador inicial |

El archivo `application.properties` utiliza estas variables para evitar almacenar credenciales directamente en el código fuente.

### Configuración en Windows PowerShell

Antes de iniciar el backend, configurar las variables en la terminal:

```powershell
$env:DB_PASSWORD="TU_PASSWORD_MYSQL"
$env:JWT_SECRET="TU_CLAVE_SECRETA_SEGURA"
$env:SERF_ADMIN_PASSWORD="TU_PASSWORD_ADMIN"
```

Reemplazar los valores de ejemplo por credenciales propias. No publicar estos valores en GitHub.

## 8. Ejecución del backend

Abrir una terminal y ubicarse en el directorio del backend:

```powershell
cd Desarrollo/Backend/serf
```

Configurar las variables de entorno indicadas anteriormente y ejecutar:

```powershell
.\mvnw.cmd spring-boot:run
```

El backend utiliza el puerto:

```text
http://localhost:8080
```

### Administrador inicial

Al iniciar la aplicación, se verifica si existe el usuario administrador.

Si todavía no está registrado, se crea automáticamente con los siguientes datos:

- Correo: `admin@financorp.com`
- Rol: `ADMINISTRADOR`
- Contraseña: definida mediante `SERF_ADMIN_PASSWORD`.

La contraseña se almacena utilizando el mecanismo de codificación configurado en Spring Security.

## 9. Ejecución del frontend

Abrir otra terminal y ubicarse en el directorio del frontend:

```powershell
cd Desarrollo/frontend
```

Instalar las dependencias:

```powershell
npm install
```

Iniciar el servidor de desarrollo:

```powershell
npm start
```

La aplicación estará disponible en:

```text
http://localhost:4200
```

## 10. Pruebas

### Backend

Desde el directorio `Desarrollo/Backend/serf`, ejecutar:

```powershell
.\mvnw.cmd test
```

El proyecto incluye pruebas relacionadas con los patrones de diseño implementados:

- Builder.
- Prototype.
- Decorator.
- Composite.
- Facade.

### Frontend

Desde el directorio `Desarrollo/frontend`, ejecutar:

```powershell
npm test
```

## 11. Principios SOLID y patrones de diseño

El proyecto contempla la aplicación de principios SOLID para favorecer la organización, mantenibilidad y reutilización del código.

También incorpora patrones de diseño creacionales y estructurales, entre ellos:

| Categoría | Patrón |
|---|---|
| Creacional | Builder |
| Creacional | Prototype |
| Estructural | Decorator |
| Estructural | Composite |
| Estructural | Facade |

Estos patrones se aplican principalmente a la construcción, organización y personalización de reportes.

## 12. Repositorio

Código fuente disponible en GitHub:

https://github.com/roy123000/SERF-FinanCorp

## 13. Autoría

**Proyecto:** Sistema Empresarial de Gestión de Reportes Financieros (SERF).

**Empresa de referencia:** FinanCorp S.A.

**Modalidad:** Proyecto académico.

---
