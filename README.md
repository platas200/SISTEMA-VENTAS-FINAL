Aportaciones individuales

# Sistema de Ventas e Inventario 




- Aportaciones de Obed GR:
Se detalla las modificaciones y funcionalidades implementadas por Obed en el proyecto de Sistema de Ventas.

 Funcionalidades Implementadas:

 1. Gestión del Carrito de Ventas
   -Botón "Vaciar Carrito": Se añadió un botón funcional que permite eliminar todos los productos del carrito simultáneamente con una confirmación de seguridad para evitar errores accidentales.
   -Edición de Cantidad en Tiempo Real: Se habilitó la capacidad de modificar la cantidad de productos directamente en la tabla del carrito. 

 2. Buscadores Inteligentes (Filtros en Tiempo Real)
   -Buscador en Carrito: Los campos de selección de Clientes y Productos ahora actúan como buscadores dinámicos. Al escribir, filtran la lista desplegable en tiempo real para agilizar la selección.
   -Buscador en Inventario/Clientes: Se añadieron campos de búsqueda superiores en las pestañas de Productos y Clientes, permitiendo filtrar tablas extensas por ID o Nombre instantáneamente.

 Archivos Modificados
* `src/CONTROLLER/ControladorPrincipal.java`: Lógica de negocio de los nuevos botones y filtros.
* `src/VIEW/VistaPrincipal.java`: Actualización de la interfaz gráfica y componentes de tabla.

+Desarrollado para el proyecto final de Paradigmas De Programacion II.
