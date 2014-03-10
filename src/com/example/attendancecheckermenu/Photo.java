package com.example.attendancecheckermenu;

public class Photo {

	private String dateTaken;
	private String pathOfFile;
	private String stdNum;
	private String stdName;
	private String className;

	public String getDateTaken() {
		return dateTaken;
	}
	public void setDateTaken(String dateTaken) {
		this.dateTaken = dateTaken;
	}

	public String getStdNum() {
		return stdNum;
	}
	public void setStdNum(String stdNum) {
		this.stdNum = stdNum;
	}
	public String getPathOfFile() {
		return pathOfFile;
	}
	public void setPathOfFile(String pathOfFile) {
		this.pathOfFile = pathOfFile;
	}
	public String getStdName() {
		return stdName;
	}
	public void setStdName(String stdName) {
		this.stdName = stdName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
}
