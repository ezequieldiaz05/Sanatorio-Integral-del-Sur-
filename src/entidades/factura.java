package entidades;

/**
 * Clase que representa una Factura emitida por un Proveedor.
 * Hereda de DocumentoComercial (aplicación de Herencia/Especialización).
 * * Regla de negocio: Una Factura INCREMENTA la deuda del sanatorio 
 * con el proveedor. Su impacto en la cuenta corriente es positivo. Además,
 * debe autovalidarse contra las Órdenes de Compra para confirmar origen y precio.
 */
public class Factura extends DocumentoComercial {
    
    /**
     * Atributo específico de la Factura para cumplir con la regla de negocio:
     * Si hay diferencias de precio o falta de OC, pasa a "Observado" (true).
     */
    private boolean observada;
    
    /**
     * Indica si un supervisor ya revisó y aprobó esta factura (útil para 
     * sortear la falta de OC o las diferencias de precio).
     */
    private boolean aprobadaPorSupervisor;

    /**
     * Constructor de la Factura.
     * nroDocumento Número único del documento.
     * proveedor Proveedor que emite la factura.
     */
    public Factura(String nroDocumento, Proveedor proveedor) {
        // Invocamos al constructor de la clase padre (DocumentoComercial)
        super(nroDocumento, proveedor);
        this.observada = false;
        this.aprobadaPorSupervisor = false;
    }

    /**
     * APLICACIÓN DE POLIMORFISMO:
     * Sobrescribe el método abstracto de la clase padre.
     * Como la Factura aumenta la deuda, retorna el monto total en POSITIVO.
     * Cuando el sistema sume todos los documentos, este valor automáticamente sumará.
     * * Devuelve el monto total a sumar a la deuda de la cuenta corriente.
     */
    @Override
    public Double getImpactoCuentaCorriente() {
        return this.getMontoTotal(); 
    }

    /**
     * Método central que orquesta las reglas de validación al registrar la factura.
     * APLICACIÓN DE EXCEPCIONES: Delega el manejo de errores al sistema central.
     * * ordenesAsociadas Lista de OCs previas que amparan los conceptos de esta factura.
     * * Lanza AutorizacionRequeridaException Si no hay OC y la factura no está aprobada por un supervisor.
     * * Lanza DiferenciaPreciosException Si el precio unitario difiere del acordado en la OC.
     */
    public void registrarFactura(List<OrdenCompra> ordenesAsociadas) 
            throws AutorizacionRequeridaException, DiferenciaPreciosException {
        
        // 1. Regla de Validación de Origen
        validarOrigen(ordenesAsociadas);
        
        // 2. Regla de Control de Precios e Importes
        if (ordenesAsociadas != null && !ordenesAsociadas.isEmpty()) {
            compararPreciosConOC(ordenesAsociadas);
        }
    }

    /**
     * Verifica que exista una Orden de Compra previa que ampare la factura.
     */
    private void validarOrigen(List<OrdenCompra> ordenesAsociadas) throws AutorizacionRequeridaException {
        if ((ordenesAsociadas == null || ordenesAsociadas.isEmpty()) && !aprobadaPorSupervisor) {
            this.observada = true;
            throw new AutorizacionRequeridaException(
                "La Factura " + this.getNroDocumento() + " no posee una Orden de Compra previa. " +
                "Pasa a estado Observado y requiere autorización de un supervisor."
            );
        }
    }

    /**
     * Compara los precios unitarios de la Factura con los precios unitarios acordados en la OC.
     */
    private void compararPreciosConOC(List<OrdenCompra> ordenesAsociadas) throws DiferenciaPreciosException {
        boolean hayDiferencia = false; 
        
        // Lógica interna: Aquí se recorrerían los detalles de la factura vs los detalles de la OC
        // if (precioFactura != precioOC) { hayDiferencia = true; }

        if (hayDiferencia && !aprobadaPorSupervisor) {
            this.observada = true;
            throw new DiferenciaPreciosException(
                "Discrepancia de precios: El precio facturado difiere del acordado en la OC vinculada. " +
                "El documento pasa a estado Observado."
            );
        }
    }

    /**
     * Método de autorización. Quita la marca de observación y fuerza la aprobación 
     * por parte de un usuario de rango superior.
     */
    public void aprobarPorSupervisor() {
        this.aprobadaPorSupervisor = true;
        this.observada = false;
    }

    // GETTERS
    public boolean isObservada() { return observada; }
    public boolean isAprobadaPorSupervisor() { return aprobadaPorSupervisor; }
}