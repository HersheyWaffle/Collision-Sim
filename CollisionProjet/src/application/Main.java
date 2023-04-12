package application;

import java.net.URL;
import java.util.ArrayList;
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
	/**
	 * La ObservableList des solides dans la sc�ne
	 */
	public static ObservableList<Solide> listeSolides = FXCollections.observableArrayList();
	/**
	 * La ObservableList des string repr�sentant le nom des Solides, Doit �tre
	 * unique, car utilis� pour le hashmap qui les lie ensemble
	 */
	public static ObservableList<String> listeNoms = FXCollections.observableArrayList();
	/**
	 * Le HashMap qui relie le solide � son nom, pour pouvoir le traiter dans le
	 * tableau ListView.
	 */
	public static HashMap<String, Solide> mapSolideNom = new HashMap<String, Solide>();

	public static Vector3d rendering_centre = new Vector3d(0, 0, 0);// Origine de l'affichage
	static int FONT_SIZE = 10; // taile des caract�res

	@Override
	public void start(Stage primaryStage) {
		try {
			final URL url = getClass().getResource("App.fxml");
			final FXMLLoader fxmlLoader = new FXMLLoader(url);

			root = fxmlLoader.load();
			Scene scene = new Scene(root);

			rendering_centre = new Vector3d(scene.getWidth() / 2, scene.getHeight() / 2, 0);

			// r�ajuse l'origine
			scene.widthProperty().addListener((obs, oldVal, newVal) -> {
				rendering_centre = new Vector3d(scene.getWidth() / 2, scene.getHeight() / 2, 0);
				update(listeSolides, Color.WHITE);
			});

			scene.heightProperty().addListener((obs, oldVal, newVal) -> {
				rendering_centre = new Vector3d(scene.getWidth() / 2, scene.getHeight() / 2, 0);
				update(listeSolides, Color.WHITE);
			});

			// GENERALISE INTO CONTROLLER
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

	/**
	 * Affiche les caract�res ASCII du solide sur la sc�ne.
	 * 
	 * @param s   - Le solide qu'on veut afficher.
	 * @param col - La couleur dans laquelle on veut l'afficher
	 */
	public static void update(ObservableList<Solide> sList, Color col) {
		((Pane) root.getCenter()).getChildren().clear();

		for (Solide s : sList) {
			s.render(FONT_SIZE);

			for (Point p : s.renderedSolide) {

				Text t = new Text(p.getEclairage());
				t.setFill(col);

				t.setFont(Font.font(STYLESHEET_CASPIAN, FontWeight.BOLD, FONT_SIZE));
				t.setLayoutX(p.getCoordonnee().x + rendering_centre.x);
				t.setLayoutY(p.getCoordonnee().y + rendering_centre.y);

				((Pane) root.getCenter()).getChildren().add(t);
			}
		}
	}

	/**
	 * Affiche les caract�res ASCII du solide sur la sc�ne. Si un solide est dans la
	 * liste en param�tre, il sera affich� en rouge au lieu de blanc. � utiliser
	 * pour montrer quels solides sont s�l�ctionn�s dans la ListView de la sc�ne
	 * 
	 * @param solide - Le ArrayList de tous les solides s�l�ctionn�s.
	 * @param col    - La couleur dans laquelle on veut afficher les solides dans la liste. Les autres seront affich�s en blanc.
	 */
	public static void updateSelective(ArrayList<Solide> solide, Color col) {
		((Pane) root.getCenter()).getChildren().clear();

		for (Solide s : listeSolides) {
			boolean solideSelected = false;

			for (Solide s2 : solide) {
				if (s2.equals(s)) solideSelected = true;
			}
			if (solideSelected) continue;

			s.render(FONT_SIZE);

			for (Point p : s.renderedSolide) {

				Text t = new Text(p.getEclairage());
				t.setFill(Color.WHITE);

				t.setFont(Font.font(STYLESHEET_CASPIAN, FontWeight.BOLD, FONT_SIZE));
				t.setLayoutX(p.getCoordonnee().x + rendering_centre.x);
				t.setLayoutY(p.getCoordonnee().y + rendering_centre.y);

				((Pane) root.getCenter()).getChildren().add(t);
			}
		}
		for (Solide s : solide) {
			s.render(FONT_SIZE);

			for (Point p : s.renderedSolide) {

				Text t = new Text(p.getEclairage());
				t.setFill(col);

				t.setFont(Font.font(STYLESHEET_CASPIAN, FontWeight.BOLD, FONT_SIZE));
				t.setLayoutX(p.getCoordonnee().x + rendering_centre.x);
				t.setLayoutY(p.getCoordonnee().y + rendering_centre.y);

				((Pane) root.getCenter()).getChildren().add(t);
			}
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
