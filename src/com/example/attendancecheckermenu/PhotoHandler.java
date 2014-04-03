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
import android.graphics.BitmapFactory.Options;
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
  private ArrayList<String> images;
  private int position;
  private Camera mCamera;
  private GridView gridview;
  private ArrayList<String> studentName;
  private PhotoDAO pd;
  private String dateTaken;
  private String studentNumber;
  private String className;
  
  public PhotoHandler(Context context,String filename,String studentNumber,String className,File pictureFile,String dateTaken,View v,View v2,ArrayList<String> images,ArrayList<String> studentName,int position,Camera mCamera,GridView gridview) {
    this.context = context;
    this.filename = filename;
    this.className = className;
    this.studentNumber = studentNumber;
    this.dateTaken = dateTaken;
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
          Toast.LENGTH_SHORT).show();
      Options opts = new Options();
  	  opts.inSampleSize = 4;
  	  Bitmap bitmap = BitmapFactory.decodeFile(pictureFile.getAbsolutePath(),opts);
  	  
  	  Matrix m = new Matrix();
  	  m.postRotate(270);
  	  Bitmap rotated = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),m,true);
      images.set(position, pictureFile.getAbsolutePath());
      img.setImageBitmap(rotated);
      pd = new PhotoDAO(context);
      AttendanceListDAO ald = new AttendanceListDAO(context);
      ald.open();
      ald.studentTakesPic(className, studentName.get(position));
      ald.setOriginalPicture(pictureFile.getAbsolutePath(), className, studentName.get(position));

      ald.close();
      pd.open();   
      
      if(pd.photoHasBeenTaken(studentName.get(position), className, dateTaken)){
    	  pd.replacePhotoPath(pictureFile.getAbsolutePath(), studentNumber, studentName.get(position), dateTaken, className);
    	  Toast.makeText(context, "Photo replaced!", Toast.LENGTH_SHORT).show();
      }
      else pd.insertPhotoToDb(pictureFile.getAbsolutePath(), studentNumber, studentName.get(position), dateTaken, className);

    
//    TODO get the student number of the student
      pd.close();
      
    } catch (Exception error) {
      Log.d("MainActivity", "File" + filename + "not saved: "
          + error.getMessage());
      Toast.makeText(context, "Image could not be saved.",
          Toast.LENGTH_LONG).show();
    }
    
  }
  
  private File getDir() {
    File sdDir = Environment
      .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    return new File(sdDir, "CameraAPIDemo");
  }
} 