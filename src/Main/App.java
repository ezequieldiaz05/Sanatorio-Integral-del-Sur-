package Main;

import controladores.ControladorProveedores;
import controladores.ControladorOrdenes;
import controladores.ControladorDocumentos;
import controladores.ControladorPagos;
import controladores.ControladorRetenciones;
import controladores.ControladorRubros;
import controladores.ControladorItems;

import vista.MenuPrincipal;

import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {

        // Obtener la única instancia de cada Controller (Singleton)
        ControladorProveedores.getInstance();
        ControladorOrdenes.getInstance();
        ControladorDocumentos.getInstance();
        ControladorPagos.getInstance();
        ControladorRetenciones.getInstance();
        ControladorRubros.getInstance();
        ControladorItems.getInstance();

        System.out.println("Sistema iniciado correctamente.");

        // Lanzar la interfaz gráfica en el Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new MenuPrincipal().setVisible(true));
    }
}
