package vista;

import controladores.ControladorOrdenes;
import controladores.ControladorProveedores;
import controladores.ControladorItems;
import entidades.OrdenCompra;
import entidades.Proveedor;
import entidades.Item;
import entidades.DetalleOrdenCompra;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// ABM de Órdenes de Compra, con carga de detalles y confirmación.
public class VistaOrdenesCompra extends JFrame {

    private final ControladorOrdenes controlador = ControladorOrdenes.getInstance();
    private final ControladorProveedores controladorProveedores = ControladorProveedores.getInstance();
    private final ControladorItems controladorItems = ControladorItems.getInstance();

    private JComboBox<Proveedor> cmbProveedor;
    private JComboBox<Item> cmbItem;
    private JTextField txtCantidad;
    private JTextField txtPrecio;

    // Detalles de la OC en construcción
    private final List<DetalleOrdenCompra> detallesTemp = new ArrayList<>();
    private DefaultTableModel modeloDetalles;

    private JTable tablaOrdenes;
    private DefaultTableModel modeloOrdenes;

    public VistaOrdenesCompra() {
        setTitle("Gestión de Órdenes de Compra");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 680);
        setLocationRelativeTo(null);

        initComponents();
        cargarCombos();
        refrescarOrdenes();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel norte = new JPanel();
        norte.setLayout(new BoxLayout(norte, BoxLayout.Y_AXIS));

        norte.add(VistaUtil.crearNota(
                "Restricciones: el Nº de OC es único. Debés elegir un proveedor ya registrado "
                + "y cargar al menos un detalle (ítem + cantidad + precio acordado) antes de crear la orden. "
                + "La orden nace en estado \"Borrador\"; al Confirmar el sistema valida el monto contra el "
                + "Límite de Deuda del proveedor: si entra queda \"Emitida\", si lo supera queda \"Pendiente Aprobacion\"."));
        norte.add(Box.createVerticalStrut(8));

        // Cabecera de la OC
        JPanel cabecera = new JPanel(new GridBagLayout());
        cabecera.setBorder(BorderFactory.createTitledBorder("Datos de la Orden"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 5, 4, 5);
        gbc.anchor = GridBagConstraints.WEST;

        cmbProveedor = new JComboBox<>();
        VistaUtil.addCampo(cabecera, gbc, 0, 0, "Proveedor:", cmbProveedor);
        norte.add(cabecera);
        norte.add(Box.createVerticalStrut(8));

        // Constructor de detalles
        JPanel pDetalle = new JPanel(new GridBagLayout());
        pDetalle.setBorder(BorderFactory.createTitledBorder("Agregar Detalle"));
        GridBagConstraints g2 = new GridBagConstraints();
        g2.insets = new Insets(4, 5, 4, 5);
        g2.anchor = GridBagConstraints.WEST;

        cmbItem = new JComboBox<>();
        txtCantidad = new JTextField(8);
        txtPrecio = new JTextField(8);
        JButton btnAgregarDetalle = new JButton("Agregar Detalle");
        btnAgregarDetalle.addActionListener(e -> agregarDetalle());

        VistaUtil.addCampo(pDetalle, g2, 0, 0, "Ítem:", cmbItem);
        VistaUtil.addCampo(pDetalle, g2, 2, 0, "Cantidad:", txtCantidad);
        VistaUtil.addCampo(pDetalle, g2, 4, 0, "Precio Acordado:", txtPrecio);
        g2.gridx = 6; g2.gridy = 0;
        pDetalle.add(btnAgregarDetalle, g2);
        norte.add(pDetalle);
        norte.add(Box.createVerticalStrut(8));

        // Tabla de detalles temporales
        modeloDetalles = new DefaultTableModel(new Object[]{"Ítem", "Cantidad", "Precio", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tablaDetalles = new JTable(modeloDetalles);
        JScrollPane spDet = new JScrollPane(tablaDetalles);
        spDet.setBorder(BorderFactory.createTitledBorder("Detalles de la Orden en construcción"));
        spDet.setPreferredSize(new Dimension(0, 130));
        norte.add(spDet);

        panel.add(norte, BorderLayout.NORTH);

        // Tabla de órdenes existentes
        modeloOrdenes = new DefaultTableModel(
                new Object[]{"Nº OC", "Proveedor", "Fecha", "Total Bruto", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaOrdenes = new JTable(modeloOrdenes);
        JScrollPane spOrd = new JScrollPane(tablaOrdenes);
        spOrd.setBorder(BorderFactory.createTitledBorder("Órdenes de Compra registradas"));
        panel.add(spOrd, BorderLayout.CENTER);

        // Botonera
        JPanel botonera = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton btnCrear = new JButton("Crear Orden");
        JButton btnConfirmar = new JButton("Confirmar Orden");
        JButton btnVerDetalles = new JButton("Ver Detalles (consola)");
        JButton btnLimpiar = new JButton("Limpiar");

        btnCrear.addActionListener(e -> crearOrden());
        btnConfirmar.addActionListener(e -> confirmarOrden());
        btnVerDetalles.addActionListener(e -> verDetalles());
        btnLimpiar.addActionListener(e -> limpiar());

        botonera.add(btnCrear);
        botonera.add(btnConfirmar);
        botonera.add(btnVerDetalles);
        botonera.add(btnLimpiar);
        panel.add(botonera, BorderLayout.SOUTH);

        setContentPane(panel);
    }

    private void cargarCombos() {
        cmbProveedor.removeAllItems();
        for (Proveedor p : controladorProveedores.getProveedores()) {
            cmbProveedor.addItem(p);
        }
        cmbItem.removeAllItems();
        for (Item it : controladorItems.getItems()) {
            cmbItem.addItem(it);
        }
    }

    private void agregarDetalle() {
        Item item = (Item) cmbItem.getSelectedItem();
        if (item == null) {
            JOptionPane.showMessageDialog(this,
                    "No hay ítems cargados. Cargá al menos uno en el módulo de Ítems.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Double cantidad = VistaUtil.parsearDouble(this, txtCantidad.getText(), "Cantidad");
        if (cantidad == null) return;
        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a cero.",
                    "Validación", JOptionPane.WARNING_MESSAGE); return;
        }
        Double precio = VistaUtil.parsearDouble(this, txtPrecio.getText(), "Precio Acordado");
        if (precio == null) return;
        if (precio <= 0) {
            JOptionPane.showMessageDialog(this, "El precio debe ser mayor a cero.",
                    "Validación", JOptionPane.WARNING_MESSAGE); return;
        }

        DetalleOrdenCompra det = new DetalleOrdenCompra(item, cantidad, precio, cantidad * precio);
        detallesTemp.add(det);
        modeloDetalles.addRow(new Object[]{
                item.getCodigo() + " - " + item.getNombre(), cantidad, precio, det.getSubtotal()});

        txtCantidad.setText("");
        txtPrecio.setText("");
    }

    private void crearOrden() {
        Integer nro = controlador.siguienteNroOC();

        Proveedor proveedor = (Proveedor) cmbProveedor.getSelectedItem();
        if (proveedor == null) {
            JOptionPane.showMessageDialog(this,
                    "No hay proveedores cargados. Cargá al menos uno en el módulo de Proveedores.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (detallesTemp.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Agregá al menos un detalle antes de crear la orden.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        OrdenCompra oc = new OrdenCompra(nro, proveedor);
        for (DetalleOrdenCompra det : detallesTemp) {
            oc.agregarDetalle(det);
        }

        if (controlador.agregarOrdenCompra(oc)) {
            JOptionPane.showMessageDialog(this,
                    "Orden OC-" + nro + " creada en estado Borrador. Total: $" + oc.getMontoTotal());
            limpiar();
            refrescarOrdenes();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo crear. Verificá que el Nº de OC no esté repetido.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void confirmarOrden() {
        Integer nro = nroSeleccionado();
        if (nro == null) return;

        if (controlador.confirmarOrden(nro)) {
            OrdenCompra oc = controlador.buscarOrdenCompra(nro);
            JOptionPane.showMessageDialog(this,
                    "Orden OC-" + nro + " confirmada. Estado resultante: " + oc.getEstado());
            refrescarOrdenes();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo confirmar la orden.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verDetalles() {
        Integer nro = nroSeleccionado();
        if (nro == null) return;
        controlador.mostrarDetallesOrden(nro);
        JOptionPane.showMessageDialog(this, "Detalles de OC-" + nro + " impresos en consola.");
    }

    private Integer nroSeleccionado() {
        int f = tablaOrdenes.getSelectedRow();
        if (f < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná una orden de la tabla.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return (Integer) modeloOrdenes.getValueAt(f, 0);
    }

    private void limpiar() {
        txtCantidad.setText("");
        txtPrecio.setText("");
        detallesTemp.clear();
        modeloDetalles.setRowCount(0);
        cargarCombos();
    }

    private void refrescarOrdenes() {
        modeloOrdenes.setRowCount(0);
        for (OrdenCompra oc : controlador.getOrdenes()) {
            modeloOrdenes.addRow(new Object[]{
                    oc.getNroOC(),
                    oc.getProveedor() != null ? oc.getProveedor().getNombreComercial() : "N/A",
                    oc.getFechaEmision(),
                    oc.getMontoTotal(),
                    oc.getEstado()
            });
        }
    }
}
