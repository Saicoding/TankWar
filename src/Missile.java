import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 * 
 * @ClassName:     Missile   
 * @Description:   子弹类
 * @author:        saiyan
 * @date:          2018年7月5日 下午10:09:27
 */
public class Missile {
	public int speedX;//x方向的速度
	public int speedY;//y方向的速度
	
	public int width = 10;//子弹宽度
	public int height = 10;//子弹高度
	
	private int angle;
	
	private boolean good;//子弹阵营
	
	private boolean live = true;//一new出来肯定是活着的
	
	public void setLive(boolean live) {//设置子弹是否活着
		this.live = live;
	}

	int x, y;//子弹位置(中心)
	
	
	//随机产生颜色
	int r = (int)(Math.random()*175);
	int g = (int)(Math.random()*175);
	int b = (int)(Math.random()*175);
	Color color = new Color(r,g,b);//子弹颜色
	
	private TankClient tc;//创建tc引用
	
	/*
	 * 构造方法
	 */
	public Missile(int x, int y,boolean good, int angle,int speed,int width) {
		this.x = x;
		this.y = y;
		this.good = good;
		this.speedX =speed;
		this.speedY =speed;
		this.width =width;
		this.angle = angle;
	}
	/*
	 * 带引用的构造方法
	 */
	public  Missile(int x ,int y,boolean good,int angle ,int speed,int width,TankClient tc) {
		this(x,y,good,angle,speed,width);
		this.tc = tc;
	}
	
	/*
	 * 画子弹
	 */
	public void draw(Graphics g) {
		//如果子弹死了,移除,并return 不画了
		if(!live) {
			tc.missiles.remove(this);
			return;
		}
		Color c = g.getColor();
		g.setColor(this.color);
		g.fillOval(x-this.width/2, y-this.width/2, this.width, this.width);
		g.setColor(c);
		
		move();
	}
	
	/*
	 * 得到子弹是否活着的方法
	 */
	public boolean isLive() {
		return live;
	}
	/*
	 * 子弹移动方法
	 */
	public void move() {
		x += (int)(Math.cos(angle * 3.14 / 180)*speedX);
		y += (int)(Math.sin(angle * 3.14 / 180)*speedY);
		//判断边界条件(粗略的,后期再优化)
		if(x < 0 || y < 0 || x > TankClient.GAME_WIDTH || y > TankClient.GAME_HEIGHT) {
			live = false;
		}
	}
	
	/*
	 * 得到正好包含子弹的一个矩形对象(用来检测碰撞)
	 */
	public Rectangle getRect() {
		return new Rectangle(x-width/2,y-width/2,width,width);//这里x,y是子弹的中心,所以要这样算矩形
	}
	
	/*
	 * 判断是否打中我的坦克
	 */
	public boolean hitMyTank(Tank t) {
		if(this.live && this.getRect().intersects(t.getRect()) && t.isLive()&& !t.isGood() == this.good) {
			t.setLife(t.getLife()-1);
			if(t.getLife() <=0) {
				t.setLive(false);
			}	
			if(!this.good)this.setLive(false);
			tc.booms.add(new Boom(t.getCx(),t.getCy(),tc));
			return true;
		}
		return false;
	}
	
	/*
	 * 判断是否打中所有坦克
	 */
	public boolean hitTanks(ArrayList<Tank> tanks) {
			for(int j =0;j < tanks.size();j++) {
				if(hitMyTank(tanks.get(j))) {
					return true;
				}
			}	
		return false;
	}

	/*
	 * 判断是否打中墙
	 */
	public boolean hitWall(Wall w) {
		if(this.live && this.getRect().intersects(w.getRect())) {
			this.live =false;
			return true;
		}
		return false;
	}
}
