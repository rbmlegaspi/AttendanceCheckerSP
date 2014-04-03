package com.example.attendancecheckermenu;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.BitmapFactory.Options;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
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

		TextView tv = (TextView) findViewById(R.id.excessiveField);
		tv.setText((cl.isExcessive()+""));
		pd = new PhotoDAO(this);
		pd.open();
		
		ImageView iv = (ImageView) findViewById(R.id.origPic);
		Options opts = new Options();
		opts.inSampleSize = 4;
	  	Bitmap bitmap = BitmapFactory.decodeFile(cl.getStudentPicPath(),opts);
		iv.setImageBitmap(bitmap);
		
		final String studentPicPath = cl.getStudentPicPath();
		
		photoClassArray = pd.getAllPhotosFromStudent(studentName,className);		
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
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				final Dialog d = new Dialog(StudentInfoScreen.this);
				d.setContentView(R.layout.dialog_android_compare);
				ImageView imgV = (ImageView) d.findViewById(R.id.compare1);
				ImageView imgV2 = (ImageView) d.findViewById(R.id.compare2);
				
				Options opts = new Options();
			  	opts.inSampleSize = 4;
			  	Matrix m = new Matrix();
			  	m.postRotate(270);
			  	Bitmap bitmap = BitmapFactory.decodeFile(studentPicPath,opts);
			  	imgV.setImageBitmap(bitmap);
			    
			  	if(pathName.get(arg2).equals("nopicture")){
			  		
			  	}else{
			    Bitmap bitmap2 = BitmapFactory.decodeFile(pathName.get(arg2),opts);
			    imgV2.setImageBitmap(bitmap2);
			  	}
				d.setTitle("Compare");
				Button dialogButton = (Button) d.findViewById(R.id.nopls);
				// if button is clicked, close the custom dialog
				dialogButton.setOnClickListener(new View.OnClickListener() {				
					public void onClick(View v) {
						// TODO Auto-generated method stub
						AttendanceListDAO ald = new AttendanceListDAO(StudentInfoScreen.this);
						ald.open();
						
						ContentValues cv = ald.addAbsent(studentName, className, 1);
						ald.close();
						
						TextView tv = (TextView) findViewById(R.id.excessiveField);
						tv.setText(cv.get("excessive")+"");
						
						TextView tv2 = (TextView) findViewById(R.id.numOfAbsencesVal);
						tv2.setText(cv.get("numAbsent")+"");	
						d.dismiss();
					}
				});
				
				Button dialogButton2 = (Button) d.findViewById(R.id.ok);
				// if button is clicked, close the custom dialog
				dialogButton2.setOnClickListener(new View.OnClickListener() {				
					public void onClick(View v) {
						// TODO Auto-generated method stub
							d.dismiss();
						
					}
				});
				
				d.show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_info_screen, menu);
		return true;
	}

	public void addAbsent(View view){
		AttendanceListDAO ald = new AttendanceListDAO(this);
		ald.open();
		
		ContentValues cv = ald.addAbsent(studentName, className, 1);
		ald.close();
		
		TextView tv = (TextView) findViewById(R.id.excessiveField);
		tv.setText(cv.get("excessive")+"");
		
		TextView tv2 = (TextView) findViewById(R.id.numOfAbsencesVal);
		tv2.setText(cv.get("numAbsent")+"");
	}
	
	public void subAbsent(View view){
		AttendanceListDAO ald = new AttendanceListDAO(this);
		ald.open();
		
		ContentValues cv = ald.addAbsent(studentName, className, -1);
		ald.close();
		
		TextView tv = (TextView) findViewById(R.id.excessiveField);
		tv.setText(cv.get("excessive")+"");
		
		TextView tv2 = (TextView) findViewById(R.id.numOfAbsencesVal);
		tv2.setText(cv.get("numAbsent")+"");
	}
	
}
