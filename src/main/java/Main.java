package com.brainfucker;

public class Main{
    public static void main(String[] args){
	Brainfucker bfr = new Brainfucker(args[0],
					  Integer.parseInt(args[1]),
					  Integer.parseInt(args[2]),
					  Double.parseDouble(args[3]),
					  Double.parseDouble(args[4])
					  );
	bfr.run();
    }
}
