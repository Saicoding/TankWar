import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class Tank {
	private int x , y,speed;
	private static final int XSPEED = 5;//x方向速度
	private static final int YSPEED = 5;//y方向速度	
	private boolean bL = false,bR = false,bU = false,bD = false;//定义初始四个方向键按下状态

	public Tank(int x, int y, int speed) {
		this.x = x;
		this.y = y;
		this.speed = speed;
	}
	
	//坦克形状
	public void draw(Graphics g) {
		Color c = g.getColor();//得到前景色
		g.setColor(Color.RED);//设置坦克的颜色是红色
		g.fillOval(x,y,30,30);//画坦克
		g.setColor(c);//恢复前景色
	}
	
	//根据真实按键设置坦克方向状态
	public void setKeyStatus(KeyEvent e,boolean b) {
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_LEFT:
			bL = b;
			break;
		case KeyEvent.VK_RIGHT:
			bR = b;
			break;
		case KeyEvent.VK_UP:
			bU = b;
			break;
		case KeyEvent.VK_DOWN:
			bD = b;
			break;
		}
	}
	
	//按键按下状态
	public void keyPressed(KeyEvent e) {
		setKeyStatus(e, true);
	}
	
	//按键释放状态
	public void keyRelease(KeyEvent e) {
		setKeyStatus(e, false);
	}		
	
	//坦克移动方法
	public void move() {
		if(bL) 
			x -=Tank.XSPEED;
		if(bR) 
			x +=Tank.XSPEED;
		if(bU) 
			y -=Tank.YSPEED;
		if(bD) 
			y +=Tank.YSPEED;	
	}
}
