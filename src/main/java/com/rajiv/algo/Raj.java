package com.rajiv.algo;

import java.util.HashSet;

public class Raj {

   
    public static void main(String[] args) {
    	System.out.println("before sync");
    	synchronized (args) {
    		System.out.println("inside sync before try");
			try {				
				System.out.println("inside sync before wait");
				args.wait();
				System.out.println("inside sync after wait ");
			} catch (InterruptedException e) {
				System.out.println("INtr ex");
			}
			System.out.println("inside sync after try");
		}
    }

}

class Job {
	public int value;
	public Job(int val) {
		this.value = val;
		
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return this.value == ((Job)obj).value;
	}
}
