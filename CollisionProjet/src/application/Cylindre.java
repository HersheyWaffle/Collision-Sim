package application;

import java.util.ArrayList;

import javax.vecmath.Vector3d;

/**
 * Objet Cylindrique.
 * 
 * @version 1.1.0 2023-02-14
 * @author Omar Ghazaly, Abel-Jimmy Oyono-Montoki
 */
public class Cylindre extends Solide {

//=========================VARIABLES=========================

	final double ESPACE_ENTRE_POINTS = 8;

	private double rayon; // Rayon du cercle
	private double hauteur; // Hateur du cylindre
	private double dThetaCercle; // Angle en Rad entre chaque point dans le cercle
	private ArrayList<Point> carre = new ArrayList<Point>(); // Liste des vecteurs 3D de chaque point dans le
																// cercle
	private ArrayList<Point> cylindre = new ArrayList<Point>(); // Liste des vecteurs 3D de chaque point dans le
																// cylindre

//=========================CONSTRUCTEUR=======================

	/**
	 * Objet cylindrique.
	 * 
	 * @param rayon   - Le rayon du cylindre.
	 * @param hauteur - La hauteur du cylindre.
	 * @param fontSize - taille des caract�res
	 */
	public Cylindre(double rayon, double hauteur, final int fontSize) {
		super.setFontSize(fontSize);
		this.rayon = rayon;
		this.hauteur = hauteur;
		dThetaCercle = 2 * Math.PI / (2 * rayon / ESPACE_ENTRE_POINTS);

		virtualCentre = new Vector3d(0, 0, 0);
		setCarre();
		Solide.setFormeRotation(dThetaCercle, carre, cylindre);
		Solide.enleveDoublons(getCylindre());

		quadrant();
		clean();
		render(super.getFontSize());
		setSolide(cylindre);
	}

//=========================METHODES=========================

	public ArrayList<Point> getCylindre() {
		return cylindre;
	}

	public ArrayList<Point> getCarre() {
		return carre;
	}

	public double getRayon() {
		return rayon;
	}

	public double getHauteur() {
		return hauteur;
	}

	public void setLongueur(double longueur) {
		rayon = longueur;
	}

	public void setHauteur(double hauteur) {
		this.hauteur = hauteur;
	}

	public void setCarre() {
		// Cree le plan
		for (double y = -hauteur; y <= hauteur; y += ESPACE_ENTRE_POINTS) {
			for (double z = -rayon; z <= 0; z += ESPACE_ENTRE_POINTS) {
				if (y >= hauteur || y <= -hauteur || z >= rayon || z <= -rayon) {
					Vector3d temp = new Vector3d(0, 0, 0);
					Vector3d u = new Vector3d(0, y, z);
					temp = getPerpendicular(u); // SUS C'est ça la norme?
					carre.add(new Point(u, temp));
				}
			}
		}
	}
}
