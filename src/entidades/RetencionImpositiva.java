package entidades;

import java.time.LocalDate;

public class RetencionImpositiva {
    private String paImpuesto;        // tipo de impuesto: "IIBB", "Ganancias", "IVA", etc.
    private String ganancia;           // descripción/categoría de la ganancia
    private Double porcRetenerBase;    // porcentaje base a retener
    private Double porcentajeAplicado; // porcentaje efectivamente aplicado (puede diferir si hay CE vigente)
    private Double montoBase;          // monto sobre el cual se calcula la retención
    private Double montoCalculado;     // resultado: montoBase * porcentajeAplicado / 100
    private LocalDate fechaRetencion;
    private Integer nroRetencion;
    private String numeroComprobante:

    public RetencionImpositiva() {
        this.fechaRetencion = LocalDate.now();
    }

    public static RetencionImpositiva crearRetencion(String paImpuesto, Double porcRetenerBase, Double montoBase) {
        RetencionImpositiva r = new RetencionImpositiva();
        r.paImpuesto = paImpuesto;
        r.porcRetenerBase = porcRetenerBase;
        r.montoBase = montoBase;
        r.calcularRetencion();
        return r;
    }

    public void calcularRetencion() {
        if (montoBase != null && porcRetenerBase != null) {
            porcentajeAplicado = porcRetenerBase;
            montoCalculado = montoBase * (porcentajeAplicado / 100);
        }
    }

    public Double calcularImpacto() {
        return montoCalculado != null ? montoCalculado : 0.0;
    }

    // Backward-compat alias used by OrdenPago
    public Double getMontoRetenido() { 
        return montoCalculado; 
    }

    // GETTERS Y SETTERS
    public String getPaImpuesto() { 
        return paImpuesto; 
    }
    public void setPaImpuesto(String paImpuesto) { 
        this.paImpuesto = paImpuesto; 
    }

    public String getGanancia() { 
        return ganancia; 
    }
    public void setGanancia(String ganancia) { 
        this.ganancia = ganancia; 
    }

    public Double getPorcRetenerBase() { 
        return porcRetenerBase; 
    }
    public void setPorcRetenerBase(Double porcRetenerBase) {
        this.porcRetenerBase = porcRetenerBase;
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

    public Double getMontoCalculado() { 
        return montoCalculado; 
    }

    public LocalDate getFechaRetencion() { 
        return fechaRetencion; 
    }
    public void setFechaRetencion(LocalDate fechaRetencion) { 
        this.fechaRetencion = fechaRetencion; 
    }

    public String getTipoImpuesto() { 
        return paImpuesto; 
    }
    public Integer getNroRetencion() { 
        return nroRetencion; 
    }
    public void setNroRetencion(Integer nro) { 
        this.nroRetencion = nro; 
    }
    public Double getPorcentajeRetencion() { 
        return porcRetenerBase; 
    }
    public Double calcularMontoNeto() { 
        return montoBase - (montoCalculado != null ? montoCalculado : 0.0); 
    }
    public String getNumeroComprobante() { 
        return numeroComprobante; 
    }
    public void setNumeroComprobante(String nro) { 
        this.numeroComprobante = nro; 
    }

    @Override
    public String toString() {
        return "RetencionImpositiva{impuesto='" + paImpuesto
                + "', porcentaje=" + porcentajeAplicado
                + "%, monto=$" + montoCalculado + "}";
    }
}
