package entidades;

import java.time.LocalDate;

// Representa un certificado de exclusion fiscal usado por proveedores
// para acreditar exención de algunos impuestos.
public class CertificadoExclusion {
    private String tipoImpuesto;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
    private String nroCertificado;

    public CertificadoExclusion() {

    }

    public CertificadoExclusion(String tipoImpuesto, LocalDate fechaDesde, LocalDate fechaHasta,
            String nroCertificado) {
        this.tipoImpuesto = tipoImpuesto;
        this.fechaDesde = fechaDesde;
        this.fechaHasta = fechaHasta;
        this.nroCertificado = nroCertificado;
    }

    // Verifica si el certificado está vigente (no ha vencido)
    public Boolean estaVigente() {
        LocalDate hoy = LocalDate.now();
        return !hoy.isBefore(fechaDesde) && !hoy.isAfter(fechaHasta);
    }

    // Verifica si el certificado es vigente en una fecha especifica
    public Boolean estaVigente(LocalDate fecha) {
        return !fecha.isBefore(fechaDesde) && !fecha.isAfter(fechaHasta);
    }

    // Calcula cuántos días faltan para que venza
    public Integer diasHastaVencimiento() {
        LocalDate hoy = LocalDate.now();
        if (hoy.isAfter(fechaHasta)) {
            return 0; // Ya venció
        }
        return (int) java.time.temporal.ChronoUnit.DAYS.between(hoy, fechaHasta);
    }

    public String getTipoImpuesto() {
        return this.tipoImpuesto;
    }

    public void setTipoImpuesto(String tipoImpuesto) {
        this.tipoImpuesto = tipoImpuesto;
    }

    public LocalDate getFechaDesde() {
        return this.fechaDesde;
    }

    public void setFechaDesde(LocalDate fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public LocalDate getFechaHasta() {
        return this.fechaHasta;
    }

    public void setFechaHasta(LocalDate fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public String getNroCertificado() {
        return this.nroCertificado;
    }

    public void setNroCertificado(String nroCertificado) {
        this.nroCertificado = nroCertificado;
    }

    @Override
    public String toString() {
        return "Certificado {" +
                "tipo: " + tipoImpuesto +
                ", vigente: " + estaVigente() +
                "}";
    }
}
