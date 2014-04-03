package com.example.attendancecheckermenu;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.SQLException;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendancecheckermenu.FullscreenActivity.loadCsvContent;
import com.example.attendancecheckermenu.util.SystemUiHider;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class FullscreenActivity extends Activity {

	String selectedClassName;
	String selectedFilename;
	AttendanceClassNameDAO acnd;
	String prevFilePath;
	Dialog dialog;
	int cameraId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
  //          WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        
        setContentView(R.layout.activity_fullscreen);

	}
	
	public void createMenu(View view){
		Intent intent = new Intent(this, CreateMenu.class);
		startActivity(intent);
	}

	public void viewMenu(View view)
	{
		ArrayAdapter<String> adapterForClassNames;
		ArrayList<String> classNames = new ArrayList<String>();
		ArrayList<ClassName> classNameArray;
		
		AlertDialog.Builder builder = new AlertDialog.Builder(FullscreenActivity.this);
		builder.setTitle("Choose a class");

		final ListView modeList = new ListView(FullscreenActivity.this);
	
		acnd = new AttendanceClassNameDAO(FullscreenActivity.this);
		acnd.open();
		
		classNameArray = acnd.viewAllClasses();
        for (int i = 0; i < classNameArray.size(); i++) {
			ClassName classObj = classNameArray.get(i);
			String listItem = classObj.getClassName();
			classNames.add(listItem);
			Log.d("FullscreenActivity",listItem);
        }
		
		acnd.close();

		adapterForClassNames = new ArrayAdapter<String>(FullscreenActivity.this, android.R.layout.simple_list_item_1, classNames);
		modeList.setAdapter(adapterForClassNames);
		modeList.setOnItemClickListener(
        	new AdapterView.OnItemClickListener() {
        		ArrayAdapter<String> adapterForFileNames;
        		File[] listFilename;
        		ArrayList<String> filename_list = new ArrayList<String>();
        		File file = Environment.getExternalStorageDirectory();
    		
        		
        		public void onItemClick(AdapterView<?> parent,View view, int position,long id)
        		{
        			
        			  	AlertDialog.Builder builder = new AlertDialog.Builder(FullscreenActivity.this);
            			builder.setTitle("Select File");

            			ListView llist = new ListView(FullscreenActivity.this);
            			
            			selectedClassName = (String) (modeList.getItemAtPosition(position));
            			
            			acnd = new AttendanceClassNameDAO(FullscreenActivity.this);
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
                	        
                	        adapterForFileNames = new ArrayAdapter<String>(FullscreenActivity.this, android.R.layout.simple_list_item_1, filename_list);
                	        llist.setAdapter(adapterForFileNames);
                			
                			builder.setView(llist).setNeutralButton("Back to one directory", 
                					new DialogInterface.OnClickListener() {
    									
    									@Override
    									public void onClick(DialogInterface dialog, int which) {
    										
    									}
    								}
                			);
                			llist.getId();
                		
                			
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
    									Toast.makeText(FullscreenActivity.this, "Root director", Toast.LENGTH_SHORT).show();
    								}
    							}
                				
                			});
                			
                			llist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
    			        				FullscreenActivity.this.selectedFilename = selectedFile.getPath();
    			        				new loadCsvContent().execute("");
    			        				
    			        				Toast.makeText(FullscreenActivity.this, "Successfully uploaded classlist", Toast.LENGTH_SHORT).show();
    			        				acnd.open();
    			        				acnd.setClassListToTrue(selectedClassName);
    			        				acnd.close();
    			        			}
    
    							}
    						});

            			}else{
            				//viewMenu
            				viewClassList();
            			}
            			
  
            		selectedClassName = (String) (modeList.getItemAtPosition(position));
        			
        		}
        	}
		);
		builder.setView(modeList);
		dialog = builder.create();
		dialog.show();
		
		
		//Intent intent = new Intent(this, FullscreenActivity.class);
		//startActivity(intent);
	
	
	}
	public void viewClassList(){

		final PasswordDAO passDAO = new PasswordDAO(FullscreenActivity.this);
		
		final Dialog d = new Dialog(FullscreenActivity.this);
		d.setTitle("Enter password to continue");
		d.setContentView(R.layout.confirm_password);
		Button bd = (Button) d.findViewById(R.id.enterPassButton);
		bd.setOnClickListener(new View.OnClickListener() {				
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TextView tv = (TextView) d.findViewById(R.id.enterPass);
				String oPass =  tv.getText()+"";
				passDAO.open();
				String message = passDAO.confirmPassword(oPass);
				passDAO.close();
				
				if(message.equals("Invalid password")){
				}
				else{
					d.dismiss();
					Intent intent = new Intent(FullscreenActivity.this,ViewClassList.class);
					intent.putExtra("className", selectedClassName);
					startActivity(intent);
				}
				Toast.makeText(FullscreenActivity.this, message, Toast.LENGTH_SHORT).show();
			}
		});

		d.show();
	}
	
	public void editMenu(View view)
	{
	//	Intent intent = new Intent(this, FileManager.class);
	//	startActivity(intent);
	}
	
	public void takeAttendance(View view){

		ArrayAdapter<String> adapterForClassNames;
		ArrayList<String> classNames = new ArrayList<String>();
		ArrayList<ClassName> classNameArray;
		
		AlertDialog.Builder builder = new AlertDialog.Builder(FullscreenActivity.this);
		builder.setTitle("Choose a class");

		final ListView modeList = new ListView(FullscreenActivity.this);
	
		acnd = new AttendanceClassNameDAO(FullscreenActivity.this);
		acnd.open();
		
		classNameArray = acnd.viewAllClasses();
        for (int i = 0; i < classNameArray.size(); i++) {
			ClassName classObj = classNameArray.get(i);
			String listItem = classObj.getClassName();
			classNames.add(listItem);
		}
		
		acnd.close();

		adapterForClassNames = new ArrayAdapter<String>(FullscreenActivity.this, android.R.layout.simple_list_item_1, classNames);
		modeList.setAdapter(adapterForClassNames);
		modeList.setOnItemClickListener(
        	new AdapterView.OnItemClickListener() {
        		
        		Dialog d;
        		public void onItemClick(AdapterView<?> parent,View view, int position,long id)
        		{
        			selectedClassName = (String) (modeList.getItemAtPosition(position));
        			
        			acnd = new AttendanceClassNameDAO(FullscreenActivity.this);
        			acnd.open();
        			boolean hasClassList = acnd.hasClassList(selectedClassName);
        			acnd.close();
        			
        			if(hasClassList){
		    			Intent intent = new Intent(FullscreenActivity.this, MainActivity.class);
		    			intent.putExtra("Class Name",selectedClassName);
		    			startActivity(intent);
        			///	Toast.makeText(getApplicationContext(), "asda",Toast.LENGTH_LONG).show();
        			}
        			else{
        				AlertDialog.Builder bd = new AlertDialog.Builder(FullscreenActivity.this);
        				bd.setTitle("No classlist found");
        				bd.setMessage("No classlist found. Please upload one in the View Classlist menu");
        				bd.setNeutralButton("Ok", new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});
        				dialog = bd.create();
        				dialog.show();
        			}
        			
        		}
        			
        	});
		builder.setView(modeList);
		dialog = builder.create();
		dialog.show();
		
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.action_bar, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.addNewClassList:
	        	createMenu(null);
	        	return true;
	        case R.id.viewClassList:
	        	viewMenu(null);
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	
	
	public void setPassword(View view){
		final PasswordDAO passDAO = new PasswordDAO(FullscreenActivity.this);
		final Dialog d = new Dialog(FullscreenActivity.this);
		passDAO.open();
		String s = passDAO.getPassword();
		passDAO.close();
		if(s!=null){
			d.setContentView(R.layout.set_password_layout);
			d.setTitle("Change password");
			Button dialogButton = (Button) d.findViewById(R.id.confirm);
			// if button is clicked, close the custom dialog
			dialogButton.setOnClickListener(new View.OnClickListener() {				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					TextView tv = (TextView) d.findViewById(R.id.oPassField);
					String oPass =  tv.getText()+"";
					TextView tv2 = (TextView) d.findViewById(R.id.nPassField);
					String nPass = tv2.getText()+"";
					passDAO.open();
					String message = passDAO.setPassword(oPass, nPass);
					passDAO.close();

					Toast.makeText(FullscreenActivity.this, message, Toast.LENGTH_SHORT).show();
					
					if(!message.equals("Invalid password")){
						d.dismiss();
					}
					
					
				}
			});

			d.show();
		}
		else{
			d.setContentView(R.layout.set_new_password);
			d.setTitle("Enter new password");
			Button dialogButton = (Button) d.findViewById(R.id.setNewPass);
			// if button is clicked, close the custom dialog
			dialogButton.setOnClickListener(new View.OnClickListener() {				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					TextView tv = (TextView) d.findViewById(R.id.newPassN);
					String oPass =  tv.getText()+"";
					TextView tv2 = (TextView) d.findViewById(R.id.conPass);
					String nPass = tv2.getText()+"";
					if(!oPass.equals(nPass)){
						Log.d("FullscreenActivity",oPass+" "+nPass);
					}
					else{
						passDAO.open();
						passDAO.setPassword(oPass);
						passDAO.close();
						d.dismiss();
					}
					
				}
			});

			d.show();
		}

	}
	
	public void debug(View view)
	{
				
	
		AttendanceClassNameDAO acnd = new AttendanceClassNameDAO(getApplicationContext());
		acnd.open();
//		acnd.dropEverything();
		acnd.dropAndCreateClassListDebug();
		acnd.close();
	
		Toast.makeText(FullscreenActivity.this, "Re-created whole db", Toast.LENGTH_SHORT).show();
	}
	
	public class loadCsvContent extends AsyncTask<String, Integer, String>{

		ProgressDialog pd;
		int classSize;
		protected void onPreExecute(){
			acnd.open();
			classSize = acnd.getClassSizeFromDb(selectedClassName);
			acnd.close();
			pd = new ProgressDialog(FullscreenActivity.this);
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
			AttendanceListDAO ald = new AttendanceListDAO(FullscreenActivity.this);
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
