package entidades.documentos;

import entidades.OrdenCompra;
import entidades.Proveedor;

import java.time.LocalDate;

public class NotaDebito extends DocumentoComercial {

    private DocumentoComercial documentoAfectado;

    public NotaDebito(String nroDocumento, Proveedor proveedor, DocumentoComercial documentoAfectado) {
        super(nroDocumento, proveedor);
        this.documentoAfectado = documentoAfectado;
    }

    public NotaDebito(String nroDocumento, LocalDate fechaEmision,
                      Proveedor proveedor, DocumentoComercial documentoAfectado) {
        super(nroDocumento, fechaEmision, proveedor);
        this.documentoAfectado = documentoAfectado;
    }

    @Override
    public boolean validarConOC(OrdenCompra oc) {
        return true;
    }

    @Override
    public boolean validarDocumentoAfectado() {
        if (documentoAfectado == null) return false;
        if (!obtenerProveedor().equals(documentoAfectado.obtenerProveedor())) return false;
        return true;
    }

    @Override
    public Double getImpactoCuentaCorriente() {
        return getMontoTotal();
    }

    public Double calcularMontoDebito() {
        return getMontoTotal();
    }

    public void generaDetalles(Double monto) {
        // Genera detalles de la nota de débito por el monto indicado
    }

    public DocumentoComercial getDocumentoAfectado() { return documentoAfectado; }
    public void setDocumentoAfectado(DocumentoComercial doc) { this.documentoAfectado = doc; }
}
