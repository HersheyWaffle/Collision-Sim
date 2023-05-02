package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.vecmath.Vector3d;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Classe utilitaire qui gère les controlles de l'interface d'utilisation.
 * 
 * @version 1.3.0 2023-03-08
 * @author Omar Ghazaly
 */
public class Controller {

//=========================VARIABLES=========================

	final double ROTATION_ANGLE = Math.PI / 12;
	final double DEPLACEMENT_CENTRE = 5;

	private Stage stage = new Stage();
	private ListView<String> lstView;

	// TODO Set contextMenu, moveable obj, resize

	private MenuItem ctxtMenuItemEdit = new MenuItem("Éditer");
	private MenuItem ctxtMenuItemDelete = new MenuItem("Supprimer");
	private ContextMenu ctxtMenuListObj = new ContextMenu();

	@FXML
	Pane panePane; // Le panneau Pane dans lequel se trouvent les solides
	@FXML
	Text txtTitreParam;
	@FXML
	TextField txtLon;
	@FXML
	TextField txtLar;
	@FXML
	TextField txtHau;
	@FXML
	TextField txtX;
	@FXML
	TextField txtY;
	@FXML
	TextField txtZ;
	@FXML
	TextField txtNom;
	@FXML
	TextField titreLon;
	@FXML
	TextField titreLar;
	@FXML
	TextField titreHau;

//=========================METHODES=========================

	/**
	 * Efface tous les objets dans la scène.
	 * 
	 * @param arg0 - L'argument du ActionEvent, dans ce cas c'est le bouton
	 *             Supprimer
	 */
	@SuppressWarnings("unchecked")
	@FXML
	protected void effaceTout(ActionEvent arg0) {

		panePane.getChildren().removeAll(panePane.getChildren());
		Main.listeNoms.removeAll(Main.listeNoms);
		Main.listeSolides.removeAll(Main.listeSolides);
		Main.mapSolideNom.clear();

		if (lstView == null) {
			lstView = (ListView<String>) ((VBox) Main.root.getRight()).getChildren().get(1);
			lstView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			lstView.setContextMenu(creeContextMenu());
		}

		lstView.setItems(Main.listeNoms);
	}

	/**
	 * Affiche le panneau À Propos du programme.
	 * 
	 * @param arg0 - L'argument du ActionEvent, dans ce cas c'est le bouton À Propos
	 */
	@FXML
	protected void afficheAPropos(ActionEvent arg0) {
		Alert alert = new Alert(AlertType.INFORMATION);
		DialogPane panneauAPropos = alert.getDialogPane(); // panneau racine de la fenetre
		panneauAPropos.setMinSize(500, 325);

		VBox panneauTitresEtTextes = new VBox(); // panneau de textes
		panneauTitresEtTextes.setPadding(new Insets(10));

		Text txtTitre = new Text("À Propos");
		txtTitre.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		VBox.setMargin(txtTitre, new Insets(0, 0, 5, 0));

		Text txtAbout = new Text("Version: 0.4.0" + "\nAuteurs: Omar Ghazaly et Abel-Jimmy Oyono-Montoki");
		txtAbout.setFont(Font.font("Arial", 14));
		VBox.setMargin(txtAbout, new Insets(0, 0, 10, 0));

		Text txtTitreInstructions = new Text("Comment Utiliser"); // titre de la section calcul du score
		txtTitreInstructions.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		VBox.setMargin(txtTitreInstructions, new Insets(20, 0, 5, 0));

		Text txtInstructions = new Text("Naviguer la barre de menu et ajouter des objets à la scène."
				+ "\nModifier et combiner les objets, puis simuler avec le bouton SIMULATION.");
		txtInstructions.setFont(Font.font("Arial", 14));
		VBox.setMargin(txtInstructions, new Insets(0, 0, 15, 0));

		Text txtTitreControles = new Text("Contrôles");
		txtTitreControles.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		VBox.setMargin(txtTitreControles, new Insets(20, 0, 5, 0));

		Text txtControles = new Text("X, Y, C: Rotation de la scène. Peser Shift pour tourner dans le sens inverse."
				+ "\nBoutons flèche: Déplacement de la scène." + "\nW, S: Déplacement de la scène en profondeur.");
		txtControles.setFont(Font.font("Arial", 14));
		VBox.setMargin(txtControles, new Insets(0, 0, 15, 0));

		// ajoute les titres au panneauTitresEtTextes
		panneauTitresEtTextes.getChildren().addAll(txtTitre, txtAbout, txtTitreInstructions, txtInstructions,
				txtTitreControles, txtControles);

		panneauTitresEtTextes.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, null, null)));
		panneauAPropos.getChildren().add(0, panneauTitresEtTextes);
		alert.setTitle("À  Propos");
		alert.setGraphic(null);
		alert.setHeaderText(null);
		alert.showAndWait();
	}

	/**
	 * Affiche la fenêtre de paramètres pour créer un object solide.
	 * 
	 * @param arg0 - L'argument du ActionEvent, dans ce cas c'est le MenuItem du
	 *             solide séléctionné.
	 * @throws IOException Dans le cas d'un fichier FXML invalide.
	 */
	@FXML
	protected void showFenetreSolide(ActionEvent arg0) throws IOException {
		URL url = getClass().getResource("Fenetre" + ((MenuItem) arg0.getSource()).getText() + ".fxml");
		FXMLLoader fxmlLoader = new FXMLLoader(url);

		BorderPane root = fxmlLoader.load();
		Scene scene = new Scene(root);

		stage.setScene(scene);
		stage.setTitle("Paramètres du " + ((MenuItem) arg0.getSource()).getText());
		if (((MenuItem) arg0.getSource()).getText().equals("Sphere")) stage.setTitle("Paramètres de la Sphère");
		stage.setResizable(false);
		stage.show();
	}

	/**
	 * Crée un solide selon les paramètres séléctionnés dans la fenêtre de
	 * paramètres et les ajoute à une liste.
	 */
	@SuppressWarnings("unchecked")
	@FXML
	protected void creeSolide() {
		// Ne fait rien s'il y a des paramètres fautifs
		if (txtLon.getText().toCharArray().length == 0) return;
		for (char c : txtLon.getText().toCharArray()) {
			if (!Character.isDigit(c) && c != '.') return;
		}
		if (txtLar.getText().toCharArray().length == 0) return;
		for (char c : txtLar.getText().toCharArray()) {
			if (!Character.isDigit(c) && c != '.') return;
		}
		if (txtX.getText().toCharArray().length == 0) return;
		for (char c : txtX.getText().toCharArray()) {
			if (!Character.isDigit(c) && c != '.' && c != '-') return;
		}
		if (txtY.getText().toCharArray().length == 0) return;
		for (char c : txtY.getText().toCharArray()) {
			if (!Character.isDigit(c) && c != '.' && c != '-') return;
		}
		if (txtZ.getText().toCharArray().length == 0) return;
		for (char c : txtZ.getText().toCharArray()) {
			if (!Character.isDigit(c) && c != '.' && c != '-') return;
		}

		// Initialise la ListView des solides si ceci n'a pas déjà été fait
		if (lstView == null) {
			lstView = (ListView<String>) ((VBox) Main.root.getRight()).getChildren().get(1);
			lstView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			lstView.setContextMenu(creeContextMenu());

			if (Main.DEBUG_MODE) System.out.println("Set ListView");
		}

		lstView = (ListView<String>) ((VBox) Main.root.getRight()).getChildren().get(1);
		lstView.setItems(Main.listeNoms);

		for (String s : Main.listeNoms) {
			if (txtNom.getText().equals(s)) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setContentText("Le nom des solides doit être unique!");
				alert.show();
				return;
			}
		}

		if (txtTitreParam.getText().toLowerCase().contains("cube")) {
			if (txtHau.getText().toCharArray().length == 0) return;
			for (char c : txtHau.getText().toCharArray()) {
				if (!Character.isDigit(c) && c != '.') return;
			}

			Cube cube = new Cube(Double.valueOf(txtLon.getText()), Double.valueOf(txtLar.getText()),
					Double.valueOf(txtHau.getText()), Main.FONT_SIZE);

			cube.virtualCentre = new Vector3d(Double.valueOf(txtX.getText()), Double.valueOf(txtY.getText()),
					Double.valueOf(txtZ.getText()));

			Lumiere.lumiere_Objet(cube.getCube());

			Main.listeSolides.add(cube);
			Main.listeNoms.add(txtNom.getText());
			Main.mapSolideNom.put(txtNom.getText(), cube);
			Main.update(Main.listeSolides, Color.WHITE);

//			Solide.creeForme(cube.getCube(), pane, Double.valueOf(txtX.getText()) + 300,
//					Double.valueOf(txtY.getText()) + 300, Double.valueOf(txtZ.getText()));
		}
		else if (txtTitreParam.getText().toLowerCase().contains("sphère")) {
			Sphere sphere = new Sphere(Double.valueOf(txtLon.getText()), Double.valueOf(txtLar.getText()),
					Main.FONT_SIZE);

			sphere.virtualCentre = new Vector3d(Double.valueOf(txtX.getText()), Double.valueOf(txtY.getText()),
					Double.valueOf(txtZ.getText()));

			Lumiere.lumiere_Objet(sphere.getSphere());

			Main.listeSolides.add(sphere);
			Main.listeNoms.add(txtNom.getText());
			Main.mapSolideNom.put(txtNom.getText(), sphere);
			Main.update(Main.listeSolides, Color.WHITE);

//			Solide.creeForme(sphere.getSphere(), pane, Double.valueOf(txtX.getText()) + 300,
//					Double.valueOf(txtY.getText()) + 300, Double.valueOf(txtZ.getText()));
		}
		else if (txtTitreParam.getText().toLowerCase().contains("cône")) {
			Cone cone = new Cone(Double.valueOf(txtLon.getText()), Double.valueOf(txtLar.getText()), Main.FONT_SIZE);

			cone.virtualCentre = new Vector3d(Double.valueOf(txtX.getText()), Double.valueOf(txtY.getText()),
					Double.valueOf(txtZ.getText()));

			Lumiere.lumiere_Objet(cone.getCone());

			Main.listeSolides.add(cone);
			Main.listeNoms.add(txtNom.getText());
			Main.mapSolideNom.put(txtNom.getText(), cone);
			Main.update(Main.listeSolides, Color.WHITE);

//			Solide.creeForme(cone.getCone(), pane, Double.valueOf(txtX.getText()) + 300,
//					Double.valueOf(txtY.getText()) + 300, Double.valueOf(txtZ.getText()));
		}
		else if (txtTitreParam.getText().toLowerCase().contains("cylindre")) {
			Cylindre cylindre = new Cylindre(Double.valueOf(txtLon.getText()), Double.valueOf(txtLar.getText()),
					Main.FONT_SIZE);

			cylindre.virtualCentre = new Vector3d(Double.valueOf(txtX.getText()), Double.valueOf(txtY.getText()),
					Double.valueOf(txtZ.getText()));

			Lumiere.lumiere_Objet(cylindre.getCylindre());

			Main.listeSolides.add(cylindre);
			Main.listeNoms.add(txtNom.getText());
			Main.mapSolideNom.put(txtNom.getText(), cylindre);
			Main.update(Main.listeSolides, Color.WHITE);

//			Solide.creeForme(cylindre.getCylindre(), pane, Double.valueOf(txtX.getText()) + 300,
//					Double.valueOf(txtY.getText()) + 300, Double.valueOf(txtZ.getText()));
		}

		if (Main.DEBUG_MODE) System.out.println(txtLon.getText() + " " + txtLar.getText() + " " + txtHau.getText());
		if (Main.DEBUG_MODE) System.out.println(Main.listeSolides.size());
	}

	/**
	 * Le Event Handler qui va gérérer les touches de clavier pour effectuer la
	 * rotation des solides dans l'éditeur.
	 * 
	 * @return Retourne le KeyEvent de la rotation
	 */
	@FXML
	public EventHandler<KeyEvent> rotationKey() {
		return new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent arg0) {
				double posNeg = arg0.isShiftDown() ? -1 : 1;

//				ArrayList<Point> tousPoints = new ArrayList<Point>();
//				if (arg0.isControlDown()) {
//					//Déplace les solides sélectionnés
//					for (String s : lstView.getSelectionModel().getSelectedItems()) {
//						tousPoints.addAll(Main.mapSolideNom.get(s).getSolide());
//						if (Main.DEBUG_MODE) System.out.println(tousPoints.size());
//					}
//				}
//				else {
//					//Sinon déplace tous les solides
//					for (Solide s : Main.listeSolides) {
//						tousPoints.addAll(s.getSolide());
//						if (Main.DEBUG_MODE) System.out.println(tousPoints.size());
//					}
//				}
//				
//
//				Solide tousSolides = new Solide();
//				tousSolides.setSolide(tousPoints);

				for (Solide tousSolides : Main.listeSolides) {
					if (arg0.getCode() == KeyCode.X) {
						tousSolides.rotate(ROTATION_ANGLE * posNeg, 0, 0);
						Main.update(Main.listeSolides, Color.WHITE);
						
						if (Main.DEBUG_MODE) System.out.println("x");
					}
					else if (arg0.getCode() == KeyCode.C) {
						tousSolides.rotate(0, ROTATION_ANGLE * posNeg, 0);
						Main.update(Main.listeSolides, Color.WHITE);
						
						if (Main.DEBUG_MODE) System.out.println("y");
					}
					else if (arg0.getCode() == KeyCode.Z) {
						tousSolides.rotate(0, 0, ROTATION_ANGLE * posNeg);
						Main.update(Main.listeSolides, Color.WHITE);
						
						if (Main.DEBUG_MODE) System.out.println("z");
					}
					else if (arg0.getCode() == KeyCode.W) {
						// FIXME Ne fait pas de zoom, augmente seulement la taille de police au lieu de
						// faire augmenter la taille de la forme
						tousSolides.deplacement(new Vector3d(0, 0, DEPLACEMENT_CENTRE));
						Main.update(Main.listeSolides, Color.WHITE);
						
						if (Main.DEBUG_MODE) System.out.println("w");
					}
					else if (arg0.getCode() == KeyCode.S) {
						// FIXME
						tousSolides.deplacement(new Vector3d(0, 0, -DEPLACEMENT_CENTRE));
						Main.update(Main.listeSolides, Color.WHITE);
						
						if (Main.DEBUG_MODE) System.out.println("s");
					}
					else {
						break;
					}
				}

				if (arg0.getCode() == KeyCode.UP) {
					Main.renderingCentre.add(new Vector3d(0, -DEPLACEMENT_CENTRE, 0));
					Main.update(Main.listeSolides, Color.WHITE);
					
					if (Main.DEBUG_MODE) System.out.println("^");
				}
				else if (arg0.getCode() == KeyCode.DOWN) {
					Main.renderingCentre.add(new Vector3d(0, DEPLACEMENT_CENTRE, 0));
					Main.update(Main.listeSolides, Color.WHITE);
					
					if (Main.DEBUG_MODE) System.out.println("v");
				}
				else if (arg0.getCode() == KeyCode.LEFT) {
					Main.renderingCentre.add(new Vector3d(-DEPLACEMENT_CENTRE, 0, 0));
					Main.update(Main.listeSolides, Color.WHITE);
					
					if (Main.DEBUG_MODE) System.out.println("<");
				}
				else if (arg0.getCode() == KeyCode.RIGHT) {
					Main.renderingCentre.add(new Vector3d(DEPLACEMENT_CENTRE, 0, 0));
					Main.update(Main.listeSolides, Color.WHITE);
					
					if (Main.DEBUG_MODE) System.out.println(">");
				}
				else {
					return;
				}
			}

			// LEGACY CODE
//				pane.getChildren().removeAll(pane.getChildren());
//				Solide.creeForme(tousPoints, pane, 0, 0, 0);
		};
	}

	/**
	 * Associe le MouseClick Event à la ListView des solides pour colorier tous les
	 * solides séléctionnés dans la liste.
	 */
	@FXML
	@SuppressWarnings("unchecked")
	public void lstSelectSolide() {
		// Parce que JavaFX...
		if (lstView == null) {
			lstView = (ListView<String>) ((VBox) Main.root.getRight()).getChildren().get(1);
			lstView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			lstView.setContextMenu(creeContextMenu());
		}

		lstView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				ArrayList<Solide> tousSolides = new ArrayList<Solide>();

				for (String s : lstView.getSelectionModel().getSelectedItems()) {
					tousSolides.add(Main.mapSolideNom.get(s));
				}
				Main.updateSelective(tousSolides, Color.RED);
				if (Main.DEBUG_MODE) System.out.println("Colored");
			}
		});

		lstView.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent e) {
				if (e.getCode() != KeyCode.DELETE) return;

				for (String s : lstView.getSelectionModel().getSelectedItems()) {
					// Clean unused data
					Main.mapSolideNom.get(s).getSolide().removeAll(Main.mapSolideNom.get(s).getSolide());

					// Enlève tous les caractères, car ils seront réinitialisés
					((Pane) Main.root.getCenter()).getChildren().removeAll((Pane) Main.root.getCenter());

					// Enlève le(s) solide(s) de toutes les listes
					Main.listeSolides.remove(Main.mapSolideNom.get(s));
					Main.listeNoms.remove(s);
					Main.mapSolideNom.remove(s);
					// Set la liste à la nouvelle valeur sans le(s) solide(s)
					lstView.setItems(Main.listeNoms);

					// Rajoute les solides à la scène
					Main.update(Main.listeSolides, Color.WHITE);

					if (Main.DEBUG_MODE) System.out.println("lstView : " + lstView.getChildrenUnmodifiable().size());
					if (Main.DEBUG_MODE) System.out.println("lstNom : " + Main.listeNoms.size());
					if (Main.DEBUG_MODE) System.out.println("lstSloide : " + Main.listeSolides.size());
				}

			}

		});
	}

	protected ContextMenu creeContextMenu() {
		ctxtMenuListObj.getItems().addAll(ctxtMenuItemEdit, ctxtMenuItemDelete);

		ctxtMenuItemDelete.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {

				// DUPLICATE de lstView OnDelete Event
				for (String s : lstView.getSelectionModel().getSelectedItems()) {
					// Clean unused data
					Main.mapSolideNom.get(s).getSolide().removeAll(Main.mapSolideNom.get(s).getSolide());

					// Enlève tous les caractères, car ils seront réinitialisés
					((Pane) Main.root.getCenter()).getChildren().removeAll((Pane) Main.root.getCenter());

					// Enlève le(s) solide(s) de toutes les listes
					Main.listeSolides.remove(Main.mapSolideNom.get(s));
					Main.listeNoms.remove(s);
					Main.mapSolideNom.remove(s);
					// Set la liste à la nouvelle valeur sans le(s) solide(s)
					lstView.setItems(Main.listeNoms);

					// Rajoute les solides à la scène
					Main.update(Main.listeSolides, Color.WHITE);

					if (Main.DEBUG_MODE) System.out.println("lstView : " + lstView.getChildrenUnmodifiable().size());
					if (Main.DEBUG_MODE) System.out.println("lstNom : " + Main.listeNoms.size());
					if (Main.DEBUG_MODE) System.out.println("lstSloide : " + Main.listeSolides.size());
				}

			}

		});

		ctxtMenuItemEdit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				URL url = getClass().getResource("FenetreEdit.fxml");
				FXMLLoader fxmlLoader = new FXMLLoader(url);

				BorderPane root;
				try {
					root = fxmlLoader.load();
					Scene scene = new Scene(root);
					stage.setScene(scene);
					stage.setTitle("Paramètres de " + lstView.getSelectionModel().getSelectedItems().get(0));
					stage.setResizable(false);
					stage.show();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});

		return ctxtMenuListObj;
	}

	/**
	 * Crée un solide selon les paramètres séléctionnés dans la fenêtre de
	 * paramètres et les ajoute à une liste.
	 */
	@SuppressWarnings("unchecked")
	@FXML
	protected void editSolide() {
		// Ne fait rien s'il y a des paramètres fautifs
		if (txtLon.getText().toCharArray().length == 0) return;
		for (char c : txtLon.getText().toCharArray()) {
			if (!Character.isDigit(c) && c != '.') return;
		}
		if (txtLar.getText().toCharArray().length == 0) return;
		for (char c : txtLar.getText().toCharArray()) {
			if (!Character.isDigit(c) && c != '.') return;
		}
		if (txtHau.getText().toCharArray().length == 0) return;
		for (char c : txtHau.getText().toCharArray()) {
			if (!Character.isDigit(c) && c != '.') return;
		}
		if (txtX.getText().toCharArray().length == 0) return;
		for (char c : txtX.getText().toCharArray()) {
			if (!Character.isDigit(c) && c != '.' && c != '-') return;
		}
		if (txtY.getText().toCharArray().length == 0) return;
		for (char c : txtY.getText().toCharArray()) {
			if (!Character.isDigit(c) && c != '.' && c != '-') return;
		}
		if (txtZ.getText().toCharArray().length == 0) return;
		for (char c : txtZ.getText().toCharArray()) {
			if (!Character.isDigit(c) && c != '.' && c != '-') return;
		}

		if (lstView == null) {
			lstView = (ListView<String>) ((VBox) Main.root.getRight()).getChildren().get(1);
			lstView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			lstView.setContextMenu(creeContextMenu());

			if (Main.DEBUG_MODE) System.out.println("Set ListView");
		}

		// TODO Edit le solide (Scale, Nom)

		Solide solide = Main.mapSolideNom.get(lstView.getSelectionModel().getSelectedItems().get(0));

		for (String s : Main.listeNoms) {
			if (txtNom.getText().equals(s)) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setContentText("Le nom des solides doit être unique!");
				alert.show();
				return;
			}
		}
		
		solide.rotateSelf(Double.valueOf(txtX.getText()) * Math.PI / 180,
						  Double.valueOf(txtY.getText()) * Math.PI / 180, 
						  Double.valueOf(txtZ.getText()) * Math.PI / 180);
		Main.update(Main.listeSolides, Color.WHITE);

		if (Main.DEBUG_MODE) System.out.println("x : " + Double.valueOf(txtX.getText()) * Math.PI / 180 + " rad");
		if (Main.DEBUG_MODE) System.out.println("y : " + Double.valueOf(txtY.getText()) * Math.PI / 180 + " rad");
		if (Main.DEBUG_MODE) System.out.println("z : " + Double.valueOf(txtZ.getText()) * Math.PI / 180 + " rad");
	}

	/**
	 * Termine le programme.
	 */
	@FXML
	protected void quitter() {
		System.exit(0);
	}
}