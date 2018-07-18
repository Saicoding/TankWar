package shape;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.Collections;


public class MyCircle extends MyShape{

	public Projection project(Vector axis) {
		
		ArrayList<Float> scalars = new ArrayList<Float>();
		MyPoint point = new MyPoint(this.x,this.y);
		Float dotProduct = new Vector(point).dotProduct(axis);
		scalars.add(dotProduct);
		scalars.add(dotProduct+this.radius);
		scalars.add(dotProduct-this.radius);
		return new Projection(Collections.min(scalars),Collections.max(scalars));
	}
	
	/*
	 * 与多边形碰撞
	 */
	public MininumTranslationVector collidesWith(MyPolygon polygon, Vector displacement) {
		return polygonCollidesWithCircle(polygon,this, displacement);
	}
	/*
	 * 与圆形碰撞
	 */
	
	public MininumTranslationVector collidesWith(MyCircle circle, Vector displacement) {
		return circleCollidesWithCircle(this, circle);
	}
	/*
	 * 圆形与圆形
	 */
	
	private MininumTranslationVector circleCollidesWithCircle(MyCircle c1, MyCircle c2) {
		int distance = (int)(Math.sqrt( Math.pow(c2.x - c1.x, 2) +Math.pow(c2.y - c1.y, 2)));
		int overlap = Math.abs(c1.radius + c2.radius) - distance;

		return overlap < 0 ?new MininumTranslationVector(null, 0) :new MininumTranslationVector(null, overlap);
	}

	/*
	 * 多边形与圆形
	 */
	public MininumTranslationVector polygonCollidesWithCircle(MyPolygon polygon,MyCircle circle,Vector displacement) {
		ArrayList<Vector> axes = polygon.getAxes();
		MyPoint closestPoint = getPolygonPointClosestToCircle(polygon, circle);

		axes.add(getCircleAxis(circle,polygon,closestPoint));
		return polygon.minimumTranslationVector(axes,circle, displacement);
	}
	
	/*
	 * 得到最近的点和圆心的单位向量
	 */
	public Vector getCircleAxis(MyCircle circle,MyPolygon polygon,MyPoint closestPoint) {
		Vector v1 = new Vector(new MyPoint(circle.x,circle.y));
		Vector v2 = new Vector(new MyPoint(closestPoint.x,closestPoint.y));
		Vector surfaceVector = v1.subTract(v2);
		return surfaceVector.normalize();
	}
	
	/*
	 * 得到离圆最近的点
	 */
	public MyPoint getPolygonPointClosestToCircle(MyPolygon polygon, MyCircle circle) {
		int min = BIG_NUMBER;
		int length = BIG_NUMBER;;
		MyPoint testPoint = new MyPoint();
		MyPoint closestPoint = new MyPoint();

		for(int i = 0;i < polygon.points.size();i++) {
			testPoint = polygon.points.get(i);
			length = (int) (Math.sqrt(Math.pow(testPoint.x - circle.x, 2)+Math.pow(testPoint.y - circle.y, 2)));
		
			if(length < min) {
				min = length;
				closestPoint = testPoint;
			}
		}
		
		return closestPoint;
	}

	/*
	 * 得到圆心
	 */
	public MyPoint centroid() {
		return new MyPoint(x,y);
	}
	
	/*
	 * 移动
	 */
	public void move() {
		x += speedV.x;
		y += speedV.y;
	}
	/*
	 * 圆的方框
	 */
	public BoundingBox boundingBox() {
		return new BoundingBox(x-this.radius,y-this.radius,2*radius,2*radius);
	}
	
	/*
	 * 路径
	 */
	public Shape createPath() {
		GeneralPath gp=new GeneralPath(); 
		Ellipse2D ellipse = new Ellipse2D.Double(x-radius,y-radius,2*radius,2*radius);
		gp.append(ellipse, true);
	    gp.closePath();  //关闭形状创建
	    return gp;
	}
	
	/*
	 * 判断由哪条向量来反射子弹的速度向量
	 */
	
	public void  checkMTVAxisDirection(MininumTranslationVector mtv, MyShape shape) {
		   Vector  centroid1 = new Vector(this.centroid());
		   Vector  centroid2 = new Vector(shape.centroid());
		   Vector  centroidVector = centroid2.subTract(centroid1);
		   Vector  centroidUnitVector = (new Vector(new MyPoint(centroidVector.x,centroidVector.y))).normalize();
		   if (mtv.axis == null)
		      return;
		   
		   if (centroidUnitVector.dotProduct(mtv.axis) > 0) {
		      mtv.axis.x = -mtv.axis.x;
		      mtv.axis.y = -mtv.axis.y;
		   }
	}
}
