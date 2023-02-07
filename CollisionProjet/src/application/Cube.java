package application;

import java.util.ArrayList;

import javax.vecmath.Matrix3d;
import javax.vecmath.Vector3d;

/**
 * Objet Cubique.
 * 
 * @version 1.0 2023-02-07
 * @author Omar Ghazaly, Abel-Jimmy Oyono-Montoki
 */
public class Cube extends Solide {
	final double ESPACE_ENTRE_POINTS = 5;		//TODO Standariser, ne devrait pas etre une constante, doit etre dynamique selon les mesures
	
	private double nbrPointsLongueur;			//nombre de points sur la longueur
	private double nbrPointsLargeur;			//nombre de points sur la largeur
	private double nbrPointsHauteur;			//nombre de points sur la hauteur
	
	ArrayList<Vector3d> carre = new ArrayList<Vector3d>();							//Liste des vecteurs 3D de chaque point dans le carre
	ArrayList<Vector3d> cote = new ArrayList<Vector3d>();							//Liste des vecteurs 3D de chaque point dans le perimetre du carre
	ArrayList<ArrayList<Vector3d>> cube = new ArrayList<ArrayList<Vector3d>>();		//Liste des listes des vecteurs 3D de chaque point dans chaque carre
	Matrix3d rotation = new Matrix3d();												//Matrice 3D servant a effectuer la rotation des points et des carres

	public Cube(double longueur, double largeur, double hauteur) {
		nbrPointsLongueur = longueur/ESPACE_ENTRE_POINTS;
		nbrPointsLargeur = largeur/ESPACE_ENTRE_POINTS;
		nbrPointsHauteur = hauteur/ESPACE_ENTRE_POINTS;
	}
	
	public void setLongueur(double longueur) {					//TODO Fonction pour changer les dimensions du cube
		nbrPointsLongueur = longueur/ESPACE_ENTRE_POINTS;
	}
	
	public void setLargeur(double largeur) {
		nbrPointsLargeur = largeur/ESPACE_ENTRE_POINTS;
	}
	
	public void setHauteur(double hauteur) {
		nbrPointsHauteur = hauteur/ESPACE_ENTRE_POINTS;
	}
	
	public void carre() {
		//Cree le plan
		for(double y = -nbrPointsLargeur/2; y <= nbrPointsLargeur/2; y += ESPACE_ENTRE_POINTS) {
			for(double z = -nbrPointsHauteur/2; z <= nbrPointsHauteur/2; z += ESPACE_ENTRE_POINTS) {
				if(y == Math.abs(nbrPointsLargeur/2) || z == Math.abs(nbrPointsHauteur/2)) {
					cote.add(new Vector3d(nbrPointsLongueur, y, z));
				}
				carre.add(new Vector3d(nbrPointsLongueur, y, z));
			}
		}
	}
	
	
	public void cube( ){															//FIXME	Ne semble pas fonctionner correctement
		ArrayList<Vector3d> perimetre = new ArrayList<Vector3d>();
		ArrayList<Vector3d> coteOppose = new ArrayList<Vector3d>();
		Vector3d vRot = new Vector3d();
		
		for(double x = -nbrPointsLongueur/2; x <= nbrPointsLongueur/2; x += ESPACE_ENTRE_POINTS) {
			for(Vector3d v : cote) {
				Vector3d u = new Vector3d(x, v.y, v.z);
				perimetre.add(u);
			}
			cube.add(perimetre);
		}
		
		//FIXME Ne marche pas??
		rotation.rotZ(Math.PI/3);
		for(Vector3d vec : carre) {
			rotation.transform(vec, vRot);
			coteOppose.add(vRot);
		}
		cube.add(coteOppose);
	}
	
	
	/**
	 * Effectue une rotation sur le cube. Les parametres sont en DEGRES
	 * 
	 * @param rotX - Rotation sur l'axe des X
	 * @param rotY - Rotation sur l'axe des Y
	 * @param rotZ - Rotation sur l'axe des Z
	 */
	public void rotateCube(double rotX, double rotY, double rotZ) {
		for(ArrayList<Vector3d> list : cube) {
			for(Vector3d v : list) {
				if(rotX != 0) {
					rotation.rotX(rotX*Math.PI/180);
					rotation.transform(v);
				}
				if(rotY != 0) {
					rotation.rotY(rotY*Math.PI/180);
					rotation.transform(v);
				}
				if(rotZ != 0) {
					rotation.rotZ(rotZ*Math.PI/180);
					rotation.transform(v);
				}
			}
		}
	}
}
