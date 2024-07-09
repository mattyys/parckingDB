/*
 * 
 */
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
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import practicaDB.dto.CocheDTO;
import practicaDB.model.Coche;


/**
 * The Class CheckOutController.
 */
public class CheckOutController implements Initializable {

    /** The btn buscar. */
    @FXML
    private JFXButton btn_buscar;

    /** The btn checkout. */
    @FXML
    private JFXButton btn_checkout;

    /** The text info. */
    @FXML
    private Text text_info;

    /** The tx matricula. */
    @FXML
    private TextField tx_matricula;

    /** The manager DB controller. */
    private ManagerDBController managerDBController;

    /** The main view controller. */
    private MainViewController mainViewController;

    /** The matricula pasada. */
    private String matriculaPasada = null;

    /**
     * Sets the matricula.
     *
     * @param matricula the new matricula
     */
    public void setMatricula(String matricula) {
	this.matriculaPasada = matricula;
	updateTextMatricula();
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
     * Sets the manager DB controller.
     *
     * @param managerDBController the new manager DB controller
     */
    public void setManagerDBController(ManagerDBController managerDBController) {
	this.managerDBController = managerDBController;
    }

    /**
     * Initialize.
     *
     * @param location  the location
     * @param resources the resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
	// TODO Auto-generated method stub
	text_info.setText("");
	btn_buscar.setText("");

    }

    /**
     * On buscar car.
     * <p>
     * Se llama ejecuta al presionar el boton buscar
     * </p>
     * <p>
     * Se validan los datos del campo matricula, se busca en la base de datos y se
     * muestra un texto informatico si se encontro o no el coche
     * </p>
     *
     * @param event the event
     */
    @FXML
    void onBuscarCar(ActionEvent event) {
	if (validarDatos()) {
	    String matricula = tx_matricula.getText();

	    Coche coche = managerDBController.search(matricula);

	    text_info.setText(setTextInfo(coche, matricula));
	} else {
	    createAlert(AlertType.ERROR, "Error", "Faltan datos", "Ingrese una Matricula.").showAndWait();
	}
    }

    /**
     * On check out.
     * <p>Saca el coche del parking, verificando si esta o no en el parking y si ya salio o no del parking</p>
     *
     * @param event the event
     */
    @FXML
    void onCheckOut(ActionEvent event) {
	// se guarda la fecha y hora de salida del coche
	LocalDateTime horaSalida = LocalDateTime.now();

	// se validan los datos del campo matricula
	if (validarDatos()) {
	    String matricula = tx_matricula.getText();
	    Coche coche = managerDBController.search(matricula);
	    // si no se encuentra el coche se muestra un mensaje de advertencia
	    if (coche == null) {
		createAlert(AlertType.WARNING, "Advertencia", "El coche no esta en el sistema",
			"El Parking no tiene el coche con matricula: " + matricula).showAndWait();
	    } else {
		// si el coche ya salio del parking se muestra un mensaje de advertencia
		if (coche.getHoraSalida() != null) {
		    createAlert(AlertType.WARNING, "Advertencia", "El coche ya salio del parking",
			    "El Parking ya no tiene el coche con matricula: " + matricula).showAndWait();
		} else {
		    // si el coche esta en el parking se actualiza la hora de salida y se muestra un
		    // mensaje de informacion
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
			mainViewController.updateTableParking();
		    } else {
			text_info.setText("Error al sacar el coche del parking");
		    }
		}
	    }

	} else {
	    aviso(false, null);
	}

    }

    /**
     * Find car key.
     * <p>Busca el coche al presionar la tecla enter en el campo matricula</p>
     *
     * @param event the event
     */
    @FXML
    void findCarKey(KeyEvent event) {
	String matricula = tx_matricula.getText();
	Coche coche = null;
	if ((coche = managerDBController.search(matricula)) != null) {
	    text_info.setText(setTextInfo(coche, matricula));
	} else {
	    text_info.setText("");
	}

    }

    /**
     * Validar datos.
     * 
     * <p>Valida los datos del campo matricula</p>
     *
     * @return true, if successful
     */
    private boolean validarDatos() {
	if (tx_matricula.getText().isEmpty()) {
	    return false;
	}
	return true;
    }

    /**
     * Aviso.
     * 
     * <p>
     * Se muestra un mensaje de informacion o error segun el booleano pasado y el
     * coche
     * </p>
     *
     * @param b     the b
     * @param coche the coche
     */
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

    /**
     * Creates the alert.
     * 
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
    private Alert createAlert(AlertType type, String title, String header, String content) {
	Alert alert = new Alert(type);
	alert.setTitle(title);
	alert.setHeaderText(header);
	alert.setContentText(content);
	return alert;
    }

    /**
     * Sets the text info.
     * <p>
     * Se crea un mensaje informativo con los datos del coche encontrado o no
     * encontrado
     * </p>
     *
     * @param coche     the coche
     * @param matricula the matricula
     * @return the string
     */
    private String setTextInfo(Coche coche, String matricula) {
	String info = null;

	if (coche != null) {
	    info = "Coche encontrado: \n" + "Matricula: " + matricula + ", Marca: " + coche.getMarca() + ", Modelo: "
		    + coche.getModelo() + ",\nHora de entrada: "
		    + coche.getHoraEntrada().format(managerDBController.DATE_FORMAT_INFO);
	} else {
	    info = "Coche no encontrado";
	}

	return info;
    }

    /**
     * Update text matricula.
     * <p>
     * Actualiza la matricula pasada por parametro de la vista principal al
     * tx_matricula
     * </p>
     */
    private void updateTextMatricula() {
	tx_matricula.setText(matriculaPasada);
    }

}
