import java.awt.Color;
import java.awt.Graphics;

/**
 * 
 * @ClassName:     Missile   
 * @Description:   子弹类
 * @author:        saiyan
 * @date:          2018年7月5日 下午10:09:27
 */
public class Missile {
	public int speedX;
	public int speedY;
	
	public int width = 10;
	public int height = 10;
	
	int x, y;
	Tank.Direction dir;
	
	//随机产生颜色
	int r = (int)Math.random()*255;
	int g = (int)Math.random()*255;
	int b = (int)Math.random()*255;
	Color color = new Color(r,g,b);
	
	public Missile(int x, int y, Tank.Direction dir,int speed) {
		this.x = x;
		this.y = y;
		this.speedX =speed;
		this.speedY =speed;
		this.dir = dir;
	}
	
	public void draw(Graphics g) {
		Color c = g.getColor();
		g.setColor(this.color);
		g.fillOval(x, y, this.width, this.height);
		g.setColor(c);
		
		move();
	}
	
	public void move() {
		switch(dir) {
			case L :
				x -=speedX;
				break;
			case R :
				x +=speedX;
				break;
			case U :
				y -=speedY;
				break;
			case D :
				y +=speedY;
				break;
			case LD :
				x -=speedX;
				y +=speedY;
				break;
			case DR :
				y +=speedY;
				x +=speedX;
				break;
			case RU :
				x +=speedX;
				y -=speedY;
				break;
			case UL :
				y -=speedY;
				x -=speedX;
				break;
			case STOP:
				x -= 0;
				y -= 0;
				break;
		}	
	}
	
}
