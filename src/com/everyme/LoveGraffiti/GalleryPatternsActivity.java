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

public class GalleryPatternsActivity extends Activity {
    private GridView gridView;
    private GridViewAdapter adapter;
	private ArrayList<GridItem> items = new ArrayList<GridItem>();
    private final String ASSET_VALUE_PATTERN = "asset_pattern";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_pattern);
        
        addDataToGrid();
        
        gridView = (GridView) findViewById(R.id.gridView);
        adapter = new GridViewAdapter(this, items);
        gridView.setAdapter(adapter);
        
        gridView.setOnItemClickListener(new OnItemClickListener() {
        
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				Intent intent = new Intent(GalleryPatternsActivity.this, GameActivity.class);
				String out = "";
				if(!out.equals(items.get(position).getPath())) out = items.get(position).getPath();
				intent.putExtra(ASSET_VALUE_PATTERN, out);
				setResult(RESULT_OK, intent);
			    finish();
			} 
		});
		
	}

   public void addDataToGrid(){
	   Bitmap bitmap = null;
	   try {
		   for(int i = 6; i < 28; i++) {
			   String strName = "patterns/pic_" + Integer.toString(i) + ".png";
			   InputStream ims = getAssets().open(strName);
			   bitmap = BitmapFactory.decodeStream(ims, null, null);
			   items.add(new GridItem(bitmap, strName)); 
		   }
	   } catch(IOException e) {
		   e.printStackTrace();
	   }
   }


}