package com.brainfucker;

class BrainfuckRunner {

    private char[] data;

    private String program;

    private String args;

    // Points to current index in data
    private int pointerIndex;

    private String output;

    private static final int ARRAYSIZE = 30000;

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

    public BrainfuckRunner(String program){
	initialize();
	this.program = program;
    }

    public BrainfuckRunner(String program, String args){
	this(program);
	this.args = args;
    }

    private void initialize(){
	data = new char[ARRAYSIZE];
	pointerIndex = 0;
	output = "";
	args = null;
    }
    
    
}
