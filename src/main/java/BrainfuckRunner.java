package com.brainfucker;

import java.util.Arrays;

class BrainfuckRunner {

    private char[] data;

    private int argPtr;
    
    private String args;

    private String output;

    private static final int ARRAYSIZE = 10;

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
	argPtr = 0;
	args = "";
	output = "";
    }

    public String run(String program) throws Exception{
	clean();
	return run(program, 0);
    }

    private String run(String program, int dataPtr) throws Exception{
	for(int i = 0; i < program.length(); i++){
	    dataPtr = interperateChar(program.charAt(i), i, dataPtr, program);
	}
	return output;
    }

    private int interperateChar(
				 char c,
				 int executeCol,
				 int dataPtr,
				 String prog
				 ) throws Exception {
	switch(c){
	case Token.NEXT:
	    
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
		data[dataPtr]--;
	    } else {
		throw new Exception("Out of bounds on data:" + dataPtr + ", too small @" + executeCol);
	    }
	    break;
	case Token.PRINT:
	    output += "" + data[dataPtr];
	    break;
	case Token.INPUT:
	    data[dataPtr] = args.charAt(argPtr);
	    break;
	case Token.BRAC_LEFT:
	    if(data[dataPtr] > 1){
   		while(data[dataPtr] > 1){
		    int rightBracLoc = -1;
		    for(int i = prog.length() - 1; i > executeCol; i--){
			if(prog.charAt(i) == Token.BRAC_RIGHT){
			    rightBracLoc = i;
			    break;
			}
		    }
		    if(rightBracLoc == -1){
			// No matching bracket
			throw new Exception("Brackets are fucked @" + dataPtr);
		    }
		    // Right bracket found or exception thrown
		    // Run middle part
		    run(prog.substring(executeCol + 1, rightBracLoc), dataPtr);
		}
	    }
	    break;
	case Token.BRAC_RIGHT:
	    //Should never happen, fingers crossed
	    break;
	default:
	    throw new Exception("Invalid char @" + executeCol);  
	}

	return dataPtr;
    }
    
}
