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

	final static int FONT_SIZE = 10;

	@Override
	public void start(Stage primaryStage) {
		try {

			Pane root = new Pane();
			root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
			Scene scene = new Scene(root, 400, 400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();

			Sphere s = new Sphere(50, 25, 50, FONT_SIZE, new Vector3d(scene.getWidth() / 2, scene.getHeight() / 2, 0));

			

			update(s, root);

			

			scene.widthProperty().addListener((obs, oldVal, newVal) -> {
				s.rendering_centre_Update(new Vector3d(scene.getWidth() / 2, scene.getHeight() / 2, 0));
				update(s, root);
			});

			scene.heightProperty().addListener((obs, oldVal, newVal) -> {
				s.rendering_centre_Update(new Vector3d(scene.getWidth() / 2, scene.getHeight() / 2, 0));
				update(s, root);
			});

			root.setOnKeyPressed(new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent arg0) {
					double posNeg = arg0.isShiftDown() ? -2 * Math.PI / 50 : 2 * Math.PI / 50;
					

					if (arg0.getCode() == KeyCode.X) {
						s.rotate(posNeg, 0, 0);
						System.out.println("nbr rendered point: " + s.renderedSphere.size());
						update(s, root);
					} else if (arg0.getCode() == KeyCode.Y) {
						s.rotate(0, posNeg, 0);
						System.out.println("nbr rendered point: " + s.renderedSphere.size());
						update(s, root);
					} else if (arg0.getCode() == KeyCode.Z) {
						s.rotate(0, 0, posNeg);
						System.out.println("nbr rendered point: " + s.renderedSphere.size());
						update(s, root);
					}
					
					else if(arg0.getCode() == KeyCode.UP) {
						s.déplacement(new Vector3d(0,-5,0));
						update(s, root);
						
					}else if(arg0.getCode() == KeyCode.DOWN) {
						s.déplacement(new Vector3d(0,5,0));
						update(s, root);
					}else if(arg0.getCode() == KeyCode.LEFT) {
						s.déplacement(new Vector3d(-5,0,0));
						update(s, root);
						
					}else if(arg0.getCode() == KeyCode.RIGHT) {
						s.déplacement(new Vector3d(5,0,0));
						update(s, root);
					}else if(arg0.getCode() == KeyCode.W) {
						s.déplacement(new Vector3d(0,0,5));
						update(s, root);
					}else if(arg0.getCode() == KeyCode.S) {
						s.déplacement(new Vector3d(0,0,-5));
						update(s, root);
					}
				}

			});

			root.requestFocus();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void update(Sphere s, Pane root) {
		s.render(FONT_SIZE);

		root.getChildren().clear();
		for (Point p : s.renderedSphere) {

			Text t = new Text(p.getÉclairage());
			t.setFill(Color.WHITE);

			t.setFont(Font.font(STYLESHEET_CASPIAN, FontWeight.BOLD, FONT_SIZE));
			t.setLayoutX(p.getCoordonnée().x + s.rendering_centre.x);
			t.setLayoutY(p.getCoordonnée().y + s.rendering_centre.y );

			root.getChildren().add(t);
		}

	}

	public static void main(String[] args) {
		launch(args);
	}
}
