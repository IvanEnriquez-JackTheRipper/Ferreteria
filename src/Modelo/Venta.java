package Modelo;

public class Venta {
    
    private int id;
    private int idCliente;   
    private int idVendedor;  
    private String cliente;  
    private String vendedor; 
    private double total;
    
    public Venta(){}

    // Getters y setters existentes...
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }

    public String getVendedor() { return vendedor; }
    public void setVendedor(String vendedor) { this.vendedor = vendedor; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    // 🔹 NUEVOS GETTERS Y SETTERS PARA LOS IDS
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public int getIdVendedor() { return idVendedor; }
    public void setIdVendedor(int idVendedor) { this.idVendedor = idVendedor; }
}
