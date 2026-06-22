package entidades.documentos;

import entidades.DetalleDocumento;
import entidades.Item;
import entidades.OrdenCompra;
import entidades.Proveedor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class DocumentoComercial {

    // ATRIBUTOS
    private String nroDocumento;
    private LocalDate fechaEmision;
    private double montoNeto;
    private double iva;
    private double montoTotal;
    private String estadoCancelacion; // "Pendiente" | "Parcialmente Cancelado" | "Cancelado"

    private Proveedor proveedor;
    private List<OrdenCompra> ordenesCompraAsociadas;
    private List<DetalleDocumento> detalles;

    // CONSTRUCTORES
    protected DocumentoComercial(String nroDocumento, Proveedor proveedor) {
        this(nroDocumento, LocalDate.now(), proveedor);
    }

    protected DocumentoComercial(String nroDocumento, LocalDate fechaEmision, Proveedor proveedor) {
        this.nroDocumento = nroDocumento;
        this.fechaEmision = fechaEmision;
        this.proveedor = proveedor;
        this.estadoCancelacion = "Pendiente";
        this.ordenesCompraAsociadas = new ArrayList<>();
        this.detalles = new ArrayList<>();
    }

    // MÉTODOS ABSTRACTOS (obligatorios en cada subclase)
    public abstract boolean validarConOC(OrdenCompra oc);
    public abstract boolean validarDocumentoAfectado();
    public abstract Double getImpactoCuentaCorriente();

    // MÉTODOS CONCRETOS COMUNES

    public boolean verificarCoherenciaConceptos() {
        for (DetalleDocumento d : detalles) {
            Item item = d.getItem();
            if (item != null && !proveedor.esAptoParaItem(item)) {
                return false;
            }
        }
        return true;
    }

    public void marcarEstado(String nuevoEstado) {
        this.estadoCancelacion = nuevoEstado;
    }

    public void agregarDetalle(DetalleDocumento detalle) {
        detalles.add(detalle);
        montoNeto += detalle.getSubtotal();
        iva += detalle.getSubtotalIVA() != null ? detalle.getSubtotalIVA() : 0.0;
        calcularMontoTotal();
    }

    protected void calcularMontoTotal() {
        this.montoTotal = this.montoNeto + this.iva;
    }

    public void asociarOrdenCompra(OrdenCompra oc) {
        if (!ordenesCompraAsociadas.contains(oc)) ordenesCompraAsociadas.add(oc);
    }

    public boolean validarDocumento() {
        return nroDocumento != null && !nroDocumento.isBlank()
                && fechaEmision != null
                && proveedor != null
                && !detalles.isEmpty()
                && verificarCoherenciaConceptos();
    }

    // GETTERS Y SETTERS
    public String getNroDocumento() { 
        return nroDocumento; 
    }
    public void setNroDocumento(String nroDocumento) { 
        this.nroDocumento = nroDocumento; 
    }

    public LocalDate getFechaEmision() { 
        return fechaEmision; 
    }
    public void setFechaEmision(LocalDate fechaEmision) { 
        this.fechaEmision = fechaEmision; 
    }

    public double getMontoNeto() { 
        return montoNeto; 
    }
    protected void setMontoNeto(double montoNeto) { 
        this.montoNeto = montoNeto; 
    }

    public double getIva() { 
        return iva; 
    }
    protected void setIva(double iva) { 
        this.iva = iva; 
    }

    public double getMontoTotal() { 
        return montoTotal; 
    }
    protected void setMontoTotal(double montoTotal) { 
        this.montoTotal = montoTotal; 
    }

    public String getEstadoCancelacion() { 
        return estadoCancelacion; 
    }

    public Proveedor getProveedor() { 
        return proveedor; 
    }
    public void setProveedor(Proveedor proveedor) { 
        this.proveedor = proveedor; 
    }

    public void marcarCancelacion(String estado) { 
        marcarEstado(estado); 
    }

    public List<DetalleDocumento> getDetalles() { 
        return new ArrayList<>(detalles); 
    }
    public List<OrdenCompra> getOrdenesCompraAsociadas() { 
        return new ArrayList<>(ordenesCompraAsociadas); 
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{nro='" + nroDocumento
                + "', proveedor=" + (proveedor != null ? proveedor.getNombreComercial() : "N/A")
                + ", total=$" + montoTotal + ", estado=" + estadoCancelacion + "}";
    }
}
