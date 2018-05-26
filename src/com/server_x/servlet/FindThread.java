package com.server_x.servlet;
import java.util.ArrayList;

public class FindThread implements Runnable {
	int k;
    private ArrayList<String> listrule;
    private String rule;
    private int start; // start index of search
    private int end;
    
    public FindThread(int i) {
            k = i;
    }

    public FindThread(int start, int end, ArrayList<String> listrule, String rule) {
    	this.start = start;
        this.end = end;
        this.listrule = listrule;
        this.rule = rule;
    }

	@Override
	public void run() {
		
	}
	

}
