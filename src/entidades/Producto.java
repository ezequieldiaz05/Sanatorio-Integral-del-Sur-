package entidades;

import java.time.LocalDate;

public class Producto extends Item {
    private String lote;
    private LocalDate vencimiento;
    private int stock;

    public Producto() {
        super();
    }

    public Producto(String codigo, String descripcion, String unidadMedida, Double precioUnitarioBase, Double alicuotaIVA, String lote, LocalDate vencimiento, int stock) {
        super(codigo, descripcion, unidadMedida, precioUnitarioBase, alicuotaIVA);

        this.lote = lote;
        this.vencimiento = vencimiento;
        this.stock = stock;
    }

    // Verifica si el producto esta vencido, retorna verdadero
    // si la fecha de vencimiento es anterior a hoy
    public Boolean estaVencido() {
        return LocalDate.now(isAfter(this.vencimiento));
    }

    // Verifica si hay stock disponible
    public Boolean hayStockDisponible() {
        return this.stock > 0;
    }

    // Reduce el stock por una compra
    // Retorna verdadero si se redujo correctamente, y falso
    // si no hay suficiente stock

    public Boolean reducirStock(int cantidad) {
        if (cantidad <= this.stock) {
            this.stock -= cantidad;
            return true;
        }
        return false;
    }

    // Aumenta el stock por un nuevo pedido
    public void aumentarStock(int cantidad) {
        this.stock += cantidad;
    }

    // GETTERS
    public String getLote() {
        return lote;
    }

    public LocalDate getVencimiento() {
        return vencimiento;
    }

    public int getStock() {
        return stock;
    }

    // SETTERS
    public void setLote(String lote) {
        this.lote = lote;
    }

    public void setVencimiento(LocalDate vencimiento) {
        this.vencimiento = vencimiento;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "codigo='" + getCodigo() + '\'' +
                ", descripcion='" + getDescripcion() + '\'' +
                ", lote='" + lote + '\'' +
                ", vencimiento=" + vencimiento +
                ", stock=" + stock +
                ", precioUnitarioBase=" + getPrecioUnitarioBase() +
                '}';
    }
}