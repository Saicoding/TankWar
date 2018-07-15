package shape;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;

import mytest.TestCircle;


public class MyPolygon extends MyShape{
	public ArrayList<MyPoint> points = new ArrayList<MyPoint>();
	
	/*
	 * 与圆相撞
	 */
	public MininumTranslationVector collidesWith(MyCircle circle,Vector displacement) {	
		return polygonCollidesWithCircle(this,circle,displacement );
	}
	/*
	 * 与多边形相撞
	 */
	public MininumTranslationVector collidesWith(MyPolygon polygon,Vector displacement) {	
		return polygonCollidesWithPolygon(this,polygon,displacement );
	}
	
	/*
	 * 与多边形相撞
	 */
	public MininumTranslationVector polygonCollidesWithPolygon(MyPolygon p1,MyPolygon p2,Vector displacement) {// displacement for p1
		MininumTranslationVector mtv1 = p1.minimumTranslationVector(p1.getAxes(),p2,displacement);
		MininumTranslationVector mtv2 = p1.minimumTranslationVector(p2.getAxes(),p2,displacement);
		if(mtv1.overlap == 0 || mtv2.overlap ==0) {
			return new MininumTranslationVector(null, 0);
		}else {
			return mtv1.overlap<mtv2.overlap?mtv1:mtv2;
		}
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
	 * 得到所有的投影轴
	 */
	public ArrayList<Vector> getAxes(){
		Vector v1 = new Vector();
		Vector v2 = new Vector();
		Vector surfaceVector = new Vector();
		ArrayList<Vector> axes = new ArrayList<Vector>();
		
		for(int i = 0;i< points.size()-1;i++) {
			v1 = new Vector(points.get(i));
			v2 = new Vector(points.get(i+1));
			
			surfaceVector = v2.subTract(v1);
			axes.add(surfaceVector.perpendicular().normalize());		
		}
		v1 = new Vector(points.get(points.size()-1));
		v2 = new Vector(points.get(0));
		
		surfaceVector = v2.subTract(v1);
		axes.add(surfaceVector.perpendicular().normalize());
		return axes;
	}

	@Override
	public Projection project(Vector axis) {
		ArrayList<Float> scalars = new ArrayList<Float>();

		for(int i = 0;i<points.size();i++) {
			MyPoint p = points.get(i);
			scalars.add(new Vector(p).dotProduct(axis));
		}
		return new Projection(Collections.min(scalars),Collections.max(scalars));
	}
	
	public void addPoint(int x,int y) {
		this.points.add(new MyPoint(x,y));
	}
	/*
	 * 创建路径
	 */
	public Shape createPath() {
		if(points.size() == 0) return null;
		GeneralPath gp=new GeneralPath(); 
		if(this.points.size() ==0)return null;
		
		MyPoint p0 = points.get(0);
		MyPoint p1 = points.get(1);
		
	    gp.append(new Line2D.Float(p0.x,p0.y,p1.x,p1.y),true);   //在形状中添加一条线，即Line2D
	    
	    for(int i=1;i<points.size();i++) {
	    	gp.lineTo(points.get(i).x,points.get(i).y); 
	    }
	    gp.lineTo(p0.x,p0.y); 
	    gp.closePath();  //关闭形状创建
	    return gp;
	}
	/*
	 * 找到中心点
	 */
	public MyPoint centroid() {
		MyPoint pointSum = new MyPoint(0,0);
		
		for (int i=0; i < this.points.size(); ++i) {
			 MyPoint point = this.points.get(i);
			 pointSum.x += point.x;
			 pointSum.y += point.y;
		}
		return new MyPoint(pointSum.x/this.points.size(), pointSum.y/this.points.size());	
	}
	
	/*
	 * 包围这个多边形的框
	 */
	public BoundingBox boundingBox() {
		int minx = BIG_NUMBER;
		int miny = BIG_NUMBER;
		int maxx = BIG_NUMBER;
		int maxy = BIG_NUMBER;
		MyPoint point = new MyPoint();
		
		for(int i = 0;i<points.size();i++) {
			point = this.points.get(i);
			minx = (int)(Math.min(minx, point.x));
			miny = (int)(Math.min(miny, point.y));
			maxx = (int)(Math.max(maxx, point.x));
			maxy = (int)(Math.max(maxx, point.y));
		}
		return new BoundingBox(minx, miny, maxx-minx, maxy-miny);		
	}
	/*
	 * 移动点
	 */
	public void changePoints(int x, int y ) {
		for(int i = 0 ; i<points.size();i++) {
			MyPoint p = points.get(i);
			p.x += x;
			p.y += y;
		}
	}
}

