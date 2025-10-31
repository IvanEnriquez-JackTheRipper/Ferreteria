package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    Connection con;

    public Connection getConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/sistema_venta?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=America/Lima";
            String user = "root"; 
            String pass = "";     

            con = DriverManager.getConnection(url, user, pass);
            return con;

        } catch (SQLException e) {
            System.out.println(" Error de conexión a la base de datos: " + e.getMessage());
            return null;
        }
    }
}

