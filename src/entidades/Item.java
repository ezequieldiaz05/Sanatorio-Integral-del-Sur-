package entidades;

public abstract class Item {
    private String codigo;
    private String descripcion;
    private String unidadMedida;
    private Double precioUnitarioBase;
    private Double alicuotaIVA;

    public Item() {
        
    }

    public Item(String codigo, String descripcion, String unidadMedida, Double precioUnitarioBase, Double alicuotaIVA) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.unidadMedida = unidadMedida;
        this.precioUnitarioBase = precioUnitarioBase;
        this.alicuotaIVA = alicuotaIVA;
    }

    public Double obtenerPrecioUnitarioBase() {
        return this.precioUnitarioBase;
    }

    public Double calcularPrecioConIVA() {
        return this.precioUnitarioBase * (1 + this.alicuotaIVA);
    }

    // GETTERS
    public String getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public Double getPrecioUnitarioBase() {
        return precioUnitarioBase;
    }

    public Double getAlicuotaIVA() {
        return alicuotaIVA;
    }

    // SETTERS
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public void setPrecioUnitarioBase(Double precioUnitarioBase) {
        this.precioUnitarioBase = precioUnitarioBase;
    }

    public void setAlicuotaIVA(Double alicuotaIVA) {
        this.alicuotaIVA = alicuotaIVA;
    }

    @Override
    public String toString() {
        return "Item{" +
                "codigo='" + codigo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", unidadMedida='" + unidadMedida + '\'' +
                ", precioUnitarioBase=" + precioUnitarioBase +
                ", alicuotaIVA=" + alicuotaIVA +
                '}';
    }
}