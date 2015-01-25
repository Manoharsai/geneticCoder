package com.brainfucker;

public class Brainfucker{

    private String desiredOutput;
    private int maxIteration;
    private GeneticSimple gs;
    
    public Brainfucker(String desiredOutput,
		       int population,
		       int maxIteration,
		       double elitismRate,
		       double mutationRate
		       ){
	this.desiredOutput = desiredOutput;
	this.maxIteration = maxIteration;
	gs = new GeneticSimple(population,  elitismRate, mutationRate, desiredOutput);
	
    }

    public void run(){
	// Creates empty populations
	gs.initPopulation();

	// Loop through each generations
	for(int i = 0; i < maxIteration; i++){
	    gs.fitness();
	    gs.sortPopulation();

	    System.out.println("iteration : " + i + " " + gs.getBest());
	   
	    if(gs.getTestPrograms().get(0).fitness == 0){
		break;
	    }

	    gs.mate();

	    
	}

    }

    
}
