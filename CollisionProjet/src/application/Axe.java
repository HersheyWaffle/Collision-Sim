package application;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.vecmath.Vector3d;
import javax.vecmath.Matrix3d;

public class Axe extends Solide {
	Vector3d direction;
	
	
	public  Axe(Vector3d direction,int FONT_SIZE){
		direction.normalize();
		this.direction = direction;
		resize = false;
		créerAxe(400);
		render(FONT_SIZE);
	}
	
	public void créerAxe(int lenght) {
		for(int i=0; i<lenght; i+=5) {
			Vector3d v = new Vector3d();
			v.scale(i, direction);
			Point point = new Point(v,new Vector3d(0,0,-1));
			solide.add(point);
		}
		
		Lumiere.lumiere_Objet(solide);
		
	}
	
	
}
