package entidades;

import java.time.LocalDate;

public class Producto extends Item {
    private LocalDate vencimiento;
    private int stock;

    public Producto() {
        super();
    }

    public Producto(String codigo, String nombre, String descripcion, String unidadMedida,
                    Double precioUnitarioBase, Double alicuotaIVA,
                    LocalDate vencimiento, int stock) {
        super(codigo, nombre, descripcion, unidadMedida, precioUnitarioBase, alicuotaIVA);
        this.vencimiento = vencimiento;
        this.stock = stock;
    }

    public Boolean estaVencido() {
        return vencimiento != null && LocalDate.now().isAfter(vencimiento);
    }

    public Boolean hayStockDisponible() {
        return this.stock > 0;
    }

    public Boolean reducirStock(int cantidad) {
        if (cantidad <= this.stock) {
            this.stock -= cantidad;
            return true;
        }
        return false;
    }

    public void aumentarStock(int cantidad) {
        this.stock += cantidad;
    }

    public LocalDate getVencimiento() { return vencimiento; }
    public void setVencimiento(LocalDate vencimiento) { this.vencimiento = vencimiento; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    @Override
    public String toString() {
        return "Producto{codigo='" + getCodigo() + "', nombre='" + getNombre()
                + "', stock=" + stock + ", vencimiento=" + vencimiento + "}";
    }
}
