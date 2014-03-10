package com.example.attendancecheckermenu;

import java.util.ArrayList;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ButtonAdapter extends BaseAdapter{

	private ArrayList<String> section;
	private Context c;
	//text lang naman
	public ButtonAdapter(Context c, ArrayList<String> section){
		this.section = section;
		this.c = c;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return section.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override

	public View getView(int position, View listView, ViewGroup parent) {
		TextView t;
		View v = listView;
		
	    if(listView==null){
        	LayoutInflater inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	v = inflater.inflate(R.layout.list_button_text, parent, false);
	    }
		
		t = (TextView) v
				.findViewById(R.id.section);
		t.setText(section.get(position));
		
	   
		 return v;
	}
}
