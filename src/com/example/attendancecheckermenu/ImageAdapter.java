package com.example.attendancecheckermenu;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
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
	private ArrayList<Bitmap> images;
	//	private Bitmap[] images;
	private ImageView img;
	private Bitmap bmp;
	
	public ImageAdapter(Context c,ArrayList<String> classList,ArrayList<Bitmap> images,ImageView img) {
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
			studentName.setText(classList.get(position));
			bmp = Bitmap.createBitmap(85,85,Config.ARGB_8888);
			imageView = (ImageView) gridView.findViewById(R.id.grid_item_image);
			if(images.get(position)==null) imageView.setImageBitmap(bmp);
			else {
				imageView.setImageBitmap(images.get(position));
				
			}
			
		    
        } else {
        	gridView = (View) convertView;
        	studentName = (TextView) gridView
					.findViewById(R.id.grid_item_label);
			studentName.setText(classList.get(position));
			Bitmap bmp = Bitmap.createBitmap(85,85,Config.ARGB_8888);
			imageView = (ImageView) gridView.findViewById(R.id.grid_item_image);
			if(images.get(position)==null) imageView.setImageBitmap(bmp);
			else {
				
				imageView.setImageBitmap(images.get(position));
				
			}
        }

		return gridView;
	}
	
	

	public void update(){
		notifyDataSetChanged();
	}
}
