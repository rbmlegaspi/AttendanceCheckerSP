package com.example.attendancecheckermenu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.SQLException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ViewClassList extends Activity implements ViewSectionFragment.GetCN{

	String className;
	ViewSectionFragment fragment;
	ViewDateFragment dFragment;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		className = "CMSC 125 s";
//		className = getIntent().getExtras().getString("Class Name");
		
		setContentView(R.layout.activity_view_class_list);
		
		
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		
		
		fragment = new ViewSectionFragment();
		dFragment = new ViewDateFragment();
		Bundle args = new Bundle();
		args.putString("className", className);
		fragmentTransaction.add(R.id.fragLayout	, fragment);
		fragmentTransaction.commit();
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.attendance_view_switch, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    	
		switch (item.getItemId()) {
	        case R.id.view_by_students:
	    		fragmentTransaction.replace(R.id.fragLayout	,fragment);
	    		fragmentTransaction.remove(dFragment);
	    		fragmentTransaction.commit();
	        	return true;
	        case R.id.view_by_date:
	        	fragmentTransaction.replace(R.id.fragLayout	, dFragment);
	        	fragmentTransaction.remove(fragment);
	    		fragmentTransaction.commit();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public String getClassName(){
		return this.className;
	}
	
}