package com.brainfucker;

import java.util.Collections;
import java.util.Comparator;

/*!
 * Comparison operator for the Fitnesses of Individual elements
 */
public class IndividualFitnessComparator {
	@Override
    public int compare(SampleProgram o1, SampleProgram o2) {
        return o1.fitness - o2.fitness;
    }
}