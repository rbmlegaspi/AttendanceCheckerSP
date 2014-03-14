package com.example.attendancecheckermenu;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class ViewSectionFragment extends Fragment{	
	ArrayList<ClassList> classListArray = new ArrayList<ClassList>();
	ArrayList<String> studentNameList = new ArrayList<String>();
	ArrayList<String> studentNumberList = new ArrayList<String>();
	ArrayList<String> pathList = new ArrayList<String>();
	String className;
	String selectedStudent;
	ListView studentList;
	
	GetCN cn;
	
	public interface GetCN{
		public String getClassName();
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
			// Inflate the layout for this fragment

			View v = inflater.inflate(R.layout.view_by_class_date, container, false);
		
			className = cn.getClassName();

			Log.d("ViewClassList",className);
			TextView tv = (TextView) v.findViewById(R.id.tw);
 			tv.setText(className);
	
 			AttendanceListDAO ald = new AttendanceListDAO(getActivity().getApplicationContext());
 			studentList = (ListView) v.findViewById(R.id.classListGrid);
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
 	        
 	        StudentNumNameAdapter adapter = new StudentNumNameAdapter(getActivity().getApplicationContext(), studentNameList, studentNumberList,pathList);
 	        studentList.setAdapter(adapter);
 			studentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

 				public void onItemClick(AdapterView<?> parent, View view, int position,
 						long id) {
 					 selectedStudent = (String) (studentList.getItemAtPosition(position));
 					 Log.d("ViewSection",selectedStudent);
 					 Intent intent = new Intent(getActivity().getApplicationContext(), StudentInfoScreen.class);
 					 intent.putExtra("studentName", selectedStudent);
 					 intent.putExtra("className", className);
 					 startActivity(intent);
 				}
 			});
 			
 			return v;
				
			}
	
	public void onAttach(Activity activity){
		super.onAttach(activity);
		
		cn = (GetCN) activity;
	}
	
	public String getClassName(){
		return cn.getClassName();
	}
}
