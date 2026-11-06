
# Patrones de diseño aplicados al sistema SERF

## 1. Introducción

El sistema SERF utilizará patrones de diseño creacionales y estructurales para organizar la creación de objetos, facilitar la reutilización de componentes y mejorar el mantenimiento de la aplicación.

La selección de estos patrones responde a los requisitos establecidos en el caso de FinanCorp S.A. y a los criterios de evaluación de la asignatura.

## 2. Patrones creacionales

### 2.1. Singleton

Se utilizará para administrar la configuración centralizada de la aplicación.

Permitirá mantener una única instancia de configuración corporativa, accesible desde los diferentes módulos del sistema.

### 2.2. Builder

Se utilizará para construir reportes financieros de manera progresiva.

Permitirá agregar elementos como títulos, secciones, tablas, gráficos y datos financieros antes de generar el reporte final.

Se implementará mediante una API fluida que facilite la construcción de objetos complejos.

### 2.3. Prototype

Se utilizará para clonar plantillas de reportes existentes.

Permitirá crear nuevas versiones de reportes para diferentes filiales sin modificar la plantilla original.

La clonación deberá generar copias independientes de los objetos internos cuando corresponda.

### 2.4. Factory Method

Se utilizará como patrón complementario para seleccionar y crear diferentes tipos de reportes según las necesidades de cada filial o país.

Permitirá separar la lógica de creación de los reportes de su utilización.

## 3. Patrones estructurales

### 3.1. Composite

Se utilizará para organizar los reportes mediante una estructura jerárquica.

Permitirá representar reportes completos, secciones y subsecciones mediante una estructura de árbol.

Los elementos individuales y los grupos de elementos podrán tratarse mediante una interfaz común.

### 3.2. Decorator

Se utilizará para incorporar funcionalidades adicionales a los reportes sin modificar su estructura original.

Permitirá agregar elementos como encabezados corporativos, gráficos, información complementaria y otros componentes de presentación.

### 3.3. Facade

Se utilizará para simplificar el proceso de generación de reportes.

Proporcionará una interfaz unificada que coordinará los diferentes componentes internos necesarios para recopilar información, consolidar datos y generar el reporte final.

## 4. Relación con los requisitos del sistema

| Requisito | Patrón |
| Configuración centralizada | Singleton |
| Reutilización de plantillas | Prototype y Factory Method |
| Construcción progresiva de reportes | Builder |
| Generación simplificada | Facade |
| Organización jerárquica | Composite |
| Funcionalidades adicionales | Decorator |

## 5. Beneficios esperados

- Mayor reutilización del código.
- Menor acoplamiento entre componentes.
- Mejor organización de las responsabilidades.
- Facilidad para incorporar nuevas filiales.
- Construcción flexible de reportes financieros.
- Mayor facilidad de mantenimiento y ampliación del sistema.

## 6. Conclusión

La aplicación de patrones creacionales y estructurales permitirá desarrollar SERF mediante componentes reutilizables y organizados, facilitando la generación de reportes financieros corporativos y la adaptación del sistema a las necesidades de FinanCorp S.A.
