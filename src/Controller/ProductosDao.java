package Controller;

import Modelo.Conexion;
import Modelo.Productos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;

public class ProductosDao {

    private Connection con;
    private final Conexion cn = new Conexion();
    private PreparedStatement ps;
    private ResultSet rs;

    // Registrar producto
    public boolean RegistrarProductos(Productos pro) {
        String sql = "INSERT INTO productos (codigo, nombre, proveedor, stock, precio) VALUES (?, ?, ?, ?, ?)";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, pro.getCodigo());
            ps.setString(2, pro.getNombre());
            ps.setString(3, pro.getProveedor());
            ps.setInt(4, pro.getStock());
            ps.setDouble(5, pro.getPrecio());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println(" Error RegistrarProductos: " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos();
        }
    }

    // Listar productos
    public List<Productos> ListarProductos() {
        List<Productos> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Productos pro = new Productos();
                pro.setId(rs.getInt("id"));
                pro.setCodigo(rs.getString("codigo"));
                pro.setNombre(rs.getString("nombre"));
                pro.setProveedor(rs.getString("proveedor"));
                pro.setStock(rs.getInt("stock"));
                pro.setPrecio(rs.getDouble("precio"));
                lista.add(pro);
            }
        } catch (SQLException e) {
            System.err.println(" Error ListarProductos: " + e.getMessage());
        } finally {
            cerrarRecursos();
        }
        return lista;
    }

    // Eliminar producto
    public boolean EliminarProductos(int id) {
        String sql = "DELETE FROM productos WHERE id = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println(" Error EliminarProductos: " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos();
        }
    }

    // Modificar productos
    public boolean ModificarProductos(Productos pro) {
        String sql = "UPDATE productos SET codigo=?, nombre=?, proveedor=?, stock=?, precio=? WHERE id=?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, pro.getCodigo());
            ps.setString(2, pro.getNombre());
            ps.setString(3, pro.getProveedor());
            ps.setInt(4, pro.getStock());
            ps.setDouble(5, pro.getPrecio());
            ps.setInt(6, pro.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println(" Error ModificarProductos: " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos();
        }
    }

    // Buscar producto por código
    public Productos BuscarPro(String cod) {
        Productos producto = new Productos();
        String sql = "SELECT * FROM productos WHERE codigo = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, cod);
            rs = ps.executeQuery();
            if (rs.next()) {
                producto.setId(rs.getInt("id"));
                producto.setCodigo(rs.getString("codigo"));
                producto.setNombre(rs.getString("nombre"));
                producto.setProveedor(rs.getString("proveedor"));
                producto.setStock(rs.getInt("stock"));
                producto.setPrecio(rs.getDouble("precio"));
            }
        } catch (SQLException e) {
            System.err.println(" Error BuscarPro: " + e.getMessage());
        } finally {
            cerrarRecursos();
        }
        return producto;
    }

    // Obtener stock por código
    public int obtenerStock(String codigo) {
        int stock = 0;
        String sql = "SELECT stock FROM productos WHERE codigo = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, codigo);
            rs = ps.executeQuery();
            if (rs.next()) {
                stock = rs.getInt("stock");
            }
        } catch (SQLException e) {
            System.err.println(" Error obtenerStock: " + e.getMessage());
        } finally {
            cerrarRecursos();
        }
        return stock;
    }

    // Llenar ComboBox con nombres de proveedores
    public void ConsultarProveedor(JComboBox<String> proveedor) {
        String sql = "SELECT nombre FROM proveedores";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            proveedor.removeAllItems();
            while (rs.next()) {
                proveedor.addItem(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            System.err.println(" Error ConsultarProveedor: " + e.getMessage());
        } finally {
            cerrarRecursos();
        }
    }
    // Obtener el ID del producto usando su código

    public int obtenerIdPorCodigo(String codigo) {
    String sql = """
        SELECT id 
        FROM productos 
        WHERE TRIM(codigo) = TRIM(?) 
           OR TRIM(codigo) = LPAD(TRIM(?), 2, '0')
    """;
    int id = -1;

    try (Connection con = cn.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, codigo);
        ps.setString(2, codigo);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                id = rs.getInt("id");
            }
        }

    } catch (SQLException e) {
        System.err.println("❌ Error obtenerIdPorCodigo: " + e.getMessage());
    }

    return id;
}



    // Cerrar conexiones y recursos JDBC
    private void cerrarRecursos() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            System.err.println("️ Error cerrando recursos: " + e.getMessage());
        }
    }
}
