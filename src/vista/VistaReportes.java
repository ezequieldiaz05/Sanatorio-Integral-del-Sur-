package vista;

import controladores.ControladorProveedores;
import controladores.ControladorDocumentos;
import controladores.ControladorOrdenes;
import controladores.ControladorPagos;
import controladores.ControladorRetenciones;
import entidades.Proveedor;
import entidades.OrdenCompra;
import entidades.OrdenPago;
import entidades.documentos.DocumentoComercial;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

// Consultas Generales y Reportes de Gestión.
public class VistaReportes extends JFrame {

    private final ControladorProveedores cProv = ControladorProveedores.getInstance();
    private final ControladorDocumentos cDoc = ControladorDocumentos.getInstance();
    private final ControladorOrdenes cOrd = ControladorOrdenes.getInstance();
    private final ControladorPagos cPag = ControladorPagos.getInstance();
    private final ControladorRetenciones cRet = ControladorRetenciones.getInstance();

    private JTextArea salida;

    public VistaReportes() {
        setTitle("Consultas y Reportes");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(820, 600);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(VistaUtil.crearNota(
                "Elegí un reporte. Algunos piden datos (proveedor, estado o rango de fechas) "
                + "que se solicitan en una ventana emergente. El resultado se muestra en el panel inferior."),
                BorderLayout.NORTH);

        // Botonera de reportes
        JPanel botonera = new JPanel(new GridLayout(0, 1, 0, 8));
        botonera.setBorder(BorderFactory.createTitledBorder("Reportes disponibles"));

        botonera.add(crearBoton("Cuenta Corriente de un Proveedor", e -> cuentaCorriente()));
        botonera.add(crearBoton("Documentos Pendientes de Pago", e -> documentosPendientes()));
        botonera.add(crearBoton("Órdenes de Compra por Estado", e -> ordenesPorEstado()));
        botonera.add(crearBoton("Pagos por Proveedor", e -> pagosPorProveedor()));
        botonera.add(crearBoton("Libro IVA Compras (por período)", e -> libroIVA()));
        botonera.add(crearBoton("Total Retenido por Impuesto", e -> totalRetenido()));
        panel.add(botonera, BorderLayout.WEST);

        // Salida
        salida = new JTextArea();
        salida.setEditable(false);
        salida.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane sp = new JScrollPane(salida);
        sp.setBorder(BorderFactory.createTitledBorder("Resultado"));
        panel.add(sp, BorderLayout.CENTER);

        setContentPane(panel);
    }

    private JButton crearBoton(String texto, java.awt.event.ActionListener accion) {
        JButton b = new JButton(texto);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.addActionListener(accion);
        return b;
    }

    // --- Reportes ---

    private void cuentaCorriente() {
        Proveedor p = elegirProveedor();
        if (p == null) return;

        StringBuilder sb = new StringBuilder();
        sb.append("=== CUENTA CORRIENTE ===\n");
        sb.append("Proveedor: ").append(p.getNombreComercial()).append(" (CUIT ").append(p.getCuit()).append(")\n\n");

        List<DocumentoComercial> docs = cDoc.getDocumentosPorProveedor(p);
        if (docs.isEmpty()) {
            sb.append("(sin documentos registrados)\n");
        } else {
            for (DocumentoComercial dc : docs) {
                sb.append(String.format("%-12s %-14s Total: $%-12s Impacto: $%-12s Estado: %s%n",
                        dc.getNroDocumento(), dc.getClass().getSimpleName(),
                        dc.getMontoTotal(), dc.getImpactoCuentaCorriente(), dc.getEstadoCancelacion()));
            }
        }
        sb.append("\nSALDO (deuda) según documentos: $").append(cDoc.getCuentaCorriente(p)).append("\n");
        sb.append("Deuda acumulada en el proveedor: $").append(p.getDeudaActual()).append("\n");
        mostrar(sb.toString());
    }

    private void documentosPendientes() {
        List<DocumentoComercial> pendientes = cDoc.getDocumentosPendientes();
        StringBuilder sb = new StringBuilder("=== DOCUMENTOS PENDIENTES DE PAGO ===\n\n");
        if (pendientes.isEmpty()) {
            sb.append("(no hay documentos pendientes)\n");
        } else {
            for (DocumentoComercial dc : pendientes) {
                sb.append(String.format("%-12s %-14s Prov: %-20s Total: $%-12s Estado: %s%n",
                        dc.getNroDocumento(), dc.getClass().getSimpleName(),
                        dc.getProveedor() != null ? dc.getProveedor().getNombreComercial() : "N/A",
                        dc.getMontoTotal(), dc.getEstadoCancelacion()));
            }
        }
        mostrar(sb.toString());
    }

    private void ordenesPorEstado() {
        String[] estados = {"Borrador", "Emitida", "Pendiente Aprobacion"};
        String estado = (String) JOptionPane.showInputDialog(this, "Elegí el estado:",
                "Órdenes por Estado", JOptionPane.QUESTION_MESSAGE, null, estados, estados[0]);
        if (estado == null) return;

        List<OrdenCompra> ordenes = cOrd.getOrdenesPorEstado(estado);
        StringBuilder sb = new StringBuilder("=== ÓRDENES DE COMPRA EN ESTADO: " + estado + " ===\n\n");
        if (ordenes.isEmpty()) {
            sb.append("(no hay órdenes en ese estado)\n");
        } else {
            for (OrdenCompra oc : ordenes) {
                sb.append(String.format("OC-%-6s Prov: %-20s Total: $%-12s%n",
                        oc.getNroOC(),
                        oc.getProveedor() != null ? oc.getProveedor().getNombreComercial() : "N/A",
                        oc.getMontoTotal()));
            }
        }
        mostrar(sb.toString());
    }

    private void pagosPorProveedor() {
        Proveedor p = elegirProveedor();
        if (p == null) return;

        List<OrdenPago> pagos = cPag.getPagosPorProveedor(p);
        StringBuilder sb = new StringBuilder("=== PAGOS DEL PROVEEDOR: " + p.getNombreComercial() + " ===\n\n");
        if (pagos.isEmpty()) {
            sb.append("(sin pagos registrados)\n");
        } else {
            for (OrdenPago op : pagos) {
                sb.append(String.format("OP-%-6s Fecha: %-12s Bruto: $%-10s Ret: $%-10s Neto: $%-10s Estado: %s%n",
                        op.getNroOP(), op.getFechaEmision(), op.getMontoBrutoAPagar(),
                        op.getTotalRetenciones(), op.getMontoNetoAFavor(), op.getEstado()));
            }
        }
        mostrar(sb.toString());
    }

    private void libroIVA() {
        LocalDate desde = pedirFecha("Fecha DESDE (aaaa-mm-dd):");
        if (desde == null) return;
        LocalDate hasta = pedirFecha("Fecha HASTA (aaaa-mm-dd):");
        if (hasta == null) return;

        List<DocumentoComercial> docs = cDoc.getLibroIVACompras(desde, hasta);
        StringBuilder sb = new StringBuilder("=== LIBRO IVA COMPRAS ===\n");
        sb.append("Período: ").append(desde).append(" a ").append(hasta).append("\n\n");
        if (docs.isEmpty()) {
            sb.append("(sin documentos en el período)\n");
        } else {
            sb.append(String.format("%-12s %-12s %-14s %-20s %-10s %-10s%n",
                    "CUIT", "Fecha", "Tipo", "Proveedor", "Neto", "IVA"));
            for (DocumentoComercial dc : docs) {
                sb.append(String.format("%-12s %-12s %-14s %-20s $%-9s $%-9s%n",
                        dc.getProveedor() != null ? dc.getProveedor().getCuit() : "N/A",
                        dc.getFechaEmision(), dc.getClass().getSimpleName(),
                        dc.getProveedor() != null ? dc.getProveedor().getNombreComercial() : "N/A",
                        dc.getMontoNeto(), dc.getIva()));
            }
            sb.append("\nTOTAL IVA del período: $").append(cDoc.getTotalIVA(desde, hasta)).append("\n");
        }
        mostrar(sb.toString());
    }

    private void totalRetenido() {
        String[] tipos = {"IVA", "Ganancias", "IIBB"};
        String tipo = (String) JOptionPane.showInputDialog(this, "Elegí el impuesto:",
                "Total Retenido", JOptionPane.QUESTION_MESSAGE, null, tipos, tipos[0]);
        if (tipo == null) return;

        Double total = cRet.getTotalRetencionesPorTipo(tipo);
        StringBuilder sb = new StringBuilder("=== TOTAL RETENIDO POR IMPUESTO ===\n\n");
        sb.append("Impuesto: ").append(tipo).append("\n");
        sb.append("Total retenido: $").append(total).append("\n");
        sb.append("Cantidad de retenciones registradas: ").append(cRet.getCantidadRetenciones()).append("\n");
        mostrar(sb.toString());
    }

    // --- Helpers ---

    private Proveedor elegirProveedor() {
        List<Proveedor> proveedores = cProv.getProveedores();
        if (proveedores.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay proveedores cargados.",
                    "Sin datos", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return (Proveedor) JOptionPane.showInputDialog(this, "Elegí el proveedor:",
                "Seleccionar Proveedor", JOptionPane.QUESTION_MESSAGE, null,
                proveedores.toArray(), proveedores.get(0));
    }

    private LocalDate pedirFecha(String mensaje) {
        String texto = JOptionPane.showInputDialog(this, mensaje);
        if (texto == null || texto.trim().isEmpty()) return null;
        try {
            return LocalDate.parse(texto.trim());
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido (usá aaaa-mm-dd).",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return null;
        }
    }

    private void mostrar(String texto) {
        salida.setText(texto);
        salida.setCaretPosition(0);
    }
}
