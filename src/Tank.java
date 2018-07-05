import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class Tank {
	int x , y, speed;

	public Tank(int x, int y, int speed) {
		this.x = x;
		this.y = y;
		this.speed = speed;
	}
	
	public void draw(Graphics g) {
		Color c = g.getColor();//得到前景色
		g.setColor(Color.RED);//设置坦克的颜色是红色
		g.fillOval(x,y,30,30);//画坦克
		g.setColor(c);//恢复前景色
	}
	
	public void move(boolean left,boolean right,boolean up,boolean down) {
		if(left) 
			x -=this.speed;
		if(right) 
			x +=this.speed;
		if(up) 
			y -=this.speed;
		if(down) 
			y +=this.speed;
		
	}
}
