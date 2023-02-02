package application;

import java.util.ArrayList;
import javax.vecmath.Vector3d;
import javax.vecmath.Matrix3d;

public class Sphere {
	final double RAYON = 100;
	final double NBR_CERCLE = 100;
	final double NBR_POINT_PAR_CERCLE = 100;
	final double DTETHA_CERCLE = 2 * Math.PI / NBR_POINT_PAR_CERCLE;
	final double DTETHA_SPHERE = 2 * Math.PI / NBR_CERCLE;

	ArrayList<Vector3d> cercle = new ArrayList<Vector3d>();
	ArrayList<ArrayList<Vector3d>> sphere = new ArrayList<ArrayList<Vector3d>>();
	Matrix3d rotation = new Matrix3d();

	public ArrayList<Vector3d> getCercle() {
		return cercle;
	}

	public void cercle() {
		rotation.rotZ(DTETHA_CERCLE);
		Vector3d initial = new Vector3d(RAYON, 0, 0);
		double theta = 0;
		do {
			cercle.add((Vector3d) initial.clone());
			rotation.transform(initial);
			theta += DTETHA_CERCLE;
		} while (theta < 2 * Math.PI);
	}

	public void sphere() {
		
		Vector3d u = new Vector3d();
		double theta = 0;
		do {
			rotation.rotY(theta);
			ArrayList<Vector3d> anneau = new ArrayList<Vector3d>();
			for (Vector3d v : cercle) {
				rotation.transform(v, u);
				anneau.add((Vector3d) u.clone());
			}
			
			sphere.add((ArrayList<Vector3d>)anneau);

			theta += DTETHA_SPHERE;
		} while (theta < 2 * Math.PI);

	}

	public static void main(String[] args) {

		Sphere s = new Sphere();
		s.cercle();
		System.out.println(s.cercle);
		/*
		 * Matrix3d rotation = new Matrix3d(); rotation.rotY(Math.PI / 3); Vector3d v =
		 * new Vector3d(1, 0, 0); System.out.println(v); rotation.transform(v);
		 * System.out.println(v); rotation.transform(v); System.out.println(v);
		 */
	}

}
