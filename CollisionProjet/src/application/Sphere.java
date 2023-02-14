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
public class Sphere {
	final double ESPACE_ENTRE_POINTS = 3;

	private double rayon;				//Rayon du cercle
	private double decalage;			//Decalage du cercle de son origine, pour former un tore
	private double dThetaCercle;		//Angle en Rad entre chaque point dans le cercle
	
	private ArrayList<Vector3d> cercle = new ArrayList<Vector3d>();		//Liste des vecteurs 3D de chaque point dans le cercle
	private	ArrayList<Vector3d> sphere = new ArrayList<Vector3d>();		//Liste des listes des vecteurs 3D de chaque point dans chaque cercle

	/**
	 * Objet spherique.
	 * 
	 * @param rayon - Le rayon de la sphere
	 * @param decalage - Le decalage du cercle. Va former un Tore.
	 */
	public Sphere(double rayon, double decalage) {
		this.rayon = rayon;
		this.decalage = decalage;
		dThetaCercle = 2 * Math.PI / (rayon / ESPACE_ENTRE_POINTS);
		
		setCercle();
		Solide.setFormeRotation(dThetaCercle, cercle, sphere);
		Solide.enleveDoublons(getSphere());
	}
	
	/**
	 * Retourne la liste de tous les points dans l'objet sphere.
	 * 
	 * @return retourne un ArrayList de vecteurs 3D chacun indiquant la position 3D du point.
	 */
	public ArrayList<Vector3d> getSphere() {
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
	 * @return retourne un double representant la valeur du decalage de l'objet sphere.
	 */
	public double getDecalage() {
		return decalage;
	}
	
	/**
	 * Retourne le cercle de points de l'objet Sphere.
	 * 
	 * @return retourne un ArrayList de vecteurs 3D chacun indiquant la position 3D du point.
	 */
	public ArrayList<Vector3d> getCercle() {
		return cercle;
	}

	/**
	 * Cree les vecteurs 3D indiquant la position de chaque point sur le cercle.
	 */
	public void setCercle() {
		Matrix3d rotation = new Matrix3d();													//Matrice 3D servant a effectuer la rotation des points et des cercles
		
		
		
		double theta = 0;
		do {
			rotation.rotZ(theta);
			Vector3d initial = new Vector3d(rayon, 0, 0);
			rotation.transform(initial);
			initial.add(new Vector3d(decalage, 0, 0));
			cercle.add(initial);
			
			theta += dThetaCercle;
		} while (theta < 2 * Math.PI);
	}

	/**
	 * Cree les cercles de vecteurs 3D de chaque point autour d'un axe, formant un cercle de points.
	 */
//	public void setSphere() {
//		Matrix3d rotation = new Matrix3d();													//Matrice 3D servant a effectuer la rotation des points et des cercles
//		Vector3d u = new Vector3d();
//		
//		double theta = 0;
//		do {
//			rotation.rotY(theta);
//			ArrayList<Vector3d> anneau = new ArrayList<Vector3d>();
//			for (Vector3d v : cercle) {
//				rotation.transform(v, u);
//				anneau.add((Vector3d) u.clone());
//			}
//			
//			sphere.addAll(anneau);
//
//			theta += dThetaSphere;
//		} while (theta < 2 * Math.PI);
//	}
}