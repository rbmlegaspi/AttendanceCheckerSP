package com.example.attendancecheckermenu;

import java.io.File;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/*
 * 
 * TODO Implement file manager class, and pass it on to calling class
 * 
 * */

public class FileManager extends Activity {
	ArrayList<String> filename_list = new ArrayList<String>();
	ArrayAdapter<String> adapterForFileNames;
	File[] list;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.activity_file_manager);
    	ListView listView = (ListView) findViewById(R.id.dateList);        

    	File file = Environment.getExternalStorageDirectory();
        this.list = file.listFiles();
    	
        for (File filenameItem : list) {
        	filename_list.add(filenameItem.getAbsolutePath());
		}
        
        adapterForFileNames = new ArrayAdapter<String>(FileManager.this, android.R.layout.simple_list_item_1, filename_list);
        listView.setAdapter(adapterForFileNames);
    	
        
        listView.setOnItemClickListener(
        	new AdapterView.OnItemClickListener() {
        		public void onItemClick(AdapterView<?> parent,View view, int position,long id)
        		{
        			String filePath = (String) parent.getItemAtPosition(position);
        			File selectedFile = new File(filePath);
        			if(selectedFile.isDirectory())
        			{
        				list = selectedFile.listFiles();
        				filename_list.clear();
        				for (File filenameItem :list) {
        					filename_list.add(filenameItem.getAbsolutePath());
						}
        				adapterForFileNames.notifyDataSetChanged();
        				//Toast.makeText(getApplicationContext(), "Directory",Toast.LENGTH_LONG).show();
        			}
        			else{
        				//if csv file
        				
        			}
        			

        		}
			}
        );
    	
	}	

	
}
