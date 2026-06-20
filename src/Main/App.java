package Main;

import controladores.ControladorProveedores;
import controladores.ControladorOrdenes;
import controladores.ControladorDocumentos;
import controladores.ControladorPagos;
import controladores.ControladorRetenciones;

public class App {
    public static void main(String[] args) throws Exception {

        // Obtener la única instancia de cada Controller (Singleton)
        ControladorProveedores ctrlProveedores = ControladorProveedores.getInstance();
        ControladorOrdenes ctrlOrdenes = ControladorOrdenes.getInstance();
        ControladorDocumentos ctrlDocumentos = ControladorDocumentos.getInstance();
        ControladorPagos ctrlPagos = ControladorPagos.getInstance();
        ControladorRetenciones ctrlRetenciones = ControladorRetenciones.getInstance();

        System.out.println("Sistema iniciado correctamente.");
        System.out.println("Controllers disponibles:");
        System.out.println("  - ControladorProveedores");
        System.out.println("  - ControladorOrdenes");
        System.out.println("  - ControladorDocumentos");
        System.out.println("  - ControladorPagos");
        System.out.println("  - ControladorRetenciones");
    }
}
