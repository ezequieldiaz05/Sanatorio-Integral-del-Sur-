package entidades;

import entidades.documentos.DocumentoComercial;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrdenPago {

    // ATRIBUTOS
    private Integer nroOP;
    private LocalDate fechaPago;
    private Double montoAbaPagar;      // suma de montos de los documentos incluidos (bruto)
    private Double totalRetenciones;   // suma de todas las retenciones aplicadas
    private Double totalReferencias;   // monto neto que recibe el proveedor (montoAbaPagar - totalRetenciones)
    private String estado;             // "Borrador" | "Emitida"

    private Proveedor proveedor;
    private List<DocumentoComercial> documentos;
    private List<RetencionImpositiva> retenciones;
    private List<MedioPago> mediosDePago;

    // CONSTRUCTORES
    public OrdenPago() {
        this.fechaPago = LocalDate.now();
        this.montoAbaPagar = 0.0;
        this.totalRetenciones = 0.0;
        this.totalReferencias = 0.0;
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
        if ("Cancelado".equals(estado)) return;
        if (!documentos.contains(doc)) {
            documentos.add(doc);
            this.montoAbaPagar += doc.getMontoTotal();
            calcularMontoNetaFavor();
        }
    }

    public void registrarMedioPago(MedioPago medio) {
            this.mediosDePago.add(medio);
        }

    public void calcularRetenciones() {
        this.totalRetenciones = 0.0;
        for (RetencionImpositiva r : retenciones) {
            this.totalRetenciones += r.calcularImpacto();
        }
        calcularMontoNetaFavor();
    }

    public void calcularMontoNetaFavor() {
        this.totalReferencias = this.montoAbaPagar - this.totalRetenciones;
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
    public List<MedioPago> getMediasPago() { 
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

    public LocalDate getFechaPago() { 
        return fechaPago; 
    }
    public void setFechaPago(LocalDate fechaPago) { 
        this.fechaPago = fechaPago; 
    }

    public Double getMontoAbaPagar() { 
        return montoAbaPagar; 
    }
    public Double getTotalRetenciones() { 
        return totalRetenciones; 
    }
    public Double getTotalReferencias() { 
        return totalReferencias; 
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
        return montoAbaPagar;
    }

    public Double getMontoNetoAFavor() {
        return totalReferencias;
    }

    @Override
    public String toString() {
        return "OrdenPago{nro=" + nroOP
                + ", proveedor=" + (proveedor != null ? proveedor.getNombreComercial() : "N/A")
                + ", bruto=$" + montoAbaPagar
                + ", retenciones=$" + totalRetenciones
                + ", neto=$" + totalReferencias
                + ", estado=" + estado + "}";
    }
}
