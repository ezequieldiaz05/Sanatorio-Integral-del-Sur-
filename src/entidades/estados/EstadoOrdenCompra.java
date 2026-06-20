package entidades.estados;
import entidades.OrdenCompra;

public interface EstadoOrdenCompra {
    void confirmar(OrdenCompra oc) throws Exception;
    void cancelar(OrdenCompra oc);
}