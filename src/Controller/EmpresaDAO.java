package Controller;
//CREADO POR DANIEL

import Modelo.Conexion;
import Modelo.Empresa;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmpresaDAO {

    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    Conexion cn = new Conexion();

    // 🔹 Método para buscar los datos de la empresa (id fijo = 1)
    public Empresa BuscarDatos() {
        Empresa e = new Empresa();
        String sql = "SELECT * FROM empresa WHERE id = 1";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next()) {
                e.setId(rs.getInt("id"));
                e.setRuc(rs.getString("ruc"));
                e.setNombre(rs.getString("nombre"));
                e.setTelefono(rs.getString("telefono"));
                e.setDireccion(rs.getString("direccion"));
                e.setRazon(rs.getString("razon"));
            } else {
                System.out.println(" No se encontró registro de empresa con ID=1.");
            }
        } catch (SQLException ex) {
            System.out.println(" Error al obtener datos de empresa: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException ex2) {
                System.out.println("Error cerrando conexión: " + ex2.getMessage());
            }
        }
        return e;
    }

    // 🔹 Método para modificar los datos de la empresa
    public boolean ModificarDatos(Empresa em) {
        String sql = "UPDATE empresa SET ruc=?, nombre=?, telefono=?, direccion=?, razon=? WHERE id=?";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, em.getRuc());
            ps.setString(2, em.getNombre());
            ps.setString(3, em.getTelefono());
            ps.setString(4, em.getDireccion());
            ps.setString(5, em.getRazon());
            ps.setInt(6, em.getId());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(" Error al modificar empresa: " + e.getMessage());
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException ex2) {
                System.out.println("Error cerrando conexión: " + ex2.getMessage());
            }
        }
    }
}

