package com.brainfucker;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.*;

public class Individual {
	public byte[] bytes;

	public int fitnees;


	/*!
	 * Initializes the individual with chars in a given range
	 * @param numbChars, a range of the number of chars will be converted to bytes thereafter
	 */
	public Individual(Range numChars) {
		int arrayLength = numChars.start + (int) Math.floor(Math.random() * numChars.absDifference());
		arrayLength *= 3; //convert to byte values

		bytes = new byte[arrayLength];

		flipRandBits(bytes.length);

		fitnees = -1;
	}

	/*!
	 * Initializes with a prewritten string of code
	 * @param code: The prewritten string of code
	 */
	public Individual(String code) {
		bytes = Gene.strSet(code);

		fitness = -1;

	}

	/*!
	 * Flips a certain number of bits
	 * @param numbBits: the number of bits, the bits are then randomly selected to be flipped in the bytes array
	 */
	public void flipRandBits(int numbBits) {
		int actNumbBits = numbBits;

		for (int i = 0; i < actNumbBits; i++) {
			int pos = (int) Math.floor(Math.random() * bytes.length);
			bytes[pos] = (bytes[pos] == 1) ? 0 : 1;
		}
	}

	/*!
	 * Returns the program converted form bytes as a conveniance
	 */
	public String getStringVal() {
		return Gene.toString(bytes);
	}

}