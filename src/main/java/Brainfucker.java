package com.brainfucker;

public class Brainfucker{

    private String desiredOutput;
    private int maxIteration;
    private Population p;
    
    public Brainfucker(String desiredOutput,
		       int population,
		       int maxIteration,
		       double elitismRate,
		       double mutationRate
		       ){
	this.desiredOutput = desiredOutput;
	this.maxIteration = maxIteration;
	p = new Population(population,  elitismRate, mutationRate, desiredOutput);
	
    }

    public void run(){
	// Creates empty populations
	p.initPopulation();

	// Loop through each generations
	for(int i = 0; i < maxIteration; i++){
	    p.fitness();
	    p.sortPopulation();

	    System.out.println("iteration : " + i + " " + p.getBest());
	   
	    if(p.getTestPrograms().get(0).fitness == 0){
		break;
	    }

	    p.mate();

	    p.swap();
	    
	}

	BrainfuckRunner bfr = new BrainfuckRunner();
	System.out.println(bfr.run(p.getBest()));

    }

    
}
