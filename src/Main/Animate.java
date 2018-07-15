package Main;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;

public class Animate{
	public int x,y,picNum;
	private ImageObserver t;
	private int step = 1;
	private int time = 0;
	String path;
	public boolean over = false;
	
	public Animate(int x,int y,String path,int picNum,ImageObserver t){
		this.x = x;
		this.y = y;
		this.picNum = picNum;
		this.path = path;
		this.t =t;
	}
	
	public void draw(Graphics g) {
		Graphics2D g2 =(Graphics2D)g;
		if(over) return ;
		if(this.step > picNum ) {
			this.over = true;
			return;
		}
		Toolkit kit = Toolkit.getDefaultToolkit();
		Image img = kit.getImage("img/"+path+"/"+step+".png");
		int iw = img.getWidth(t);
		int ih = img.getHeight(t);
		g2.drawImage(img,x-iw/2,y-ih/2,t);	
		
		//time每7次换一次画面
		this.time++;
		if(this.time>7) {
			this.step++;
			this.time =0;
		}
	}
}
