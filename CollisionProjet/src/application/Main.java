package application;

import java.net.URL;
import java.util.ArrayList;

import javax.vecmath.Vector3d;

import javafx.application.Application;
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
	static BorderPane root;
	
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
			
//			Sphere s = new Sphere(50, 120);
//			Cube cube1 = new Cube(100, 100, 100);
//			Cylindre cylindre = new Cylindre(100, 200);
			Cone cone = new Cone(200, 100);
			
			ArrayList<Vector3d> forme = cone.getCone();

			Solide.creeForme(forme, ((Pane) root.getChildren().get(0)), 350, 400);
//			Solide.creeForme(cube1.getCube(), root);
//			Solide.creeForme(s.getSphere(), root);
			
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
