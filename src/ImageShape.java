import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;

public class ImageShape extends MyPolygon{
	int x,y,iw,ih;
	Image img ;
	String imageSource;
	ArrayList<MyPoint> points = new ArrayList<MyPoint>();
	TankClient tc;
	public ImageShape(int x, int y, String imageSource,TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
		this.imageSource = imageSource;
		this.img = getImage();
		this.iw = img.getWidth(tc);
		this.ih = img.getHeight(tc);		
		this.setPolygonPoints();
	}
	
	public Image getImage() {
		Toolkit kit = Toolkit.getDefaultToolkit();
		Image img = kit.getImage(imageSource+".png");
		return img;
	}
	
	public void setPolygonPoints() {
		this.points.add(new MyPoint(x,y));
		this.points.add(new MyPoint(x+iw,y));
		this.points.add(new MyPoint(x+iw,y+ih));
		this.points.add(new MyPoint(x,y+ih));
	}
	
	public void draw(Graphics g) {
		Graphics2D g2 =(Graphics2D)g;
		g2.drawImage(img,x,y,tc);	
	}
}
