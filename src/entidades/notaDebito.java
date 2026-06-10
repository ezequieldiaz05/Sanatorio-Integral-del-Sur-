package entidades;

/**
 * Clase que representa una Nota de Débito en el sistema.
 * Hereda de DocumentoComercial (aplicación de Herencia/Especialización).
 * * Regla de negocio: Una Nota de Débito INCREMENTA la deuda del sanatorio 
 * con el proveedor. Por lo tanto, su impacto en la cuenta corriente es positivo
 */
public class NotaDebito extends DocumentoComercial {
    
    /**
     * Documento previo (generalmente una Factura) sobre el cual recae este débito.
     * Representa una relación de asociación/dependencia.
     */
    private DocumentoComercial documentoAfectado;

    /**
     * Constructor de la Nota de Débito.
     * nroDocumento Número único del documento.
     * proveedor Proveedor que emite la nota.
     * documentoAfectado El documento original al que se le aplica el débito.
     */
    public NotaDebito(String nroDocumento, Proveedor proveedor, DocumentoComercial documentoAfectado) {
        // Invocamos al constructor de la clase padre (DocumentoComercial)
        super(nroDocumento, proveedor);
        this.documentoAfectado = documentoAfectado;
    }

    /**
     * APLICACIÓN DE POLIMORFISMO:
     * Sobrescribe el método abstracto de la clase padre.
     * Como la Nota de Débito aumenta la deuda, retorna el monto total en POSITIVO.
     * * Devuelve el monto total a sumar a la deuda de la cuenta corriente.
     */
    @Override
    public Double getImpactoCuentaCorriente() {
        return this.getMontoTotal(); 
    }

    /**
     * Valida que la Nota de Débito sea consistente con el documento que afecta.
     * APLICACIÓN DE EXCEPCIONES CHEQUEADAS (Visto en la Clase 9):
     * En lugar de devolver un simple "false", lanzamos un error descriptivo si algo falla.
     * * Lanza DocumentoInvalidoException Si el documento afectado es nulo o si es de otro proveedor.
     */
    public void validarDocumentoAfectado() throws DocumentoInvalidoException {
        // 1. Validamos que exista un documento asociado
        if (this.documentoAfectado == null) {
            throw new DocumentoInvalidoException("Error: La Nota de Débito debe estar vinculada a un documento previo existente.");
        }
        
        // 2. Validamos la coherencia de datos: El proveedor de la Nota debe ser el mismo que el del documento afectado
        if (!this.getProveedor().equals(this.documentoAfectado.getProveedor())) {
            throw new DocumentoInvalidoException("Inconsistencia: El proveedor de la Nota de Débito no coincide con el del documento afectado.");
        }
    }
    
    // GETTERS
    public DocumentoComercial getDocumentoAfectado() {
        return documentoAfectado;
    }
}

