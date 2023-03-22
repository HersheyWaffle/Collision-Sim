package application;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.vecmath.Vector3d;
import javax.vecmath.Matrix3d;

public class Point {
	private Vector3d coordonnée = new Vector3d();
	private Vector3d norme = new Vector3d();
	private Vector3d shadow = new Vector3d();
	private String éclairage;
	private String quadrant = new String();
	private boolean isRendered = true;

	public Vector3d getShadow() {
		return shadow;
	}

	public void setShadow(Vector3d shadow) {
		this.shadow = shadow;
	}
	
	public void addQuadrant(String s) {
		quadrant+= s;
	}
	
	public void clearQuadrant() {
		quadrant = new String();
	}
	
	public String getQuadrant() {
		return quadrant;
	}
	
	
	public String getÉclairage() {
		return éclairage;
	}

	public void setÉclairage(String éclairage) {
		this.éclairage = éclairage;
	}

	public boolean isRendered() {
		return isRendered;
	}

	public void setRendered(boolean isRendered) {
		this.isRendered = isRendered;
	}

	public Vector3d getCoordonnée() {
		return coordonnée;
	}

	public void setCoordonnée(Vector3d coordonnée) {
		this.coordonnée = coordonnée;
	}

	public Vector3d getNorme() {
		return norme;
	}

	public void setNorme(Vector3d norme) {
		this.norme = norme;
	}

	public Point clone() {
		return new Point((Vector3d) coordonnée.clone(), (Vector3d) norme.clone());
	}

	public String toString() {
		return String.format("X: %f Y: %f Z:%f\nÉclairage:", coordonnée.x, coordonnée.y, coordonnée.z, éclairage);
	}

	public Point(Vector3d coordonnée, Vector3d norme) {
		norme.normalize();
		this.coordonnée = coordonnée;
		this.norme = norme;
	}

}
