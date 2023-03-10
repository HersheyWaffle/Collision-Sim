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
 * Classe utilitaire avec des fonctions generiques pour traiter les objets.
 * 
 * @version 1.2.0 2023-02-14
 * @author Omar Ghazaly, Abel-Jimmy Oyono-Montoki
 */
public class Solide {
	/**
	 * Enleve les points poses a la meme position.
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
	 * Effectue une rotation sur le cube. Les parametres sont en DEGRES
	 * 
	 * @param solide - Le solide sur lequel on effectue la rotation
	 * @param rotX - Rotation sur l'axe des X
	 * @param rotY - Rotation sur l'axe des Y
	 * @param rotZ - Rotation sur l'axe des Z
	 */
	public static void rotateSolide(ArrayList<Vector3d> solide, double rotX, double rotY, double rotZ) {
		Matrix3d rotation = new Matrix3d();			//Matrice 3D servant a effectuer la rotation des points et des plans
		
		for(Vector3d v : solide) {
			if(rotX != 0) {
				rotation.rotX(rotX*Math.PI/180);
				rotation.transform(v);
			}
			if(rotY != 0) {
				rotation.rotY(rotY*Math.PI/180);
				rotation.transform(v);
			}
			if(rotZ != 0) {
				rotation.rotZ(rotZ*Math.PI/180);
				rotation.transform(v);
			}
		}
	}
	
	/**
	 * Genere les points d'un solide en effectuant une rotation sur un plan.
	 * 
	 * @param dThetaCercle - Angle en Rad entre chaque point dans le cercle
	 * @param plan - La liste de tous les points du plan qui va former le solide.
	 * @param solide - La liste de tous les points du solide.
	 */
	public static void setFormeRotation(double dThetaCercle, ArrayList<Vector3d> plan, ArrayList<Vector3d> solide){
		Matrix3d rotation = new Matrix3d();		//Matrice 3D servant a effectuer la rotation des points et des cercles
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
	 * Ajoute des objets Circle au root selon la liste de points du solide.
	 * 
	 * @param solide - La liste de tous les points du solide.
	 * @param root - Le pane sur lequel on appose les cercles.
	 */
	public static void creeForme(ArrayList<Vector3d> solide, Pane root) {
		for (Vector3d v : solide) {
			Circle cercle = new Circle(1);
			cercle.setLayoutX(v.x + 200);
			cercle.setLayoutY(v.y + 200);
			if(v.z<0) {
				cercle.setFill(Color.RED);
				cercle.setRadius(3);
			}
			
			root.getChildren().add(cercle);
		}
	}
}
