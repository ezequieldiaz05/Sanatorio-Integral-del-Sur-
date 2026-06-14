package entidades;

import entidades.documentos.DocumentoComercial;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Clase central que actúa como controlador y gestor de todas las colecciones del sistema.
public class Sistema {

    private List<Proveedor> proveedores;
    private List<OrdenCompra> ordenesCompra;
    private List<DocumentoComercial> documentosComerciales;
    private List<OrdenPago> ordenesPago;
    private List<RetencionImpositiva> retenciones;

    public Sistema() {
        this.proveedores = new ArrayList<>();
        this.ordenesCompra = new ArrayList<>();
        this.documentosComerciales = new ArrayList<>();
        this.ordenesPago = new ArrayList<>();
        this.retenciones = new ArrayList<>();
    }

    // BÚSQUEDAS

    public Proveedor buscarProveedor(String cuit) {
        for (Proveedor p : proveedores) {
            if (cuit.equals(p.getCuit())) return p;
        }
        return null;
    }

    public OrdenCompra buscarOrdenCompraNro(Integer nroOC) {
        for (OrdenCompra oc : ordenesCompra) {
            if (nroOC.equals(oc.getNroOC())) return oc;
        }
        return null;
    }

    public DocumentoComercial buscarDocumentoComercial(String nroDocumento) {
        for (DocumentoComercial dc : documentosComerciales) {
            if (nroDocumento.equals(dc.getNroDocumento())) return dc;
        }
        return null;
    }

    public OrdenPago buscarOrdenPagoNroOP(Integer nroOP) {
        for (OrdenPago op : ordenesPago) {
            if (nroOP.equals(op.getNroOP())) return op;
        }
        return null;
    }

    // CONSULTAS DE CUENTA CORRIENTE

    public List<DocumentoComercial> getCuentaCorrienteProveedor(Proveedor proveedor) {
        List<DocumentoComercial> resultado = new ArrayList<>();
        for (DocumentoComercial dc : documentosComerciales) {
            if (proveedor.equals(dc.obtenerProveedor())) resultado.add(dc);
        }
        return resultado;
    }

    public List<DocumentoComercial> getDocumentosPendientes() {
        List<DocumentoComercial> resultado = new ArrayList<>();
        for (DocumentoComercial dc : documentosComerciales) {
            if ("Pendiente".equals(dc.getEstadoCancelacion())) resultado.add(dc);
        }
        return resultado;
    }

    public List<DocumentoComercial> getDocumentosPorProveedor(Proveedor proveedor) {
        return getCuentaCorrienteProveedor(proveedor);
    }

    // CONSULTAS DE ÓRDENES

    public List<OrdenCompra> getOrdenesCompraProveedor(Proveedor proveedor) {
        List<OrdenCompra> resultado = new ArrayList<>();
        for (OrdenCompra oc : ordenesCompra) {
            if (proveedor.equals(oc.obtenerProveedor())) resultado.add(oc);
        }
        return resultado;
    }

    public List<OrdenCompra> getOrdenesParaRubros(Proveedor proveedor) {
        List<OrdenCompra> resultado = new ArrayList<>();
        List<Rubro> rubrosProveedor = proveedor.getRubros();
        for (OrdenCompra oc : ordenesCompra) {
            if (!proveedor.equals(oc.obtenerProveedor())) continue;
            for (DetalleOrdenCompra det : oc.getDetalles()) {
                if (det.getItem() != null) {
                    resultado.add(oc);
                    break;
                }
            }
        }
        return resultado;
    }

    // CONSULTAS DE PAGOS

    public List<OrdenPago> getPagosPorRubros(Proveedor proveedor) {
        List<OrdenPago> resultado = new ArrayList<>();
        for (OrdenPago op : ordenesPago) {
            if (proveedor.equals(op.getProveedor())) resultado.add(op);
        }
        return resultado;
    }

    public List<OrdenPago> getPagosPorPeriodo(LocalDate desde, LocalDate hasta) {
        List<OrdenPago> resultado = new ArrayList<>();
        for (OrdenPago op : ordenesPago) {
            LocalDate fecha = op.getFechaPago();
            if (fecha != null && !fecha.isBefore(desde) && !fecha.isAfter(hasta)) {
                resultado.add(op);
            }
        }
        return resultado;
    }

    public Double getLibroIVACompras(LocalDate desde, LocalDate hasta) {
        double totalIVA = 0.0;
        for (DocumentoComercial dc : documentosComerciales) {
            LocalDate fecha = dc.getFechaEmision();
            if (fecha != null && !fecha.isBefore(desde) && !fecha.isAfter(hasta)) {
                totalIVA += dc.getIva();
            }
        }
        return totalIVA;
    }

    public List<RetencionImpositiva> getRetencionesPorPeriodo(LocalDate desde, LocalDate hasta) {
        List<RetencionImpositiva> resultado = new ArrayList<>();
        for (RetencionImpositiva r : retenciones) {
            LocalDate fecha = r.getFechaRetencion();
            if (fecha != null && !fecha.isBefore(desde) && !fecha.isAfter(hasta)) {
                resultado.add(r);
            }
        }
        return resultado;
    }

    // AGREGADOS

    public void agregarProveedor(Proveedor proveedor) {
        proveedores.add(proveedor);
    }

    public void agregarOrdenCompra(OrdenCompra oc) {
        ordenesCompra.add(oc);
        if (oc.obtenerProveedor() != null) {
            oc.obtenerProveedor().actualizarDeuda(oc.getMontoTotalAcordado());
        }
    }

    public void agregarOrdenCompraPorDocumentoComercial(OrdenCompra oc, DocumentoComercial dc) {
        agregarOrdenCompra(oc);
        dc.asociarOrdenCompra(oc);
    }

    public void agregarDocumentoComercial(DocumentoComercial dc) {
        documentosComerciales.add(dc);
    }

    public void agregarOrdenPago(OrdenPago op) {
        ordenesPago.add(op);
        for (RetencionImpositiva r : op.getRetenciones()) {
            retenciones.add(r);
        }
    }

    // GETTERS de colecciones
    public List<Proveedor> getProveedores() { return new ArrayList<>(proveedores); }
    public List<OrdenCompra> getOrdenesCompra() { return new ArrayList<>(ordenesCompra); }
    public List<DocumentoComercial> getDocumentosComerciales() { return new ArrayList<>(documentosComerciales); }
    public List<OrdenPago> getOrdenesPago() { return new ArrayList<>(ordenesPago); }
    public List<RetencionImpositiva> getRetenciones() { return new ArrayList<>(retenciones); }
}
