package entidades;

import entidades.documentos.DocumentoComercial;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrdenPago {

    // ATRIBUTOS
    private Integer nroOP;
    private LocalDate fechaEmision;
    private Double montoAPagar;      // suma de montos de los documentos incluidos (bruto)
    private Double totalRetenciones;   // suma de todas las retenciones aplicadas
    private Double montoNetoAFavor;   // monto neto que recibe el proveedor (montoAbaPagar - totalRetenciones)
    private String estado;             // "Borrador" | "Emitida"

    private Proveedor proveedor;
    private List<DocumentoComercial> documentos;
    private List<RetencionImpositiva> retenciones;
    private List<MedioPago> mediosDePago;

    // CONSTRUCTORES
    public OrdenPago() {
        this.fechaEmision = LocalDate.now();
        this.montoAPagar = 0.0;
        this.totalRetenciones = 0.0;
        this.montoNetoAFavor = 0.0;
        this.estado = "Borrador";
        this.documentos = new ArrayList<>();
        this.retenciones = new ArrayList<>();
        this.mediosDePago = new ArrayList<>();
    }

    public OrdenPago(Integer nroOP, Proveedor proveedor) {
        this();
        this.nroOP = nroOP;
        this.proveedor = proveedor;
    }

    // MÉTODOS DEL DIAGRAMA

    public void seleccionarDocumentos(DocumentoComercial doc, Map<String, Double> montos) {
        String estado = doc.getEstadoCancelacion();
        if ("Cancelado".equals(estado)) {
            return;
        }
        if (!documentos.contains(doc)) {
            documentos.add(doc);
            this.montoAPagar += doc.getMontoTotal();
            calcularMontoNetoAFavor();
        }
    }

    public void registrarMedioPago(MedioPago medio) {
            this.mediosDePago.add(medio);
    }

    public void calcularRetenciones() {
        this.totalRetenciones = 0.0;
        for (RetencionImpositiva r : retenciones) {
            this.totalRetenciones += r.calcularImpuesto();
        }
        calcularMontoNetoAFavor();
    }

    public void calcularMontoNetoAFavor() {
        this.montoNetoAFavor = this.montoAPagar - this.totalRetenciones;
    }

    public void actualizarEstadoDocumentos() {
        for (DocumentoComercial doc : documentos) {
            doc.marcarEstado("Cancelado");
        }
    }

    public void emitirOrdenPago() {
        calcularRetenciones();
        actualizarEstadoDocumentos();
        this.estado = "Emitida";
    }

    public void agregarRetencion(RetencionImpositiva retencion) {
        retenciones.add(retencion);
        calcularRetenciones();
    }

    public void agregarMedioPago(MedioPago medio) {
        mediosDePago.add(medio);
    }

    public Double getTotalMediosPago() {
        double total = 0.0;
        for (MedioPago mp : mediosDePago) {
            if (mp.getMonto() != null) total += mp.getMonto();
        }
        return total;
    }

    // GETTERS
    public List<RetencionImpositiva> getRetenciones() { 
        return new ArrayList<>(retenciones); 
    }
    public List<MedioPago> getMediosPago() { 
        return new ArrayList<>(mediosDePago); 
    }
    public List<DocumentoComercial> getDocumentos() { 
        return new ArrayList<>(documentos); 
    }

    public Integer getNroOP() { 
        return nroOP; 
    }
    public void setNroOP(Integer nroOP) { 
        this.nroOP = nroOP; 
    }

    public LocalDate getFechaEmision() { 
        return fechaEmision; 
    }
    public void setFechaEmision(LocalDate fechaEmision) { 
        this.fechaEmision = fechaEmision; 
    }

    public Double getMontoAPagar() { 
        return montoAPagar; 
    }
    public Double getTotalRetenciones() { 
        return totalRetenciones; 
    }
    public Double getMontoNetoAFavor() { 
        return montoNetoAFavor; 
    }

    public String getEstado() { 
        return estado; 
    }

    public Proveedor getProveedor() { 
        return proveedor; 
    }
    public void setProveedor(Proveedor proveedor) { 
        this.proveedor = proveedor; 
    }

    public Double getMontoBrutoAPagar() {
        return montoAPagar;
    }

    @Override
    public String toString() {
        return "OrdenPago{nro=" + nroOP
                + ", proveedor=" + (proveedor != null ? proveedor.getNombreComercial() : "N/A")
                + ", bruto=$" + montoAPagar
                + ", retenciones=$" + totalRetenciones
                + ", neto=$" + montoNetoAFavor
                + ", estado=" + estado + "}";
    }
}
