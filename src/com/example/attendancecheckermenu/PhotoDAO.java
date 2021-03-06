package com.example.attendancecheckermenu;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class PhotoDAO {
	
	private AttendanceDbHelper dbHelper;
	private SQLiteDatabase db;
	private final String[] PhotoTableCol = {
					AttendanceDbHelper.COL_PICTURE_LOCAL_PATH,
					AttendanceDbHelper.COL_PICTURE_STDNUM,
					AttendanceDbHelper.COL_PICTURE_STDNAME,
					AttendanceDbHelper.COL_PICTURE_DATE_TAKEN,
					AttendanceDbHelper.COL_PICTURE_CLASS_NAME,
	}; 
	private Context context;
	
	public PhotoDAO(Context context)
	{
		dbHelper = new AttendanceDbHelper(context);
		this.context = context;
	}
	
	public void open() throws SQLException
	{
		this.db = dbHelper.getWritableDatabase();
	}
	
	public void close()
	{
		dbHelper.close();
	}
	
	public long insertPhotoToDb(String pathname,String stdnum,String stdname,String dateTaken,String className){
		ContentValues cv = new ContentValues();
		cv.put(PhotoTableCol[0], pathname);
		cv.put(PhotoTableCol[1], stdnum);
		cv.put(PhotoTableCol[2], stdname);
		cv.put(PhotoTableCol[3], dateTaken);
		cv.put(PhotoTableCol[4], className);
		long id = db.insert(AttendanceDbHelper.DB_TABLE_PICDB, null, cv);
		
		return id;
	}
	
	
	public ArrayList<Photo> getAllPhotosFromDate(String dateTaken,String className) throws SQLException{
		ArrayList<Photo> photoAL = new ArrayList<Photo>();
		Cursor c = db.query(AttendanceDbHelper.DB_TABLE_PICDB, PhotoTableCol,
				AttendanceDbHelper.COL_PICTURE_DATE_TAKEN+" = '"+dateTaken+"' and "+
				AttendanceDbHelper.COL_CLASS_NAME+" = '"+className+"'",
				null, null, null, null);
		
		c.moveToFirst();
		
		while(!c.isAfterLast()){
			Photo tempClass = new Photo();
			tempClass.setPathOfFile(c.getString(0));
			tempClass.setStdNum(c.getString(1));
			tempClass.setStdName(c.getString(2));
			tempClass.setDateTaken(c.getString(3));
			tempClass.setClassName(c.getString(4));
			photoAL.add(tempClass);
			c.moveToNext();
		}
		
		return photoAL;
	}
	
	public boolean photoHasBeenTaken(String studentName, String className, String date){
		String[] s = {
				AttendanceDbHelper.COL_PICTURE_STDNAME
		};
		
		Cursor c = db.query(AttendanceDbHelper.DB_TABLE_PICDB, s, 
				AttendanceDbHelper.COL_PICTURE_DATE_TAKEN+" = '"+date+"' and "
				+AttendanceDbHelper.COL_PICTURE_STDNAME+" = '"+studentName+"' and "
				+AttendanceDbHelper.COL_PICTURE_CLASS_NAME+" = '"+className+"'"	,
				null, null, null, null);
		
		
		c.moveToFirst();
		
		if(c.getCount()==0) return false;
		else return true;
		
	}
	
	public ArrayList<Photo> getAllPhotosFromStudent(String studentName,String className) throws SQLException{
		ArrayList<Photo> photoAL = new ArrayList<Photo>();
		Cursor c = db.query(AttendanceDbHelper.DB_TABLE_PICDB, PhotoTableCol,
				AttendanceDbHelper.COL_PICTURE_STDNAME+" = '"+studentName+"' and " +
				AttendanceDbHelper.COL_PICTURE_CLASS_NAME+" = '"+className+"'",
				null, null, null, null);
		
		c.moveToFirst();
		
		while(!c.isAfterLast()){
			Photo tempClass = new Photo();
			tempClass.setPathOfFile(c.getString(0));
			tempClass.setStdNum(c.getString(1));
			tempClass.setStdName(c.getString(2));
			tempClass.setDateTaken(c.getString(3));
			tempClass.setClassName(c.getString(4));
			photoAL.add(tempClass);
			c.moveToNext();
		}
		
		return photoAL;
	}
	
	public ArrayList<String> getDates(String className){
		ArrayList<String> dates = new ArrayList<String>();
		String[] datesCol = {
				AttendanceDbHelper.COL_PICTURE_DATE_TAKEN
		};
		
		Cursor c = db.query(true,AttendanceDbHelper.DB_TABLE_PICDB, datesCol,
				AttendanceDbHelper.COL_PICTURE_CLASS_NAME+" = '"+className+"'",
				null, null, null, null,null);
		
		c.moveToFirst();
		
		while(!c.isAfterLast()){
			String date = c.getString(0);
			dates.add(date);
			c.moveToNext();
		}
		
		return dates;
	}
	
	public ArrayList<Photo> getAllPhotosFromClassAndDate(String className,String dateTaken) throws SQLException{
		ArrayList<Photo> photoAL = new ArrayList<Photo>();
		Cursor c = db.query(AttendanceDbHelper.DB_TABLE_PICDB, PhotoTableCol,
				AttendanceDbHelper.COL_PICTURE_CLASS_NAME+" = '"+className+"' and " +
				AttendanceDbHelper.COL_PICTURE_DATE_TAKEN+" = '"+dateTaken+"'",
				null, null, null, null);
		
		c.moveToFirst();
		
		while(!c.isAfterLast()){
			Photo tempClass = new Photo();
			tempClass.setPathOfFile(c.getString(0));
			tempClass.setStdNum(c.getString(1));
			tempClass.setStdName(c.getString(2));
			tempClass.setDateTaken(c.getString(3));
			tempClass.setClassName(c.getString(4));
			photoAL.add(tempClass);
			c.moveToNext();
		}
		
		return photoAL;
	}
	
	public ArrayList<Photo> getAllPhotosFromClass(String className) throws SQLException{
		ArrayList<Photo> photoAL = new ArrayList<Photo>();
		Cursor c = db.query(AttendanceDbHelper.DB_TABLE_PICDB, PhotoTableCol,
				AttendanceDbHelper.COL_PICTURE_CLASS_NAME+" = '"+className+"'",
				null, null, null, null);
		
		c.moveToFirst();
		
		while(!c.isAfterLast()){
			Photo tempClass = new Photo();
			tempClass.setPathOfFile(c.getString(0));
			tempClass.setStdNum(c.getString(1));
			tempClass.setStdName(c.getString(2));
			tempClass.setDateTaken(c.getString(3));
			tempClass.setClassName(c.getString(4));
			photoAL.add(tempClass);
			c.moveToNext();
		}
		
		return photoAL;
	}
	
	public ArrayList<Photo> getAllPhotos() throws SQLException{
		ArrayList<Photo> photoAL = new ArrayList<Photo>();
		Cursor c = db.query(AttendanceDbHelper.DB_TABLE_PICDB, PhotoTableCol, null, null, null, null, null);
		
		c.moveToFirst();
		
		while(!c.isAfterLast()){
			Photo tempClass = new Photo();
			tempClass.setPathOfFile(c.getString(0));
			tempClass.setStdNum(c.getString(1));
			tempClass.setStdName(c.getString(2));
			tempClass.setDateTaken(c.getString(3));
			tempClass.setClassName(c.getString(4));
			photoAL.add(tempClass);
			c.moveToNext();
		}
		
		return photoAL;
	}

	public void replacePhotoPath(String absolutePath, String studentName,
			String string, String dateTaken, String className) {
		ContentValues cv = new ContentValues();
		cv.put(AttendanceDbHelper.COL_PICTURE_LOCAL_PATH,absolutePath);
		db.update(AttendanceDbHelper.DB_TABLE_PICDB, cv, 
				AttendanceDbHelper.COL_PICTURE_STDNAME+" = '"+studentName+"' and "+
				AttendanceDbHelper.COL_PICTURE_CLASS_NAME+" = '"+className+"' and " +
				AttendanceDbHelper.COL_PICTURE_DATE_TAKEN+" = '"+dateTaken+"'"
				, null);
		
	}
}
