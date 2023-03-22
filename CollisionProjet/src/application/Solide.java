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
 * Classe utilitaire avec des fonctions g�n�riques pour traiter les objets.
 * 
 * @version 1.4.0 2023-03-08
 * @author Omar Ghazaly, Abel-Jimmy Oyono-Montoki
 */
public class Solide {

//=========================VARIABLES=========================	

	private ArrayList<Vector3d> solide = new ArrayList<Vector3d>();

//=========================METHODES=========================	

	public ArrayList<Vector3d> getSolide() {
		return solide;
	}

	public void setSolide(ArrayList<Vector3d> solide) {
		this.solide = solide;
	}

	/**
	 * Enl�ve les points pos�s � la m�me position.
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

//============DEBUG================	
		System.out.println("Avant: " + countA);
		System.out.println("Apres: " + countB);
//=================================
	}

	/**
	 * Effectue une rotation sur le solide. Les param�tres sont en DEGR�S
	 * 
	 * @param solide - Le solide sur lequel on effectue la rotation
	 * @param rotX   - Rotation sur l'axe des X
	 * @param rotY   - Rotation sur l'axe des Y
	 * @param rotZ   - Rotation sur l'axe des Z
	 */
	public static void rotateSolide(ArrayList<Vector3d> solide, double rotX, double rotY, double rotZ, double posX,
			double posY, double posZ) {
		Matrix3d rotation = new Matrix3d(); // Matrice 3D servant � effectuer la rotation des points et des plans

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
	 * G�n�re les points d'un solide en effectuant une rotation sur un plan.
	 * 
	 * @param dThetaCercle - Angle en Rad entre chaque point dans le cercle
	 * @param plan         - La liste de tous les points du plan qui va former le
	 *                     solide.
	 * @param solide       - La liste de tous les points du solide.
	 */
	public static void setFormeRotation(double dThetaCercle, ArrayList<Vector3d> plan, ArrayList<Vector3d> solide) {
		Matrix3d rotation = new Matrix3d(); // Matrice 3D servant � effectuer la rotation des points et des cercles
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
