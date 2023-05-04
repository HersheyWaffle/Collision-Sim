package application;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.vecmath.Matrix3d;
import javax.vecmath.Vector3d;

import javafx.scene.paint.Color;

public class Solide {
	// le solide dans l'espace
	ArrayList<Point> solide = new ArrayList<Point>();
	ArrayList<Point> renderedSolide = new ArrayList<Point>();
	Vector3d virtualCentre = new Vector3d(0, 0, 0);

	// collision
	double rayonDeCollision;
	boolean isColliding = false;

	// optimisation
	ArrayList<ArrayList<Point>> space = new ArrayList<ArrayList<Point>>();
	ArrayList<ArrayList<Point>> zBufferSpace;

	// paramètre physique
	Vector3d vitesse = new Vector3d();
	Vector3d vitesseAngulaire = new Vector3d(0, 0, 0);
	double masse;
	double momentInertie = 0;
	double coefficientDeRestitution;

	// utilitaire
	static Matrix3d rotation = new Matrix3d();

	// constante
	static double Z_CONST = 400;
	static double distanceObservateur = 0;
	static final int CONSTANTE_OMBRE = 10;
	int FONT_SIZE = 10;
	final double ESPACE_ENTRE_POINT = FONT_SIZE - 1;
	boolean resize = true; // si tu dois resize les caractères

	/**
	 * Deplace le centre du solide
	 * 
	 * @param dv -le deplaceement
	 */

	public Solide(Vector3d vitesse, Vector3d virtualCentre, double masse, double coefficientDeRestitution) {
		this.vitesse = vitesse;
		this.virtualCentre = virtualCentre;
		inertie(new Vector3d(1, 0, 0));
		this.masse = masse;
		this.coefficientDeRestitution = coefficientDeRestitution;
	}

	public Solide(Vector3d vitesse, Vector3d virtualCentre, double masse) {
		this.vitesse = vitesse;
		this.virtualCentre = virtualCentre;
		inertie(new Vector3d(1, 0, 0));
		this.masse = masse;
		coefficientDeRestitution = 19 / 20;
	}

	public Solide() {
		this.vitesse = new Vector3d();
		this.virtualCentre = new Vector3d();
		inertie(new Vector3d(1, 0, 0));
		masse = 30;
		coefficientDeRestitution = 19 / 20;
	}

	public void inertie(Vector3d axe) {
		double dm = masse / solide.size();
		axe.normalize();
		for (Point p : solide) {
			Vector3d projection = (Vector3d) axe.clone();
			projection.scale(p.getCoordonnee().dot(axe));
			Vector3d R = new Vector3d();
			R.sub(p.getCoordonnee(), projection);
			momentInertie += R.lengthSquared() * dm;
		}
	}

	public void deplacement(Vector3d dv) {

		virtualCentre.add(dv);

	}

	public void deplacement2(double dt) {

		// deplacement lineaire
		Vector3d dv = new Vector3d();
		dv.scale(dt, vitesse);
		virtualCentre.add(dv);

		// deplacement angulaire

		double phi1 = 0;
		double phi2 = 0;
		double phi3 = vitesseAngulaire.length() * dt;

		Matrix3d dtheta = new Matrix3d();
		Matrix3d m1 = new Matrix3d();
		Matrix3d m2 = new Matrix3d();

		dtheta.setIdentity();

		Vector3d w = (Vector3d) vitesseAngulaire.clone();

		// les angles pour ramener sur l'axe des x
		phi1 = Math.atan2(w.y, w.z);
		m1.rotX(phi1);
		m1.transform(w);
		phi2 = Math.atan2(w.z, w.x);
		dtheta.mul(m1);

		// ramène sur l'axe des x
		m2.rotY(phi2);
		dtheta.mul(m2);
		// fait la rotation en x
		m1.rotX(phi3);
		dtheta.mul(m1);
		// ramène sur l'axe de rotation original
		m2.rotY(-phi2);
		m1.rotX(-phi1);
		dtheta.mul(m2);
		dtheta.mul(m1);
		transform(dtheta);

	}

	/**
	 * Donne un vecteur perpendiculaire à un vecteur donnee
	 * 
	 * @param vecteur donne
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
	 * Cree un cercle de rayon r ,perpendiculaire à un vecteur v donne.
	 * 
	 * @param dThetaCercle-angle d'incrementation
	 * @param v-vecteur          donne
	 * @param rayon-rayon        du cercle
	 * 
	 * @return une liste de vecteur cercle
	 */
	static public ArrayList<Vector3d> cercle(double dThetaCercle, Vector3d v, double rayon, double ArcDeCercle) {
		ArrayList<Vector3d> cercle = new ArrayList<Vector3d>();
		Matrix3d rotation = new Matrix3d();

		double theta = 0;
		double phi1 = 0;
		double phi2 = 0;

		Vector3d v1 = (Vector3d) v.clone();

		// l'angle pour ramener sur l'axe des z

		phi1 = Math.atan2(v1.y, v1.z);

		rotation.rotX(phi1);
		rotation.transform(v1);

		// l'angle pour ramener sur l'axe des x
		phi2 = Math.atan2(v1.z, v1.x);
		rotation.rotY(phi2);

		do {
			Vector3d initial = new Vector3d(0, 0, rayon);

			// cree le cercle autour de l'axe des x
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
	 * Divise un objet en region avec le local sensitivity-Hashing
	 * 
	 * @param section- les vecteurs qui definissent les sous-sections
	 * @param objet-   le solide
	 * 
	 * @return retourne les regions
	 * 
	 */
	public static ArrayList<ArrayList<Point>> quadrant(ArrayList<Vector3d> section, ArrayList<Point> objet) {

		ArrayList<ArrayList<Point>> regions = new ArrayList<ArrayList<Point>>();

		for (int i = 0; i < Math.pow(2, section.size()); i++) {
			regions.add(new ArrayList<Point>());
		}
		for (Point p : objet) {
			for (Vector3d v : section) {

				if (v.dot(p.getCoordonnee()) < 0) {
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
	 * Cree un ZBuffer selon un vecteur v
	 * 
	 * @param v1-Le    vecteur pour le Z buffer
	 * @param objet-le solide
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
					} else {
						objet.get(i).setRendered(false);
					}

				}
			}
		}

		// to clean renderSphere
		for (int i = 0; i < objet.size(); i++) {

			if (!objet.get(i).isRendered()) {

				objet.remove(objet.get(i));
				i--;
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
	 * Calcul la sphère a affiche
	 * 
	 * @param FONT_SIZE
	 */
	public void render(final int FONT_SIZE) {
		renderedSolide.clear();

		// scale les caractères du donut

		if (resize)
			this.FONT_SIZE = (int) Math.ceil((Z_CONST * FONT_SIZE) / (Z_CONST + virtualCentre.z + distanceObservateur));

		// copie les points de la sphere dans la rendered sphere
		for (Point p : solide) {
			Vector3d v = (Vector3d) p.getCoordonnee().clone();

			// update to rendering position

			v.z += virtualCentre.z + distanceObservateur + Z_CONST;

			// ajoute le point seulement si il est devant l'observateur
			if (v.z > 0) {

				if (resize) {
					//approxime la position des points
					v.x *= (Z_CONST) / (v.z);
					v.y *= (Z_CONST) / (v.z);

					v.x += (virtualCentre.x * Z_CONST) / (virtualCentre.z + distanceObservateur + Z_CONST);
					v.y += (virtualCentre.y * Z_CONST) / (virtualCentre.z + distanceObservateur + Z_CONST);

					v.x = ((int) v.x / this.FONT_SIZE) * this.FONT_SIZE;
					v.y = ((int) v.y / this.FONT_SIZE) * this.FONT_SIZE;
				}

				Point renderedPoint = new Point(v, new Vector3d());
				renderedPoint.setEclairage(p.getEclairage());
				renderedPoint.setColor(p.getColor());
				renderedSolide.add(renderedPoint);
			}
		}

		ArrayList<Vector3d> section = cercle(Math.PI / 6, new Vector3d(0, 0, 1), 1, Math.PI);

		zBufferSpace = quadrant(section, renderedSolide);

		// z-buffer
		for (ArrayList<Point> quadrant : zBufferSpace) {

			for (int i = 0; i < quadrant.size() - 1; i++) {
				for (int j = i + 1; j < quadrant.size(); j++) {
					// for vector rendered at the same place

					if ((int) quadrant.get(i).getCoordonnee().x == (int) quadrant.get(j).getCoordonnee().x
							&& (int) quadrant.get(i).getCoordonnee().y == (int) quadrant.get(j).getCoordonnee().y) {
						// delete the fartest one
						if (quadrant.get(i).getCoordonnee().z < quadrant.get(j).getCoordonnee().z) {
							quadrant.get(j).setRendered(false);
						} else {
							quadrant.get(i).setRendered(false);
						}

					}
				}
			}
		}
		// Lumiere.ombre_Objet(renderedSolide);

		// to clean renderSphere
		for (int i = 0; i < renderedSolide.size(); i++) {

			if (!renderedSolide.get(i).isRendered()) {

				renderedSolide.remove(renderedSolide.get(i));
				i--;
			}

		}
	}

	/**
	 * Fait tourner le solide sur lui même
	 * 
	 * @param: thetax, thetay, thetaz en radians
	 */
	public void rotate(double thetax, double thetay, double thetaz) {

		// rotation par rapport à l'origine

		rotation.rotZ(thetaz);
		rotation.transform(vitesseAngulaire);

		rotation.rotY(thetay);
		rotation.transform(vitesseAngulaire);

		rotation.rotX(thetax);
		rotation.transform(vitesseAngulaire);

		for (Point p : solide) {

			rotation.rotZ(thetaz);
			rotation.transform(p.getCoordonnee());
			rotation.transform(p.getNorme());

			rotation.rotY(thetay);
			rotation.transform(p.getCoordonnee());
			rotation.transform(p.getNorme());
			rotation.rotX(thetax);
			rotation.transform(p.getCoordonnee());
			rotation.transform(p.getNorme());

			p.setRendered(true);

		}
		Lumiere.lumiere_Objet(solide);
	}

	/**
	 * Fait tourner le solide autour de l'origine
	 * 
	 * @param: thetax, thetay, thetaz en radians
	 */
	public void rotateOrigine(double thetax, double thetay, double thetaz) {

		// rotation par rapport à l'origine
		Vector3d util1 = (Vector3d) virtualCentre.clone();

		rotation.rotZ(thetaz);
		rotation.transform(util1);
		rotation.transform(vitesse);
		rotation.transform(vitesseAngulaire);

		rotation.rotY(thetay);
		rotation.transform(util1);
		rotation.transform(vitesse);
		rotation.transform(vitesseAngulaire);

		rotation.rotX(thetax);
		rotation.transform(util1);
		rotation.transform(vitesse);
		rotation.transform(vitesseAngulaire);

		for (Point p : solide) {

			Vector3d util2 = new Vector3d();
			Vector3d util3 = new Vector3d();
			util2.add(p.getCoordonnee(), virtualCentre);
			util3.add(p.getNorme(), virtualCentre);

			rotation.rotZ(thetaz);
			rotation.transform(util2);
			rotation.transform(util3);

			rotation.rotY(thetay);
			rotation.transform(util2);
			rotation.transform(util3);
			rotation.rotX(thetax);
			rotation.transform(util2);
			rotation.transform(util3);

			util2.sub(util1);
			util3.sub(util1);
			p.setCoordonnee(util2);
			p.setNorme(util3);

			p.setRendered(true);

		}
		virtualCentre = util1;
		Lumiere.lumiere_Objet(solide);
	}

	/**
	 * Applique une transformation lineaire à tout les points du solide
	 * 
	 * @param transform - matrice
	 */
	public void transform(Matrix3d transform) {
		// pour les test
		// transform.transform(virtualCentre);
		for (Point p : solide) {
			transform.transform(p.getCoordonnee());
			transform.transform(p.getNorme());
		}
	}

	/**
	 * Detecte si deux solides sont proche
	 * 
	 * @param solide2 - le second solide
	 * @param dt      - intervalle de collision
	 * @return boolean - si les deux solides s'intersecte
	 */
	public boolean detecteurDeProximite(Solide solide2, double dt) {
		Vector3d distance = new Vector3d();

		distance.sub(this.virtualCentre, solide2.virtualCentre);

		this.isColliding = solide2.isColliding = distance.length() <= this.rayonDeCollision + solide2.rayonDeCollision
				? true
				: false;
		return distance.length() <= this.rayonDeCollision + solide2.rayonDeCollision;

	}

	/**
	 * Detecte si deux solides sont en collision
	 * 
	 * @param solide2 - le second solide
	 * @param dt      - intervalle de collision en secondes
	 * 
	 */

	public void detecteurDeCollision(Solide solide2, double dt) {

		Vector3d distance2 = new Vector3d();

		ArrayList<Point> potentialCollisionPoint1 = new ArrayList<Point>();
		ArrayList<Point> potentialCollisionPoint2 = new ArrayList<Point>();

		// trouve les points potentiel de collision du solide 1
		for (Point p : solide) {
			Vector3d coordonneeReel = new Vector3d();

			coordonneeReel.add(p.getCoordonnee(), this.virtualCentre);

			distance2.sub(coordonneeReel, solide2.virtualCentre);
			if (distance2.length() < solide2.rayonDeCollision) {
				potentialCollisionPoint1.add(p);

			}
		}

		// trouve les points potentiel de collision du solide 2

		for (Point p : solide2.solide) {
			Vector3d coordonneeReel = new Vector3d();

			coordonneeReel.add(p.getCoordonnee(), solide2.virtualCentre);

			distance2.sub(coordonneeReel, this.virtualCentre);
			if (distance2.length() < this.rayonDeCollision) {
				potentialCollisionPoint2.add(p);

			}
		}
		// System.out.println("1: " + potentialCollisionPoint1.size());
		// System.out.println("2: " + potentialCollisionPoint2.size());

		ArrayList<Point> CollisionPoint1 = new ArrayList<Point>();
		ArrayList<Point> CollisionPoint2 = new ArrayList<Point>();

		// trouve les points de collision
		for (Point p1 : potentialCollisionPoint1) {
			for (Point p2 : potentialCollisionPoint2) {
				Vector3d v1 = new Vector3d();
				Vector3d v2 = new Vector3d();

				v1.add(p1.getCoordonnee(), this.virtualCentre);
				v2.add(p2.getCoordonnee(), solide2.virtualCentre);

				distance2.sub(v1, v2);

				if (distance2.length() < 10) {
					CollisionPoint1.add(p1);
					CollisionPoint2.add(p2);
					p1.setColor(Color.RED);
					p2.setColor(Color.RED);

				}
			}

		}

		// average des points de collisions
		if (CollisionPoint1.size() != 0) {
			Vector3d coordonnee = new Vector3d();
			Vector3d norme = new Vector3d();
			Point l1 = new Point(coordonnee, norme);
			for (Point p : CollisionPoint1) {
				coordonnee.add(p.getCoordonnee());
				norme.add(p.getNorme());

			}

			coordonnee.scale(1.0 / CollisionPoint1.size());
			norme.scale(1.0 / CollisionPoint1.size());
			l1.setCoordonnee(coordonnee);
			l1.setNorme(norme);

			Vector3d coordonnee2 = new Vector3d();
			Vector3d norme2 = new Vector3d();
			Point l2 = new Point(coordonnee2, norme2);
			for (Point p : CollisionPoint2) {
				coordonnee2.add(p.getCoordonnee());

				norme2.add(p.getNorme());

			}
			coordonnee2.scale(1.0 / CollisionPoint2.size());
			norme2.scale(1.0 / CollisionPoint2.size());
			l2.setCoordonnee(coordonnee2);
			l2.setNorme(norme2);

			resolveCollision(solide2, dt, l1, l2);

		}

		// System.out.println("3: " + CollisionPoint1.size());
		// System.out.println("4: " + CollisionPoint2.size());

	}

	/**
	 * resout les collisions de manière elastique
	 * 
	 * @param solide2 solide avec lequel il est en collision
	 * @param dt      intervalle de temps
	 * @param p1      point de collision du solide1
	 * @param p2      point de collision du solide2
	 */

	public void resolveCollision(Solide solide2, double dt, Point p1, Point p2) {
		// collision elastique
		// calcul les vitesses finales
		Vector3d vitesseFinale1 = new Vector3d();
		Vector3d vitesseFinale2 = new Vector3d();
		Vector3d u1 = new Vector3d();
		Vector3d u2 = new Vector3d();
		u1.scaleAdd(2 * solide2.masse / (this.masse + solide2.masse), solide2.vitesse, new Vector3d());
		u2.scaleAdd((this.masse - solide2.masse) / (this.masse + solide2.masse), this.vitesse, new Vector3d());
		vitesseFinale1.add(u1, u2);

		u1.scaleAdd(2 * this.masse / (this.masse + solide2.masse), this.vitesse, new Vector3d());
		u2.scaleAdd((solide2.masse - this.masse) / (this.masse + solide2.masse), solide2.vitesse, new Vector3d());
		vitesseFinale2.add(u1, u2);

		// change les vitesses finales pour qu'ils ont l'air plus réalistique
		
		Vector3d direction = new Vector3d();
		direction.sub(this.virtualCentre, solide2.virtualCentre);
		direction.normalize();
		/*
		vitesseFinale1.scale(vitesseFinale1.length(), direction);
		direction.scale(-1);
		vitesseFinale2.scale(vitesseFinale2.length(), direction);*/

		// calcul les forces subits par les objets
		Vector3d force1 = new Vector3d();
		Vector3d force2 = new Vector3d();

		force1.sub(vitesseFinale1, this.vitesse);
		force1.scale(this.masse / dt);
		force2.sub(vitesseFinale2, solide2.vitesse);
		force2.scale(solide2.masse / dt);
		System.out.println(force1);
		System.out.println(force2);

		// calcul la vitesse angulaire final des objets
		Vector3d vitesseAngulaire1 = new Vector3d();
		Vector3d vitesseAngulaire2 = new Vector3d();

		vitesseAngulaire1.cross(p1.getCoordonnee(), force1);
		vitesseAngulaire1.scale(dt / this.momentInertie);
		vitesseAngulaire1.add(this.vitesseAngulaire);

		vitesseAngulaire2.cross(p2.getCoordonnee(), force2);
		vitesseAngulaire2.scale(dt / solide2.momentInertie);
		vitesseAngulaire2.add(solide2.vitesseAngulaire);

		// change les paramètres physiques
		this.vitesse = vitesseFinale1;
		this.vitesseAngulaire = vitesseAngulaire1;
		solide2.vitesse = vitesseFinale2;
		solide2.vitesseAngulaire = vitesseAngulaire2;

		System.out.println("solide 1: " + vitesseAngulaire);
		System.out.println("solide 2: " + solide2.vitesseAngulaire);

		// resout la collision

		direction.scale(10);
		this.deplacement(direction);
		direction.scale(-1);
		solide2.deplacement(direction);

	}

	/**
	 * resout les collisions de manière inélastique
	 * 
	 * @param solide2 solide avec lequel il est en collision
	 * @param dt      intervalle de temps
	 * @param p1      point de collision du solide1
	 * @param p2      point de collision du solide2
	 */
	public void resolveCollision2(Solide solide2, double dt, Point p1, Point p2) {
		// collision elastique
		// calcul les vitesses finales
		Vector3d vitesseFinale1 = new Vector3d();
		Vector3d vitesseFinale2 = new Vector3d();
		Vector3d u1 = new Vector3d();
		Vector3d u2 = new Vector3d();
		u1.scaleAdd((this.coefficientDeRestitution * solide2.masse + solide2.masse) / (this.masse + solide2.masse),
				solide2.vitesse, new Vector3d());
		u2.scaleAdd((-this.coefficientDeRestitution * solide2.masse + this.masse) / (this.masse + solide2.masse),
				this.vitesse, new Vector3d());
		vitesseFinale1.add(u1, u2);

		u1.scaleAdd((solide2.coefficientDeRestitution * this.masse + this.masse) / (this.masse + solide2.masse),
				this.vitesse, new Vector3d());
		u2.scaleAdd((-solide2.coefficientDeRestitution * this.masse + solide2.masse) / (this.masse + solide2.masse),
				solide2.vitesse, new Vector3d());
		vitesseFinale2.add(u1, u2);

		// calcul les forces subits par les objets
		Vector3d force1 = new Vector3d();
		Vector3d force2 = new Vector3d();

		force1.sub(vitesseFinale1, this.vitesse);
		force1.scale(this.masse / dt);
		force2.sub(vitesseFinale2, solide2.vitesse);
		force2.scale(solide2.masse / dt);

		// calcul les forces subits par les objets
		Vector3d vitesseAngulaire1 = new Vector3d();
		Vector3d vitesseAngulaire2 = new Vector3d();

		vitesseAngulaire1.cross(p1.getCoordonnee(), force1);
		vitesseAngulaire1.scale(dt / this.momentInertie);
		vitesseAngulaire1.add(this.vitesseAngulaire);

		vitesseAngulaire2.cross(p2.getCoordonnee(), force2);
		vitesseAngulaire2.scale(dt / solide2.momentInertie);
		vitesseAngulaire2.add(solide2.vitesseAngulaire);

		// change les paramètres physiques
		this.vitesse = vitesseFinale1;
		this.vitesseAngulaire = vitesseAngulaire1;
		solide2.vitesse = vitesseFinale2;
		solide2.vitesseAngulaire = vitesseAngulaire2;

		// resout la collision
		// this.deplacement2(dt);
		// solide2.deplacement2(dt);

	}

	/**
	 * change la couleur des points en blanc
	 */
	public void setWhite() {
		for (Point p : solide) {
			p.setColor(Color.WHITE);
		}
	}

	/**
	 * change la couleur des points en une couleur c
	 * 
	 * @param c la couleur en question
	 */

	public void setColor(Color c) {
		for (Point p : solide) {
			p.setColor(c);
		}
	}

	public void setCharacter(String c) {
		for (Point p : solide) {
			p.setEclairage(c);
		}
	}

}
