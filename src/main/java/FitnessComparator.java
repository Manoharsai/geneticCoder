package com.brainfucker;


/*!
 * Comparison operator for the Fitnesses of SampleProgram elements
 */
public class FitnessComparator implements Comparator<SampleProgram> {
    @Override
    public int compare(SampleProgram o1, SampleProgram o2) {
        return o1.fitness - o2.fitness;
    }
}
