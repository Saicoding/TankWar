package Obstacle;

import java.awt.Graphics;
import java.awt.Graphics2D;

import Main.TankClient;
import shape.ImagePolygonShape;
import shape.MyPoint;

public class Wall extends ImagePolygonShape{
	public boolean live = true;
	public Wall(float x, float y, String imageSource, TankClient tc) {
		super(x, y, imageSource, tc);
		this.iw = 60;
		this.ih = 60;
		points.add(new MyPoint(x-30,y-30));
		points.add(new MyPoint(x+30,y-30));
		points.add(new MyPoint(x+30,y+30));
		points.add(new MyPoint(x-30,y+30));
	}
	
	public void draw(Graphics g) {
		if(!live) {
			tc.walls.remove(this);
			return;
		}
		Graphics2D g2 =(Graphics2D)g;
		g2.drawImage(img,(int)(x-iw/2),(int)(y-ih/2),tc);	
	}

}
