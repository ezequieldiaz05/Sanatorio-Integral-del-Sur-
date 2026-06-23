package entidades;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import entidades.enums.CondicionImpositiva;

public class Proveedor {
    private String cuit;
    private String razonSocial;
    private String nombreComercial;
    private String correoElectronico;
    private String domicilio;
    private String telefono;
    private String nroInscripcionFiscal;
    private CondicionImpositiva condicionImpositiva;
    private LocalDate fechaInicioActividades;
    private Double limiteDeudaAutorizado;
    private Double deudaActual;
    private String estado;

    private List<Rubro> rubros;
    private List<CertificadoExclusion> certificadosExclusion;

    public Proveedor() {
        this.rubros = new ArrayList<>();
        this.certificadosExclusion = new ArrayList<>();
        this.deudaActual = 0.0;
        this.limiteDeudaAutorizado = 0.0;
        this.estado = "Activo";
    }

    public Proveedor(String cuit, String razonSocial, CondicionImpositiva condicionImpositiva, 
                    LocalDate fechaInicioActividades, Double limiteDeudaAutorizado) {
        this();
        this.cuit = cuit;
        this.razonSocial = razonSocial;
        this.condicionImpositiva = condicionImpositiva;
        this.fechaInicioActividades = fechaInicioActividades;
        this.limiteDeudaAutorizado = limiteDeudaAutorizado;
    }

    // MÉTODOS DE NEGOCIO

    public void agregarRubro(Rubro rubro) {
        rubros.add(rubro);
    }

    public void agregarCertificado(CertificadoExclusion certificado) {
        this.certificadosExclusion.add(certificado);
    }

    public Double calcularDeudaActual() {
        return this.deudaActual;
    }

    public boolean esAptoParaItem(Item item) {
        if (item == null || item.getRubro() == null) return false;
        return rubros.contains(item.getRubro());
    }

    public void marcarEstado(String estado) {
        this.estado = estado;
    }

    // Valida que el proveedor tenga los datos mínimos obligatorios
    public boolean esValido() {
        return cuit != null && !cuit.isBlank() && razonSocial != null && !razonSocial.isBlank()
                && nombreComercial != null && !nombreComercial.isBlank();
    }

    public List<CertificadoExclusion> getCertificadosVigentes() {
        List<CertificadoExclusion> vigentes = new ArrayList<>();
        for (CertificadoExclusion c : certificadosExclusion) {
            if (c.estaVigente()) {
                vigentes.add(c);
            }
        }
        return vigentes;
    }

    public boolean puedeAsumir(Double monto) {
        return (deudaActual + monto) <= limiteDeudaAutorizado;
    }

    public void actualizarDeuda(Double monto) {
        this.deudaActual += monto;
    }

    public void actualizarLimiteDeuda(Double nuevoLimite) {
        if (nuevoLimite < 0)
            throw new IllegalArgumentException("El límite no puede ser negativo.");
        this.limiteDeudaAutorizado = nuevoLimite;
    }

    // GETTERS Y SETTERS
    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public CondicionImpositiva getCondicionImpositiva() {
        return condicionImpositiva;
    }

    public void setCondicionImpositiva(CondicionImpositiva condicionImpositiva) {
        this.condicionImpositiva = condicionImpositiva;
    }

    public LocalDate getFechaInicioActividades() {
        return fechaInicioActividades;
    }

    public void setFechaInicioActividades(LocalDate fechaInicioActividades) {
        this.fechaInicioActividades = fechaInicioActividades;
    }

    public Double getLimiteDeudaAutorizado() {
        return limiteDeudaAutorizado;
    }

    public void setLimiteDeudaAutorizado(Double limiteDeudaAutorizado) {
        this.limiteDeudaAutorizado = limiteDeudaAutorizado;
    }

    public Double getDeudaActual() {
        return deudaActual;
    }

    public void setDeudaActual(Double deudaActual) {
        this.deudaActual = deudaActual;
    }

    public String getEstado() {
        return estado;
    }

    public String getDomicilio() { 
        return domicilio; 
    }
    public void setDomicilio(String domicilio) { 
        this.domicilio = domicilio; 
    }

    public String getTelefono() { 
        return telefono; 
    }
    public void setTelefono(String telefono) { 
        this.telefono = telefono; 
    }

    public String getNroInscripcionFiscal() { 
        return nroInscripcionFiscal; 
    }
    public void setNroInscripcionFiscal(String nro) { 
        this.nroInscripcionFiscal = nro; 
    }

    // Alias para el Controller
    public Double getLimiteDeuda() { 
        return limiteDeudaAutorizado; 
    }
    public void setLimiteDeuda(Double limite) { 
        this.limiteDeudaAutorizado = limite; 
    }

    public List<Rubro> getRubros() {
        return new ArrayList<>(rubros);
    }

    public List<CertificadoExclusion> getCertificadosExclusion() {
        return new ArrayList<>(certificadosExclusion);
    }

    // Alias para el Controller
    public List<CertificadoExclusion> getCertificados() { 
        return new ArrayList<>(certificadosExclusion); 
    }

    @Override
    public String toString() {
        return "Proveedor{cuit='" + cuit + "', nombre='" + nombreComercial
                + "', deuda=$" + deudaActual + "}";
    }
}
