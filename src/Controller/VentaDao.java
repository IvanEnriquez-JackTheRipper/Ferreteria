package Controller;
//CREADO POR DANIEL
import Modelo.Conexion;
import Modelo.Detalle;
import Modelo.Venta;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VentaDao {

    Connection con;
    Conexion cn = new Conexion();
    PreparedStatement ps;
    ResultSet rs;
    int r;

    public int IdVenta() {
        int id = 0;
        String sql = "SELECT MAX(id) FROM ventas";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return id;
    }

    // Registrar venta
    public int RegistrarVenta(Venta v) {
        String sql = "INSERT INTO ventas (id_cliente, id_vendedor, total) VALUES (?,?,?)";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, v.getIdCliente());
            ps.setInt(2, v.getIdVendedor());
            ps.setDouble(3, v.getTotal());
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // Retorna id generado de la venta
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
        return 0;
    }

    public boolean RegistrarDetalle(Detalle Dv) {
        String sql = "INSERT INTO detalles_venta (id_producto, cantidad, precio_unitario, id_venta) VALUES (?,?,?,?)";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, Dv.getIdProducto());
            ps.setInt(2, Dv.getCantidad());
            ps.setDouble(3, Dv.getPrecio());
            ps.setInt(4, Dv.getIdVenta());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
    }

    public boolean ActualizarStockPorCodigo(int cantidadVendida, String codProducto){
    String sql = "UPDATE productos SET stock = stock - ? WHERE codigo = ?";
    try {
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        ps.setInt(1, cantidadVendida);
        ps.setString(2, codProducto);
        ps.executeUpdate();
        return true;
    } catch(SQLException e) {
        System.out.println(e.toString());
        return false;
    } finally {
        try { con.close(); } catch(SQLException e){ System.out.println(e.toString()); }
    }
}


    public List<Venta> Listarventas() {
        List<Venta> ListaVenta = new ArrayList<>();
        String sql = "SELECT v.id, c.nombre AS cliente, u.nombre AS vendedor, v.total "
                + "FROM ventas v "
                + "INNER JOIN clientes c ON v.id_cliente = c.id "
                + "LEFT JOIN usuarios u ON v.id_vendedor = u.id";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Venta vent = new Venta();
                vent.setId(rs.getInt("id"));
                vent.setCliente(rs.getString("cliente"));
                vent.setVendedor(rs.getString("vendedor"));
                vent.setTotal(rs.getDouble("total"));
                ListaVenta.add(vent);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
        return ListaVenta;
    }

    public boolean GuardarVentaConStock(Venta v, List<Detalle> detalles){
    try{
        con = cn.getConnection();
        con.setAutoCommit(false); // inicia transacción

        // Registrar venta
        String sqlVenta = "INSERT INTO ventas (id_cliente, id_vendedor, total) VALUES (?,?,?)";
        ps = con.prepareStatement(sqlVenta, PreparedStatement.RETURN_GENERATED_KEYS);
        ps.setInt(1, v.getIdCliente());
        ps.setInt(2, v.getIdVendedor());
        ps.setDouble(3, v.getTotal());
        ps.executeUpdate();

        rs = ps.getGeneratedKeys();
        int idVenta = 0;
        if(rs.next()){
            idVenta = rs.getInt(1); // ID de la venta recién insertada
        }

        // Registrar detalles y actualizar stock
        String sqlDetalle = "INSERT INTO detalles_venta (id_producto, cantidad, precio_unitario, id_venta) VALUES (?,?,?,?)";
        String sqlStock = "UPDATE productos SET stock = stock - ? WHERE id = ?";

        for(Detalle d : detalles){
            // Insertar detalle
            ps = con.prepareStatement(sqlDetalle);
            ps.setInt(1, d.getIdProducto());
            ps.setInt(2, d.getCantidad());
            ps.setDouble(3, d.getPrecio());
            ps.setInt(4, idVenta);
            ps.executeUpdate();

            // Actualizar stock
            ps = con.prepareStatement(sqlStock);
            ps.setInt(1, d.getCantidad());
            ps.setInt(2, d.getIdProducto());
            ps.executeUpdate();
        }

        con.commit(); // confirmar transacción
        return true;

    } catch(SQLException e){
        try{ con.rollback(); } catch(SQLException ex){ System.out.println(ex.toString()); }
        System.out.println(e.toString());
        return false;
    } finally {
        try{ con.setAutoCommit(true); con.close(); } catch(SQLException e){ System.out.println(e.toString()); }
    }
}

    }
