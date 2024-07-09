package practicaDB.dto;


import java.time.LocalDateTime;


import practicaDB.model.Coche;



// TODO: Auto-generated Javadoc
/**
 * The Class CocheDTO.
 */
public class CocheDTO extends Coche {

    /** The matricula. */
    private String matricula;
    
    /**
     * Instantiates a new coche DTO.
     */
    public CocheDTO() {
	super();
    }
    
    /**
     * Instantiates a new coche DTO.
     *
     * @param matricula the matricula
     * @param marca the marca
     * @param modelo the modelo
     * @param horaEntrada the hora entrada
     * @param horaSalida the hora salida
     */
    public CocheDTO(String matricula,String marca, String modelo, LocalDateTime horaEntrada, LocalDateTime horaSalida) {
	super(marca, modelo, horaEntrada, horaSalida);
	this.matricula = matricula;
    }
    
    /**
     * Gets the matricula.
     *
     * @return the matricula
     */
    public String getMatricula() {
	return matricula;
    }
    
    /**
     * Sets the matricula.
     *
     * @param matricula the new matricula
     */
    public void setMatricula(String matricula) {
	this.matricula = matricula;
    }

}
