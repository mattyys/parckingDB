package practicaDB.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
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
import practicaDB.model.Coche;

public class MainViewController implements Initializable {

    @FXML
    private BorderPane root;

    @FXML
    private TableView<Coche> tableCars;

    @FXML
    private Label lb_titulo;

    @FXML
    private TableColumn<Coche, LocalDateTime> tb_col_ckin;

    @FXML
    private TableColumn<Coche, LocalDateTime> tb_col_ckout;

    @FXML
    private TableColumn<Coche, String> tb_col_marca;

    @FXML
    private TableColumn<Coche, String> tb_col_matricula;

    @FXML
    private TableColumn<Coche, String> tb_col_modelo;

    ObservableMap<String, Coche> listCars;

    private ManagerDBController managerDBController;

    public ManagerDBController getManagerDBController() {
	return managerDBController;
    }

    public void setManagerDBController(ManagerDBController managerDBController) {
	this.managerDBController = managerDBController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
	// TODO Auto-generated method stub
	managerDBController = new ManagerDBController();
	listCars = FXCollections.observableHashMap();
	lb_titulo.setText("");
    }

    @FXML
    void ListAllCarInSystem(ActionEvent event) {

	tableCars.getItems().clear();
	listCars.clear();
	managerDBController.searchAll().forEach((key, coche) -> listCars.put(key, coche));

	// INSERTAR datos en la tabla
	// tableCars.setItems(listCars);

	tb_col_matricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
	tb_col_marca.setCellValueFactory(new PropertyValueFactory<>("marca"));
	tb_col_modelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
	tb_col_ckin.setCellValueFactory(new PropertyValueFactory<>("horaEntrada"));
	/*
	 * tb_col_ckin.setCellValueFactory(cellData -> { LocalDateTime dateIn =
	 * cellData.getValue().getHoraEntrada(); String formattedDate =
	 * dateIn.format(formatter); return new
	 * SimpleStringProperty(LocalDateTime.parse(formattedDate)); });
	 */
	tb_col_ckout.setCellValueFactory(new PropertyValueFactory<>("horaSalida"));

	lb_titulo.setText("Lista de coches en el sistema");

	listCars.forEach((key, coche) -> tableCars.getItems().add(coche));

    }

    // agrege set y get de controller ver si sirve o no.

    @FXML
    void checkInCar(ActionEvent event) {

	FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddCarView.fxml"));

	try {
	    Parent gpRoot = loader.load();
	    AddCarViewController addCarViewController = loader.getController();
	    addCarViewController.setManagerDBController(this.managerDBController);
	    
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

    @FXML
    void checkOutCar(ActionEvent event) {
	FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CheckOutView.fxml"));
	
	try {
	    Parent gpRoot2 = loader.load();
	    CheckOutController checkOutController = loader.getController();
	    checkOutController.setManagerDBController(this.managerDBController);
	    
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
	    
	}catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    @FXML
    void listParkingInfo(ActionEvent event) {
	tableCars.getItems().clear();
	listCars.clear();
	managerDBController.searchAllFilterHoraSalida().forEach((key, coche) -> listCars.put(key, coche));

	tb_col_matricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
	tb_col_marca.setCellValueFactory(new PropertyValueFactory<>("marca"));
	tb_col_modelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
	tb_col_ckin.setCellValueFactory(new PropertyValueFactory<>("horaEntrada"));
	tb_col_ckout.setCellValueFactory(new PropertyValueFactory<>("horaSalida"));

	lb_titulo.setText("Coches en Parcking");

	listCars.forEach((key, coche) -> tableCars.getItems().add(coche));

    }

}
