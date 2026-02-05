package PuntoVentaQuintoSemestre;

import VIEW.VistaPrincipal;
import VIEW.VistaLogin;
import CONTROLLER.ControladorPrincipal;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // 1. Aplicar estilo visual de Windows/Sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            // 2. Mostrar Login primero
            VistaLogin login = new VistaLogin();
            login.setVisible(true);

            // 3. Si se autenticó, abrir la ventana principal con su ROL
            if (login.esAutenticado()) {
                VistaPrincipal vista = new VistaPrincipal();
                // Pasamos la vista y el rol al controlador
                new ControladorPrincipal(vista, login.getRol());
                
                vista.setTitle(vista.getTitle() + " - Usuario: " + login.getRol());
                vista.setVisible(true);
            } else {
                System.exit(0);
            }
        });
    }
}