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
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.database.SQLException;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ViewClassList extends Activity {

	ArrayList<String> class_names = new ArrayList<String>();
	ArrayList<ClassName> classNameArray;
	String selectedClassName;
	String selectedFilename;
	Dialog dialog;
	AttendanceClassNameDAO acnd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_view_class_list);

    	final ListView list = (ListView) findViewById(R.id.classListView);
    	AttendanceClassNameDAO ald = new AttendanceClassNameDAO(ViewClassList.this);
    	
        
        ald.open();
        
        classNameArray = ald.viewAllClasses();
        for (int i = 0; i < classNameArray.size(); i++) {
			ClassName classObj = classNameArray.get(i);
			String listItem = classObj.getClassName();
			class_names.add(listItem);
			
		}
    	
        ald.close();
        ArrayAdapter<String> adapterForClassList = new ArrayAdapter<String>(ViewClassList.this, android.R.layout.simple_list_item_1, class_names);
        list.setAdapter(adapterForClassList);
        list.setOnItemClickListener(
            	new AdapterView.OnItemClickListener() {
            		ArrayAdapter<String> adapterForFileNames;
            		File[] listFilename;
            		ArrayList<String> filename_list = new ArrayList<String>();
            		String prevFilePath;
        			File file = Environment.getExternalStorageDirectory();
            		
        			
        			public void onItemClick(AdapterView<?> parent,View view, int position,long id)
            		{
            			AlertDialog.Builder builder = new AlertDialog.Builder(ViewClassList.this);
            			builder.setTitle("Select File");

            			ListView modeList = new ListView(ViewClassList.this);
            		
            			selectedClassName = (String) (list.getItemAtPosition(position));
            			
            			acnd = new AttendanceClassNameDAO(ViewClassList.this);
            			acnd.open();
            			boolean hasClassList = acnd.hasClassList(selectedClassName);
            			acnd.close();
            			if(hasClassList==false){
                  			filename_list.clear();
                			prevFilePath = file.getAbsolutePath();
                	        this.listFilename = file.listFiles();
                	    	
                	        for (File filenameItem : listFilename) {
                	        	filename_list.add(filenameItem.getAbsolutePath());
                			}
                	        
                	        adapterForFileNames = new ArrayAdapter<String>(ViewClassList.this, android.R.layout.simple_list_item_1, filename_list);
                	        modeList.setAdapter(adapterForFileNames);
                			
                			builder.setView(modeList).setNeutralButton("Back to one directory", 
                					new DialogInterface.OnClickListener() {
    									
    									@Override
    									public void onClick(DialogInterface dialog, int which) {
    										
    									}
    								}
                			);
                			modeList.getId();
                		
                			
                			dialog = builder.create();
                			dialog.show();
                			((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener(){

    							@Override
    							public void onClick(View view) {
    								// TODO Auto-generated method stub
    								if(!prevFilePath.equals(file.getAbsoluteFile()))
    								{
    									File filepath = new File(prevFilePath);
    									listFilename = filepath.listFiles();
    			        				filename_list.clear();
    			        				for (File filenameItem :listFilename) {
    			        					filename_list.add(filenameItem.getAbsolutePath());
    									}
    			        				adapterForFileNames.notifyDataSetChanged();
    								}
    								else{
    									Toast.makeText(ViewClassList.this, "Root director", Toast.LENGTH_SHORT).show();
    								}
    							}
                				
                			});
                			
                			modeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    							@Override
    							public void onItemClick(AdapterView<?> parent,
    									View view, int position, long id) {
    								
    								String filePath = (String) parent.getItemAtPosition(position);
    			        			File selectedFile = new File(filePath);
    			        			if(selectedFile.isDirectory())
    			        			{
    			        				listFilename = selectedFile.listFiles();
    			        				filename_list.clear();
    			        				for (File filenameItem :listFilename) {
    			        					filename_list.add(filenameItem.getAbsolutePath());
    									}
    			        				adapterForFileNames.notifyDataSetChanged();
    			        				Toast.makeText(getApplicationContext(), "Directory",Toast.LENGTH_LONG).show();
    									
    			        			}
    			        			else if(selectedFile.getPath().substring(selectedFile.getPath().lastIndexOf(".")).equals(".csv")){
    			        				//if csv file
    			        				ViewClassList.this.selectedFilename = selectedFile.getPath();
    			        				new loadCsvContent().execute("");
    			        				acnd.open();
    			        				acnd.setClassListToTrue(selectedClassName);
    			        				acnd.close();
    			        			}
    
    							}
    						});

            			}else{
            				//viewMenu
            				Log.d("ViewClassList","Has classlist");
            			}
            			
  
            		}
    			}
            );
        
	}
	
	public class loadCsvContent extends AsyncTask<String, Integer, String>{

		ProgressDialog pd;
		int classSize;
		protected void onPreExecute(){
			acnd.open();
			classSize = acnd.getClassSizeFromDb(selectedClassName);
			acnd.close();
			pd = new ProgressDialog(ViewClassList.this);
			pd.setTitle("Reading CSV file");
			pd.setMessage("Please wait");
			pd.setMax(classSize);
			pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pd.setIndeterminate(false);
			pd.show();
		
		}
		
		@Override
		protected String doInBackground(String... params){
		//	Log.d("ViewClassList",selectedFilename);
			//Toast.makeText(ViewClassList.this, selectedFilename, Toast.LENGTH_LONG).show();
			// TODO Auto-generated method stub
			FileReader fr;
			try {
				fr = new FileReader(selectedFilename);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				fr = null;
				e.printStackTrace();
			}
			CSVReader reader = new CSVReader(fr);
			
			
			String[] nextLine;
			AttendanceListDAO ald = new AttendanceListDAO(ViewClassList.this);
			ald.open();
			try {
				while ((nextLine = reader.readNext()) != null) {
					ald.insertClassListToDb(nextLine[0], nextLine[1],selectedClassName,nextLine[2],nextLine[3]);
					pd.incrementProgressBy(1);
					Thread.sleep(100);
					
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			pd.dismiss();
			dialog.dismiss();
			ald.close();
			return null;
		}
		
		protected void onProgressUpdate(Integer... progress){
			publishProgress(progress);
		}
		
		protected void onPostExecute(){
			
		}
		
		
	}
}