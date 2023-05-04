package application;

import java.util.ArrayList;

import javax.vecmath.Vector3d;

/**
 * Objet Conique.
 * 
 * @version 1.0.0 2023-02-14
 * @author Omar Ghazaly, Abel-Jimmy Oyono-Montoki
 */
public class Cone extends Solide {

//=========================VARIABLES=========================

	final double ESPACE_ENTRE_POINTS = 8;

	private double rayon; // Rayon du cercle
	private double hauteur; // Hateur du cylindre
	private double dThetaCercle; // Angle en Rad entre chaque point dans le cercle
	private ArrayList<Point> triangle = new ArrayList<Point>(); // Liste des vecteurs 3D de chaque point dans le
																// cercle
	private ArrayList<Point> cone = new ArrayList<Point>(); // Liste des vecteurs 3D de chaque point dans le
															// cylindre

//=========================CONSTRUCTEUR=======================	

	/**
	 * Objet cylindrique.
	 * 
	 * @param rayon    - Le rayon du cylindre.
	 * @param hauteur  - La hauteur du cylindre.
	 * @param fontSize - taille des caract�res
	 */
	public Cone(double rayon, double hauteur, final int fontSize) {
		super.setFontSize(fontSize);
		this.rayon = rayon;
		this.hauteur = hauteur;
		dThetaCercle = 2 * Math.PI / (2 * rayon / ESPACE_ENTRE_POINTS);

		virtualCentre = new Vector3d(0, 0, 0);
		setTriangle();
		Solide.setFormeRotation(dThetaCercle, triangle, cone);
		Solide.enleveDoublons(getCone());

		Vector3d util = new Vector3d(rayon, 0, hauteur);
		rayonDeCollision = util.length();

		quadrant();
		clean();
		render(super.getFontSize());
		setSolide(cone);
		inertie(new Vector3d(1, 0, 0));
	}

//=========================METHODES=========================	

	public ArrayList<Point> getCone() {
		return cone;
	}

	public ArrayList<Point> getTriangle() {
		return triangle;
	}

	public double getNbrPointsRayon() {
		return rayon;
	}

	public double getNbrPointsHauteur() {
		return hauteur;
	}

	public void setLongueur(double longueur) {
		rayon = longueur;
	}

	public void setHauteur(double hauteur) {
		this.hauteur = hauteur;
	}

	public void setTriangle() {
		// Cree le plan
		double z = hauteur;

		for (double y = 0; y <= rayon; y += ESPACE_ENTRE_POINTS, z -= ESPACE_ENTRE_POINTS * hauteur / rayon) {
			Vector3d temp = new Vector3d(0, 0, 0);

			Vector3d u = new Vector3d(0, 0, y);
			temp = getPerpendicular(u); // SUS C'est ça la norme?
			triangle.add(new Point(u, temp));

			Vector3d v = new Vector3d(0, z, y);
			temp = getPerpendicular(v); // SUS C'est ça la norme?
			triangle.add(new Point(v, temp));
		}
	}
}
