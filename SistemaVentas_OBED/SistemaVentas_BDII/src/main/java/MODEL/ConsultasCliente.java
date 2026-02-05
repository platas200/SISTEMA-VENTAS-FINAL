package MODEL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.io.InputStream;
import javax.swing.table.DefaultTableModel;

public class ConsultasCliente {
    private Connection con;
    private Properties queries = new Properties();

    public ConsultasCliente() {
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

    public boolean crear(String id, String nombre, String apellido, String telefono, String direccion) {
        try (PreparedStatement ps = con.prepareStatement(queries.getProperty("cli.insert"))) {
            ps.setString(1, id); ps.setString(2, nombre); ps.setString(3, apellido);
            ps.setString(4, telefono); ps.setString(5, direccion);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public DefaultTableModel leer() {
        String[] columnas = {"ID", "Nombre", "Apellido", "Teléfono", "Dirección"};
        DefaultTableModel modelo = new DefaultTableModel(null, columnas);
        try (Statement st = con.createStatement(); 
             ResultSet rs = st.executeQuery(queries.getProperty("cli.select_all"))) {
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getString("IDCLIENTE"), rs.getString("NOMBRECLIENTE"),
                    rs.getString("APELLIDOCLIENTE"), rs.getString("TELEFONO"), rs.getString("DIRECCION")
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return modelo;
    }

    public boolean actualizar(String id, String nombre, String apellido, String telefono, String direccion) {
        try (PreparedStatement ps = con.prepareStatement(queries.getProperty("cli.update"))) {
            ps.setString(1, nombre); ps.setString(2, apellido);
            ps.setString(3, telefono); ps.setString(4, direccion); ps.setString(5, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public boolean borrar(String id) {
        try (PreparedStatement ps = con.prepareStatement(queries.getProperty("cli.delete"))) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public List<String> obtenerListaClientes() {
        List<String> lista = new ArrayList<>();
        try (Statement st = con.createStatement(); 
             ResultSet rs = st.executeQuery(queries.getProperty("cli.id_nombre"))) {
            while (rs.next()) {
                lista.add(rs.getString("IDCLIENTE") + " - " + rs.getString("NOMBRECLIENTE"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
}