package entidades;
/**
 * Clase que representa una Nota de Crédito en el sistema.
 * Hereda de DocumentoComercial (aplicación de Herencia/Especialización).
 * * Regla de negocio: Una Nota de Crédito DISMINUYE la deuda del sanatorio 
 * con el proveedor (es plata a favor nuestro). Por lo tanto, su impacto 
 * en la cuenta corriente es negativo (resta deuda).
 */
public class NotaCredito extends DocumentoComercial {
    
    /**
     * Documento previo (generalmente una Factura) sobre el cual recae este crédito.
     */
    private DocumentoComercial documentoAfectado;

    /**
     * Constructor de la Nota de Crédito.
     * nroDocumento Número único del documento.
     * proveedor Proveedor que emite la nota.
     * documentoAfectado El documento original al que se le aplica el crédito.
     */
    public NotaCredito(String nroDocumento, Proveedor proveedor, DocumentoComercial documentoAfectado) {
        // Invocamos al constructor de la clase padre (DocumentoComercial)
        super(nroDocumento, proveedor);
        this.documentoAfectado = documentoAfectado;
    }

    /**
     * APLICACIÓN DE POLIMORFISMO:
     * Sobrescribe el método abstracto de la clase padre.
     * Como la Nota de Crédito disminuye la deuda, retorna el monto total en NEGATIVO.
     * Cuando el sistema sume todos los documentos, este valor automáticamente restará.
     * * Devuelve el monto total a restar a la deuda de la cuenta corriente.
     */
    @Override
    public Double getImpactoCuentaCorriente() {
        // Notar el signo negativo (-) antes de obtener el monto
        return -this.getMontoTotal(); 
    }

    /**
     * Valida que la Nota de Crédito cumpla con las reglas de negocio estrictas.
     * * Lanza DocumentoInvalidoException Si falla alguna validación lógica.
     */
    public void validarDocumentoAfectado() throws DocumentoInvalidoException {
        // 1. Validamos que exista un documento asociado
        if (this.documentoAfectado == null) {
            throw new DocumentoInvalidoException("Error: La Nota de Crédito debe estar vinculada a un documento previo existente.");
        }
        
        // 2. Regla de Negocio: No nos pueden hacer un crédito por más plata de lo que valía la factura original
        if (this.getMontoTotal() > this.documentoAfectado.getMontoTotal()) {
             throw new DocumentoInvalidoException("Inconsistencia: El monto de la Nota de Crédito no puede superar al del documento afectado.");
        }
        
        // 3. Validamos la coherencia de proveedores
        if (!this.getProveedor().equals(this.documentoAfectado.getProveedor())) {
            throw new DocumentoInvalidoException("Inconsistencia: El proveedor de la Nota de Crédito no coincide con el del documento afectado.");
        }
    }
    
    // GETTERS
    public DocumentoComercial getDocumentoAfectado() {
        return documentoAfectado;
    }
}