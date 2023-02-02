package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import javax.vecmath.Vector3d;
import javax.vecmath.Matrix3d;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Pane root = new Pane();
			Scene scene = new Scene(root, 400, 400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();

			Sphere s = new Sphere();
			s.cercle();
			s.sphere();
			ArrayList<ArrayList<Vector3d>> coordonnée = s.sphere;

			

			for (ArrayList<Vector3d> av : coordonnée) {
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
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		launch(args);
	}
}
