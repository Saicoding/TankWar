package shape;
/**
 * 
 * Projection   
 * 判断两个投影是否重合 
 * saiyan       
 * 2018年7月10日 下午9:24:08
 */
public class Projection {
	public float min;
	public float max;
	public Projection(float min, float max) {
		this.min = min;
		this.max = max;
	}
	/*
	 * 判断是否相交
	 */
	public boolean overlaps(Projection p) {
		return max>p.min && p.max>min;
	}
	
	public float getOverlap(Projection p) {
		float overlap;
		if(!this.overlaps(p)) return 0;
		if(this.max > p.max) {
			overlap = p.max -this.min;
		}else {
			overlap = this.max - p.min;
		}
		return overlap;
	}
	
}
