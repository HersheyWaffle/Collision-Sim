package application;

import java.util.ArrayList;

import javax.vecmath.Vector3d;

/**
 * Objet Cylindrique.
 * 
 * @version 1.0.0 2023-02-08
 * @author Omar Ghazaly, Abel-Jimmy Oyono-Montoki
 */
public class Cylindre {
	final double ESPACE_ENTRE_POINTS = 8;
	
	private double rayon;				//Rayon du cercle
	private double hauteur;				//Hateur du cylindre
	private double dThetaCercle;		//Angle en Rad entre chaque point dans le cercle
	private ArrayList<Vector3d> carre = new ArrayList<Vector3d>();			//Liste des vecteurs 3D de chaque point dans le cercle
	private ArrayList<Vector3d> cylindre = new ArrayList<Vector3d>();		//Liste des vecteurs 3D de chaque point dans le cylindre

	/**
	 * Objet cylindrique.
	 * 
	 * @param rayon - Le rayon du cylindre.
	 * @param hauteur - La hauteur du cylindre.
	 */
	public Cylindre(double rayon, double hauteur) {
		this.rayon = rayon;
		this.hauteur = hauteur;
		dThetaCercle = 2 * Math.PI / (2 * rayon / ESPACE_ENTRE_POINTS);
		
		setCarre();
		Solide.setFormeRotation(dThetaCercle, carre, cylindre);
		Solide.enleveDoublons(getCylindre());
	}
	
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
		//Cree le plan
		for(double y = -hauteur; y <= hauteur; y += ESPACE_ENTRE_POINTS) {
			for(double z = -rayon; z <= 0; z += ESPACE_ENTRE_POINTS) {
				if(y >= hauteur || y <= -hauteur || z >= rayon || z <= -rayon) carre.add(new Vector3d(0, y, z));
			}
		}
	}
	
	
//	public void setCylindre( ){
//		Matrix3d rotation = new Matrix3d();		//Matrice 3D servant a effectuer la rotation des points et des cercles
//		Vector3d u = new Vector3d();
//		
//		double theta = 0;
//		do {
//			rotation.rotY(theta);
//			ArrayList<Vector3d> anneau = new ArrayList<Vector3d>();
//			for (Vector3d v : carre) {
//				rotation.transform(v, u);
//				anneau.add((Vector3d) u.clone());
//			}
//			
//			cylindre.addAll(anneau);
//
//			theta += dThetaCercle;
//		} while (theta < 2 * Math.PI);
//	}
}
