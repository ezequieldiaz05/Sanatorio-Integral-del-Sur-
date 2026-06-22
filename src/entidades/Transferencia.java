package entidades;

import java.time.LocalDate;

public class Transferencia extends MedioPago {

    private LocalDate fecha;
    private String nroReferencia;
    private String cuentaOrigen;

    public Transferencia() {
        super();
        this.fecha = LocalDate.now();
    }

    public Transferencia(Double monto, String nroReferencia, String cuentaOrigen) {
        super(monto);
        this.nroReferencia = nroReferencia;
        this.cuentaOrigen = cuentaOrigen;
        this.fecha = LocalDate.now();
    }

    public Transferencia(Double monto, LocalDate fecha, String nroReferencia, String cuentaOrigen) {
        super(monto);
        this.fecha = fecha;
        this.nroReferencia = nroReferencia;
        this.cuentaOrigen = cuentaOrigen;
    }

    @Override
    public boolean validar() {
        return getMonto() != null && getMonto() > 0
                && fecha != null
                && (nroReferencia != null && cuentaOrigen != null);
    }

    public LocalDate getFecha() { 
        return fecha; 
    }
    public void setFecha(LocalDate fecha) { 
        this.fecha = fecha; 
    }

    public String getNroReferencia() { 
        return nroReferencia; 
    }
    public void setNroReferencia(String nroReferencia) { 
        this.nroReferencia = nroReferencia; 
    }

    public String getCuentaOrigen() { 
        return cuentaOrigen; 
    }
    public void setCuentaOrigen(String cuentaOrigen) { 
        this.cuentaOrigen = cuentaOrigen; 
    }

    @Override
    public String toString() {
        return "Transferencia{monto=$" + getMonto() + ", nroReferencia='" + nroReferencia + "', cuentaOrigen='" + cuentaOrigen + "'}";
    }
}
