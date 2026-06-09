package entidades;

// Representa una línea de una orden de compra
public class DetalleOrdenCompra {
    private Item item;
    private Double cantidad;
    private Double precioUnitarioAcordado;
    private Double subtotal;

    public DetalleOrdenCompra() {

    }

    // Constructor que calcula automáticamente el subtotal
    public DetalleOrdenCompra(Item item, Double cantidad, Double precioUnitarioAcordado) {
        this.item = item;
        this.cantidad = cantidad;
        this.precioUnitarioAcordado = precioUnitarioAcordado;
        this.subtotal = this.cantidad * this.precioUnitarioAcordado;
    }

    // Getters y Setters
    public Item getItem() {
        return this.item;
    }

    public Double getCantidad() {
        return this.cantidad;
    }

    public Double getPrecioUnitarioAcordado() {
        return this.precioUnitarioAcordado;
    }

    public Double getSubtotal() {
        return this.subtotal;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    // Cuando cambio cantidad, recalculo el subtotal
    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
        this.subtotal = this.cantidad * this.precioUnitarioAcordado;
    }

    // Cuando cambio precio, recalculo el subtotal
    public void setPrecioUnitarioAcordado(Double precioUnitarioAcordado) {
        this.precioUnitarioAcordado = precioUnitarioAcordado;
        this.subtotal = this.cantidad * this.precioUnitarioAcordado;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    @Override
    public String toString() {
        return "DetalleOC {" +
                item.getCodigo() +
                ", cant: " + cantidad +
                ", $" + subtotal +
                "}";
    }
}
