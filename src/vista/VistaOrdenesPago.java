package vista;

import controladores.ControladorPagos;
import controladores.ControladorProveedores;
import controladores.ControladorDocumentos;
import entidades.OrdenPago;
import entidades.Proveedor;
import entidades.RetencionImpositiva;
import entidades.MedioPago;
import entidades.Efectivo;
import entidades.Transferencia;
import entidades.ChequePropio;
import entidades.ChequeTercero;
import entidades.documentos.DocumentoComercial;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// ABM de Órdenes de Pago: selección de documentos, retenciones y medios de pago.
public class VistaOrdenesPago extends JFrame {

    private final ControladorPagos controlador = ControladorPagos.getInstance();
    private final ControladorProveedores controladorProveedores = ControladorProveedores.getInstance();
    private final ControladorDocumentos controladorDocumentos = ControladorDocumentos.getInstance();

    private JTextField txtNroOP;
    private JComboBox<Proveedor> cmbProveedor;

    // Documentos a cancelar
    private JComboBox<DocumentoComercial> cmbDocPendiente;
    private final List<DocumentoComercial> docsTemp = new ArrayList<>();
    private DefaultTableModel modeloDocs;

    // Retenciones
    private JComboBox<String> cmbTipoRet;
    private JTextField txtPorcentaje;
    private JTextField txtBase;
    private final List<RetencionImpositiva> retencionesTemp = new ArrayList<>();
    private DefaultTableModel modeloRet;

    // Medios de pago
    private JComboBox<String> cmbTipoMedio;
    private JTextField txtMonto;
    private JTextField txtReferencia;
    private JTextField txtBanco;
    private JTextField txtFirmante;
    private final List<MedioPago> mediosTemp = new ArrayList<>();
    private DefaultTableModel modeloMedios;

    private JTable tablaOP;
    private DefaultTableModel modeloOP;

    public VistaOrdenesPago() {
        setTitle("Gestión de Órdenes de Pago");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(960, 740);
        setLocationRelativeTo(null);

        initComponents();
        cargarProveedores();
        refrescarOP();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel norte = new JPanel();
        norte.setLayout(new BoxLayout(norte, BoxLayout.Y_AXIS));

        norte.add(VistaUtil.crearNota(
                "Restricciones: el Nº de OP es único. Elegí un proveedor y agregá al menos un documento "
                + "pendiente a cancelar (los documentos ya Cancelados no se ofrecen). "
                + "Las retenciones (IVA, Ganancias, IIBB) se descuentan del monto bruto; el Neto a Pagar = Bruto - Retenciones. "
                + "Podés combinar varios medios de pago. Al Emitir la OP, los documentos asociados pasan a estado \"Cancelado\"."));
        norte.add(Box.createVerticalStrut(8));

        // Cabecera
        JPanel cabecera = new JPanel(new GridBagLayout());
        cabecera.setBorder(BorderFactory.createTitledBorder("Datos de la Orden de Pago"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 5, 4, 5);
        gbc.anchor = GridBagConstraints.WEST;

        txtNroOP = new JTextField(10);
        cmbProveedor = new JComboBox<>();
        cmbProveedor.addActionListener(e -> cargarDocumentosPendientes());
        VistaUtil.addCampo(cabecera, gbc, 0, 0, "Nº OP:", txtNroOP);
        VistaUtil.addCampo(cabecera, gbc, 2, 0, "Proveedor:", cmbProveedor);
        norte.add(cabecera);
        norte.add(Box.createVerticalStrut(8));

        norte.add(crearPestanias());

        panel.add(norte, BorderLayout.NORTH);

        // Tabla de OP
        modeloOP = new DefaultTableModel(
                new Object[]{"Nº OP", "Proveedor", "Fecha", "Bruto", "Retenciones", "Neto", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaOP = new JTable(modeloOP);
        JScrollPane spOP = new JScrollPane(tablaOP);
        spOP.setBorder(BorderFactory.createTitledBorder("Órdenes de Pago registradas"));
        panel.add(spOP, BorderLayout.CENTER);

        // Botonera
        JPanel botonera = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton btnCrear = new JButton("Crear OP");
        JButton btnEmitir = new JButton("Emitir OP");
        JButton btnDetalles = new JButton("Ver Detalles (consola)");
        JButton btnLimpiar = new JButton("Limpiar");

        btnCrear.addActionListener(e -> crearOP());
        btnEmitir.addActionListener(e -> emitirOP());
        btnDetalles.addActionListener(e -> verDetalles());
        btnLimpiar.addActionListener(e -> limpiar());

        botonera.add(btnCrear);
        botonera.add(btnEmitir);
        botonera.add(btnDetalles);
        botonera.add(btnLimpiar);
        panel.add(botonera, BorderLayout.SOUTH);

        setContentPane(panel);
    }

    private JTabbedPane crearPestanias() {
        JTabbedPane tabs = new JTabbedPane();

        // --- Documentos ---
        JPanel pDocs = new JPanel(new BorderLayout(5, 5));
        JPanel barraDocs = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cmbDocPendiente = new JComboBox<>();
        JButton btnAddDoc = new JButton("Agregar Documento");
        btnAddDoc.addActionListener(e -> agregarDocumento());
        barraDocs.add(new JLabel("Documento pendiente:"));
        barraDocs.add(cmbDocPendiente);
        barraDocs.add(btnAddDoc);
        modeloDocs = tablaSoloLectura(new Object[]{"Nº", "Tipo", "Total", "Estado"});
        pDocs.add(barraDocs, BorderLayout.NORTH);
        pDocs.add(new JScrollPane(new JTable(modeloDocs)), BorderLayout.CENTER);
        tabs.addTab("Documentos", pDocs);

        // --- Retenciones ---
        JPanel pRet = new JPanel(new BorderLayout(5, 5));
        JPanel barraRet = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cmbTipoRet = new JComboBox<>(new String[]{"IVA", "Ganancias", "IIBB"});
        txtPorcentaje = new JTextField(6);
        txtBase = new JTextField(8);
        JButton btnAddRet = new JButton("Agregar Retención");
        btnAddRet.addActionListener(e -> agregarRetencion());
        barraRet.add(new JLabel("Tipo:"));
        barraRet.add(cmbTipoRet);
        barraRet.add(new JLabel("Porcentaje:"));
        barraRet.add(txtPorcentaje);
        barraRet.add(new JLabel("Base imponible:"));
        barraRet.add(txtBase);
        barraRet.add(btnAddRet);
        modeloRet = tablaSoloLectura(new Object[]{"Tipo", "Porcentaje", "Base", "Retenido"});
        pRet.add(barraRet, BorderLayout.NORTH);
        pRet.add(new JScrollPane(new JTable(modeloRet)), BorderLayout.CENTER);
        tabs.addTab("Retenciones", pRet);

        // --- Medios de pago ---
        JPanel pMedios = new JPanel(new BorderLayout(5, 5));
        JPanel barraMedios = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cmbTipoMedio = new JComboBox<>(new String[]{"Efectivo", "Transferencia", "ChequePropio", "ChequeTercero"});
        txtMonto = new JTextField(8);
        txtReferencia = new JTextField(8);
        txtBanco = new JTextField(8);
        txtFirmante = new JTextField(8);
        JButton btnAddMedio = new JButton("Agregar Medio");
        btnAddMedio.addActionListener(e -> agregarMedio());
        barraMedios.add(new JLabel("Tipo:"));
        barraMedios.add(cmbTipoMedio);
        barraMedios.add(new JLabel("Monto:"));
        barraMedios.add(txtMonto);
        barraMedios.add(new JLabel("Nº/Ref:"));
        barraMedios.add(txtReferencia);
        barraMedios.add(new JLabel("Banco/Cuenta:"));
        barraMedios.add(txtBanco);
        barraMedios.add(new JLabel("Firmante:"));
        barraMedios.add(txtFirmante);
        barraMedios.add(btnAddMedio);
        modeloMedios = tablaSoloLectura(new Object[]{"Tipo", "Monto", "Nº/Ref", "Banco/Cuenta"});
        pMedios.add(barraMedios, BorderLayout.NORTH);
        pMedios.add(new JScrollPane(new JTable(modeloMedios)), BorderLayout.CENTER);
        tabs.addTab("Medios de Pago", pMedios);

        tabs.setPreferredSize(new Dimension(0, 220));
        return tabs;
    }

    private DefaultTableModel tablaSoloLectura(Object[] columnas) {
        return new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private void cargarProveedores() {
        cmbProveedor.removeAllItems();
        for (Proveedor p : controladorProveedores.getProveedores()) {
            cmbProveedor.addItem(p);
        }
        cargarDocumentosPendientes();
    }

    private void cargarDocumentosPendientes() {
        if (cmbDocPendiente == null) return;
        cmbDocPendiente.removeAllItems();
        Proveedor proveedor = (Proveedor) cmbProveedor.getSelectedItem();
        if (proveedor == null) return;
        for (DocumentoComercial dc : controladorDocumentos.getDocumentosPendientes()) {
            if (proveedor.equals(dc.getProveedor())) {
                cmbDocPendiente.addItem(dc);
            }
        }
    }

    private void agregarDocumento() {
        DocumentoComercial doc = (DocumentoComercial) cmbDocPendiente.getSelectedItem();
        if (doc == null) {
            JOptionPane.showMessageDialog(this,
                    "El proveedor seleccionado no tiene documentos pendientes.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (docsTemp.contains(doc)) {
            JOptionPane.showMessageDialog(this, "Ese documento ya fue agregado.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        docsTemp.add(doc);
        modeloDocs.addRow(new Object[]{
                doc.getNroDocumento(), doc.getClass().getSimpleName(),
                doc.getMontoTotal(), doc.getEstadoCancelacion()});
    }

    private void agregarRetencion() {
        Double porcentaje = VistaUtil.parsearDouble(this, txtPorcentaje.getText(), "Porcentaje");
        if (porcentaje == null) return;
        if (porcentaje <= 0 || porcentaje > 100) {
            JOptionPane.showMessageDialog(this, "El porcentaje debe estar entre 0 y 100.",
                    "Validación", JOptionPane.WARNING_MESSAGE); return;
        }
        Double base = VistaUtil.parsearDouble(this, txtBase.getText(), "Base imponible");
        if (base == null) return;
        if (base <= 0) {
            JOptionPane.showMessageDialog(this, "La base imponible debe ser mayor a cero.",
                    "Validación", JOptionPane.WARNING_MESSAGE); return;
        }

        String tipo = (String) cmbTipoRet.getSelectedItem();
        RetencionImpositiva ret = RetencionImpositiva.crearRetencion(tipo, porcentaje, base);
        retencionesTemp.add(ret);
        modeloRet.addRow(new Object[]{tipo, porcentaje, base, ret.getMontoRetenido()});

        txtPorcentaje.setText("");
        txtBase.setText("");
    }

    private void agregarMedio() {
        Double monto = VistaUtil.parsearDouble(this, txtMonto.getText(), "Monto");
        if (monto == null) return;
        if (monto <= 0) {
            JOptionPane.showMessageDialog(this, "El monto debe ser mayor a cero.",
                    "Validación", JOptionPane.WARNING_MESSAGE); return;
        }

        String tipo = (String) cmbTipoMedio.getSelectedItem();
        String ref = txtReferencia.getText().trim();
        String banco = txtBanco.getText().trim();
        String firmante = txtFirmante.getText().trim();

        MedioPago medio;
        switch (tipo) {
            case "Transferencia":
                medio = new Transferencia(monto, ref, banco);
                break;
            case "ChequePropio":
                medio = new ChequePropio(monto, ref, banco, null, null, firmante);
                break;
            case "ChequeTercero":
                medio = new ChequeTercero(monto, ref, banco, null, null, firmante);
                break;
            default:
                medio = new Efectivo(monto);
        }
        mediosTemp.add(medio);
        modeloMedios.addRow(new Object[]{tipo, monto, ref, banco});

        txtMonto.setText("");
        txtReferencia.setText("");
        txtBanco.setText("");
        txtFirmante.setText("");
    }

    private void crearOP() {
        Integer nro = VistaUtil.parsearInt(this, txtNroOP.getText(), "Nº OP");
        if (nro == null) return;

        Proveedor proveedor = (Proveedor) cmbProveedor.getSelectedItem();
        if (proveedor == null) {
            JOptionPane.showMessageDialog(this,
                    "No hay proveedores cargados. Cargá al menos uno en el módulo de Proveedores.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (docsTemp.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Agregá al menos un documento a cancelar antes de crear la OP.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        OrdenPago op = new OrdenPago(nro, proveedor);
        for (DocumentoComercial doc : docsTemp) {
            op.seleccionarDocumentos(doc);
        }
        for (RetencionImpositiva ret : retencionesTemp) {
            op.agregarRetencion(ret);
        }
        for (MedioPago medio : mediosTemp) {
            op.agregarMedioPago(medio);
        }

        double totalMedios = op.getTotalMediosPago();
        double montoNeto = op.getMontoNetoAFavor();
        if (Math.abs(totalMedios - montoNeto) > 0.001) {
            JOptionPane.showMessageDialog(this,
                    String.format("Los medios de pago ($%.2f) no cubren el monto neto a pagar ($%.2f).",
                            totalMedios, montoNeto),
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (controlador.agregarOrdenPago(op)) {
            JOptionPane.showMessageDialog(this,
                    "OP-" + nro + " creada.\nBruto: $" + op.getMontoBrutoAPagar()
                    + "\nRetenciones: $" + op.getTotalRetenciones()
                    + "\nNeto a pagar: $" + op.getMontoNetoAFavor());
            limpiar();
            refrescarOP();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo crear. Verificá que el Nº de OP no esté repetido.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void emitirOP() {
        Integer nro = nroSeleccionado();
        if (nro == null) return;
        OrdenPago op = controlador.buscarOrdenPago(nro);
        if (op == null) return;

        op.emitirOrdenPago();
        JOptionPane.showMessageDialog(this,
                "OP-" + nro + " emitida. Los documentos asociados quedaron Cancelados.");
        refrescarOP();
    }

    private void verDetalles() {
        Integer nro = nroSeleccionado();
        if (nro == null) return;
        controlador.mostrarDetallesOrdenPago(nro);
        JOptionPane.showMessageDialog(this, "Detalles de OP-" + nro + " impresos en consola.");
    }

    private Integer nroSeleccionado() {
        int f = tablaOP.getSelectedRow();
        if (f < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná una OP de la tabla.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return (Integer) modeloOP.getValueAt(f, 0);
    }

    private void limpiar() {
        txtNroOP.setText("");
        docsTemp.clear();
        modeloDocs.setRowCount(0);
        retencionesTemp.clear();
        modeloRet.setRowCount(0);
        mediosTemp.clear();
        modeloMedios.setRowCount(0);
        txtPorcentaje.setText("");
        txtBase.setText("");
        txtMonto.setText("");
        txtReferencia.setText("");
        txtBanco.setText("");
        txtFirmante.setText("");
        cargarProveedores();
    }

    private void refrescarOP() {
        modeloOP.setRowCount(0);
        for (OrdenPago op : controlador.getOrdenesPago()) {
            modeloOP.addRow(new Object[]{
                    op.getNroOP(),
                    op.getProveedor() != null ? op.getProveedor().getNombreComercial() : "N/A",
                    op.getFechaEmision(),
                    op.getMontoBrutoAPagar(),
                    op.getTotalRetenciones(),
                    op.getMontoNetoAFavor(),
                    op.getEstado()
            });
        }
    }
}
