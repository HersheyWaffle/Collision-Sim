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
	final int ESPACE_ENTRE_POINTS = 5;		//TODO Standariser, ne devrait pas etre une constante, doit etre dynamique selon les mesures
	
	
	private int nbrPointsLongueur;			//nombre de points sur la longueur
	private int nbrPointsLargeur;			//nombre de points sur la largeur
	private int nbrPointsHauteur;			//nombre de points sur la hauteur
	
	ArrayList<Vector3d> carre = new ArrayList<Vector3d>();							//Liste des vecteurs 3D de chaque point dans le carre
	ArrayList<Vector3d> cote = new ArrayList<Vector3d>();							//Liste des vecteurs 3D de chaque point dans le perimetre du carre
	ArrayList<ArrayList<Vector3d>> cube = new ArrayList<ArrayList<Vector3d>>();		//Liste des listes des vecteurs 3D de chaque point dans chaque carre
	Matrix3d rotation = new Matrix3d();												//Matrice 3D servant a effectuer la rotation des points et des carres

	public Cube(int longueur, int largeur, int hauteur) {
		nbrPointsLongueur = longueur/ESPACE_ENTRE_POINTS;
		nbrPointsLargeur = largeur/ESPACE_ENTRE_POINTS;
		nbrPointsHauteur = hauteur/ESPACE_ENTRE_POINTS;
	}
	
	public void setLongueur(int longueur) {					//TODO Fonction pour changer les dimensions du cube
		nbrPointsLongueur = longueur/ESPACE_ENTRE_POINTS;
	}
	
	public void setLargeur(int largeur) {
		nbrPointsLargeur = largeur/ESPACE_ENTRE_POINTS;
	}
	
	public void setHauteur(int hauteur) {
		nbrPointsHauteur = hauteur/ESPACE_ENTRE_POINTS;
	}
	
	public void carre() {
		//Cree le plan
		for(int y = -nbrPointsLargeur/2; y <= nbrPointsLargeur/2; y += ESPACE_ENTRE_POINTS) {
			for(int z = -nbrPointsHauteur/2; z <= nbrPointsHauteur/2; z += ESPACE_ENTRE_POINTS) {
				carre.add(new Vector3d(nbrPointsLongueur/2, y, z));
			}
		}
	}
	
	
	public void cube( ){															//FIXME	Ne semble pas fonctionner correctement

//		cube.add(carre);
		
		ArrayList<Vector3d> perimetre = new ArrayList<Vector3d>();
		ArrayList<Vector3d> coteOppose = new ArrayList<Vector3d>();
		ArrayList<Vector3d> carreDernier = new ArrayList<Vector3d>();
		Vector3d vRot = new Vector3d();
		
		for(int x = nbrPointsLongueur/2; x >= -nbrPointsLongueur/2; x -= ESPACE_ENTRE_POINTS) {
			ArrayList<Vector3d> carreSuivant = new ArrayList<Vector3d>();
			
			for(Vector3d v : carre) {
				Vector3d u = new Vector3d(x, v.y, v.z);
				if( x == nbrPointsLongueur/2  ||v.y == nbrPointsLargeur/2  || v.z == nbrPointsHauteur/2  || x == -nbrPointsLongueur/2  || v.y == -nbrPointsLargeur/2  || v.z == -nbrPointsHauteur/2 ) {
					carreSuivant.add(u);
				}
			}
			cube.add(carreSuivant);
		}
		
		for(Vector3d v : carre) {
			Vector3d u = new Vector3d(-v.x, v.y, v.z);
			carreDernier.add(u);
		}
		cube.add(carreDernier);
		
		//FIXME Ne marche pas??
//		rotation.rotZ(Math.PI);
//		for(Vector3d vec : carre) {
//			rotation.transform(vec, vRot);
//			coteOppose.add(vRot);
//		}
//		cube.add(coteOppose);
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