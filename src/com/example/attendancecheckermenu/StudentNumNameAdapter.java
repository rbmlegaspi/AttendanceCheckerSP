package com.example.attendancecheckermenu;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class StudentNumNameAdapter extends ArrayAdapter<String>{
	
	private Context context;
	private ArrayList<String> values1,values2,values3;
	
	public StudentNumNameAdapter(Context context,ArrayList<String> values1, ArrayList<String> values2,ArrayList<String> values3){
		super(context,R.layout.activity_view_section,values1);
		this.context = context;
		this.values1 = values1;
		this.values2 = values2;
		this.values3 = values3;
	}
	
	public View getView(int position, View listView, ViewGroup parent) {
	    View v = listView;
		
	    if(listView==null){
        	LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	v = inflater.inflate(R.layout.list_item_student, parent, false);
	    }
        TextView textView1 = (TextView) v.findViewById(R.id.StudentNameItem);
	    TextView textView2 = (TextView) v.findViewById(R.id.StudentNumItem);
	    ImageView imageView = (ImageView) v.findViewById(R.id.student_pic);
	    
	    textView2.setText(values2.get(position));
	    textView1.setText(values1.get(position));
	    
	    if(values3.get(position).equals("nopic")){
	    	
	    }
	    
	    // change the icon for Windows and iPhone
	    return v;
	  }
}

