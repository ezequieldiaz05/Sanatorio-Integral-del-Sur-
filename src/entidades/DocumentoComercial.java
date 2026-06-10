package entidades;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class DocumentoComercial {

    // =========================================================
    // ATRIBUTOS
    // =========================================================
    private String nroDocumento;
    private Date fechaEmision;
    private double montoNeto;
    private double iva;
    private double montoTotal;
    private String estadoCancelacion;  // "Pendiente" | "Parcialmente Cancelado" | "Cancelado"

    private Proveedor proveedor;
    private List<OrdenCompra> ordenesCompraAsociadas;
    private List<DetalleDocumento> detalles;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================
    public DocumentoComercial(String nroDocumento, Date fechaEmision, Proveedor proveedor) {
        this.nroDocumento = nroDocumento;
        this.fechaEmision = fechaEmision;
        this.proveedor = proveedor;
        this.estadoCancelacion = "Pendiente";
        this.ordenesCompraAsociadas = new ArrayList<>();
        this.detalles = new ArrayList<>();
    }

    // =========================================================
    // GETTERS Y SETTERS
    // =========================================================
    public String getNroDocumento() {
        return nroDocumento;
    }

    public void setNroDocumento(String nroDocumento) {
        this.nroDocumento = nroDocumento;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public double getMontoNeto() {
        return montoNeto;
    }

    public void setMontoNeto(double montoNeto) {
        this.montoNeto = montoNeto;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    // montoTotal se calcula internamente, solo getter
    public double getMontoTotal() {
        return montoTotal;
    }

    protected void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }

    // estadoCancelacion se controla con marcarEstado(), no con setter público
    public String getEstadoCancelacion() {
        return estadoCancelacion;
    }

    public Proveedor obtenerProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public List<DetalleDocumento> getDetalles() {
        return new ArrayList<>(detalles); // copia defensiva
    }

    public List<OrdenCompra> getOrdenesCompraAsociadas() {
        return new ArrayList<>(ordenesCompraAsociadas);
    }

    // =========================================================
    // MÉTODOS CONCRETOS COMUNES
    // =========================================================

    /**
     * Calcula montoTotal = montoNeto + iva.
     * Las subclases pueden llamarlo después de poblar los detalles.
     */
    protected void calcularMontoTotal() {
        this.montoTotal = this.montoNeto + this.iva;
    }

    /**
     * Agrega un detalle al documento y recalcula totales.
     */
    public void agregarDetalle(DetalleDocumento detalle) {
        this.detalles.add(detalle);
        this.montoNeto += detalle.getSubtotal();
        // IVA se acumula por ítem en cada DetalleDocumento
        this.iva += detalle.getSubtotal() * detalle.getAlicuotaIVA();
        calcularMontoTotal();
    }

    /**
     * Asocia una OC a este documento (una factura puede respaldar 1 o más OCs).
     */
    public void asociarOrdenCompra(OrdenCompra oc) {
        if (!ordenesCompraAsociadas.contains(oc)) {
            ordenesCompraAsociadas.add(oc);
        }
    }

    /**
     * Actualiza el estado de cancelación según el porcentaje pagado.
     * Llamado desde OrdenPago al registrar un pago.
     */
    public void marcarEstado(String nuevoEstado) {
        this.estadoCancelacion = nuevoEstado;
    }

    /**
     * Verifica coherencia de conceptos:
     * Los ítems del documento deben pertenecer al catálogo
     * autorizado del proveedor y a sus rubros asociados.
     */
    public boolean verificarCoherenciaConceptos() {
        for (DetalleDocumento detalle : detalles) {
            Item item = detalle.getItem();
            if (!proveedor.esAptoParaItem(item)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Validación general del documento.
     * Puede sobreescribirse en subclases si hay lógica adicional.
     */
    public boolean validarDocumento() {
        return nroDocumento != null && !nroDocumento.isBlank()
                && fechaEmision != null
                && proveedor != null
                && !detalles.isEmpty()
                && verificarCoherenciaConceptos();
    }

    // =========================================================
    // MÉTODOS ABSTRACTOS (cada subclase los implementa)
    // =========================================================

    /**
     * Valida que el documento esté respaldado por la OC indicada
     * (precios, ítems, estado de la OC).
     */
    public abstract boolean validarConOC(OrdenCompra oc);

    /**
     * Verifica si el documento tiene un documento afectado válido
     * (aplica a NC y ND; en Factura puede retornar true directamente).
     */
    public abstract boolean validarDocumentoAfectado();
}