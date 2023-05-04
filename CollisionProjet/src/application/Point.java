package application;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.vecmath.Vector3d;

import javafx.scene.paint.Color;

import javax.vecmath.Matrix3d;

public class Point {
	private Vector3d coordonnee = new Vector3d();
	private Vector3d norme = new Vector3d();
	private Vector3d shadow = new Vector3d();
	private String eclairage;
	private String quadrant = new String();
	private boolean isRendered = true;
	private Color color = Color.WHITE;
	
	public Vector3d getCoordonnee() {
		return coordonnee;
	}

	public void setCoordonnee(Vector3d coordonnee) {
		this.coordonnee = coordonnee;
	}

	public Vector3d getNorme() {
		return norme;
	}

	public void setNorme(Vector3d norme) {
		this.norme = norme;
	}
	
	public Vector3d getShadow() {
		return shadow;
	}

	public void setShadow(Vector3d shadow) {
		this.shadow = shadow;
	}
	
	public String getEclairage() {
		return eclairage;
	}

	public void setEclairage(String eclairage) {
		this.eclairage = eclairage;
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
	
	public boolean isRendered() {
		return isRendered;
	}

	public void setRendered(boolean isRendered) {
		this.isRendered = isRendered;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	
	
	public Point clone() {
		return new Point((Vector3d) coordonnee.clone(), (Vector3d) norme.clone());
	}

	public String toString() {
		return String.format("X: %f Y: %f Z:%f\nEclairage:", coordonnee.x, coordonnee.y, coordonnee.z, eclairage);
	}

	public Point(Vector3d coordonnee, Vector3d norme) {
		norme.normalize();
		this.coordonnee = coordonnee;
		this.norme = norme;
	}
	
	public Point(Vector3d coordonnee) {	
		this.coordonnee = coordonnee;
	}
	
	public Point() {

	}

}
