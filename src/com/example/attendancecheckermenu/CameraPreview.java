package com.example.attendancecheckermenu;

import java.io.IOException;


import android.util.Log;
import android.view.*;
import android.widget.Toast;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.content.Context;
import android.content.pm.PackageManager;

/** A basic Camera preview class */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    public static Camera mCamera;
    public static int cameraId = 0;
    private static final String TAG = "Preview";
    private Context context;
    
    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        context = this.context;
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
        	mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    	if (mCamera != null) {
    		mCamera.release();
    		mCamera=null;
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {        
        if (mHolder.getSurface() == null){
          // preview surface does not exist
          return;
        }

        try {
            mCamera.stopPreview();
        } catch (Exception e){
        }
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }
    
    private boolean checkCameraHardware(Context context) {
	    if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
	        // this device has a camera
	        return true;
	    } else {
	        // no camera on this device
	        return false;
	    }
	}
  
   
    
  public Camera getCameraInstance(){
	    Camera c = null;
	    try {
	        c = Camera.open(); // attempt to get a Camera instance
	    }
	    catch (Exception e){
	        // Camera is not available (in use or does not exist)
	    }
	    return c; // returns null if camera is unavailable
	}

}
