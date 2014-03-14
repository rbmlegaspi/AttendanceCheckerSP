package com.example.attendancecheckermenu;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PictureAdapter extends BaseAdapter{

	ArrayList<String> dateTaken;
	ArrayList<String> pathName;
	Context context;
	Bitmap b;
	
	public PictureAdapter(ArrayList<String> dateTaken, ArrayList<String> pathName,Context context){
		this.dateTaken = dateTaken;
		this.pathName = pathName;
		this.context = context;
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dateTaken.size();
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
	public View getView(int position, View convertView, ViewGroup vg) {
		View gridView = convertView;
		ImageView imageView;
		TextView dateTakenView;
		
		Bitmap b = Bitmap.createBitmap(55,55,Bitmap.Config.ARGB_8888);

	      LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);		
			
			
		if (convertView == null) {  // if it's not recycled, initialize some attributes
				gridView = new View(context);
				gridView = inflater.inflate(R.layout.griditem_picture, null);
				dateTakenView = (TextView) gridView.findViewById(R.id.date);
				dateTakenView.setText(dateTaken.get(position));

				Bitmap bmp = Bitmap.createBitmap(85,85,Config.ARGB_8888);
				imageView = (ImageView) gridView.findViewById(R.id.attendancePicture);
				
				if(pathName.get(position).equals("no picture")) imageView.setImageBitmap(bmp);
				else {
	    	    	Options opts = new Options();
	    	    	opts.inSampleSize = 4;
					Bitmap bitmap = BitmapFactory.decodeFile(pathName.get(position),opts);
					
				}

	    }
		else{
        	gridView = (View) convertView;
			gridView = inflater.inflate(R.layout.griditem_picture, null);
			dateTakenView = (TextView) gridView.findViewById(R.id.date);
			dateTakenView.setText(dateTaken.get(position));

			Bitmap bmp = Bitmap.createBitmap(85,85,Config.ARGB_8888);
			imageView = (ImageView) gridView.findViewById(R.id.attendancePicture);
			
			if(pathName.get(position).equals("no picture")) imageView.setImageBitmap(bmp);
			else {
    	    	Options opts = new Options();
    	    	opts.inSampleSize = 4;
				Bitmap bitmap = BitmapFactory.decodeFile(pathName.get(position),opts);
				imageView.setImageBitmap(bitmap);
				
			}

		}
		

				
		// TODO Auto-generated method stub
		return gridView;
	}

}
