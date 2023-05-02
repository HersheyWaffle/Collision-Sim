package application;

import java.util.ArrayList;

import javax.vecmath.Matrix3d;
import javax.vecmath.Vector3d;

public class test {
	
	
	public static Vector3d getPerpendicular(Vector3d v) {
		Vector3d u = new Vector3d(1,1,1);
		if(v.x != 0) {
			u.x = -(v.y*u.y + v.z*u.z)/v.x;
			u.normalize();
			return u;
		} else if(v.y !=0) {
			u.y = -(v.x*u.x + v.z*u.z)/v.y;
			u.normalize();
			return u;
		}else if(v.z !=0) {
			u.z = -(v.x*u.x + v.y*u.y)/v.z;
			u.normalize();
			return u;
		}else {
			return new Vector3d(1,0,0);
		}
	}

	public static void main(String[] args) {
		/*Vector3d v = new Vector3d(0,0,1);
		Vector3d u = new Vector3d();
		u.cross(v, getPerpendicular(v));

		System.out.println(getPerpendicular(v).toString() +"   " + u.toString() + "   "+ v.toString());*/
		/*
		Matrix3d rotation = new Matrix3d();
		double phi1 =0;
		double phi2 =0;
		
		Vector3d v = new Vector3d(20,12,41);
		//System.out.println(v);
		
		
		//angle entre y et z rot de y à z
		phi1 = Math.atan2(v.y,v.z);
		rotation.rotX( phi1);
		rotation.transform(v);
		//System.out.println(v);
		
		//angle entre z et x, rot de z à x
		phi2= Math.atan2(v.z,v.x);
		rotation.rotY(phi2);
		rotation.transform(v);
		System.out.println(v);
		
		
		rotation.rotY(-phi2);
		rotation.transform(v);
		
		
		
		//System.out.println(v);
		
		
		
		rotation.rotX(-phi1);
		rotation.transform(v);
		System.out.println(v);
		
		
		double theta = 0;
		double dThetaCercle = Math.PI/10;
		double rayon = 2;
		ArrayList<Vector3d> cercle = new ArrayList<Vector3d>();
		do {
			
			Vector3d initial = new Vector3d(0, 0, rayon);
			
			rotation.rotX(theta);
			rotation.transform(initial);
			
			rotation.rotX(-phi2);
			rotation.transform(initial);
			
			rotation.rotY(-phi1);
			rotation.transform(initial);

			
			cercle.add(initial);

			
			theta += dThetaCercle;
		} while (theta < 2 * Math.PI);
		
		System.out.println(cercle);
		*/
		
		Matrix3d dtheta = new Matrix3d();
		Matrix3d m1 = new Matrix3d();
		Matrix3d m2 = new Matrix3d();
		
		m1.rotX(Math.PI/3);
		System.out.println(m1);
		m2.rotY(Math.PI/3);
		System.out.println(m2);
		dtheta.mul(m1,m2);
		System.out.println(dtheta);
	    
		
		
		
		
		

	}

}
