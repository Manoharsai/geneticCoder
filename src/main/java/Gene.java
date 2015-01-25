package com.brainfucker;

import java.util.HashMap;

public class Gene{

    public static final byte[] NEXT = {0,0,0};
    public static final byte[] PREV = {0,0,1};
    public static final byte[] INC = {0,1,1};
    public static final byte[] DEC = {1,1,1};
    public static final byte[] PRINT = {1,1,0};
    public static final byte[] INPUT = {1,0,1};
    public static final byte[] BRAC_LEFT = {0,1,0};
    public static final byte[] BRAC_RIGHT = {1,0,0};
    
    public static final HashMap<byte[],char> geneToChar;
    public static final HashMap<char,byte[]> charToGene;
    static {
	geneToChar = new HashMap<byte[],char>();
	geneToChar.add(NEXT, '>');
	geneToChar.add(PREV, '<');
	geneToChar.add(INC, '+');
	geneToChar.add(DEC, '-');
	geneToChar.add(PRINT, '.');
	geneToChar.add(INPUT, ',');
	geneToChar.add(BRAC_LEFT, '[');
	geneToChar.add(BRAC_RIGHT, ']');

	charToGene = new HashMap<char,byte[]>();
	charToGene.add('>', NEXT);
	charToGene.add('<', PREV);
	charToGene.add('+', INC);
	charToGene.add('-', DEC);
	charToGene.add('.', PRINT);
	charToGene.add(',', INPUT);
	charToGene.add('[', BRAC_LEFT);
	charToGene.add(']', BRAC_RIGHT);
	
    }

    public static void strToByte(String s){
	byte[] a = new byte[s.length()];
	for(int i = 0; i < s.length(); i++){
	    a[i] = Integer.parseInt(s.charAt(i));
	}
    }

    public static void byteToString(byte[] a){
	String s = "";
	for(byte b : a){
	    s += "" + b;
	}
	return s;
    }

    /**
     * Moves string data in gene objects
     * @param genome: the genes all in sequence in a string, must be divisible by 3
     */
    public static Gene[] strToGenes(String genome){
	Gene[] a = new Gene[genome.length() / 3];
	// Loop through by threes and make genes
	int index = 0;
	for(int i = 0; i < genome.length(); i += 3){
	    a[index] = new Gene(genome.substring(i, i + 3));
	    index++;
	}
	return a;
    }

    private byte[] data;

    /**
     * Creates a random gene
     */
    public Gene(){
	makeRandom();
    }
    
    public Gene(byte[] data){
	this.data = data;
    }

    public Gene(char c){
	data = charToGene.put(c);
    }


    public void strSet(String s){
	data = strToGenes(s);
    }

    /**
     * Sets data from a byte array
     */
    public void dataSet(byte[] a){
	data = a;
    }

    public void makeRandom(){
	for(int i = 0; i < 3; i++){
	    // Set each bit to one or zero randomly
	    data[i] = Math.floor( Math.Random() * 2);
	}
    }
    
    public String toString(){
        return "" + geneToChar.put(data);
    }
    
}
