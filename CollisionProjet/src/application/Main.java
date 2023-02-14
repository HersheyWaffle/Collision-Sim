package application;

import java.util.ArrayList;

import javax.vecmath.Vector3d;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;


/**
 * La clase main qui initialise le simulateur.
 * 
 * @version 1.0 2023-02-07
 * @author Omar Ghazaly, Abel-Jimmy Oyono-Montoki
 */
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Pane root = new Pane();
			Scene scene = new Scene(root, 400, 400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();

			Sphere s = new Sphere(50, 120);
			Cube cube1 = new Cube(100, 100, 100);
			Cylindre cylindre = new Cylindre(100, 200);
			Cone cone = new Cone(200, 100);
			
			ArrayList<Vector3d> forme = s.getSphere();

			Solide.creeForme(forme, root);
//			Solide.creeForme(cube1.getCube(), root);
//			Solide.creeForme(s.getSphere(), root);
			
			root.setOnKeyPressed(new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent arg0) {
					double posNeg = arg0.isShiftDown() ? -15 : 15;
					
					if(arg0.getCode() == KeyCode.X) {
						Solide.rotateSolide(forme,posNeg,0,0);
						System.out.println("x");
						root.getChildren().removeAll(root.getChildren());
						Solide.creeForme(forme, root);
					}
					else if(arg0.getCode() == KeyCode.Y) {
						Solide.rotateSolide(forme,0,posNeg,0);
						System.out.println("y");
						root.getChildren().removeAll(root.getChildren());
						Solide.creeForme(forme, root);
					}
					else if(arg0.getCode() == KeyCode.Z) {
						Solide.rotateSolide(forme,0,0,posNeg);
						System.out.println("z");
						root.getChildren().removeAll(root.getChildren());
						Solide.creeForme(forme, root);
					}
				}
				
			});
			
			root.requestFocus();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		launch(args);
	}
}
