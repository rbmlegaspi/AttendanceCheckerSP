package com.example.attendancecheckermenu;

import java.util.ArrayList;

import com.example.attendancecheckermenu.ViewSectionFragment.GetCN;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.util.AndroidRuntimeException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

public class ViewDateFragment extends Fragment{
	
	ArrayList<Photo> studentArray = new ArrayList<Photo>();
	ArrayList<String> dates = new ArrayList<String>();
	
	String className,dateTaken;
	GetCN cn;
	Dialog dialog;
	GridView gridView;
	ListView listView;
	PhotoDAO pd;
	DateGalleryAdapter dateGalleryAdapter;
	public interface GetCN{
		public String getClassName();
		public String getDateTaken();
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.view_menu, container, false);
		
		listView = (ListView) v.findViewById(R.id.dateList);
		gridView = (GridView) v.findViewById(R.id.classListGrid);
		className = cn.getClassName();

		pd = new PhotoDAO(getActivity().getApplicationContext());
    	pd.open();
    	for (String date : pd.getDates(className)) {
			dates.add(date);
		}
    	
		studentArray = pd.getAllPhotosFromDate(dates.get(0), className);		
		dateGalleryAdapter = new DateGalleryAdapter(getActivity().getApplicationContext(),studentArray);
		gridView.setAdapter(dateGalleryAdapter);
    	
    	pd.close();
    	
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),R.layout.list_item_la,R.id.item_text, dates);
    	listView.setAdapter(adapter);
    	listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				 dateTaken = (String) (listView.getItemAtPosition(position));
				 
				 pd.open();
				 studentArray = pd.getAllPhotosFromDate(dateTaken, className);
				 pd.close();
				 
				 dateGalleryAdapter = new DateGalleryAdapter(getActivity().getApplicationContext(),studentArray);
				 gridView.setAdapter(dateGalleryAdapter);
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
	
	public String getDateTaken(){
		return cn.getDateTaken();
	}
	
}