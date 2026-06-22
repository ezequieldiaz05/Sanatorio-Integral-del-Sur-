package entidades;

import java.time.LocalDate;

public class CertificadoExclusion {
    private String paImpuesto;       // "IVA", "Ganancias", "IIBB"
    private String nombre;         // nombre del certificado
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;

    public CertificadoExclusion() {}

    public CertificadoExclusion(String paImpuesto, String nombre,
                                 LocalDate fechaDesde, LocalDate fechaHasta) {
        this.paImpuesto = paImpuesto;
        this.nombre = nombre;
        this.fechaDesde = fechaDesde;
        this.fechaHasta = fechaHasta;
    }

    public boolean estaVigente() {
        LocalDate hoy = LocalDate.now();
        return !hoy.isBefore(fechaDesde) && !hoy.isAfter(fechaHasta);
    }

    public boolean estaVigente(LocalDate fecha) {
        return !fecha.isBefore(fechaDesde) && !fecha.isAfter(fechaHasta);
    }

    public Integer diasHastaVencimiento() {
        LocalDate hoy = LocalDate.now();
        if (hoy.isAfter(fechaHasta)) return 0;
        return (int) java.time.temporal.ChronoUnit.DAYS.between(hoy, fechaHasta);
    }

    // GETTERS Y SETTERS
    public String getPaImpuesto() { return paImpuesto; }
    public void setPaImpuesto(String paImpuesto) { this.paImpuesto = paImpuesto; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public LocalDate getFechaDesde() { return fechaDesde; }
    public void setFechaDesde(LocalDate fechaDesde) { this.fechaDesde = fechaDesde; }

    public LocalDate getFechaHasta() { return fechaHasta; }
    public void setFechaHasta(LocalDate fechaHasta) { this.fechaHasta = fechaHasta; }

    @Override
    public String toString() {
        return "CertificadoExclusion{impuesto=" + paImpuesto + ", nombre='" + nombre
                + "', vigente=" + estaVigente() + "}";
    }
}
