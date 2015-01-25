package com.brainfucker;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.*;

public class Population {
	private List<Individual> testPrograms;
	private List<Individual> buffer;

	private int population;
	private double elitismRate;
	private double mutationRate;
	private String desiredOutput;
    
    private static final Range RANGE = new Range(40,120);

    public Population(int population, double elitismRate, double mutationRate, String desiredOutput) {
       
		this.population    = population;
		this.elitismRate   = elitismRate;
		this.mutationRate  = mutationRate;	
		this.desiredOutput = desiredOutput; 
	
		
		/*!
		 * Sets up the testPrograms and the buffer with default values, random for programs and empty for buffer
		 */
		testPrograms = new ArrayList<Individual>(population);
		buffer       = new ArrayList<Individual>(population);

	   
	}

	public List<Individual> getTestPrograms() {
		return testPrograms;
	}


	/*!
	 * Initializes the population and the buffer, though the buffer is merely empty
	 */
	public void initPopulation() {
		for (int i = 0; i < population; i++) {
			Individual temp = new Individual(RANGE);
			testPrograms.add(temp);
			buffer.add(temp);
		}
		
		
	}

	/*!
	 * Swaps the population and the buffer
	 */
	public void swap() {
	    testPrograms = buffer;
	    buffer = new ArrayList<Individual>();
	    	for (int i = 0; i < population; i++) {
			Individual temp = new Individual(RANGE);
			buffer.add(temp);
		}
	}

	/*!
	 * Passes the Elites on to the buffer, requires the array be sorted in order to work
	 * @return the number of elites used
	 */
	public int CalcElitism() {
		int numberElites = (int) (elitismRate * (double) population);
		for (int i = 0; i < numberElites; i++) {
			buffer.set(i, testPrograms.get(i));
			buffer.get(i).mutate(2);
		}
		return numberElites;
	}

	/*!
	 * Sorts the programs based off of their fitness
	 */
	public void sortPopulation() {
		Collections.sort(testPrograms, new IndividualFitnessComparator());
	}

	/*!
	 * Determines the fitness of each individual in the testPrograms array
	 */
	public void fitness() {
		int errorVal = 10000;
		BrainfuckRunner bRRunner = new BrainfuckRunner();
		for (int i = 0; testPrograms.size() > i; i++) {
			int fitness = 0;
			//sample regex
			
			String output = bRRunner.run(Gene.toString(testPrograms.get(i).bytes));

			testPrograms.get(i).output = output;


			Pattern loopPattern = Pattern.compile("infinite\\sloop",Pattern.CASE_INSENSITIVE);
			Matcher loopMatcher = loopPattern.matcher(output);
			
			if (loopMatcher.find()) {
			    testPrograms.get(i).fillRandomly(true);
			    fitness = errorVal;
						  
			}else if (output.contains("<=$:$=>")) {
				fitness += errorVal;
			} else {
				for (int foo = 0; foo < desiredOutput.length(); foo++) {
				    int charValDesired    = desiredOutput.charAt(foo);
				    int charValActual     = (foo >= output.length()) ? 0 : output.charAt(foo);
					int difference 		  = charValDesired - charValActual;

					fitness += (difference < 0) ? -difference : difference;
				}
			}




			//implement!
			
			testPrograms.get(i).fitness = fitness;
		}


	}

	/*!
	 * Mates members of the population and adds them into the buffer
	 * Mating approach is to cut the program in half and swap it out with another program
	 * Activates elitism
	 * Activates mutation of elemnents
	 */
	public void mate() {
		int numbElites = CalcElitism();

		for (int a = numbElites; a < testPrograms.size() - 1; a += 2) {

			Individual parent1 = testPrograms.get(i);
			Individual parent2 = testPrograms.get(i + 1);

			String shortCode   = (parent1.bytes.length < parent2.bytes.length) ? Gene.toString(parent1.bytes) : Gene.toString(parent2.bytes);
			String longcode    = (parent1.bytes.length > parent2.bytes.length) ? Gene.toString(parent1.bytes) : Gene.toString(parent2.bytes);

			int breakpoint     = (int) ((double) shortCode.length() * 1 / 4.0) + (Math.random() * (double) shortCode.length() * 3.0 / 4.0);

			while (breakpoint % 3 != 0) {
				breakpoint     = (int) ((double) shortCode.length() * 1 / 4.0) + (Math.random() * (double) shortCode.length() * 3.0 / 4.0);
			}

			String codeOne     = shortCode.substring(0, breakpoint - 1) + longcode.substring(breakpoint, longcode.length() - 1);
			String codeTwo     = longcode.substring(0, breakpoint - 1) + shortCode.substring(breakpoint, longcode.length() - 1);

			buffer.set(a, new Individual(codeOne));
			buffer.set(a + 1, new Individual(codeTwo));
		}

	}

	/*!
	 * Prints the best program of the set 
	 */
	public String getBest() {
		return testPrograms.get(0).getStringVal() + "    fitness: " + testPrograms.get(0).fitness + "     output :" + testPrograms.get(0).output;
	}

}
