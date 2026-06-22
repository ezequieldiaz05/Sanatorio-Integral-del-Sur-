package controladores;
 
import entidades.RetencionImpositiva;
 
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Clase ControladorRetenciones: Gestiona todas las operaciones relacionadas
// con Retenciones Impositivas

// SINGLETON
public class ControladorRetenciones {
 
    private static ControladorRetenciones INSTANCE = null;
    private List<RetencionImpositiva> retenciones;
 
    private ControladorRetenciones() {
        this.retenciones = new ArrayList<>();
    }
 
    public static synchronized ControladorRetenciones getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ControladorRetenciones();
        }
        return INSTANCE;
    }

    // METODOS DE AGREGACION

    public Boolean agregarRetencion(RetencionImpositiva retencion) {
        if (retencion == null) {
            System.err.println("Error: Retención nula");
            return false;
        }
 
        retenciones.add(retencion);
        System.out.println("Retención agregada: " + retencion.getTipoImpuesto());
        return true;
    }

    // METODOS DE BUSQUEDA

    public RetencionImpositiva buscarRetencion(Integer nroRetencion) {
        if (nroRetencion == null) {
            return null;
        }
 
        for (RetencionImpositiva r : retenciones) {
            if (r.getNroRetencion().equals(nroRetencion)) {
                return r;
            }
        }
        return null;
    }
 
    public Boolean existeRetencion(Integer nroRetencion) {
        return buscarRetencion(nroRetencion) != null;
    }
    
    // Obtiene retenciones dentro de un periodo especifico
    public List<RetencionImpositiva> getRetencionesPorPeriodo(LocalDate fechaDesde, LocalDate fechaHasta) {
        List<RetencionImpositiva> resultado = new ArrayList<>();
 
        if (fechaDesde == null || fechaHasta == null) {
            return resultado;
        }
 
        for (RetencionImpositiva r : retenciones) {
            if (!r.getFechaRetencion().isBefore(fechaDesde) &&
                !r.getFechaRetencion().isAfter(fechaHasta)) {
                resultado.add(r);
            }
        }
        return resultado;
    }
    
    // Obtiene retenciones por tipo de impuesto
    public List<RetencionImpositiva> getRetencionesPorTipo(String tipoImpuesto) {
        List<RetencionImpositiva> resultado = new ArrayList<>();
 
        if (tipoImpuesto == null || tipoImpuesto.isEmpty()) {
            return resultado;
        }
 
        for (RetencionImpositiva r : retenciones) {
            if (r.getTipoImpuesto().equals(tipoImpuesto)) {
                resultado.add(r);
            }
        }
        return resultado;
    }
    
    // Obtiene todas las retenciones
    public List<RetencionImpositiva> getRetenciones() {
        return new ArrayList<>(retenciones);
    }
 
    public Integer getCantidadRetenciones() {
        return retenciones.size();
    }

    // METODOS DE CONSULTA

    // Total de retenciones en un periodo
    public Double getTotalRetencionesPeriodo(LocalDate fechaDesde, LocalDate fechaHasta) {
        Double total = 0.0;
        List<RetencionImpositiva> retencionesDelPeriodo = getRetencionesPorPeriodo(fechaDesde, fechaHasta);
 
        for (RetencionImpositiva r : retencionesDelPeriodo) {
            total += r.getMontoRetenido();
        }
 
        return total;
    }
 
    // Total de un tipo de impuesto retenido
    public Double getTotalRetencionesPorTipo(String tipoImpuesto) {
        Double total = 0.0;
        List<RetencionImpositiva> retencionesDelTipo = getRetencionesPorTipo(tipoImpuesto);
 
        for (RetencionImpositiva r : retencionesDelTipo) {
            total += r.getMontoRetenido();
        }
 
        return total;
    }
 
    public List<RetencionImpositiva> getRetencionesPorComprobante(String numeroComprobante) {
        List<RetencionImpositiva> resultado = new ArrayList<>();
 
        if (numeroComprobante == null || numeroComprobante.isEmpty()) {
            return resultado;
        }
 
        for (RetencionImpositiva r : retenciones) {
            if (r.getNumeroComprobante().equals(numeroComprobante)) {
                resultado.add(r);
            }
        }
        return resultado;
    }

    // METODOS DE REPORTE

    public void listarRetenciones() {
        if (retenciones.isEmpty()) {
            System.out.println("No hay retenciones registradas");
            return;
        }
 
        System.out.println("\n=== LISTA DE RETENCIONES ===");
        for (int i = 0; i < retenciones.size(); i++) {
            RetencionImpositiva r = retenciones.get(i);
            System.out.println((i + 1) + ". " + r);
        }
    }
 
    public void mostrarDetallesRetencion(Integer nroRetencion) {
        RetencionImpositiva r = buscarRetencion(nroRetencion);
 
        if (r == null) {
            System.err.println("Error: Retención no encontrada");
            return;
        }
 
        System.out.println("\n=== DETALLES DE LA RETENCIÓN ===");
        System.out.println("Número: " + r.getNroRetencion());
        System.out.println("Tipo de Impuesto: " + r.getTipoImpuesto());
        System.out.println("Monto Base: $" + r.getMontoBase());
        System.out.println("Porcentaje: " + r.getPorcentajeRetencion() + "%");
        System.out.println("Monto Retenido: $" + r.getMontoRetenido());
        System.out.println("Monto Neto: $" + r.calcularMontoNeto());
        System.out.println("Fecha: " + r.getFechaRetencion());
        System.out.println("Comprobante: " + r.getNumeroComprobante());
    }
    
    // Genera un reporte completo de retenciones en un periodo
    public void reportePeriodo(LocalDate fechaDesde, LocalDate fechaHasta) {
        System.out.println("\n=== REPORTE DE RETENCIONES ===");
        System.out.println("Período: " + fechaDesde + " a " + fechaHasta);
        System.out.println();
 
        // Totales por tipo de impuesto
        System.out.println("Retenciones por Tipo:");
        System.out.println("  Ganancias: $" + getTotalRetencionesPorTipo("Ganancias"));
        System.out.println("  IIBB: $" + getTotalRetencionesPorTipo("IIBB"));
        System.out.println("  IVA: $" + getTotalRetencionesPorTipo("IVA"));
 
        System.out.println("\nTotal Período: $" + getTotalRetencionesPeriodo(fechaDesde, fechaHasta));
    }
}