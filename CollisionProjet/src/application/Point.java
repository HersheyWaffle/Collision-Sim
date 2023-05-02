package application;

import javax.vecmath.Vector3d;

/**
 * Classe Vecteur avec plus d'information que Vector3D ne fournit pas. Utilisé
 * entre autre pour calculer la norme des points dans les solides pour pouvoir
 * effectuer le calcul d'ombre de la lumière.
 * 
 * @version 1.0.1 2023-03-22
 * @author Abel-Jimmy Oyono-Montoki
 */
public class Point {

//=========================VARIABLES=========================	

	private Vector3d coordonnee; // Vector3D des coordonnées du point
	private Vector3d norme; // Vector3D de la norme du point
	private Vector3d shadow; // Vector3D résultant de l'ombre projet�e par un point
	private String eclairage; // Caractère représentant le niveau de luminosit� d'un point ".,-~:;=!*#$@"
	private String quadrant; // String représentant la région dans laquelle se trouve le point. Définit par
								// une séquence de 0 et de 1, représentant les coordonnées du quadrant
	private boolean isRendered; // Détermine si le point sera rendered sur l'écran

//=========================CONSTRUCTEUR=======================		

	/**
	 * Objet Point.
	 * 
	 * @param coordonnee - Le Vector3D des coordonnées du point.
	 * @param norme      - Le Vector3D de la norme du point.
	 */
	public Point(Vector3d coordonnee, Vector3d norme) {
		this.coordonnee = new Vector3d(); // Initialisation des variables pour qu'elles ne lancent pas un
											// NullPointerException
		this.norme = new Vector3d();
		shadow = new Vector3d();
		quadrant = new String();
		isRendered = true;

		norme.normalize();
		this.coordonnee = coordonnee;
		this.norme = norme;
	}

//=========================METHODES=========================	

	/**
	 * Retourne le Vector3D qui représente les coordonnées du point.
	 * 
	 * @return retourne un vecteurs 3D indiquant la position 3D du point.
	 */
	public Vector3d getCoordonnee() {
		return coordonnee;
	}

	/**
	 * D�finit la valeur du Vector3D des coordonnées du point.
	 * 
	 * @param coordonnee - Le vecteurs 3D indiquant la position 3D du point.
	 */
	public void setCoordonnee(Vector3d coordonnee) {
		this.coordonnee = coordonnee;
	}

	/**
	 * Retourne le Vector3D qui représente la Norme.
	 * 
	 * @return retourne un vecteurs 3D indiquant la position 3D de la norme du
	 *         point.
	 */
	public Vector3d getNorme() {
		return norme;
	}

	/**
	 * D�finit la valeur du Vector3Dqui représente la Norme du point.
	 * 
	 * @param norme - Le vecteurs 3D indiquant la position 3D de la norme du point.
	 */
	public void setNorme(Vector3d norme) {
		this.norme = norme;
	}

	/**
	 * Retourne le Vector3D qui représente le Shadow.
	 * 
	 * @return retourne un vecteurs 3D indiquant la position 3D du point Shadow.
	 */
	public Vector3d getShadow() {
		return shadow;
	}

	/**
	 * Définit la valeur du Vector3D du point Shadow.
	 * 
	 * @param shadow - Le vecteur 3d du point Shadow.
	 */
	public void setShadow(Vector3d shadow) {
		this.shadow = shadow;
	}

	/**
	 * Retourne le caractère repr�sentant le degré de luminosité du point. Les 12
	 * niveaux sont ".,-~:;=!*#$@"
	 * 
	 * @return retourne un String indiquant le degré de luminosité du point.
	 */
	public String getEclairage() {
		return eclairage;
	}

	/**
	 * D�finit la valeur de l'éclairage.
	 * 
	 * @param eclairage - Le String représentant le degré d'éclairage du point.
	 *                  Valeurs possibles: ".,-~:;=!*#$@"
	 */
	public void setEclairage(String eclairage) {
		this.eclairage = eclairage;
	}

	/**
	 * Définit la valeur du quadrant.
	 * 
	 * @param s - Le String représentant le nouveau quadrant. Définit par une
	 *          séquence de 0 et de 1, représentant les coordonnées du quadrant
	 */
	public void addQuadrant(String s) {
		quadrant += s;
	}

	/**
	 * Efface le quadrant, le redéfinissant en tant qu'espace vide.
	 */
	public void clearQuadrant() {
		quadrant = new String();
	}

	/**
	 * Retourne le quadrant dans lequel se trouve le point.
	 * 
	 * @return retourne un String indiquant le quadrant du point.
	 */
	public String getQuadrant() {
		return quadrant;
	}

	/**
	 * Vérifie si le point doit être rendered.
	 * 
	 * @return retourne un Boolean indiquant si le point sera rendered ou non.
	 */
	public boolean isRendered() {
		return isRendered;
	}

	/**
	 * D�finit si le point doit être rendered.
	 * 
	 * @param isRendered - Le Boolean indiquant si le point sera rendered ou non.
	 */
	public void setRendered(boolean isRendered) {
		this.isRendered = isRendered;
	}

	public Point clone() {
		return new Point((Vector3d) coordonnee.clone(), (Vector3d) norme.clone());
	}

	public String toString() {
		return String.format("X: %f Y: %f Z:%f\nEclairage:", coordonnee.x, coordonnee.y, coordonnee.z, eclairage);
	}
}