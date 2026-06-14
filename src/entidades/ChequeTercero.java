package entidades;

import java.time.LocalDate;

public class ChequeTercero extends Cheque {

    private String firmanteOriginal;

    public ChequeTercero() {
        super();
    }

    public ChequeTercero(Double monto, String nroCheque, String banco,
                         LocalDate fechaEmision, LocalDate fechaVencimiento, String firmanteOriginal) {
        super(monto, nroCheque, banco, fechaEmision, fechaVencimiento);
        this.firmanteOriginal = firmanteOriginal;
    }

    @Override
    public boolean validar() {
        return getMonto() != null && getMonto() > 0
                && getNroCheque() != null
                && getBanco() != null
                && !estaVencido()
                && firmanteOriginal != null;
    }

    public String getFirmanteOriginal() { return firmanteOriginal; }
    public void setFirmanteOriginal(String firmanteOriginal) { this.firmanteOriginal = firmanteOriginal; }

    @Override
    public String toString() {
        return "ChequeTercero{nro=" + getNroCheque() + ", banco='" + getBanco()
                + "', monto=$" + getMonto() + ", firmante='" + firmanteOriginal
                + "', vencimiento=" + getFechaVencimiento() + "}";
    }
}
