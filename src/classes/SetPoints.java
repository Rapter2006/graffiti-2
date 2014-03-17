package classes;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

//Класс рисует один пшик маркером
public class SetPoints {
	private ArrayList<DrawPoint> points;
    private float x;
    private float y;
    private float radius;
    private float step;
    
    
	private boolean inCircle(float radius, float xCircle, float yCircle, float x, float y) {
		if ((y - yCircle) * (y - yCircle) + (x - xCircle) * (x - xCircle) <= radius * radius)
			return true;
		else return false;			
	}
	
	public SetPoints() {
		this.points = new ArrayList<DrawPoint>();
	}
	
	private void initPoints() {
		Random random = new Random();
		points = new ArrayList<DrawPoint>();
		float xMin = x - radius;
		float yMin = y - radius;
		float xMax = x + radius;
		float yMax = y + radius;
		
		for(float i = xMin; i < xMax; i += step) {
			for(float j = yMin; j < yMax; j+= step) {
				int pointInCoords = random.nextInt(10);
				
				if (pointInCoords > 8  && inCircle(radius, x, y, i, j)) {
					points.add(new DrawPoint(i, j));
					
				for(int u = 0; u < random.nextInt(10); u++) {
					float new_y =  random.nextFloat() * ( step * 2) + j;
					float new_x =  random.nextFloat() * ( step * 2) + i;
					points.add(new DrawPoint(new_x, new_y));
				}
				if(random.nextInt(15) > 10) {
						float radiusX = random.nextFloat() * radius; 
						float radiusY = random.nextFloat() * radius; 
						int k = random.nextInt(4);
						switch(k) {
						case 0: {
							points.add(new DrawPoint(i - radiusX, j - radiusY)); break;
						}
						case 1: {
							points.add(new DrawPoint(i - radiusX, j + radiusY)); break;
						}
						case 2: {
							points.add(new DrawPoint(i + radiusX, j + radiusY)); break;
						}
						case 3: {
							points.add(new DrawPoint(i + radiusX, j - radiusY)); break;
						}
					  }
					}
					}
				
			}	
			}
		
	}
	
	//Генерит точки окружности маркером 
	public SetPoints(float x, float y, float radius, float step) {
		this.x = x; this.radius = radius;
		this.y = y; this.step = step;
		initPoints();
	}
	
	
	public void drawSetPoints(Canvas canvas, Paint paint) {
		for(DrawPoint point: points) {
    		canvas.drawPoint(point.getX(), point.getY(), paint);
    	}
	}
	
	 public boolean inPattern(float x, float y, float xTr, float yTr, float w, float h) {
	    	if((x < xTr + w) && (x >= xTr) 
	    			&& (y < yTr + h) && (y >= yTr)) {
	    		return true;
	    	}
	    	else 
	    		return false;	
	    }
	    
	
	 
	public void drawSetPointTwoCanvas(Canvas global, Canvas pattern, Paint paint,
			float xTr, float yTr, Bitmap traffaret) {
		    float w = traffaret.getWidth();
		    float h = traffaret.getHeight();
		     
			for(DrawPoint point: points) {
				float x = point.getX();
				float y = point.getY(); 
				
				if(inPattern(x, y, xTr, yTr, w, h)) {
					int pixel = traffaret.getPixel((int)(x - xTr), (int)(y - yTr));
	        		int alphaValue = Color.alpha(pixel);
	        		
					if( alphaValue < 10 ) {
						global.drawPoint(x, y, paint);
					}
					else{
						pattern.drawPoint(x - xTr, y - yTr, paint);
					}
						
				}
				else	
					global.drawPoint(x, y, paint); 
			}
	}
	
	// Получить все точки
	public ArrayList<DrawPoint> getPoints() {
		return points;
	}

	public void setPoints(ArrayList<DrawPoint> points) {
		this.points = points;
	}
}
