package vista;

import controladores.ControladorDocumentos;
import controladores.ControladorProveedores;
import controladores.ControladorItems;
import entidades.documentos.DocumentoComercial;
import entidades.documentos.Factura;
import entidades.documentos.NotaCredito;
import entidades.documentos.NotaDebito;
import entidades.Proveedor;
import entidades.Item;
import entidades.DetalleDocumento;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// ABM de Documentos Comerciales: Facturas, Notas de Crédito y Notas de Débito.
public class VistaDocumentos extends JFrame {

    private final ControladorDocumentos controlador = ControladorDocumentos.getInstance();
    private final ControladorProveedores controladorProveedores = ControladorProveedores.getInstance();
    private final ControladorItems controladorItems = ControladorItems.getInstance();

    private JComboBox<Proveedor> cmbProveedor;
    private JComboBox<String> cmbTipo;
    private JComboBox<DocumentoComercial> cmbAfectado;

    private JComboBox<Item> cmbItem;
    private JTextField txtCantidad;
    private JTextField txtPrecio;
    private JComboBox<Double> cmbAlicuota;

    private final List<DetalleDocumento> detallesTemp = new ArrayList<>();
    private DefaultTableModel modeloDetalles;

    private JTable tablaDocumentos;
    private DefaultTableModel modeloDocumentos;

    public VistaDocumentos() {
        setTitle("Gestión de Documentos Comerciales");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(940, 700);
        setLocationRelativeTo(null);

        initComponents();
        cargarCombos();
        refrescarDocumentos();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel norte = new JPanel();
        norte.setLayout(new BoxLayout(norte, BoxLayout.Y_AXIS));

        norte.add(VistaUtil.crearNota(
                "Restricciones: el Nº de documento es único. Hay que elegir un proveedor registrado "
                + "y cargar al menos un detalle. El proveedor debe tener al menos un rubro asignado "
                + "(coherencia de conceptos), si no el documento se rechaza. "
                + "Las Notas de Crédito y de Débito requieren seleccionar el documento afectado "
                + "(una Factura previa del mismo proveedor). "
                + "Impacto en cuenta corriente: Factura y Nota de Débito suman deuda; Nota de Crédito la resta."));
        norte.add(Box.createVerticalStrut(8));

        // Cabecera
        JPanel cabecera = new JPanel(new GridBagLayout());
        cabecera.setBorder(BorderFactory.createTitledBorder("Datos del Documento"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 5, 4, 5);
        gbc.anchor = GridBagConstraints.WEST;

        cmbProveedor = new JComboBox<>();
        cmbTipo = new JComboBox<>(new String[]{"Factura", "NotaCredito", "NotaDebito"});
        cmbAfectado = new JComboBox<>();
        cmbAfectado.setEnabled(false);
        cmbTipo.addActionListener(e -> actualizarEstadoAfectado());
        cmbProveedor.addActionListener(e -> actualizarDocumentosAfectados());

        VistaUtil.addCampo(cabecera, gbc, 0, 0, "Proveedor:", cmbProveedor);
        VistaUtil.addCampo(cabecera, gbc, 0, 1, "Tipo:", cmbTipo);
        VistaUtil.addCampo(cabecera, gbc, 2, 1, "Doc. Afectado (NC/ND):", cmbAfectado);
        norte.add(cabecera);
        norte.add(Box.createVerticalStrut(8));

        // Constructor de detalles
        JPanel pDetalle = new JPanel(new GridBagLayout());
        pDetalle.setBorder(BorderFactory.createTitledBorder("Agregar Detalle"));
        GridBagConstraints g2 = new GridBagConstraints();
        g2.insets = new Insets(4, 5, 4, 5);
        g2.anchor = GridBagConstraints.WEST;

        cmbItem = new JComboBox<>();
        txtCantidad = new JTextField(7);
        txtPrecio = new JTextField(8);
        cmbAlicuota = new JComboBox<>(new Double[]{0.0, 0.025, 0.05, 0.105, 0.21, 0.27});
        cmbAlicuota.setSelectedItem(0.21);
        cmbAlicuota.setRenderer(VistaUtil.rendererAlicuota(100.0));
        JButton btnAgregarDetalle = new JButton("Agregar Detalle");
        btnAgregarDetalle.addActionListener(e -> agregarDetalle());

        VistaUtil.addCampo(pDetalle, g2, 0, 0, "Ítem:", cmbItem);
        VistaUtil.addCampo(pDetalle, g2, 2, 0, "Cantidad:", txtCantidad);
        VistaUtil.addCampo(pDetalle, g2, 4, 0, "Precio Unit.:", txtPrecio);
        VistaUtil.addCampo(pDetalle, g2, 6, 0, "Alícuota IVA:", cmbAlicuota);
        g2.gridx = 8; g2.gridy = 0;
        pDetalle.add(btnAgregarDetalle, g2);
        norte.add(pDetalle);
        norte.add(Box.createVerticalStrut(8));

        modeloDetalles = new DefaultTableModel(
                new Object[]{"Ítem", "Cantidad", "Precio", "Alícuota", "Subtotal", "IVA"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tablaDetalles = new JTable(modeloDetalles);
        JScrollPane spDet = new JScrollPane(tablaDetalles);
        spDet.setBorder(BorderFactory.createTitledBorder("Detalles del documento en construcción"));
        spDet.setPreferredSize(new Dimension(0, 120));
        norte.add(spDet);

        panel.add(norte, BorderLayout.NORTH);

        // Tabla de documentos
        modeloDocumentos = new DefaultTableModel(
                new Object[]{"Nº", "Tipo", "Proveedor", "Fecha", "Neto", "IVA", "Total", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaDocumentos = new JTable(modeloDocumentos);
        JScrollPane spDoc = new JScrollPane(tablaDocumentos);
        spDoc.setBorder(BorderFactory.createTitledBorder("Documentos registrados"));
        panel.add(spDoc, BorderLayout.CENTER);

        // Botonera
        JPanel botonera = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton btnCrear = new JButton("Crear Documento");
        JButton btnVerDetalles = new JButton("Ver Detalles (consola)");
        JButton btnLimpiar = new JButton("Limpiar");

        btnCrear.addActionListener(e -> crearDocumento());
        btnVerDetalles.addActionListener(e -> verDetalles());
        btnLimpiar.addActionListener(e -> limpiar());

        botonera.add(btnCrear);
        botonera.add(btnVerDetalles);
        botonera.add(btnLimpiar);
        panel.add(botonera, BorderLayout.SOUTH);

        setContentPane(panel);
    }

    private void actualizarEstadoAfectado() {
        boolean requiere = !"Factura".equals(cmbTipo.getSelectedItem());
        cmbAfectado.setEnabled(requiere);
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
        actualizarDocumentosAfectados();
    }

    private void actualizarDocumentosAfectados() {
        cmbAfectado.removeAllItems();
        Proveedor p = (Proveedor) cmbProveedor.getSelectedItem();
        if (p == null) return;
        for (DocumentoComercial dc : controlador.getDocumentos()) {
            if (p.equals(dc.getProveedor())) {
                cmbAfectado.addItem(dc);
            }
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
        Double precio = VistaUtil.parsearDouble(this, txtPrecio.getText(), "Precio Unit.");
        if (precio == null) return;
        Double alicuota = (Double) cmbAlicuota.getSelectedItem();

        DetalleDocumento det = DetalleDocumento.crearDetalle(item, cantidad, precio, alicuota);
        detallesTemp.add(det);
        modeloDetalles.addRow(new Object[]{
                item.getCodigo() + " - " + item.getNombre(),
                cantidad, precio, alicuota, det.getSubtotal(), det.getSubtotalIVA()});

        txtCantidad.setText("");
        txtPrecio.setText("");
        cmbAlicuota.setSelectedItem(0.21);
    }

    private void crearDocumento() {
        String nro = controlador.siguienteNroDocumento();
        Proveedor proveedor = (Proveedor) cmbProveedor.getSelectedItem();
        if (proveedor == null) {
            JOptionPane.showMessageDialog(this,
                    "No hay proveedores cargados. Cargá al menos uno en el módulo de Proveedores.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (detallesTemp.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Agregá al menos un detalle antes de crear el documento.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String tipo = (String) cmbTipo.getSelectedItem();
        DocumentoComercial doc;

        if ("Factura".equals(tipo)) {
            doc = new Factura(nro, proveedor);
        } else {
            DocumentoComercial afectado = (DocumentoComercial) cmbAfectado.getSelectedItem();
            if (afectado == null) {
                JOptionPane.showMessageDialog(this,
                        "Las Notas de Crédito/Débito requieren un documento afectado. "
                        + "Cargá primero una Factura del mismo proveedor.",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if ("NotaCredito".equals(tipo)) {
                doc = new NotaCredito(nro, proveedor, afectado);
            } else {
                doc = new NotaDebito(nro, proveedor, afectado);
            }
        }

        for (DetalleDocumento det : detallesTemp) {
            doc.agregarDetalle(det);
        }

        if (controlador.agregarDocumento(doc)) {
            JOptionPane.showMessageDialog(this,
                    tipo + " " + nro + " registrado. Total: $" + doc.getMontoTotal());
            limpiar();
            refrescarDocumentos();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo registrar. Verificá que el Nº no esté repetido y que el proveedor "
                    + "tenga al menos un rubro asignado (coherencia de conceptos).",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verDetalles() {
        int f = tablaDocumentos.getSelectedRow();
        if (f < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná un documento de la tabla.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String nro = String.valueOf(modeloDocumentos.getValueAt(f, 0));
        controlador.mostrarDetallesDocumento(nro);
        JOptionPane.showMessageDialog(this, "Detalles del documento " + nro + " impresos en consola.");
    }

    private void limpiar() {
        txtCantidad.setText("");
        txtPrecio.setText("");
        cmbAlicuota.setSelectedItem(0.21);
        detallesTemp.clear();
        modeloDetalles.setRowCount(0);
        cargarCombos();
        actualizarEstadoAfectado();
    }

    private void refrescarDocumentos() {
        modeloDocumentos.setRowCount(0);
        for (DocumentoComercial dc : controlador.getDocumentos()) {
            modeloDocumentos.addRow(new Object[]{
                    dc.getNroDocumento(),
                    dc.getClass().getSimpleName(),
                    dc.getProveedor() != null ? dc.getProveedor().getNombreComercial() : "N/A",
                    dc.getFechaEmision(),
                    dc.getMontoNeto(),
                    dc.getIva(),
                    dc.getMontoTotal(),
                    dc.getEstadoCancelacion()
            });
        }
    }
}
