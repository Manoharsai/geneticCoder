package com.brainfucker;
/*!
 * A simple class that dictates a range betweeen two numbers
 */
public class Range {
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