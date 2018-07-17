package shape;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;

import Main.TankClient;

public class ImageShape extends MyPolygon{
	public float iw,ih;
	public Image img ;
	public String imageSource;
	public TankClient tc;
	public ImageShape(float x, float y, String imageSource,TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
		this.imageSource = imageSource;
		this.img = getImage();
//		this.iw = img.getWidth(tc);//解决不了图片第一次载入得不到iw的问题
//		this.ih = img.getHeight(tc);
	}
	
	public Image getImage() {
		Toolkit kit = Toolkit.getDefaultToolkit();
		Image img = kit.getImage(imageSource+".png");
		return img;
	}
	
	public void draw(Graphics g) {
		Graphics2D g2 =(Graphics2D)g;
		g2.drawImage(img,(int)(x-iw/2),(int)(y-ih/2),tc);	
	}
}
