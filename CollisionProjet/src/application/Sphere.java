package application;

import java.util.ArrayList;

import javax.vecmath.Vector3d;
import javax.vecmath.Matrix3d;

/**
 * Objet Spherique.
 * 
 * @version 1.0.2 2023-02-14
 * @author Omar Ghazaly, Abel-Jimmy Oyono-Montoki
 */
public class Sphere extends Solide {

//=========================VARIABLES=========================	

	final double ESPACE_ENTRE_POINTS = 3;

	private double rayon; // Rayon du cercle
	private double decalage; // Decalage du cercle de son origine, pour former un tore
	private double dThetaCercle; // Angle en Rad entre chaque point dans le cercle

	private ArrayList<Point> cercle = new ArrayList<Point>(); // Liste des vecteurs 3D de chaque point dans le
																	// cercle
	private ArrayList<Point> sphere = new ArrayList<Point>(); // Liste des listes des vecteurs 3D de chaque point
																	// dans chaque cercle

//=========================CONSTRUCTEUR=======================	

	/**
	 * Objet spherique.
	 * 
	 * @param rayon    - Le rayon de la sphere
	 * @param decalage - Le decalage du cercle. Va former un Tore.
	 * @param FONT_SIZE - taille des caractères
	 */
	public Sphere(double rayon, double decalage, final int FONT_SIZE) {
		this.rayon = rayon;
		this.decalage = decalage;
		dThetaCercle = 2 * Math.PI / (rayon / ESPACE_ENTRE_POINTS);

		virtual_centre = new Vector3d(0, 0, 0);
		setCercle();
		Solide.setFormeRotation(dThetaCercle, cercle, sphere);
		Solide.enleveDoublons(getSphere());

		quadrant();
		clean();
		render(FONT_SIZE);
		setSolide(sphere);
	}

//=========================METHODES=========================	

	/**
	 * Retourne la liste de tous les points dans l'objet sphere.
	 * 
	 * @return retourne un ArrayList de vecteurs 3D chacun indiquant la position 3D
	 *         du point.
	 */
	public ArrayList<Point> getSphere() {
		return sphere;
	}

	/**
	 * Retourne le rayon de l'objet Sphere.
	 * 
	 * @return retourne un double representant la valeur du rayon de l'objet sphere.
	 */
	public double getRayon() {
		return rayon;
	}

	/**
	 * Retourne le decalage de l'objet Sphere.
	 * 
	 * @return retourne un double representant la valeur du decalage de l'objet
	 *         sphere.
	 */
	public double getDecalage() {
		return decalage;
	}

	/**
	 * Retourne le cercle de points de l'objet Sphere.
	 * 
	 * @return retourne un ArrayList de vecteurs 3D chacun indiquant la position 3D
	 *         du point.
	 */
	public ArrayList<Point> getCercle() {
		return cercle;
	}

	/**
	 * Cree les vecteurs 3D indiquant la position de chaque point sur le cercle.
	 */
	public void setCercle() {
		Matrix3d rotation = new Matrix3d(); // Matrice 3D servant a effectuer la rotation des points et des cercles

		double theta = 0;
		do {
			rotation.rotZ(theta);
			Vector3d initial = new Vector3d(rayon, 0, 0);
			rotation.transform(initial);
			Vector3d v = new Vector3d(decalage, 0, 0);
			initial.add(v);
			//v.normalize(initial);
			cercle.add(new Point(initial, (Vector3d) initial.clone()));

			theta += dThetaCercle;
		} while (theta < 2 * Math.PI);
	}
}