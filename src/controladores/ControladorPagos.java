package controladores;

import entidades.RetencionImpositiva;
 
import entidades.OrdenPago;
import entidades.Proveedor;
 
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Clase ControladorPagos: Gestiona todas las operaciones con Ordenes de Pago

// SINGLETON
public class ControladorPagos {
 
    private static ControladorPagos INSTANCE = null;
    private List<OrdenPago> ordenesPago;
 
    private ControladorPagos() {
        this.ordenesPago = new ArrayList<>();
    }
 
    public static synchronized ControladorPagos getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ControladorPagos();
        }
        return INSTANCE;
    }

    // METODOS DE AGREGACION

    public Boolean agregarOrdenPago(OrdenPago orden) {
        if (orden == null) {
            System.err.println("Error: Orden de pago nula");
            return false;
        }
 
        if (existeOrdenPago(orden.getNroOP())) {
            System.err.println("Error: Ya existe una orden de pago con número " + orden.getNroOP());
            return false;
        }
 
        ordenesPago.add(orden);

        // Agregar retenciones automáticamente al ControladorRetenciones
        ControladorRetenciones controlarRetenciones = ControladorRetenciones.getInstance();
        for (RetencionImpositiva r : orden.getRetenciones()) {
            controlarRetenciones.agregarRetencion(r);
        }
        
        System.out.println("Orden de pago agregada: " + orden.getNroOP());
        return true;
    }

    // METODOS DE BUSQUEDA

    public OrdenPago buscarOrdenPago(Integer nroOP) {
        if (nroOP == null) {
            return null;
        }
 
        for (OrdenPago op : ordenesPago) {
            if (op.getNroOP().equals(nroOP)) {
                return op;
            }
        }
        return null;
    }
 
    public Boolean existeOrdenPago(Integer nroOP) {
        return buscarOrdenPago(nroOP) != null;
    }

    public Integer siguienteNroOP() {
        int max = 0;
        for (OrdenPago op : ordenesPago) {
            if (op.getNroOP() != null && op.getNroOP() > max) {
                max = op.getNroOP();
            }
        }
        return max + 1;
    }
 
    // Obtiene todos los pagos realizados a un proveedor especifico
    public List<OrdenPago> getPagosPorProveedor(Proveedor proveedor) {
        List<OrdenPago> resultado = new ArrayList<>();
 
        if (proveedor == null) {
            return resultado;
        }
 
        for (OrdenPago op : ordenesPago) {
            if (op.getProveedor() != null && op.getProveedor().equals(proveedor)) {
                resultado.add(op);
            }
        }
        return resultado;
    }
 
    // Obtiene pagos dentro de un periodo especifico
    public List<OrdenPago> getPagosPorPeriodo(LocalDate fechaDesde, LocalDate fechaHasta) {
        List<OrdenPago> resultado = new ArrayList<>();
 
        if (fechaDesde == null || fechaHasta == null) {
            return resultado;
        }
 
        for (OrdenPago op : ordenesPago) {
            if (!op.getFechaEmision().isBefore(fechaDesde) &&
                !op.getFechaEmision().isAfter(fechaHasta)) {
                resultado.add(op);
            }
        }
        return resultado;
    }
 
    // Obtiene pagos por estado
    public List<OrdenPago> getPagosPorEstado(String estado) {
        List<OrdenPago> resultado = new ArrayList<>();
 
        if (estado == null || estado.isEmpty()) {
            return resultado;
        }
 
        for (OrdenPago op : ordenesPago) {
            if (op.getEstado().equals(estado)) {
                resultado.add(op);
            }
        }
        return resultado;
    }
 
    public List<OrdenPago> getOrdenesPago() {
        return new ArrayList<>(ordenesPago);
    }
 
    public Integer getCantidadOrdenesPago() {
        return ordenesPago.size();
    }

    // METODOS DE CONSULTA

    // Total pagado neto (despues de retenciones) en un periodo
    public Double getTotalPagadoPeriodo(LocalDate fechaDesde, LocalDate fechaHasta) {
        Double total = 0.0;
        List<OrdenPago> pagos = getPagosPorPeriodo(fechaDesde, fechaHasta);
 
        for (OrdenPago op : pagos) {
            total += op.getMontoNetoAFavor();
        }
 
        return total;
    }
 
    // Total de retenciones en un periodo
    public Double getTotalRetencionesPeriodo(LocalDate fechaDesde, LocalDate fechaHasta) {
        Double total = 0.0;
        List<OrdenPago> pagos = getPagosPorPeriodo(fechaDesde, fechaHasta);
 
        for (OrdenPago op : pagos) {
            total += op.getTotalRetenciones();
        }
 
        return total;
    }

    // METODOS DE REPORTE

    public void listarOrdenesPago() {
        if (ordenesPago.isEmpty()) {
            System.out.println("No hay órdenes de pago registradas");
            return;
        }
 
        System.out.println("\n=== LISTA DE ÓRDENES DE PAGO ===");
        for (int i = 0; i < ordenesPago.size(); i++) {
            OrdenPago op = ordenesPago.get(i);
            System.out.println((i + 1) + ". " + op);
        }
    }
 
    public void mostrarDetallesOrdenPago(Integer nroOP) {
        OrdenPago op = buscarOrdenPago(nroOP);
 
        if (op == null) {
            System.err.println("Error: Orden de pago no encontrada");
            return;
        }
 
        System.out.println("\n=== DETALLES DE LA ORDEN DE PAGO ===");
        System.out.println("Número: " + op.getNroOP());
        System.out.println("Proveedor: " + op.getProveedor().getNombreComercial());
        System.out.println("Fecha de Pago: " + op.getFechaEmision());
        System.out.println("Monto Bruto: $" + op.getMontoBrutoAPagar());
        System.out.println("Total Retenciones: $" + op.getTotalRetenciones());
        System.out.println("Monto Neto: $" + op.getMontoNetoAFavor());
        System.out.println("Estado: " + op.getEstado());
    }
}