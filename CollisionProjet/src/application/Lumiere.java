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
	private final static double INTENSITE_LUMINEUSE = 0.6;
	
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

	/**
	 * Détermine si les points du solide sont ombragés à l'aide du z-buffer.
	 * 
	 * @param objet - La liste de points du solide sur lequel on veut effectuer le
	 *              calcul
	 */
	public static void ombreObjet(ArrayList<Point> objet) {
		ArrayList<Vector3d> section = Solide.cerclePerpendiculaire(Math.PI / 5, source, 1, Math.PI);
		ArrayList<ArrayList<Point>> ensemble = Solide.quadrant(section, objet);

		for (ArrayList<Point> quadrant : ensemble) {
			Solide.vBuffer(source, quadrant);
		}
	}

	/**
	 * Définit le niveau de luminosité de chaque point du solide.
	 * 
	 * @param objet - La liste de points du solide sur lequel on veut effectuer le
	 *              calcul
	 */
	public static void lumiereObjet(ArrayList<Point> objet) {
		source.normalize();
		for (Point n : objet) {
			n.getNorme().normalize();
			double d = Math.floor((NIVEAU_DE_LUMIERE.length() - 1) * Math.pow(source.dot(n.getNorme()), INTENSITE_LUMINEUSE));
			if (d >= 0) {
				n.setEclairage(NIVEAU_DE_LUMIERE.charAt((int) d) + "");
			}
			else {
				n.setRendered(false);
			}
		}
	}
}