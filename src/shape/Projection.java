package shape;
/**
 * 
 * Projection   
 * �ж�����ͶӰ�Ƿ��غ� 
 * saiyan       
 * 2018��7��10�� ����9:24:08
 */
public class Projection {
	public float min;
	public float max;
	public Projection(float min, float max) {
		this.min = min;
		this.max = max;
	}
	/*
	 * �ж��Ƿ��ཻ
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
