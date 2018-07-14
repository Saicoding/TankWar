
public class MyPoint {
	float x,y;

	public MyPoint(float x, float y) {
		this.x = x;
		this.y = y;
	}
	public MyPoint() {
		this.x = 0;
		this.y = 0;
	}
	
	public MyPoint rotate(MyPoint rotationPoint,float angle) {
	      float tx, ty, rx, ry;
	   
	      tx = this.x - rotationPoint.x; // tx = translated X
	      ty = this.y - rotationPoint.y; // ty = translated Y

	      rx = (float)(tx * Math.cos(-angle) - // rx = rotated X
	           ty * Math.sin(-angle));

	      ry = (float)(tx * Math.sin(-angle) + // ry = rotated Y
	           ty * Math.cos(-angle));

	      return new MyPoint(rx + rotationPoint.x, ry + rotationPoint.y); 
	   }
}
