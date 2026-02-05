package MODEL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:mysql://localhost:3306/Proyecto_BDII";
    private static final String USER = "root";
    private static final String PASSWORD = "root"; // Ajusta tu password si es necesario

    public static Connection getConexion() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("❌ Error abriendo conexión: " + e.getMessage());
            return null;
        }
    }
}
