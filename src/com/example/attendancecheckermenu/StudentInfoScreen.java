package com.example.attendancecheckermenu;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.database.SQLException;
import android.util.Log;
import android.view.Menu;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StudentInfoScreen extends Activity {

	private ArrayList<Photo> photoClassArray;
	private ArrayList<String> dateTaken = new ArrayList<String>();
	private ArrayList<String> pathName = new ArrayList<String>();
	private PhotoDAO pd;
	private String studentName;
	private String className;
	private GridView gridView;
	private PictureAdapter pictureAdapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_info_screen);

		studentName = getIntent().getExtras().getString("studentName");
		className = getIntent().getExtras().getString("className");

		gridView = (GridView) findViewById(R.id.pictureGallery);
		TextView nameView = (TextView) findViewById(R.id.StudentName);
		nameView.setText(studentName);
		AttendanceListDAO ald = new AttendanceListDAO(this);
		ald.open();
		ClassList cl = ald.viewClassListFromClassPerName(studentName, className);
		ald.close();
		
		TextView View1 = (TextView) findViewById(R.id.StudentNumber);
		View1.setText(cl.getStudentNumber());
		
		TextView View2 = (TextView) findViewById(R.id.sectionText);
		View2.setText(cl.getSection());
		
		TextView View3 = (TextView) findViewById(R.id.numOfAbsencesVal);
		View3.setText(cl.getNumOfAbsences()+"");
				
		pd = new PhotoDAO(this);
		pd.open();
		
		photoClassArray = pd.getAllPhotosFromStudent(studentName,className);		
//		photoClassArray = pd.getAllPhotos();
		if(photoClassArray.size()>0){
			for (Photo p : photoClassArray) {
				dateTaken.add(p.getDateTaken());
				pathName.add(p.getPathOfFile());
			}
		}
		else{
			Log.d("StudentInfo","Student doesn't have a record");
		}
		pd.close();
		pictureAdapter = new PictureAdapter(dateTaken, pathName, getApplicationContext());
		gridView.setAdapter(pictureAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_info_screen, menu);
		return true;
	}

}
