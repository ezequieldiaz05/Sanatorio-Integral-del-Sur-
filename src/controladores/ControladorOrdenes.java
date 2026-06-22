package controladores;

import entidades.OrdenCompra;
import entidades.Proveedor;
import entidades.Rubro;
import entidades.DetalleOrdenCompra;

import java.util.ArrayList;
import java.util.List;

// Clase ControladorOrdenes: Gestiona todas las operaciones relacionadas 
// con Ordenes de Compra.

// SINGLETON
public class ControladorOrdenes {
    private static ControladorOrdenes INSTANCE = null;
    private List<OrdenCompra> ordenesCompra;

    private ControladorOrdenes() {
        this.ordenesCompra = new ArrayList<>();
    }

    public static synchronized ControladorOrdenes getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ControladorOrdenes();
        }
        return INSTANCE;
    }

    // METODOS DE AGREGACION

    // Agrega una nueva orden de compra al sistema. Valida que no exista otra con el
    // mismo numero.
    public Boolean agregarOrdenCompra(OrdenCompra orden) {
        if (orden == null) {
            System.err.println("Error: Orden nula");
            return false;
        }

        if (existeOrdenCompra(orden.getNroOC())) {
            System.err.println("Error: Orden OC-" + orden.getNroOC() + " ya existe");
            return false;
        }

        ordenesCompra.add(orden);

        // Actualizar deuda del proveedor cuando se agrega la orden
        if (orden.getProveedor() != null) {
            orden.getProveedor().actualizarDeuda(orden.getMontoTotal());
        }

        System.out.println("✓ Orden agregada: OC-" + orden.getNroOC());
        
        return true;
    }
    // METODOS DE BUSQUEDA

    // Busca una orden por su numero. Retorna la Orden si existe, null si no existe
    public OrdenCompra buscarOrdenCompra(Integer nroOC) {
        if (nroOC == null) {
            return null;
        }

        for (OrdenCompra oc : ordenesCompra) {
            if (oc.getNroOC().equals(nroOC)) {
                return oc;
            }
        }
        return null;
    }

    // Verifica si existe una orden con el numero dado
    public Boolean existeOrdenCompra(Integer nroOC) {
        return buscarOrdenCompra(nroOC) != null;
    }

    // Obtiene todas las ordenes en un estado especifico
    public List<OrdenCompra> getOrdenesPorEstado(String estado) {
        List<OrdenCompra> resultado = new ArrayList<>();

        if (estado == null || estado.isEmpty()) {
            return resultado;
        }

        for (OrdenCompra oc : ordenesCompra) {
            if (oc.getEstado().equals(estado)) {
                resultado.add(oc);
            }
        }
        return resultado;
    }

    // Obtiene todas las ordenes emitidas a un proveedor especifico
    public List<OrdenCompra> getOrdenesPorProveedor(Proveedor proveedor) {
        List<OrdenCompra> resultado = new ArrayList<>();

        if (proveedor == null) {
            return resultado;
        }

        for (OrdenCompra oc : ordenesCompra) {
            if (oc.getProveedor().equals(proveedor)) {
                resultado.add(oc);
            }
        }
        return resultado;
    }

    // Obtiene todas las ordenes que contienen items de un rubro especifico
    public List<OrdenCompra> getOrdenesPorRubro(Rubro rubro) {
        List<OrdenCompra> resultado = new ArrayList<>();

        if (rubro == null) {
            return resultado;
        }

        // Recorre todas las ordenes y sus detalles
        for (OrdenCompra oc : ordenesCompra) {
            boolean tieneItemDelRubro = false;
            for (DetalleOrdenCompra det : oc.getDetalles()) {
                if (det.getItem() != null && rubro.equals(det.getItem().getRubro())) {
                    tieneItemDelRubro = true;
                }
            }
            if (tieneItemDelRubro) {
                resultado.add(oc);
            }
        }
        return resultado;
    }

    // Obtiene todas las ordenes de compra
    public List<OrdenCompra> getOrdenes() {
        return new ArrayList<>(ordenesCompra);
    }

    // Retorna la cantidad de ordenes
    public Integer getCantidadOrdenes() {
        return ordenesCompra.size();
    }

    // METODOS DE OPERACION

    // Confirma una orden de compra. La orden se valida contra el limite de
    // credito del proveedor. Si excede el limite "Pendiente Aprobacion".
    // Si esta dentro "Orden Confirmada"
    public Boolean confirmarOrden(Integer nroOC) {
        OrdenCompra oc = buscarOrdenCompra(nroOC);

        if (oc == null) {
            System.err.println("Error: Orden no encontrada");
            return false;
        }

        // Valida automaticamente contra el limite del proveedor
        oc.confirmarOC();
        System.out.println("Orden confirmada. Estado: " + oc.getEstado());
        return true;
    }

    // Agrega un detalle a una orden existente
    public Boolean agregarDetalleAOrden(Integer nroOC, DetalleOrdenCompra detalle) {
        OrdenCompra oc = buscarOrdenCompra(nroOC);

        if (oc == null) {
            System.err.println("Error: Orden no encontrada");
            return false;
        }

        if (detalle == null) {
            System.err.println("Error: Detalle nulo");
            return false;
        }

        // agregarDetalle() tambien recalcula el total automaticamente
        oc.agregarDetalle(detalle);
        System.out.println("Detalle agregado a la orden");
        return true;
    }

    // METODOS DE REPORTE

    // Lista todas las ordenes de compra
    public void listarOrdenes() {
        if (ordenesCompra.isEmpty()) {
            System.out.println("No hay órdenes de compra registradas");
            return;
        }

        System.out.println("\n=== LISTA DE ÓRDENES DE COMPRA ===");
        for (int i = 0; i < ordenesCompra.size(); i++) {
            OrdenCompra oc = ordenesCompra.get(i);
            System.out.println((i + 1) + ". " + oc);
        }
    }

    // Muestra detalles completos de una orden especifica, incluyendo todos
    // los items y sus precios
    public void mostrarDetallesOrden(Integer nroOC) {
        OrdenCompra oc = buscarOrdenCompra(nroOC);

        if (oc == null) {
            System.err.println("Error: Orden no encontrada");
            return;
        }

        System.out.println("\n=== DETALLES DE LA ORDEN ===");
        System.out.println("Número: " + oc.getNroOC());
        System.out.println("Proveedor: " + oc.getProveedor().getNombreComercial());
        System.out.println("Fecha Emisión: " + oc.getFechaEmision());
        System.out.println("Total Bruto: $" + oc.getMontoTotal());
        System.out.println("Estado: " + oc.getEstado());
        System.out.println("\nDetalles:");
        for (DetalleOrdenCompra det : oc.getDetalles()) {
            System.out.println("  - " + det);
        }
    }

    // Obtiene órdenes de un proveedor que contienen items
    public List<OrdenCompra> getOrdenesParaRubros(Proveedor proveedor) {
        List<OrdenCompra> resultado = new ArrayList<>();

        if (proveedor == null) {
            return resultado;
        }

        for (OrdenCompra oc : ordenesCompra) {
        if (oc.getProveedor().equals(proveedor)) {
            boolean tieneItem = false;
            for (DetalleOrdenCompra det : oc.getDetalles()) {
                if (det.getItem() != null) {
                    tieneItem = true;
                }
            }
            if (tieneItem) {
                resultado.add(oc);
            }
        }
    }

        return resultado;
    }
}