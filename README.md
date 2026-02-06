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

+Desarrollado para el proyecto final de Paradigmas De Programacion II.
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
* '
