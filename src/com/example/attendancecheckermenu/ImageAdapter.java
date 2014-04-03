package com.example.attendancecheckermenu;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageAdapter extends BaseAdapter{

	private Context mContext;
//	private String[] classList;
	private ArrayList<String> classList;
	private ArrayList<String> images;
	//	private Bitmap[] images;
	private ImageView img;
	private Bitmap bmp;
	
	public ImageAdapter(Context c,ArrayList<String> classList,ArrayList<String> images,ImageView img) {
		this.classList = classList;
		mContext = c;
		this.images = images;
		this.img = img;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return classList.size();
		//return classList.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub 
		return classList.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		TextView studentName;
		View gridView;
		Bitmap b = Bitmap.createBitmap(55,55,Bitmap.Config.ARGB_8888);
		
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);		
		
		
		if (convertView == null) {  // if it's not recycled, initialize some attributes
			gridView = new View(mContext);
			gridView = inflater.inflate(R.layout.mobile, null);
			
			studentName = (TextView) gridView
					.findViewById(R.id.grid_item_label);
			studentName.setText(classList.get(position).substring(0, classList.get(position).indexOf(",")));
			bmp = Bitmap.createBitmap(85,85,Config.ARGB_8888);
			imageView = (ImageView) gridView.findViewById(R.id.grid_item_image);
			if(images.get(position).equals("nopic"));// imageView.setImageBitmap(bmp);
			else {
			    Options opts = new Options();
			  	opts.inSampleSize = 8;
			  	Bitmap bitmap = BitmapFactory.decodeFile(images.get(position),opts);
			  	Matrix m = new Matrix();
			  	m.postRotate(270);
			  	Bitmap rotated = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),m,true);
//				imageView.setImageBitmap(bitmap);
				imageView.setImageBitmap(rotated);
			}
			
		    
        } else {
        	gridView = (View) convertView;
        	studentName = (TextView) gridView
					.findViewById(R.id.grid_item_label);
			studentName.setText(classList.get(position).substring(0, classList.get(position).indexOf(",")));
			Bitmap bmp = Bitmap.createBitmap(85,85,Config.ARGB_8888);
			imageView = (ImageView) gridView.findViewById(R.id.grid_item_image);
			if(images.get(position).equals("nopic")) {
				
//				imageView.setImageBitmap(bmp);
			}
			else {
				Options opts = new Options();
			  	opts.inSampleSize = 8;
			  	Bitmap bitmap = BitmapFactory.decodeFile(images.get(position),opts);
			  	Matrix m = new Matrix();
			  	m.postRotate(270);
			  	Bitmap rotated = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),m,true);
//				imageView.setImageBitmap(bitmap);
				imageView.setImageBitmap(rotated);
				
			}
        }

		return gridView;
	}
	
	

	public void update(){
		notifyDataSetChanged();
	}
}
