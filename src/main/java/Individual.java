package com.brainfucker;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.*;

public class Individual implements Comparable{
    public Gene[] genes;
    
    public int fitness;

    public String output;
    
    
    /*!
     * Initializes the individual with chars in a given range
     * @param numbChars, a range of the number of chars will be converted to bytes thereafter
     */
    public Individual(Range numChars) {
	int arrayLength = numChars.start + (int) Math.floor(Math.random() * numChars.absDifference());
	
	genes = new Gene[arrayLength];
	
    for (int i = 0; i < arrayLength; i++) {
        genes[i] = new Gene();
    }
	
	fitness = -1;
    }
    
    /*!
     * Initializes with a prewritten string of code
     * @param code: The prewritten string of code
     */
    public Individual(String genome) {
    for (int i = 0; i < genes.length; i++) {
        genes[i] = new Gene(genome.charAt(i));
    }
	
	fitness = -1;
	
    }

    public int getFitness(){
	return this.fitness;
    }
    
    /*!
     * Flips a certain number of bits
     * @param numbBits: the number of bits, the bits are then randomly selected to be flipped in the bytes array
     */
    public void flipRandBits(int numbBits) {
        for (int i = 0; i < genes.length; i++) {
            genes[i].makeRandom();
        }
    }

    /*!
     * Supports some older code
     */
    public void mutate(int numbBits) {
        flipRandBits(numbBits);
    }

    /*!
     * Sets all the genes to random characters
     */
    public void fillRandomly() {
        flipRandBits(genes.length);
    }
    
    /*!
     * Returns the program converted form bytes as a conveniance
     */
    public String getStringVal() {
	String s = "";
	for(Gene g : genes){
	    s += g;
	}
	return s;
    }

    @Override
    public int compareTo(Object o){
	if( o instanceof Individual){
	    Individual i = (Individual) o;
	    if(i.getFitness() > this.getFitness()){
		return 1;
	    } else {
		return -1;
	    }
	} else {
	    return -1;
	}
    }
    
}
