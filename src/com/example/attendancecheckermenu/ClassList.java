package com.example.attendancecheckermenu;

public class ClassList{

	private String studentNumber;
	private String studentName;
	private String className;
	private String lectureSection;
	private String section;
	private String studentPicPath;
	private int numOfAbsences;
	private boolean excessive;
	private String dates_absent;
	
	public String getStudentNumber() {
		return studentNumber;
	}
	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public String getStudentPicPath() {
		return studentPicPath;
	}
	public void setStudentPicPath(String studentPicPath) {
		this.studentPicPath = studentPicPath;
	}
	public int getNumOfAbsences() {
		return numOfAbsences;
	}
	public void setNumOfAbsences(int numOfAbsences) {
		this.numOfAbsences = numOfAbsences;
	}
	public boolean isExcessive() {
		return excessive;
	}
	public void setExcessive(boolean excessive) {
		this.excessive = excessive;
	}
	public String getDates_absent() {
		return dates_absent;
	}
	public void setDates_absent(String dates_absent) {
		this.dates_absent = dates_absent;
	}
	public String getLectureSection() {
		return lectureSection;
	}
	public void setLectureSection(String lectureSection) {
		this.lectureSection = lectureSection;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}	
	
}