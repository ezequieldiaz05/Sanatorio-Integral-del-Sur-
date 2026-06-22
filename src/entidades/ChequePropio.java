package entidades;

import java.time.LocalDate;

public class ChequePropio extends Cheque {

    private String firmante;

    public ChequePropio() {
        super();
    }

    public ChequePropio(Double monto, String nroCheque, String banco,
                        LocalDate fechaEmision, LocalDate fechaVencimiento, String firmante) {
        super(monto, nroCheque, banco, fechaEmision, fechaVencimiento);
        this.firmante = firmante;
    }

    @Override
    public boolean validar() {
        return getMonto() != null && getMonto() > 0
                && getNroCheque() != null
                && getBanco() != null
                && !estaVencido()
                && firmante != null;
    }

    public String getFirmante() { 
        return firmante; 
    }
    public void setFirmante(String firmante) { 
        this.firmante = firmante; 
    }

    @Override
    public String toString() {
        return "ChequePropio{nro=" + getNroCheque() + ", banco='" + getBanco()
                + "', monto=$" + getMonto() + ", firmante='" + firmante
                + "', vencimiento=" + getFechaVencimiento() + "}";
    }
}
