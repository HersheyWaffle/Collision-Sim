package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.vecmath.Vector3d;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;


/**
 * La clase main qui initialise le simulateur.
 * 
 * @version 1.0 2023-02-07
 * @author Omar Ghazaly, Abel-Jimmy Oyono-Montoki
 */
public class Main extends Application {
	public static BorderPane root;
	public static ObservableList<Solide> listeSolides = FXCollections.observableArrayList();
	public static ObservableList<String> listeNoms = FXCollections.observableArrayList();
	public static HashMap<String, Solide> mapSolideNom = new HashMap<String, Solide>();
	
	public static BorderPane getRoot() {
		return root;
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			final URL url = getClass().getResource("App.fxml");
			final FXMLLoader fxmlLoader = new FXMLLoader(url);
			
			root = fxmlLoader.load();
			Scene scene = new Scene(root);
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("Simulateur de collisions");
			primaryStage.setScene(scene);
			primaryStage.show();

			Controller controller = new Controller();
			
			root.setOnKeyPressed(controller.rotationKey());
			root.requestFocus();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		launch(args);
	}
}
