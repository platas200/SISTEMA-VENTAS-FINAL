package MODEL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.io.InputStream;
import javax.swing.table.DefaultTableModel;

public class ConsultasProducto {
    private Connection con;
    private Properties queries = new Properties();

    public ConsultasProducto() {
        con = Conexion.getConexion();
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

    public boolean crear(String id, String nombre, String descripcion, double precio, int stock) {
        try (PreparedStatement ps = con.prepareStatement(queries.getProperty("prod.insert"))) {
            ps.setString(1, id);
            ps.setString(2, nombre);
            ps.setString(3, descripcion);
            ps.setDouble(4, precio);
            ps.setInt(5, stock);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public DefaultTableModel leer() {
        String[] columnas = {"ID", "Nombre", "Descripción", "Precio", "Stock"};
        DefaultTableModel modelo = new DefaultTableModel(null, columnas);
        try (Statement st = con.createStatement(); 
             ResultSet rs = st.executeQuery(queries.getProperty("prod.select_all"))) {
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getString("IDPRODUCTO"), rs.getString("NOMBREPRODUCTO"),
                    rs.getString("DESCRIPCION"), rs.getDouble("PRECIO"), rs.getInt("STOCK")
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return modelo;
    }

    public boolean actualizar(String id, String nombre, String desc, double precio, int stock) {
        try (PreparedStatement ps = con.prepareStatement(queries.getProperty("prod.update"))) {
            ps.setString(1, nombre); ps.setString(2, desc);
            ps.setDouble(3, precio); ps.setInt(4, stock); ps.setString(5, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public boolean borrar(String id) {
        try (PreparedStatement ps = con.prepareStatement(queries.getProperty("prod.delete"))) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public double getPrecio(String idProducto) {
        if (idProducto == null) return 0.0;
        if (idProducto.contains(" - ")) idProducto = idProducto.split(" - ")[0];
        try (PreparedStatement ps = con.prepareStatement(queries.getProperty("prod.precio"))) {
            ps.setString(1, idProducto);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble("PRECIO");
        } catch (SQLException e) { e.printStackTrace(); }
        return 0.0;
    }
    
    public int getStock(String idProducto) {
        if (idProducto == null) return 0;
        if (idProducto.contains(" - ")) idProducto = idProducto.split(" - ")[0];
        try (PreparedStatement ps = con.prepareStatement(queries.getProperty("prod.stock"))) {
            ps.setString(1, idProducto);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("STOCK");
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    public List<String> obtenerListaProductos() {
        List<String> lista = new ArrayList<>();
        try (Statement st = con.createStatement(); 
             ResultSet rs = st.executeQuery(queries.getProperty("prod.id_nombre"))) {
            while (rs.next()) {
                lista.add(rs.getString("IDPRODUCTO") + " - " + rs.getString("NOMBREPRODUCTO"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
}