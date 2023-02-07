package application;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.vecmath.Vector3d;

public class Solide {
	ArrayList<ArrayList<Vector3d>> solide;
	ArrayList<Vector3d> plan;
	
	/**
	 * Enleve les points poses a la meme position.
	 * Pas necessaire??
	 * 
	 * @param solide. Le ArrayList de ArrayList des points du solide
	 */
	public void enleveDoublons(ArrayList<ArrayList<Vector3d>> solide) {
		int countA = 0;
		int countB = 0;
		Set<Vector3d> set = new LinkedHashSet<>();
        
		for(ArrayList<Vector3d> listPlan : solide) {
			countA += listPlan.size();
        	set.addAll(listPlan);
        }
		
		ArrayList<Vector3d> solideFinal = new ArrayList<Vector3d>();
		solideFinal.addAll(set);
		set.clear();
		solide.clear();
		solide.add(solideFinal);
		
		
//============DEBUG================		
		countB = solideFinal.size();
		
		System.out.println("Avant: " + countA);
		System.out.println("Apres: " + countB);
//=================================
	}
}
