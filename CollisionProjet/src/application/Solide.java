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

	Vector3d virtual_centre = new Vector3d(0, 0, 0);
	static Matrix3d rotation = new Matrix3d();

	final double Z_CONST = 400;
	final static int CONSTANTE_OMBRE = 10;
	final double ESPACE_ENTRE_POINT = 1;

	private ArrayList<Point> solide = new ArrayList<Point>();
	ArrayList<Point> renderedSolide = new ArrayList<Point>();
	ArrayList<ArrayList<Point>> space = new ArrayList<ArrayList<Point>>();
	ArrayList<ArrayList<Point>> zBufferSpace;

//=========================METHODES=========================	

	/**
	 * Déplace le centre du solide
	 * 
	 * @param dv -le déplaceement
	 */
	public void deplacement(Vector3d dv) {
		virtual_centre.add(dv);
	}

	/**
	 * @return Un Array List de tous les points du solide.
	 */
	public ArrayList<Point> getSolide() {
		return solide;
	}

	/**
	 * @param solide - the solide to set
	 */
	public void setSolide(ArrayList<Point> solide) {
		this.solide = solide;
	}

	/**
	 * @return the renderedSolide
	 */
	public ArrayList<Point> getRenderedSolide() {
		return renderedSolide;
	}

	/**
	 * @param renderedSolide - the renderedSolide to set
	 */
	public void setRenderedSolide(ArrayList<Point> renderedSolide) {
		this.renderedSolide = renderedSolide;
	}

	/**
	 * Enlève les points posés à la même position.
	 * 
	 * @param solide - Le ArrayList des points du solide
	 */
	public static void enleveDoublons(ArrayList<Point> solide) {
		int countA = solide.size();

		Set<Point> set = new LinkedHashSet<>();
		set.addAll(solide);

		solide.clear();
		solide.addAll(set);

		int countB = solide.size();

		if (Main.DEBUG_MODE) System.out.println("Avant: " + countA);
		if (Main.DEBUG_MODE) System.out.println("Apres: " + countB);
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
	 * Retourne un ArrayList d'un cercle de points dont les vecteurs sont
	 * perpendiculaires au vecteur en paramètre.
	 * 
	 * @param dThetaCercle - Angle en Rad entre chaque point dans le cercle
	 * @param v            - Le Vector3D duquel nous voulons déterminer le cercle de
	 *                     vecteurs perpendiculaires.
	 * @param rayon        - Le rayon du cercle.
	 * @param arcCercle     - L'angle en radians de l'arc du cercle.
	 * 
	 * @return retourne un ArrayList de Vector3D, chacun perpendiculaire au vecteur
	 *         v.
	 */
	public static ArrayList<Vector3d> cerclePerpendiculaire(double dThetaCercle, Vector3d v, double rayon,
			double arcCercle) {
		ArrayList<Vector3d> cercle = new ArrayList<Vector3d>();
		Matrix3d rotation = new Matrix3d();

		double theta = 0;
		double phi1 = 0;
		double phi2 = 0;

		Vector3d v1 = (Vector3d) v.clone();

		//Calcul l'angle Ramène sur l'axe des z

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
	 * Retourne un ArrayList du Array List des points du solide, divisés selon les quadrants.
	 * 
	 * @param section - Le Array List de tous les vecteurs représentant les quadrants de la carte
	 * @param objet - Le solide représenté par une liste de points
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

	/**
	 * Divise le solide en quadrants avec local-sensivity hashing
	 */
	public void quadrant() {
		ArrayList<Vector3d> section = new ArrayList<Vector3d>();

		section.add(new Vector3d(1, 0, 0));
		section.add(new Vector3d(0, 1, 0));
		section.add(new Vector3d(0, 0, 1));
		space = quadrant(section, solide);

	}

	/**
	 * Indique quel point est dans l'ombre de la lumière
	 * 
	 * @param v1 - Le vecteur représentant la source de la lumière
	 * @param objet - La liste de points représentant le solide.
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
					// delete the farthest one
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
	 * Enlève les points côte à côte
	 */
	public void clean() {
		solide.clear();
		for (ArrayList<Point> quadrant : space) {
			for (int i = 0; i < quadrant.size() - 1; i++) {
				for (int j = i + 1; j < quadrant.size(); j++) {

					Vector3d distance = new Vector3d();
					distance.sub(quadrant.get(i).getCoordonnee(), quadrant.get(j).getCoordonnee());

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
	 * Calcule le solide à afficher
	 * 
	 * @param FONT_SIZE - La taille de police des caractères, ne devrait techniquement pas changer.
	 */
	public void render(final int FONT_SIZE) {
		renderedSolide.clear();

		// copie les points de la sphere dans la rendered sphere
		for (Point p : solide) {
			Vector3d v = ((Vector3d) p.getCoordonnee().clone());
			
			//FIXME Update par bonds au lieu de linéairement
//			v.scale(Double.valueOf(FONT_SIZE/CONSTANTE_OMBRE));
			
			// update to rendering position
			v.z += virtual_centre.z + Z_CONST;

			v.x *= (Z_CONST) / (v.z);
			v.y *= (Z_CONST) / (v.z);

			//
			v.x += (virtual_centre.x * Z_CONST) / (virtual_centre.z + Z_CONST);
			v.y += (virtual_centre.y * Z_CONST) / (virtual_centre.z + Z_CONST);

			v.x = ((int) v.x / FONT_SIZE) * FONT_SIZE;
			v.y = ((int) v.y / FONT_SIZE) * FONT_SIZE;

			Point renderedPoint = new Point(v, new Vector3d());
			renderedPoint.setEclairage(p.getEclairage());
			renderedSolide.add(renderedPoint);
		}

		ArrayList<Vector3d> section = cerclePerpendiculaire(Math.PI / 6, new Vector3d(0, 0, 1), 1, Math.PI);

		zBufferSpace = quadrant(section, renderedSolide);

		// z-buffer
		for (ArrayList<Point> quadrant : zBufferSpace) {
			for (int i = 0; i < quadrant.size() - 1; i++) {
				for (int j = i + 1; j < quadrant.size(); j++) {

					// for vector rendered at the same place
					if ((int) quadrant.get(i).getCoordonnee().x == (int) quadrant.get(j).getCoordonnee().x
							&& (int) quadrant.get(i).getCoordonnee().y == (int) quadrant.get(j).getCoordonnee().y) {
						// delete the farthest one
						if (quadrant.get(i).getCoordonnee().z < quadrant.get(j).getCoordonnee().z) {
							quadrant.get(j).setRendered(false);
						}
						else {
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
	 * Fait tourner le solide sur le centre de la scène.
	 * 
	 * @param thetax - en radians
	 * @param thetay - en radians
	 * @param thetaz - en radians
	 */
	public void rotate(double thetax, double thetay, double thetaz) {
		for (Point p : solide) {
			p.getCoordonnee().add(virtual_centre);
			p.getNorme().add(virtual_centre);
			
			rotation.rotX(thetax);
			rotation.transform(p.getCoordonnee());
			rotation.transform(p.getNorme());
			rotation.rotY(thetay);
			rotation.transform(p.getCoordonnee());
			rotation.transform(p.getNorme());
			rotation.rotZ(thetaz);
			rotation.transform(p.getCoordonnee());
			rotation.transform(p.getNorme());
			
			p.getCoordonnee().sub(virtual_centre);
			p.getNorme().sub(virtual_centre);
			p.getNorme().normalize();
			p.setRendered(true);
		}
		Lumiere.lumiere_Objet(solide);
	}

	/**
	 * Génère les points d'un solide en effectuant une rotation sur un plan.
	 * 
	 * @param dThetaCercle - Angle en Rad entre chaque point dans le cercle
	 * @param plan         - La liste de tous les points du plan qui va former le
	 *                     solide.
	 * @param solide       - La liste de tous les points du solide.
	 */
	public static void setFormeRotation(double dThetaCercle, ArrayList<Point> plan, ArrayList<Point> solide) {
		Matrix3d rotation = new Matrix3d(); // Matrice 3D servant à effectuer la rotation des points et des cercles
		Vector3d u = new Vector3d();

		double theta = 0;
		do {
			rotation.rotY(theta);
			ArrayList<Point> anneau = new ArrayList<Point>();
			for (Point v : plan) {
				rotation.transform(v.getCoordonnee(), u);
				anneau.add(new Point((Vector3d) u.clone(), (Vector3d) u.clone()));
			}

			solide.addAll(anneau);

			theta += dThetaCercle;
		} while (theta < 2 * Math.PI);
	}
	
	/**
	 * Effectue une rotation sur le solide. Les paramètres sont en DEGRÉS
	 * 
	 * @param solide - Le solide sur lequel on effectue la rotation
	 * @param rotX   - Rotation sur l'axe des X
	 * @param rotY   - Rotation sur l'axe des Y
	 * @param rotZ   - Rotation sur l'axe des Z
	 */

	@Deprecated
	public static void rotateSolide(ArrayList<Point> solide, double rotX, double rotY, double rotZ, double posX,
			double posY, double posZ) {
		Matrix3d rotation = new Matrix3d(); // Matrice 3D servant à effectuer la rotation des points et des plans

		for (Point v : solide) {
			if (rotX != 0) {
				rotation.rotX(rotX * Math.PI / 180);
				rotation.transform(v.getCoordonnee());
				rotation.transform(v.getNorme());
			}
			if (rotY != 0) {
				rotation.rotY(rotY * Math.PI / 180);
				rotation.transform(v.getCoordonnee());
				rotation.transform(v.getNorme());
			}
			if (rotZ != 0) {
				rotation.rotZ(rotZ * Math.PI / 180);
				rotation.transform(v.getCoordonnee());
				rotation.transform(v.getNorme());
			}
		}
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
