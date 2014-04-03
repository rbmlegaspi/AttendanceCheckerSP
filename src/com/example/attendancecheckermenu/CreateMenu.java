package com.example.attendancecheckermenu;

import com.example.attendancecheckermenu.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class CreateMenu extends Activity {
	AttendanceClassNameDAO ald;
	EditText classNameText,classNumText,lecText,sa1Text,sa2Text,sa3Text,excNum;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_create_menu);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		ald = new AttendanceClassNameDAO(CreateMenu.this);
		
		
	}

	public void saveClassList(View view)
	{
		classNameText = (EditText) findViewById (R.id.classNameFragItem);
		classNumText = (EditText) findViewById(R.id.numStud);
		lecText = (EditText) findViewById(R.id.lecturer);
		sa1Text = (EditText) findViewById(R.id.sa1);
		sa2Text = (EditText) findViewById(R.id.sa2);
		sa3Text = (EditText) findViewById(R.id.sa3);
		excNum = (EditText) findViewById(R.id.numExcessiveF);
		
		ald.open();
		boolean exists = ald.classNameExists(classNameText.getText().toString());
		ald.close();
		

		if(exists){
			Toast.makeText(CreateMenu.this, "Class already exists", Toast.LENGTH_LONG).show();
		}
		else{
			try
			{
					String className = classNameText.getText().toString();
					int numOfStudent = Integer.parseInt(classNumText.getText().toString());
					String lecturer = lecText.getText().toString();
					String sa1 = sa1Text.getText().toString();
					String sa2 = sa2Text.getText().toString();
					String sa3 = sa3Text.getText().toString();
					int numExc = Integer.parseInt(excNum.getText().toString());
					Toast.makeText(CreateMenu.this, "Class list succesfully updated", Toast.LENGTH_SHORT).show();
					ald.open();
					ald.insertClassToDb(className, numOfStudent, lecturer, sa1, sa2, sa3,numExc);
					ald.close();
						
				}catch(Exception e){
					Toast.makeText(CreateMenu.this, e.toString(), Toast.LENGTH_LONG).show();
				}finally{
					
				}
				
				finish();
		}
		
	}
	
}
