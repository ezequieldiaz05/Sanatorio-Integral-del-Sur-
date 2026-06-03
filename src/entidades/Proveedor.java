package entidades;

import java.sql.Date;
import java.util.List;

public class Proveedor {
    private String CUIT;
    private String razonSocial;
    private String nombreComercial;
    private String domicio;
    private String telefono;
    private String correoElectronico;
    private String condicionImpositiva; //TODO hacer un array para esto, como es una lista predefinida y se usa para hacer otros cálculos, conviene tenerlo como su propio tipo de elemnto
    private int numeroInscripcionFiscal;
    private Date fechaInicioActividades;
    private float limiteDeuda;
    private List<Rubro> rubros;
    private List<CertificadoExclusion> certificadoExclusion;

}
