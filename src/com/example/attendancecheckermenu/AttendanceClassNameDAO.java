package com.example.attendancecheckermenu;

import java.util.ArrayList;

import android.app.DownloadManager.Query;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AttendanceClassNameDAO {
	
	private AttendanceDbHelper dbHelper;
	private SQLiteDatabase db;

	private final String[] AttendanceClassNameCol = {
			AttendanceDbHelper.COL_CLASS_NAME,
			AttendanceDbHelper.COL_CLASS_SIZE,
			AttendanceDbHelper.COL_LECTURER,
			AttendanceDbHelper.COL_SA_1,
			AttendanceDbHelper.COL_SA_2,
			AttendanceDbHelper.COL_SA_3,
			AttendanceDbHelper.COL_HAS_CLASS_LIST
	};
	
	 
	private Context c;
	
	public AttendanceClassNameDAO(Context context)
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
	
	public long insertClassToDb(String className,int classSize,String lecturer, String SA_1,String SA_2, String SA_3)
	{
		ContentValues values = new ContentValues();
		values.put(AttendanceClassNameCol[0], className);
		values.put(AttendanceClassNameCol[1], classSize);
		values.put(AttendanceClassNameCol[2], lecturer);
		values.put(AttendanceClassNameCol[3], SA_1);
		values.put(AttendanceClassNameCol[4], SA_2);
		values.put(AttendanceClassNameCol[5], SA_3);
		values.put(AttendanceClassNameCol[6], "false");
		long insertClassName = db.insert(AttendanceDbHelper.DB_TABLE_CLASS_NAME, null, values);
		
		return insertClassName;
	}
	
	public void dropDebug()
	{
		db.execSQL("DROP TABLE IF EXISTS " + AttendanceDbHelper.DB_TABLE_CLASS_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + AttendanceDbHelper.DB_TABLE_CLASS_LIST);
		db.execSQL("DROP TABLE IF EXISTS " + AttendanceDbHelper.DB_TABLE_PASSWORD);
		db.execSQL("DROP TABLE IF EXISTS " + AttendanceDbHelper.DB_TABLE_PICDB);
	}
	
	public void dropAndCreateClassListDebug(){
		Log.d("FullscreenActivity","asd");
		db.execSQL("DROP TABLE IF EXISTS " + AttendanceDbHelper.DB_TABLE_CLASS_LIST);
		db.execSQL("CREATE TABLE "+AttendanceDbHelper.DB_TABLE_CLASS_LIST+" ("
				+ AttendanceDbHelper.COL_STDNUM +" TEXT PRIMARY KEY,"
				+ AttendanceDbHelper.COL_STDNAME+" INTEGER NOT NULL,"
				+ AttendanceDbHelper.COL_CLASS_NAME + " TEXT NOT NULL,"
				+ AttendanceDbHelper.COL_LEC_SECTION+" TEXT NOT NULL,"
				+ AttendanceDbHelper.COL_SECTION+" INTEGER NOT NULL,"
				+ AttendanceDbHelper.COL_STDPIC +" TEXT NOT NULL,"
				+ AttendanceDbHelper.COL_NUM_ABSENCES + " TEXT NOT NULL,"
				+ AttendanceDbHelper.COL_EXCESSIVE + " TEXT NOT NULL,"
				+ AttendanceDbHelper.COL_DATES_ABSENT + " TEXT NOT NULL,"
				+ AttendanceDbHelper.COL_HAS_TAKEN_PICTURE + " TEXT NOT NULL"
				+ ");"
		);
	}
	
	public void createTableDebug()
	{
		db.execSQL("CREATE TABLE "+ AttendanceDbHelper.DB_TABLE_CLASS_NAME +" ("
				+ AttendanceDbHelper.COL_CLASS_NAME +" TEXT PRIMARY KEY,"
				+ AttendanceDbHelper.COL_CLASS_SIZE +" INTEGER NOT NULL,"
				+ AttendanceDbHelper.COL_LECTURER + " TEXT NOT NULL,"
				+ AttendanceDbHelper.COL_SA_1 + " TEXT NOT NULL,"
				+ AttendanceDbHelper.COL_SA_2 + " TEXT NOT NULL,"
				+ AttendanceDbHelper.COL_SA_3 + " TEXT NOT NULL"
				+ AttendanceDbHelper.COL_HAS_CLASS_LIST +" TEXT NOT NULL"				
				+ ");"
		);
		
		db.execSQL("CREATE TABLE "+AttendanceDbHelper.DB_TABLE_CLASS_LIST+" ("
				+ AttendanceDbHelper.COL_STDNUM +" TEXT PRIMARY KEY,"
				+ AttendanceDbHelper.COL_STDNAME+" INTEGER NOT NULL,"
				+ AttendanceDbHelper.COL_LEC_SECTION+" INTEGER NOT NULL,"
				+ AttendanceDbHelper.COL_SECTION+" INTEGER NOT NULL,"
				+ AttendanceDbHelper.COL_STDPIC +" TEXT NOT NULL,"
				+ AttendanceDbHelper.COL_NUM_ABSENCES + " TEXT NOT NULL,"
				+ AttendanceDbHelper.COL_EXCESSIVE + " TEXT NOT NULL,"
				+ AttendanceDbHelper.COL_DATES_ABSENT + " TEXT NOT NULL"
				+ ");"
		);
		
		db.execSQL("CREATE TABLE "+AttendanceDbHelper.DB_TABLE_PICDB+" ("
				+ AttendanceDbHelper.COL_PICTURE_LOCAL_PATH +" TEXT NOT NULL,"
				+ AttendanceDbHelper.COL_PICTURE_STDNUM +" TEXT NOT NULL,"
				+AttendanceDbHelper.COL_PICTURE_DATE_TAKEN +" TEXT NOT NULL"
				+ ");"
		);
		
		db.execSQL("CREATE TABLE "+AttendanceDbHelper.DB_TABLE_PASSWORD+" ("
				+ AttendanceDbHelper.COL_PASSWORD +" TEXT NOT NULL,"
				+ AttendanceDbHelper.COL_PASSWORD_MD5 +" INTEGER NOT NULL"
				+ ");"
		);
	}

	public void debugColumns(String className){
		Cursor c = db.query(AttendanceDbHelper.DB_TABLE_CLASS_LIST, null, null, null, null, null, null);
		
		String[] wow = c.getColumnNames();
		
		String s = "";
		
		for (String x : wow) {
			s+= x;
		}
		
		Log.d("ViewClassList",s);
		
	}
	
	public void setClassListToTrue(String className){
	
		ContentValues cv = new ContentValues();
		cv.put(AttendanceDbHelper.COL_HAS_CLASS_LIST, "true");
		db.update(AttendanceDbHelper.DB_TABLE_CLASS_NAME, cv, AttendanceDbHelper.COL_CLASS_NAME+" = '"+className+"'", null);
		
	}
	
	public void setClassListToFalse(String className){
		
		ContentValues cv = new ContentValues();
		cv.put(AttendanceDbHelper.COL_HAS_CLASS_LIST, "false");
		db.update(AttendanceDbHelper.DB_TABLE_CLASS_NAME, cv, AttendanceDbHelper.COL_CLASS_NAME+" = '"+className+"'", null);
		
	}
	
	public int getClassSizeFromDb(String className){
		Cursor c = db.query(AttendanceDbHelper.DB_TABLE_CLASS_NAME,AttendanceClassNameCol,AttendanceDbHelper.COL_CLASS_NAME+" = '"+className+"'", null,null,null,null);
		
		c.moveToFirst();
		
		return c.getInt(1);
	}
	
	public boolean hasClassList(String className){
		Cursor c = db.query(AttendanceDbHelper.DB_TABLE_CLASS_NAME,AttendanceClassNameCol,AttendanceDbHelper.COL_CLASS_NAME+" = '"+className+"'", null,null,null,null);
		
		c.moveToFirst();
		
		return Boolean.parseBoolean(c.getString(6));
	}

	public ArrayList<ClassName> viewAllClasses() 
	{
		ArrayList<ClassName> ClassName = new ArrayList<ClassName>();
		Cursor c = db.query(AttendanceDbHelper.DB_TABLE_CLASS_NAME, AttendanceClassNameCol, null, null,null,null,null);
		//Cursor c = db.rawQuery("SELECT * from "+AttendanceDbHelper.DB_TABLE_CLASS_NAME,null);
		c.moveToFirst();
		
		while(!c.isAfterLast())
		{
			
			ClassName cl = new ClassName();
			cl.setClassName(c.getString(0));
			cl.setClassSize(c.getInt(1));
			cl.setLecturer(c.getString(2));
			cl.setSA_1(c.getString(3));
			cl.setSA_2(c.getString(4));
			cl.setSA_3(c.getString(5));
			cl.setHasClassList(Boolean.parseBoolean(c.getString(6)));
			ClassName.add(cl);
			c.moveToNext();
		}
		
		c.close();
		return ClassName;
		
	}
}
