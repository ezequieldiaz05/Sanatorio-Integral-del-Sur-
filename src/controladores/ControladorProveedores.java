package controladores;
 
import entidades.Proveedor;
import entidades.Rubro;
import entidades.CertificadoExclusion;
 
import java.util.ArrayList;
import java.util.List;

// Clase ControladorProveedores: Gestiona todas las operaciones relacionadas
// con Proveedores.

// SINGLETON
public class ControladorProveedores {

    private static ControladorProveedores INSTANCE = null;
    private List<Proveedor> proveedores;
 
    private ControladorProveedores() {
        this.proveedores = new ArrayList<>();
    }
 
    public static synchronized ControladorProveedores getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ControladorProveedores();
        }
        return INSTANCE;
    }

    // METODOS DE AGREGACION

    public Boolean agregarProveedor(Proveedor proveedor) {
        if (proveedor == null) {
            System.err.println("Error: Proveedor nulo");
            return false;
        }
 
        if (existeProveedor(proveedor.getCuit())) {
            System.err.println("Error: Ya existe proveedor con CUIT " + proveedor.getCuit());
            return false;
        }
 
        if (!proveedor.esValido()) {
            System.err.println("Error: Proveedor inválido");
            return false;
        }
 
        proveedores.add(proveedor);
        System.out.println("Proveedor agregado: " + proveedor.getNombreComercial());
        return true;
    }

    // METODOS DE BUSQUEDA

    public Proveedor buscarProveedor(String cuit) {
        if (cuit == null || cuit.isEmpty()) {
            return null;
        }
 
        for (Proveedor p : proveedores) {
            if (p.getCuit().equals(cuit)) {
                return p;
            }
        }
        return null;
    }
 
    public Boolean existeProveedor(String cuit) {
        return buscarProveedor(cuit) != null;
    }
 
    // Búsqueda parcial por razón social
    public List<Proveedor> buscarPorRazonSocial(String razonSocial) {
        List<Proveedor> resultado = new ArrayList<>();
 
        if (razonSocial == null || razonSocial.isEmpty()) {
            return resultado;
        }
 
        String busqueda = razonSocial.toLowerCase();
        for (Proveedor p : proveedores) {
            if (p.getRazonSocial().toLowerCase().contains(busqueda)) {
                resultado.add(p);
            }
        }
        return resultado;
    }
 
    public List<Proveedor> buscarPorRubro(Rubro rubro) {
        List<Proveedor> resultado = new ArrayList<>();
 
        if (rubro == null) {
            return resultado;
        }
 
        for (Proveedor p : proveedores) {
            if (p.getRubros().contains(rubro)) {
                resultado.add(p);
            }
        }
        return resultado;
    }
 
    public List<Proveedor> getProveedores() {
        return new ArrayList<>(proveedores);
    }
 
    public Integer getCantidadProveedores() {
        return proveedores.size();
    }

    // METODOS DE MODIFICACION

    public Boolean actualizarLimiteDeuda(String cuit, Double nuevoLimite) {
        Proveedor p = buscarProveedor(cuit);
 
        if (p == null) {
            System.err.println("Error: Proveedor no encontrado");
            return false;
        }
 
        if (nuevoLimite < 0) {
            System.err.println("Error: Límite no puede ser negativo");
            return false;
        }
 
        p.setLimiteDeuda(nuevoLimite);
        System.out.println("Límite actualizado");
        return true;
    }
 
    public Boolean agregarRubroAProveedor(String cuit, Rubro rubro) {
        Proveedor p = buscarProveedor(cuit);
 
        if (p == null) {
            System.err.println("Error: Proveedor no encontrado");
            return false;
        }
 
        if (rubro == null) {
            System.err.println("Error: Rubro nulo");
            return false;
        }
 
        p.agregarRubro(rubro);
        System.out.println("Rubro agregado");
        return true;
    }
 
    public Boolean agregarCertificadoAProveedor(String cuit, CertificadoExclusion certificado) {
        Proveedor p = buscarProveedor(cuit);
 
        if (p == null) {
            System.err.println("Error: Proveedor no encontrado");
            return false;
        }
 
        if (certificado == null) {
            System.err.println("Error: Certificado nulo");
            return false;
        }
 
        p.agregarCertificado(certificado);
        System.out.println("Certificado agregado");
        return true;
    }

    // METODOS DE VALIDACION

    // Valida si el proveedor puede asumir nueva deuda sin exceder su limite
    public Boolean puedeAsumirDeuda(String cuit, Double monto) {
        Proveedor p = buscarProveedor(cuit);
 
        if (p == null) {
            System.err.println("Error: Proveedor no encontrado");
            return false;
        }
 
        return p.puedeAsumir(monto);
    }

    // METODOS DE REPORTE

    public void listarProveedores() {
        if (proveedores.isEmpty()) {
            System.out.println("No hay proveedores registrados");
            return;
        }
 
        System.out.println("\n=== LISTA DE PROVEEDORES ===");
        for (int i = 0; i < proveedores.size(); i++) {
            Proveedor p = proveedores.get(i);
            System.out.println((i + 1) + ". " + p);
        }
    }
 
    public void mostrarDetallesProveedor(String cuit) {
        Proveedor p = buscarProveedor(cuit);
 
        if (p == null) {
            System.err.println("Error: Proveedor no encontrado");
            return;
        }
 
        System.out.println("\n=== DETALLES DEL PROVEEDOR ===");
        System.out.println("CUIT: " + p.getCuit());
        System.out.println("Razón Social: " + p.getRazonSocial());
        System.out.println("Nombre Comercial: " + p.getNombreComercial());
        System.out.println("Domicilio: " + p.getDomicilio());
        System.out.println("Teléfono: " + p.getTelefono());
        System.out.println("Email: " + p.getCorreoElectronico());
        System.out.println("Condición Fiscal: " + p.getCondicionImpositiva());
        System.out.println("Nro. Inscripción: " + p.getNroInscripcionFiscal());
        System.out.println("Fecha Inicio Actividades: " + p.getFechaInicioActividades());
        System.out.println("Límite de Deuda: $" + p.getLimiteDeuda());
        System.out.println("Rubros: " + p.getRubros().size());
        System.out.println("Certificados: " + p.getCertificados().size());
    }
}