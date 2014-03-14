package com.example.attendancecheckermenu;

import java.lang.reflect.Array;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class AttendanceListDAO {
	
	private AttendanceDbHelper dbHelper;
	private SQLiteDatabase db;
	private final String[] AttendanceListCol = {
			AttendanceDbHelper.COL_STDNUM,
			AttendanceDbHelper.COL_STDNAME,
			AttendanceDbHelper.COL_CLASS_NAME,
			AttendanceDbHelper.COL_LEC_SECTION,
			AttendanceDbHelper.COL_SECTION,
			AttendanceDbHelper.COL_STDPIC,
			AttendanceDbHelper.COL_NUM_ABSENCES,
			AttendanceDbHelper.COL_EXCESSIVE,
			AttendanceDbHelper.COL_DATES_ABSENT,
			AttendanceDbHelper.COL_HAS_TAKEN_PICTURE
	}; 
	private Context c;
	
	public AttendanceListDAO(Context context)
	{
		dbHelper = new AttendanceDbHelper(context);
		c = context;
	}
	
	public void open() throws SQLException
	{
		this.db = dbHelper.getWritableDatabase();
	}
	
	public void close()
	{
		dbHelper.close();
	}
	
	public long insertClassListToDb(String studentNumber,String studentName,String className,String lectureSection,String section) throws SQLException
	{
		ContentValues values = new ContentValues();
		values.put(AttendanceListCol[0], studentNumber);
		values.put(AttendanceListCol[1], studentName);
		values.put(AttendanceListCol[2], className);
		values.put(AttendanceListCol[3], lectureSection);
		values.put(AttendanceListCol[4], section);
		values.put(AttendanceListCol[5], "nopic");
		values.put(AttendanceListCol[6], 0);
		values.put(AttendanceListCol[7], "false");
		values.put(AttendanceListCol[8], "none");
		values.put(AttendanceListCol[9], "false");
		long insertClassList = db.insert(AttendanceDbHelper.DB_TABLE_CLASS_LIST, null, values);
		
		return insertClassList;
	}
	
	public ArrayList<String> viewClassListPerSection(String className,String section){
		ArrayList<String> students = new ArrayList<String>();
		String[] studentCol = {AttendanceDbHelper.COL_STDNAME};
		
		
		Cursor c = db.query(AttendanceDbHelper.DB_TABLE_CLASS_LIST, 
				studentCol, AttendanceDbHelper.COL_CLASS_NAME+" = '"+className+"' and "+
						AttendanceDbHelper.COL_SECTION+" = '"+section+"'",
						null,
						null, null, null);
		
		c.moveToFirst();
		
		while(!c.isAfterLast()){
			students.add(c.getString(0));
			c.moveToNext();
		}
		
		return students;
		
	}
	
	public ArrayList<String> viewClassListPerSectionStdNum(String className,String section){
		ArrayList<String> students = new ArrayList<String>();
		String[] studentCol = {AttendanceDbHelper.COL_STDNUM};
		
		
		Cursor c = db.query(AttendanceDbHelper.DB_TABLE_CLASS_LIST, 
				studentCol, AttendanceDbHelper.COL_CLASS_NAME+" = '"+className+"' and "+
						AttendanceDbHelper.COL_SECTION+" = '"+section+"'",
						null,
						null, null, null);
		
		c.moveToFirst();
		
		while(!c.isAfterLast()){
			students.add(c.getString(0));
			c.moveToNext();
		}
		
		return students;
		
	}

	
	public ArrayList<ClassList> viewClassListFromClass(String className){
		ArrayList<ClassList> ClassList = new ArrayList<ClassList>();
		Cursor c = db.query(AttendanceDbHelper.DB_TABLE_CLASS_LIST, AttendanceListCol, AttendanceDbHelper.COL_CLASS_NAME+" = '"+className+"'", null,null,null,null);
		c.moveToFirst();
		
		while(!c.isAfterLast())
		{
			
			ClassList cl = new ClassList();
			cl.setStudentNumber(c.getString(0));
			cl.setStudentName(c.getString(1));
			cl.setClassName(c.getString(2));
			cl.setLectureSection(c.getString(3));
			cl.setSection(c.getString(4));
			cl.setStudentPicPath(c.getString(5));
			cl.setNumOfAbsences(c.getInt(6));
			cl.setExcessive(Boolean.parseBoolean(c.getString(7)));
			cl.setDates_absent(c.getString(8));
			cl.setHasPictureTaken(Boolean.parseBoolean(c.getString(9)));
			ClassList.add(cl);
			c.moveToNext();
		}
		
		c.close();
		return ClassList;
	}
	
	public ClassList viewClassListFromClassPerName(String studentName,String className){
		Cursor c = db.query(AttendanceDbHelper.DB_TABLE_CLASS_LIST, AttendanceListCol, 
				AttendanceDbHelper.COL_STDNAME+" = '"+studentName+"' and "+
				AttendanceDbHelper.COL_CLASS_NAME+" = '"+className+"'", 
				null,null,null,null);
			
		c.moveToFirst();
			
		ClassList cl = new ClassList();
		cl.setStudentNumber(c.getString(0));
		cl.setStudentName(c.getString(1));
		cl.setClassName(c.getString(2));
		cl.setLectureSection(c.getString(3));
		cl.setSection(c.getString(4));
		cl.setStudentPicPath(c.getString(5));
		cl.setNumOfAbsences(c.getInt(6));
		cl.setExcessive(Boolean.parseBoolean(c.getString(7)));
		cl.setDates_absent(c.getString(8));
		cl.setHasPictureTaken(Boolean.parseBoolean(c.getString(9)));
		
		c.close();
		return cl;
	}
	
	public void studentTakesPic(String className, String studentName){
		ContentValues cv = new ContentValues();
		cv.put(AttendanceDbHelper.COL_HAS_TAKEN_PICTURE, "true");
		db.update(AttendanceDbHelper.DB_TABLE_CLASS_LIST, cv, 
				AttendanceDbHelper.COL_CLASS_NAME+" = '"+className+"' and "+
				AttendanceDbHelper.COL_STDNAME+" = '"+studentName+"'"
				, null);
	}
	
	public String getPicTaken(String className,String studentName){
		String[] sectionCol = {AttendanceDbHelper.COL_HAS_TAKEN_PICTURE};
		Cursor c = db.query(true, AttendanceDbHelper.DB_TABLE_CLASS_LIST, sectionCol, 
				AttendanceDbHelper.COL_CLASS_NAME+" = '"+className+"' and "+
				AttendanceDbHelper.COL_STDNAME+" = '"+studentName+"'",
				null, null, null, null, null);
		
		c.moveToFirst();
		
		return c.getString(0);
	};
	
	public void setNoPicToAbsent(String className,String student){
		ContentValues cv = new ContentValues();
		cv.put(AttendanceDbHelper.COL_HAS_TAKEN_PICTURE, "false");
		
		db.update(AttendanceDbHelper.DB_TABLE_CLASS_LIST, cv, 
				AttendanceDbHelper.COL_CLASS_NAME+" = '"+className+"' and "+
				AttendanceDbHelper.COL_STDNAME+" = '"+student+"'"
				, null);
	}
	
	public ArrayList<String> viewAllSections(String className){
		ArrayList<String> sections = new ArrayList<String>();
		String[] sectionCol = {AttendanceDbHelper.COL_SECTION};
		Cursor c = db.query(true, AttendanceDbHelper.DB_TABLE_CLASS_LIST, sectionCol, AttendanceDbHelper.COL_CLASS_NAME+" = '"+className+"'", null, null, null, null, null);
		
		c.moveToFirst();
		
		while(!c.isAfterLast()){
			sections.add(c.getString(0));
			Log.d("MainActivity",c.getString(0));
			c.moveToNext();
		}
		
		return sections;
		
	}
	
	public void resetToFalse(String className){			
			ContentValues cv = new ContentValues();
			cv.put(AttendanceDbHelper.COL_HAS_TAKEN_PICTURE, "false");
			db.update(AttendanceDbHelper.DB_TABLE_CLASS_LIST, cv, AttendanceDbHelper.COL_CLASS_NAME+" = '"+className+"'", null);
	}
	
	public ArrayList<ClassList> viewClassList() 
	{
		ArrayList<ClassList> ClassList = new ArrayList<ClassList>();
		Cursor c = db.query(AttendanceDbHelper.DB_TABLE_CLASS_LIST, AttendanceListCol, null, null,null,null,null);
		c.moveToFirst();
		
		while(!c.isAfterLast())
		{
			
			ClassList cl = new ClassList();
			cl.setStudentNumber(c.getString(0));
			cl.setStudentName(c.getString(1));
			cl.setClassName(c.getString(2));
			cl.setLectureSection(c.getString(3));
			cl.setSection(c.getString(4));
			cl.setStudentPicPath(c.getString(5));
			cl.setNumOfAbsences(c.getInt(6));
			cl.setExcessive(Boolean.parseBoolean(c.getString(7)));
			cl.setDates_absent(c.getString(8));
			cl.setHasPictureTaken(Boolean.parseBoolean(c.getString(7)));
			ClassList.add(cl);
			c.moveToNext();
		}
		
		c.close();
		return ClassList;
		
	}

	public ContentValues addAbsent(String studentName, String className, int num) {
		// TODO Auto-generated method stub
		String[] numAbsent = {
				AttendanceDbHelper.COL_NUM_ABSENCES,
				AttendanceDbHelper.COL_EXCESSIVE
		
		};
		Cursor c = db.query(true, AttendanceDbHelper.DB_TABLE_CLASS_LIST, numAbsent, 
				AttendanceDbHelper.COL_CLASS_NAME+" = '"+className+"' and "+
				AttendanceDbHelper.COL_STDNAME+" = '"+studentName+"'",
				null, null, null, null, null);
		
		c.moveToFirst();
		
		int new_absent = c.getInt(0)+num;
		boolean excessive = Boolean.parseBoolean(c.getString(1));
		ContentValues cv = new ContentValues();
		ContentValues cvret = new ContentValues();
		cv.put(AttendanceDbHelper.COL_NUM_ABSENCES, new_absent);
		if(new_absent>7){
			excessive = true;
			cv.put(AttendanceDbHelper.COL_EXCESSIVE, excessive);
		}
		else if(new_absent<=7){
			excessive = false;
			cv.put(AttendanceDbHelper.COL_EXCESSIVE, excessive);
		}
		
		db.update(AttendanceDbHelper.DB_TABLE_CLASS_LIST, cv, 
				AttendanceDbHelper.COL_CLASS_NAME+" = '"+className+"' and "+
				AttendanceDbHelper.COL_STDNAME+" = '"+studentName+"'"
				, null);
		

		cvret.put("excessive",excessive);
		cvret.put("numAbsent",new_absent);
		
		return cvret;
		
	}
	
}
