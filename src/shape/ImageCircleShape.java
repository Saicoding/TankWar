package shape;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

import Main.TankClient;

public class ImageCircleShape extends MyCircle{
	public Image img ;
	public String imageSource;
	public TankClient tc;
	
	public ImageCircleShape(float x, float y, String imageSource,TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
		this.imageSource = imageSource;
		this.img = getImage();
	}
	
	public Image getImage() {
		Toolkit kit = Toolkit.getDefaultToolkit();
		Image img = kit.getImage(imageSource+".png");
		return img;
	}
	
	public void draw(Graphics g) {
		Graphics2D g2 =(Graphics2D)g;
		g2.drawImage(img,(int)(x-radius),(int)(y-radius),tc);	
	}
}
