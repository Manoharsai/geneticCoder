package com.brainfucker;

public class Brainfucker{

    private String desiredOutput;
    private int maxIteration;
    private GeneticSimple gs;
    
    public Brainfucker(String desiredOutput,
		       int population,
		       int maxIteration,
		       float elitismRate,
		       float mutationRate
		       ){
	this.desiredOutput = desiredOutput;
	this.maxIteration = maxIteration;
	gs = new GeneticSimple(population, maxIteration, elitismRate, mutationRate);
	
    }

    public void run(){
	// Creates empty populations
	gs.initPopulation();

	// Loop through each generations
	for(int i = 0; i < maxiteration; i++){
	    gs.fitness();
	    gs.sortPopulation();

	    System.out.println("iteration : " + i + "     ");

	    if(gs.getTestPrograms.get(0).fitness == 0){
		break;
	    }

	    gs.mate();

	    
	}

    }

    
}
