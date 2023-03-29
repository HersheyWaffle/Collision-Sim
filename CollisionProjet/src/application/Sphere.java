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
	
	private double rayon; // Rayon du cercle
	private double dThetaCercle; // Angle en Rad entre chaque point dans le cercle
	private double dThetaSphere; // Angle en Rad entre chaque cercle dans la sphere

	ArrayList<Point> cercle = new ArrayList<Point>(); // Liste des vecteurs 3D de chaque point dans le cercle
	
	

	


	

	/**
	 * constructeur
	
	 * @param rayon- rayon de la sphere
	 * @param nbrCercle - nombre de cercle
	 * @param nbrPointParCercle - nombre de point par Cercle
	 * @param FONT_SIZE - taille des caractères
	 */
	public Sphere(double rayon, double nbrCercle, double nbrPointParCercle, final int FONT_SIZE) {
		this.rayon = rayon;
		rayonDeCollision = rayon+50;
		
		dThetaCercle = 2 * Math.PI / nbrPointParCercle;
		dThetaSphere = 2 * Math.PI / nbrCercle;

		cercle_Sphere(dThetaCercle, new Vector3d(0, 0, 1), rayon);
		sphere();
		quadrant();
		clean();
		render(FONT_SIZE);
	}

	
	

	/**
	 * Crée un cercle
	 */

	public void cercle_Sphere(double dThetaCercle, Vector3d v, double rayon) {
		ArrayList<Vector3d> c = cercle(dThetaCercle, v, rayon, 2* Math.PI);

		for (Vector3d u1 : c) {
			Vector3d u2 = (Vector3d) u1.clone();
			u1.add(new Vector3d(50,0,0));
			Point p = new Point(u1, u2);
			cercle.add(p);

		}

	}

	/**
	 * Crée une sphere
	 */
	public void sphere() {

		double theta = 0;
		do {
			rotation.rotY(theta);

			for (Point p1 : cercle) {
				Point p2 = p1.clone();
				rotation.transform(p2.getCoordonnée());
				rotation.transform(p2.getNorme());
				solide.add(p2);

			}

			theta += dThetaSphere;
		} while (theta < 2 * Math.PI);
		
		//applique de la lumière sur l'objet
		Lumiere.lumière_Objet(solide);

	}

	
	
	

	
	
	


}