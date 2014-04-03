package com.example.attendancecheckermenu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.BitmapFactory.Options;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class DialogForCompare extends BaseAdapter{

	private Context c;
	private String origPic, buttonPic;
	
	public DialogForCompare(Context c,String origPic,String buttonPic){
		this.c = c;
		this.origPic = origPic;
		this.buttonPic = buttonPic;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		View v = arg1;
		
		if(v==null){
			LayoutInflater inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	v = inflater.inflate(R.layout.dialog_android_compare, arg2, false);
		}
		
		ImageView img1 = (ImageView) v.findViewById(R.id.compare1);
		ImageView img2 = (ImageView) v.findViewById(R.id.compare2);
		
		Options opts = new Options();
	  	opts.inSampleSize = 8;
	  	Matrix m = new Matrix();
	  	m.postRotate(270);
	  	Bitmap bitmap = BitmapFactory.decodeFile(origPic,opts);
	  	Bitmap rotated = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),m,true);
	    img1.setImageBitmap(rotated);
	    
		Bitmap bitmap2 = BitmapFactory.decodeFile(buttonPic,opts);
		Bitmap rotated2 = Bitmap.createBitmap(bitmap2,0,0,bitmap2.getWidth(),bitmap2.getHeight(),m,true);
		img2.setImageBitmap(rotated2);
		
		return v;
	}
	
}
