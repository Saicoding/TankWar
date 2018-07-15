package mytest;

import java.awt.event.KeyEvent;

import shape.MininumTranslationVector;
import shape.MyPoint;
import shape.MyPolygon;
import shape.Vector;

public class TestPolygon extends MyPolygon{
	int w = 100;
	int h = 50;
	public MyTest mt ;
	public Vector lastPosition = new Vector();
	public MininumTranslationVector mtv1 = new MininumTranslationVector(null, 0);
	public MininumTranslationVector mtv2 = new MininumTranslationVector(null, 0);
	public TestPolygon(int x, int y ,int speed,MyTest mt) {
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.speedV.x = speed;
		this.speedV.y = speed;
		this.mt = mt;
		setPoints(x,y);
	}
	
	public void setPoints(int x,int y) {
		this.points.add(new MyPoint(x-100,y-50));
		this.points.add(new MyPoint(x+100,y-50));
		this.points.add(new MyPoint(x+100,y+50));
		this.points.add(new MyPoint(x-100,y+50));
	}
	

	
	public void keyPressed(KeyEvent e) {
		lastPosition.x = x;
		lastPosition.y = y;
		
		switch(e.getKeyCode()) {
			case KeyEvent.VK_A:
				this.x -=speed;
				changePoints(-speed,0);
				break;
			case KeyEvent.VK_D:
				this.x +=speed;
				changePoints(speed,0);
				break;
			case KeyEvent.VK_W:
				this.y -=speed;
				changePoints(0,-speed);
				break;
			case KeyEvent.VK_S:
				this.y +=speed;
				changePoints(0,speed);
				break;
		}
	}
	
	public void collidesWithPolygon() {
		Vector positon = new Vector(new MyPoint(x,y));
		Vector displacement = positon.subTract(lastPosition);
		mtv1 = collidesWith(mt.testPolygon1, displacement);
	}
	
	public void collidesWithCircle() {
		Vector positon = new Vector(new MyPoint(x,y));
		Vector displacement = positon.subTract(lastPosition);
		mtv2 = collidesWith(mt.testCircle1, displacement);
	}
}
