package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
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
	int FONT_SIZE = 10; // taile des caractère
	double dt = 1;
	int i = 0;
	boolean isPlaying = true;
	Matrix3d util = new Matrix3d();
	
	double ajust = 40 * (Solide.Z_CONST+Solide.distanceObservateur) / (Solide.Z_CONST );
	Vector3d v1 = new Vector3d(ajust, 0, 0);
	Vector3d v2 = new Vector3d(0, ajust, 0);

	@Override
	public void start(Stage primaryStage) {
		try {

			Pane root = new Pane();
			root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
			Scene scene = new Scene(root, 400, 400);

			primaryStage.setScene(scene);
			primaryStage.show();

			rendering_centre = new Vector3d(scene.getWidth() / 2, scene.getHeight() / 2, 0);

			// declare les objets
			Sphere s1 = new Sphere(50, 100, 50, FONT_SIZE, new Vector3d(10, 0, 0), new Vector3d(-300, 0, 0), 10);
			Sphere s2 = new Sphere(50, 50, 50, FONT_SIZE, new Vector3d(-10, 0, 0), new Vector3d(300, 0, 0), 10);
			ArrayList<Solide> open = new ArrayList<Solide>();
			open.add(s1);
			open.add(s2);

			// System.out.println(s1.momentInertie);
			// System.out.println(s2.momentInertie);

			update(root, open);

			// joue l'animation
			Timeline loop = new Timeline(new KeyFrame(Duration.millis(75), new EventHandler<ActionEvent>() {
				public void handle(ActionEvent arg) {
					if (open.size() != 0) {
						for (Solide s : open) {
							s.deplacement2(dt);

						}

						update(root, open);
					}

				}
			}));
			loop.setCycleCount(Timeline.INDEFINITE);
			loop.play();

			// reajuse l'origine
			scene.widthProperty().addListener((obs, oldVal, newVal) -> {
				rendering_centre = new Vector3d(scene.getWidth() / 2, scene.getHeight() / 2, 0);
				update(root, open);
			});

			scene.heightProperty().addListener((obs, oldVal, newVal) -> {
				rendering_centre = new Vector3d(scene.getWidth() / 2, scene.getHeight() / 2, 0);
				update(root, open);
			});

			root.setOnKeyPressed(new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent arg0) {
					double posNeg = arg0.isShiftDown() ? -2 * Math.PI / 50 : 2 * Math.PI / 50;
					
					// changement de selecteur
					if (arg0.getCode() == KeyCode.K) {
						i = (i + 1) % open.size();
					} // rotation de l'ensemble
					else if (arg0.getCode() == KeyCode.DIGIT1) {
						for (Solide s : open) {
							s.rotateOrigine(posNeg, 0, 0);
						}
						util.rotX(posNeg);
						util.transform(v1);
						util.transform(v2);

					} else if (arg0.getCode() == KeyCode.DIGIT2) {
						for (Solide s : open) {
							s.rotateOrigine(0, posNeg, 0);
						}
						util.rotY(posNeg);
						util.transform(v1);
						util.transform(v2);

					} else if (arg0.getCode() == KeyCode.DIGIT3) {
						for (Solide s : open) {
							s.rotateOrigine(0, 0, posNeg);
						}
						util.rotZ(posNeg);
						util.transform(v1);
						util.transform(v2);

					} // deplacement de l'ensemble
					else if (arg0.getCode() == KeyCode.DIGIT4) {
						for (Solide s : open) {
							s.deplacement(v1);
							
						}

					} else if (arg0.getCode() == KeyCode.DIGIT5) {
						for (Solide s : open) {
							v1.scale(-1);
							s.deplacement(v1);
							v1.scale(-1);
						}

					} else if (arg0.getCode() == KeyCode.DIGIT6) {
						for (Solide s : open) {
							s.deplacement(v2);
						}

					} else if (arg0.getCode() == KeyCode.DIGIT7) {
						for (Solide s : open) {
							v2.scale(-1);
							s.deplacement(v2);
							v2.scale(-1);
						}

					}

					// rotation objet
					else if (arg0.getCode() == KeyCode.X) {
						open.get(i).rotate(posNeg, 0, 0);

					} else if (arg0.getCode() == KeyCode.Y) {
						open.get(i).rotate(0, posNeg, 0);

					} else if (arg0.getCode() == KeyCode.Z) {
						open.get(i).rotate(0, 0, posNeg);

					}

					// deplacement d'objet
					else if (arg0.getCode() == KeyCode.UP) {

						open.get(i).deplacement(new Vector3d(0, -5, 0));

					} else if (arg0.getCode() == KeyCode.DOWN) {

						open.get(i).deplacement(new Vector3d(0, 5, 0));

					} else if (arg0.getCode() == KeyCode.LEFT) {

						open.get(i).deplacement(new Vector3d(-5, 0, 0));

					} else if (arg0.getCode() == KeyCode.RIGHT) {

						open.get(i).deplacement(new Vector3d(5, 0, 0));

					} else if (arg0.getCode() == KeyCode.W) {

						open.get(i).deplacement(new Vector3d(0, 0, 5));

					} else if (arg0.getCode() == KeyCode.S) {

						open.get(i).deplacement(new Vector3d(0, 0, -5));

					} // pause
					else if (arg0.getCode() == KeyCode.P) {

						if (isPlaying) {
							loop.pause();
							isPlaying = false;
						} else {
							loop.play();
							isPlaying = true;
						}

					}
					update(root, open);

				}

			});

			// eloigne les objets de nous
			root.setOnScroll((ScrollEvent event) -> {

				double z = event.getDeltaY();

				Solide.distanceObservateur += z;
				update(root, open);
			});

			root.requestFocus();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void update(Pane root, Solide... solides) {
		root.getChildren().clear();
		for (Solide s : solides) {
			s.isColliding = false;
			s.setWhite();
			Lumiere.lumiere_Objet(s.solide);
		}

		for (int i = 0; i < solides.length - 1; i++) {
			for (int j = i + 1; j < solides.length; j++) {
				if (solides[i].detecteurDeProximite(solides[j], dt)) {
					solides[i].detecteurDeCollision(solides[j], dt);

				}
			}
		}

		for (Solide s : solides) {
			update(s, root);
		}

	}

	public void update(Pane root, ArrayList<Solide> solides) {
		root.getChildren().clear();
		for (Solide s : solides) {
			s.isColliding = false;
			s.setWhite();
			Lumiere.lumiere_Objet(s.solide);

			// enlève les objets trop loin
			/*
			 * if (s.virtualCentre.length() > 300) { solides.remove(s); }
			 */
		}

		for (int i = 0; i < solides.size() - 1; i++) {
			for (int j = i + 1; j < solides.size(); j++) {
				if (solides.get(i).detecteurDeProximite(solides.get(j), dt)) {
					solides.get(i).detecteurDeCollision(solides.get(j), dt);

				}
			}
		}

		for (Solide s : solides) {
			update(s, root);
		}

	}

	public void update(Solide s, Pane root) {
		s.render(FONT_SIZE);

		// Color c = s.isColliding?Color.RED:Color.WHITE;
		for (Point p : s.renderedSolide) {

			Text t = new Text(p.getEclairage());
			t.setFill(p.getColor());

			t.setFont(Font.font(STYLESHEET_CASPIAN, FontWeight.BOLD, s.FONT_SIZE));
			t.setLayoutX(p.getCoordonnee().x + rendering_centre.x);
			t.setLayoutY(p.getCoordonnee().y + rendering_centre.y);

			root.getChildren().add(t);
		}

	}

	public static void main(String[] args) {
		launch(args);
	}
}
