package classes;

import android.graphics.Bitmap;

public class GridItem {
	private Bitmap image;
	private String path;
	
	
	public GridItem(Bitmap item, String title) {
		this.image = item;
		this.path = title;
	}
	
	public Bitmap getImage() {
		return image;
	}
	
	public String getPath() {
		return path;
	}

	
}
