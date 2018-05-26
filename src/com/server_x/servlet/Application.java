package com.server_x.servlet;

import java.io.Serializable;

/**
 * Created by cuongdx on 12/25/2016.
 */

public class Application  implements Serializable{

    int []listPermission;
    String name;
    boolean result;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public Application(int []listPermission, String name){
        this.listPermission=listPermission;
        this.name=name;
    }
    public int[] getListPermission() {
        return listPermission;
    }

    public String getName() {
        return name;
    }

    public void setListPermission(int[] listPermission) {
        this.listPermission = listPermission;
    }

    public void setName(String name) {
        this.name = name;
    }
}
