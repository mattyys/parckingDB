package practicaDB;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// TODO: Auto-generated Javadoc
/**
 * The Class AppFx.
 */
public class AppFx extends Application {

    /**
     * Start.
     *
     * @param primaryStage the primary stage
     */
    @Override
    public void start(Stage primaryStage) {
	URL url = getClass().getResource("/fxml/MainView.fxml");
	System.out.println(url);
	Parent root = null;

	try {
	    root = FXMLLoader.load(url);
	} catch (IOException e) {
	    e.printStackTrace();
	}

	if (root != null) {
	    try {
		Scene scene = new Scene(root);
		scene.getStylesheets().add("css/main.css");
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Control Parking");
		primaryStage.show();
	    } catch (Exception e) {
		System.err.println("Exception occurred in start method:");
		e.printStackTrace();
		if (e.getCause() != null) {
		    System.err.println("Caused by:");
		    e.getCause().printStackTrace();
		}
	    }
	} else {
	    System.out.println("Error al cargar el archivo fxml: root is null");
	}

    }

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
	launch(args);
    }
}
