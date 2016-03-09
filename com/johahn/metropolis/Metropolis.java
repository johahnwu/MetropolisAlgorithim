package com.johahn.metropolis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class Metropolis {
	static Random rand = new Random(); 
	double lambda = 0;
	int numVertex = 0;
	double remove = 0;
	double add = 0; 
	HashSet<Integer> setS; 
	
	public Metropolis(double lambda, int numVertex){
		//The empty set is always an independent set
		setS = new HashSet<Integer>(); 
		this.lambda = lambda;
		this.numVertex = numVertex; 
		remove = Math.min(1.0, 1.0/lambda); 
		add = Math.min(1.0, lambda); 
		
		
	}
	
	//generate random integer (vector) with prob 1/(max)
	public static int randInt(int max){
		return (rand.nextInt(max) + 1);
	}
	
	public int twoStepApproach(){
		Integer newVertex = randInt(numVertex); 
		if(setS.contains(newVertex)){
			//flip 
			if(rand.nextDouble() < remove){
				//remove
				setS.remove(newVertex); 
				//System.out.println("Removed vertex " + newVertex);
			}
		}
		else{
			if(checkStillIndependentAfterAdd(newVertex)){
				//flip 
				if(rand.nextDouble() < add){
					//add
					setS.add(newVertex); 
					//System.out.println("Added vertex " + newVertex); 
				}
				//else set i + 1 = set i
			}
			//else set i + 1 = set i
		}
		return setS.size();
	}
	
	/**
	 * In our graph every vertex V is connected to V+1, V+2, V-1, V-2
	 * @param newVertex
	 * @return
	 */
	public boolean checkStillIndependentAfterAdd(Integer newVertex){
		for(Integer v : setS){
			if(Math.abs(v-newVertex) <= 2){
				//is a neighbor of one of the vertexes in the set
				return false;
			}
		}
		return true; 
	}
	
	public int iterateTillConvergeOrMax(int maxIterations, int minStay){
		int iterations = 0;
		int stayCounter = 0; 
		int setSize = setS.size(); 
		while(stayCounter < minStay && iterations != maxIterations){
			iterations++; 
			int newSetSize = twoStepApproach(); 
			if(setSize == newSetSize){
				stayCounter++;
			}
			else{
				stayCounter = 0; 
			}
			setSize = newSetSize; 
		}
		System.out.println("Total iterations: " + iterations); 
		return setSize; 
	}
	
	public static void main(String[] args){
		List<Integer> results = new ArrayList<Integer>(); 
		for(int i = 0; i < 50; i++){
			System.out.println(i); 
			Metropolis m = new Metropolis(.5, 1000); 
			results.add(m.iterateTillConvergeOrMax(-1, 35)); 
		}
		int total = 0; 
		for(Integer i : results){
			total = total + i;
		}
		System.out.println(total/results.size()); 
		System.out.println(Arrays.toString(results.toArray()));
	}
}
