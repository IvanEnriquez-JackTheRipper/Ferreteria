package Modelo;

public class Detalle {
    private int id;
    private int idProducto;  // ID del producto
    private int cantidad;
    private double precio;
    private int idVenta;

    public Detalle() {}

    public Detalle(int id, int idProducto, int cantidad, double precio, int idVenta) {
        this.id = id;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.idVenta = idVenta;
    }

    // getters y setters
    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getIdVenta() { return idVenta; }
    public void setIdVenta(int idVenta) { this.idVenta = idVenta; }
}
