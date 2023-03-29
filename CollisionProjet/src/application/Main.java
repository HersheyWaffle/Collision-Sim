package application;

import java.net.URL;
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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * La clase main qui initialise le simulateur.
 * 
 * @version 1.0 2023-02-07
 * @author Omar Ghazaly, Abel-Jimmy Oyono-Montoki
 */
public class Main extends Application {
	public static final boolean DEBUG_MODE = true;

	public static BorderPane root;
	public static ObservableList<Solide> listeSolides = FXCollections.observableArrayList();
	public static ObservableList<String> listeNoms = FXCollections.observableArrayList();
	public static HashMap<String, Solide> mapSolideNom = new HashMap<String, Solide>();

	public static Vector3d rendering_centre = new Vector3d(0, 0, 0);// Origine de l'affichage
	static int FONT_SIZE = 10; // taile des caractères

	@Override
	public void start(Stage primaryStage) {
		try {
			final URL url = getClass().getResource("App.fxml");
			final FXMLLoader fxmlLoader = new FXMLLoader(url);

			root = fxmlLoader.load();
			Scene scene = new Scene(root);

			rendering_centre = new Vector3d(scene.getWidth() / 2, scene.getHeight() / 2, 0);
			
			//GENERALISE INTO CONTROLLER
//			Sphere s = new Sphere(50, 25, FONT_SIZE);
//			update(s, root);
			
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

	public static void update(Solide s, Pane pane) {
		s.render(FONT_SIZE);

		pane.getChildren().clear();
		for (Point p : s.renderedSolide) {

			Text t = new Text(p.getEclairage());
			t.setFill(Color.WHITE);

			t.setFont(Font.font(STYLESHEET_CASPIAN, FontWeight.BOLD, FONT_SIZE));
			t.setLayoutX(p.getCoordonnee().x + rendering_centre.x);
			t.setLayoutY(p.getCoordonnee().y + rendering_centre.y );

			pane.getChildren().add(t);
		}

	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
