package com.brainfucker;

class BrainfuckRunner {

    private char[] data;

    // Points to current index in data
    private int dataPtr;

    private int argPtr;

    // Excecution is at this colunm
    private int executeCol;

    private String prog;
    
    private String args;

    private String output;

    private static final int ARRAYSIZE = 30000;

    private static final int MAX_CHAR_NUM = 300;

    private static class Token {
	public static final char NEXT = '>';
	public static final char PREV = '<';
	public static final char INC = '+';
	public static final char DEC = '-';
	public static final char PRINT = '.';
	public static final char INPUT = ',';
	public static final char BRAC_LEFT = '[';
	public static final char BRAC_RIGHT = ']';
    }

    public BrainfuckRunner(){
	clean();
    }

    private void clean(){
	data = new char[ARRAYSIZE];
	dataPtr = 0;
	argPtr = 0;
	executeCol = 0;
	args = "";
	output = "";
    }

    public  String run(String program) throws Exception{
	
	for(int i = 0; i < prog.length(); i++){
	    interperateChar(prog.charAt(i), i);
	}
	return output;
    }

    private void interperateChar(char c, int progLoc) throws Exception {
	switch(c){
	case Token.NEXT:
	    System.out.println("t");
	    if(dataPtr + 1 < data.length){
		dataPtr++;
	    } else {
		throw new Exception("Out of bounds, too big @" + executeCol);
	    }
	    break;
	case Token.PREV:
	    if(dataPtr - 1 >= 0){
		dataPtr--;
	    } else {
		throw new Exception("Out of bounds, too small @" + executeCol);
	    }
	    break;
	case Token.INC:
	    if(data[dataPtr] < MAX_CHAR_NUM){
		data[dataPtr]++;
	    } else {
		throw new Exception("Out of bounds on data, too big @" + executeCol);
	    }
	    break;
	case Token.DEC:
	    if(data[dataPtr] > 0){

	    } else {
		throw new Exception("Out of bounds on data:" + dataPtr + ", too big @" + executeCol);
	    }
	    break;
	case Token.PRINT:
	    output += dataPtr;
	    break;
	case Token.INPUT:
	    data[dataPtr] = args.charAt(argPtr);
	    break;
	case Token.BRAC_LEFT:
	    if(data[dataPtr] > 0){
		//backup data ptr
		int dpt = dataPtr;
   		while(data[dataPtr] > 0){
		    int rightBracLoc = -1;
		    for(int i = prog.length(); i > progLoc; i--){
			if(prog.charAt(i) == Token.BRAC_RIGHT){
			    rightBracLoc = i;
			} else {
			    // No matching bracket
			    throw new Exception("Brackets are fucked @" + dataPtr);
			}
		    }
		    // Right bracket found or exception thrown
		    // Run middle part
		    for(int p = dataPtr + 1; p < rightBracLoc; p++){
			interperateChar(prog.charAt(p), p);
		    }
		    
		}
	    }
	    break;
	case Token.BRAC_RIGHT:
	    //Should never happen, fingers crossed
	    break;
	default:
	    throw new Exception("Invalid char @" + executeCol);
	    	}
    }
    
}
