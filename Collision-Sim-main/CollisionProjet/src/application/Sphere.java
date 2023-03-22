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
	// constante
	Vector3d virtual_centre = new Vector3d(0, 0, 0);// Centre virtuel de l'objet
	Vector3d rendering_centre = new Vector3d(0, 0, 0);// Centre virtuel de l'objet
	final double ESPACE_ENTRE_POINT = 1;
	final double Z_CONST = 400;

	private double rayon; // Rayon du cercle
	private double dThetaCercle; // Angle en Rad entre chaque point dans le cercle
	private double dThetaSphere; // Angle en Rad entre chaque cercle dans la sphere

	ArrayList<Point> cercle = new ArrayList<Point>(); // Liste des vecteurs 3D de chaque point dans le cercle
	ArrayList<Point> sphere = new ArrayList<Point>(); // Liste des listes des vecteurs 3D de chaque point dans chaque
	ArrayList<ArrayList<Point>> space = new ArrayList<ArrayList<Point>>(); // divise l'objet en quadrant
	ArrayList<ArrayList<Point>> zBufferSpace = new ArrayList<ArrayList<Point>>();
	ArrayList<Point> renderedSphere = new ArrayList<Point>();

	Matrix3d rotation = new Matrix3d(); // Matrice 3D servant a effectuer la rotation des points et des cercles

	/*
	 * constructeur
	 */
	public Sphere(double rayon, double nbrCercle, double nbrPointParCercle, final int FONT_SIZE, Vector3d centre) {
		this.rayon = rayon;
		this.rendering_centre = centre;
		dThetaCercle = 2 * Math.PI / nbrPointParCercle;
		dThetaSphere = 2 * Math.PI / nbrCercle;

		cercle_Sphere(dThetaCercle, new Vector3d(0, 0, 1), rayon);
		sphere();
		quadrant();
		clean();
		render(FONT_SIZE);
	}

	/**
	 * Retourne le cercle de points de l'objet Sphere.
	 * 
	 * @return retourne un ArrayList de vecteurs 3D chacun indiquant la position 3D
	 *         du point.
	 */
	public ArrayList<Point> getCercle() {
		return cercle;
	}

	/**
	 * Cree les vecteurs 3D indiquant la position de chaque point sur le cercle.
	 */

	public void cercle_Sphere(double dThetaCercle, Vector3d v, double rayon) {
		ArrayList<Vector3d> c = cercle(dThetaCercle, v, rayon);

		for (Vector3d u1 : c) {
			Vector3d u2 = (Vector3d) u1.clone();
			u1.add(new Vector3d(100,0,0));
			Point p = new Point(u1, u2);
			cercle.add(p);

		}

	}

	/**
	 * Cree les cercles de vecteurs 3D de chaque point autour d'un axe, formant un
	 * cercle de points.
	 */
	public void sphere() {

		double theta = 0;
		do {
			rotation.rotY(theta);

			for (Point p1 : cercle) {
				Point p2 = p1.clone();
				rotation.transform(p2.getCoordonnée());
				rotation.transform(p2.getNorme());
				sphere.add(p2);

			}

			theta += dThetaSphere;
		} while (theta < 2 * Math.PI);

		Lumiere.lumière_Objet(sphere);

	}

	/*
	 * Change le centre de la sphère
	 */
	public void rendering_centre_Update(Vector3d centre) {
		this.rendering_centre = centre;
	}
	
	public void déplacement(Vector3d dv) {
		dv.x *= (Z_CONST) / (virtual_centre.z + Z_CONST);
		dv.y *= (Z_CONST) / (virtual_centre.z + Z_CONST);
		virtual_centre.add(dv);
	}

	/*
	 * Divise la sphere en quadrant avec local-sensivity hashing
	 */
	public void quadrant() {
		ArrayList<Vector3d> section = new ArrayList<Vector3d>();

		section.add(new Vector3d(1, 0, 0));
		section.add(new Vector3d(0, 1, 0));
		section.add(new Vector3d(0, 0, 1));
		space = quadrant(section, sphere);

	}

	/*
	 * Enlève les points à proximité
	 */
	public void clean() {
		sphere.clear();
		for (ArrayList<Point> quadrant : space) {
			for (int i = 0; i < quadrant.size() - 1; i++) {
				for (int j = i + 1; j < quadrant.size(); j++) {

					Vector3d distance = new Vector3d();
					distance.sub(quadrant.get(i).getCoordonnée(), quadrant.get(j).getCoordonnée());

					if (distance.length() < ESPACE_ENTRE_POINT) {
						quadrant.remove(j);
						j--;
					}
				}
			}

			sphere.addAll(quadrant);
		}

	}

	/*
	 * Calcul la sphere a affiché
	 */
	public void render(final int FONT_SIZE) {
		renderedSphere.clear();

		// copie les points de la sphere dans la rendered sphere
		for (Point p : sphere) {
			Vector3d v = (Vector3d) p.getCoordonnée().clone();
			//update to rendering position
			
			v.z += virtual_centre.z + Z_CONST;

			v.x *= (Z_CONST) / (v.z);
			v.y *= (Z_CONST) / (v.z);
			
			v.x += virtual_centre.x;
			v.y += virtual_centre.y;

			v.x = ((int) v.x / FONT_SIZE) * FONT_SIZE;
			v.y = ((int) v.y / FONT_SIZE) * FONT_SIZE;

			Point renderedPoint = new Point(v, new Vector3d());
			renderedPoint.setÉclairage(p.getÉclairage());
			renderedSphere.add(renderedPoint);
		}

		ArrayList<Vector3d> section = Solide.semicercle(Math.PI / 6, new Vector3d(0, 0, 1), 1);

		zBufferSpace = quadrant(section, renderedSphere);

		// z-buffer
		for (ArrayList<Point> quadrant : zBufferSpace) {
			for (int i = 0; i < quadrant.size() - 1; i++) {
				for (int j = i + 1; j < quadrant.size(); j++) {
					// for vector rendered at the same place

					if ((int) quadrant.get(i).getCoordonnée().x == (int) quadrant.get(j).getCoordonnée().x
							&& (int) quadrant.get(i).getCoordonnée().y == (int) quadrant.get(j).getCoordonnée().y) {
						// delete the fartest one
						if (quadrant.get(i).getCoordonnée().z < quadrant.get(j).getCoordonnée().z) {
							quadrant.get(j).setRendered(false);
						} else {
							quadrant.get(i).setRendered(false);
						}

					}
				}
			}
		}
		Lumiere.ombre_Objet(renderedSphere);

		// to clean renderSphere
		for (int i = 0; i < renderedSphere.size(); i++) {

			if (!renderedSphere.get(i).isRendered()) {

				renderedSphere.remove(renderedSphere.get(i));
				i--;
			}

		}
	}

	/*
	 * Fait tourner la sphère
	 */
	public void rotate(double thetax, double thetay, double thetaz) {
		for (Point p : sphere) {

			rotation.rotX(thetax);
			rotation.transform(p.getCoordonnée());
			rotation.transform(p.getNorme());
			rotation.rotY(thetay);
			rotation.transform(p.getCoordonnée());
			rotation.transform(p.getNorme());
			rotation.rotZ(thetaz);
			rotation.transform(p.getCoordonnée());
			rotation.transform(p.getNorme());
			p.setRendered(true);
			Lumiere.lumière_Objet(sphere);

		}
	}

	public static void main(String[] args) {

		// Sphere s = new Sphere(100, 100, 100, 10);
		// s.cercle();
		// s.sphere();
		// for (Point p : s.cercle) {
		// System.out.println(p.getCoordonnée());
		// }
		/*
		 * Matrix3d rotation = new Matrix3d(); rotation.rotY(Math.PI / 3); Vector3d v =
		 * new Vector3d(1, 0, 0); System.out.println(v); rotation.transform(v);
		 * System.out.println(v); rotation.transform(v); System.out.println(v);
		 */
	}
}