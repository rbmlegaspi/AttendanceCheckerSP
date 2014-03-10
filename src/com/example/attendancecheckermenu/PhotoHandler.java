package com.example.attendancecheckermenu;

import java.io.File;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.media.ExifInterface;
import android.os.Environment;
import android.view.View;
import android.text.style.ImageSpan;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PhotoHandler implements PictureCallback {

  private final Context context;
  private String filename = "";
  private File pictureFile;
  private ImageView img;
  private TextView txt;
  private ArrayList<Bitmap> images;
  private int position;
  private Camera mCamera;
  private GridView gridview;
  private ArrayList<String> studentName;
  private PhotoDAO pd;
  
  public PhotoHandler(Context context,String filename,File pictureFile,View v,View v2,ArrayList<Bitmap> images,ArrayList<String> studentName,int position,Camera mCamera,GridView gridview) {
    this.context = context;
    this.filename = filename;
    this.pictureFile = pictureFile;
    this.img = (ImageView) v;
    this.txt = (TextView) v2;
    this.images = images;
    this.position = position;
    this.mCamera = mCamera;
    this.studentName = studentName;
    this.gridview = gridview;
  }

  @Override
  public void onPictureTaken(byte[] data, Camera camera) {

    try {
    	mCamera.startPreview();
      FileOutputStream fos = new FileOutputStream(pictureFile);
      fos.write(data);
      fos.close();
      Toast.makeText(context, "New Image saved:" + pictureFile.getAbsolutePath(),
          Toast.LENGTH_LONG).show();
      
      Bitmap bitmap = BitmapFactory.decodeFile(pictureFile.getAbsolutePath());
      bitmap = Bitmap.createScaledBitmap(bitmap, 400, 300, false);
      images.set(position, bitmap);
      img.setImageBitmap(bitmap);
      pd = new PhotoDAO(context);
      pd.open();
      
//    TODO get the student number of the student
      pd.insertPhotoToDb(pictureFile.getAbsolutePath(), null, studentName.get(position), dateTaken, className);
      
      pd.close();
      
    } catch (Exception error) {
      Log.d(MainActivity.DEBUG_TAG, "File" + filename + "not saved: "
          + error.getMessage());
      Toast.makeText(context, "Image could not be saved.",
          Toast.LENGTH_LONG).show();
    }
    
  }
/*
  public void setFilename(String filename){
	  Toast.makeText(context, "New Image saved:" + filename,
	          Toast.LENGTH_LONG).show();
	  this.filename = filename;
  }
  
  public String getFilename(){
	  return this.filename;
  }*/
  
  private File getDir() {
    File sdDir = Environment
      .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    return new File(sdDir, "CameraAPIDemo");
  }
} 