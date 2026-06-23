package vista;

import javax.swing.*;
import java.awt.*;

// Utilidades compartidas por las vistas Swing.
public final class VistaUtil {

    private VistaUtil() {}

    // Crea la "nota de restricciones" que se muestra como paréntesis previo
    // arriba de cada formulario de carga.
    public static JComponent crearNota(String texto) {
        JLabel nota = new JLabel("<html><i>" + texto + "</i></html>");
        nota.setForeground(new Color(90, 90, 90));
        nota.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        return nota;
    }

    // Agrega un par etiqueta + campo en la grilla del formulario.
    public static void addCampo(JPanel panel, GridBagConstraints gbc, int x, int y,
                                String etiqueta, JComponent campo) {
        gbc.gridx = x; gbc.gridy = y;
        panel.add(new JLabel(etiqueta), gbc);
        gbc.gridx = x + 1;
        panel.add(campo, gbc);
    }

    // Parsea un Double mostrando un mensaje si el campo es inválido. Devuelve null si falla.
    public static Double parsearDouble(Component padre, String texto, String campo) {
        String t = texto.trim();
        if (t.isEmpty()) {
            JOptionPane.showMessageDialog(padre, "El campo \"" + campo + "\" es obligatorio.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        try {
            return Double.parseDouble(t.replace(",", "."));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(padre, "El campo \"" + campo + "\" debe ser numérico.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return null;
        }
    }

    // Parsea un Integer mostrando un mensaje si el campo es inválido. Devuelve null si falla.
    public static Integer parsearInt(Component padre, String texto, String campo) {
        String t = texto.trim();
        if (t.isEmpty()) {
            JOptionPane.showMessageDialog(padre, "El campo \"" + campo + "\" es obligatorio.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        try {
            return Integer.parseInt(t);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(padre, "El campo \"" + campo + "\" debe ser un número entero.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return null;
        }
    }
}