package practicaDB.controller;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
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

    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss / dd-MM-yyyy");

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

	    text_info.setText(setTextInfo(coche, matricula));
	}
    }

    @FXML
    void onCheckOut(ActionEvent event) {
	LocalDateTime horaSalida = LocalDateTime.now();
	if (validarDatos()) {
	    String matricula = tx_matricula.getText();
	    Coche coche = managerDBController.search(matricula);
	    if (coche == null) {
		createAlert(AlertType.WARNING, "Advertencia", "El coche no esta en el sistema",
			"El Parking no tiene el coche con matricula: " + matricula).showAndWait();
	    } else {
		if (coche.getHoraSalida() != null) {
		    createAlert(AlertType.WARNING, "Advertencia", "El coche ya salio del parking",
			    "El Parking ya no tiene el coche con matricula: " + matricula).showAndWait();
		} else {
		    CocheDTO cocheDTO = new CocheDTO();
		    cocheDTO.setMatricula(matricula);
		    cocheDTO.setMarca(coche.getMarca());
		    cocheDTO.setModelo(coche.getModelo());
		    cocheDTO.setHoraEntrada(coche.getHoraEntrada());
		    cocheDTO.setHoraSalida(horaSalida);
		    if ((managerDBController.update(matricula, horaSalida) == 1)) {
			aviso(true, cocheDTO);
			// limpiar campos
			tx_matricula.setText("");
			text_info.setText("");
			Stage stage = (Stage) btn_checkout.getScene().getWindow();
			stage.close();
		    } else {
			text_info.setText("Error al sacar el coche del parking");
		    }
		}
	    }

	} else {
	    aviso(false, null);
	}

    }

    @FXML
    void findCarKey(KeyEvent event) {
	String matricula = tx_matricula.getText();
	if (managerDBController.search(matricula) != null) {
	    Coche coche = managerDBController.search(matricula);
	}

    }

    private boolean validarDatos() {
	if (tx_matricula.getText().isEmpty()) {
	    return false;
	}
	return true;
    }

    private void aviso(boolean b, CocheDTO coche) {
	if (b) {
	    float importe = coche.cantidadAPagar();
	    String matricula = coche.getMatricula();

	    createAlert(AlertType.INFORMATION, "Informacion", "Coche " + matricula + " sacado del parking",
		    "El coche " + matricula + " debe pagar: $ " + importe).showAndWait();
	} else {
	    createAlert(AlertType.ERROR, "Error", "Faltan datos", "Faltan datos").showAndWait();

	}
    }

    private Alert createAlert(AlertType type, String title, String header, String content) {
	Alert alert = new Alert(type);
	alert.setTitle(title);
	alert.setHeaderText(header);
	alert.setContentText(content);
	return alert;
    }

    private String setTextInfo(Coche coche, String matricula) {
	String info = null;

	if (coche != null) {
	    info = "Coche encontrado: \n" + "Matricula: " + matricula + ", Marca: " + coche.getMarca() + ", Modelo: "
		    + coche.getModelo() + ",\nHora de entrada: " + coche.getHoraEntrada().format(FORMATTER);
	} else {
	    info = "Coche no encontrado";
	}

	return info;
    }

}
