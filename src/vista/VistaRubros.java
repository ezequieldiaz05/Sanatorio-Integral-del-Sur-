package vista;

import controladores.ControladorRubros;
import entidades.Rubro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// ABM del catálogo de Rubros.
public class VistaRubros extends JFrame {

    private final ControladorRubros controlador = ControladorRubros.getInstance();

    private JTextField txtNombre;
    private JTextField txtDescripcion;
    private JTable tabla;
    private DefaultTableModel modelo;

    public VistaRubros() {
        setTitle("Gestión de Rubros");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(640, 460);
        setLocationRelativeTo(null);

        initComponents();
        refrescarTabla();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBorder(BorderFactory.createTitledBorder("Datos del Rubro"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        txtNombre = new JTextField(20);
        txtDescripcion = new JTextField(20);

        gbc.gridx = 0; gbc.gridy = 0;
        formulario.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        formulario.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formulario.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1;
        formulario.add(txtDescripcion, gbc);

        JPanel norte = new JPanel();
        norte.setLayout(new BoxLayout(norte, BoxLayout.Y_AXIS));
        norte.add(VistaUtil.crearNota(
                "El ID se asigna automáticamente. El Nombre es obligatorio. "
                + "Los Rubros son el punto de partida del sistema: proveedores e ítems los necesitan "
                + "para poder ser creados. Para editar, seleccioná un rubro de la tabla y usá Actualizar."));
        norte.add(Box.createVerticalStrut(8));
        norte.add(formulario);
        panel.add(norte, BorderLayout.NORTH);

        modelo = new DefaultTableModel(new Object[]{"ID", "Nombre", "Descripción"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.getSelectionModel().addListSelectionListener(e -> cargarSeleccion());
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel botonera = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton btnAgregar = new JButton("Agregar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnLimpiar = new JButton("Limpiar");

        btnAgregar.addActionListener(e -> agregar());
        btnActualizar.addActionListener(e -> actualizar());
        btnLimpiar.addActionListener(e -> limpiar());

        botonera.add(btnAgregar);
        botonera.add(btnActualizar);
        botonera.add(btnLimpiar);
        panel.add(botonera, BorderLayout.SOUTH);

        setContentPane(panel);
    }

    private void agregar() {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El Nombre es obligatorio.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Integer id = controlador.siguienteId();
        Rubro rubro = new Rubro(id, nombre, txtDescripcion.getText().trim());
        if (controlador.agregarRubro(rubro)) {
            JOptionPane.showMessageDialog(this, "Rubro agregado correctamente.");
            limpiar();
            refrescarTabla();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo agregar el rubro. Verificá que el nombre no esté vacío.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná un rubro de la tabla para actualizar.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Integer id = (Integer) modelo.getValueAt(fila, 0);
        if (controlador.actualizarRubro(id, txtNombre.getText().trim(), txtDescripcion.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Rubro actualizado correctamente.");
            limpiar();
            refrescarTabla();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo actualizar. Verificá que el nombre no esté vacío.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return;
        txtNombre.setText(String.valueOf(modelo.getValueAt(fila, 1)));
        Object desc = modelo.getValueAt(fila, 2);
        txtDescripcion.setText(desc != null ? desc.toString() : "");
    }

    private void limpiar() {
        txtNombre.setText("");
        txtDescripcion.setText("");
        tabla.clearSelection();
    }

    private void refrescarTabla() {
        modelo.setRowCount(0);
        List<Rubro> rubros = controlador.getRubros();
        for (Rubro r : rubros) {
            modelo.addRow(new Object[]{r.getIdRubro(), r.getNombre(), r.getDescripcion()});
        }
    }
}
