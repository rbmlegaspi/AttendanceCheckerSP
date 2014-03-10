package com.example.attendancecheckermenu;

import android.app.Activity;


import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.os.*;
import android.util.Log;
import android.view.LayoutInflater;
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
    //text = (EditText) findViewById(R.id.editText1);
    inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    gridview = (GridView) findViewById(R.id.gridview);
    gridviewButtons = (GridView) findViewById(R.id.gridView1);
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
    	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmm");
    	    String date = dateFormat.format(new Date());
    	    String photoFile = classListArrayTemp.get(position) + date + ".jpg";
    	    String filename = pictureFileDir.getPath() + File.separator + photoFile;
    	    File pictureFile = new File(filename);
    	    ImageView img = (ImageView) v.findViewById(R.id.grid_item_image);
            TextView txt = (TextView) v.findViewById(R.id.grid_item_label);
            String studentNumber = studentNumMap.get(currSection).get(position);
            Log.d("MainActivity",studentNumber);
//    	    mCamera.takePicture(null, null, new PhotoHandler(getApplicationContext(),filename,pictureFile,img,txt,pictureArrayTemp,classListArrayTemp,position,mCamera,gridview,imgAdapter));
    	 
            mCamera.takePicture(null, null, new PhotoHandler(getApplicationContext(),filename,studentNumber,className, pictureFile,date, img,txt,pictureArrayTemp,classListArrayTemp,position,mCamera,gridview));
    	}
    });
    
   }

  
  public void finishAttendance(View view){
	  ald = new AttendanceListDAO(getApplicationContext());
	  ald.open();

	  ArrayList<ClassList> classList = ald.viewClassListFromClass(className);
	  
	  for (ClassList cl : classList) {
		if(cl.hasPictureTakenM()){
			Log.d("MainActivity",cl.getStudentName()+" is present");
		}	
		else{
			Log.d("MainActivity",cl.getStudentName()+" is absent");
		}
	  }
	  
	  ald.resetToFalse(className);
	  
	  ald.close();
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