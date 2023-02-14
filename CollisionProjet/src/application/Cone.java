package application;

import java.util.ArrayList;

import javax.vecmath.Vector3d;

/**
 * Objet Conique.
 * 
 * @version 1.0.0 2023-02-14
 * @author Omar Ghazaly, Abel-Jimmy Oyono-Montoki
 */
public class Cone {
	final double ESPACE_ENTRE_POINTS = 8;
	
	private double rayon;				//Rayon du cercle
	private double hauteur;				//Hateur du cylindre
	private double dThetaCercle;		//Angle en Rad entre chaque point dans le cercle
	private ArrayList<Vector3d> triangle = new ArrayList<Vector3d>();			//Liste des vecteurs 3D de chaque point dans le cercle
	private ArrayList<Vector3d> cone = new ArrayList<Vector3d>();		//Liste des vecteurs 3D de chaque point dans le cylindre

	/**
	 * Objet cylindrique.
	 * 
	 * @param rayon - Le rayon du cylindre.
	 * @param hauteur - La hauteur du cylindre.
	 */
	public Cone(double rayon, double hauteur) {
		this.rayon = rayon;
		this.hauteur = hauteur;
		dThetaCercle = 2 * Math.PI / (2 * rayon / ESPACE_ENTRE_POINTS);
		
		setTriangle();
		Solide.setFormeRotation(dThetaCercle, triangle, cone);
		Solide.enleveDoublons(getCone());
	}
	
	public ArrayList<Vector3d> getCone() {
		return cone;
	}
	
	public ArrayList<Vector3d> getTriangle() {
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
		//Cree le plan
		double z = hauteur;
		
		for(double y = 0; y <= rayon; y += ESPACE_ENTRE_POINTS, z -= ESPACE_ENTRE_POINTS*hauteur/rayon) {
			triangle.add(new Vector3d(0, 0, y));
			triangle.add(new Vector3d(0, z, y));
		}
	}
	
	
//	public void setCone( ){
//		Matrix3d rotation = new Matrix3d();		//Matrice 3D servant a effectuer la rotation des points et des cercles
//		Vector3d u = new Vector3d();
//		
//		double theta = 0;
//		do {
//			rotation.rotY(theta);
//			ArrayList<Vector3d> anneau = new ArrayList<Vector3d>();
//			for (Vector3d v : triangle) {
//				rotation.transform(v, u);
//				anneau.add((Vector3d) u.clone());
//			}
//			
//			cone.addAll(anneau);
//
//			theta += dThetaCercle;
//		} while (theta < 2 * Math.PI);
//	}
}
