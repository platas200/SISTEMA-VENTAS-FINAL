package VIEW;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class VistaLogin extends JDialog {
    public JTextField txtUsuario;
    public JPasswordField txtPassword;
    public JComboBox<String> cbxRol;
    public JButton btnEntrar;
    private boolean autenticado = false;
    private String rolUsuario = "";

    public VistaLogin() {
        setTitle("Acceso al Sistema");
        setSize(360, 360);
        setModal(true);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel pnlPrincipal = new JPanel(new BorderLayout());
        pnlPrincipal.setBackground(Color.WHITE);
        pnlPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("BIENVENIDO", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(120, 0, 0));
        pnlPrincipal.add(lblTitulo, BorderLayout.NORTH);

        JPanel pnlCampos = new JPanel();
        pnlCampos.setLayout(new BoxLayout(pnlCampos, BoxLayout.Y_AXIS));
        pnlCampos.setBackground(Color.WHITE);
        pnlCampos.setAlignmentX(Component.CENTER_ALIGNMENT);

        Font fuenteLabel = new Font("Segoe UI", Font.BOLD, 15);
        Color colorTexto = new Color(60, 60, 60);

        // Rol de Usuario
        pnlCampos.add(crearEtiqueta("Rol de Usuario:", fuenteLabel, colorTexto));
        pnlCampos.add(Box.createVerticalStrut(6)); // Espacio entre etiqueta y combo
        cbxRol = new JComboBox<>(new String[]{"Admin", "Vendedor"});
        cbxRol.setMaximumSize(new Dimension(120, 35)); // Más corto
        cbxRol.setAlignmentX(Component.CENTER_ALIGNMENT);
        cbxRol.setBackground(Color.WHITE);
        cbxRol.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbxRol.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(0, 10, 0, 10)
        ));
        pnlCampos.add(cbxRol);
        pnlCampos.add(Box.createVerticalStrut(12));

        // Usuario
        pnlCampos.add(crearEtiqueta("Usuario:", fuenteLabel, colorTexto));
        txtUsuario = new JTextField();
        estilizarComponente(txtUsuario);
        pnlCampos.add(txtUsuario);
        pnlCampos.add(Box.createVerticalStrut(12));

        // Contraseña
        pnlCampos.add(crearEtiqueta("Contraseña:", fuenteLabel, colorTexto));
        txtPassword = new JPasswordField();
        estilizarComponente(txtPassword);
        pnlCampos.add(txtPassword);
        pnlCampos.add(Box.createVerticalStrut(20));

        // Botón
        btnEntrar = new JButton("INICIAR SESIÓN");
        btnEntrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnEntrar.setPreferredSize(new Dimension(0, 45));
        btnEntrar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnEntrar.setBackground(new Color(128, 0, 0));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnEntrar.setFocusPainted(false);
        btnEntrar.setOpaque(true);
        btnEntrar.setBorderPainted(false);
        btnEntrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pnlCampos.add(btnEntrar);

        pnlPrincipal.add(pnlCampos, BorderLayout.CENTER);
        add(pnlPrincipal);

        btnEntrar.addActionListener(e -> validar());
        txtPassword.addActionListener(e -> btnEntrar.doClick());
    }

    private JLabel crearEtiqueta(String texto, Font f, Color c) {
        JLabel l = new JLabel(texto);
        l.setFont(f);
        l.setForeground(c);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        return l;
    }

    private void estilizarComponente(JComponent c) {
        c.setMaximumSize(new Dimension(220, 35));
        c.setBackground(Color.WHITE);
        c.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        c.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(0, 10, 0, 10)
        ));
    }

    private void validar() {
        String user = txtUsuario.getText();
        String pass = new String(txtPassword.getPassword());
        String rol = cbxRol.getSelectedItem().toString();

        if (rol.equals("Admin") && user.equals("admin") && pass.equals("4321")) {
            autenticado = true; rolUsuario = "Administrador"; dispose();
        } else if (rol.equals("Vendedor") && user.equals("vendedor") && pass.equals("1234")) {
            autenticado = true; rolUsuario = "Vendedor"; dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Acceso Denegado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean esAutenticado() { return autenticado; }
    public String getRol() { return rolUsuario; }
}
