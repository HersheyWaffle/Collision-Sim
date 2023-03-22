package application;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.vecmath.Matrix3d;
import javax.vecmath.Vector3d;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Classe utilitaire avec des fonctions génériques pour traiter les objets.
 * 
 * @version 1.4.0 2023-03-08
 * @author Omar Ghazaly, Abel-Jimmy Oyono-Montoki
 */
public class Solide {

//=========================VARIABLES=========================	

	final static int CONSTANTE_OMBRE = 15;
	private ArrayList<Vector3d> solide = new ArrayList<Vector3d>();

//=========================METHODES=========================	

	public ArrayList<Vector3d> getSolide() {
		return solide;
	}

	public void setSolide(ArrayList<Vector3d> solide) {
		this.solide = solide;
	}

	/**
	 * Enlève les points posés à la même position.
	 * 
	 * @param solide - Le ArrayList des points du solide
	 */
	public static void enleveDoublons(ArrayList<Vector3d> solide) {
		int countA = solide.size();

		Set<Vector3d> set = new LinkedHashSet<>();
		set.addAll(solide);

		solide.clear();
		solide.addAll(set);

		int countB = solide.size();

		if(Main.DEBUG_MODE) System.out.println("Avant: " + countA);
		if(Main.DEBUG_MODE) System.out.println("Apres: " + countB);
	}

	/**
	 * Retourne un vecteur perpendiculaire au vecteur en paramètre. Si le vecteur
	 * est nul, retourne le vecteur (1, 0, 0)
	 * 
	 * @param v - Le Vector3D duquel nous voulons déterminer le vecteur
	 *          perpendiculaire correspondant
	 * 
	 * @return retourne un Vector3D perpendiculaire au vecteur v.
	 */
	public static Vector3d getPerpendicular(Vector3d v) {
		Vector3d u = new Vector3d(1, 1, 1);
		if (v.x != 0) {
			u.x = -(v.y * u.y + v.z * u.z) / v.x;
			u.normalize();
			return u;
		}
		else if (v.y != 0) {
			u.y = -(v.x * u.x + v.z * u.z) / v.y;
			u.normalize();
			return u;
		}
		else if (v.z != 0) {
			u.z = -(v.x * u.x + v.y * u.y) / v.z;
			u.normalize();
			return u;
		}
		else {
			return new Vector3d(1, 0, 0);
		}
	}

	/**
	 * Retourne un ArrayList d'un cercle de points dont les vecteurs sont perpendiculaires au vecteur en paramètre.
	 * 
	 * @param dThetaCercle - Angle en Rad entre chaque point dans le cercle
	 * @param v - Le Vector3D duquel nous voulons déterminer le cercle de vecteurs perpendiculaires.
	 * @param rayon - Le rayon du cercle.
	 * @param arCercle - L'angle en radians de l'arc du cercle.
	 * 
	 * @return retourne un ArrayList de Vector3D, chacun perpendiculaire au vecteur v.
	 */
	public static ArrayList<Vector3d> cerclePerpendiculaire(double dThetaCercle, Vector3d v, double rayon, double arcCercle) {
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
		} while (theta < arcCercle);
		return cercle;
	}

	/**
	 * Retourne un ArrayList d'un cercle de points dont les vecteurs sont perpendiculaires au vecteur en paramètre.
	 * 
	 * @param dThetaCercle - Angle en Rad entre chaque point dans le cercle
	 * @param v - Le Vector3D duquel nous voulons déterminer le cercle de vecteurs perpendiculaires.
	 * @param rayon - Le rayon du cercle.
	 * @param arCercle - L'angle en radians de l'arc du cercle.
	 * 
	 * @return retourne un ArrayList de Vector3D, chacun perpendiculaire au vecteur v.
	 */
	public static ArrayList<ArrayList<Point>> quadrant(ArrayList<Vector3d> section, ArrayList<Point> objet) {

		ArrayList<ArrayList<Point>> retour = new ArrayList<ArrayList<Point>>();

		for (int i = 0; i < Math.pow(2, section.size()); i++) {
			retour.add(new ArrayList<Point>());
		}
		for (Point p : objet) {
			for (Vector3d v : section) {

				if (v.dot(p.getCoordonnee()) < 0) {
					p.addQuadrant("1");
				}
				else {
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
	 */
	public static void vBuffer(Vector3d v1, ArrayList<Point> objet) {
		v1.normalize();
		Vector3d v2 = getPerpendicular(v1);
		v2.normalize();
		Vector3d v3 = new Vector3d();
		v3.cross(v1, v2);
		v3.normalize();

		Matrix3d m = new Matrix3d(v3.x, v2.x, v1.x, v3.y, v2.y, v1.y, v3.z, v2.z, v1.z);
		m.invert();

		// met les points dans une autre base pour les ombres
		for (Point p : objet) {
			Vector3d u = new Vector3d();

			// approxime la position des points
			m.transform(p.getCoordonnee(), u);
			u.x = ((int) u.x / CONSTANTE_OMBRE) * CONSTANTE_OMBRE;
			u.y = ((int) u.y / CONSTANTE_OMBRE) * CONSTANTE_OMBRE;

			p.setShadow(u);
		}

		// v-buffer
		for (int i = 0; i < objet.size() - 1; i++) {
			for (int j = i + 1; j < objet.size(); j++) {
				// for vector rendered at the same place

				if ((int) objet.get(i).getCoordonnee().x == (int) objet.get(j).getCoordonnee().x
						&& (int) objet.get(i).getShadow().y == (int) objet.get(j).getShadow().y) {
					// delete the fartest one
					if (objet.get(i).getShadow().z > objet.get(j).getShadow().z) {
						objet.get(j).setRendered(false);
					}
					else {
						objet.get(i).setRendered(false);
					}

				}
			}
		}

	}

	/**
	 * Effectue une rotation sur le solide. Les paramètres sont en DEGRÉS
	 * 
	 * @param solide - Le solide sur lequel on effectue la rotation
	 * @param rotX   - Rotation sur l'axe des X
	 * @param rotY   - Rotation sur l'axe des Y
	 * @param rotZ   - Rotation sur l'axe des Z
	 */
	public static void rotateSolide(ArrayList<Vector3d> solide, double rotX, double rotY, double rotZ, double posX,
			double posY, double posZ) {
		Matrix3d rotation = new Matrix3d(); // Matrice 3D servant à effectuer la rotation des points et des plans

		for (Vector3d v : solide) {
			if (rotX != 0) {
				v.x = v.x - posX;
				v.y = v.y - posY;
				v.z = v.z - posZ;
				rotation.rotX(rotX * Math.PI / 180);
				rotation.transform(v);
				v.x = v.x + posX;
				v.y = v.y + posY;
				v.z = v.z + posZ;
			}
			if (rotY != 0) {
				v.x = v.x - posX;
				v.y = v.y - posY;
				v.z = v.z - posZ;
				rotation.rotY(rotY * Math.PI / 180);
				rotation.transform(v);
				v.x = v.x + posX;
				v.y = v.y + posY;
				v.z = v.z + posZ;
			}
			if (rotZ != 0) {
				v.x = v.x - posX;
				v.y = v.y - posY;
				v.z = v.z - posZ;
				rotation.rotZ(rotZ * Math.PI / 180);
				rotation.transform(v);
				v.x = v.x + posX;
				v.y = v.y + posY;
				v.z = v.z + posZ;
			}
		}
	}

	/**
	 * Génère les points d'un solide en effectuant une rotation sur un plan.
	 * 
	 * @param dThetaCercle - Angle en Rad entre chaque point dans le cercle
	 * @param plan         - La liste de tous les points du plan qui va former le
	 *                     solide.
	 * @param solide       - La liste de tous les points du solide.
	 */
	public static void setFormeRotation(double dThetaCercle, ArrayList<Vector3d> plan, ArrayList<Vector3d> solide) {
		Matrix3d rotation = new Matrix3d(); // Matrice 3D servant à effectuer la rotation des points et des cercles
		Vector3d u = new Vector3d();

		double theta = 0;
		do {
			rotation.rotY(theta);
			ArrayList<Vector3d> anneau = new ArrayList<Vector3d>();
			for (Vector3d v : plan) {
				rotation.transform(v, u);
				anneau.add((Vector3d) u.clone());
			}

			solide.addAll(anneau);

			theta += dThetaCercle;
		} while (theta < 2 * Math.PI);
	}

	/**
	 * Ajoute des objets Circle au pane selon la liste de points du solide.
	 * 
	 * @param solide - La liste de tous les points du solide.
	 * @param pane   - Le pane sur lequel on appose les cercles.
	 * @param posX   - La position initiale en X du solide.
	 * @param posY   - La position initiale en Y du solide.
	 * @param posZ   - La position initiale en Z du solide.
	 */
	
	@Deprecated
	public static void creeForme(ArrayList<Vector3d> solide, Pane pane, double posX, double posY, double posZ) {
		for (Vector3d v : solide) {
			Circle cercle = new Circle(1);
			v.x = v.x + posX;
			v.y = v.y + posY;
			v.z = v.z + posZ;
			cercle.setLayoutX(v.x);
			cercle.setLayoutY(v.y);
			if (v.z < 0) {
				cercle.setFill(Color.RED);
				cercle.setRadius(3);
			}

			pane.getChildren().add(cercle);
		}
	}
}
