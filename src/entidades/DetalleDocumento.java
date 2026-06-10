package entidades;

public class DetalleDocumento {

    // =========================================================
    // ATRIBUTOS
    // =========================================================
    private double cantidad;
    private double precioUnitario;
    private double alicuotaIVA;   // ej: 0.21, 0.105, 0.0
    private double subtotal;      // cantidad * precioUnitario (sin IVA)

    private Item item;

    // =========================================================
    // CONSTRUCTOR VACÍO
    // =========================================================
    public DetalleDocumento() {}

    // =========================================================
    // MÉTODO DE FÁBRICA (según diagrama: crearDetalle)
    // =========================================================

    /**
     * Crea y devuelve un DetalleDocumento completamente inicializado.
     * El subtotal se calcula como cantidad * precioUnitario (neto, sin IVA).
     *
     * @param item            ítem del catálogo
     * @param cantidad        cantidad facturada
     * @param precioUnitario  precio unitario neto acordado
     * @param alicuotaIVA     alícuota de IVA como decimal (ej: 0.21)
     */
    public static DetalleDocumento crearDetalle(Item item, double cantidad,
                                                double precioUnitario, double alicuotaIVA) {
        DetalleDocumento detalle = new DetalleDocumento();
        detalle.item = item;
        detalle.cantidad = cantidad;
        detalle.precioUnitario = precioUnitario;
        detalle.alicuotaIVA = alicuotaIVA;
        detalle.subtotal = cantidad * precioUnitario;
        return detalle;
    }

    // =========================================================
    // GETTERS (no hay setters: el detalle se crea de una vez)
    // =========================================================
    public Item getItem() {
        return item;
    }

    public double getCantidad() {
        return cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public double getAlicuotaIVA() {
        return alicuotaIVA;
    }

    public double getSubtotal() {
        return subtotal;
    }

    /**
     * Importe de IVA correspondiente a este detalle.
     * Útil para discriminar IVA por línea en el Libro IVA Compras.
     */
    public double getImporteIVA() {
        return subtotal * alicuotaIVA;
    }

    /**
     * Total de la línea incluyendo IVA.
     */
    public double getTotalConIVA() {
        return subtotal + getImporteIVA();
    }

    // =========================================================
    // TO STRING (útil para depuración)
    // =========================================================
    @Override
    public String toString() {
        return String.format("DetalleDocumento [item=%s, cantidad=%.2f, precioUnit=%.2f, alicuotaIVA=%.0f%%, subtotal=%.2f]",
                item != null ? item.getCodigo() : "null",
                cantidad, precioUnitario, alicuotaIVA * 100, subtotal);
    }
}