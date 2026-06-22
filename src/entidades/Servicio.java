package entidades;

public class Servicio extends Item {
    private String tipoPrestacion;
    private String detallePrestacion;

    public Servicio() {
        super();
    }

    public Servicio(String codigo, String nombre, String descripcion, 
                    String unidadMedida, Double precioUnitarioBase, 
                    Double alicuotaIVA, String tipoPrestacion, String detallePrestacion) {
        super(codigo, nombre, descripcion, unidadMedida, precioUnitarioBase, alicuotaIVA);
        this.tipoPrestacion = tipoPrestacion;
        this.detallePrestacion = detallePrestacion;
    }

    public boolean estaCompleto() {
        return getCodigo() != null && getDescripcion() != null
                && tipoPrestacion != null && detallePrestacion != null
                && getPrecioUnitarioBase() != null;
    }

    public boolean esValido() {
        return estaCompleto() && getPrecioUnitarioBase() > 0 && getAlicuotaIVA() >= 0;
    }

    public String getTipoPrestacion() { 
        return tipoPrestacion; 
    }
    public void setTipoPrestacion(String tipoPrestacion) { 
        this.tipoPrestacion = tipoPrestacion; 
    }

    public String getDetallePrestacion() { 
        return detallePrestacion; 
    }
    public void setDetallePrestacion(String detallePrestacion) { 
        this.detallePrestacion = detallePrestacion; 
    }

    @Override
    public String toString() {
        return "Servicio{codigo='" + getCodigo() + "', nombre='" + getNombre()
                + "', tipo='" + tipoPrestacion + "'}";
    }
}
