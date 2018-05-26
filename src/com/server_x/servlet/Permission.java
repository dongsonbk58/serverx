package com.server_x.servlet;

/**
 * Created by cuongdx on 11/18/2016.
 */
public class Permission {
    public String name;
    public int id;



    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Permission() {

    }

    public Permission(int id, String name) {
        this.id = id;
        this.name = name;
    }

}
