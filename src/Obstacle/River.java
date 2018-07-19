package Obstacle;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

import Main.TankClient;
import shape.ImagePolygonShape;
import shape.MyPoint;

public class River extends ImagePolygonShape{
	private int step = 1;
	private int time = 0;
	public int picNum ;
	public River(float x, float y, String imageSource,int picNum, TankClient tc) {
		super(x, y, imageSource, tc);
		this.picNum = picNum;
		this.iw = 101;
		this.ih = 68;
		points.add(new MyPoint(x-iw/2,y-ih/2));
		points.add(new MyPoint(x+iw/2,y-ih/2));
		points.add(new MyPoint(x+iw/2,y+ih/2));
		points.add(new MyPoint(x-iw/2,y+ih/2));
	}
	
	public void draw(Graphics g) {
		Graphics2D g2 =(Graphics2D)g;
		if(this.step > picNum ) {
			this.step = 1;
		}
		Toolkit kit = Toolkit.getDefaultToolkit();
		Image img = kit.getImage("img/"+imageSource+"/"+step+".png");
		g2.drawImage(img,(int)(x-iw/2),(int)(y-ih/2),tc);	
		
		//time每7次换一次画面
		this.time++;
		if(this.time>1) {
			this.step++;
			this.time =0;
		}
	}
	
}
