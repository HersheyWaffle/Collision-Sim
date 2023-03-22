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
	private ArrayList<Vector3d> carre = new ArrayList<Vector3d>(); // Liste des vecteurs 3D de chaque point dans le
																	// cercle
	private ArrayList<Vector3d> cylindre = new ArrayList<Vector3d>(); // Liste des vecteurs 3D de chaque point dans le
																		// cylindre

//=========================CONSTRUCTEUR=======================

	/**
	 * Objet cylindrique.
	 * 
	 * @param rayon   - Le rayon du cylindre.
	 * @param hauteur - La hauteur du cylindre.
	 */
	public Cylindre(double rayon, double hauteur) {
		this.rayon = rayon;
		this.hauteur = hauteur;
		dThetaCercle = 2 * Math.PI / (2 * rayon / ESPACE_ENTRE_POINTS);

		setCarre();
		Solide.setFormeRotation(dThetaCercle, carre, cylindre);
		Solide.enleveDoublons(getCylindre());

		setSolide(cylindre);
	}

//=========================METHODES=========================

	public ArrayList<Vector3d> getCylindre() {
		return cylindre;
	}

	public ArrayList<Vector3d> getCarre() {
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
				if (y >= hauteur || y <= -hauteur || z >= rayon || z <= -rayon) carre.add(new Vector3d(0, y, z));
			}
		}
	}
}
