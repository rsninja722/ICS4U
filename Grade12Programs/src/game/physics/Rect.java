package game.physics;

/** rectangle object used for physics and drawing */
public class Rect {
	public double x;
	public double y;
	public int w;
	public int h;
    
	public int halfW;
	public int halfH;

    /**
     * Creates a new rectangle object centered on x,y that can be used in physics and drawing
     * @param x center x
     * @param y center y
     * @param w width
     * @param h height
     */
	public Rect(double x, double y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
        this.h = h;
        
        // calculate half width/height once
		this.halfW = w / 2;
		this.halfH = h / 2;
	}

    /**
     * changes width and height, and recalculates the half width and half hight
     * @param w width
     * @param h height
     */     
	public void resize(int w, int h) {
		this.w = w;
		this.h = h;
		this.halfW = w / 2;
		this.halfH = h / 2;
	}
}
