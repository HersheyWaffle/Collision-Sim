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
 * Classe utilitaire qui g�re les controlles de l'interface d'utilisation.
 * 
 * @version 1.3.0 2023-03-08
 * @author Omar Ghazaly
 */
public class Controller {
//=========================VARIABLES=========================

	private Stage stage = new Stage();
	private ListView<String> lstView;
	
	//TODO Set contextMenu, moveable obj, resize, delete
	
	private double centreX = 300;
	private double centreY = 300;
	private double centreZ =   0;

	private MenuItem ctxtMenuItemEdit = new MenuItem("�diter");
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

	public double getCentreX() {
		return centreX;
	}

	public void setCentreX(double centreX) {
		this.centreX = centreX;
	}

	public double getCentreY() {
		return centreY;
	}

	public void setCentreY(double centreY) {
		this.centreY = centreY;
	}

	public double getCentreZ() {
		return centreZ;
	}

	public void setCentreZ(double centreZ) {
		this.centreZ = centreZ;
	}

	/**
	 * Efface tous les objets dans la sc�ne.
	 * 
	 * @param arg0 - L'argument du ActionEvent, dans ce cas c'est le bouton
	 *             Supprimer
	 */
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
	
	protected void effaceSolide(ActionEvent arg0) {

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
	 * Affiche le panneau � Propos du programme.
	 * 
	 * @param arg0 - L'argument du ActionEvent, dans ce cas c'est le bouton � Propos
	 */
	@FXML
	protected void afficheAPropos(ActionEvent arg0) {
		Alert alert = new Alert(AlertType.INFORMATION);
		DialogPane panneauAPropos = alert.getDialogPane(); // panneau racine de la fenetre
		panneauAPropos.setMinSize(500, 250);

		VBox panneauTitresEtTextes = new VBox(); // panneau de textes
		panneauTitresEtTextes.setPadding(new Insets(10));

		Text txtTitre = new Text("� Propos");
		txtTitre.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		VBox.setMargin(txtTitre, new Insets(0, 0, 5, 0));

		Text txtAbout = new Text("Version: 0.0.1" + "\nAuteurs: Omar Ghazaly et Abel-Jimmy Oyono-Montoki");
		txtAbout.setFont(Font.font("Arial", 14));
		VBox.setMargin(txtAbout, new Insets(0, 0, 10, 0));

		Text txtTitreInstructions = new Text("Comment Utiliser"); // titre de la section calcul du score
		txtTitreInstructions.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		VBox.setMargin(txtTitreInstructions, new Insets(20, 0, 5, 0));

		Text txtInstructions = new Text("Naviguer la barre de menu et ajouter des objets � la sc�ne."
				+ "\nModifier et combiner les objets, puis simuler avec le bouton SIMULATION.");
		txtInstructions.setFont(Font.font("Arial", 14));
		VBox.setMargin(txtInstructions, new Insets(0, 0, 15, 0));

		// ajoute les titres au panneauTitresEtTextes
		panneauTitresEtTextes.getChildren().addAll(txtTitre, txtAbout, txtTitreInstructions, txtInstructions);

		panneauTitresEtTextes.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, null, null)));
		panneauAPropos.getChildren().add(0, panneauTitresEtTextes);
		alert.setTitle("�� Propos");
		alert.setGraphic(null);
		alert.setHeaderText(null);
		alert.showAndWait();
	}

	/**
	 * Affiche la fen�tre de param�tres pour cr�er un object cubique.
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
		stage.setTitle("Param�tres du Cube");
		stage.setResizable(false);
		stage.show();
	}

	/**
	 * Affiche la fen�tre de param�tres pour cr�er un object sph�rique.
	 * 
	 * @param arg0 - L'argument du ActionEvent, dans ce cas c'est le bouton Sph�re.
	 * @throws IOException
	 */
	@FXML
	protected void showFenetreSphere(ActionEvent arg0) throws IOException {
		URL url = getClass().getResource("FenetreSphere.fxml");
		FXMLLoader fxmlLoader = new FXMLLoader(url);

		BorderPane root = fxmlLoader.load();
		Scene scene = new Scene(root);

		stage.setScene(scene);
		stage.setTitle("Param�tres de la Sph�re");
		stage.setResizable(false);
		stage.show();
	}

	/**
	 * Affiche la fen�tre de param�tres pour cr�er un object sph�rique.
	 * 
	 * @param arg0 - L'argument du ActionEvent, dans ce cas c'est le bouton Sph�re.
	 * @throws IOException
	 */
	@FXML
	protected void showFenetreCone(ActionEvent arg0) throws IOException {
		URL url = getClass().getResource("FenetreCone.fxml");
		FXMLLoader fxmlLoader = new FXMLLoader(url);

		BorderPane root = fxmlLoader.load();
		Scene scene = new Scene(root);

		stage.setScene(scene);
		stage.setTitle("Param�tres du C�ne");
		stage.setResizable(false);
		stage.show();
	}

	/**
	 * Affiche la fen�tre de param�tres pour cr�er un object sph�rique.
	 * 
	 * @param arg0 - L'argument du ActionEvent, dans ce cas c'est le bouton Sph�re.
	 * @throws IOException
	 */
	@FXML
	protected void showFenetreCylindre(ActionEvent arg0) throws IOException {
		URL url = getClass().getResource("FenetreCylindre.fxml");
		FXMLLoader fxmlLoader = new FXMLLoader(url);

		BorderPane root = fxmlLoader.load();
		Scene scene = new Scene(root);

		stage.setScene(scene);
		stage.setTitle("Param�tres dy Cylindre");
		stage.setResizable(false);
		stage.show();
	}

	/**
	 * Cr�e un solide selon les param�tres s�l�ctionn�s dans la fen�tre de
	 * param�tres et les ajoute � une liste.
	 */
	@FXML
	protected void creeSolide() {
		Pane pane = (Pane) Main.root.getChildren().get(0);

		// Ne fait rien s'il y a des param�tres fautifs
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
		
		if (lstView == null) {
			lstView = (ListView<String>) ((VBox) Main.root.getRight()).getChildren().get(1);
			lstView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			lstView.setContextMenu(creeContextMenu());
			System.out.println("d0one");
		}
		
		lstView.setItems(Main.listeNoms);

		if (txtTitreParam.getText().toLowerCase().contains("cube")) {
			if (txtHau.getText().toCharArray().length == 0) return;
			for (char c : txtHau.getText().toCharArray()) {
				if (!Character.isDigit(c) && c != '.') return;
			}

			Cube cube = new Cube(Double.valueOf(txtLon.getText()), Double.valueOf(txtLar.getText()),
					Double.valueOf(txtHau.getText()));

			Main.listeSolides.add(cube);
			Main.listeNoms.add(txtNom.getText());
			Main.mapSolideNom.put(txtNom.getText(), cube);

			Solide.creeForme(cube.getCube(), pane, Double.valueOf(txtX.getText()) + 300,
					Double.valueOf(txtY.getText()) + 300, Double.valueOf(txtZ.getText()));
		}
		else if (txtTitreParam.getText().toLowerCase().contains("sph�re")) {
			Sphere sphere = new Sphere(Double.valueOf(txtLon.getText()), Double.valueOf(txtLar.getText()));

			Main.listeSolides.add(sphere);
			Main.listeNoms.add(txtNom.getText());
			Main.mapSolideNom.put(txtNom.getText(), sphere);

			Solide.creeForme(sphere.getSphere(), pane, Double.valueOf(txtX.getText()) + 300,
					Double.valueOf(txtY.getText()) + 300, Double.valueOf(txtZ.getText()));
		}
		else if (txtTitreParam.getText().toLowerCase().contains("c�ne")) {
			Cone cone = new Cone(Double.valueOf(txtLon.getText()), Double.valueOf(txtLar.getText()));

			Main.listeSolides.add(cone);
			Main.listeNoms.add(txtNom.getText());
			Main.mapSolideNom.put(txtNom.getText(), cone);

			Solide.creeForme(cone.getCone(), pane, Double.valueOf(txtX.getText()) + 300,
					Double.valueOf(txtY.getText()) + 300, Double.valueOf(txtZ.getText()));
		}
		else if (txtTitreParam.getText().toLowerCase().contains("cylindre")) {
			Cylindre cylindre = new Cylindre(Double.valueOf(txtLon.getText()), Double.valueOf(txtLar.getText()));

			Main.listeSolides.add(cylindre);
			Main.listeNoms.add(txtNom.getText());
			Main.mapSolideNom.put(txtNom.getText(), cylindre);

			Solide.creeForme(cylindre.getCylindre(), pane, Double.valueOf(txtX.getText()) + 300,
					Double.valueOf(txtY.getText()) + 300, Double.valueOf(txtZ.getText()));
		}

//============DEBUG================
		System.out.println(txtLon.getText() + " " + txtLar.getText() + " " + txtHau.getText());
		System.out.println(Main.listeSolides.size());
//=================================
	}

	/**
	 * Le Event Handler qui va g�r�rer les touches de clavier pour effectuer la
	 * rotation des solides dans l'�diteur.
	 * 
	 * @return Retourne le KeyEvent de la rotation
	 */
	@FXML
	public EventHandler<KeyEvent> rotationKey() {
		return new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent arg0) {
				double posNeg = arg0.isShiftDown() ? -15 : 15;
				Pane pane = (Pane) Main.root.getChildren().get(0);

				ArrayList<Vector3d> tousPoints = new ArrayList<Vector3d>();
				for (Solide s : Main.listeSolides) {
					tousPoints.addAll(s.getSolide());
					System.out.println(tousPoints.size());
				}

				if (arg0.getCode() == KeyCode.X) {
					Solide.rotateSolide(tousPoints, posNeg, 0, 0, centreX, centreY, centreZ);
					System.out.println("x");

				}
				else if (arg0.getCode() == KeyCode.C) {
					Solide.rotateSolide(tousPoints, 0, posNeg, 0, centreX, centreY, centreZ);
					System.out.println("y");
				}
				else if (arg0.getCode() == KeyCode.Z) {
					Solide.rotateSolide(tousPoints, 0, 0, posNeg, centreX, centreY, centreZ);
					System.out.println("z");
				}
				else {
					return;
				}
				pane.getChildren().removeAll(pane.getChildren());
				Solide.creeForme(tousPoints, pane, 0, 0, 0);
			}
		};
	}
	
	protected ContextMenu creeContextMenu() {
		ctxtMenuListObj.getItems().addAll(ctxtMenuItemEdit, ctxtMenuItemDelete);
		
		ctxtMenuItemDelete.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				for(String s : lstView.getSelectionModel().getSelectedItems()) {
					for(Vector3d v : Main.mapSolideNom.get(s).getSolide()) {
						//
					}
					((Pane) Main.root.getCenter()).getChildren().remove(lstView.getSelectionModel().getSelectedIndex());
					Main.listeSolides.remove(Main.mapSolideNom.get(s));
					Main.listeNoms.remove(s);
					Main.mapSolideNom.remove(s);
					lstView.setItems(Main.listeNoms);
					
					
				}
				
			}
			
		});
		
		return ctxtMenuListObj;
	}

	/**
	 * Termine le programme.
	 */
	@FXML
	protected void quitter() {
		System.exit(0);
	}
}