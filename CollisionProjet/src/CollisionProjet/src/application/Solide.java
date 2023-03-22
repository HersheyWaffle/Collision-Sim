package application;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.vecmath.Matrix3d;
import javax.vecmath.Vector3d;

public class Solide {
	ArrayList<Point> solide = new ArrayList<Point>();
	ArrayList<Point> renderedSolide = new ArrayList<Point>(); 
	

	
	ArrayList<ArrayList<Point>> space = new ArrayList<ArrayList<Point>>(); 
	ArrayList<ArrayList<Point>> zBufferSpace ;
	
	Vector3d virtual_centre = new Vector3d(0, 0, 0);
	
	static Matrix3d rotation = new Matrix3d(); 
	
	final double Z_CONST = 400;
	static final int CONSTANTE_OMBRE = 10;
	final double ESPACE_ENTRE_POINT = 1;
		

	
	/**
	 * Déplace le centre du solide
	 * 
	 *@param dv -le déplaceement 
	 * */
	public void déplacement(Vector3d dv) {
		virtual_centre.add(dv);
	}
	
	

	/**
	 * Donne un vecteur perpendiculaire à un vecteur donnée
	 * 
	 * @param vecteur donné
	 * 
	 * @return vecteur perpendiculaire
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
	
	
	/**
	 * Crée un cercle de rayon r ,perpendiculaire à un vecteur v donné.
	 * 
	 * @param dThetaCercle-angle d'incrémentation
	 * @param v-vecteur donné
	 * @param rayon-rayon du cercle
	 * 
	 * @return une liste de vecteur cercle
	 * */
	static public ArrayList<Vector3d> cercle(double dThetaCercle, Vector3d v, double rayon,double ArcDeCercle) {
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
		} while (theta < ArcDeCercle);
		return cercle;

	}
	
	
	/**
	 * Divise un objet en région avec le local sensitivity-Hashing
	 * 
	 * @param section- les vecteurs qui définissent les sous-sections
	 * @param objet- le solide
	 * 
	 * @return retourne les régions
	 * 
	 * */
	public static ArrayList<ArrayList<Point>> quadrant(ArrayList<Vector3d> section, ArrayList<Point> objet) {

		ArrayList<ArrayList<Point>> regions = new ArrayList<ArrayList<Point>>();

		for (int i = 0; i < Math.pow(2, section.size()); i++) {
			regions.add(new ArrayList<Point>());
		}
		for (Point p : objet) {
			for (Vector3d v : section) {

				if (v.dot(p.getCoordonnée()) < 0) {
					p.addQuadrant("1");
				} else {
					p.addQuadrant("0");
				}

			}

			regions.get(Integer.parseInt(p.getQuadrant(), 2)).add(p);
			p.clearQuadrant();
		}

		return regions;

	}
	
	/**
	 * Crée un ZBuffer selon un vecteur v
	 * 
	 * @param v1-Le vecteur pour le Z buffer
	 * @param objet-le solide
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
	
	
	/**
	 * Divise la sphere en quadrant avec local-sensivity hashing
	 */
	public void quadrant() {
		ArrayList<Vector3d> section = new ArrayList<Vector3d>();

		section.add(new Vector3d(1, 0, 0));
		section.add(new Vector3d(0, 1, 0));
		section.add(new Vector3d(0, 0, 1));
		space = quadrant(section, solide);

	}

	/**
	 * Enlève les point cote à cote
	 */
	public void clean() {
		solide.clear();
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

			solide.addAll(quadrant);
		}

	}

	/**
	 * Calcul la sphère a affiché
	 * 
	 * @param FONT_SIZE
	 */
	public void render(final int FONT_SIZE) {
		renderedSolide.clear();
		
		

		// copie les points de la sphere dans la rendered sphere
		for (Point p : solide) {
			Vector3d v = (Vector3d) p.getCoordonnée().clone();
			
			//update to rendering positio
			v.z += virtual_centre.z + Z_CONST;

			v.x *= (Z_CONST) / (v.z);
			v.y *= (Z_CONST) / (v.z);
			
			//
			v.x += (virtual_centre.x*Z_CONST) / (virtual_centre.z + Z_CONST);
			v.y += (virtual_centre.y*Z_CONST) / (virtual_centre.z + Z_CONST);
			

			v.x = ((int) v.x / FONT_SIZE) * FONT_SIZE;
			v.y = ((int) v.y / FONT_SIZE) * FONT_SIZE;

			Point renderedPoint = new Point(v, new Vector3d());
			renderedPoint.setÉclairage(p.getÉclairage());
			renderedSolide.add(renderedPoint);
		}

		ArrayList<Vector3d> section = cercle(Math.PI / 6, new Vector3d(0, 0, 1), 1, Math.PI);

		zBufferSpace  = quadrant(section, renderedSolide);
		
		
		
		
		
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
		Lumiere.ombre_Objet(renderedSolide);

		// to clean renderSphere
		for (int i = 0; i < renderedSolide.size(); i++) {

			if (!renderedSolide.get(i).isRendered()) {

				renderedSolide.remove(renderedSolide.get(i));
				i--;
			}

		}
	}

	/**
	 * Fait tourner le solide 
	 * 
	 * @param: thetax, thetay, thetaz en radians
	 */
	public void rotate(double thetax, double thetay, double thetaz) {
		for (Point p : solide) {

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
			Lumiere.lumière_Objet(solide);

		}
	}
	
	
	
	
}
