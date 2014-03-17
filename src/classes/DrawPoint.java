package classes;

public class DrawPoint {
	private float x;
	private float y;
	
	public DrawPoint() {
		this.setX(0);
		this.setY(0);
	}
	
	public DrawPoint(float x, float y) {
		this.setX(x);
		this.setY(y);
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
}
