# Sistema de Ventas e Inventario 

Aportaciones individuales

- Aportaciones de Obed Yasir Guzmán Robles:
  
 1. Gestión del Carrito de Ventas.
   -Botón "Vaciar Carrito": Se añadió un botón funcional que permite eliminar todos los productos del carrito simultáneamente con una confirmación de seguridad para evitar errores accidentales.
   -Edición de Cantidad en Tiempo Real: Se habilitó la capacidad de modificar la cantidad de productos directamente en la tabla del carrito. 

 2. Buscadores Inteligentes (Filtros en Tiempo Real).
   -Buscador en Carrito: Los campos de selección de Clientes y Productos ahora actúan como buscadores dinámicos. Al escribir, filtran la lista desplegable en tiempo real para agilizar la selección.
   -Buscador en Inventario/Clientes: Se añadieron campos de búsqueda superiores en las pestañas de Productos y Clientes, permitiendo filtrar tablas extensas por ID o Nombre instantáneamente.

 Archivos Modificados
* `src/CONTROLLER/ControladorPrincipal.java`: Lógica de negocio de los nuevos botones y filtros.
* `src/VIEW/VistaPrincipal.java`: Actualización de la interfaz gráfica y componentes de tabla.
*Fin aportaciones Obed*

- Aportaciones de Marcela Platas Revuelta:

  1. Buscadores Inteligentes (Filtros en Tiempo Real).
    - Buscador en ventana HistorialVentas: Permite buscar una venta por fecha, id y nombre de producto vendido.

  2. Impresión de PDF.
    - Botón "Imprimir PDF": Permite imprimir en PDF los detalles de una venta seleccionada.

  3. Gestión del Carrito de Ventas.
    - Edición de Cantidad en Tiempo Real: Se habilitó la capacidad de modificar la cantidad de productos directamente en la tabla del carrito mediante botones + y -. (FUNCIONAL)

  4. Gestión del Login.
    - Botón "Cerrar Sesión": Permite desde cualquier ventana del sistema cerrar sesión inmediata para entrar como otro usuario.
  
Archivos modificados:
* `src/CONTROLLER/ControladorPrincipal.java`: Lógica de los nuevos botones y filtros.
* `src/VIEW/VistaPrincipal.java`: Actualización de la interfaz gráfica y componentes de tabla.
*Fin aportaciones Marcela*

- Aportaciones de Candy Mariela Toral Sosa:

Buscadores Inteligentes (Filtros en Tiempo Real).
Se implementaron funcionalidades que permiten realizar búsquedas dinámicas dentro del sistema, mejorando la interacción del usuario y facilitando la localización de información dentro de tablas con gran cantidad de registros. Estos filtros funcionan en tiempo real, es decir, conforme el usuario escribe en los campos de búsqueda, los datos se actualizan automáticamente sin necesidad de presionar botones adicionales.

1. Buscador en Producto:
Se desarrolló un sistema de filtrado para la tabla de productos que permite localizar registros utilizando múltiples criterios de búsqueda simultáneamente. El usuario puede buscar productos mediante el ID, el Nombre o la Descripción del producto.
Este buscador mejora la eficiencia en la gestión del inventario, ya que permite identificar productos de forma rápida y precisa, especialmente cuando existen muchos registros almacenados.

2. Buscador en Clientes:
Se implementó un filtro dinámico en la tabla de clientes que permite buscar registros utilizando el ID del cliente o su Nombre.
Esta funcionalidad facilita la localización de clientes dentro del sistema, optimizando los procesos de venta y administración, ya que reduce el tiempo necesario para encontrar información específica.

Archivos modificados:
* `src/CONTROLLER/ControladorPrincipal.java`: Lógica de los nuevos filtros.
* `src/VIEW/VistaPrincipal.java`: Actualización de la interfaz gráfica y componentes de tabla.
*Fin aportaciones Candy Mariela*
    


