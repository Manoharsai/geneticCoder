package com.brainfucker;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.*;

public class GeneticSimple {
	private List<SampleProgram> testPrograms;
	private List<SampleProgram> buffer;

	private int population;
	private double elitismRate;
	private double mutationRate;
	private String desiredOutput;
    
    private static final Range RANGE = new Range(27,30);

    public GeneticSimple(int population, double elitismRate, double mutationRate, String desiredOutput) {
       
		this.population    = population;
		this.elitismRate   = elitismRate;
		this.mutationRate  = mutationRate;	
		this.desiredOutput = desiredOutput; 
	
		
		/*!
		 * Sets up the testPrograms and the buffer with default values, random for programs and empty for buffer
		 */
		testPrograms = new ArrayList<SampleProgram>(population);
		buffer       = new ArrayList<SampleProgram>(population);

	   
	}

	public List<SampleProgram> getTestPrograms() {
		return testPrograms;
	}


	/*!
	 * Initializes the population and the buffer, though the buffer is merely empty
	 */
	public void initPopulation() {
		for (int i = 0; i < population; i++) {
			SampleProgram temp = new SampleProgram(RANGE);
			testPrograms.add(temp);
			buffer.add(temp);
		}
		
		
	}

	/*!
	 * Swaps the population and the buffer
	 */
	public void swap() {
	    testPrograms = buffer;
	    buffer = new ArrayList<SampleProgram>();
	    	for (int i = 0; i < population; i++) {
			SampleProgram temp = new SampleProgram(RANGE);
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
		}
		return numberElites;
	}

	/*!
	 * Sorts the programs based off of their fitness
	 */
	public void sortPopulation() {
		Collections.sort(testPrograms, new FitnessComparator());
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
			
			String output = bRRunner.run(testPrograms.get(i).getStringVal());

			//	System.out.println(testPrograms.get(i).getStringVal() + "  ===  " + output);

			//Pattern pattern = Pattern.compile("@\\s*(\\d+)");
			//Matcher matcher = pattern.matcher(output);

			Pattern loopPattern = Pattern.compile("infinite\\sloop",Pattern.CASE_INSENSITIVE);
			Matcher loopMatcher = loopPattern.matcher(output);
			
			if (loopMatcher.find()) {
			    testPrograms.get(i).fillRandomly(true);
			    fitness = errorVal;
						  
			}else if (output.contains("<=$:$=>")) {
				fitness += errorVal;
			} else {
				for (int foo = 0; foo < output.length(); foo++) {
				    int charVal = (foo >= desiredOutput.length()) ? 0 : desiredOutput.charAt(foo);
					int difference = charVal - output.charAt(foo);
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

			SampleProgram parent1 = testPrograms.get(a);
			SampleProgram parent2 = testPrograms.get(a + 1);

			char[] code1 = parent1.testProgram;
			char[] code2 = parent2.testProgram;

			//length of the new code is a number between the other two 
			int codeLen  = code1.length + (int) Math.floor( Math.random() * (code2.length - code1.length));

			char[] newCode1 = new char[codeLen];
			char[] newCode2 = new char[codeLen];
 
			//the breakpoint where the codes are spliced
			int breakpoint = (int) Math.floor( (double) codeLen * Math.random() );

			while (breakpoint >= code1.length || breakpoint >= code2.length) {
				breakpoint = (int) Math.floor( (double) codeLen * Math.random() );
			}

			//sets the values to there initial values til the breakpoint
			for (int i = 0; i < breakpoint; i++) {
				newCode1[i] = code1[i];
				newCode2[i] = code2[i];
			}

			//splits the code
			for (int i = breakpoint; i < codeLen; i++) {
				newCode1[i] = (i < code2.length) ? code2[i] : code1[i];
				newCode2[i] = (i < code1.length) ? code1[i] : code2[i];
			}

			SampleProgram child1 = new SampleProgram(newCode1);
			SampleProgram child2 = new SampleProgram(newCode2);

			//calculate the mutation
			if (Math.random() < mutationRate) {
			    child1.fillRandomly(true);
			} 
			if (Math.random() < mutationRate) {
			    child2.fillRandomly(true);
			} 

			buffer.set(a, child1);
			buffer.set(a + 1, child2);
		}

	}

	/*!
	 * Prints the best program of the set 
	 */
	public String getBest() {
		return testPrograms.get(0).getStringVal() + "    fitness: " + testPrograms.get(0).fitness;
	}

}
