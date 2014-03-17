package classes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.everyme.LoveGraffiti.R;

public class Graffiti extends View{
    private final String beginFolder = "images/";
    private final String endFolder = ".jpg";
    private Paint paint;
    private String pathToImage = ""; 
    private boolean firstTime = false, typeDrawOrMovePattern = false, withoutPattern = true;
    private float xLast, yLast, xTr, yTr;
    private Context context;
    private float zoomX, zoomY; 
    private boolean box = false;
    private float drawStep = 1f;
    private float drawRadius = 15f;
    private Bitmap backImage, globalBitmap, traffaret, cross, arrow;
    private SetPoints point = new SetPoints();
	private Canvas mBitmapCanvas = new Canvas();
	private Canvas mTraffaretCanvas = new Canvas();
	private boolean mTouchInArrow;
	
    public Graffiti(Context context) {
        super(context);
        initAll(context);
    }
    
    public void setDrawStepAndRadius(float step, float radius) {
    	this.drawRadius = radius;
    	this.drawStep = step;
    }

    @Override
    public void onDraw(Canvas canvas) {
     	drawBackImage(canvas);
     	if(!firstTime) {
    		    	globalBitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), 
    		    			Bitmap.Config.ARGB_8888);
    	} else {
    		 canvas.drawBitmap(globalBitmap, 0, 0, paint);	
    		 if(!withoutPattern) {
    			canvas.save();
    			canvas.translate(xTr, yTr);
    			canvas.drawBitmap(traffaret, 0, 0, paint);  
    			canvas.restore();
    			drawCross(canvas);
    			drawArrow(canvas);
    		 }	
    		}
    }
    
    public void setWithoutPattern(boolean flag) {
    	withoutPattern = flag;
    	xTr = 100;
    	yTr = 100;
    	invalidate();
    }
    
    public void drawBackImage(Canvas canvas) {
    	canvas.save();
    	canvas.scale(zoomX, zoomY);
    	canvas.drawBitmap(backImage, 0, 0, paint);
    	canvas.restore();
    }
    
    public void drawCross(Canvas canvas) {
    	canvas.drawBitmap(cross, xTr + traffaret.getWidth() - cross.getWidth() / 2, 
    			yTr - cross.getHeight() / 2, paint);
    }
    
    public void drawArrow(Canvas canvas) {
    	canvas.drawBitmap(arrow, xTr - arrow.getWidth() / 2, yTr - arrow.getHeight() / 2, paint);
    }
    
    
    private void initAll(Context context) {
	    this.context = context;
	    paint = new Paint();
	    firstTime = false;
	    paint.setColor(Color.RED);
	    traffaret = BitmapFactory.decodeResource(getResources(),
                R.drawable.traf2);
	    cross = BitmapFactory.decodeResource(getResources(),
                R.drawable.cross);
	    arrow = BitmapFactory.decodeResource(getResources(),
                R.drawable.arrow);
    }
   
    
    public Graffiti(Context context, AttributeSet attrs) {
		super(context, attrs);
		initAll(context);
	}
    
    public void setAllForLevel() {
    	// load background image
    	pathToImage  = beginFolder + "wall1" + endFolder;
    	backImage = loadImageFromAssets(pathToImage);
    }
        
    private Bitmap loadImageFromGallery(String path) {
    	Bitmap bitmap = null;
    	File file = new File(path);
    	try{
    		bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
    	} catch(Exception e){
    		e.printStackTrace();
    	}
    	
    	return bitmap;
    }
    
    public Bitmap loadImageFromAssets(String path) {
    	Bitmap bitmap = null;
    	try{
	        InputStream ims = context.getAssets().open(path);
	        Drawable drawbleTemp = Drawable.createFromStream(ims, null);
	        bitmap = ((BitmapDrawable) drawbleTemp).getBitmap();
    	 }
        catch(IOException ex) {
        	ex.printStackTrace();
        }
        return bitmap; 
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        miscalculationParameters();
    }
    
    private void miscalculationParameters() {
    	zoomX = (float) getWidth() / backImage.getWidth();
    	zoomY = (float) getHeight() / backImage.getHeight();
    	if (zoomX == 0) zoomX = 1;
    	if (zoomY == 0) zoomY = 1;
    }
    
    public boolean inPattern(float x, float y) {
    	if((x < xTr + traffaret.getWidth()) && (x >= xTr) 
    			&& (y < yTr + traffaret.getHeight()) && (y >= yTr)) {
    		return true;
    	}
    	else 
    		return false;	
    }
   
    
    public boolean inCross(float x, float y) {
    	float crossBiginX = xTr + traffaret.getWidth() - cross.getWidth() / 2;
    	float crossBeginY = yTr - cross.getHeight() / 2;
    	float crossEndX = xTr + traffaret.getWidth() + cross.getWidth() / 2;
    	float crossEndY = yTr + cross.getHeight() / 2 ; 
    	
    	if(x < crossEndX &&  x >= crossBiginX && y < crossEndY && y >= crossBeginY) {
    		return true;
    	}
    	else 
    		return false;	
    }
    
    public boolean inArrow(float x, float y) {
    	final float radius = 30;
    	float crossBiginX = xTr - radius;
    	float crossBeginY = yTr - radius;
    	float crossEndX = xTr + radius + arrow.getWidth();
    	float crossEndY = yTr + radius + arrow.getHeight(); 
    	
    	if(x < crossEndX &&  x >= crossBiginX  && y < crossEndY && y >= crossBeginY) {
    		return true;
    	}
    	else 
    		return false;
    }
    
    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }
    
    public Bitmap invert(Bitmap original) {
        Bitmap inversion = original.copy(Config.ARGB_8888, true);

        int width = inversion.getWidth();
        int height = inversion.getHeight();
       
        for(int i = 0; i < width; i++) {
        	for(int j = 0; j < height; j++) {
        		int pixel = original.getPixel(i, j);
        		int alphaValue = Color.alpha(pixel);
        		int redValue = Color.red(pixel);
    			int blueValue = Color.blue(pixel);
    			int greenValue = Color.green(pixel);
        		if(alphaValue > 0) {
        			inversion.setPixel(i, j, Color.argb(0, redValue, greenValue, blueValue));
        		}
        		else {
        			inversion.setPixel(i, j, Color.argb(255, redValue, greenValue, blueValue));
        		}
        	}
        }
        return inversion;
    }
    
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
	    switch (event.getAction()) {    
	         case MotionEvent.ACTION_DOWN: {
	        	     xLast = event.getX();
	        	     yLast = event.getY();
	        	     
	        	     if(inCross(xLast, yLast)) {
           	    		 withoutPattern = true;
           	    		 invalidate();
           	    		 return true;
           	    	 } //*/
	        	     if(!withoutPattern) {
	        	    	 if(inCross(xLast, yLast)) {
			           	    		 withoutPattern = true;
			           	    		 invalidate();
			           	    		 return true;
			           	    	 } 
	        	    	 
	            		 if(inArrow(xLast, yLast)) {
	            			 mTouchInArrow = true;
	            			 xTr = xLast;
	               	 		 yTr = yLast;
	               	 		 box = true;
	               	 		 invalidate();
	               	 		 return true;
	            		 }
	            		 typeDrawOrMovePattern = true;
	        	    	 if(typeDrawOrMovePattern) {
	           	    	 point = new SetPoints(xLast, yLast, drawRadius, drawStep);
	           	    	 
	           	    	 if(inPattern(xLast, yLast)) {
	           	    				point.drawSetPointTwoCanvas(mBitmapCanvas, mTraffaretCanvas, paint, 
	           	    						xTr, yTr, traffaret);	
	           	    				
	           	    	 }
	           	    	 else {
	           	    		 point.drawSetPoints(mBitmapCanvas, paint);
	           	    	 }
	           	     }
	        	   else {
	           	 		 xTr = xLast; 
	           	 		 yTr = yLast;
	           	 	 }
	        	     }
	        	     else {
	        	    	 point = new SetPoints(xLast, yLast, drawRadius, drawStep);
	        	    	 point.drawSetPoints(mBitmapCanvas, paint);
	        	     }
	            	
	            	 firstTime = true;
	        	    
	        	     
	        	     mBitmapCanvas = new Canvas(globalBitmap);
	        	     try{
	        	    	 mTraffaretCanvas = new Canvas(traffaret);
	        	     }
	        	     catch(Exception e) {
	        	    	 e.printStackTrace();
	        	     }
	        	     
	        	     invalidate();
	        	     
	        	     return true;
	         }
	         
	         
	         case MotionEvent.ACTION_MOVE: {
	        	 	
	        	 	if(!withoutPattern) {
	        	 		
	            		 if(mTouchInArrow) {
	            			 xTr += event.getX() - xLast;
	            			 yTr += event.getY() - yLast;
	    		             xLast = event.getX();
	    		             yLast = event.getY();

	               	 		 invalidate();
	               	 		 return true;
	            		 } 
	            		 typeDrawOrMovePattern = true; 
	        	    	 if(typeDrawOrMovePattern) {
	           	    	 point = new SetPoints(xLast, yLast, drawRadius, drawStep);
	           	    	 
	           	    	 if(inPattern(xLast, yLast)) {
	           	    				point.drawSetPointTwoCanvas(mBitmapCanvas, mTraffaretCanvas, paint, 
	           	    						xTr, yTr, traffaret);	
	           	    				
	           	    	 }
	           	    	 else {
	           	    		 point.drawSetPoints(mBitmapCanvas, paint);
	           	    	 }
	           	     }
	        	   else {
	           	 		 xTr = xLast; 
	           	 		 yTr = yLast;
	           	 	 }
	        	     }
	        	     else {
	        	    	 point = new SetPoints(xLast, yLast, drawRadius, drawStep);
	        	    	 point.drawSetPoints(mBitmapCanvas, paint);
	        	     }
	        	     invalidate();
	        	     
		             xLast = event.getX();
		             yLast = event.getY();
		             break;
	             }
	         case MotionEvent.ACTION_UP:
	        	 mTouchInArrow = false;
	        	 break;
	         }
    	return super.onTouchEvent(event);
    }


	public void setColorPaint(int globalColor) {
		paint.setColor(Color.rgb(Color.red(globalColor), Color.green(globalColor), 
				Color.blue(globalColor)));
	}

	public float getDrawStep() {
		return drawStep;
	}

	public void setDrawStep(float drawStep) {
		this.drawStep = drawStep;
	}

	public float getDrawRadius() {
		return drawRadius;
	}

	public void setColor(int mColor) {
		paint.setColor(mColor); 
	}
	
	public void setDrawRadius(float drawRadius) {
		this.drawRadius = drawRadius;
	}

	public void setBackgroundImageGallery(String path) {
		pathToImage = path;
		backImage = loadImageFromGallery(pathToImage);
		firstTime = false;
		miscalculationParameters(); 
		invalidate();
	}
	
	public void setBackgroundImageAssets(String path) {
		pathToImage = path;
		backImage = loadImageFromAssets(pathToImage);
		firstTime = false;
		miscalculationParameters(); 
		invalidate();
	}
	
	public void setNewPattern(String path) {
		traffaret = getResizedBitmap(loadImageFromAssets(path), 
				(int)(getWidth() / 2),
				(int)(getHeight() / 2))
				.copy(Bitmap.Config.ARGB_8888, true);
		firstTime = true;
		withoutPattern = false;
		xTr =100; yTr =100;
		miscalculationParameters(); 
		invalidate();
	}
	
	private Bitmap overlayBitmap(Bitmap canvasBitmap, Bitmap backGroundBitmap) {
		Bitmap scaled = Bitmap.createScaledBitmap(backGroundBitmap, canvasBitmap.getWidth(),
				canvasBitmap.getHeight(), false);
		Bitmap bmpOverlay = Bitmap.createBitmap(canvasBitmap.getWidth(), canvasBitmap.getHeight(), 
				canvasBitmap.getConfig());
	    Canvas canvas = new Canvas(bmpOverlay);
	    canvas.drawBitmap(scaled, new Matrix(), null);
	    canvas.drawBitmap(canvasBitmap, new Matrix(), null);
	    return bmpOverlay;
	}
	
	private void saveDrawableImageToSD(String fullRootPath, String fname) {
		File myDir = new File(fullRootPath);    
		myDir.mkdirs();
		Bitmap overlay = overlayBitmap(globalBitmap, backImage);
		File file = new File (myDir, fname);
		if (file.exists ()) file.delete (); 
		try {
		       FileOutputStream out = new FileOutputStream(file);
		       overlay.compress(Bitmap.CompressFormat.JPEG, 90, out);
		       out.flush();
		       out.close();

		} catch (Exception e) {
		       e.printStackTrace();
		}
	}
	
	public String getImagePath() {
		String fullRootPath = Environment.getExternalStorageDirectory().toString() + "/graffiti/";
		Random generator = new Random();
		int n = 100000;
		n = generator.nextInt(n);
		String fname = "Image-" + n + ".jpg";
		saveDrawableImageToSD(fullRootPath, fname);
		return fullRootPath + fname;
	}

	public boolean isType() {
		return typeDrawOrMovePattern;
	}

}
