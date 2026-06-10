package entidades;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Proveedor {
    private String CUIT;
    private String razonSocial;
    private String nombreComercial;
    private String domicilio;
    private String telefono;
    private String correoElectronico;
    private String condicionImpositiva; //TODO hacer un array para esto, como es una lista predefinida y se usa para hacer otros cálculos, conviene tenerlo como su propio tipo de elemnto
    private int numeroInscripcionFiscal;
    private Date fechaInicioActividades;
    private float limiteDeuda;
    private List<Rubro> rubros;
    private List<CertificadoExclusion> certificadoExclusion;

    public Proveedor(String CUIT, String razonSocial, String nombreComercial, String domicilio, String telefono, String correoElectronico, String condicionImpositiva, int numeroInscripcionFiscal, Date fechaInicioActividades, float limiteDeuda, List<Rubro> rubros, List<CertificadoExclusion> certificadoExclusion){
        this.CUIT = CUIT;
        this.razonSocial = razonSocial;
        this.nombreComercial = nombreComercial;
        this.domicilio = domicilio;
        this.telefono = telefono;
        this.correoElectronico = correoElectronico;
        this.condicionImpositiva = condicionImpositiva;
        this.numeroInscripcionFiscal = numeroInscripcionFiscal;
        this.fechaInicioActividades = fechaInicioActividades;
        this.limiteDeuda = limiteDeuda;
        this.rubros = new ArrayList<>();
        this.certificadoExclusion = new ArrayList<>();
    }

    public agregarRubros(Rubro rubro){
        rubros.add(rubro);
    }

    public agregarCertificado(CertificadoExclusion certificadoExclusion){
        certificadoExclusion.add(certificadoExclusion);
    }

    //TODO hacer los cuits, para agregar los cuits y demases

    public void actualizarLimiteDeuda(float nuevoLimite){
        if (nuevoLimite<0){
            throw new IllegalArgumentException("El límite no puede ser negativo."); //TODO tirar un try-catch donde sea que lo llame
        }
        this.limiteDeuda = nuevoLimite; 
    }
}