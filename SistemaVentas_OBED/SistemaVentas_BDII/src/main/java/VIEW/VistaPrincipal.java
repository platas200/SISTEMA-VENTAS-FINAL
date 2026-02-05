package VIEW;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VistaPrincipal extends JFrame {

    public JTabbedPane pestañas;
    public JTable tablaProductos, tablaClientes, tablaCarrito, tablaVentasHistorial, tablaDetallesVenta;
    public JButton btnCrearProd, btnActProd, btnEliProd, btnRefProd;
    public JButton btnCrearCli, btnActCli, btnEliCli, btnRefCli;
    public JButton btnAgregarCarrito, btnVender, btnEliminarCarrito;
    public JComboBox<String> comboClientes, comboProductos;
    public JTextField txtPrecioCarrito, txtCantidadCarrito;
    public JLabel lblTotalCarrito;
    public DefaultTableModel modeloCarrito;
    public JButton btnRefrescarVentas, btnEliminarVenta, btnVaciarCarrito;

    public VistaPrincipal() {
        setTitle("Sistema Profesional de Ventas - BDII (Admin)");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);

        UIManager.put("TabbedPane.selected", new Color(245,245,245));

        pestañas = new JTabbedPane();
        pestañas.setFont(new Font("Arial", Font.BOLD, 14));

        pestañas.addTab("Productos", panelGenerico("Productos",
                tablaProductos = new JTable(),
                btnCrearProd = new JButton("Crear Producto"),
                btnActProd = new JButton("Actualizar Producto"),
                btnEliProd = new JButton("Eliminar Producto"),
                btnRefProd = new JButton("Refrescar Tabla")));

        pestañas.addTab("Clientes", panelGenerico("Clientes",
                tablaClientes = new JTable(),
                btnCrearCli = new JButton("Crear Cliente"),
                btnActCli = new JButton("Actualizar Cliente"),
                btnEliCli = new JButton("Eliminar Cliente"),
                btnRefCli = new JButton("Refrescar Tabla")));

        pestañas.addTab("Carrito", panelVentas());
        pestañas.addTab("Ventas", panelHistorial());

        add(pestañas);
    }

    // ---------------- PANEL GENERICO (PRODUCTOS / CLIENTES) ----------------
    private JPanel panelGenerico(String titulo, JTable tabla, JButton... botones) {
        JPanel panel = new JPanel(new BorderLayout(15,15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        // TÍTULO CON FONDO VINO
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBackground(new Color(139, 0, 0));
        lblTitulo.setOpaque(true);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JPanel pnlTitulo = new JPanel(new BorderLayout());
        pnlTitulo.add(lblTitulo, BorderLayout.WEST);
        pnlTitulo.setBorder(BorderFactory.createLineBorder(new Color(139,0,0), 2));
        pnlTitulo.setBackground(Color.WHITE);

        // TABLA
        Estilos.aplicarEstiloTabla(tabla);
        JScrollPane sp = new JScrollPane(tabla);
        sp.setBorder(BorderFactory.createLineBorder(new Color(139,0,0), 2));

        // BOTONES ABAJO (HORIZONTALES)
        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnlBotones.setBackground(Color.WHITE);

        for (JButton b : botones) {
            Estilos.aplicarEstiloBoton(b);
            pnlBotones.add(b);
        }

        panel.add(pnlTitulo, BorderLayout.NORTH);
        panel.add(sp, BorderLayout.CENTER);
        panel.add(pnlBotones, BorderLayout.SOUTH);

        return panel;
    }

    // ---------------- PANEL CARRITO ----------------
    private JPanel panelVentas() {
        JPanel panel = new JPanel(new BorderLayout(10,10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JPanel pnlNorte = new JPanel(new FlowLayout(FlowLayout.LEFT,15,10));
        pnlNorte.setBorder(BorderFactory.createTitledBorder("Nueva Venta"));
        pnlNorte.setBackground(new Color(245,245,245));

        comboClientes = new JComboBox<>();
        comboProductos = new JComboBox<>();
        comboClientes.setEditable(true);
        comboProductos.setEditable(true);
        txtPrecioCarrito = new JTextField(7);
        txtPrecioCarrito.setEditable(false);
        txtCantidadCarrito = new JTextField(5);
        btnAgregarCarrito = new JButton("Agregar");
        Estilos.aplicarEstiloBoton(btnAgregarCarrito);

        pnlNorte.add(new JLabel("Cliente:")); pnlNorte.add(comboClientes);
        pnlNorte.add(new JLabel("Producto:")); pnlNorte.add(comboProductos);
        pnlNorte.add(new JLabel("Precio:")); pnlNorte.add(txtPrecioCarrito);
        pnlNorte.add(new JLabel("Cant:")); pnlNorte.add(txtCantidadCarrito);
        pnlNorte.add(btnAgregarCarrito);

        String[] col = {"ID","Nombre","Precio","Cant","Subtotal"};
        modeloCarrito = new DefaultTableModel(null, col);
        tablaCarrito = new JTable(modeloCarrito);
        Estilos.aplicarEstiloTabla(tablaCarrito);

        lblTotalCarrito = new JLabel("TOTAL: $0.00");
        lblTotalCarrito.setFont(new Font("Arial", Font.BOLD, 20));
        lblTotalCarrito.setForeground(new Color(139,0,0));

        btnVender = new JButton("Finalizar Venta");
        btnEliminarCarrito = new JButton("Eliminar seleccionado");
        btnVaciarCarrito = new JButton("Vaciar Carrito");
        Estilos.aplicarEstiloBoton(btnVender);
        Estilos.aplicarEstiloBoton(btnEliminarCarrito);
        Estilos.aplicarEstiloBoton(btnVaciarCarrito);

        JPanel pnlSur = new JPanel(new BorderLayout());
        pnlSur.setBackground(Color.WHITE);
        pnlSur.add(lblTotalCarrito, BorderLayout.WEST);

        JPanel pnlBtns = new JPanel();
        pnlBtns.add(btnEliminarCarrito);
        pnlBtns.add(btnVaciarCarrito);	
        pnlBtns.add(btnVender);

        pnlSur.add(pnlBtns, BorderLayout.EAST);

        panel.add(pnlNorte, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaCarrito), BorderLayout.CENTER);
        panel.add(pnlSur, BorderLayout.SOUTH);

        return panel;
    }

    // ---------------- PANEL HISTORIAL ----------------
    private JPanel panelHistorial() {
        JPanel panel = new JPanel(new BorderLayout(10,10));
        panel.setBackground(Color.WHITE);

        tablaVentasHistorial = new JTable();
        tablaDetallesVenta = new JTable();
        Estilos.aplicarEstiloTabla(tablaVentasHistorial);
        Estilos.aplicarEstiloTabla(tablaDetallesVenta);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(tablaVentasHistorial),
                new JScrollPane(tablaDetallesVenta));
        split.setDividerLocation(250);
        split.setBorder(BorderFactory.createLineBorder(new Color(139,0,0),2));

        btnRefrescarVentas = new JButton("Refrescar");
        btnEliminarVenta = new JButton("Eliminar Venta");
        Estilos.aplicarEstiloBoton(btnRefrescarVentas);
        Estilos.aplicarEstiloBoton(btnEliminarVenta);

        JPanel pnlB = new JPanel();
        pnlB.add(btnRefrescarVentas);
        pnlB.add(btnEliminarVenta);

        panel.add(split, BorderLayout.CENTER);
        panel.add(pnlB, BorderLayout.SOUTH);

        return panel;
    }
}