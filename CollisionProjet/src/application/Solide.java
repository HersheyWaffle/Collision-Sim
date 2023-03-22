package application;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.vecmath.Matrix3d;
import javax.vecmath.Vector3d;

public class Solide {
	ArrayList<ArrayList<Vector3d>> solide;
	ArrayList<Vector3d> plan;
	final static int CONSTANTE_OMBRE = 15;

	/**
	 * Enleve les points poses a la meme position. Pas necessaire??
	 * 
	 * @param solide. Le ArrayList de ArrayList des points du solide
	 */
	public void enleveDoublons(ArrayList<ArrayList<Vector3d>> solide) {
		int countA = 0;
		int countB = 0;
		Set<Vector3d> set = new LinkedHashSet<>();

		for (ArrayList<Vector3d> listPlan : solide) {
			countA += listPlan.size();
			set.addAll(listPlan);
		}

		ArrayList<Vector3d> solideFinal = new ArrayList<Vector3d>();
		solideFinal.addAll(set);
		set.clear();
		solide.clear();
		solide.add(solideFinal);

//============DEBUG================		
		countB = solideFinal.size();

		System.out.println("Avant: " + countA);
		System.out.println("Apres: " + countB);
//=================================
	}

	/*
	 * Donne un vecteur perpendiculaire à un vecteur donnée
	 */
	public static Vector3d getPerpendicular(Vector3d v) {
		Vector3d u = new Vector3d(1, 1, 1);
		if (v.x != 0) {
			u.x = -(v.y * u.y + v.z * u.z) / v.x;
			u.normalize();
			return u;
		} else if (v.y != 0) {
			u.y = -(v.x * u.x + v.z * u.z) / v.y;
			u.normalize();
			return u;
		} else if (v.z != 0) {
			u.z = -(v.x * u.x + v.y * u.y) / v.z;
			u.normalize();
			return u;
		} else {
			return new Vector3d(1, 0, 0);
		}
	}

	static public ArrayList<Vector3d> cercle(double dThetaCercle, Vector3d v, double rayon) {
		ArrayList<Vector3d> cercle = new ArrayList<Vector3d>();
		Matrix3d rotation = new Matrix3d();

		double theta = 0;
		double phi1 = 0;
		double phi2 = 0;

		Vector3d v1 = (Vector3d) v.clone();

		/*
		 * Calcul l'angle Ramène sur l'axe des z
		 */

		phi1 = Math.atan2(v1.y, v1.z);

		rotation.rotX(phi1);
		rotation.transform(v1);

		phi2 = Math.atan2(v1.z, v1.x);
		rotation.rotY(phi2);

		do {
			Vector3d initial = new Vector3d(0, 0, rayon);

			// crée le cercle autour de l'axe des x
			rotation.rotX(theta);
			rotation.transform(initial);

			// le met autour du vecteur v

			rotation.rotY(-phi2);
			rotation.transform(initial);

			rotation.rotX(-phi1);
			rotation.transform(initial);

			cercle.add(initial);

			theta += dThetaCercle;
		} while (theta < 2 * Math.PI);
		return cercle;

	}
	
	
	static public ArrayList<Vector3d> semicercle(double dThetaCercle, Vector3d v, double rayon) {
		ArrayList<Vector3d> semicercle = new ArrayList<Vector3d>();
		Matrix3d rotation = new Matrix3d();

		double theta = 0;
		double phi1 = 0;
		double phi2 = 0;

		Vector3d v1 = (Vector3d) v.clone();

		/*
		 * Calcul l'angle Ramène sur l'axe des z
		 */

		phi1 = Math.atan2(v1.y, v1.z);

		rotation.rotX(phi1);
		rotation.transform(v1);

		phi2 = Math.atan2(v1.z, v1.x);
		rotation.rotY(phi2);

		do {
			Vector3d initial = new Vector3d(0, 0, rayon);

			// crée le cercle autour de l'axe des x
			rotation.rotX(theta);
			rotation.transform(initial);

			// le met autour du vecteur v

			rotation.rotY(-phi2);
			rotation.transform(initial);

			rotation.rotX(-phi1);
			rotation.transform(initial);

			semicercle.add(initial);

			theta += dThetaCercle;
		} while (theta < Math.PI);
		return semicercle;

	}

	public static ArrayList<ArrayList<Point>> quadrant(ArrayList<Vector3d> section, ArrayList<Point> objet) {

		ArrayList<ArrayList<Point>> retour = new ArrayList<ArrayList<Point>>();

		for (int i = 0; i < Math.pow(2, section.size()); i++) {
			retour.add(new ArrayList<Point>());
		}
		for (Point p : objet) {
			for (Vector3d v : section) {

				if (v.dot(p.getCoordonnée()) < 0) {
					p.addQuadrant("1");
				} else {
					p.addQuadrant("0");
				}

			}

			retour.get(Integer.parseInt(p.getQuadrant(), 2)).add(p);
			p.clearQuadrant();
		}

		return retour;

	}
	
	/*
	 * Indique quel point est dans l'ombre de la lumière
	 * */
	public static void vBuffer(Vector3d v1, ArrayList<Point> objet) {
		v1.normalize();
		Vector3d v2 = getPerpendicular(v1);
		v2.normalize();
		Vector3d v3 = new Vector3d();
		v3.cross(v1, v2);
		v3.normalize();

		Matrix3d m = new Matrix3d(v3.x, v2.x, v1.x, v3.y, v2.y, v1.y, v3.z, v2.z, v1.z);
		m.invert();
		
		//met les points dans une autre base pour les ombres
		for (Point p : objet) {
			Vector3d u = new Vector3d();
			
			//approxime la position des points
			m.transform(p.getCoordonnée(), u);
			u.x = ((int) u.x / CONSTANTE_OMBRE) * CONSTANTE_OMBRE;
			u.y = ((int) u.y / CONSTANTE_OMBRE) * CONSTANTE_OMBRE;
			
			p.setShadow(u);
		}

		// v-buffer
		for (int i = 0; i < objet.size() - 1; i++) {
			for (int j = i + 1; j < objet.size(); j++) {
				// for vector rendered at the same place

				if ((int) objet.get(i).getCoordonnée().x == (int) objet.get(j).getCoordonnée().x
						&& (int) objet.get(i).getShadow().y == (int) objet.get(j).getShadow().y) {
					// delete the fartest one
					if (objet.get(i).getShadow().z > objet.get(j).getShadow().z) {
						objet.get(j).setRendered(false);
					} else {
						objet.get(i).setRendered(false);
					}

				}
			}
		}

	}
}
