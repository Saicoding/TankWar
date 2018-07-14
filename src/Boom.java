import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

public class Boom {
	int x,y;
	private boolean live = true;
	
	int step = 1;
	int time = 0;
	TankClient tc;
	/*
	 * 构造函数引用
	 */
	public Boom(int x,int y,TankClient tc){
		this(x,y);
		this.tc =tc;
	}
	
	public Boom(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void draw(Graphics g) {
		Graphics2D g2 =(Graphics2D)g;
		if(!live) return ;
		if(this.step >5) {
			this.live = false;
			this.tc.booms.remove(this);
			return;
		}
		Toolkit kit = Toolkit.getDefaultToolkit();
		Image img = kit.getImage("img/boom/"+step+".png");
		int iw = img.getWidth(tc);
		int ih = img.getHeight(tc);
		g2.drawImage(img,x-iw/2,y-ih/2,tc);	
		
		//time每7次换一次画面
		this.time++;
		if(this.time>7) {
			this.step++;
			this.time =0;
		}
	}
}
