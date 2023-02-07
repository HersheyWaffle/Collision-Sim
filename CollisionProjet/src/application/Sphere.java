package application;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.vecmath.Vector3d;
import javax.vecmath.Matrix3d;

/**
 * Objet Spherique.
 * 
 * @version 1.0.1 2023-02-07
 * @author Omar Ghazaly, Abel-Jimmy Oyono-Montoki
 */
public class Sphere extends Solide {
//	final double RAYON = 100;
//	final double NBR_CERCLE = 100;
//	final double NBR_POINT_PAR_CERCLE = 100;
//	final double DTETHA_CERCLE = 2 * Math.PI / NBR_POINT_PAR_CERCLE;
//	final double DTETHA_SPHERE = 2 * Math.PI / NBR_CERCLE;

	private double rayon;				//Rayon du cercle
	private double dThetaCercle;		//Angle en Rad entre chaque point dans le cercle
	private double dThetaSphere;		//Angle en Rad entre chaque cercle dans la sphere
	
	ArrayList<Vector3d> cercle = new ArrayList<Vector3d>();								//Liste des vecteurs 3D de chaque point dans le cercle
	ArrayList<ArrayList<Vector3d>> sphere = new ArrayList<ArrayList<Vector3d>>();		//Liste des listes des vecteurs 3D de chaque point dans chaque cercle
	Matrix3d rotation = new Matrix3d();													//Matrice 3D servant a effectuer la rotation des points et des cercles

	public Sphere(double rayon, double nbrCercle, double nbrPointParCercle) {
		this.rayon = rayon;
		dThetaCercle = 2 * Math.PI / nbrPointParCercle;
		dThetaSphere = 2 * Math.PI / nbrCercle;
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
	public void cercle() {
		rotation.rotZ(dThetaCercle);
		Vector3d initial = new Vector3d(rayon, 0, 0);
		double theta = 0;
		do {
			cercle.add((Vector3d) initial.clone());
			rotation.transform(initial);
			theta += dThetaCercle;
		} while (theta < 2 * Math.PI);
	}

	/**
	 * Cree les cercles de vecteurs 3D de chaque point autour d'un axe, formant un cercle de points.
	 */
	public void sphere() {
		Vector3d u = new Vector3d();
		double theta = 0;
		do {
			rotation.rotY(theta);
			ArrayList<Vector3d> anneau = new ArrayList<Vector3d>();
			for (Vector3d v : cercle) {
				rotation.transform(v, u);
				anneau.add((Vector3d) u.clone());
			}
			
			sphere.add((ArrayList<Vector3d>)anneau);

			theta += dThetaSphere;
		} while (theta < 2 * Math.PI);
	}
	
	public static void main(String[] args) {

		Sphere s = new Sphere(100, 100, 100);
		s.cercle();
		System.out.println(s.cercle);
		/*
		 * Matrix3d rotation = new Matrix3d(); rotation.rotY(Math.PI / 3); Vector3d v =
		 * new Vector3d(1, 0, 0); System.out.println(v); rotation.transform(v);
		 * System.out.println(v); rotation.transform(v); System.out.println(v);
		 */
	}
}