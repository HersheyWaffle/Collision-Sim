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
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * La clase main qui initialise le simulateur.
 * 
 * @version 1.6 2023-05-11
 * @author Omar Ghazaly, Abel-Jimmy Oyono-Montoki
 */
public class Main extends Application {

//=========================VARIABLES=========================		

	// Global Variables
	public static final double SECONDS_PER_UPDATE = 0.05;
	public static boolean DEBUG_MODE = false;
	public static boolean COLLISIONS_INELASTIQUES = false;

	// Origine
	public static Vector3d renderingCentre = new Vector3d(0, 0, 0);// Origine de l'affichage
	public static Vector3d renderingFocus = new Vector3d(0, 0, 0);// Origine changée

	// Utilitaires
	static int FONT_SIZE = 10; // taile des caractères

	// Traitement du ListView
	public static BorderPane root;
	/**
	 * La ObservableList des solides dans la scène
	 */
	public static ObservableList<Solide> listeSolides = FXCollections.observableArrayList();
	/**
	 * La ObservableList des string représentant le nom des Solides, Doit être
	 * unique, car utilisé pour le hashmap qui les lie ensemble
	 */
	public static ObservableList<String> listeNoms = FXCollections.observableArrayList();
	/**
	 * Le HashMap qui relie le solide à son nom, pour pouvoir le traiter dans le
	 * tableau ListView.
	 */
	public static HashMap<String, Solide> mapSolideNom = new HashMap<String, Solide>();

//=========================METHODES=========================

	@Override
	public void start(Stage primaryStage) {
		try {
			final URL url = getClass().getResource("App.fxml");
			final FXMLLoader fxmlLoader = new FXMLLoader(url);

			root = fxmlLoader.load();
			Scene scene = new Scene(root);

			// Centre de focus
			renderingCentre = new Vector3d(scene.getWidth() / 2, scene.getHeight() / 2, 0);

			// réajuse l'origine
			scene.widthProperty().addListener((obs, oldVal, newVal) -> {
				renderingCentre = new Vector3d(scene.getWidth() / 2, scene.getHeight() / 2, 0);
				updateCollision(Main.listeSolides);
			});

			scene.heightProperty().addListener((obs, oldVal, newVal) -> {
				renderingCentre = new Vector3d(scene.getWidth() / 2, scene.getHeight() / 2, 0);
				updateCollision(Main.listeSolides);
			});

			root.setOnScroll((ScrollEvent event) -> {
				double z = event.getDeltaY();

				Solide.distanceObservateur += z;
				updateCollision(Main.listeSolides);
			});

			update(listeSolides, Color.WHITE);

			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("Simulateur de collisions");
			primaryStage.setScene(scene);
			primaryStage.show();

			Controller controller = new Controller();

			root.setOnMouseClicked((MouseEvent event) -> {
				// restore le virtual centre original
				for (Solide s : listeSolides) {
					s.virtualCentre.add(renderingFocus);
				}

				// change le focus
				if (event.getButton() == MouseButton.PRIMARY) {
					Vector3d previousFocus = (Vector3d) renderingFocus.clone();

					// coordonnée du clic
					renderingFocus = new Vector3d(event.getSceneX() - scene.getWidth() / 2,
							event.getSceneY() - scene.getHeight() / 2, -Solide.distanceObservateur);
					Solide.distanceObservateur = 0;

					renderingFocus.add(previousFocus);
				} else if (event.getButton() == MouseButton.SECONDARY) {
					if (!renderingFocus.equals(new Vector3d())) {
						// Solide.distanceObservateur = -renderingFocus.z;
						renderingFocus = new Vector3d();
					} else {
						Solide.distanceObservateur = -renderingFocus.z;
						renderingFocus = new Vector3d();
					}

				}

				// change le virtual centre en fonction du nouveau focus
				for (Solide s : listeSolides) {
					if (DEBUG_MODE)
						System.out.println("centre de l'objet avant: " + s.virtualCentre);
					s.virtualCentre.sub(renderingFocus);
					if (DEBUG_MODE)
						System.out.println("centre de l'objet après: " + s.virtualCentre);
				}
				// ajuste l'origine
				updateCollision(listeSolides);
			});

			root.setOnKeyPressed(controller.rotationKey());
			root.requestFocus();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Réinitialise la collision des solides.
	 * 
	 * @param sList - La liste de solides. Puisque nous n'avons pas les axes, c'est
	 *              toujours listeSolides.
	 */
	public static void updateCollision(ObservableList<Solide> sList) {
		((Pane) root.getCenter()).getChildren().clear();
		for (Solide s : sList) {
			s.isColliding = false;
			if (s.resize)
				s.setWhite();
			Lumiere.lumiereObjet(s.getSolide());
		}
		for (int i = 0; i < sList.size() - 1; i++) {
			for (int j = i + 1; j < sList.size(); j++) {
				if (sList.get(i).detecteurDeProximite(sList.get(j), SECONDS_PER_UPDATE)) {
					sList.get(i).detecteurDeCollision(sList.get(j), SECONDS_PER_UPDATE);
				}
			}
		}
		update(sList, Color.WHITE);
	}

	/**
	 * Affiche les caractères ASCII du solide sur la scène.
	 * 
	 * @param s   - Le solide qu'on veut afficher.
	 * @param col - La couleur dans laquelle on veut l'afficher
	 */
	public static void update(ObservableList<Solide> sList, Color col) {
		((Pane) root.getCenter()).getChildren().clear();

		if (DEBUG_MODE)
			System.out.println("Update sList " + sList);

		for (Solide s : sList) {
			if (DEBUG_MODE)
				System.out.println(sList.indexOf(s) + " Prerender " + s.getRenderedSolide().size());
			s.render(FONT_SIZE);
			if (DEBUG_MODE)
				System.out.println(sList.indexOf(s) + " Postrender " + s.getRenderedSolide().size());

			for (Point p : s.renderedSolide) {
				Text t = new Text(p.getEclairage());
				if (s.resize)
					t.setFill(col);

				t.setFont(Font.font(STYLESHEET_CASPIAN, FontWeight.BOLD, s.fontSize));
				t.setLayoutX(p.getCoordonnee().x + renderingCentre.x);
				t.setLayoutY(p.getCoordonnee().y + renderingCentre.y);

				((Pane) root.getCenter()).getChildren().add(t);
			}
		}
	}

	/**
	 * Affiche les caractères ASCII du solide sur la scène. Si un solide est dans la
	 * liste en paramètre, il sera affiché en rouge au lieu de blanc. À utiliser
	 * pour montrer quels solides sont séléctionnés dans la ListView de la scène.
	 * 
	 * @param solide - Le ArrayList de tous les solides séléctionnés.
	 * @param col    - La couleur dans laquelle on veut afficher les solides dans la
	 *               liste. Les autres seront affichés en blanc.
	 */
	public static void updateSelective(ArrayList<Solide> solide, Color col) {
		((Pane) root.getCenter()).getChildren().clear();

		for (Solide s : listeSolides) {
			boolean solideSelected = false;

			for (Solide s2 : solide) {
				if (s2.equals(s))
					solideSelected = true;
			}
			if (solideSelected)
				continue;

			s.render(FONT_SIZE);

			for (Point p : s.renderedSolide) {

				Text t = new Text(p.getEclairage());
				t.setFill(Color.WHITE);

				t.setFont(Font.font(STYLESHEET_CASPIAN, FontWeight.BOLD, s.fontSize));
				t.setLayoutX(p.getCoordonnee().x + renderingCentre.x);
				t.setLayoutY(p.getCoordonnee().y + renderingCentre.y);

				((Pane) root.getCenter()).getChildren().add(t);
			}
		}
		for (Solide s : solide) {
			s.render(FONT_SIZE);

			for (Point p : s.renderedSolide) {

				Text t = new Text(p.getEclairage());
				t.setFill(col);

				t.setFont(Font.font(STYLESHEET_CASPIAN, FontWeight.BOLD, s.fontSize));
				t.setLayoutX(p.getCoordonnee().x + renderingCentre.x);
				t.setLayoutY(p.getCoordonnee().y + renderingCentre.y);

				((Pane) root.getCenter()).getChildren().add(t);
			}
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
