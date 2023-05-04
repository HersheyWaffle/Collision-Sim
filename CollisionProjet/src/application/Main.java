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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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

	// origine
	Vector3d renderingCentre = new Vector3d(0, 0, 0);// Origine de l'affichage
	Vector3d renderingFocus = new Vector3d(0, 0, 0);// Origine changée
	Point origine = new Point();

	// variable utilitaire
	int FONT_SIZE = 10; // taile des caractère
	double dt = 0.05; // temps entre itération
	int i = 0; // objet sélecter
	boolean isPlaying = true; // si l'animation joue
	

	// variable pour déplacer l'ensemble
	Matrix3d util = new Matrix3d();
	double ajust = 40 * (Solide.Z_CONST + Solide.distanceObservateur) / (Solide.Z_CONST);
	Vector3d v1 = new Vector3d(ajust, 0, 0);
	Vector3d v2 = new Vector3d(0, ajust, 0);

	// position de l'origine
	Vector3d positionCentre = new Vector3d();

	//
	ArrayList<Point> PointOpen = new ArrayList<Point>();

	@Override
	public void start(Stage primaryStage) {
		try {

			// javafx
			Pane root = new Pane();
			root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
			Scene scene = new Scene(root, 400, 400);

			primaryStage.setScene(scene);
			primaryStage.show();

			// centre de focus
			renderingCentre = new Vector3d(scene.getWidth() / 2, scene.getHeight() / 2, 0);
			origine.setEclairage("@");
			origine.setColor(Color.RED);

			positionCentre.scale(-1, renderingFocus);
			origine.setCoordonnee(positionCentre);

			// declare les objets
			Tore t1 = new Tore(50, 100, 100, 100, FONT_SIZE, new Vector3d(100, 0, 0), new Vector3d(-300, 50, 0), 10);
			Tore t2 = new Tore(50, 100, 100, 100, FONT_SIZE, new Vector3d(0, 0, 0), new Vector3d(300, 0, 0), 10);
			ArrayList<Solide> open = new ArrayList<Solide>();
			open.add(t1);
			open.add(t2);

			// déclare les axes
			Axe x = new Axe(new Vector3d(1, 0, 0), FONT_SIZE);
			x.setColor(Color.BLUE);
			Axe y = new Axe(new Vector3d(0, 1, 0), FONT_SIZE);
			y.setColor(Color.GREEN);
			Axe z = new Axe(new Vector3d(0, 0, 1), FONT_SIZE);
			z.setColor(Color.RED);
			ArrayList<Solide> axis = new ArrayList<Solide>();
			axis.add(x);
			axis.add(y);
			axis.add(z);
			

			update(root, open);
			update2(root, axis,true);

			// joue l'animation
			Timeline loop = new Timeline(new KeyFrame(Duration.millis(75), new EventHandler<ActionEvent>() {
				public void handle(ActionEvent arg) {
					if (open.size() != 0) {
						for (Solide s : open) {
							s.deplacement2(dt);

						}

						update(root, open);
						update2(root, axis,true);
					}

				}
			}));
			loop.setCycleCount(Timeline.INDEFINITE);
			loop.play();

			// reajuse l'origine
			scene.widthProperty().addListener((obs, oldVal, newVal) -> {
				renderingCentre = new Vector3d(scene.getWidth() / 2, scene.getHeight() / 2, 0);

				update(root, open);
				update2(root, axis,true);
			});

			scene.heightProperty().addListener((obs, oldVal, newVal) -> {
				renderingCentre = new Vector3d(scene.getWidth() / 2, scene.getHeight() / 2, 0);

				update(root, open);
				update2(root, axis,true);
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
						for (Solide s : axis) {
							s.rotateOrigine(posNeg, 0, 0);
						}
						util.rotX(posNeg);
						util.transform(v1);
						util.transform(v2);

					} else if (arg0.getCode() == KeyCode.DIGIT2) {
						for (Solide s : open) {
							s.rotateOrigine(0, posNeg, 0);
						}
						for (Solide s : axis) {
							s.rotateOrigine(0, posNeg, 0);
						}
						util.rotY(posNeg);
						util.transform(v1);
						util.transform(v2);

					} else if (arg0.getCode() == KeyCode.DIGIT3) {
						for (Solide s : open) {
							s.rotateOrigine(0, 0, posNeg);
						}
						for (Solide s : axis) {
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
						for (Solide s : axis) {
							s.deplacement(v1);
						}

					} else if (arg0.getCode() == KeyCode.DIGIT5) {
						for (Solide s : open) {
							v1.scale(-1);
							s.deplacement(v1);
							v1.scale(-1);
						}
						for (Solide s : axis) {
							v1.scale(-1);
							s.deplacement(v1);
							v1.scale(-1);
						}

					} else if (arg0.getCode() == KeyCode.DIGIT6) {
						for (Solide s : open) {
							s.deplacement(v2);
						}
						for (Solide s : axis) {
							s.deplacement(v2);
						}
						

					} else if (arg0.getCode() == KeyCode.DIGIT7) {
						for (Solide s : open) {
							v2.scale(-1);
							s.deplacement(v2);
							v2.scale(-1);
						}
						for (Solide s : axis) {
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
					update2(root, axis,true);

				}

			});

			// eloigne les objets de nous
			root.setOnScroll((ScrollEvent event) -> {

				double depth = event.getDeltaY();

				Solide.distanceObservateur += depth;
				update(root, open);
				update2(root, axis,true);
			});

			root.setOnMouseClicked((MouseEvent event) -> {
				// restore le virtual centre original
				for (Solide s : open) {
					s.virtualCentre.add(renderingFocus);
				}
				for (Solide s : axis) {
					s.virtualCentre.add(renderingFocus);
				}

				// change le focus
				if (event.getButton() == MouseButton.PRIMARY) {
					Vector3d previousFocus = (Vector3d) renderingFocus.clone();
					renderingFocus.x = event.getSceneX();
					renderingFocus.y = event.getSceneY();
					//renderingFocus.z = Solide.distanceObservateur;
					renderingFocus.sub(renderingCentre);
					renderingFocus.add(previousFocus);

				} else if (event.getButton() == MouseButton.SECONDARY) {
					renderingFocus = new Vector3d();
				}

				// change le virtual centre en fonction du nouveau focus
				for (Solide s : open) {
					s.virtualCentre.sub(renderingFocus);
				}
				for (Solide s : axis) {
					s.virtualCentre.sub(renderingFocus);
				}
				// ajuste l'origine

				positionCentre.scale(-1, renderingFocus);
				origine.setCoordonnee(positionCentre);
				update(root, open);
				update2(root, axis,true);

			});

			root.requestFocus();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void update(Pane root, ArrayList<Solide> solides) {
		root.getChildren().clear();
		for (Solide s : solides) {
			s.isColliding = false;
			s.setWhite();
		}
		update2(root,solides,false);

	}
	
	public void update2(Pane root, ArrayList<Solide> solides,boolean b) {
		
		for (Solide s : solides) {
			Lumiere.lumiere_Objet(s.solide);
		}
		
		if(b) {
			for (Solide s : solides) {
				s.setCharacter(".");
			}
		}

		solides.remove(null);

		// collision
		for (int i = 0; i < solides.size() - 1; i++) {
			for (int j = i + 1; j < solides.size(); j++) {
				if (solides.get(i).detecteurDeProximite(solides.get(j), dt)) {
					solides.get(i).detecteurDeCollision(solides.get(j), dt);

				}
			}
		}

		

		// affichage
		for (Solide s : solides) {
			s.render(FONT_SIZE);
			update(s, root);
		}

	}

	public void update(Solide s, Pane root) {

		// Color c = s.isColliding?Color.RED:Color.WHITE;
		for (Point p : s.renderedSolide) {

			Text t = new Text(p.getEclairage());
			t.setFill(p.getColor());

			t.setFont(Font.font(STYLESHEET_CASPIAN, FontWeight.BOLD, s.FONT_SIZE));
			t.setLayoutX(p.getCoordonnee().x + renderingCentre.x);
			t.setLayoutY(p.getCoordonnee().y + renderingCentre.y);

			root.getChildren().add(t);
		}

	}

	public static void main(String[] args) {
		launch(args);
	}
}
