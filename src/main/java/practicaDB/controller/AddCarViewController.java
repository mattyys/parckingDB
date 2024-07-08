package practicaDB.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import practicaDB.dto.CocheDTO;
import practicaDB.model.Coche;

public class AddCarViewController {

    @FXML
    private JFXButton btn_Agregar;

    @FXML
    private TextField tx_marca;

    @FXML
    private TextField tx_matricula;

    @FXML
    private TextField tx_modelo;

    private ManagerDBController managerDBController;

    public ManagerDBController getManagerDBController() {
	return managerDBController;
    }

    public void setManagerDBController(ManagerDBController managerDBController) {
	this.managerDBController = managerDBController;
    }

    @FXML
    void addCar(ActionEvent event) {
	// obtener los datos de los campos
	System.out.println(managerDBController.mensajeConeccion());

	if (validarDatos()) {
	    String matricula = tx_matricula.getText();
	    // validara si existe la matricula
	    CocheDTO cocheDTO = (CocheDTO) managerDBController.search(matricula);

	    // queda chequear la ora de salida para saber si esta en el parking
	    // sii esta en parqking no se puede volver a agregasr si esta en el sistema si.

	    if (cocheDTO != null) {

		if (cocheDTO.getHoraSalida() != null) {
		    Optional<ButtonType> opcion = createAlert(AlertType.CONFIRMATION, "Coche existente",
			    "El coche se encuentra en el sistema", "Agregar al parking?").showAndWait();

		    if (opcion.get() == ButtonType.OK) {
			// volver a agregar coche al parking
			LocalDateTime ckin = LocalDateTime.now();
			cocheDTO.setMatricula(matricula);
			cocheDTO.setHoraEntrada(ckin);
			if ((managerDBController.updateCarInParcking(cocheDTO) == 1)) {
			    aviso(true);
			}
		    } else {
			// limpiar campos
			tx_matricula.setText("");
			tx_marca.setText("");
			tx_modelo.setText("");
			tx_matricula.requestFocus();
		    }
		} else {
		    String msj = "Coche con matricula: " + matricula + " en el parking, Hora:"
			    + cocheDTO.getHoraEntrada().format(managerDBController.DATE_FORMAT_INFO);
		    
		    createAlert(AlertType.WARNING, "Coche existente", "El coche se encuentra en el PARKING", msj)
			    .showAndWait();
		    tx_matricula.setText("");
		    tx_marca.setText("");
		    tx_modelo.setText("");
		    tx_matricula.requestFocus();
		}

	    } else {

		String marca = tx_marca.getText();
		String modelo = tx_modelo.getText();
		LocalDateTime ckin = LocalDateTime.now();
		// crear un coche

		CocheDTO coche = new CocheDTO(matricula, marca, modelo, ckin, null);

		// agregarlo a la base de datos
		managerDBController.insert(matricula, coche);

		aviso(true);
		// cerrar la ventana
		// obtener el stage
		Stage stage = (Stage) btn_Agregar.getScene().getWindow();
		stage.close();
	    }
	} else {
	    aviso(false);
	}

    }

    @FXML
    void findCarKey(KeyEvent event) {
	// buscar coche en la base de datos
	String matricula = tx_matricula.getText();
	Coche coche = null;
	if ((coche = managerDBController.search(matricula)) != null) {
	    tx_marca.setText(coche.getMarca());
	    tx_modelo.setText(coche.getModelo());
	} else {
	    tx_marca.setText("");
	    tx_modelo.setText("");
	}

    }

    private boolean validarDatos() {
	if (tx_matricula.getText().isEmpty() || tx_marca.getText().isEmpty() || tx_modelo.getText().isEmpty()) {

	    if (tx_matricula.getText().isEmpty()) {
		tx_matricula.requestFocus();
	    } else if (tx_marca.getText().isEmpty()) {
		tx_marca.requestFocus();
	    } else {
		tx_modelo.requestFocus();
	    }
	    return false;
	}

	return true;
    }

    private void aviso(boolean correcto) {
	if (correcto) {
	    createAlert(Alert.AlertType.INFORMATION, "Coche agregado", "Coche agregado correctamente",
		    "El coche ha sido agregado correctamente al Parking").showAndWait();
	} else {
	    createAlert(AlertType.ERROR, "Faltan datos", "Error al agregar coche", "Todos los campos son obligatorios")
		    .showAndWait();
	}
    }

    private Alert createAlert(Alert.AlertType type, String title, String header, String content) {
	Alert alert = new Alert(type);
	alert.setTitle(title);
	alert.setHeaderText(header);
	alert.setContentText(content);
	return alert;
    }

}
