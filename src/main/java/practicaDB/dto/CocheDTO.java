package practicaDB.dto;


import java.time.LocalDateTime;


import practicaDB.model.Coche;



public class CocheDTO extends Coche {

    private String matricula;
    
    public CocheDTO() {
	super();
    }
    
    public CocheDTO(String matricula,String marca, String modelo, LocalDateTime horaEntrada, LocalDateTime horaSalida) {
	super(marca, modelo, horaEntrada, horaSalida);
	this.matricula = matricula;
    }
    
    public String getMatricula() {
	return matricula;
    }
    
    public void setMatricula(String matricula) {
	this.matricula = matricula;
    }

}
