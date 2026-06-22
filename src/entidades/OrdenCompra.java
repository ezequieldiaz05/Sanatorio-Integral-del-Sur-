package entidades;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrdenCompra {
    private Integer nroOC;
    private LocalDate fechaOrdenAcuerdo;
    private Double montoTotalBruto;
    private String estado;

    private Proveedor proveedor;
    private List<DetalleOrdenCompra> detalles;

    public OrdenCompra() {
        this.detalles = new ArrayList<>();
        this.estado = "Borrador";
        this.fechaOrdenAcuerdo = LocalDate.now();
        this.montoTotalBruto = 0.0;
    }

    public OrdenCompra(Integer nroOC, Proveedor proveedor) {
        this();
        this.nroOC = nroOC;
        this.proveedor = proveedor;
    }

    public void agregarDetalle(DetalleOrdenCompra detalle) {
        this.detalles.add(detalle);
        calcularMontoTotal();
    }

    private void calcularMontoTotal() {
        this.montoTotalBruto = 0.0;
        for (DetalleOrdenCompra det : detalles) {
            this.montoTotalBruto += det.getSubtotal();
        }
    }

    public void confirmarOC() {
        if (validarOCLimiteCredito()) {
            this.estado = "Emitida";
        } else {
            this.estado = "Pendiente Aprobacion";
        }
    }

    public boolean validarOCLimiteCredito() {
        return proveedor != null && proveedor.puedeAsumir(montoTotalBruto);
    }

    public void marcarEstado(String nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public List<DetalleOrdenCompra> getDetalles() {
        return new ArrayList<>(detalles);
    }

    // GETTERS Y SETTERS
    public Integer getNroOC() { 
        return nroOC; 
    }
    public void setNroOC(Integer nroOC) { 
        this.nroOC = nroOC; 
    }

    public LocalDate getFechaOrdenAcuerdo() { 
        return fechaOrdenAcuerdo; 
    }
    public void setFechaOrdenAcuerdo(LocalDate fecha) { 
        this.fechaOrdenAcuerdo = fecha; 
    }

    public Double getMontoTotalBruto() { 
        return montoTotalBruto; 
    }

    public String getEstado() { 
        return estado; 
    }
    public void setEstado(String estado) { 
        this.estado = estado; 
    }

    public Proveedor getProveedor() { 
        return proveedor; 
    }
    public void setProveedor(Proveedor proveedor) { 
        this.proveedor = proveedor; 
    }

    public Double getMontoTotal() { 
        return montoTotalBruto; 
    }
    public LocalDate getFechaEmision() { 
        return fechaOrdenAcuerdo; 
    }

    @Override
    public String toString() {
        return "OrdenCompra{nro=" + nroOC
                + ", proveedor=" + (proveedor != null ? proveedor.getNombreComercial() : "N/A")
                + ", total=$" + montoTotalBruto + ", estado=" + estado + "}";
    }
}
