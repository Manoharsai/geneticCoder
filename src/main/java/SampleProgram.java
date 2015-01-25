package com.brainfucker;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.*;


public class SampleProgram {
	public char[] testProgram;

	public int failurePoint;

	public int fitness;

	public int length; 

	public String output = "";

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
	 * Remove Simple indefinate loops
	 */
	private void removeInfiniteLoops() {
		for (int i = 0; i < testProgram.length - 1; i++) {
			if (testProgram[i] == '[' && testProgram[i + 1] == ']') {
				char[] excludedChars = {'[', ']'};
				testProgram[i]     = randomValidChar(excludedChars);
				testProgram[i + 1] = randomValidChar(excludedChars);
			}
		}
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
		/*
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
		*/

		removeInfiniteLoops();
	}

	/*!
	 * Fills the string with random characters, based off of its length, defined by the range
	 * @param closeBrackets: Specifies whether the the loops should be closed automatically
	 */
	public void fillRandomly(boolean closeBrackets) {
		int numbStartBrackets = 0;
		int numbEndBrackets   = 1;

		while (numbStartBrackets != numbEndBrackets) {
			numbStartBrackets = 0;
			numbEndBrackets   = 0;
			
			int lastStartBracketPos = -1;

			char[] initialExclusion = {'[','<','.',',','-'};
			testProgram[0] = randomValidChar(initialExclusion);
			char lastChar  = testProgram[0];
			for (int i = 1; i < length; i++) {
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
		removeInfiniteLoops();
	}

	/*************** Debug ****************/
	public void printArray() {
		System.out.println(Arrays.toString(testProgram));
	}
	
	public String toString() {
		return getStringVal();
	}

 }
