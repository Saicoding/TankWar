package shape;
import java.util.ArrayList;

public class MyShape {
	public int x;
	public int y;
	public Vector speedV = new Vector();
	public int BIG_NUMBER = 10000;
	public int radius;
	public int speed;
	
	/*
	 * 检测碰撞
	 */
	public boolean collidesWith(MyShape shape,Vector displacement) {
		System.out.println("这是父类空方法collidesWith");
		return false;
	}
	/*
	 * 得到2个碰撞对象中是否有重合的地方
	 */
	public boolean separationOnAxes(ArrayList<Vector> axes,MyShape shape ) {
		for(int i=0;i<axes.size();++i) {
			Vector axis = axes.get(i);
			Projection projection1 = shape.project(axis);
			Projection projection2 = this.project(axis);
			if(!projection1.overlaps(projection2)) {
				return true;
			}
		}
		return false;
	}
	public MyPoint centroid() {
		System.out.println("MyShape没有此方法");
		return null;
	}
	

	public Projection project(Vector axis) {
		System.out.println("这是父类空方法project");
		return null;
	}
	
	public void move() {
		System.out.println("这是父类空方法move");
	}
	
	public MininumTranslationVector getMTV(MyShape shape1,MyShape shape2, Vector displacement,ArrayList<Vector>axes) {
		   float minimumOverlap = BIG_NUMBER;
		   float overlap;
		   Vector axisWithSmallestOverlap = new Vector();
		   MininumTranslationVector  mtv;

		   for (int i=0; i < axes.size(); ++i) {
		      Vector axis = axes.get(i);
		      Projection projection1 = shape1.project(axis);
		      Projection projection2 = shape2.project(axis);
		      overlap = projection1.getOverlap(projection2);

		      if (overlap == 0) {
		         return new MininumTranslationVector(null, 0);
		      }
		      else {
		         if (overlap < minimumOverlap) {
		            minimumOverlap = overlap;
		            if(displacement.x > 0) axis.x = -axis.x;
		            if(displacement.y > 0) axis.y = -axis.y;
		            axisWithSmallestOverlap = axis;    
		         }
		      }
		   }
		   mtv = new MininumTranslationVector(axisWithSmallestOverlap,minimumOverlap);
		   return mtv;
	}
	
	public MininumTranslationVector minimumTranslationVector(ArrayList<Vector> axes,MyShape shape,Vector displacement){
		return getMTV(this,shape,displacement,axes);
	}
	
}

