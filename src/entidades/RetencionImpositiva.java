package entidades;

import java.time.LocalDate;

public class RetencionImpositiva {
    private String tipoImpuesto;        // tipo de impuesto: "IIBB", "Ganancias", "IVA", etc.
    private Double porcentajeRetencion;    // porcentaje a retener
    private Double porcentajeAplicado; // porcentaje efectivamente aplicado (puede diferir si hay CE vigente)
    private Double montoBase;          // monto sobre el cual se calcula la retención
    private Double montoRetenido;     // resultado: montoBase * porcentajeAplicado / 100
    private LocalDate fechaRetencion;
    private Integer nroRetencion;
    private String numeroComprobante;
    private String ganancia;

    public RetencionImpositiva() {
        this.fechaRetencion = LocalDate.now();
    }

    public static RetencionImpositiva crearRetencion(String tipoImpuesto, Double porcentajeRetencion, Double montoBase) {
        RetencionImpositiva r = new RetencionImpositiva();
        r.tipoImpuesto = tipoImpuesto;
        r.porcentajeRetencion = porcentajeRetencion;
        r.montoBase = montoBase;
        r.calcularRetencion();
        return r;
    }

    public void calcularRetencion() {
        if (montoBase != null && porcentajeRetencion != null) {
            porcentajeAplicado = porcentajeRetencion;
            montoRetenido = montoBase * (porcentajeAplicado / 100);
        }
    }

    public Double calcularImpuesto() {
        return montoRetenido != null ? montoRetenido : 0.0;
    }

    public Double getMontoRetenido() { 
        return montoRetenido; 
    }

    // GETTERS Y SETTERS
    public String getTipoImpuesto() { 
        return tipoImpuesto; 
    }
    public void setTipoImpuesto(String tipoImpuesto) { 
        this.tipoImpuesto = tipoImpuesto; 
    }

    public Double getPorcentajeRetencion() { 
        return porcentajeRetencion; 
    }
    public void setPorcentajeRetencion(Double porcentajeRetencion) {
        this.porcentajeRetencion = porcentajeRetencion;
        calcularRetencion();
    }

    public Double getPorcentajeAplicado() { 
        return porcentajeAplicado; 
    }

    public Double getMontoBase() { 
        return montoBase; 
    }
    public void setMontoBase(Double montoBase) {
        this.montoBase = montoBase;
        calcularRetencion();
    }

    public LocalDate getFechaRetencion() { 
        return fechaRetencion; 
    }
    public void setFechaRetencion(LocalDate fechaRetencion) { 
        this.fechaRetencion = fechaRetencion; 
    }

    public Integer getNroRetencion() { 
        return nroRetencion; 
    }
    public void setNroRetencion(Integer nro) { 
        this.nroRetencion = nro; 
    }
    
    public Double calcularMontoNeto() { 
        return montoBase - (montoRetenido != null ? montoRetenido : 0.0); 
    }
    
    public String getNumeroComprobante() { 
        return numeroComprobante; 
    }
    public void setNumeroComprobante(String nro) { 
        this.numeroComprobante = nro; 
    }

    public String getGanancia() {
        return ganancia;
    }

    public void setGanancia(String ganancia) {
        this.ganancia = ganancia;
    }

    @Override
    public String toString() {
        return "RetencionImpositiva{impuesto='" + tipoImpuesto
                + "', porcentaje=" + porcentajeAplicado
                + "%, monto=$" + montoRetenido + "}";
    }
}
