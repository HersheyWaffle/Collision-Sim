package application;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.vecmath.Vector3d;
import javax.vecmath.Matrix3d;

public class Point {
	private Vector3d coordonn�e = new Vector3d();
	private Vector3d norme = new Vector3d();
	private Vector3d shadow = new Vector3d();
	private String �clairage;
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
	
	
	public String get�clairage() {
		return �clairage;
	}

	public void set�clairage(String �clairage) {
		this.�clairage = �clairage;
	}

	public boolean isRendered() {
		return isRendered;
	}

	public void setRendered(boolean isRendered) {
		this.isRendered = isRendered;
	}

	public Vector3d getCoordonn�e() {
		return coordonn�e;
	}

	public void setCoordonn�e(Vector3d coordonn�e) {
		this.coordonn�e = coordonn�e;
	}

	public Vector3d getNorme() {
		return norme;
	}

	public void setNorme(Vector3d norme) {
		this.norme = norme;
	}

	public Point clone() {
		return new Point((Vector3d) coordonn�e.clone(), (Vector3d) norme.clone());
	}

	public String toString() {
		return String.format("X: %f Y: %f Z:%f\n�clairage:", coordonn�e.x, coordonn�e.y, coordonn�e.z, �clairage);
	}

	public Point(Vector3d coordonn�e, Vector3d norme) {
		norme.normalize();
		this.coordonn�e = coordonn�e;
		this.norme = norme;
	}

}
