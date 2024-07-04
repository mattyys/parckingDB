package practicaDB.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
	    //sii esta en parqking no se puede volver a agregasr si esta en el sistema si.

	    if (cocheDTO != null) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Matricula existente");
		alert.setHeaderText("El coche se encuentra en el sistema");
		alert.setContentText("Desea ingresarlo al Parking?");
		Optional<ButtonType> opcion = alert.showAndWait();

		if (opcion.get() == ButtonType.OK) {
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
	if (managerDBController.search(matricula) != null) {
	    Coche coche = managerDBController.search(matricula);
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
	    Alert alert = new Alert(Alert.AlertType.INFORMATION);
	    alert.setTitle("Coche agregado");
	    alert.setHeaderText("Coche agregado correctamente");
	    alert.setContentText("El coche ha sido agregado correctamente al Parking");
	    alert.showAndWait();
	} else {
	    Alert alert = new Alert(Alert.AlertType.ERROR);
	    alert.setTitle("Faltan datos");
	    alert.setHeaderText("Error al agregar coche");
	    alert.setContentText("Todos los campos son obligatorios");
	    alert.showAndWait();
	}
    }

}
