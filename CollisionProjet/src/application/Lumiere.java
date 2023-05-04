package application;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.vecmath.Vector3d;
import javax.vecmath.Matrix3d;

public class Lumiere {
	static Vector3d source = new Vector3d(0,0,-1);
	static String lumiere = ".,-~:;=!*#$@";
	static int intensite_lumineuse=2;
	/*
	public Lumiere(Vector3d source) {
		source.normalize();
		this.source = source;
		
	}
	
	public Lumiere() {
		Vector3d source = new Vector3d(0,0,1);
	}*/
	
	static public void ombre_Objet(ArrayList<Point> objet) {
		ArrayList<Vector3d> section = Solide.cercle(Math.PI/5, source, 1, Math.PI);
		Solide.vBuffer(source, objet);
		//ArrayList<ArrayList<Point>> ensemble = Solide.quadrant(section, objet);
		
		/*for(ArrayList<Point> quadrant : ensemble) {
			Solide.vBuffer(source, quadrant);
		}*/
		
	}
	
	
	
	static public void lumiere_Objet(ArrayList<Point> objet) {
		source.normalize();
		for(Point n : objet) {
			n.getNorme().normalize();
			double d = Math.floor((lumiere.length()-1)*Math.pow(source.dot(n.getNorme()), 0.6) );
			if(d>=0) {
				
				n.setEclairage(lumiere.charAt((int)d)+"");
			}else {
				n.setRendered(false);
			}
			    
		}
		
	}

	public static void main(String[] args) {

	}

}
