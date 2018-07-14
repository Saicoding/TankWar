
public class Vector {
	public float x ;
	public float y;
	public Vector(MyPoint p) {
		this.x = p.x;
		this.y = p.y;
	}
	public Vector() {
		this.x = 0;
		this.y = 0;
	}
	
	/*
	 * 得到向量长度
	 */
	public float getMagnitude() {
		return (float)(Math.sqrt(Math.pow(x,2)+Math.pow(y,2)));
	}
	/*
	 * 设置向量长度
	 */
	public void setMagnitude(float m) {
		Vector uv = normalize();
		x = uv.x * m;
		y = uv.y * m;
	}
	/*
	 * 相加
	 */
	public Vector add(Vector v) {
		Vector newV = new Vector();
		newV.x = x + v.x;
		newV.y = y + v.y;
		return newV;
	}
	/*
	 * 相减
	 */
	public Vector subTract(Vector v) {
		Vector newV = new Vector();
		newV.x = x-v.x;
		newV.y = y-v.y;
		return newV;
	}
	/*
	 * 点积
	 */
	public float dotProduct(Vector v) {
		return x * v.x + y * v.y;
	}	
	
	/*
	 * 边缘向量
	 */
	public Vector edge(Vector v) {
		return subTract(v);
	}
	
	/*
	 * 垂直向量
	 */
	public Vector perpendicular() {
		Vector v = new Vector();
		v.x = y;
		v.y = 0 - x;
		return v;
	}
	/*
	 * 单位向量
	 */
	public Vector normalize() {
		Vector v = new Vector();
		float m = getMagnitude();
		v.x = x / m;
		v.y = y / m;
		return v;	
	}

	/*
	 * 垂直单位向量
	 */
	public Vector normal() {
		Vector p = perpendicular();
		return p.normalize();	
	}
	/*
	 * 反射
	 */
	public Vector reflect(Vector axis) {
	     float dotProductRatio;
	     
	     
	     Vector v = new Vector();
	     float vdotl = this.dotProduct(axis);
	     float ldotl = axis.dotProduct(axis);
	     dotProductRatio = vdotl / ldotl;

	     v.x = 2 * dotProductRatio * axis.x - this.x;
	     v.y = 2 * dotProductRatio * axis.y - this.y;
	     
	     return v;
	}
	
}
