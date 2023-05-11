package application;

import javax.vecmath.Vector3d;

/**
 * Classe pour créer et afficher les axes de rotation dans la scène. N'est pas
 * utilisé à cause de problèmes de réforme du code pour qu'il fonctionne dans le
 * système à interface, et toute fonctionnalité et mention dans le Main a été
 * omise, donc cette classe n'existe encore que pour des raisons d'archivage.
 * 
 * @version 1.0.0 2023-05-03
 * @author Abel-Jimmy Oyono-Montoki
 */
public class Axe extends Solide {

//=========================VARIABLES=========================		

	private Vector3d direction;
	public final static int AXIS_SIZE = 400;

//=========================CONSTRUCTEUR=======================		

	/**
	 * Initialise les axes de rotation dans la scène.
	 * 
	 * @param direction - La direction de l'axe.
	 * @param fontSize  - La taille de la police.
	 */
	public Axe(Vector3d direction, int fontSize) {
		direction.normalize();
		this.direction = direction;
		resize = false;
		creeAxe(AXIS_SIZE);
		render(fontSize);
	}

//=========================METHODES=========================	

	/**
	 * Crée une axe de rotation dans la scène.
	 * 
	 * @param length - La longueur de l'axe.
	 */
	public void creeAxe(int length) {
		for (int i = 0; i < length; i += 5) {
			Vector3d v = new Vector3d();
			v.scale(i, direction);
			Point point = new Point(v, new Vector3d(0, 0, -1));
			getSolide().add(point);
		}
		Lumiere.lumiereObjet(getSolide());
	}
}