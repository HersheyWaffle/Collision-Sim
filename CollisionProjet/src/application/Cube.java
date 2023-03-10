package application;

import java.util.ArrayList;

import javax.vecmath.Vector3d;

/**
 * Objet Cubique.
 * 
 * @version 1.1.0 2023-02-08
 * @author Omar Ghazaly, Abel-Jimmy Oyono-Montoki
 */
public class Cube {
	final double ESPACE_ENTRE_POINTS = 8;
	
	private double nbrPointsLongueur;									//nombre de points sur la longueur
	private double nbrPointsLargeur;									//nombre de points sur la largeur
	private double nbrPointsHauteur;									//nombre de points sur la hauteur
	private ArrayList<Vector3d> carre = new ArrayList<Vector3d>();		//Liste des vecteurs 3D de chaque point dans le carre
	private ArrayList<Vector3d> cube = new ArrayList<Vector3d>();		//Liste des vecteurs 3D de chaque point dans le cube

	/**
	 * Objet cubique.
	 * 
	 * @param longueur - La longueur du cube.
	 * @param largeur - La largeur du cube.
	 * @param hauteur - La hauteur du cube.
	 */
	public Cube(double longueur, double largeur, double hauteur) {
		nbrPointsLongueur = longueur;
		nbrPointsLargeur = largeur;
		nbrPointsHauteur = hauteur;
		
		setCarre();
		setCube();
		Solide.enleveDoublons(getCube());
	}
	
	public ArrayList<Vector3d> getCube() {
		return cube;
	}
	
	public ArrayList<Vector3d> getCarre() {
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

	public void setLongueur(double longueur) {					//TODO Fonction pour changer les dimensions du cube
		nbrPointsLongueur = longueur;
	}
	
	public void setLargeur(double largeur) {
		nbrPointsLargeur = largeur;
	}
	
	public void setHauteur(double hauteur) {
		nbrPointsHauteur = hauteur;
	}
	
	public void setCarre() {
		//Cree le plan
		for(double y = -nbrPointsLargeur/2; y <= nbrPointsLargeur/2; y += ESPACE_ENTRE_POINTS) {
			for(double z = -nbrPointsHauteur/2; z <= nbrPointsHauteur/2; z += ESPACE_ENTRE_POINTS) {
				carre.add(new Vector3d(nbrPointsLongueur/2, y, z));
			}
		}
	}
	
	
	public void setCube( ){
		for(double x = nbrPointsLongueur/2; x >= -nbrPointsLongueur/2; x -= ESPACE_ENTRE_POINTS) {
			ArrayList<Vector3d> carreSuivant = new ArrayList<Vector3d>();
			
			for(Vector3d v : carre) {
				Vector3d u = new Vector3d(x, v.y, v.z);
				if(v.y >= nbrPointsLargeur/2 - ESPACE_ENTRE_POINTS || v.z >= nbrPointsHauteur/2 - ESPACE_ENTRE_POINTS || v.y <= -nbrPointsLargeur/2 + ESPACE_ENTRE_POINTS || v.z <= -nbrPointsHauteur/2 + ESPACE_ENTRE_POINTS || x <= -nbrPointsLongueur/2 + ESPACE_ENTRE_POINTS || x >= nbrPointsLongueur/2 - ESPACE_ENTRE_POINTS) {
					carreSuivant.add(u);
				}
			}
			cube.addAll(carreSuivant);
		}
	}
}
