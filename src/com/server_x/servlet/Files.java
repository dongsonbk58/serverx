package com.server_x.servlet;

public class Files {
private String name;
private Boolean check;
private int countline;
private int lengthbegin;
private int lengthtail;

public int getCountline() {
	return countline;
}

public void setCountline(int countline) {
	this.countline = countline;
}

public Files( String name, Boolean check) {
	this.name=name;
	this.check=check;
}

public Files(String name, int countline, int lengthbegin, int lengthtail) {
	this.name = name;
	this.countline = countline;
	this.lengthbegin = lengthbegin;
	this.lengthtail = lengthtail;
}

public int getLengthbegin() {
	return lengthbegin;
}

public void setLengthbegin(int lengthbegin) {
	this.lengthbegin = lengthbegin;
}

public int getLengthtail() {
	return lengthtail;
}

public void setLengthtail(int lengthtail) {
	this.lengthtail = lengthtail;
}

public Files( String name, int countline) {
	this.name=name;
	this.countline=countline;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public Boolean getCheck() {
	return check;
}

public void setCheck(Boolean check) {
	this.check = check;
}
}
