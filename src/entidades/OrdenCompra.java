package entidades;

import java.time.LocalDate;
import java.util.ArrayList;;

public class OrdenCompra {
    private Integer nroOC;
    private LocalDate fechaEmision;
    private Proveedor proveedor;
    private ArrayList detalles;
    private Double montoTotal;
    private String estado;

    public OrdenCompra() {
        this.detalles = new ArrayList();
        this.estado = "Borrador";
        this.fechaEmision = LocalDate.now();
        this.montoTotal = 0.0;
    }

    public OrdenCompra(Integer nroOC, Proveedor proveedor) {
        this.nroOC = nroOC;
        this.proveedor = proveedor;
        this.detalles = new ArrayList();
        this.estado = "Borrador";
        this.fechaEmision = LocalDate.now();
        this.montoTotal = 0.0;
    }

    // Agrega un detalle y recalcula el total
    public void agregarDetalle(DetalleOrdenCompra detalle) {
        this.detalles.add(detalle);
        calcularMontoTotal();
    }

    // Suma los subtotales de todos los detalles
    public void calcularMontoTotal() {
        this.montoTotal = 0.0;
        for (int i = 0; i < this.detalles.size(); i++) {
            DetalleOrdenCompra det = (DetalleOrdenCompra) this.detalles.get(i);
            this.montoTotal = this.montoTotal + det.getSubtotal();
        }
    }

    // Valida límite de crédito y cambia el estado
    public void confirmarOC() {
        if (this.proveedor.puedeAsumir(this.montoTotal)) {
            this.estado = "Emitida";
        } else {
            this.estado = "Pendiente Aprobacion";
        }
    }

    // Getters y Setters
    public Integer getNroOC() {
        return this.nroOC;
    }

    public void setNroOC(Integer nroOC) {
        this.nroOC = nroOC;
    }

    public LocalDate getFechaEmision() {
        return this.fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Proveedor getProveedor() {
        return this.proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public ArrayList getDetalles() {
        return this.detalles;
    }

    public void setDetalles(ArrayList detalles) {
        this.detalles = detalles;
    }

    public Double getMontoTotal() {
        return this.montoTotal;
    }

    public void setMontoTotal(Double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public String getEstado() {
        return this.estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "OC {nro: " + nroOC +
                ", proveedor: " + proveedor.getNombreComercial() +
                ", total: $" + montoTotal +
                ", estado: " + estado +
                "}";
    }
}
