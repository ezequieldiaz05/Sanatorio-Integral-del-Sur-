package entidades;

public class DetalleDocumento {

    private Double cantidad;
    private Double precioUnitario;
    private Double alicuotaIVA;    // ej: 0.21, 0.105, 0.0
    private Double subtotal;       // cantidad * precioUnitario (neto sin IVA)
    private Double subtotalIVA;    // subtotal * alicuotaIVA

    private Item item;

    public DetalleDocumento() {}

    public static DetalleDocumento crearDetalle(Double cantidad, Double precioUnitario,
                                                Double alicuotaIVA) {
        DetalleDocumento d = new DetalleDocumento();
        d.cantidad = cantidad;
        d.precioUnitario = precioUnitario;
        d.alicuotaIVA = alicuotaIVA;
        d.subtotal = cantidad * precioUnitario;
        d.subtotalIVA = d.subtotal * alicuotaIVA;
        return d;
    }

    public static DetalleDocumento crearDetalle(Item item, Double cantidad,
                                                Double precioUnitario, Double alicuotaIVA) {
        DetalleDocumento d = new DetalleDocumento();
        d.item = item;
        d.cantidad = cantidad;
        d.precioUnitario = precioUnitario;
        d.alicuotaIVA = alicuotaIVA;
        d.subtotal = cantidad * precioUnitario;
        d.subtotalIVA = d.subtotal * alicuotaIVA;
        return d;
    }

    public Double getImporteIVA() { 
        return subtotalIVA; 
    }

    public Double getTotalConIVA() { 
        return subtotal + subtotalIVA; 
    }

    public Item getItem() { 
        return item; 
    }
    public void setItem(Item item) { 
        this.item = item; 
    }

    public Double getCantidad() { 
        return cantidad; 
    }
    public Double getPrecioUnitario() { 
        return precioUnitario; 
    }
    public Double getAlicuotaIVA() { 
        return alicuotaIVA; 
    }
    public Double getSubtotal() { 
        return subtotal; 
    }
    public Double getSubtotalIVA() { 
        return subtotalIVA; 
    }

    @Override
    public String toString() {
        return "DetalleDocumento{item=" + (item != null ? item.getCodigo() : "null")
                + ", cant=" + cantidad + ", precioUnit=" + precioUnitario
                + ", subtotal=$" + subtotal + "}";
    }
}
