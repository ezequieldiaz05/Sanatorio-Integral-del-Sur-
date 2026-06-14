package entidades;

import java.time.LocalDate;

public class Transferencia extends MedioPago {

    private LocalDate fecha;
    private String cbu;
    private String alias;

    public Transferencia() {
        super();
        this.fecha = LocalDate.now();
    }

    public Transferencia(Double monto, String cbu, String alias) {
        super(monto);
        this.cbu = cbu;
        this.alias = alias;
        this.fecha = LocalDate.now();
    }

    public Transferencia(Double monto, LocalDate fecha, String cbu, String alias) {
        super(monto);
        this.fecha = fecha;
        this.cbu = cbu;
        this.alias = alias;
    }

    @Override
    public boolean validar() {
        return getMonto() != null && getMonto() > 0
                && fecha != null
                && (cbu != null || alias != null);
    }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getCbu() { return cbu; }
    public void setCbu(String cbu) { this.cbu = cbu; }

    public String getAlias() { return alias; }
    public void setAlias(String alias) { this.alias = alias; }

    @Override
    public String toString() {
        return "Transferencia{monto=$" + getMonto() + ", cbu='" + cbu + "', alias='" + alias + "'}";
    }
}
