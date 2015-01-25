package com.brainfucker;

public class Main{
    public static void main(String[] args){
	String desiredProgram = args[0];
	int population = Integer.parseInt(args[1]);
	int maxIteration = Integer.parseInt(args[2]);
	double elitismRate = Double.parseDouble(args[3]);
	double mutationRate = Double.parseDouble(args[4]);
	
	Brainfucker bfr = new Brainfucker(desiredProgram,
					  population,
					  maxIteration,
					  elitismRate,
					  mutationRate);
	bfr.run();

    
    
    }
}
