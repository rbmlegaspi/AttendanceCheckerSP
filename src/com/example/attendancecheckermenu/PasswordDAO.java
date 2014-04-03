package com.example.attendancecheckermenu;

import java.security.MessageDigest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class PasswordDAO {
	private Context c;
	private AttendanceDbHelper dbHelper;
	private SQLiteDatabase db;
	
	
	public PasswordDAO(Context context)
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
	
	public String getPassword(){
		String[] col = {
				AttendanceDbHelper.COL_PASSWORD,
				AttendanceDbHelper.COL_PASSWORD_MD5
		};
		
		Cursor cr = db.query(AttendanceDbHelper.DB_TABLE_PASSWORD, col, null, null, null, null, null);
		
		cr.moveToFirst();
		
		if(cr.getCount()!=0){
			return cr.getString(0);
		}
		else return null;
	
		
	}
	
	public void setPassword(String password){
		String[] col = {
				AttendanceDbHelper.COL_PASSWORD,
				AttendanceDbHelper.COL_PASSWORD_MD5
		};
		
		Cursor cr = db.query(AttendanceDbHelper.DB_TABLE_PASSWORD, col, null, null, null, null, null);
		
		cr.moveToFirst();
		ContentValues cv = new ContentValues();
		cv.put(AttendanceDbHelper.COL_PASSWORD, password);
		cv.put(AttendanceDbHelper.COL_PASSWORD_MD5, password);
		
		db.insert(AttendanceDbHelper.DB_TABLE_PASSWORD, null, cv);
	}
	
	public String setPassword(String cPassword,String nPassword){
		
		String oldPassword = getPassword();
		
		Log.d("FullscreenActivity",oldPassword+" "+cPassword);
		
		if(oldPassword.equals(cPassword)){
			ContentValues cv = new ContentValues();
			cv.put(AttendanceDbHelper.COL_PASSWORD, nPassword);
			cv.put(AttendanceDbHelper.COL_PASSWORD_MD5, nPassword);
			db.update(AttendanceDbHelper.DB_TABLE_PASSWORD, cv, null, null);
			
			return "Successfully changed password";
		}
		else{
			return "Invalid password";
		}
		
		
	}

	public String confirmPassword(String enteredPass) {
		// TODO Auto-generated method stub
		
		String currentPassword = getPassword();
		
		
		if(enteredPass.equals(currentPassword)){			
			return "Confirmed!";
		}
		else{
			return "Invalid password";
		}
	}
}
