package com.example.attendancecheckermenu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;


import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.os.*;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.*;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.content.*;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import android.widget.AdapterView.OnItemClickListener;

import java.util.*;


public class MainActivity extends Activity {
  private Camera mCamera;
  private int cameraId;
  private ImageAdapter imgAdapter;
  private Adapter adapter;
  private CameraPreview mPreview;
  private LayoutInflater inflater;
  private ImageView imageV;
  public final static String DEBUG_TAG = "MainActivity";
  private GridView gridview = null;
  private GridView gridviewButtons = null;
  static ArrayList<String> classListArrayTemp;
  private ArrayList<String> sectionList;
  static ArrayList<Bitmap> pictureArrayTemp;
  private AttendanceListDAO ald;
  private String className;
  private String currSection;
  private HashMap<String,ArrayList<String>> sectionMap;
  private HashMap<String,ArrayList<Bitmap>> bitMap;
  private HashMap<String,ArrayList<String>> studentNumMap;
  
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.cam_app);
    inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    gridview = (GridView) findViewById(R.id.gridview);
    gridviewButtons = (GridView) findViewById(R.id.pictureGallery);
    ald = new AttendanceListDAO(getApplicationContext());
    ald.open();
    className = getIntent().getExtras().getString("Class Name");
    ald.viewClassListFromClass(className);
    
    sectionList = ald.viewAllSections(className);
    Log.d("MainActivity",sectionList.size()+"");
    currSection = sectionList.get(0);
    
    int index=0;
    
    sectionMap = new HashMap<String, ArrayList<String>>();
    studentNumMap = new HashMap<String, ArrayList<String>>();
    bitMap = new HashMap<String, ArrayList<Bitmap>>();
    
    for(String s: sectionList){
    	
    	ArrayList<String> studTemp = ald.viewClassListPerSection(className, s);
    	sectionMap.put(s, studTemp);
    	ArrayList<String> studNTemp = ald.viewClassListPerSectionStdNum(className, s);
    	studentNumMap.put(s, studNTemp);
    	
    	ArrayList<Bitmap> bitTemp = new ArrayList<Bitmap>();
    	for(int i=0;i<sectionMap.get(s).size();i++){
    		Bitmap bmp = Bitmap.createBitmap(85,85,Config.ARGB_8888);
    		bitTemp.add(bmp);
    	}
    	bitMap.put(s, bitTemp);
    	index++;
    }
    
    classListArrayTemp = sectionMap.get(currSection);
    pictureArrayTemp = bitMap.get(currSection);
    imgAdapter = new ImageAdapter(this,classListArrayTemp,pictureArrayTemp,imageV);
    gridview.setAdapter(imgAdapter);
    gridviewButtons.setAdapter(new ButtonAdapter(this, sectionList));
    ald.close();
    gridviewButtons.setOnItemClickListener(new OnItemClickListener() {
    	
    	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
    		currSection = sectionList.get(position);
    		classListArrayTemp = sectionMap.get(currSection);
    		pictureArrayTemp = bitMap.get(currSection);
    		imgAdapter = new ImageAdapter(getApplicationContext(),classListArrayTemp,pictureArrayTemp,imageV);
    		gridview.setAdapter(imgAdapter);
    	}
    	
    });
    gridview.setOnItemClickListener(new OnItemClickListener() {
    	
    	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
    		File pictureFileDir = getDir();

    	    if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {

    	      Toast.makeText(MainActivity.this, "Can't create directory to save image.",Toast.LENGTH_LONG).show();

    	    }
    	    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yy hh:mm:ss");
    	    String date = dateFormat.format(new Date());
    	    String photoFile = classListArrayTemp.get(position)+" "+ date + ".jpg";
    	    SimpleDateFormat dateFormatmdy = new SimpleDateFormat("MM-dd-yy");
    	    String date2 = dateFormatmdy.format(new Date());
    	    String filename = pictureFileDir.getPath() + File.separator + photoFile;
    	    File pictureFile = new File(filename);
    	    ImageView img = (ImageView) v.findViewById(R.id.grid_item_image);
            TextView txt = (TextView) v.findViewById(R.id.grid_item_label);
            String studentNumber = studentNumMap.get(currSection).get(position);
            Log.d("MainActivity",studentNumber);
//    	    mCamera.takePicture(null, null, new PhotoHandler(getApplicationContext(),filename,pictureFile,img,txt,pictureArrayTemp,classListArrayTemp,position,mCamera,gridview,imgAdapter));
    	 
            mCamera.takePicture(null, null, new PhotoHandler(getApplicationContext(),filename,studentNumber,className, pictureFile,date2, img,txt,pictureArrayTemp,classListArrayTemp,position,mCamera,gridview));
    	}
    });
    
   }

  
  public void finishAttendance(View view){
	  ald = new AttendanceListDAO(getApplicationContext());
	  ald.open();

	  ArrayList<ClassList> classList = ald.viewClassListFromClass(className);
	  
	  SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yy");
	  String dateTaken = dateFormat.format(new Date());
	  Log.d("MainActivity",dateTaken);
	  
	  
	  for (ClassList cl : classList) {
		if(cl.hasPictureTakenM()){
		}	
		else{			
			AttendanceListDAO ald = new AttendanceListDAO(getApplicationContext());
			ald.open();
			ald.addAbsent(cl.getStudentName(),className,1);
			ald.close();
			PhotoDAO pd = new PhotoDAO(getApplicationContext());
			pd.open();
			pd.insertPhotoToDb("no picture", cl.getStudentNumber(), cl.getStudentName(), dateTaken, className);			
			pd.close();
		}
	  }
	  
	  ald.resetToFalse(className);
	  ald.close();
	  finish();
  }
  
  public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.action_bar_attendance, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
		
		switch (item.getItemId()) {
	        case R.id.finish_attendance_op:
	        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        	builder.setMessage("Do you want to finish checking the attendance?");
	        	builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						finishAttendance(null);
						
					}
				});
	        	builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
					}
				});
	        	
	        	AlertDialog dialog = builder.create();
	        	dialog.show();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
  
  private File getDir() {
	    File sdDir = Environment
	      .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
	    return new File(sdDir, "CameraAPIDemo");	
	  }
  

 
  private void releaseCamera(){
      if (mCamera != null){
          mCamera.release();        // release the camera for other applications
          mCamera = null;
      }
  }
  
  public void onResume()
  {
	  super.onResume();
	
	  int n = Camera.getNumberOfCameras();
		for(int i=0;i<n;i++){
			CameraInfo info = new CameraInfo();
			 Camera.getCameraInfo(i, info);
			 if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
			        cameraId = i;
			        break;
			}
		}
	  
		Log.d("MainActivity",cameraId+"");
		
	  mCamera = Camera.open(cameraId);
	  mPreview = new CameraPreview(this, mCamera);
	    FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
	  	ViewGroup.LayoutParams params = mPreview.getLayoutParams();
	    preview.addView(mPreview);
	  
	  
  }
  
  
  
  protected void onPause() {
	    if (mCamera != null) {
	      mCamera.release();
	     
	    }
	    super.onPause();
}
  
} 