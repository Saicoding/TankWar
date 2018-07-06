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
	
	private boolean live = true;//一new出来肯定是活着的
	
	int x, y;
	
	Tank.Direction dir;
	
	//随机产生颜色
	int r = (int)(Math.random()*175);
	int g = (int)(Math.random()*175);
	int b = (int)(Math.random()*175);
	Color color = new Color(r,g,b);//子弹颜色
	
	private TankClient tc;//创建tc引用
	
	public Missile(int x, int y, Tank.Direction dir,int speed) {
		this.x = x;
		this.y = y;
		this.speedX =speed;
		this.speedY =speed;
		this.dir = dir;
	}
	
	public  Missile(int x ,int y, Tank.Direction dir ,int speed,TankClient tc) {
		this(x,y,dir,speed);
		this.tc = tc;
	}
	
	public void draw(Graphics g) {
		Color c = g.getColor();
		g.setColor(this.color);
		g.fillOval(x-this.width/2, y-this.height/2, this.width, this.height);
		g.setColor(c);
		
		move();
	}
	
	//得到子弹是否活着的方法
	public boolean isLive() {
		return live;
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
				x -=(int)(speedX/Math.sqrt(2));
				y +=(int)(speedY/Math.sqrt(2));
				break;
			case DR :
				y +=(int)(speedY/Math.sqrt(2));
				x +=(int)(speedX/Math.sqrt(2));
				break;
			case RU :
				x +=(int)(speedX/Math.sqrt(2));
				y -=(int)(speedY/Math.sqrt(2));
				break;
			case UL :
				y -=(int)(speedY/Math.sqrt(2));
				x -=(int)(speedX/Math.sqrt(2));
				break;
			case STOP:
				x -= 0;
				y -= 0;
				break;
		}
		//判断边界条件(粗略的,后期再优化)
		if(x < 0 || y < 0 || x > TankClient.GAME_WIDTH || y > TankClient.GAME_HEIGHT) {
			live = false;
			tc.missiles.remove(this);//这里有tc引用会非常方便
		}
	}
	
}
