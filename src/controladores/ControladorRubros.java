package controladores;

import entidades.Rubro;

import java.util.ArrayList;
import java.util.List;

// Clase ControladorRubros: Gestiona el catálogo de Rubros de prestación.

// SINGLETON
public class ControladorRubros {

    private static ControladorRubros INSTANCE = null;
    private List<Rubro> rubros;

    private ControladorRubros() {
        this.rubros = new ArrayList<>();
    }

    public static synchronized ControladorRubros getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ControladorRubros();
        }
        return INSTANCE;
    }

    // METODOS DE AGREGACION

    public Boolean agregarRubro(Rubro rubro) {
        if (rubro == null) {
            System.err.println("Error: Rubro nulo");
            return false;
        }

        if (rubro.getIdRubro() == null) {
            System.err.println("Error: El rubro debe tener un ID");
            return false;
        }

        if (existeRubro(rubro.getIdRubro())) {
            System.err.println("Error: Ya existe un rubro con ID " + rubro.getIdRubro());
            return false;
        }

        if (rubro.getNombre() == null || rubro.getNombre().isBlank()) {
            System.err.println("Error: El rubro debe tener un nombre");
            return false;
        }

        rubros.add(rubro);
        System.out.println("Rubro agregado: " + rubro.getNombre());
        return true;
    }

    // METODOS DE BUSQUEDA

    public Rubro buscarRubro(Integer idRubro) {
        if (idRubro == null) {
            return null;
        }

        for (Rubro r : rubros) {
            if (idRubro.equals(r.getIdRubro())) {
                return r;
            }
        }
        return null;
    }

    public Boolean existeRubro(Integer idRubro) {
        return buscarRubro(idRubro) != null;
    }

    // Búsqueda parcial por nombre
    public List<Rubro> buscarPorNombre(String nombre) {
        List<Rubro> resultado = new ArrayList<>();

        if (nombre == null || nombre.isEmpty()) {
            return resultado;
        }

        String busqueda = nombre.toLowerCase();
        for (Rubro r : rubros) {
            if (r.getNombre() != null && r.getNombre().toLowerCase().contains(busqueda)) {
                resultado.add(r);
            }
        }
        return resultado;
    }

    public List<Rubro> getRubros() {
        return new ArrayList<>(rubros);
    }

    public Integer getCantidadRubros() {
        return rubros.size();
    }

    // METODOS DE MODIFICACION

    public Boolean actualizarRubro(Integer idRubro, String nombre, String descripcion) {
        Rubro r = buscarRubro(idRubro);

        if (r == null) {
            System.err.println("Error: Rubro no encontrado");
            return false;
        }

        if (nombre == null || nombre.isBlank()) {
            System.err.println("Error: El nombre no puede estar vacío");
            return false;
        }

        r.setNombre(nombre);
        r.setDescripcion(descripcion);
        System.out.println("Rubro actualizado");
        return true;
    }

    // METODOS DE REPORTE

    public void listarRubros() {
        if (rubros.isEmpty()) {
            System.out.println("No hay rubros registrados");
            return;
        }

        System.out.println("\n=== LISTA DE RUBROS ===");
        for (int i = 0; i < rubros.size(); i++) {
            Rubro r = rubros.get(i);
            System.out.println((i + 1) + ". " + r);
        }
    }
}
