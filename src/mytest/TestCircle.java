package mytest;

import java.awt.event.KeyEvent;

import shape.MininumTranslationVector;
import shape.MyCircle;
import shape.MyPoint;
import shape.Vector;


public class TestCircle extends MyCircle {
	public MyTest mt ;
	public Vector lastPosition = new Vector();
	public MininumTranslationVector mtv1 = new MininumTranslationVector(null, 0);
	public MininumTranslationVector mtv2 = new MininumTranslationVector(null, 0);
	public TestCircle(int x ,int y ,int radius,int speed,MyTest mt) {
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.speed = speed;
		this.speedV.x = speed;
		this.speedV.y = speed;
		this.mt = mt;
	}
	
	public void keyPressed(KeyEvent e) {
		lastPosition.x = x;
		lastPosition.y = y;
		
		switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				this.x -=speed;
				break;
			case KeyEvent.VK_RIGHT:
				this.x +=speed;
				break;
			case KeyEvent.VK_UP:
				this.y -=speed;
				break;
			case KeyEvent.VK_DOWN:
				this.y +=speed;
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
