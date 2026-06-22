package entidades;

public abstract class Item {
    private String codigo;
    private String nombre;
    private String descripcion;
    private String unidadMedida;
    private Double precioUnitarioBase;
    private Double precioIVA;
    private Double alicuotaIVA;
    private Rubro rubro;

    public Item() {}

    public Item(String codigo, String nombre, String descripcion, String unidadMedida,
                Double precioUnitarioBase, Double alicuotaIVA) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.unidadMedida = unidadMedida;
        this.precioUnitarioBase = precioUnitarioBase;
        this.alicuotaIVA = alicuotaIVA;
        this.precioIVA = precioUnitarioBase * alicuotaIVA;
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
    public String getNombre() { 
        return nombre; 
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
    public Double getPrecioIVA() { 
        return precioIVA; 
    }
    public Double getAlicuotaIVA() { 
        return alicuotaIVA; 
    }
    public Rubro getRubro() { 
        return rubro; 
    }

    // SETTERS
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }
    public void setPrecioUnitarioBase(Double precioUnitarioBase) {
        this.precioUnitarioBase = precioUnitarioBase;
        if (this.alicuotaIVA != null) this.precioIVA = precioUnitarioBase * alicuotaIVA;
    }
    public void setAlicuotaIVA(Double alicuotaIVA) {
        this.alicuotaIVA = alicuotaIVA;
        if (this.precioUnitarioBase != null) this.precioIVA = precioUnitarioBase * alicuotaIVA;
    }
    public void setRubro(Rubro rubro) { 
        this.rubro = rubro; 
    }

    @Override
    public String toString() {
        return "Item{codigo='" + codigo + "', nombre='" + nombre + "', precioBase=" + precioUnitarioBase + "}";
    }
}
