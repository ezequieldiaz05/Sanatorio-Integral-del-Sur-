package vista;

import javax.swing.*;
import java.awt.*;

// Ventana principal del sistema. Da acceso a cada módulo de gestión.
public class MenuPrincipal extends JFrame {

    public MenuPrincipal() {
        setTitle("Sanatorio Integral del Sur - Sistema de Gestión de Compras y Pagos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(560, 480);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título
        JLabel titulo = new JLabel("Menú Principal", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panel.add(titulo, BorderLayout.NORTH);

        // Botonera de módulos
        JPanel botonera = new JPanel(new GridLayout(0, 1, 0, 10));

        botonera.add(crearBoton("Rubros", e -> new VistaRubros().setVisible(true)));
        botonera.add(crearBoton("Ítems (Productos y Servicios)", e -> new VistaItems().setVisible(true)));
        botonera.add(crearBoton("Proveedores", e -> new VistaProveedores().setVisible(true)));
        botonera.add(crearBoton("Órdenes de Compra", e -> new VistaOrdenesCompra().setVisible(true)));
        botonera.add(crearBoton("Documentos Comerciales", e -> new VistaDocumentos().setVisible(true)));
        botonera.add(crearBoton("Órdenes de Pago", e -> new VistaOrdenesPago().setVisible(true)));
        botonera.add(crearBoton("Consultas y Reportes", e -> new VistaReportes().setVisible(true)));

        panel.add(botonera, BorderLayout.CENTER);

        // Salir
        JButton btnSalir = new JButton("Salir");
        btnSalir.addActionListener(e -> System.exit(0));
        JPanel sur = new JPanel();
        sur.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        sur.add(btnSalir);
        panel.add(sur, BorderLayout.SOUTH);

        setContentPane(panel);
    }

    private JButton crearBoton(String texto, java.awt.event.ActionListener accion) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        boton.setPreferredSize(new Dimension(0, 40));
        boton.addActionListener(accion);
        return boton;
    }
}
