package CONTROLLER;

import MODEL.*;
import VIEW.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ControladorPrincipal implements ActionListener {

    private VistaPrincipal vista;
    private ConsultasProducto modeloProducto;
    private ConsultasCliente modeloCliente;
    private ConsultasVenta modeloVenta;
    private String rolUsuario;

    public ControladorPrincipal(VistaPrincipal vista, String rol) {
        this.vista = vista;
        this.rolUsuario = rol;
        this.modeloProducto = new ConsultasProducto();
        this.modeloCliente = new ConsultasCliente();
        this.modeloVenta = new ConsultasVenta();

        inicializarEventos();
        actualizarTodo();
        aplicarPermisos();
        configurarBuscadores();
    }

    private void inicializarEventos() {
        vista.btnCrearProd.addActionListener(this);
        vista.btnActProd.addActionListener(this);
        vista.btnEliProd.addActionListener(this);
        vista.btnRefProd.addActionListener(e -> vista.tablaProductos.setModel(modeloProducto.leer()));

        vista.btnCrearCli.addActionListener(this);
        vista.btnActCli.addActionListener(this);
        vista.btnEliCli.addActionListener(this);
        vista.btnRefCli.addActionListener(e -> vista.tablaClientes.setModel(modeloCliente.leer()));

        vista.btnAgregarCarrito.addActionListener(this);
        vista.btnVender.addActionListener(this);
        vista.btnEliminarCarrito.addActionListener(this);
        
        vista.btnVaciarCarrito.addActionListener(e -> {
            if (vista.modeloCarrito.getRowCount() > 0) {
            	Object[] opciones = {"Sí", "No"};	
            	int respuesta = JOptionPane.showOptionDialog(vista, 
                        "¿Estás seguro de que deseas eliminar todos los productos del carrito?", 
                        "Vaciar Carrito", 
                        JOptionPane.YES_NO_OPTION, 
                        JOptionPane.QUESTION_MESSAGE, 
                        null, 
                        opciones, 
                        opciones[0]);
                if (respuesta == JOptionPane.YES_OPTION) {
                    vista.modeloCarrito.setRowCount(0); // Esto limpia la tabla
                    recalcularTotal(); // Esto pone el label en $0.00
                }
            } else {
                JOptionPane.showMessageDialog(vista, "El carrito ya está vacío.");
            }
        });
        vista.comboProductos.addActionListener(e -> {
            String item = (String) vista.comboProductos.getSelectedItem();
            if (item != null) {
                String id = item.split(" - ")[0];
                vista.txtPrecioCarrito.setText(String.valueOf(modeloProducto.getPrecio(id)));
            }
        });

        vista.btnRefrescarVentas.addActionListener(e -> vista.tablaVentasHistorial.setModel(modeloVenta.leerVentas()));
        vista.btnEliminarVenta.addActionListener(this);
        
        vista.tablaVentasHistorial.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = vista.tablaVentasHistorial.getSelectedRow();
                if (fila >= 0) {
                    String idVenta = vista.tablaVentasHistorial.getValueAt(fila, 0).toString();
                    vista.tablaDetallesVenta.setModel(modeloVenta.leerDetalles(idVenta));
                }
            }
        
        });
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object f = e.getSource();
        if (f == vista.btnCrearProd) crearProducto();
        else if (f == vista.btnActProd) actualizarProducto();
        else if (f == vista.btnEliProd) eliminarProducto();
        else if (f == vista.btnCrearCli) crearCliente();
        else if (f == vista.btnActCli) actualizarCliente();
        else if (f == vista.btnEliCli) eliminarCliente();
        else if (f == vista.btnAgregarCarrito) agregarAlCarrito();
        else if (f == vista.btnEliminarCarrito) quitarDelCarrito();
        else if (f == vista.btnVender) finalizarVenta();
        else if (f == vista.btnEliminarVenta) eliminarVenta();
    }

    // ============================================================
    // VALIDACIONES Y CRUD PRODUCTOS
    // ============================================================
    private void crearProducto() {
        try {
            String id = JOptionPane.showInputDialog(vista, "ID del Producto:");
            if (id == null || id.trim().isEmpty()) return;

            String nom = JOptionPane.showInputDialog(vista, "Nombre:");
            if (nom == null || nom.trim().isEmpty()) return;

            String des = JOptionPane.showInputDialog(vista, "Descripción:");
            
            String resPrecio = JOptionPane.showInputDialog(vista, "Precio:");
            if (!esNumeroDouble(resPrecio)) {
                JOptionPane.showMessageDialog(vista, "Error: El precio debe ser un número válido (ej: 10.50)");
                return;
            }
            double pre = Double.parseDouble(resPrecio);

            String resStock = JOptionPane.showInputDialog(vista, "Stock Inicial:");
            if (!esNumeroEntero(resStock)) {
                JOptionPane.showMessageDialog(vista, "Error: El stock debe ser un número entero.");
                return;
            }
            int sto = Integer.parseInt(resStock);

            if (modeloProducto.crear(id, nom, des, pre, sto)) {
                actualizarTodo();
                JOptionPane.showMessageDialog(vista, "Producto registrado.");
            }
        } catch (Exception ex) { JOptionPane.showMessageDialog(vista, "Error inesperado."); }
    }

    private void actualizarProducto() {
        int fila = vista.tablaProductos.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(vista, "Seleccione una fila."); return; }
        
        try {
            String id = vista.tablaProductos.getValueAt(fila, 0).toString();
            String nom = JOptionPane.showInputDialog(vista, "Nuevo Nombre:", vista.tablaProductos.getValueAt(fila, 1));
            String des = JOptionPane.showInputDialog(vista, "Nueva Descripción:", vista.tablaProductos.getValueAt(fila, 2));
            
            String resPre = JOptionPane.showInputDialog(vista, "Nuevo Precio:", vista.tablaProductos.getValueAt(fila, 3));
            if (!esNumeroDouble(resPre)) return;
            
            String resSto = JOptionPane.showInputDialog(vista, "Nuevo Stock:", vista.tablaProductos.getValueAt(fila, 4));
            if (!esNumeroEntero(resSto)) return;

            if (modeloProducto.actualizar(id, nom, des, Double.parseDouble(resPre), Integer.parseInt(resSto))) {
                actualizarTodo();
            }
        } catch (Exception ex) { }
    }

    // ============================================================
    // VALIDACIONES Y CRUD CLIENTES
    // ============================================================
    private void crearCliente() {
        String id = JOptionPane.showInputDialog(vista, "ID Cliente:");
        if (id == null || id.isEmpty()) return;

        String nom = JOptionPane.showInputDialog(vista, "Nombre:");
        String ape = JOptionPane.showInputDialog(vista, "Apellido:");
        
        String tel = JOptionPane.showInputDialog(vista, "Teléfono (10 dígitos):");
        if (!validarTelefono(tel)) {
            JOptionPane.showMessageDialog(vista, "Error: El teléfono debe tener exactamente 10 números y no contener letras.");
            return;
        }

        String dir = JOptionPane.showInputDialog(vista, "Dirección:");
        if (modeloCliente.crear(id, nom, ape, tel, dir)) {
            actualizarTodo();
            JOptionPane.showMessageDialog(vista, "Cliente guardado.");
        }
    }

    private void actualizarCliente() {
        int fila = vista.tablaClientes.getSelectedRow();
        if (fila < 0) return;
        
        String id = vista.tablaClientes.getValueAt(fila, 0).toString();
        String nom = JOptionPane.showInputDialog(vista, "Nombre:", vista.tablaClientes.getValueAt(fila, 1));
        String ape = JOptionPane.showInputDialog(vista, "Apellido:", vista.tablaClientes.getValueAt(fila, 2));
        
        String tel = JOptionPane.showInputDialog(vista, "Teléfono:", vista.tablaClientes.getValueAt(fila, 3));
        if (!validarTelefono(tel)) {
            JOptionPane.showMessageDialog(vista, "Teléfono inválido. No se actualizó.");
            return;
        }

        String dir = JOptionPane.showInputDialog(vista, "Dirección:", vista.tablaClientes.getValueAt(fila, 4));
        if (modeloCliente.actualizar(id, nom, ape, tel, dir)) actualizarTodo();
    }

    // ============================================================
    // MÉTODOS DE VALIDACIÓN 
    // ============================================================
    
    private boolean esNumeroDouble(String str) {
        if (str == null) return false;
        try {
            double d = Double.parseDouble(str);
            return d >= 0;
        } catch (NumberFormatException e) { return false; }
    }

    private boolean esNumeroEntero(String str) {
        if (str == null) return false;
        try {
            int i = Integer.parseInt(str);
            return i >= 0;
        } catch (NumberFormatException e) { return false; }
    }

    private boolean validarTelefono(String tel) {
        // Verifica que no sea nulo, que tenga 10 dígitos y que sean solo números
        return tel != null && tel.matches("\\d{10}");
    }

    // ============================================================
    // RESTO DE LÓGICA (VENTAS / CARRITO / PERMISOS)
    // ============================================================
    
    private void eliminarProducto() {
        int f = vista.tablaProductos.getSelectedRow();
        if (f >= 0 && JOptionPane.showConfirmDialog(vista, "¿Borrar?") == 0) {
            if (modeloProducto.borrar(vista.tablaProductos.getValueAt(f, 0).toString())) actualizarTodo();
        }
    }

    private void eliminarCliente() {
        int f = vista.tablaClientes.getSelectedRow();
        if (f >= 0 && JOptionPane.showConfirmDialog(vista, "¿Borrar cliente?") == 0) {
            if (modeloCliente.borrar(vista.tablaClientes.getValueAt(f, 0).toString())) actualizarTodo();
        }
    }

    private void agregarAlCarrito() {
        try {
            String item = (String) vista.comboProductos.getSelectedItem();
            String id = item.split(" - ")[0];
            String nom = item.split(" - ")[1];
            
            if (!esNumeroDouble(vista.txtPrecioCarrito.getText()) || !esNumeroEntero(vista.txtCantidadCarrito.getText())) {
                JOptionPane.showMessageDialog(vista, "Precio o cantidad inválidos.");
                return;
            }

            double pre = Double.parseDouble(vista.txtPrecioCarrito.getText());
            int cant = Integer.parseInt(vista.txtCantidadCarrito.getText());

            if (cant <= modeloProducto.getStock(id)) {
                vista.modeloCarrito.addRow(new Object[]{id, nom, pre, cant, (pre * cant)});
                recalcularTotal();
            } else {
                JOptionPane.showMessageDialog(vista, "Stock insuficiente.");
            }
        } catch (Exception ex) { JOptionPane.showMessageDialog(vista, "Error al añadir."); }
    }

    private void quitarDelCarrito() {
        int f = vista.tablaCarrito.getSelectedRow();
        if (f >= 0) {
            vista.modeloCarrito.removeRow(f);
            recalcularTotal();
        }
    }
    private void configurarBuscadores() {
        // --- BUSCADOR DE CLIENTES ---
        JTextField editorCliente = (JTextField) vista.comboClientes.getEditor().getEditorComponent();
        editorCliente.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                // No filtrar si son teclas de navegación (flechas, enter, etc)
                if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
                    return;
                }
                
                String filtro = editorCliente.getText().toLowerCase();
                DefaultComboBoxModel<String> modelo = new DefaultComboBoxModel<>();
                
                for (String c : modeloCliente.obtenerListaClientes()) {
                    if (c.toLowerCase().contains(filtro)) {
                        modelo.addElement(c);
                    }
                }
                vista.comboClientes.setModel(modelo);
                editorCliente.setText(filtro); // Mantiene lo que el usuario está escribiendo
                if (modelo.getSize() > 0) vista.comboClientes.showPopup();
            }
        });

        // --- BUSCADOR DE PRODUCTOS ---
        JTextField editorProducto = (JTextField) vista.comboProductos.getEditor().getEditorComponent();
        editorProducto.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
                    return;
                }
                
                String filtro = editorProducto.getText().toLowerCase();
                DefaultComboBoxModel<String> modelo = new DefaultComboBoxModel<>();
                
                for (String p : modeloProducto.obtenerListaProductos()) {
                    if (p.toLowerCase().contains(filtro)) {
                        modelo.addElement(p);
                    }
                }
                vista.comboProductos.setModel(modelo);
                editorProducto.setText(filtro);
                if (modelo.getSize() > 0) vista.comboProductos.showPopup();
            }
        });
    }

    private void finalizarVenta() {
        if (vista.modeloCarrito.getRowCount() == 0) return;
        String idCli = ((String) vista.comboClientes.getSelectedItem()).split(" - ")[0];
        List<String> ids = new ArrayList<>();
        List<Integer> cants = new ArrayList<>();
        for (int i = 0; i < vista.modeloCarrito.getRowCount(); i++) {
            ids.add(vista.modeloCarrito.getValueAt(i, 0).toString());
            cants.add(Integer.parseInt(vista.modeloCarrito.getValueAt(i, 3).toString()));
        }
        if (modeloVenta.realizarVenta(idCli, ids, cants).equals("OK")) {
            JOptionPane.showMessageDialog(vista, "¡Venta Exitosa!");
            vista.modeloCarrito.setRowCount(0);
            recalcularTotal();
            actualizarTodo();
        }
    }

    private void eliminarVenta() {
        int f = vista.tablaVentasHistorial.getSelectedRow();
        if (f >= 0) {
            String id = vista.tablaVentasHistorial.getValueAt(f, 0).toString();
            if (modeloVenta.eliminarVenta(id)) {
                actualizarTodo();
                vista.tablaDetallesVenta.setModel(new DefaultTableModel());
            }
        }
    }

    private void recalcularTotal() {
        double total = 0;
        for (int i = 0; i < vista.modeloCarrito.getRowCount(); i++) {
            total += Double.parseDouble(vista.modeloCarrito.getValueAt(i, 4).toString());
        }
        vista.lblTotalCarrito.setText("TOTAL: $" + total);
    }

    private void actualizarTodo() {
        vista.tablaProductos.setModel(modeloProducto.leer());
        vista.tablaClientes.setModel(modeloCliente.leer());
        vista.tablaVentasHistorial.setModel(modeloVenta.leerVentas());
        vista.comboClientes.removeAllItems();
        modeloCliente.obtenerListaClientes().forEach(vista.comboClientes::addItem);
        vista.comboProductos.removeAllItems();
        modeloProducto.obtenerListaProductos().forEach(vista.comboProductos::addItem);
    }

    private void aplicarPermisos() {
        if (rolUsuario != null && rolUsuario.equalsIgnoreCase("Vendedor")) {
            vista.pestañas.setEnabledAt(0, false);
            vista.pestañas.setEnabledAt(1, false);
            vista.pestañas.setSelectedIndex(2);
        }
    }
}