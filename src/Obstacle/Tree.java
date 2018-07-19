package Obstacle;

import java.awt.Graphics;
import java.awt.Graphics2D;

import Main.TankClient;
import shape.ImageCircleShape;

public class Tree extends ImageCircleShape{
	public boolean live = true;
	public Tree(float x, float y, String imageSource, TankClient tc) {
		super(x, y, imageSource, tc);
		this.radius = 10;
	}
	
	public void draw(Graphics g) {
		if(!live) {
			tc.trees.remove(this);
			return;
		}
		Graphics2D g2 =(Graphics2D)g;
		g2.drawImage(img,(int)(x-30),(int)(y-30),tc);	
	}
	
}
