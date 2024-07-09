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


/**
 * The Class AddCarViewController.
 */
public class AddCarViewController {

    /** The btn agregar. */
    @FXML
    private JFXButton btn_Agregar;

    /** The tx marca. */
    @FXML
    private TextField tx_marca;

    /** The tx matricula. */
    @FXML
    private TextField tx_matricula;

    /** The tx modelo. */
    @FXML
    private TextField tx_modelo;

    /** The manager DB controller. */
    private ManagerDBController managerDBController;

    /** The coche DTO. */
    private CocheDTO cocheDTO = null;

    /** The main view controller. */
    private MainViewController mainViewController;

    /**
     * Gets the manager DB controller.
     *
     * @return the manager DB controller
     */
    public ManagerDBController getManagerDBController() {
	return managerDBController;
    }

    /**
     * Sets the main view controller.
     *
     * @param mainViewController the new main view controller
     */
    public void setMainViewController(MainViewController mainViewController) {
	this.mainViewController = mainViewController;
    }

    /**
     * Sets the coche dto.
     *
     * @param cocheDTO the new coche dto
     */
    public void setCocheDto(CocheDTO cocheDTO) {
	this.cocheDTO = cocheDTO;
	updateCoche();
    }

    /**
     * Sets the manager DB controller.
     *
     * @param managerDBController the new manager DB controller
     */
    public void setManagerDBController(ManagerDBController managerDBController) {
	this.managerDBController = managerDBController;
    }

    /**
     * Adds the car.
     * <p>
     * Sera llamado cuando se presione el boton de Agregar
     * </p>
     * <p>
     * Se validara que los campos esten completos, si la matricula ya existe se
     * preguntara si se quiere agregar nuevamente al parking, si ya esta en el
     * parking se da aviso y se limpiaran los campos para ingresar una nueva
     * matricula, si no existe se creara un nuevo coche y se agregara a la base de
     * datos
     * </p>
     *
     * @param event the event
     */
    @FXML
    void addCar(ActionEvent event) {

	// se validan que esten todos los campos completos
	if (validarDatos()) {
	    String matricula = tx_matricula.getText();
	    // validara si existe la matricula
	    CocheDTO cocheDTO = (CocheDTO) managerDBController.search(matricula);

	    // verifica si el coche existe
	    if (cocheDTO != null) {

		// verifica si el coche ya esta en el sistema
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
			    volverAMain();
			}
		    } else {
			// limpiar campos
			limpiarCampos();
		    }
		} else {
		    String msjCocheEnParking = "Coche con matricula: " + matricula + " en el parking, Hora:"
			    + cocheDTO.getHoraEntrada().format(managerDBController.DATE_FORMAT_INFO);

		    createAlert(AlertType.WARNING, "Coche existente", "El coche se encuentra en el PARKING",
			    msjCocheEnParking).showAndWait();
		    limpiarCampos();
		}

	    } else {

		String marca = tx_marca.getText();
		String modelo = tx_modelo.getText();
		LocalDateTime ckin = LocalDateTime.now();
		// crear un coche

		CocheDTO coche = new CocheDTO(matricula, marca, modelo, ckin, null);

		// agregarlo a la base de datos
		if (managerDBController.insert(matricula, coche) == 1) {

		    aviso(true);
		    volverAMain();
		}
	    }
	} else {
	    aviso(false);
	}

    }

    /**
     * Find car key.
     * 
     * <p>
     * Sera llamado cuando se presione una tecla en el campo de matricula
     * </p>
     * <p>
     * Con cada tecla presionada en el campo de matricula se busca la coincidencia
     * en la base de datos
     * </p>
     *
     * @param event the event
     */
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

    /**
     * Validar datos.
     * <p>
     * Metodo para validar los campos de la ventana
     * </p>
     *
     * @return true, if successful
     */
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

    /**
     * Aviso.
     * <p>
     * Metodo para mostrar un mensaje de alerta, recibe un booleano para saber si el
     * coche fue agregado correctamente o no
     * </p>
     *
     * @param correcto the correcto
     */
    private void aviso(boolean correcto) {
	if (correcto) {
	    createAlert(Alert.AlertType.INFORMATION, "Coche agregado", "Coche agregado correctamente",
		    "El coche ha sido agregado correctamente al Parking").showAndWait();
	} else {
	    createAlert(AlertType.ERROR, "Faltan datos", "Error al agregar coche", "Todos los campos son obligatorios")
		    .showAndWait();
	}
    }

    /**
     * Creates the alert.
     * <p>
     * Alerta personalizada, recibe el tipo de alerta, el titulo, el header y el
     * contenido
     * </p>
     *
     * @param type    the type
     * @param title   the title
     * @param header  the header
     * @param content the content
     * @return the alert
     */
    private Alert createAlert(Alert.AlertType type, String title, String header, String content) {
	Alert alert = new Alert(type);
	alert.setTitle(title);
	alert.setHeaderText(header);
	alert.setContentText(content);
	return alert;
    }

    /**
     * Limpiar campos.
     * <p>
     * Limpia los campos de la ventana y coloca el foco en el primer campo a
     * completar
     * </p>
     */
    private void limpiarCampos() {
	tx_matricula.setText("");
	tx_marca.setText("");
	tx_modelo.setText("");
	tx_matricula.requestFocus();
    }

    /**
     * Update coche.
     * <p>
     * Metodo para actualizar los campos de la ventana con los datos del coche que
     * fue seleccionado en el la vista principal
     * </p>
     */
    private void updateCoche() {
	tx_matricula.setText(cocheDTO.getMatricula());
	tx_marca.setText(cocheDTO.getMarca());
	tx_modelo.setText(cocheDTO.getModelo());
    }

    /**
     * Volver A main.
     * <p>
     * Metodo para cerrar la ventana y volver a la ventana principal actualizando la
     * lista del parking
     * </p>
     */
    private void volverAMain() {
	Stage stage = (Stage) btn_Agregar.getScene().getWindow();
	stage.close();
	mainViewController.updateTableParking();
    }

}
