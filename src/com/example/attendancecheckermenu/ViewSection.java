package com.example.attendancecheckermenu;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ViewSection extends Activity {

	ArrayList<ClassList> classListArray = new ArrayList<ClassList>();
	ArrayList<String> studentNameList = new ArrayList<String>();
	ArrayList<String> studentNumberList = new ArrayList<String>();
	ArrayList<String> pathList = new ArrayList<String>();
	String className;
	String selectedStudent;
	ListView studentList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		className = getIntent().getExtras().getString("className")+"";
		
		setContentView(R.layout.activity_view_section);
		//final ListView list = (ListView) findViewById(R.id.classListView);
		
		
		AttendanceListDAO ald = new AttendanceListDAO(getApplicationContext());
		studentList = (ListView) findViewById(R.id.classListGrid);
	//	TextView tv = (TextView) findViewById(R.id.textView10);
	//	tv.setText(className);
        ald.open();
        
        classListArray = ald.viewClassListFromClass(className);
        for (int i = 0; i < classListArray.size(); i++) {
			ClassList classObj = classListArray.get(i);
			String listItem1 = classObj.getStudentName();
			String listItem2 = classObj.getStudentNumber();
			String listItem3 = classObj.getStudentPicPath();
			studentNameList.add(listItem1);
			studentNumberList.add(listItem2);
			pathList.add(listItem3);
		}
    	
        ald.close();
        
        StudentNumNameAdapter adapter = new StudentNumNameAdapter(ViewSection.this, studentNameList, studentNumberList,pathList);
        studentList.setAdapter(adapter);
		studentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				 selectedStudent = (String) (studentList.getItemAtPosition(position));
				 Log.d("ViewSection",selectedStudent);
				 Intent intent = new Intent(getApplicationContext(), StudentInfoScreen.class);
				 intent.putExtra("studentName", selectedStudent);
				 intent.putExtra("className", className);
				 startActivity(intent);
			}
		});
	}


}
