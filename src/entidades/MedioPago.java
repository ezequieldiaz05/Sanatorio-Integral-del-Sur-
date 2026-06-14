package entidades;

public abstract class MedioPago {

    private Double monto;

    public MedioPago() {}

    public MedioPago(Double monto) {
        this.monto = monto;
    }

    public abstract boolean validar();

    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }

    // Alias kept for backward compatibility
    public Double getImporte() { return monto; }
    public void setImporte(Double importe) { this.monto = importe; }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{monto=$" + monto + "}";
    }
}
