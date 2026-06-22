package entidades;

import java.time.LocalDate;

public class Efectivo extends MedioPago {

    private LocalDate fecha;

    public Efectivo() {
        super();
        this.fecha = LocalDate.now();
    }

    public Efectivo(Double monto) {
        super(monto);
        this.fecha = LocalDate.now();
    }

    public Efectivo(Double monto, LocalDate fecha) {
        super(monto);
        this.fecha = fecha;
    }

    @Override
    public boolean validar() {
        return getMonto() != null && getMonto() > 0 && fecha != null;
    }

    public LocalDate getFecha() { 
        return fecha; 
    }
    public void setFecha(LocalDate fecha) { 
        this.fecha = fecha; 
    }

    @Override
    public String toString() {
        return "Efectivo{monto=$" + getMonto() + ", fecha=" + fecha + "}";
    }
}
