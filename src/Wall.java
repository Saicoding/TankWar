import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Wall extends MyPolygon{
	private int x,y,w,h;
	private int cx;//ǽ������x
	private int cy;//ǽ������y
	TankClient tc;
	public Wall(TankClient tc) {
		this.tc = tc;
		points.add(new MyPoint(300,200));
		points.add(new MyPoint(320,200));
		points.add(new MyPoint(320,500));
		points.add(new MyPoint(300,500));
	}
	
	public int getW() {
		return w;
	}

	public int getH() {
		return h;
	}

	public int getCx() {
		return cx;
	}

	public int getCy() {
		return cy;
	}

	public void draw(Graphics g) {//����һ��:���Խ���һ������,�и�draw����,��ʲô��drawʲô
		Graphics2D g2 = (Graphics2D)g;
		Color c = g2.getColor();
		g2.setColor(new Color(133,85,203));
		g2.fill(createPath());
		g2.setColor(c);
	}

	public Rectangle getRect() {
		return new Rectangle(x,y,w,h);
	}
	/*
	 * ��ӵ�
	 */
	public void addPoints(ArrayList<MyPoint> ps) {
		points.addAll(ps);
	}
}
