package Main;

import java.awt.image.ImageObserver;

import shape.MyPoint;

public class Blood extends Tools{

	public Blood(float x, float y, String path,String name, int picNum,int w,int h,ImageObserver t) {
		super(x, y, path,name, picNum, w, h, t);	
		initPoints();
	}
	public void initPoints() {
		points.add(new MyPoint(x-w/2,y-h/2));
		points.add(new MyPoint(x+w/2,y-h/2));
		points.add(new MyPoint(x+w/2,y+h/2));
		points.add(new MyPoint(x-w/2,y+h/2));
	}
}
