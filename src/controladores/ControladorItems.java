package controladores;

import entidades.Item;
import entidades.Producto;
import entidades.Servicio;
import entidades.Rubro;
import entidades.Proveedor;
import entidades.OrdenCompra;
import entidades.DetalleOrdenCompra;

import java.util.ArrayList;
import java.util.List;

// Clase ControladorItems: Gestiona el catálogo de Bienes y Prestaciones
// (Productos y Servicios), que comparten la clase base abstracta Item.

// SINGLETON
public class ControladorItems {

    private static ControladorItems INSTANCE = null;
    private List<Item> items;

    private ControladorItems() {
        this.items = new ArrayList<>();
    }

    public static synchronized ControladorItems getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ControladorItems();
        }
        return INSTANCE;
    }

    // METODOS DE AGREGACION

    public Boolean agregarItem(Item item) {
        if (item == null) {
            System.err.println("Error: Item nulo");
            return false;
        }

        if (item.getCodigo() == null || item.getCodigo().isBlank()) {
            System.err.println("Error: El item debe tener un código");
            return false;
        }

        if (existeItem(item.getCodigo())) {
            System.err.println("Error: Ya existe un item con código " + item.getCodigo());
            return false;
        }

        if (item.getRubro() == null) {
            System.err.println("Error: El item debe pertenecer a un rubro");
            return false;
        }

        items.add(item);
        System.out.println("Item agregado: " + item.getNombre());
        return true;
    }

    // METODOS DE BUSQUEDA

    public Item buscarItem(String codigo) {
        if (codigo == null || codigo.isEmpty()) {
            return null;
        }

        for (Item it : items) {
            if (codigo.equals(it.getCodigo())) {
                return it;
            }
        }
        return null;
    }

    public Boolean existeItem(String codigo) {
        return buscarItem(codigo) != null;
    }

    // Búsqueda parcial por nombre
    public List<Item> buscarPorNombre(String nombre) {
        List<Item> resultado = new ArrayList<>();

        if (nombre == null || nombre.isEmpty()) {
            return resultado;
        }

        String busqueda = nombre.toLowerCase();
        for (Item it : items) {
            if (it.getNombre() != null && it.getNombre().toLowerCase().contains(busqueda)) {
                resultado.add(it);
            }
        }
        return resultado;
    }

    public List<Item> getItemsPorRubro(Rubro rubro) {
        List<Item> resultado = new ArrayList<>();

        if (rubro == null) {
            return resultado;
        }

        for (Item it : items) {
            if (rubro.equals(it.getRubro())) {
                resultado.add(it);
            }
        }
        return resultado;
    }

    // Obtiene solo los Productos (bienes tangibles)
    public List<Producto> getProductos() {
        List<Producto> resultado = new ArrayList<>();

        for (Item it : items) {
            if (it instanceof Producto) {
                resultado.add((Producto) it);
            }
        }
        return resultado;
    }

    // Obtiene solo los Servicios (prestaciones)
    public List<Servicio> getServicios() {
        List<Servicio> resultado = new ArrayList<>();

        for (Item it : items) {
            if (it instanceof Servicio) {
                resultado.add((Servicio) it);
            }
        }
        return resultado;
    }

    public List<Item> getItems() {
        return new ArrayList<>(items);
    }

    public Integer getCantidadItems() {
        return items.size();
    }

    // METODOS DE CONSULTA

    // Comparación de precios de un ítem entre distintos proveedores.
    // Recorre las órdenes de compra emitidas y arma el precio acordado por proveedor.
    // (Reporte de Análisis de Costos y Precios)
    public void compararPreciosEntreProveedores(String codigoItem) {
        Item item = buscarItem(codigoItem);

        if (item == null) {
            System.err.println("Error: Item no encontrado");
            return;
        }

        ControladorOrdenes ctrlOrdenes = ControladorOrdenes.getInstance();

        System.out.println("\n=== COMPARACIÓN DE PRECIOS ===");
        System.out.println("Item: " + item.getCodigo() + " - " + item.getNombre());
        System.out.println("Precio base catálogo: $" + item.getPrecioUnitarioBase());
        System.out.println("\nPrecios acordados por proveedor:");

        boolean encontroAlguno = false;
        for (OrdenCompra oc : ctrlOrdenes.getOrdenes()) {
            for (DetalleOrdenCompra det : oc.getDetalles()) {
                if (det.getItem() != null && codigoItem.equals(det.getItem().getCodigo())) {
                    Proveedor p = oc.getProveedor();
                    String nombreProv = (p != null) ? p.getNombreComercial() : "N/A";
                    System.out.println("  - " + nombreProv
                            + " (OC-" + oc.getNroOC() + "): $" + det.getPrecioUnitarioAcordado());
                    encontroAlguno = true;
                }
            }
        }

        if (!encontroAlguno) {
            System.out.println("  (sin órdenes de compra que incluyan este item)");
        }
    }

    // METODOS DE REPORTE

    public void listarItems() {
        if (items.isEmpty()) {
            System.out.println("No hay items registrados");
            return;
        }

        System.out.println("\n=== CATÁLOGO DE ITEMS ===");
        for (int i = 0; i < items.size(); i++) {
            Item it = items.get(i);
            System.out.println((i + 1) + ". " + it);
        }
    }

    public void mostrarDetallesItem(String codigo) {
        Item it = buscarItem(codigo);

        if (it == null) {
            System.err.println("Error: Item no encontrado");
            return;
        }

        System.out.println("\n=== DETALLES DEL ITEM ===");
        System.out.println("Código: " + it.getCodigo());
        System.out.println("Nombre: " + it.getNombre());
        System.out.println("Descripción: " + it.getDescripcion());
        System.out.println("Tipo: " + it.getClass().getSimpleName());
        System.out.println("Rubro: " + (it.getRubro() != null ? it.getRubro().getNombre() : "N/A"));
        System.out.println("Unidad de Medida: " + it.getUnidadMedida());
        System.out.println("Precio Base: $" + it.getPrecioUnitarioBase());
        System.out.println("Alícuota IVA: " + it.getAlicuotaIVA());
        System.out.println("Precio con IVA: $" + it.calcularPrecioConIVA());

        if (it instanceof Producto) {
            Producto prod = (Producto) it;
            System.out.println("Stock: " + prod.getStock());
            System.out.println("Vencimiento: " + prod.getVencimiento());
        } else if (it instanceof Servicio) {
            Servicio serv = (Servicio) it;
            System.out.println("Tipo Prestación: " + serv.getTipoPrestacion());
            System.out.println("Detalle Prestación: " + serv.getDetallePrestacion());
        }
    }
}
