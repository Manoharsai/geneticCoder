package com.brainfucker;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.*;


class Information {
	public static final char[] validBFChars = {'+', '-', '.', ',', '<', '>','[',']'};
}

/*!
 * A simple class that dictates a range betweeen two numbers
 */
class Range {
	int start;
	int end;

	public int absDifference() {
		int initial = end - start;
		return (initial < 0) ? -initial : initial;
	}

	public Range(int start, int end) {
		this.start = start;
		this.end   = end;
	}

}

class SampleProgram {
	public char[] testProgram;

	public int failurePoint;

	public int fitness;

	public int length; 

	/*!
	 * Indicates the likelehood that a + is followed by a +, conversly that a - is followed by a -, or that a > is followed by a >
	 */
	public double probSimilar = 0.95;

	public SampleProgram(Range numbRange) {
		/*!
		 * Randomizes the length of the string
		 */
		length = numbRange.start + (int) Math.floor(Math.random() * (double) numbRange.absDifference());
		
		this.testProgram = new char[length];
		
		fillRandomly(true);

		fitness = 1000;
	}

	public SampleProgram(char[] code) {
		length = code.length;

		fitness = 1000;

		testProgram = code;

	}


	public String getStringVal() {
		return String.valueOf(testProgram);
	}

	/*!
	 * Generates a random character from the validBFChars array
	 */
	public char randomValidChar() {
		int index = (int) Math.floor(Math.random() * Information.validBFChars.length);
		return Information.validBFChars[index];
	}

	/*!
	 * @param excludeChars: Characters that should not be matched
	 */
	public char randomValidChar(char[] excludeChars) {
		int index  = (int) Math.floor(Math.random() * Information.validBFChars.length);
		char value = Information.validBFChars[index];

		for (char ec : excludeChars) {
			if (value == ec) {
				value = randomValidChar(excludeChars);
				break;
			}
		}
		return value;

	}

	/*!
	 * Calculates the last number of  open brackets that were not closed
	 * @return the number of non closed brackets, is negative if there are more closing than opening
	 */
	private int numbNonClosedBrackets() {
		int numOpenBrackets       = 0;
		int numbClosedBrackets = 0;

		for (int i = 0; i < testProgram.length; i++) {
			if (testProgram[i] == '[') {
				numOpenBrackets++;
			} else if (testProgram[i] == ']') {
				numbClosedBrackets++;
			}
		}

		return numOpenBrackets - numbClosedBrackets;
	}
	

	/*!
	 * Mutates the program 
	 * @param maxNumbMutations: The number of mutations that can occur in the string
	 * @param grouped: Weather all the mutations should be grouped in a certain spot or should be random throught the string 
	 */
	public void mutate(int maxNumbMutations, boolean grouped) {
		if (grouped) {
			int start = (int) Math.floor(Math.random() * (double) testProgram.length);
			int numberMutations = (int) Math.floor( Math.random() * (double) maxNumbMutations );

			if (numberMutations + start >= testProgram.length - 1) {
				numberMutations = testProgram.length - 1 - start;
			}

			for (int i = start; i < (start + numberMutations); i++) {
				testProgram[i] = randomValidChar();
			}

		} else {
			int numberMutations = (int) Math.floor( Math.random() * (double) maxNumbMutations );
			for (int i = 0; i < maxNumbMutations; i++) {
				int index = (int) Math.floor(Math.random() * testProgram.length);
				testProgram[index] = randomValidChar();
			}
		}
		int numBrackets = numbNonClosedBrackets();

		if (numBrackets > 0 ) {
			int posLast = 0;
			for (int i = testProgram.length - 1; i >=0; i--) {
				if (testProgram[i] == '[') {
					posLast = i;
					break;
				}
			}

			//now have a range to put stuff between posLast and the end of the string
			for (int i = 0; i < numBrackets; i++) {
				int randomPos = posLast + (int) Math.floor(Math.random() * (double) (testProgram.length - posLast));
				testProgram[randomPos] = ']';
			}
		} else if (numBrackets < 0) {
			int posLast = 0;
			for (int i = 0; i < testProgram.length; i++) {
				if (testProgram[i] == ']') {
					posLast = i;
					break;
				}
			}

			//now have a range to put stuff between posLast and the end of the string
			for (int i = 0; i < numBrackets; i++) {
				int randomPos = (int) Math.floor(Math.random() * posLast);
				testProgram[randomPos] = '[';
			}
		}

	}

	/*!
	 * Fills the string with random characters, based off of its length, defined by the range
	 * @param closeBrackets: Specifies whether the the loops should be closed automatically
	 */
	public void fillRandomly(boolean closeBrackets) {
		int numbStartBrackets = 0;
		int numbEndBrackets   = 1;

		char lastChar         = 'A';
		while (numbStartBrackets != numbEndBrackets) {
			numbStartBrackets = 0;
			numbEndBrackets   = 0;
			
			int lastStartBracketPos = -1;
			for (int i = 0; i < length; i++) {
				char newChar = randomValidChar();
				if (closeBrackets) {
					if (numbStartBrackets <= numbEndBrackets) {
						char[] excluded = {']'};
						newChar = randomValidChar(excluded);
					}


					if (lastChar == '[') {
						char[] excluded = {']', '['};
						newChar = randomValidChar(excluded);
					} else if (lastChar == '-' && newChar == '+') {
						double shouldHappen = Math.random();
						if (shouldHappen < probSimilar) {
							newChar = '-';
						}
					} else if (lastChar == '+' && newChar == '-') {
						double shouldHappen = Math.random();
						if (shouldHappen < probSimilar) {
							newChar = '+';
						}
					} else if (lastChar == '>' && newChar == '<') {
						double shouldHappen = Math.random();
						if (shouldHappen < probSimilar) {
							newChar = '>';
						}
					} else if (lastChar == '<' && newChar == '>') {
						double shouldHappen = Math.random();
						if (shouldHappen < probSimilar) {
							newChar = '<';
						}
					} else if (lastChar == '.' && newChar == '.') {
						char[] excluded = {'.'};
						newChar = randomValidChar(excluded);
					}



					if (newChar == '[') {
						numbStartBrackets++;
						lastStartBracketPos = i;
					} else if (newChar == ']') {
						numbEndBrackets++;
						
						if (closeBrackets && lastStartBracketPos != -1) {
							boolean isNeg = false;
							for (int foo = lastStartBracketPos; foo < i; foo++) {
								if (testProgram[foo] == '-') {
									isNeg = true;
								}
							}
							if (!isNeg) {
								int positionSwap = lastStartBracketPos + (int) Math.floor(Math.random() * (double) (i - lastStartBracketPos));
								testProgram[positionSwap] = '-';
							}
							
							lastStartBracketPos = -1;
						}
					}
				}
			testProgram[i] = newChar;
			lastChar 	   = newChar;
			}
		}
	}

	/*************** Debug ****************/
	public void printArray() {
		System.out.println(Arrays.toString(testProgram));
	}
	
	public String toString() {
		return getStringVal();
	}

 }


/*!
 * Comparison operator for the 
 */
class FitnessComparator implements Comparator<SampleProgram> {
    @Override
    public int compare(SampleProgram o1, SampleProgram o2) {
        return o1.fitness - o2.fitness;
    }
}

class GeneticSimple {
	private List<SampleProgram> testPrograms;
	private List<SampleProgram> buffer;

	private int population;
	private float elitismRate;
	private float mutationRate;

	public GeneticSimple(int population, int maxIteration, float elitismRate, float mutationRate) {
		this.population   = population;
		this.elitismRate  = elitismRate;
		this.mutationRate = mutationRate;	
	
		
		/*!
		 * Sets up the testPrograms and the buffer with default values, random for programs and empty for buffer
		 */
		testPrograms = new ArrayList<SampleProgram>(population);
		buffer       = new ArrayList<SampleProgram>(population);

	    
	    initPopulation();
	    
	    /*!
	     * Handels the actual running of the genetic algorithm element
	     */
	    for (int i = 0; i < maxIteration; i++) {
	        fitness();
	        sortPopulation();
	
			System.out.print("iteration : " + i + "       ");
	        printBest();
	        
	        if (testPrograms.get(0).fitness == 0 ) {
	            break;
	        }
	        
	        mate();
	        
	        swap();
	    }
	}

	/*!
	 * Initializes the population and the buffer, though the buffer is merely empty
	 */
	public void initPopulation() {
		for (int i = 0; i < population; i++) {
			SampleProgram temp = new SampleProgram(new Range(10, 30));
			testPrograms.add(temp);
			buffer.add(temp);
		}
		
		
	}

	/*!
	 * Swaps the population and the buffer
	 */
	public void swap() {
		List<SampleProgram> temp = new ArrayList<SampleProgram>(testPrograms);

		testPrograms = new ArrayList<SampleProgram>();

		for (SampleProgram elem : buffer) {
			testPrograms.add(elem);
		}

		buffer = temp;
	}

	/*!
	 * Passes the Elites on to the buffer, requires the array be sorted in order to work
	 * @return the number of elites used
	 */
	public int CalcElitism() {
		int numberElites = (int) (elitismRate * (float) population);
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
		for (int i = 0; testPrograms.size() > i; i++) {
			//sample regex
			Pattern pattern = Pattern.compile("[\\w\\s]*(\\w)\\s*@\\s*(\\d+)");
			Matcher matcher = pattern.matcher(testPrograms.get(i).getStringVal());

			//implement!
			
			testPrograms.get(i).fitness = (int) Math.floor(Math.random() * (double) 100) + 2;
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

		for (int a = numbElites; a < testPrograms.size(); a += 2) {

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
				child1.mutate(10, false);
			} 
			if (Math.random() < mutationRate) {
				child2.mutate(10, false);
			} 

			buffer.set(a, child1);
			buffer.set(a + 1, child2);
		}

	}

	/*!
	 * Prints the best program of the set 
	 */
	public void printBest() {
		System.out.println(testPrograms.get(0).getStringVal() + "    fitness: " + testPrograms.get(0).fitness);
	}

}