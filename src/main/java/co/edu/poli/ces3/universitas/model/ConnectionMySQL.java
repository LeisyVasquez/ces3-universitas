package co.edu.poli.ces3.universitas.model;

import java.sql.*;

public class ConnectionMySQL {

    // Librería de MySQL
    private String driver = "com.mysql.jdbc.Driver";

    // Nombre de la base de datos
    private String database = "universitas";

    // Host
    private String hostname = "localhost";

    // Puerto
    private String port = "3306";

    // Ruta de nuestra base de datos (desactivamos el uso de SSL con "?useSSL=false")
    private String url = "jdbc:mysql://" + hostname + ":" + port + "/" + database + "?useSSL=false";

    // Nombre de usuario
    private String username = "root";

    // Clave de usuario
    private String password = "";

    private Connection conn = null;

    public Connection getConnectionMySQL() {
        try {
            Class.forName(driver);
            return DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}