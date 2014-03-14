package com.example.attendancecheckermenu;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DateGalleryAdapter extends BaseAdapter{
	
	private Context context;
	private ArrayList<Photo> values;
	
	public DateGalleryAdapter(Context context,ArrayList<Photo> values){
		this.context = context;
		this.values = values;
	}
	
	public View getView(int position, View listView, ViewGroup parent) {
	    View v = listView;
		
	   	LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	     
	    if(listView==null){
        	
	    	v = inflater.inflate(R.layout.griditem_classgrid, parent, false);
        	
            TextView textView1 = (TextView) v.findViewById(R.id.griditem_name);
    	    TextView textView2 = (TextView) v.findViewById(R.id.griditem_stdnum);
    	    ImageView imageView = (ImageView) v.findViewById(R.id.griditem_pic);
    	    
    	    textView2.setText(values.get(position).getStdNum());
    	    textView1.setText(values.get(position).getStdName());
    	    
    	    if(!values.get(position).getPathOfFile().equals("nopic")){
    	    	Options opts = new Options();
    	    	opts.inSampleSize = 4;
    	    	Bitmap bitmap = BitmapFactory.decodeFile(values.get(position).getPathOfFile(),opts);
    			imageView.setImageBitmap(bitmap);
    	    }
    	    else{
    			Bitmap bmp = Bitmap.createBitmap(85,85,Config.ARGB_8888);
    			imageView.setImageBitmap(bmp);
    	    }
	    
	    }
	    else{
        	v = inflater.inflate(R.layout.griditem_classgrid, parent, false);
	    	
	        TextView textView1 = (TextView) v.findViewById(R.id.griditem_name);
		    TextView textView2 = (TextView) v.findViewById(R.id.griditem_stdnum);
		    ImageView imageView = (ImageView) v.findViewById(R.id.griditem_pic);
		    
		    textView2.setText(values.get(position).getStdNum());
		    textView1.setText(values.get(position).getStdName());
		    
		    if(!values.get(position).getPathOfFile().equals("nopic")){
    	    	Options opts = new Options();
    	    	opts.inSampleSize = 4;
    	    	Bitmap bitmap = BitmapFactory.decodeFile(values.get(position).getPathOfFile(),opts);
    			imageView.setImageBitmap(bitmap);
		    }
    	    else{
    			Bitmap bmp = Bitmap.createBitmap(85,85,Config.ARGB_8888);
    			imageView.setImageBitmap(bmp);
    	    }

	    }
	    
	    // change the icon for Windows and iPhone
	    return v;
	  }
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return values.size();
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
}

