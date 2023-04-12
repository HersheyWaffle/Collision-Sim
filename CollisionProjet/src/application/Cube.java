package application;

import java.util.ArrayList;

import javax.vecmath.Vector3d;

/**
 * Objet Cubique.
 * 
 * @version 1.1.0 2023-02-08
 * @author Omar Ghazaly, Abel-Jimmy Oyono-Montoki
 */
public class Cube extends Solide {

//=========================VARIABLES=========================

	final double ESPACE_ENTRE_POINTS = 15;

	private double nbrPointsLongueur; // nombre de points sur la longueur
	private double nbrPointsLargeur; // nombre de points sur la largeur
	private double nbrPointsHauteur; // nombre de points sur la hauteur
	private ArrayList<Point> carre = new ArrayList<Point>(); // Liste des vecteurs 3D de chaque point dans le
																// carre
	private ArrayList<Point> cube = new ArrayList<Point>(); // Liste des vecteurs 3D de chaque point dans le cube

//=========================CONSTRUCTEUR=======================	

	/**
	 * Objet cubique.
	 * 
	 * @param longueur  - La longueur du cube.
	 * @param largeur   - La largeur du cube.
	 * @param hauteur   - La hauteur du cube.
	 * @param FONT_SIZE - taille des caractères
	 */
	public Cube(double longueur, double largeur, double hauteur, final int FONT_SIZE) {
		nbrPointsLongueur = longueur;
		nbrPointsLargeur = largeur;
		nbrPointsHauteur = hauteur;

		virtual_centre = new Vector3d(0, 0, 0);
		setCarre();
		setCube();
		Solide.enleveDoublons(getCube());

		quadrant();
		clean();
		render(FONT_SIZE);
		setSolide(cube);
	}

//=========================METHODES=========================	

	public ArrayList<Point> getCube() {
		return cube;
	}

	public ArrayList<Point> getCarre() {
		return carre;
	}

	public double getNbrPointsLongueur() {
		return nbrPointsLongueur;
	}

	public double getNbrPointsLargeur() {
		return nbrPointsLargeur;
	}

	public double getNbrPointsHauteur() {
		return nbrPointsHauteur;
	}

	public void setLongueur(double longueur) { // TODO Fonction pour changer les dimensions du cube
		nbrPointsLongueur = longueur;
	}

	public void setLargeur(double largeur) {
		nbrPointsLargeur = largeur;
	}

	public void setHauteur(double hauteur) {
		nbrPointsHauteur = hauteur;
	}

	public void setCarre() {
		// Cree le plan
		for (double y = -nbrPointsLargeur; y <= nbrPointsLargeur; y += ESPACE_ENTRE_POINTS) {
			for (double z = -nbrPointsHauteur; z <= nbrPointsHauteur; z += ESPACE_ENTRE_POINTS) {
				Vector3d temp = new Vector3d(1, 0, 0);
				carre.add(new Point(new Vector3d(nbrPointsLongueur / 2, y, z), temp));
			}
		}
	}

	public void setCube() {
		for (double x = nbrPointsLongueur; x >= -nbrPointsLongueur; x -= ESPACE_ENTRE_POINTS) {
			for (Point v : carre) {
				if (v.getCoordonnee().y >= nbrPointsLargeur - ESPACE_ENTRE_POINTS) {
					Vector3d u = new Vector3d(x, v.getCoordonnee().y, v.getCoordonnee().z);
					Vector3d temp = new Vector3d(0, 1, 0);

					cube.add(new Point(u, temp));
				}
				else if (v.getCoordonnee().z >= nbrPointsHauteur - ESPACE_ENTRE_POINTS) {
					Vector3d u = new Vector3d(x, v.getCoordonnee().y, v.getCoordonnee().z);
					Vector3d temp = new Vector3d(0, 0, 1);

					cube.add(new Point(u, temp));
				}
				else if (v.getCoordonnee().y <= -nbrPointsLargeur + ESPACE_ENTRE_POINTS) {
					Vector3d u = new Vector3d(x, v.getCoordonnee().y, v.getCoordonnee().z);
					Vector3d temp = new Vector3d(0, -1, 0);

					cube.add(new Point(u, temp));
				}
				else if (v.getCoordonnee().z <= -nbrPointsHauteur + ESPACE_ENTRE_POINTS) {
					Vector3d u = new Vector3d(x, v.getCoordonnee().y, v.getCoordonnee().z);
					Vector3d temp = new Vector3d(0, 0, -1);

					cube.add(new Point(u, temp));
				}
				else if (x <= -nbrPointsLongueur + ESPACE_ENTRE_POINTS) {
					Vector3d u = new Vector3d(x, v.getCoordonnee().y, v.getCoordonnee().z);
					Vector3d temp = new Vector3d(-1, 0, 0);

					cube.add(new Point(u, temp));
				}
				else if (x >= nbrPointsLongueur - ESPACE_ENTRE_POINTS) {
					Vector3d u = new Vector3d(x, v.getCoordonnee().y, v.getCoordonnee().z);
					Vector3d temp = new Vector3d(1, 0, 0);

					cube.add(new Point(u, temp));
				}
			}
		}
	}
}
