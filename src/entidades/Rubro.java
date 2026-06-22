package entidades;

// Representa una categoria de productos o servicios
public class Rubro {
    private Integer idRubro;
    private String nombre;
    private String descripcion;

    public Rubro() {}

    public Rubro(Integer idRubro, String nombre, String descripcion) {
        this.idRubro = idRubro;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Integer getIdRubro() {
        return this.idRubro;
    }

    public void setIdRubro(Integer idRubro) {
        this.idRubro = idRubro;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Rubro {" + nombre + "}";
    }

    // Comparar por ID
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Rubro)) {
            return false;
        }
        Rubro otro = (Rubro) obj;
        return this.idRubro != null && this.idRubro.equals(otro.idRubro);
    }
}
