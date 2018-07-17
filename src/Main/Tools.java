package Main;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;

import shape.MyPolygon;

public class Tools extends MyPolygon{
	public String path;
	public int picNum;
	public int step = 1;
	private int time = 0;
	public int w,h;
	public String name;
	
	public ImageObserver t;
	public boolean live = true;
	/*
	 * 构造方法
	 */
	public Tools (float x, float y, String path,String name,int picNum,int w,int h,ImageObserver t) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.live = true;
		this.name = name;
		this.path = path;
		this.t = t;
		this.picNum = picNum;	
	}
	
	public void draw(Graphics g) {
		Graphics2D g2 =(Graphics2D)g;

		if(this.step > picNum ) {
			this.step = 1;
			return;
		}
		Toolkit kit = Toolkit.getDefaultToolkit();
		Image img = kit.getImage("img/"+path+"/"+step+".png");
		int iw = img.getWidth(t);
		int ih = img.getHeight(t);
		g2.drawImage(img,(int)(x-iw/2),(int)(y-ih/2),t);	
		
		//time每7次换一次画面
		this.time++;
		if(this.time>1) {
			this.step++;
			this.time =0;
		}
	}
}
