package MODEL;

public class DetalleVenta {
    private String idDetalle;
    private String idVenta;
    private String idProducto;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;

    public DetalleVenta() {}

    public DetalleVenta(String idDetalle, String idVenta, String idProducto, int cantidad, double precioUnitario) {
        this.idDetalle = idDetalle;
        this.idVenta = idVenta;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        calcularSubtotal();
    }

    private void calcularSubtotal() { this.subtotal = cantidad * precioUnitario; }
    
    // Getters y Setters
    public String getIdDetalle() { return idDetalle; }
    public void setIdDetalle(String idDetalle) { this.idDetalle = idDetalle; }
    public String getIdVenta() { return idVenta; }
    public void setIdVenta(String idVenta) { this.idVenta = idVenta; }
    public String getIdProducto() { return idProducto; }
    public void setIdProducto(String idProducto) { this.idProducto = idProducto; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; calcularSubtotal(); }
    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; calcularSubtotal(); }
    public double getSubtotal() { return subtotal; }
}