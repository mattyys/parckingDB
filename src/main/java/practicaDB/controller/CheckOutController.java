package practicaDB.controller;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import practicaDB.dto.CocheDTO;
import practicaDB.model.Coche;

public class CheckOutController implements Initializable {

    @FXML
    private JFXButton btn_buscar;

    @FXML
    private JFXButton btn_checkout;

    @FXML
    private Text text_info;

    @FXML
    private TextField tx_matricula;

    private ManagerDBController managerDBController;

    public void setManagerDBController(ManagerDBController managerDBController) {
	this.managerDBController = managerDBController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
	// TODO Auto-generated method stub
	text_info.setText("");
	btn_buscar.setText("");

    }

    @FXML
    void onBuscarCar(ActionEvent event) {
	if (validarDatos()) {
	    String matricula = tx_matricula.getText();
	    
	    Coche coche = managerDBController.search(matricula);
	    
	    if (coche != null) {
		text_info.setText("Coche encontrado: \n" +"Matricula: "+ matricula
			+", Marca: " + coche.getMarca() + ", Modelo: " + coche.getModelo()
			+ ",\nHora de entrada: " + coche.getHoraEntrada());
	    } else {
		text_info.setText("Coche no encontrado");
	    }
	}
    }

    @FXML
    void onCheckOut(ActionEvent event) {
	LocalDateTime horaSalida = LocalDateTime.now();
	if (validarDatos()) {
	    String matricula = tx_matricula.getText();
	    Coche coche =  managerDBController.search(matricula);
	    CocheDTO cocheDTO = new CocheDTO();
	    cocheDTO.setMatricula(matricula);
	    cocheDTO.setMarca(coche.getMarca());
	    cocheDTO.setModelo(coche.getModelo());
	    cocheDTO.setHoraEntrada(coche.getHoraEntrada());
	    cocheDTO.setHoraSalida(horaSalida);
	    if ((managerDBController.update(matricula, horaSalida) == 1)) {
		aviso(true, cocheDTO);
		//limpiar campos
		tx_matricula.setText("");
		text_info.setText("");
		Stage stage = (Stage) btn_checkout.getScene().getWindow();
		stage.close();
	    } else {
		text_info.setText("Error al sacar el coche del parking");
	    }

	} else {
	    aviso(false, null);
	}

    }

    private boolean validarDatos() {
	if (tx_matricula.getText().isEmpty()) {
	    return false;
	}
	return true;
    }

    private void aviso(boolean b, CocheDTO coche) {
	Alert alert = null;
	if (b) {
	    float importe = coche.cantidadAPagar();
	    String matricula = coche.getMatricula();

	    alert = new Alert(AlertType.INFORMATION);
	    alert.setTitle("Informacion");
	    alert.setHeaderText("Coche " + matricula + " sacado del parking");
	    alert.setContentText("El coche " + matricula + " debe pagar: $ " + importe);
	    alert.showAndWait();
	} else {
	    alert = new Alert(AlertType.ERROR);
	    alert.setTitle("Error");
	    alert.setHeaderText("Faltan datos");
	    alert.setContentText("Faltan datos");
	    alert.showAndWait();
	}
    }

}
