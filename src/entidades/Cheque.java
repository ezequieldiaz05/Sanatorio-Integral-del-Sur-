package entidades;

import java.time.LocalDate;

public abstract class Cheque extends MedioPago {

    private String nroCheque;
    private String banco;
    private LocalDate fechaEmision;
    private LocalDate fechaVencimiento;

    public Cheque() {
        super();
    }

    public Cheque(Double monto, String nroCheque, String banco,
                  LocalDate fechaEmision, LocalDate fechaVencimiento) {
        super(monto);
        this.nroCheque = nroCheque;
        this.banco = banco;
        this.fechaEmision = fechaEmision;
        this.fechaVencimiento = fechaVencimiento;
    }

    @Override
    public abstract boolean validar();

    public boolean estaVencido() {
        return fechaVencimiento != null && LocalDate.now().isAfter(fechaVencimiento);
    }

    // GETTERS Y SETTERS
    public String getNroCheque() { return nroCheque; }
    public void setNroCheque(String nroCheque) { this.nroCheque = nroCheque; }

    public String getBanco() { return banco; }
    public void setBanco(String banco) { this.banco = banco; }

    public LocalDate getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDate fechaEmision) { this.fechaEmision = fechaEmision; }

    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{nro=" + nroCheque
                + ", banco='" + banco + "', monto=$" + getMonto()
                + ", vencimiento=" + fechaVencimiento + "}";
    }
}
