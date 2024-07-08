/*
 * 
 */
package practicaDB.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import practicaDB.dto.CocheDTO;
import practicaDB.model.Coche;

/**
 * The Class MainViewController.
 */
public class MainViewController implements Initializable {

    /** The root. */
    @FXML
    private BorderPane root;

    /** The table cars. */
    @FXML
    private TableView<CocheDTO> tableCars;

    /** The lb titulo. */
    @FXML
    private Label lb_titulo;

    /** The tb col ckin. */
    @FXML
    private TableColumn<Coche, LocalDateTime> tb_col_ckin;

    /** The tb col ckout. */
    @FXML
    private TableColumn<Coche, LocalDateTime> tb_col_ckout;

    /** The tb col marca. */
    @FXML
    private TableColumn<Coche, String> tb_col_marca;

    /** The tb col matricula. */
    @FXML
    private TableColumn<Coche, String> tb_col_matricula;

    /** The tb col modelo. */
    @FXML
    private TableColumn<Coche, String> tb_col_modelo;

    /** The list cars. */
    ObservableMap<String, CocheDTO> listCars;

    /** The manager DB controller. */
    private ManagerDBController managerDBController;

    /**
     * Gets the manager DB controller.
     *
     * @return the manager DB controller
     */
    public ManagerDBController getManagerDBController() {
	return managerDBController;
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
	// se instancia el managerDBController y se inicializa el observableHashMap
	managerDBController = new ManagerDBController();
	listCars = FXCollections.observableHashMap();
	lb_titulo.setText("");
    }

    /**
     * List all car in system.
     * <p>
     * Lista todos los coches en el sistema
     * </p>
     *
     * @param event the event
     */
    @FXML
    void ListAllCarInSystem(ActionEvent event) {
	updateTableSystem();
    }

    /**
     * Check in car.
     * <p>
     * Abre la ventana para agregar un coche al Parking, pasando el
     * managerDBController y el mainViewCOntroller para tener la coneccion a base de
     * datos y pasar un coche seleccionado a la nueva ventana
     * </p>
     *
     * @param event the event
     */
    @FXML
    void checkInCar(ActionEvent event) {

	FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddCarView.fxml"));

	try {
	    Parent gpRoot = loader.load();
	    // se obtiene el controller de la nueva ventana y se le pasan los datos
	    AddCarViewController addCarViewController = loader.getController();
	    addCarViewController.setManagerDBController(this.managerDBController);
	    addCarViewController.setMainViewController(this);

	    // se chequea si hay un coche seleccionado
	    if (tableCars.getSelectionModel().getSelectedItem() != null) {
		addCarViewController.setCocheDto(tableCars.getSelectionModel().getSelectedItem());
	    }

	    // se crea la nueva ventana
	    Scene scene = new Scene(gpRoot);

	    Stage stage = new Stage();
	    stage.setTitle("Check In Car");
	    scene.getStylesheets().add("/css/addCarStyle.css");
	    stage.setScene(scene);
	    stage.initOwner(root.getScene().getWindow());
	    stage.initModality(Modality.APPLICATION_MODAL);
	    stage.setResizable(false);
	    stage.setIconified(false);
	    stage.showAndWait();

	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    /**
     * Check out car.
     * <p>
     * Abre la ventana para quitar un coche del Parking, pasando el
     * managerDBController y el mainViewCOntroller para tener la coneccion a base de
     * datos y pasar un coche seleccionado a la nueva ventana
     * </p>
     * 
     * @param event the event
     */
    @FXML
    void checkOutCar(ActionEvent event) {
	FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CheckOutView.fxml"));

	try {
	    Parent gpRoot2 = loader.load();
	    // se obtiene el controller de la nueva ventana y se le pasan los datos
	    CheckOutController checkOutController = loader.getController();
	    checkOutController.setManagerDBController(this.managerDBController);
	    checkOutController.setMainViewController(this);

	    // se chequea si hay un coche seleccionado
	    if (tableCars.getSelectionModel().getSelectedItem() != null) {
		checkOutController.setMatricula(tableCars.getSelectionModel().getSelectedItem().getMatricula());
	    }

	    // se crea la nueva ventana
	    Scene scene = new Scene(gpRoot2);

	    Stage stage = new Stage();
	    stage.setTitle("Check Out Car");
	    scene.getStylesheets().add("/css/checkOutStyle.css");
	    stage.setScene(scene);
	    stage.initOwner(root.getScene().getWindow());
	    stage.initModality(Modality.APPLICATION_MODAL);
	    stage.setResizable(false);
	    stage.setIconified(false);
	    stage.showAndWait();

	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    /**
     * List parking info.
     * <p>
     * Lista todos los coches en el parking
     * </p>
     * 
     * @param event the event
     */
    @FXML
    void listParkingInfo(ActionEvent event) {
	updateTableParking();
    }

    /**
     * Update table parking.
     * <p>
     * Actualiza la tabla de coches en el parking
     * </p>
     */
    public void updateTableParking() {
	cargarDatosEnTabla("Coches en el parking", managerDBController.searchAllFilterHoraSalida());
    }

    /**
     * Update table system.
     * <p>
     * Actualiza la tabla de todos los coches en el sistema
     * </p>
     */
    public void updateTableSystem() {
	cargarDatosEnTabla("Listado coches en el sistema", managerDBController.searchAll());
    }

    /**
     * Cargar datos en tabla.
     * <p>
     * Metodo que carga los datos en el tableview con el titulo y Map pasados.
     * </p>
     *
     * @param titulo   the titulo
     * @param cochesBD the coches BD
     */
    private void cargarDatosEnTabla(String titulo, Map<String, Coche> cochesBD) {
	// se limpian los items de la tabla y el observableHashMap
	tableCars.getItems().clear();
	listCars.clear();

	// se obtienen los coches en el sistema y se agregan al observableHashMap
	cochesBD.forEach((key, coche) -> {
	    CocheDTO cocheDTO = (CocheDTO) coche;
	    cocheDTO.setMatricula(key);
	    listCars.put(key, cocheDTO);
	});

	// se setean las columnas de la tabla
	tb_col_matricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
	tb_col_marca.setCellValueFactory(new PropertyValueFactory<>("marca"));
	tb_col_modelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
	tb_col_ckin.setCellValueFactory(new PropertyValueFactory<>("horaEntrada"));
	tb_col_ckout.setCellValueFactory(new PropertyValueFactory<>("horaSalida"));

	// se setea el titulo de la tabla
	lb_titulo.setText(titulo);

	// se agregan los coches a la tabla
	listCars.forEach((key, coche) -> tableCars.getItems().add(coche));
    }

}
