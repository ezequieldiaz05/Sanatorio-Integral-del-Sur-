package controladores;
 
import entidades.documentos.DocumentoComercial;
import entidades.Proveedor;
 
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Clase ControladorDocumentos: Gestiona todas las operaciones relacionadas 
// con Documentos Comerciales (Facturas, NotasCredito, NotasDebito)

// SINGLETON
public class ControladorDocumentos {
 
    private static ControladorDocumentos INSTANCE = null;
    private List<DocumentoComercial> documentosComerciales;
 
    private ControladorDocumentos() {
        this.documentosComerciales = new ArrayList<>();
    }
 
    public static synchronized ControladorDocumentos getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ControladorDocumentos();
        }
        return INSTANCE;
    }

    // METODOS DE AGREGACION

    // Agrega un nuevo documento al sistema. Valida que no exista previamente y que sea valido.
    public Boolean agregarDocumento(DocumentoComercial documento) {
        if (documento == null) {
            System.err.println("Error: Documento nulo");
            return false;
        }

        // No puede existir otro con el mismo numero
        if (existeDocumento(documento.getNroDocumento())) {
            System.err.println("Error: Ya existe un documento con número " + documento.getNroDocumento());
            return false;
        }
 
        // Valida que el documento sea correcto antes de agregar
        if (!documento.validarDocumento()) {
            System.err.println("Error: Documento inválido");
            return false;
        }
 
        documentosComerciales.add(documento);
        System.out.println("Documento agregado: " + documento.getNroDocumento());
        return true;
    }

    // METODOS DE BUSQUEDA

    // Busca un documento por su numero
    public DocumentoComercial buscarDocumento(String nroDocumento) {
        if (nroDocumento == null || nroDocumento.isEmpty()) {
            return null;
        }
 
        for (DocumentoComercial dc : documentosComerciales) {
            if (dc.getNroDocumento().equals(nroDocumento)) {
                return dc;
            }
        }
        return null;
    }
    
    // Verifica si existe un documento con el numero dado
    public Boolean existeDocumento(String nroDocumento) {
        return buscarDocumento(nroDocumento) != null;
    }
 
    // Obtiene documentos que aun NO han sido pagados
    public List<DocumentoComercial> getDocumentosPendientes() {
        List<DocumentoComercial> resultado = new ArrayList<>();
 
        for (DocumentoComercial dc : documentosComerciales) {
            if (dc.getEstadoCancelacion().equals("Pendiente") ||
                dc.getEstadoCancelacion().equals("Parcialmente Cancelado")) {
                resultado.add(dc);
            }
        }
        return resultado;
    }
 
    // Obtiene todos los documentos de un proveedor especifico.
    // Util para generar la cuenta corriente.
    public List<DocumentoComercial> getDocumentosPorProveedor(Proveedor proveedor) {
        List<DocumentoComercial> resultado = new ArrayList<>();
 
        if (proveedor == null) {
            return resultado;
        }
 
        for (DocumentoComercial dc : documentosComerciales) {
            if (dc.getProveedor().equals(proveedor)) {
                resultado.add(dc);
            }
        }
        return resultado;
    }
 
    // Obtiene documentos por estado especifico
    public List<DocumentoComercial> getDocumentosPorEstado(String estado) {
        List<DocumentoComercial> resultado = new ArrayList<>();
 
        if (estado == null || estado.isEmpty()) {
            return resultado;
        }
 
        for (DocumentoComercial dc : documentosComerciales) {
            if (dc.getEstadoCancelacion().equals(estado)) {
                resultado.add(dc);
            }
        }
        return resultado;
    }
 
    // Obtiene documentos entre dos fechas.
    public List<DocumentoComercial> getDocumentosPorPeriodo(LocalDate fechaDesde, LocalDate fechaHasta) {
        List<DocumentoComercial> resultado = new ArrayList<>();
 
        if (fechaDesde == null || fechaHasta == null) {
            return resultado;
        }
 
        for (DocumentoComercial dc : documentosComerciales) {
            // Verificar que la fecha este dentro del rango
            if (!dc.getFechaEmision().isBefore(fechaDesde) &&
                !dc.getFechaEmision().isAfter(fechaHasta)) {
                resultado.add(dc);
            }
        }
        return resultado;
    }
    
    // Obtiene TODOS los documentos
    public List<DocumentoComercial> getDocumentos() {
        return new ArrayList<>(documentosComerciales);
    }

    // METODOS DE CONSULTA

    //** Calcula la DEUDA de un proveedor.
    // Suma el impacto en cuenta corriente de todos sus documentos.
    // 
    // Factura: impacto NEGATIVO (aumenta deuda)
    // NotaCredito: impacto POSITIVO (reduce deuda)
    // NotaDebito: impacto NEGATIVO (aumenta deudo) */
    public Double getCuentaCorriente(Proveedor proveedor) {
        if (proveedor == null) {
            return 0.0;
        }
 
        Double saldo = 0.0;
        List<DocumentoComercial> documentos = getDocumentosPorProveedor(proveedor);
 
        for (DocumentoComercial dc : documentos) {
            // Retorna un valor que suma o resta
            saldo += dc.getImpactoCuentaCorriente();
        }
 
        return saldo;
    }
    
    // Obtiene el Libro IVA de Compras en un periodo. 
    public List<DocumentoComercial> getLibroIVACompras(LocalDate fechaDesde, LocalDate fechaHasta) {
        return getDocumentosPorPeriodo(fechaDesde, fechaHasta);
    }
    
    // Obtiene el monto total de IVA en un periodo.
    public Double getTotalIVA(LocalDate fechaDesde, LocalDate fechaHasta) {
        Double totalIVA = 0.0;
        List<DocumentoComercial> documentos = getDocumentosPorPeriodo(fechaDesde, fechaHasta);
 
        for (DocumentoComercial dc : documentos) {
            totalIVA += dc.getIva();
        }
 
        return totalIVA;
    }

    // METODOS DE OPERACION

    public Boolean cambiarEstadoDocumento(String nroDocumento, String nuevoEstado) {
        DocumentoComercial dc = buscarDocumento(nroDocumento);
 
        if (dc == null) {
            System.err.println("Error: Documento no encontrado");
            return false;
        }
 
        dc.marcarEstado(nuevoEstado);
        System.out.println("✓ Estado actualizado a: " + nuevoEstado);
        return true;
    }

    // METODOS DE REPORTE

    public void listarDocumentos() {
        if (documentosComerciales.isEmpty()) {
            System.out.println("No hay documentos comerciales registrados");
            return;
        }
 
        System.out.println("\n=== LISTA DE DOCUMENTOS COMERCIALES ===");
        for (int i = 0; i < documentosComerciales.size(); i++) {
            DocumentoComercial dc = documentosComerciales.get(i);
            System.out.println((i + 1) + ". " + dc);
        }
    }
    
    // Muestra informacion detallada de un documento especifico
    public void mostrarDetallesDocumento(String nroDocumento) {
        DocumentoComercial dc = buscarDocumento(nroDocumento);
 
        if (dc == null) {
            System.err.println("Error: Documento no encontrado");
            return;
        }
 
        System.out.println("\n=== DETALLES DEL DOCUMENTO ===");
        System.out.println("Número: " + dc.getNroDocumento());
        System.out.println("Tipo: " + dc.getClass().getSimpleName());
        System.out.println("Proveedor: " + dc.getProveedor().getNombreComercial());
        System.out.println("Fecha: " + dc.getFechaEmision());
        System.out.println("Monto Neto: $" + dc.getMontoNeto());
        System.out.println("IVA: $" + dc.getIva());
        System.out.println("Total: $" + dc.getMontoTotal());
        System.out.println("Estado: " + dc.getEstadoCancelacion());
    }
}