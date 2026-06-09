package entidades;

public class Servicio extends Item {
    private String tipoPrestacion;
    private String detallePrestacion;

    // Constructor vacio por defecto
    public Servicio() {
        super();
    }

    // Constructor con parámetros heredados y específicos
    public Servicio(String codigo, String descripcion, String unidadMedida, Double precioUnitarioBase,
            Double alicuotaIVA, String tipoPrestacion, String detallePrestacion) {
        super(codigo, descripcion, unidadMedida, precioUnitarioBase, alicuotaIVA);
        this.tipoPrestacion = tipoPrestacion;
        this.detallePrestacion = detallePrestacion;
    }

    // Métodos de negocio

    // Obtiene informacion completa del servicio
    // String con toda la info presentada
    public String obtenerInfoServicio() {
        return "Tipo: " + this.tipoPrestacion +
                " | Detalle: " + this.detallePrestacion +
                " | Descripción: " + getDescripcion();
    }

    // Valida si el servicio tiene información completa
    // Si es verdadero retorna todos los datos requeridos
    public Boolean estaCompleto() {
        return getCodigo() != null &&
                getDescripcion() != null &&
                this.tipoPrestacion != null &&
                this.detallePrestacion != null &&
                getPrecioUnitarioBase() != null;
    }

    // Valida el servicio para ser utilizado en una orden
    // Retorna True si el servicio es válido
    public Boolean esValido() {
        return this.estaCompleto() &&
                getPrecioUnitarioBase() > 0 &&
                getAlicuotaIVA() >= 0;
    }

    // Getters
    public String getTipoPrestacion() {
        return tipoPrestacion;
    }

    public String getDetallePrestacion() {
        return detallePrestacion;
    }

    // Setters
    public void setTipoPrestacion(String tipoPrestacion) {
        this.tipoPrestacion = tipoPrestacion;
    }

    public void setDetallePrestacion(String detallePrestacion) {
        this.detallePrestacion = detallePrestacion;
    }

    @Override
    public String toString() {
        return "Servicio{" +
                "codigo='" + getCodigo() + '\'' +
                ", descripcion='" + getDescripcion() + '\'' +
                ", tipoPrestacion='" + tipoPrestacion + '\'' +
                ", detallePrestacion='" + detallePrestacion + '\'' +
                ", precioUnitarioBase=" + getPrecioUnitarioBase() +
                ", alicuotaIVA=" + getAlicuotaIVA() +
                '}';
    }
}
