package VIEW;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class Estilos {

    public static Color colorVino = new Color(139, 0, 0);
    public static Color textoBlanco = Color.WHITE;
    public static Color fondoGrisClaro = new Color(245, 245, 245);

    // ---------------- BOTONES ----------------
    public static void aplicarEstiloBoton(JButton boton) {
        boton.setFont(new Font("Arial", Font.BOLD, 13));
        boton.setBackground(colorVino);
        boton.setForeground(textoBlanco);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(160, 40));
        boton.setOpaque(true);
        boton.setBorderPainted(false);
    }

    // ---------------- TABLAS ----------------
    public static void aplicarEstiloTabla(JTable tabla) {
        tabla.setRowHeight(28);
        tabla.setFont(new Font("Arial", Font.PLAIN, 13));
        tabla.setGridColor(new Color(220, 220, 220));
        tabla.setSelectionBackground(new Color(255, 230, 230));
        tabla.setSelectionForeground(Color.BLACK);
        tabla.setShowVerticalLines(false);
        tabla.setIntercellSpacing(new Dimension(0, 0));

        JTableHeader header = tabla.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setOpaque(true);

        // 🔴 RENDERIZADOR PERSONALIZADO DEL ENCABEZADO
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {

                JLabel lbl = new JLabel(value.toString());
                lbl.setOpaque(true);
                lbl.setBackground(colorVino);     // Fondo vino
                lbl.setForeground(textoBlanco);   // Letras blancas
                lbl.setFont(new Font("Arial", Font.BOLD, 14));
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setBorder(BorderFactory.createLineBorder(Color.WHITE));

                return lbl;
            }
        });

        // 🔹 CENTRAR DATOS DE LA TABLA
        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(centrado);
        }
    }
}