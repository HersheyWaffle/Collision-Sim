package application;

import java.util.ArrayList;
import javax.vecmath.Vector3d;

/**
 * Classe utilitaire qui gère l'affichage de la lumière dans la scène.
 * 
 * @version 1.0.1 2023-03-22
 * @author Abel-Jimmy Oyono-Montoki
 */
public class Lumiere {
	
//=========================VARIABLES=========================	
	
	public static Vector3d source = new Vector3d(0, -1, -1);
	private final static String NIVEAU_DE_LUMIERE = ".,-~:;=!*#$@";
	private final static int INTENSITE_LUMINEUSE = 2;
	
//=========================METHODES=========================	
	
	/*
	public Lumiere(Vector3d source) {
		source.normalize();
		this.source = source;
	}
	
	public Lumiere() {
		Vector3d source = new Vector3d(0,0,1);
	}
	*/

	public static void ombre_Objet(ArrayList<Point> objet) {
		ArrayList<Vector3d> section = Solide.cerclePerpendiculaire(Math.PI / 5, source, 1, Math.PI);
		ArrayList<ArrayList<Point>> ensemble = Solide.quadrant(section, objet);

		for (ArrayList<Point> quadrant : ensemble) {
			Solide.vBuffer(source, quadrant);
		}
	}

	public static void lumiere_Objet(ArrayList<Point> objet) {
		source.normalize();
		for (Point n : objet) {
			n.getNorme().normalize();
			double d = Math.floor((NIVEAU_DE_LUMIERE.length() - 1) * Math.pow(source.dot(n.getNorme()), 0.6));
			if (d >= 0) {
				n.setEclairage(NIVEAU_DE_LUMIERE.charAt((int) d) + "");
			}
			else {
				n.setRendered(false);
			}
		}
	}
}