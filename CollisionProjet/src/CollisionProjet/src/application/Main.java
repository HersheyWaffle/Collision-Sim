package application;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

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
	
	Vector3d rendering_centre = new Vector3d(0, 0, 0);// Origine de l'affichage
	int FONT_SIZE = 10; // taile des caract�re

	@Override
	public void start(Stage primaryStage) {
		try {

			Pane root = new Pane();
			root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
			Scene scene = new Scene(root, 400, 400);
			
			primaryStage.setScene(scene);
			primaryStage.show();
			
			rendering_centre = new Vector3d(scene.getWidth() / 2, scene.getHeight() / 2, 0);
			Sphere s = new Sphere(50, 25, 50, FONT_SIZE);

			

			update(s, root);

			
			//r�ajuse l'origine 
			scene.widthProperty().addListener((obs, oldVal, newVal) -> {
				rendering_centre = new Vector3d(scene.getWidth() / 2, scene.getHeight() / 2, 0);
				update(s, root);
			});

			scene.heightProperty().addListener((obs, oldVal, newVal) -> {
				rendering_centre = new Vector3d(scene.getWidth() / 2, scene.getHeight() / 2, 0);
				update(s, root);
			});

			root.setOnKeyPressed(new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent arg0) {
					double posNeg = arg0.isShiftDown() ? -2 * Math.PI / 50 : 2 * Math.PI / 50;
					

					if (arg0.getCode() == KeyCode.X) {
						s.rotate(posNeg, 0, 0);
						System.out.println("nbr rendered point: " + s.renderedSolide.size());
						update(s, root);
					} else if (arg0.getCode() == KeyCode.Y) {
						s.rotate(0, posNeg, 0);
						System.out.println("nbr rendered point: " + s.renderedSolide.size());
						update(s, root);
					} else if (arg0.getCode() == KeyCode.Z) {
						s.rotate(0, 0, posNeg);
						System.out.println("nbr rendered point: " + s.renderedSolide.size());
						update(s, root);
					}
					
					else if(arg0.getCode() == KeyCode.UP) {
						s.d�placement(new Vector3d(0,-5,0));
						update(s, root);
						
					}else if(arg0.getCode() == KeyCode.DOWN) {
						s.d�placement(new Vector3d(0,5,0));
						update(s, root);
					}else if(arg0.getCode() == KeyCode.LEFT) {
						s.d�placement(new Vector3d(-5,0,0));
						update(s, root);
						
					}else if(arg0.getCode() == KeyCode.RIGHT) {
						s.d�placement(new Vector3d(5,0,0));
						update(s, root);
					}else if(arg0.getCode() == KeyCode.W) {
						s.d�placement(new Vector3d(0,0,5));
						update(s, root);
					}else if(arg0.getCode() == KeyCode.S) {
						s.d�placement(new Vector3d(0,0,-5));
						update(s, root);
					}
				}

			});

			root.requestFocus();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void update(Sphere s, Pane root) {
		s.render(FONT_SIZE);

		root.getChildren().clear();
		for (Point p : s.renderedSolide) {

			Text t = new Text(p.get�clairage());
			t.setFill(Color.WHITE);

			t.setFont(Font.font(STYLESHEET_CASPIAN, FontWeight.BOLD, FONT_SIZE));
			t.setLayoutX(p.getCoordonn�e().x + rendering_centre.x);
			t.setLayoutY(p.getCoordonn�e().y + rendering_centre.y );

			root.getChildren().add(t);
		}

	}

	public static void main(String[] args) {
		launch(args);
	}
}
