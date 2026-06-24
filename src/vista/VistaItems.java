package vista;

import controladores.ControladorItems;
import controladores.ControladorRubros;
import entidades.Item;
import entidades.Producto;
import entidades.Servicio;
import entidades.Rubro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

// ABM del catálogo de Ítems (Productos y Servicios).
public class VistaItems extends JFrame {

    private final ControladorItems controlador = ControladorItems.getInstance();
    private final ControladorRubros controladorRubros = ControladorRubros.getInstance();

    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtDescripcion;
    private JTextField txtUnidad;
    private JTextField txtPrecio;
    private JComboBox<Double> cmbAlicuota;
    private JComboBox<Rubro> cmbRubro;
    private JComboBox<String> cmbTipo;

    // Específicos de Producto
    private JTextField txtStock;
    private JTextField txtVencimiento;
    // Específicos de Servicio
    private JTextField txtTipoPrestacion;
    private JTextField txtDetallePrestacion;

    private JTable tabla;
    private DefaultTableModel modelo;

    public VistaItems() {
        setTitle("Gestión de Ítems (Productos y Servicios)");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(780, 560);
        setLocationRelativeTo(null);

        initComponents();
        cargarRubros();
        refrescarTabla();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Formulario
        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBorder(BorderFactory.createTitledBorder("Datos del Ítem"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 5, 4, 5);
        gbc.anchor = GridBagConstraints.WEST;

        txtCodigo = new JTextField(12);
        txtNombre = new JTextField(18);
        txtDescripcion = new JTextField(18);
        txtUnidad = new JTextField(12);
        txtPrecio = new JTextField(10);
        cmbAlicuota = new JComboBox<>(new Double[]{0.0, 2.5, 5.0, 10.5, 21.0, 27.0});
        cmbAlicuota.setSelectedItem(21.0);
        cmbAlicuota.setRenderer(VistaUtil.rendererAlicuota(1.0));
        cmbRubro = new JComboBox<>();
        cmbTipo = new JComboBox<>(new String[]{"Producto", "Servicio"});
        txtStock = new JTextField(10);
        txtVencimiento = VistaUtil.crearCampoFecha();
        txtTipoPrestacion = new JTextField(14);
        txtDetallePrestacion = new JTextField(14);

        // Columna izquierda
        int fila = 0;
        addCampo(formulario, gbc, 0, fila, "Código:", txtCodigo);
        addCampo(formulario, gbc, 2, fila++, "Nombre:", txtNombre);
        addCampo(formulario, gbc, 0, fila, "Descripción:", txtDescripcion);
        addCampo(formulario, gbc, 2, fila++, "Unidad Medida:", txtUnidad);
        addCampo(formulario, gbc, 0, fila, "Precio Base:", txtPrecio);
        addCampo(formulario, gbc, 2, fila++, "Alícuota IVA (%):", cmbAlicuota);
        addCampo(formulario, gbc, 0, fila, "Rubro:", cmbRubro);
        addCampo(formulario, gbc, 2, fila++, "Tipo:", cmbTipo);
        addCampo(formulario, gbc, 0, fila, "Stock (Producto):", txtStock);
        addCampo(formulario, gbc, 2, fila++, "Vencimiento (aaaammdd):", txtVencimiento);
        addCampo(formulario, gbc, 0, fila, "Tipo Prest. (Servicio):", txtTipoPrestacion);
        addCampo(formulario, gbc, 2, fila++, "Detalle Prest.:", txtDetallePrestacion);

        JPanel norte = new JPanel();
        norte.setLayout(new BoxLayout(norte, BoxLayout.Y_AXIS));
        norte.add(VistaUtil.crearNota(
                "Restricciones: el Código es único y obligatorio. Debe seleccionarse un Rubro "
                + "(si no hay ninguno, creá uno primero en el módulo de Rubros). "
                + "La Alícuota IVA tiene valores fijos: 0%, 2.5%, 5%, 10.5%, 21% y 27%. "
                + "El Vencimiento es opcional para Productos (aaaammdd); si el día no existe en el mes indicado se rechaza. "
                + "Comparar Precios muestra en consola los precios del ítem entre distintos proveedores."));
        norte.add(Box.createVerticalStrut(8));
        norte.add(formulario);
        panel.add(norte, BorderLayout.NORTH);

        // Tabla
        modelo = new DefaultTableModel(
                new Object[]{"Código", "Nombre", "Tipo", "Rubro", "Precio Base", "IVA"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modelo);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Botonera
        JPanel botonera = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton btnAgregar = new JButton("Agregar");
        JButton btnComparar = new JButton("Comparar Precios");
        JButton btnLimpiar = new JButton("Limpiar");

        btnAgregar.addActionListener(e -> agregar());
        btnComparar.addActionListener(e -> compararPrecios());
        btnLimpiar.addActionListener(e -> limpiar());

        botonera.add(btnAgregar);
        botonera.add(btnComparar);
        botonera.add(btnLimpiar);
        panel.add(botonera, BorderLayout.SOUTH);

        setContentPane(panel);
    }

    private void addCampo(JPanel p, GridBagConstraints gbc, int x, int y, String etiqueta, JComponent campo) {
        gbc.gridx = x; gbc.gridy = y;
        p.add(new JLabel(etiqueta), gbc);
        gbc.gridx = x + 1;
        p.add(campo, gbc);
    }

    private void cargarRubros() {
        cmbRubro.removeAllItems();
        for (Rubro r : controladorRubros.getRubros()) {
            cmbRubro.addItem(r);
        }
    }

    private void agregar() {
        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        String unidad = txtUnidad.getText().trim();

        if (codigo.isEmpty() || nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Código y Nombre son obligatorios.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Rubro rubro = (Rubro) cmbRubro.getSelectedItem();
        if (rubro == null) {
            JOptionPane.showMessageDialog(this,
                    "Debés seleccionar un rubro. Cargá al menos uno en el módulo de Rubros.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Double precio = parsearDouble(txtPrecio.getText(), "Precio Base");
        if (precio == null) return;
        Double alicuota = (Double) cmbAlicuota.getSelectedItem() / 100.0;

        Item item;
        if ("Producto".equals(cmbTipo.getSelectedItem())) {
            int stock = 0;
            if (!txtStock.getText().trim().isEmpty()) {
                Double s = parsearDouble(txtStock.getText(), "Stock");
                if (s == null) return;
                stock = s.intValue();
            }
            LocalDate vencimiento = parsearFecha(txtVencimiento.getText());
            if (vencimiento == null && !txtVencimiento.getText().trim().isEmpty()) return;
            item = new Producto(codigo, nombre, descripcion, unidad, precio, alicuota, vencimiento, stock);
        } else {
            item = new Servicio(codigo, nombre, descripcion, unidad, precio, alicuota,
                    txtTipoPrestacion.getText().trim(), txtDetallePrestacion.getText().trim());
        }
        item.setRubro(rubro);

        if (controlador.agregarItem(item)) {
            JOptionPane.showMessageDialog(this, "Ítem agregado correctamente.");
            limpiar();
            refrescarTabla();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo agregar el ítem. Verificá que el código sea único.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void compararPrecios() {
        String codigo = txtCodigo.getText().trim();
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            codigo = String.valueOf(modelo.getValueAt(fila, 0));
        }
        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Indicá un código (en el campo o seleccionando una fila) para comparar precios.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // El reporte detallado se imprime por consola desde el controlador.
        controlador.compararPreciosEntreProveedores(codigo);
        JOptionPane.showMessageDialog(this,
                "Comparación de precios para \"" + codigo + "\" impresa en consola.");
    }

    private Double parsearDouble(String texto, String campo) {
        String t = texto.trim();
        if (t.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El campo \"" + campo + "\" es obligatorio.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        try {
            return Double.parseDouble(t.replace(",", "."));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El campo \"" + campo + "\" debe ser numérico.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return null;
        }
    }

    private LocalDate parsearFecha(String texto) {
        String t = texto.trim();
        if (t.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(t);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Fecha inválida: \"" + t + "\". Verificá que el día exista en el mes indicado.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return null;
        }
    }

    private void limpiar() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtUnidad.setText("");
        txtPrecio.setText("");
        cmbAlicuota.setSelectedItem(21.0);
        txtStock.setText("");
        txtVencimiento.setText("");
        txtTipoPrestacion.setText("");
        txtDetallePrestacion.setText("");
        tabla.clearSelection();
        cargarRubros();
    }

    private void refrescarTabla() {
        modelo.setRowCount(0);
        List<Item> items = controlador.getItems();
        for (Item it : items) {
            modelo.addRow(new Object[]{
                    it.getCodigo(),
                    it.getNombre(),
                    it.getClass().getSimpleName(),
                    it.getRubro() != null ? it.getRubro().getNombre() : "",
                    it.getPrecioUnitarioBase(),
                    it.getAlicuotaIVA()
            });
        }
    }
}
