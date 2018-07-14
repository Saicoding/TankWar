import java.util.ArrayList;

public class MyShape {
	public int x = 70;
	public int y = 0;
	public Vector speedV = new Vector();
	public int BIG_NUMBER = 10000;
	public int radius;
	
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
	
}

