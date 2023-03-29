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
	int FONT_SIZE = 10; // taile des caractère

	@Override
	public void start(Stage primaryStage) {
		try {

			Pane root = new Pane();
			root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
			Scene scene = new Scene(root, 400, 400);
			
			primaryStage.setScene(scene);
			primaryStage.show();
			
			rendering_centre = new Vector3d(scene.getWidth() / 2, scene.getHeight() / 2, 0);
			Sphere s1 = new Sphere(25, 50, 50, FONT_SIZE);
			Sphere s2 = new Sphere(25, 50, 50, FONT_SIZE);
			
			
			
			
			update(root,s1,s2);
			
			
			
			//réajuse l'origine 
			scene.widthProperty().addListener((obs, oldVal, newVal) -> {
				rendering_centre = new Vector3d(scene.getWidth() / 2, scene.getHeight() / 2, 0);
				update(root,s1,s2);
			});

			scene.heightProperty().addListener((obs, oldVal, newVal) -> {
				rendering_centre = new Vector3d(scene.getWidth() / 2, scene.getHeight() / 2, 0);
				update(root,s1,s2);
			});

			root.setOnKeyPressed(new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent arg0) {
					double posNeg = arg0.isShiftDown() ? -2 * Math.PI / 50 : 2 * Math.PI / 50;
					

					if (arg0.getCode() == KeyCode.X) {
						s1.rotate(posNeg, 0, 0);
						//System.out.println("nbr rendered point: " + s1.renderedSolide.size());
						
						update(root,s1,s2);
					} else if (arg0.getCode() == KeyCode.Y) {
						s1.rotate(0, posNeg, 0);
						//System.out.println("nbr rendered point: " + s1.renderedSolide.size());
						
						update(root,s1,s2);
					} else if (arg0.getCode() == KeyCode.Z) {
						s1.rotate(0, 0, posNeg);
						//System.out.println("nbr rendered point: " + s1.renderedSolide.size());
						update(root,s1,s2);
					}
					
					else if(arg0.getCode() == KeyCode.UP) {
						
						s1.déplacement(new Vector3d(0,-5,0));
						
						update(root,s1,s2);
						
					}else if(arg0.getCode() == KeyCode.DOWN) {
						
						s1.déplacement(new Vector3d(0,5,0));
						
						update(root,s1,s2);
					}else if(arg0.getCode() == KeyCode.LEFT) {
						
						s1.déplacement(new Vector3d(-5,0,0));
						
						update(root,s1,s2);
						
					}else if(arg0.getCode() == KeyCode.RIGHT) {
						
						s1.déplacement(new Vector3d(5,0,0));
						
						update(root,s1,s2);
					}else if(arg0.getCode() == KeyCode.W) {
						
						s1.déplacement(new Vector3d(0,0,5));
						
						update(root,s1,s2);
					}else if(arg0.getCode() == KeyCode.S) {
						
						s1.déplacement(new Vector3d(0,0,-5));
						
						update(root,s1,s2);
					}
				}

			});

			root.requestFocus();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	
	public void update(Pane root,Solide... solides) {
		root.getChildren().clear();
		for(Solide s : solides) {
			s.isColliding = false;
		}
		
		
		
		for(int i=0; i<solides.length-1;i++) {
			for(int j=i+1; j<solides.length;j++) {
				if(solides[i].DetecteurDeCollision(solides[j].virtual_centre, solides[j].rayonDeCollision) ) {
					solides[i].isColliding = true;
					solides[j].isColliding = true;
					
				}else {
					
				}
			}
		}
		
		for(Solide s : solides) {
			update(s, root);
		}
		
	}

	public void update(Solide s, Pane root) {
		s.render(FONT_SIZE);
		
		Color c = s.isColliding?Color.RED:Color.WHITE;
		for (Point p : s.renderedSolide) {

			Text t = new Text(p.getÉclairage());
			t.setFill(c);
			

			t.setFont(Font.font(STYLESHEET_CASPIAN, FontWeight.BOLD, s.FONT_SIZE));
			t.setLayoutX(p.getCoordonnée().x + rendering_centre.x);
			t.setLayoutY(p.getCoordonnée().y + rendering_centre.y );

			root.getChildren().add(t);
		}

	}

	public static void main(String[] args) {
		launch(args);
	}
}
