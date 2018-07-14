import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.Collections;

public class MyCircle extends MyShape{
	public int x =80,y,radius;
	

	public MyCircle() {
		
	}

	public Projection project(Vector axis) {
		
		ArrayList<Float> scalars = new ArrayList<Float>();
		Missile m = (Missile)this;
		MyPoint point = new MyPoint(m.x,m.y);
		Float dotProduct = new Vector(point).dotProduct(axis);
		scalars.add(dotProduct);
		scalars.add(dotProduct+m.radius);
		scalars.add(dotProduct-m.radius);
		return new Projection(Collections.min(scalars),Collections.max(scalars));
	}
	
	/*
	 * 与多边形碰撞
	 */
	public MininumTranslationVector collidesWith(MyPolygon polygon, Vector displacement) {
		Missile m = (Missile)this;
		System.out.println(m.x);
		return polygonCollidesWithCircle(polygon,m, displacement);
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
		Missile m = (Missile)circle;
		MyPoint closestPoint = getPolygonPointClosestToCircle(polygon, m);
		System.out.println(m.x);
		axes.add(getCircleAxis(m,polygon,closestPoint));
		return polygon.minimumTranslationVector(axes,m, displacement);
	}
	
	public MininumTranslationVector minimumTranslationVector(ArrayList<Vector> axes,MyCircle circle,Vector displacement){
		return getMTV(this,circle,displacement,axes);
	}
	
	public MininumTranslationVector getMTV(MyCircle circle,MyShape shape2, Vector displacement,ArrayList<Vector>axes) {
		   float minimumOverlap = BIG_NUMBER;
		   float overlap;
		   Vector axisWithSmallestOverlap = new Vector();
		   MininumTranslationVector  mtv;

		   for (int i=0; i < axes.size(); ++i) {
		      Vector axis = axes.get(i);
		      Projection projection1 = circle.project(axis);
		      Projection projection2 = shape2.project(axis);
		      overlap = projection1.getOverlap(projection2);

		      if (overlap == 0) {
		         return new MininumTranslationVector(null, 0);
		      }
		      else {
		         if (overlap < minimumOverlap) {
		            minimumOverlap = overlap;
		            axisWithSmallestOverlap = axis;    
		         }
		      }
		   }
		   mtv = new MininumTranslationVector(axisWithSmallestOverlap,minimumOverlap);
		   return mtv;
	}

	
	/*
	 * 得到最近的点和圆心的单位向量
	 */
	public Vector getCircleAxis(MyCircle circle,MyPolygon polygon,MyPoint closestPoint) {
		Missile m = (Missile)circle;
		System.out.println(m.x);
		Vector v1 = new Vector(new MyPoint(m.x,m.y));
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
		
		Missile m = (Missile)circle;
		System.out.println(m.x);
		for(int i = 0;i < polygon.points.size();i++) {
			testPoint = polygon.points.get(i);
			length = (int) (Math.sqrt(Math.pow(testPoint.x - m.x, 2)+Math.pow(testPoint.y - m.y, 2)));
		
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
		System.out.println("还没实现此方法");
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
