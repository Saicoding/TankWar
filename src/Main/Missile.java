package Main;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

import Thread.SoundThread;
import shape.MininumTranslationVector;
import shape.MyCircle;
import shape.MyPoint;
import shape.MyShape;
import shape.Vector;

/**
 * 
 * @ClassName:     Missile   
 * @Description:   子弹类
 * @author:        saiyan
 * @date:          2018年7月5日 下午10:09:27
 */
public class Missile extends MyCircle{
	
	private float angle;
	private boolean good;//子弹阵营	
	private boolean live = true;//一new出来肯定是活着的
	public Vector lastPosition = new Vector();
	
	
	public void setLive(boolean live) {//设置子弹是否活着
		this.live = live;
	}

	//随机产生颜色
	int r = (int)(Math.random()*175);
	int g = (int)(Math.random()*175);
	int b = (int)(Math.random()*175);
	Color color = new Color(r,g,b);//子弹颜色
	
	public TankClient tc;//创建tc引用

	
	/*
	 * 构造方法
	 */
	public Missile(float x, float y,int radius ,boolean good, float angle,int speed) {
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.good = good;
		this.speed = speed;
		this.angle = angle;
	}
	/*
	 * 带引用的构造方法
	 */
	public  Missile(float x ,float y,int radius ,boolean good,float angle ,int speed,TankClient tc) {
		this(x,y,radius,good,angle,speed);
		this.tc = tc;
		initSpeedV();
	}
	
	public void initSpeedV() {
	    float l = (float) ((angle * Math.PI) / 180);        
	    float cosv = (float) Math.cos(l);  
	    float sinv = (float) Math.sin(l);  
		speedV.x = cosv*speed;
		speedV.y = sinv*speed;
	}
	
	/*
	 * 画子弹
	 */
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		//使线段更平滑
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
		Stroke s = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g2.setStroke(s);
		//如果子弹死了,移除,并return 不画了
		if(!live) {
			tc.missiles.remove(this);
			return;
		}
		Color c = g2.getColor();
		g2.setColor(color);
		Ellipse2D ellipse1 = new Ellipse2D.Double(x-radius, y-radius, 2*radius, 2*radius);
		g2.fill(ellipse1);
		g2.setColor(c);
		
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
		lastPosition.x = x;
		lastPosition.y = y;
		x += speedV.x;
		y += speedV.y;
	}
	
	public void collidesWithScreen() {
		//判断边界条件(粗略的,后期再优化)
		if(x < 0 || y < 0 || x > TankClient.GAME_WIDTH || y > TankClient.GAME_HEIGHT) {
			if(good) new Thread(new SoundThread("sound/子弹掉落1.wav")).start();//只有友军坦克有声音		
			live = false;
		}
	}
	
	public boolean collidesWithTank(Tank t) {
		Vector position = new Vector(new MyPoint(x,y));
		Vector displacement = position.subTract(lastPosition);
		if(this.isLive() && t.isLive() && collidesWith(t,displacement).axis!=null &&  collidesWith(t,displacement).overlap !=0 && this.good !=t.isGood()) {//
			t.setLife(t.getLife()-1);
			if(t.getLife() <=0) {
				t.setLive(false);
				new Thread(new SoundThread("sound/坦克爆炸.wav")).start();//启用新进程
			}	
			this.setLive(false);
			tc.animates.add(new Animate(t.getX(),t.getY(),"boom",7,tc));
			return true;
		}
		return false;
	}
	
	public boolean collidesWithMissile(Missile m) {
		Vector position = new Vector(new MyPoint(x,y));
		Vector displacement = position.subTract(lastPosition);
		if(this.isLive() && m.isLive()  &&  collidesWith(m,displacement).overlap !=0 && good != m.good  ) {//
			this.setLive(false);	
			m.setLive(false);
			if(!this.good)this.setLive(false);
			tc.animates.add(new Animate(m.x,m.y,"boom",7,tc));
			return true;
		}
		return false;
	}
	
	/*
	 * 判断是否打中所有坦克
	 */
	public boolean hitTanks(ArrayList<Tank> tanks) {
		for(int j =0;j < tanks.size();j++) {
			if(collidesWithTank(tanks.get(j))) {
				return true;
			}
		}	
		return false;
	}
	/*
	 * 判断是否打中子弹
	 */
	public void hitMissiles(ArrayList<Missile> missiles) {
		for(int j =0;j < missiles.size();j++) {
			Missile m = missiles.get(j);
			if(this != m && m !=null) {
				collidesWithMissile(m);
			}			
		}	
	}
	
	
	/*
	 * 分离方法
	 */
	public void separate(MininumTranslationVector mtv) {
		float dx,dy;
		float theta = 0;
		
		if(mtv.axis.x == 0) {
			theta = (float)(Math.PI/2);
		}else {
			theta = (float)(Math.atan(mtv.axis.y / mtv.axis.x));
		}
		
		dy = (float)(mtv.overlap * Math.sin(theta));
		dx = (float)(mtv.overlap * Math.cos(theta));

		if (mtv.axis.x < 0 && dx > 0 || mtv.axis.x > 0 && dx < 0) dx = -dx; // account for negative angle
		if (mtv.axis.y < 0 && dy > 0 || mtv.axis.y > 0 && dy < 0) dy = -dy;
		
		x += dx;
		y += dy;		
	}
	
	/*
	 * 反弹方法
	 */
	public void  bounce(MininumTranslationVector mtv, MyShape shape,int bounceCoefficient) {//bounceCoefficient反弹系数
		Vector velocityVector = new Vector(new MyPoint(speedV.x, speedV.y));
		   Vector velocityUnitVector = velocityVector.normalize();
		   float  velocityVectorMagnitude = velocityVector.getMagnitude();
		   Vector reflectAxis = new Vector();
		   Vector point;

		   checkMTVAxisDirection(mtv, shape);
		   

		      if (mtv.axis != null) {
		         reflectAxis = mtv.axis.perpendicular();
		      }

		      separate(mtv);

		      point = velocityUnitVector.reflect(reflectAxis);
		      
		      speedV.x = point.x * velocityVectorMagnitude * bounceCoefficient;
		      speedV.y = point.y * velocityVectorMagnitude * bounceCoefficient;
	}
	
}
