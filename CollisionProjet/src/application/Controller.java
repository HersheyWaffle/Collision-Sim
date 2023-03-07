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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListView;
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

public class Controller {
//=========================VARIABLES=========================

	private Stage stage = new Stage();
	
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
	@FXML
	protected void effaceTout(ActionEvent arg0) {
		@SuppressWarnings("unchecked")
		ListView<String> lstView = (ListView<String>) ((VBox) Main.getRoot().getRight()).getChildren().get(1);

		panePane.getChildren().removeAll(panePane.getChildren());
		Main.listeNoms.removeAll(Main.listeNoms);
		Main.listeSolides.removeAll(Main.listeSolides);
		Main.mapSolideNom.clear();
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
		panneauAPropos.setMinSize(500, 250);

		VBox panneauTitresEtTextes = new VBox(); // panneau de textes
		panneauTitresEtTextes.setPadding(new Insets(10));

		Text txtTitre = new Text("À Propos");
		txtTitre.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		VBox.setMargin(txtTitre, new Insets(0, 0, 5, 0));

		Text txtAbout = new Text("Version: 0.0.1" + "\nAuteurs: Omar Ghazaly et Abel-Jimmy Oyono-Montoki");
		txtAbout.setFont(Font.font("Arial", 14));
		VBox.setMargin(txtAbout, new Insets(0, 0, 10, 0));

		Text txtTitreInstructions = new Text("Comment Utiliser"); // titre de la section calcul du score
		txtTitreInstructions.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		VBox.setMargin(txtTitreInstructions, new Insets(20, 0, 5, 0));

		Text txtInstructions = new Text("Naviguer la barre de menu et ajouter des objets à la scène."
				+ "\nModifier et combiner les objets, puis simuler avec le bouton SIMULATION.");
		txtInstructions.setFont(Font.font("Arial", 14));
		VBox.setMargin(txtInstructions, new Insets(0, 0, 15, 0));

		// ajoute les titres au panneauTitresEtTextes
		panneauTitresEtTextes.getChildren().addAll(txtTitre, txtAbout, txtTitreInstructions, txtInstructions);

		panneauTitresEtTextes.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, null, null)));
		panneauAPropos.getChildren().add(0, panneauTitresEtTextes);
		alert.setTitle("À  Propos");
		alert.setGraphic(null);
		alert.setHeaderText(null);
		alert.showAndWait();
	}

	/**
	 * Affiche la fenêtre de paramètres pour créer un object cubique.
	 * 
	 * @param arg0 - L'argument du ActionEvent, dans ce cas c'est le bouton Cube.
	 * @throws IOException
	 */
	@FXML
	protected void showFenetreCube(ActionEvent arg0) throws IOException {
		URL url = getClass().getResource("FenetreCube.fxml");
		FXMLLoader fxmlLoader = new FXMLLoader(url);

		BorderPane root = fxmlLoader.load();
		Scene scene = new Scene(root);

		stage.setScene(scene);
		stage.setTitle("Paramètres du Cube");
		stage.setResizable(false);
		stage.show();
	}

	/**
	 * Affiche la fenêtre de paramètres pour créer un object sphérique.
	 * 
	 * @param arg0 - L'argument du ActionEvent, dans ce cas c'est le bouton Sphère.
	 * @throws IOException
	 */
	@FXML
	protected void showFenetreSphere(ActionEvent arg0) throws IOException {
		URL url = getClass().getResource("FenetreSphere.fxml");
		FXMLLoader fxmlLoader = new FXMLLoader(url);

		BorderPane root = fxmlLoader.load();
		Scene scene = new Scene(root);

		stage.setScene(scene);
		stage.setTitle("Paramètres de la Sphère");
		stage.setResizable(false);
		stage.show();
	}

	/**
	 * Affiche la fenêtre de paramètres pour créer un object sphérique.
	 * 
	 * @param arg0 - L'argument du ActionEvent, dans ce cas c'est le bouton Sphère.
	 * @throws IOException
	 */
	@FXML
	protected void showFenetreCone(ActionEvent arg0) throws IOException {
		URL url = getClass().getResource("FenetreCone.fxml");
		FXMLLoader fxmlLoader = new FXMLLoader(url);

		BorderPane root = fxmlLoader.load();
		Scene scene = new Scene(root);

		stage.setScene(scene);
		stage.setTitle("Paramètres du Cône");
		stage.setResizable(false);
		stage.show();
	}

	/**
	 * Affiche la fenêtre de paramètres pour créer un object sphérique.
	 * 
	 * @param arg0 - L'argument du ActionEvent, dans ce cas c'est le bouton Sphère.
	 * @throws IOException
	 */
	@FXML
	protected void showFenetreCylindre(ActionEvent arg0) throws IOException {
		URL url = getClass().getResource("FenetreCylindre.fxml");
		FXMLLoader fxmlLoader = new FXMLLoader(url);

		BorderPane root = fxmlLoader.load();
		Scene scene = new Scene(root);

		stage.setScene(scene);
		stage.setTitle("Paramètres dy Cylindre");
		stage.setResizable(false);
		stage.show();
	}

	/**
	 * Crée un solide selon les paramètres séléctionnés dans la fenêtre de
	 * paramètres et les ajoute à une liste.
	 */
	@FXML
	protected void creeSolide() {
		Pane pane = (Pane) Main.getRoot().getChildren().get(0);
		@SuppressWarnings("unchecked")
		ListView<String> lstView = (ListView<String>) ((VBox) Main.getRoot().getRight()).getChildren().get(1);

		// Ne fait rien s'il y a des paramètres fautifs
		for (char c : txtLon.getText().toCharArray()) {
			if (!Character.isDigit(c) && c != '.') return;
		}
		for (char c : txtLar.getText().toCharArray()) {
			if (!Character.isDigit(c) && c != '.') return;
		}
		for (char c : txtX.getText().toCharArray()) {
			if (!Character.isDigit(c) && c != '.' && c != '-') return;
		}
		for (char c : txtY.getText().toCharArray()) {
			if (!Character.isDigit(c) && c != '.' && c != '-') return;
		}

		lstView.setItems(Main.listeNoms);
		
		if (txtTitreParam.getText().toLowerCase().contains("cube")) {
			for (char c : txtHau.getText().toCharArray()) {
				if (!Character.isDigit(c) && c != '.') return;
			}
			
			Cube cube = new Cube(Double.valueOf(txtLon.getText()), Double.valueOf(txtLar.getText()),
					Double.valueOf(txtHau.getText()));

			Main.listeSolides.add(cube);
			Main.listeNoms.add(txtNom.getText());
			Main.mapSolideNom.put(txtNom.getText(), cube);

			Solide.creeForme(cube.getCube(), pane, Double.valueOf(txtX.getText()) + 300, Double.valueOf(txtY.getText()) + 300);
		}
		else if (txtTitreParam.getText().toLowerCase().contains("sphère")) {
			Sphere sphere = new Sphere(Double.valueOf(txtLon.getText()), Double.valueOf(txtLar.getText()));

			Main.listeSolides.add(sphere);
			Main.listeNoms.add(txtNom.getText());
			Main.mapSolideNom.put(txtNom.getText(), sphere);

			Solide.creeForme(sphere.getSphere(), pane, Double.valueOf(txtX.getText()) + 300, Double.valueOf(txtY.getText()) + 300);
		}
		else if (txtTitreParam.getText().toLowerCase().contains("cône")) {
			Cone cone = new Cone(Double.valueOf(txtLon.getText()), Double.valueOf(txtLar.getText()));

			Main.listeSolides.add(cone);
			Main.listeNoms.add(txtNom.getText());
			Main.mapSolideNom.put(txtNom.getText(), cone);

			Solide.creeForme(cone.getCone(), pane, Double.valueOf(txtX.getText()) + 300, Double.valueOf(txtY.getText()) + 300);
		}
		else if (txtTitreParam.getText().toLowerCase().contains("cylindre")) {
			Cylindre cylindre = new Cylindre(Double.valueOf(txtLon.getText()), Double.valueOf(txtLar.getText()));

			Main.listeSolides.add(cylindre);
			Main.listeNoms.add(txtNom.getText());
			Main.mapSolideNom.put(txtNom.getText(), cylindre);

			Solide.creeForme(cylindre.getCylindre(), pane, Double.valueOf(txtX.getText()) + 300, Double.valueOf(txtY.getText()) + 300);
		}

//============DEBUG================
		System.out.println(txtLon.getText() + " " + txtLar.getText() + " " + txtHau.getText());
//=================================

		System.out.println(Main.listeSolides.size());
	}

	/**
	 * Le Event Handler qui va gérérer les touches de clavier pour effectuer la rotation des solides dans l'éditeur.
	 * 
	 * @return Retourne le KeyEvent de la rotation
	 */
	@FXML
	public EventHandler<KeyEvent> rotationKey() {
		return new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent arg0) {
				double posNeg = arg0.isShiftDown() ? -15 : 15;
				Pane pane = (Pane) Main.getRoot().getChildren().get(0);

				ArrayList<Vector3d> tousPoints = new ArrayList<Vector3d>();
				for (Solide s : Main.listeSolides) {
					tousPoints.addAll(s.getSolide());
					System.out.println(tousPoints.size());
				}

				if (arg0.getCode() == KeyCode.X) {
					Solide.rotateSolide(tousPoints, posNeg, 0, 0, 200, 200);
					System.out.println("x");

				}
				else if (arg0.getCode() == KeyCode.C) {
					Solide.rotateSolide(tousPoints, 0, posNeg, 0, 200, 200);
					System.out.println("y");
				}
				else if (arg0.getCode() == KeyCode.Z) {
					Solide.rotateSolide(tousPoints, 0, 0, posNeg, 200, 200);
					System.out.println("z");
				}
				else {
					return;
				}
				pane.getChildren().removeAll(pane.getChildren());
				Solide.creeForme(tousPoints, pane, 0,0);
			}
		};
	}

	/**
	 * Termine le programme.
	 */
	@FXML
	protected void quitter() {
		System.exit(0);
	}
}