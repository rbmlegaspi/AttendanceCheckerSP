package com.example.attendancecheckermenu;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class AttendanceDbHelper extends SQLiteOpenHelper{

	public static final String DB_NAME = "AttendanceDb";
	public static final String DB_TABLE_CLASS_NAME = "ATTENDANCE_RECORD";
	public static final String DB_TABLE_CLASS_LIST = "CLASS_LIST";
	public static final String DB_TABLE_PASSWORD = "PASSWORD";
	public static final String DB_TABLE_PICDB = "PICTURE_DATABASE";
	
	
	private static final int DB_VERSION = 1;
	
	public static final String COL_CLASS_NAME = "CLASS_NAME";
	public static final String COL_CLASS_SIZE = "CLASS_SIZE";
	public static final String COL_LECTURER = "LECTURER";
	public static final String COL_SA_1 = "SA_1";
	public static final String COL_SA_2 = "SA_2";
	public static final String COL_SA_3 = "SA_3";
	public static final String COL_HAS_CLASS_LIST = "HAS_CLASSLIST";
	public static final String COL_EXCESSIVE_NUM = "NUM_OF_EXCESSIVE";
	
	public static final String COL_STDNUM = "STUDENT_NUMBER";
	public static final String COL_STDNAME = "STUDENT_NAME";
	public static final String COL_LEC_SECTION = "STUDENT_LEC_SECTION";
	public static final String COL_SECTION = "STUDENT_SECTION";
	public static final String COL_STDPIC = "STUDENT_PICTURE";
	public static final String COL_NUM_ABSENCES = "NUM_OF_ABSENCES";
	public static final String COL_EXCESSIVE = "EXCESSIVE";
	public static final String COL_DATES_ABSENT = "DATES_ABSENT";
	public static final String COL_HAS_TAKEN_PICTURE = "HAS_TAKEN_PICTURE";
	
	public static final String COL_PASSWORD = "PASSWORD";
	public static final String COL_PASSWORD_MD5 = "PASSWORD_MD5";
	
	public static final String COL_PICTURE_LOCAL_PATH = "PICTURE_LOCAL_PATH";
	public static final String COL_PICTURE_STDNUM = "PICTURE_STDNUM";
	public static final String COL_PICTURE_STDNAME = "PICTURE_STDNAME";
	public static final String COL_PICTURE_DATE_TAKEN = "DATE_TAKEN";
	public static final String COL_PICTURE_CLASS_NAME = "CLASS_NAME";
	public static final String COL_ID = "ID";
	
	
	Context c;
	
	public AttendanceDbHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		c = context;
		// TODO Auto-generated constructor stub
	}

	@Override
	
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
		db.execSQL("CREATE TABLE "+ DB_TABLE_CLASS_NAME +" ("
				+ COL_CLASS_NAME +" TEXT PRIMARY KEY,"
				+ COL_CLASS_SIZE +" INTEGER NOT NULL,"
				+ COL_LECTURER + " TEXT NOT NULL,"
				+ COL_SA_1 + " TEXT NOT NULL,"
				+ COL_SA_2 + " TEXT NOT NULL,"
				+ COL_SA_3 + " TEXT NOT NULL,"
				+ COL_HAS_CLASS_LIST+ " TEXT NOT NULL,"
				+ COL_EXCESSIVE_NUM+" INTEGER NOT NULL"
				+ ");"
		);
		
		
		db.execSQL("CREATE TABLE "+DB_TABLE_CLASS_LIST+" ("
				+ COL_STDNUM +" TEXT PRIMARY KEY,"
				+ COL_STDNAME+" INTEGER NOT NULL,"
				+ COL_LEC_SECTION+" TEXT NOT NULL,"
				+ COL_CLASS_NAME+" TEXT NOT NULL,"
				+ COL_SECTION+" INTEGER NOT NULL,"
				+ COL_STDPIC +" TEXT NOT NULL,"
				+ COL_NUM_ABSENCES + " TEXT NOT NULL,"
				+ COL_EXCESSIVE + " TEXT NOT NULL,"
				+ COL_DATES_ABSENT + " TEXT NOT NULL,"
				+ COL_HAS_TAKEN_PICTURE + " TEXT NOT NULL"
				+ ");"
		);
		
		db.execSQL("CREATE TABLE "+DB_TABLE_PICDB+" ("
				+ COL_PICTURE_LOCAL_PATH +" TEXT NOT NULL,"
				+ COL_PICTURE_STDNUM +" TEXT NOT NULL,"
				+ COL_PICTURE_STDNAME +" TEXT NOT NULL,"
				+ COL_PICTURE_DATE_TAKEN +" TEXT NOT NULL,"
				+ COL_PICTURE_CLASS_NAME +" TEXT NOT NULL"
				+ ");"
		);
		
		db.execSQL("CREATE TABLE "+DB_TABLE_PASSWORD+" ("
				+ COL_PASSWORD +" TEXT NOT NULL,"
				+ COL_PASSWORD_MD5 +" INTEGER NOT NULL"
				+ ");"
		);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_CLASS_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_CLASS_LIST);
		db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_PASSWORD);
		db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_PICDB);
		this.onCreate(db);
	}

}
