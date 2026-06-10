package entidades;

import java.time.LocalDate;

// Representa una retención de impuesto en un pago. Se genera cuando
// el sanatorio paga a un proveedor y debe retener un porcentaje
// por ley.
public class RetencionImpositiva {
    private Integer nroRetencion;
    private String tipoImpuesto;
    private Double porcentajeRetencion;
    private Double montoBase;
    private Double montoRetenido;
    private LocalDate fechaRetencion;
    private String numeroComprobante;

    public RetencionImpositiva() {
        this.fechaRetencion = LocalDate.now();
    }

    // Constructor que calcula automaticamente el monto retenido
    public RetencionImpositiva(Integer nroRetencion, String tipoImpuesto, Double porcentajeRetencion, Double montoBase,
            String numeroComprobante) {
        this.nroRetencion = nroRetencion;
        this.tipoImpuesto = tipoImpuesto;
        this.porcentajeRetencion = porcentajeRetencion;
        this.montoBase = montoBase;
        this.numeroComprobante = numeroComprobante;
        this.fechaRetencion = LocalDate.now();
        this.montoRetenido = calcularMontoRetenido();
    }

    // Calcula el monto que se retiene basado en el porcentaje
    public Double calcularMontoRetenido() {
        if (this.montoBase != null && this.porcentajeRetencion != null) {
            this.montoRetenido = this.montoBase * (this.porcentajeRetencion / 100);
        }
        return this.montoRetenido;
    }

    // Calcula el monto neto (lo que recibe realmente el proveedor
    public Double calcularMontoNeto() {
        if (this.montoBase != null && this.montoRetenido != null) {
            return this.montoBase - this.montoRetenido;
        }
        return this.montoBase;
    }

    public Integer getNroRetencion() {
        return nroRetencion;
    }

    public void setNroRetencion(Integer nroRetencion) {
        this.nroRetencion = nroRetencion;
    }

    public String getTipoImpuesto() {
        return this.tipoImpuesto;
    }

    public void setTipoImpuesto(String tipoImpuesto) {
        this.tipoImpuesto = tipoImpuesto;
    }

    public Double getPorcentajeRetencion() {
        return this.porcentajeRetencion;
    }

    public void setPorcentajeRetencion(Double porcentajeRetencion) {
        this.porcentajeRetencion = porcentajeRetencion;
        calcularMontoRetenido();
    }

    public Double getMontoBase() {
        return this.montoBase;
    }

    public void setMontoBase(Double montoBase) {
        this.montoBase = montoBase;
        calcularMontoRetenido();
    }

    public Double getMontoRetenido() {
        return this.montoRetenido;
    }

    public void setMontoRetenido(Double montoRetenido) {
        this.montoRetenido = montoRetenido;
    }

    public LocalDate getFechaRetencion() {
        return this.fechaRetencion;
    }

    public void setFechaRetencion(LocalDate fechaRetencion) {
        this.fechaRetencion = fechaRetencion;
    }

    public String getNumeroComprobante() {
        return this.numeroComprobante;
    }

    public void setNumeroComprobante(String numeroComprobante) {
        this.numeroComprobante = numeroComprobante;
    }

    @Override
    public String toString() {
        return "Retencion {" +
                "tipo: " + tipoImpuesto +
                ", porcentaje: " + porcentajeRetencion + "%" +
                ", monto retenido: $" + montoRetenido +
                "}";
    }
}
