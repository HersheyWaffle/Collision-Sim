package application;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import javax.vecmath.Vector3d;
import javax.vecmath.Matrix3d;


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

			Sphere s = new Sphere(100, 100, 100);
			s.cercle();
			s.sphere();
			
			Cube cube = new Cube(500, 1000, 1000);
			cube.carre();
			cube.cube();
			cube.enleveDoublons(cube.cube);
			//cube.rotateCube(45,45,45);
			
			
			
			//N'enleve pas de points donc je ne l'utilise pas
//			s.enleveDoublons(s.sphere);
			
			
			ArrayList<ArrayList<Vector3d>> coordonnée = s.sphere;

			ArrayList<ArrayList<Vector3d>> coordonnée2 = cube.cube;
			
			for (ArrayList<Vector3d> av : coordonnée2) {
//			for (ArrayList<Vector3d> av : coordonnée) {
				for (Vector3d v : av) {
					Circle cercle = new Circle(1);
					cercle.setLayoutX(v.x + 200);
					cercle.setLayoutY(v.y + 200);
					if(v.z<0) {
						cercle.setFill(Color.RED);
						cercle.setRadius(3);
					}
					
					root.getChildren().add(cercle);

				}
			}
			
			root.setOnKeyPressed(new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent arg0) {
					int posNeg = arg0.isShiftDown() ? -15 : 15;
					
					if(arg0.getCode() == KeyCode.X) {
						cube.rotateCube(posNeg,0,0);
						System.out.println("yo");
						root.getChildren().removeAll(root.getChildren());
						for (ArrayList<Vector3d> av : coordonnée2) {
//						for (ArrayList<Vector3d> av : coordonnée) {
							for (Vector3d v : av) {
								Circle cercle = new Circle(1);
								cercle.setLayoutX(v.x + 200);
								cercle.setLayoutY(v.y + 200);
								if(v.z<0) {
									cercle.setFill(Color.RED);
									cercle.setRadius(3);
								}
									
								root.getChildren().add(cercle);

							}
						}
					}
					else if(arg0.getCode() == KeyCode.Y) {
						cube.rotateCube(0,posNeg,0);
						System.out.println("yo");
						root.getChildren().removeAll(root.getChildren());
						for (ArrayList<Vector3d> av : coordonnée2) {
//						for (ArrayList<Vector3d> av : coordonnée) {
							for (Vector3d v : av) {
								Circle cercle = new Circle(1);
								cercle.setLayoutX(v.x + 200);
								cercle.setLayoutY(v.y + 200);
								if(v.z<0) {
									cercle.setFill(Color.RED);
									cercle.setRadius(3);
								}
									
								root.getChildren().add(cercle);

							}
						}
					}
					else if(arg0.getCode() == KeyCode.Z) {
						cube.rotateCube(0,0,posNeg);
						System.out.println("yo");
						root.getChildren().removeAll(root.getChildren());
						for (ArrayList<Vector3d> av : coordonnée2) {
//						for (ArrayList<Vector3d> av : coordonnée) {
							for (Vector3d v : av) {
								Circle cercle = new Circle(1);
								cercle.setLayoutX(v.x + 200);
								cercle.setLayoutY(v.y + 200);
								if(v.z<0) {
									cercle.setFill(Color.RED);
									cercle.setRadius(3);
								}
									
								root.getChildren().add(cercle);

							}
						}
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
