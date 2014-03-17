package com.everyme.LoveGraffiti;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import adapters.GridViewAdapter;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import classes.GridItem;

public class GalleryActivity extends Activity {
    private GridView gridView;
    private GridViewAdapter adapter;
	private ArrayList<GridItem> items = new ArrayList<GridItem>();
    private final String ASSET_VALUE = "asset";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.grid);
        
        addDataToGrid();
        
        gridView = (GridView) findViewById(R.id.gridView);
        adapter = new GridViewAdapter(this, items);
        gridView.setAdapter(adapter);
        
        gridView.setOnItemClickListener(new OnItemClickListener() {
        
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				Intent intent = new Intent(GalleryActivity.this, GameActivity.class);
				intent.putExtra(ASSET_VALUE, items.get(position).getPath());
				startActivity(intent);
			}
		});
		
	}

   public void addDataToGrid(){
	   Bitmap bitmap = null;
	   try{
		   for(int i = 1; i <= 11; i++) {
			   String strName = "images/wall" + Integer.toString(i) + ".jpg";
			   InputStream ims = getAssets().open(strName);
			   BitmapFactory.Options ops = new BitmapFactory.Options();
			   ops.inSampleSize = 2;
			   ops.inJustDecodeBounds = false;
			   bitmap = BitmapFactory.decodeStream(ims, null, ops);
			   items.add(new GridItem(bitmap, strName)); 
		   }
	   } catch(IOException e) {
		   e.printStackTrace();
	   }
   }


}