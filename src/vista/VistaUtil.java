package vista;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

// Utilidades compartidas por las vistas Swing.
public final class VistaUtil {

    private VistaUtil() {}

    // Renderer para JComboBox<Double> de alícuotas IVA.
    // factor=1   si los valores ya están en porcentaje (ej. 21.0 → "21 %")
    // factor=100 si los valores son fracción decimal  (ej. 0.21 → "21 %")
    public static ListCellRenderer<Object> rendererAlicuota(double factor) {
        return new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Double) {
                    double pct = (Double) value * factor;
                    setText(pct == (long) pct ? (long) pct + " %" : pct + " %");
                }
                return this;
            }
        };
    }

    // Crea la "nota de restricciones" que se muestra arriba de cada formulario.
    // Se envuelve en un JPanel para que ocupe siempre el ancho completo del contenedor,
    // independientemente del layout manager del padre (BoxLayout o BorderLayout).
    public static JComponent crearNota(String texto) {
        JLabel nota = new JLabel("<html><i>" + texto + "</i></html>");
        nota.setForeground(new Color(90, 90, 90));
        nota.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(nota, BorderLayout.CENTER);
        return wrapper;
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
        texto = texto.trim().replace(',', '.');
        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(padre, "El campo \"" + campo + "\" es obligatorio.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        try {
            return Double.valueOf(texto);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(padre, "El campo \"" + campo + "\" debe ser numérico.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return null;
        }
    }

    // Crea un JTextField de fecha que auto-inserta guiones (aaaammdd → aaaa-mm-dd)
    // y delega la validación de días inexistentes a LocalDate.parse.
    public static JTextField crearCampoFecha() {
        JTextField campo = new JTextField(10);
        campo.setToolTipText("Ingresá la fecha como aaaammdd — se formatea automáticamente");
        ((AbstractDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {

            private String formatear(String digitos) {
                int len = digitos.length();
                if (len <= 4) return digitos;
                if (len <= 6) return digitos.substring(0, 4) + "-" + digitos.substring(4);
                return digitos.substring(0, 4) + "-" + digitos.substring(4, 6) + "-" + digitos.substring(6);
            }

            private String soloDigitos(String s) { return s.replaceAll("[^0-9]", ""); }

            private void aplicar(FilterBypass fb, String raw, AttributeSet attr) throws BadLocationException {
                String digitos = soloDigitos(raw);
                if (digitos.length() > 8) digitos = digitos.substring(0, 8);
                fb.replace(0, fb.getDocument().getLength(), formatear(digitos), attr);
            }

            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                aplicar(fb, fb.getDocument().getText(0, fb.getDocument().getLength()) + string, attr);
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                String actual = fb.getDocument().getText(0, fb.getDocument().getLength());
                aplicar(fb, actual.substring(0, offset) + text + actual.substring(offset + length), attrs);
            }

            @Override
            public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                String actual = fb.getDocument().getText(0, fb.getDocument().getLength());
                String digitosAntes = soloDigitos(actual.substring(0, offset));
                String digitosBorrados = soloDigitos(actual.substring(offset, offset + length));
                String digitosDespues = soloDigitos(actual.substring(offset + length));
                String resultado = digitosBorrados.isEmpty() && !digitosAntes.isEmpty()
                        ? digitosAntes.substring(0, digitosAntes.length() - 1) + digitosDespues
                        : digitosAntes + digitosDespues;
                aplicar(fb, resultado, null);
            }
        });
        return campo;
    }

    // Parsea un Integer mostrando un mensaje si el campo es inválido. Devuelve null si falla.
    public static Integer parsearInt(Component padre, String texto, String campo) {
        texto = texto.trim();
        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(padre, "El campo \"" + campo + "\" es obligatorio.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        try {
            return Integer.valueOf(texto);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(padre, "El campo \"" + campo + "\" debe ser un número entero.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return null;
        }
    }
}