package entidades.documentos;

import entidades.OrdenCompra;
import entidades.Proveedor;

import java.time.LocalDate;

public class NotaCredito extends DocumentoComercial {

    private DocumentoComercial documentoAfectado;

    public NotaCredito(String nroDocumento, Proveedor proveedor, DocumentoComercial documentoAfectado) {
        super(nroDocumento, proveedor);
        this.documentoAfectado = documentoAfectado;
    }

    public NotaCredito(String nroDocumento, LocalDate fechaEmision, Proveedor proveedor, DocumentoComercial documentoAfectado) {
        super(nroDocumento, fechaEmision, proveedor);
        this.documentoAfectado = documentoAfectado;
    }

    @Override
    public boolean validarConOC(OrdenCompra oc) {
        return true;
    }

    @Override
    public boolean validarDocumentoAfectado() {
        return documentoAfectado != null && getMontoTotal() <= documentoAfectado.getMontoTotal() && getProveedor().equals(documentoAfectado.getProveedor());
    }

    @Override
    public Double getImpactoCuentaCorriente() {
        return -getMontoTotal();
    }

    public Double calcularMontoCredito() {
        return getMontoTotal();
    }

    public void generarDetalles(Double monto) {
        // Genera detalles de la nota de crédito por el monto indicado
    }

    public DocumentoComercial getDocumentoAfectado() { return documentoAfectado; }
    public void setDocumentoAfectado(DocumentoComercial doc) { this.documentoAfectado = doc; }
}
