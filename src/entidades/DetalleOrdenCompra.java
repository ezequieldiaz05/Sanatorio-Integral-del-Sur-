package entidades;

public class DetalleOrdenCompra {
    private Item item;
    private Double cantidad;
    private Double precioUnitarioAcordado;
    private Double subtotal;

    public DetalleOrdenCompra() {}

    public DetalleOrdenCompra(Item item, Double cantidad, Double precioUnitarioAcordado) {
        this.item = item;
        this.cantidad = cantidad;
        this.precioUnitarioAcordado = precioUnitarioAcordado;
        this.subtotal = cantidad * precioUnitarioAcordado;
    }

    public static DetalleOrdenCompra crearDetalle(Double cantidad,
                                                   Double precioUnitarioAcordado,
                                                   Double subtotal) {
        DetalleOrdenCompra d = new DetalleOrdenCompra();
        d.cantidad = cantidad;
        d.precioUnitarioAcordado = precioUnitarioAcordado;
        d.subtotal = subtotal;
        return d;
    }

    // GETTERS Y SETTERS
    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }

    public Double getCantidad() { return cantidad; }
    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
        if (this.precioUnitarioAcordado != null) this.subtotal = cantidad * precioUnitarioAcordado;
    }

    public Double getPrecioUnitarioAcordado() { return precioUnitarioAcordado; }
    public void setPrecioUnitarioAcordado(Double precio) {
        this.precioUnitarioAcordado = precio;
        if (this.cantidad != null) this.subtotal = cantidad * precio;
    }

    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }

    @Override
    public String toString() {
        return "DetalleOC{item=" + (item != null ? item.getCodigo() : "null")
                + ", cant=" + cantidad + ", subtotal=$" + subtotal + "}";
    }
}
