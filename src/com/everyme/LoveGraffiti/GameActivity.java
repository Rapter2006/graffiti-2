package com.everyme.LoveGraffiti;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import classes.Graffiti;


public class GameActivity extends Activity implements OnClickListener {
	private final String SAVED_NUMBER_LEVEL = "saved_number";
	private Graffiti drawView;
	private SharedPreferences sPref;
	private ImageButton pickerButton, thicknessButton, galleryButton, shareButton, 
		 selectPattern;
	private int thikness = 10; 
	private int numberOfLevel = 0;
	private int dispersibility = 1;
	private String selectedImagePath = "";
	private final String ASSET_VALUE = "asset"; 
	private final String ASSET_VALUE_PATTERN = "asset_pattern";
	private String nString;
	
	public void loadFromGallery() {
		Intent intent = getIntent();
		nString = "";
		try {
			nString = intent.getStringExtra(ASSET_VALUE);
		    if(nString != null) {
		    	drawView.setBackgroundImageAssets(nString);
		    }
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
		
		loadInfo();	

		LinearLayout bannerPlace = (LinearLayout) findViewById(R.id.bannerPlaceholder);
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		int heught = Math.round((50 * metrics.heightPixels) / 320);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, heught);
		bannerPlace.setLayoutParams(layoutParams);
        
        selectPattern = (ImageButton) findViewById(R.id.buttonSelectPattern);
        selectPattern.setOnClickListener(this);
		
		pickerButton = (ImageButton) findViewById(R.id.buttonPickerColor);
		pickerButton.setOnClickListener(this);
		
		thicknessButton = (ImageButton) findViewById(R.id.buttonThickness);
		thicknessButton.setOnClickListener(this);
		
		galleryButton = (ImageButton) findViewById(R.id.buttonGallery);
		galleryButton.setOnClickListener(this);
		
		shareButton = (ImageButton) findViewById(R.id.buttonShare);
		shareButton.setOnClickListener(this);
		
		drawView = (Graffiti) findViewById(R.id.drawView);
	    drawView.setAllForLevel();
	    loadFromGallery();
	}
	
	
	private float getConvertedValue(int intVal) {
		float floatVal = 1.0f;
		floatVal = 0.5f * intVal;
		return floatVal;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK) {
			if(requestCode == 1) {
				Uri selectedImageURI = data.getData();
				selectedImagePath = getPath(selectedImageURI);
				drawView.setBackgroundImageGallery(selectedImagePath);
			}
			else
			if(requestCode == 2) {
				String answer = data.getStringExtra(ASSET_VALUE_PATTERN);
				if(!answer.equals("")) {
					drawView.setNewPattern(answer);
					drawView.setWithoutPattern(false);
				}
			}
		}
	}
	@Override
	public void onResume() {
		
		super.onResume();
	}
	
	private String getPath(Uri uri) {
		if(uri == null) { 
			return null;
		}
		String[] projection = {MediaStore.Images.Media.DATA};
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		if(cursor != null) {
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		}
		return uri.getPath();
	}
	
	private void openGallery() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select picture"), 1);
	}
	
	private void openWalls() {
		Intent intent = new Intent(this, GalleryActivity.class);
		startActivity(intent);
	}
	
	private void openGalleryOrWalls() {
		final Dialog galleryDialog = new Dialog(this);
		galleryDialog.setTitle("Gallery");
		galleryDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.gallery, (ViewGroup)findViewById(R.id.layGallery));
		galleryDialog.setContentView(layout);
		
		ImageButton startGallery = (ImageButton) layout.findViewById(R.id.buttonGallery);
		ImageButton startWalls = (ImageButton) layout.findViewById(R.id.buttonWalls);
		
		startGallery.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				openGallery();
				galleryDialog.dismiss();
			}
		});
		
		startWalls.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				openWalls();
				galleryDialog.dismiss();
			}
		});
		
		galleryDialog.show();
	}
	
	private void shareImage() {
		Intent share = new Intent(Intent.ACTION_SEND);
		share.setType("image/*");
		String imagePath = drawView.getImagePath();
		File imageFileToShare = new File(imagePath);
		Uri uri = Uri.fromFile(imageFileToShare);
		share.putExtra(Intent.EXTRA_STREAM, uri);
		startActivity(Intent.createChooser(share, "Share image"));
	}
	
	public void pickColor() {
		final Dialog colorDialog = new Dialog(this);
		colorDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.ballon, (ViewGroup)findViewById(R.id.layColor));
		colorDialog.setContentView(layout);
		
		ImageButton red = (ImageButton) layout.findViewById(R.id.buttonRed);
		ImageButton blue = (ImageButton) layout.findViewById(R.id.buttonBlue);
		ImageButton yellow = (ImageButton) layout.findViewById(R.id.buttonYellow);
		ImageButton white = (ImageButton) layout.findViewById(R.id.buttonWhite);
		ImageButton black = (ImageButton) layout.findViewById(R.id.buttonBlack);
		ImageButton purple = (ImageButton) layout.findViewById(R.id.buttonPurple); 
		ImageButton green = (ImageButton) layout.findViewById(R.id.buttonGreen);
		ImageButton cyan = (ImageButton) layout.findViewById(R.id.buttonCyan);
		
		red.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				drawView.setColor(Color.RED);
				colorDialog.dismiss();
			}
		});
		
		blue.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				drawView.setColor(Color.BLUE);
				colorDialog.dismiss();
			}
		});
		
		
		yellow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				drawView.setColor(Color.YELLOW);
				colorDialog.dismiss();
			}
		});
		
		white.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				drawView.setColor(Color.WHITE);
				colorDialog.dismiss();
			}
		});
		
		black.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				drawView.setColor(Color.BLACK);
				colorDialog.dismiss();
			}
		});
		
		
		purple.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				drawView.setColor(Color.MAGENTA);
				colorDialog.dismiss();
			}
		});
		
		green.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				drawView.setColor(Color.GREEN);
				colorDialog.dismiss();
			}
		});
		
		cyan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				drawView.setColor(Color.CYAN);
				colorDialog.dismiss();
			}
		});
		
		colorDialog.show();
	}
	
	public void setThickness() {
		final Dialog thiknessDialog = new Dialog(this);
		thiknessDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.thickness, (ViewGroup)findViewById(R.id.layThickness));
		thiknessDialog.setContentView(layout);
		thiknessDialog.getWindow().setLayout(500, 250);
		
		Button dialogButtonOK = (Button)layout.findViewById(R.id.buttonSaveThickness);
		Button dialogButtonCancel = (Button)layout.findViewById(R.id.buttonCancelThickness);
		
		SeekBar dialogSeekBarThickness = (SeekBar)layout.findViewById(R.id.seekbarThickness);
		dialogSeekBarThickness.setMax(50);
		dialogSeekBarThickness.setProgress(thikness);
		
		SeekBar dialogSeekBarDispersibility = (SeekBar)layout.findViewById(R.id.seekbarDispersibility);
		dialogSeekBarDispersibility.setMax(5);
		dialogSeekBarDispersibility.setProgress((int)dispersibility);
		
		dialogButtonOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (dispersibility == 0) dispersibility = 1;
				drawView.setDrawStepAndRadius(dispersibility, thikness);
				thiknessDialog.dismiss();
				}
			});
		
		dialogButtonCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
					thiknessDialog.dismiss();
				}
			});
		
		
		 dialogSeekBarThickness.setOnSeekBarChangeListener( new OnSeekBarChangeListener() {
			    @Override
			    public void onStopTrackingTouch(SeekBar seekBar) {}
			    @Override
			    public void onStartTrackingTouch(SeekBar seekBar) {}
			    @Override
			    public void onProgressChanged(SeekBar seekBark, int progress, boolean fromUser) {
			           thikness = progress;
			    }
			 });
		 
		 dialogSeekBarDispersibility.setOnSeekBarChangeListener( new OnSeekBarChangeListener() {
			    @Override
			    public void onStopTrackingTouch(SeekBar seekBar) {}
			    @Override
			    public void onStartTrackingTouch(SeekBar seekBar) {}
			    @Override
			    public void onProgressChanged(SeekBar seekBark, int progress, boolean fromUser) {
			            dispersibility = progress;
			    }
			 });
		 thiknessDialog.show();
	}
	
   private void saveInfo() {
	    sPref = getPreferences(MODE_PRIVATE);
	    Editor ed = sPref.edit();	 
	    numberOfLevel += 1;
	    ed.putInt(SAVED_NUMBER_LEVEL, numberOfLevel);
	    ed.commit();
    }
	  
   private void loadInfo() { 
	    sPref = getPreferences(MODE_PRIVATE);
	    numberOfLevel = sPref.getInt(SAVED_NUMBER_LEVEL, 0);  
	 }
   
   private void copyImagesFromAssetsToSD() {
	   AssetManager assetManager = getAssets();
	   String sdPath = Environment.getExternalStorageDirectory().toString() + "/graffiti/";
	   File folder = new File(sdPath);
	   folder.mkdirs();
	   boolean wasError = false;
	   for(int i = 1; i<= 11; i++) {
		   InputStream in = null;
		   OutputStream out = null;
		   try{
			   String filename = "wall" + Integer.toString(i) + ".jpg";
			   in = assetManager.open("images/" + filename);
			   out = new FileOutputStream(sdPath + filename);
			   copyFile(in, out);
			   in.close();
			   in = null;
			   out.flush();
			   out.close();
			   out = null;
		   }catch(Exception e) {
			   wasError = true;
			   e.printStackTrace();
		   }
	   }
	   if(!wasError) saveInfo();
   }
   
   private void copyFile(InputStream in, OutputStream out) throws IOException {
	   byte[] buffer = new byte[1024];
	   int read;
	   while((read = in.read(buffer)) != -1) {
		   out.write(buffer, 0, read);
	   }
   }
   
   
   private void openPatterns() {
	    Intent intent = new Intent(this, GalleryPatternsActivity.class);
	    startActivityForResult(intent, 2);
   }
   
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.buttonShare: {
			shareImage();
			break;
		}
		case R.id.buttonGallery: {
			openGalleryOrWalls();
			break;
		}
		case R.id.buttonPickerColor: {
			pickColor();
			break;
		}
		case R.id.buttonThickness: {
			 setThickness(); 
			 break;
			}
	
		case R.id.buttonSelectPattern: {
			openPatterns();
			break;
		}
		}
		
		
	}
}
