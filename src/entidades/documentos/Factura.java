package entidades.documentos;

import entidades.DetalleDocumento;
import entidades.DetalleOrdenCompra;
import entidades.OrdenCompra;
import entidades.Proveedor;

import java.time.LocalDate;
import java.util.List;

public class Factura extends DocumentoComercial {

    public Factura(String nroDocumento, Proveedor proveedor) {
        super(nroDocumento, proveedor);
    }

    public Factura(String nroDocumento, LocalDate fechaEmision, Proveedor proveedor) {
        super(nroDocumento, fechaEmision, proveedor);
    }

    @Override
    public boolean validarConOC(OrdenCompra oc) {
        if (oc == null) return false;
        if (!"Emitida".equals(oc.getEstado())) return false;
        return compararPreciosOrdenCompra(oc);
    }

    public boolean compararPreciosOrdenCompra(OrdenCompra oc) {
        List<DetalleDocumento> detallesFactura = getDetalles();
        List<DetalleOrdenCompra> detallesOC = oc.getDetalles();

        for (DetalleDocumento dd : detallesFactura) {
            if (dd.getItem() == null) continue;
            for (DetalleOrdenCompra doc : detallesOC) {
                if (doc.getItem() != null
                        && dd.getItem().getCodigo().equals(doc.getItem().getCodigo())) {
                    if (dd.getPrecioUnitario() > doc.getPrecioUnitarioAcordado()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean validarDocumentoAfectado() {
        return true;
    }

    @Override
    public Double getImpactoCuentaCorriente() {
        return getMontoTotal();
    }

    public void generaDetalles() {
        // Genera o recalcula detalles internos de la factura
    }
}
