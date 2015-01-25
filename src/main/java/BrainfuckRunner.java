package com.brainfucker;

import java.util.Arrays;
import java.util.Stack;

public class BrainfuckRunner {

    private char[] data;

    private int argPtr;
    
    private String args;

    private String output;

    private static final int ARRAYSIZE = 300;

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

    public String run(String program){
	clean();
	try{
	    return run(program, 0);
	} catch(Exception e){
	    return e.getMessage();
	}
    }

    private String run(String program, int dataPtr) throws Exception{
	for(int i = 0; i < program.length(); i++){
	    dataPtr = interperateChar(program.charAt(i), i, dataPtr, program);
	}
	return output;
    }

    public int findOtherBracket(String prog, int openingBrac) throws Exception{
	// Create new stack to start process
	Stack<Character> s = new Stack();
	
	// keep track of tailing bracket
	int lastRightBrack = openingBrac;

	for(int i = openingBrac; i < prog.length(); i++){
	    if(prog.charAt(i) == '['){
		// Put opening brac in stack without checking
		s.push('[');
	    }
	    if (prog.charAt(i) == ']'){
		// Get char from top of stack
		char last = s.pop();
		// If it does not match, there is a problem
		if(last == ']'){
		    throw new Exception("Too many ]");
		}
		//Check if empty and return value
		if(s.isEmpty()){
		    // This is the spot
		    return i;
		}
	    }
	}

	// Extra [ if here
	throw new Exception("Too many [");
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
		    try {
			rightBracLoc = findOtherBracket(prog, executeCol);
		    } catch (Exception e) {
			// No matching bracket
			throw new Exception("Brackets are fucked @" + executeCol);
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
