package MODEL;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.Properties;
import java.io.InputStream;
import javax.swing.table.DefaultTableModel;

public class ConsultasVenta {
    private Properties queries = new Properties();

    public ConsultasVenta() {
        cargarConsultas();
    }

    private void cargarConsultas() {
        try {
            // En Eclipse/Maven, a veces se necesita el "/" inicial para buscar en la raíz del jar
            InputStream is = getClass().getResourceAsStream("src/main/java/consultas.properties");
            
            if (is == null) {
                // Intento alternativo
                is = Thread.currentThread().getContextClassLoader().getResourceAsStream("consultas.properties");
            }

            if (is != null) {
                queries.load(is);
                System.out.println("✅ consultas.properties cargado correctamente en Eclipse.");
            } else {
                System.err.println("❌ ERROR: No se encontró consultas.properties. Verifica que esté en src/main/resources");
                // Valores de emergencia para que no truene el programa mientras lo arreglas
                queries.setProperty("prod.select_all", "SELECT * FROM PRODUCTO");
                queries.setProperty("cli.select_all", "SELECT * FROM CLIENTE");
                queries.setProperty("vta.select_all", "SELECT IDVENTA, FECHAVENTA, TOTAL FROM VENTA");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String realizarVenta(String idCliente, List<String> productos, List<Integer> cantidades) {
        Connection con = null;
        try {
            con = Conexion.getConexion();
            con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            con.setAutoCommit(false);

            String idVenta = generarIdVenta(con);
            double total = 0;

            Map<String, Integer> productosConsolidados = new HashMap<>();
            for (int i = 0; i < productos.size(); i++) {
                String idProd = productos.get(i).split(" - ")[0];
                productosConsolidados.merge(idProd, cantidades.get(i), Integer::sum);
            }

            List<String> idsOrdenados = new ArrayList<>(productosConsolidados.keySet());
            Collections.sort(idsOrdenados);

            Map<String, Double> precios = new HashMap<>();

            for (String idProd : idsOrdenados) {
                int cantidadNecesaria = productosConsolidados.get(idProd);
                try (PreparedStatement ps = con.prepareStatement(queries.getProperty("vta.lock"))) {
                    ps.setString(1, idProd);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        int stockActual = rs.getInt("STOCK");
                        double precioUnitario = rs.getDouble("PRECIO");
                        if (stockActual < cantidadNecesaria) {
                            con.rollback();
                            return "Error: Stock insuficiente para " + idProd;
                        }
                        precios.put(idProd, precioUnitario);
                        total += (precioUnitario * cantidadNecesaria);
                    }
                }
            }

            try (PreparedStatement psVenta = con.prepareStatement(queries.getProperty("vta.insert"))) {
                psVenta.setString(1, idVenta);
                psVenta.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
                psVenta.setDouble(3, total);
                psVenta.executeUpdate();
            }

            int item = 1;
            for (String idProd : idsOrdenados) {
                int cant = productosConsolidados.get(idProd);
                double pu = precios.get(idProd);
                String idDetalle = idVenta + "-D" + (item++);

                try (PreparedStatement psDet = con.prepareStatement(queries.getProperty("vta.insert_det"))) {
                    psDet.setString(1, idDetalle);
                    psDet.setString(2, idVenta);
                    psDet.setString(3, idProd);
                    psDet.setInt(4, cant);
                    psDet.setDouble(5, pu);
                    psDet.setDouble(6, (cant * pu));
                    psDet.executeUpdate();
                }

                try (PreparedStatement psStock = con.prepareStatement(queries.getProperty("vta.upd_stock"))) {
                    psStock.setInt(1, cant);
                    psStock.setString(2, idProd);
                    psStock.executeUpdate();
                }
            }

            con.commit();
            return "Venta Exitosa: " + idVenta;

        } catch (Exception e) {
            if (con != null) try { con.rollback(); } catch (SQLException ex) {}
            return "Error en Transacción: " + e.getMessage();
        } finally {
            if (con != null) try { con.close(); } catch (SQLException e) {}
        }
    }

    private String generarIdVenta(Connection con) throws SQLException {
        String sql = "SELECT COUNT(*) FROM VENTA";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return "V" + (rs.getInt(1) + 1);
        }
        return "V" + System.currentTimeMillis();
    }

    public DefaultTableModel leerVentas() {
        String[] col = {"ID Venta", "Fecha", "Total"};
        DefaultTableModel modelo = new DefaultTableModel(null, col);
        try (Connection con = Conexion.getConexion(); 
             Statement st = con.createStatement(); 
             ResultSet rs = st.executeQuery(queries.getProperty("vta.select_all"))) {
            while (rs.next()) {
                modelo.addRow(new Object[]{rs.getString(1), rs.getDate(2), rs.getDouble(3)});
            }
        } catch (Exception e) { e.printStackTrace(); }
        return modelo;
    }

    public DefaultTableModel leerDetalles(String idVenta) {
        String[] col = {"ID Detalle", "ID Prod", "Nombre", "Cant", "Precio", "Subtotal"};
        DefaultTableModel modelo = new DefaultTableModel(null, col);
        try (Connection con = Conexion.getConexion(); 
             PreparedStatement ps = con.prepareStatement(queries.getProperty("vta.detalles"))) {
            ps.setString(1, idVenta);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getString(1), rs.getString(2), rs.getString(3),
                    rs.getInt(4), rs.getDouble(5), rs.getDouble(6)
                });
            }
        } catch (Exception e) { e.printStackTrace(); }
        return modelo;
    }

    public boolean eliminarVenta(String idVenta) {
        try (Connection con = Conexion.getConexion()) {
            con.setAutoCommit(false);
            try (PreparedStatement ps1 = con.prepareStatement(queries.getProperty("vta.del_det"));
                 PreparedStatement ps2 = con.prepareStatement(queries.getProperty("vta.del_vta"))) {
                ps1.setString(1, idVenta);
                ps1.executeUpdate();
                ps2.setString(1, idVenta);
                ps2.executeUpdate();
                con.commit();
                return true;
            } catch (Exception e) {
                con.rollback();
                return false;
            }
        } catch (SQLException e) { return false; }
    }
}