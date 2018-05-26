package com.server_x.servlet;

import java.util.ArrayList;

public class RulesThread implements Runnable {

    private ArrayList<String> listrule;
    private int start;
    private int end;

    public RulesThread(int start, int end, ArrayList<String> listrule) {
        this.start = start;
        this.end = end;
        this.listrule = listrule;
    }

    @Override
    public void run() {

    }

}

