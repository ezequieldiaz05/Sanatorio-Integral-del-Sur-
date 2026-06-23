package vista;

import controladores.ControladorProveedores;
import controladores.ControladorRubros;
import entidades.Proveedor;
import entidades.Rubro;
import entidades.enums.CondicionImpositiva;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

// ABM de Proveedores.
public class VistaProveedores extends JFrame {

    private final ControladorProveedores controlador = ControladorProveedores.getInstance();
    private final ControladorRubros controladorRubros = ControladorRubros.getInstance();

    private JTextField txtCuit;
    private JTextField txtRazonSocial;
    private JTextField txtNombreComercial;
    private JTextField txtDomicilio;
    private JTextField txtTelefono;
    private JTextField txtEmail;
    private JComboBox<CondicionImpositiva> cmbCondicion;
    private JTextField txtNroInscripcion;
    private JTextField txtFechaInicio;
    private JTextField txtLimite;

    private JTable tabla;
    private DefaultTableModel modelo;

    public VistaProveedores() {
        setTitle("Gestión de Proveedores");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(820, 600);
        setLocationRelativeTo(null);

        initComponents();
        refrescarTabla();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Nota de restricciones (paréntesis previo)
        JPanel norte = new JPanel(new BorderLayout(0, 8));
        norte.add(VistaUtil.crearNota(
                "Restricciones: el CUIT es obligatorio y único (clave primaria). "
                + "La Razón Social y el Nombre Comercial son obligatorios. "
                + "El Límite de Deuda no puede ser negativo. "
                + "La Fecha de Inicio usa formato aaaa-mm-dd. "
                + "Para que un proveedor pueda recibir documentos debe tener al menos un rubro asignado."),
                BorderLayout.NORTH);

        // Formulario
        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBorder(BorderFactory.createTitledBorder("Datos del Proveedor"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 5, 4, 5);
        gbc.anchor = GridBagConstraints.WEST;

        txtCuit = new JTextField(14);
        txtRazonSocial = new JTextField(18);
        txtNombreComercial = new JTextField(18);
        txtDomicilio = new JTextField(18);
        txtTelefono = new JTextField(14);
        txtEmail = new JTextField(18);
        cmbCondicion = new JComboBox<>(CondicionImpositiva.values());
        txtNroInscripcion = new JTextField(14);
        txtFechaInicio = new JTextField(12);
        txtLimite = new JTextField(12);

        int fila = 0;
        VistaUtil.addCampo(formulario, gbc, 0, fila, "CUIT:", txtCuit);
        VistaUtil.addCampo(formulario, gbc, 2, fila++, "Condición Impositiva:", cmbCondicion);
        VistaUtil.addCampo(formulario, gbc, 0, fila, "Razón Social:", txtRazonSocial);
        VistaUtil.addCampo(formulario, gbc, 2, fila++, "Nombre Comercial:", txtNombreComercial);
        VistaUtil.addCampo(formulario, gbc, 0, fila, "Domicilio:", txtDomicilio);
        VistaUtil.addCampo(formulario, gbc, 2, fila++, "Teléfono:", txtTelefono);
        VistaUtil.addCampo(formulario, gbc, 0, fila, "Email:", txtEmail);
        VistaUtil.addCampo(formulario, gbc, 2, fila++, "Nro. Inscripción:", txtNroInscripcion);
        VistaUtil.addCampo(formulario, gbc, 0, fila, "Fecha Inicio (aaaa-mm-dd):", txtFechaInicio);
        VistaUtil.addCampo(formulario, gbc, 2, fila++, "Límite de Deuda:", txtLimite);

        norte.add(formulario, BorderLayout.CENTER);
        panel.add(norte, BorderLayout.NORTH);

        // Tabla
        modelo = new DefaultTableModel(
                new Object[]{"CUIT", "Razón Social", "Nombre Comercial", "Condición", "Límite", "Deuda", "Rubros"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modelo);
        tabla.getSelectionModel().addListSelectionListener(e -> cargarSeleccion());
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Botonera
        JPanel botonera = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton btnAgregar = new JButton("Agregar");
        JButton btnLimite = new JButton("Actualizar Límite");
        JButton btnRubro = new JButton("Asignar Rubro");
        JButton btnLimpiar = new JButton("Limpiar");

        btnAgregar.addActionListener(e -> agregar());
        btnLimite.addActionListener(e -> actualizarLimite());
        btnRubro.addActionListener(e -> asignarRubro());
        btnLimpiar.addActionListener(e -> limpiar());

        botonera.add(btnAgregar);
        botonera.add(btnLimite);
        botonera.add(btnRubro);
        botonera.add(btnLimpiar);
        panel.add(botonera, BorderLayout.SOUTH);

        setContentPane(panel);
    }

    private void agregar() {
        String cuit = txtCuit.getText().trim();
        String razon = txtRazonSocial.getText().trim();
        String nombre = txtNombreComercial.getText().trim();

        if (cuit.isEmpty() || razon.isEmpty() || nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "CUIT, Razón Social y Nombre Comercial son obligatorios.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Double limite = VistaUtil.parsearDouble(this, txtLimite.getText(), "Límite de Deuda");
        if (limite == null) return;
        if (limite < 0) {
            JOptionPane.showMessageDialog(this, "El Límite de Deuda no puede ser negativo.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate fechaInicio = parsearFechaOpcional(txtFechaInicio.getText());
        if (fechaInicio == null && !txtFechaInicio.getText().trim().isEmpty()) return;

        CondicionImpositiva condicion = (CondicionImpositiva) cmbCondicion.getSelectedItem();

        Proveedor p = new Proveedor(cuit, razon, condicion, fechaInicio, limite);
        p.setNombreComercial(nombre);
        p.setDomicilio(txtDomicilio.getText().trim());
        p.setTelefono(txtTelefono.getText().trim());
        p.setCorreoElectronico(txtEmail.getText().trim());
        p.setNroInscripcionFiscal(txtNroInscripcion.getText().trim());

        if (controlador.agregarProveedor(p)) {
            JOptionPane.showMessageDialog(this, "Proveedor agregado correctamente.");
            limpiar();
            refrescarTabla();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo agregar. Verificá que el CUIT no esté repetido.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarLimite() {
        String cuit = txtCuit.getText().trim();
        if (cuit.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccioná un proveedor (CUIT) primero.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Double limite = VistaUtil.parsearDouble(this, txtLimite.getText(), "Límite de Deuda");
        if (limite == null) return;

        if (controlador.actualizarLimiteDeuda(cuit, limite)) {
            JOptionPane.showMessageDialog(this, "Límite actualizado.");
            refrescarTabla();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo actualizar. Verificá el CUIT y que el límite no sea negativo.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void asignarRubro() {
        String cuit = txtCuit.getText().trim();
        if (cuit.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccioná un proveedor (CUIT) primero.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        List<Rubro> rubros = controladorRubros.getRubros();
        if (rubros.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay rubros cargados. Cargá al menos uno en el módulo de Rubros.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Rubro rubro = (Rubro) JOptionPane.showInputDialog(this, "Elegí el rubro a asignar:",
                "Asignar Rubro", JOptionPane.QUESTION_MESSAGE, null,
                rubros.toArray(), rubros.get(0));
        if (rubro == null) return;

        if (controlador.agregarRubroAProveedor(cuit, rubro)) {
            JOptionPane.showMessageDialog(this, "Rubro asignado al proveedor.");
            refrescarTabla();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo asignar el rubro.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private LocalDate parsearFechaOpcional(String texto) {
        String t = texto.trim();
        if (t.isEmpty()) return null;
        try {
            return LocalDate.parse(t);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "La fecha debe tener formato aaaa-mm-dd.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return null;
        }
    }

    private void cargarSeleccion() {
        int f = tabla.getSelectedRow();
        if (f < 0) return;
        txtCuit.setText(String.valueOf(modelo.getValueAt(f, 0)));
        txtRazonSocial.setText(String.valueOf(modelo.getValueAt(f, 1)));
        txtNombreComercial.setText(String.valueOf(modelo.getValueAt(f, 2)));
    }

    private void limpiar() {
        txtCuit.setText("");
        txtRazonSocial.setText("");
        txtNombreComercial.setText("");
        txtDomicilio.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
        txtNroInscripcion.setText("");
        txtFechaInicio.setText("");
        txtLimite.setText("");
        tabla.clearSelection();
    }

    private void refrescarTabla() {
        modelo.setRowCount(0);
        for (Proveedor p : controlador.getProveedores()) {
            modelo.addRow(new Object[]{
                    p.getCuit(),
                    p.getRazonSocial(),
                    p.getNombreComercial(),
                    p.getCondicionImpositiva(),
                    p.getLimiteDeuda(),
                    p.getDeudaActual(),
                    p.getRubros().size()
            });
        }
    }
}