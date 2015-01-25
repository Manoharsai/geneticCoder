package com.brainfucker;

import java.util.HashMap;
import java.util.Arrays;

public class Gene{

    public static final byte[] NEXT = {0,0,0};
    public static final byte[] PREV = {0,0,1};
    public static final byte[] INC = {0,1,1};
    public static final byte[] DEC = {1,1,1};
    public static final byte[] PRINT = {1,1,0};
    public static final byte[] INPUT = {1,0,1};
    public static final byte[] BRAC_LEFT = {0,1,0};
    public static final byte[] BRAC_RIGHT = {1,0,0};
    
    public static HashMap<byte[],Character> geneToChar;
    public static HashMap<Character,byte[]> charToGene;
    static {
	instanciateMaps();
	
    }

    private static void instanciateMaps(){
	geneToChar = new HashMap<byte[],Character>();
	geneToChar.put(NEXT, '>');
	geneToChar.put(PREV, '<');
	geneToChar.put(INC, '+');
	geneToChar.put(DEC, '-');
	geneToChar.put(PRINT, '.');
	geneToChar.put(INPUT, ',');
	geneToChar.put(BRAC_LEFT, '[');
	geneToChar.put(BRAC_RIGHT, ']');

	charToGene = new HashMap<Character,byte[]>();
	charToGene.put('>', NEXT);
	charToGene.put('<', PREV);
	charToGene.put('+', INC);
	charToGene.put('-', DEC);
	charToGene.put('.', PRINT);
	charToGene.put(',', INPUT);
	charToGene.put('[', BRAC_LEFT);
	charToGene.put(']', BRAC_RIGHT);
    }

    public static byte[] strToByte(String s){
	byte[] a = new byte[s.length()];
        for(int i = 0; i < s.length(); i++){
	    a[i] = (byte) Character.getNumericValue(s.charAt(i));
	}
	return a;
    }

    public static String byteToString(byte[] a){
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
	instanciateMaps();
	makeRandom();
	Arrays.toString(data);
    }
    
    public Gene(byte[] data){
	instanciateMaps();
	this.data = data;
	Arrays.toString(data);
    }

    public Gene(String s){
	instanciateMaps();
	this.data = strToByte(s);
	Arrays.toString(data);
    }

    public Gene(char c){
	instanciateMaps();
	this.data = charToGene.get(c);
	Arrays.toString(data);
    }

    public String getStrData(){
	String s = "";
	for(byte b : data){
	    s += "" +  b;
	}
	return s;
    }


    public void strSet(String s){
	data = strToByte(s);
    }

    /**
     * Sets data from a byte array
     */
    public void dataSet(byte[] a){
	data = a;
    }

    public void makeRandom(){
	data = new byte[3];
	for(int i = 0; i < 3; i++){
	    // Set each bit to one or zero randomly
	    data[i] = (byte) Math.floor( Math.random() * 2);
	}
    }
    
    public String toString(){
        return "" + geneToChar.get(data);
    }
    
}
