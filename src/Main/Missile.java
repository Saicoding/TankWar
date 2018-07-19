package Main;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;

import Obstacle.Tree;
import Obstacle.Wall;
import Thread.SoundThread;
import shape.ImagePolygonShape;
import shape.MininumTranslationVector;
import shape.MyPoint;
import shape.Vector;

/**
 * 
 * @ClassName:     Missile   
 * @Description:   子弹类
 * @author:        saiyan
 * @date:          2018年7月5日 下午10:09:27
 */
public class Missile extends ImagePolygonShape{
	
	private float angle;
	private boolean good;//子弹阵营	
	public boolean spi = false;//子弹是不是间谍子弹
	private boolean live = true;//一new出来肯定是活着的
	public String owner;
	public String imageSource; 
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
	public Missile(float x, float y, String imageSource,TankClient tc,boolean good,boolean spi,float angle ,int speed) {
		super(x, y, imageSource, tc);
		this.x = x;
		this.y = y;
		this.good = good;
		this.speed = speed;
		this.speedV.x = speed;//初始化子弹方向和图的默认方向一致
		this.speedV.y = 0;
		this.angle = angle;
		this.tc = tc;
		this.iw = 50;
		this.ih = 13;
		this.spi = spi;
		initSpeedV();
		initPoints();
	}
	
	public void initPoints() {	
		this.points.add(clockWiseTurn(new MyPoint(x-iw/2,y-ih/2),new MyPoint(x,y),angle));
		this.points.add(clockWiseTurn(new MyPoint(x+iw/2,y-ih/2),new MyPoint(x,y),angle));
		this.points.add(clockWiseTurn(new MyPoint(x+iw/2,y+ih/2),new MyPoint(x,y),angle));
		this.points.add(clockWiseTurn(new MyPoint(x-iw/2,y+ih/2),new MyPoint(x,y),angle));
		
	}
	
	public void initSpeedV() {
	    float l = (float) ((angle * Math.PI) / 180);        
	    float cosv = (float) Math.cos(l);  
	    float sinv = (float) Math.sin(l);  
		speedV.x = cosv*speed;
		speedV.y = sinv*speed;
	}
	
 	private void update(float dx,float dy) {	
		x += dx ;
		y += dy ;
		for(int i = 0 ; i< points.size();i++) {
			MyPoint p = points.get(i);
			p.x += dx;
			p.y += dy;
		}
	}
	
	public MyPoint clockWiseTurn(MyPoint p,MyPoint cp,float angle) {
	    // calc arc   
		float l = (float) ((angle * Math.PI) / 180);  
	      
	    //sin/cos value  
		float cosv = (float)Math.cos(l);  
		float sinv = (float) Math.sin(l);  
	  
	    // calc new Point  
		float newX = (p.x - cp.x) * cosv - (p.y - cp.y) * sinv + cp.x;  
		float newY = (p.x - cp.x) * sinv + (p.y - cp.y) * cosv + cp.y;  
	    return new MyPoint((int) newX, (int) newY);  
	}
	
	/*
	 * 画子弹
	 */
	public void draw(Graphics g) {
//		//使线段更平滑
//		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
//		Stroke s = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
//		g2.setStroke(s);
//
//		Color c = g2.getColor();
//		g2.setColor(color);
//		Ellipse2D ellipse1 = new Ellipse2D.Double(x-radius, y-radius, 2*radius, 2*radius);
//		g2.fill(ellipse1);
//		g2.setColor(c);
		
		Graphics2D g2 = (Graphics2D)g;
		g2.rotate(Math.toRadians(angle),x,y);//以cx,cy为中心旋转画布
		g2.drawImage(img,(int)(x-iw/2),(int)(y-ih/2),tc);
		g2.rotate(Math.toRadians(-angle),x,y);//以cx,cy为中心旋转画布
		//如果子弹死了,移除,并return 不画了
		if(!live) {
			tc.missiles.remove(this);
			return;
		}
		
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
		update(speedV.x, speedV.y);
	}
	/*
	 * 得到当前最左或者最右或者最上或者最下的点的值
	 */
	public float getMostXY(String dir,String minOrMax) {
		ArrayList<Float> allPoint = new ArrayList<Float>();//声明一个集合装所有x
		for(int i=0;i<points.size();i++) {
			MyPoint p = points.get(i);
			if(dir == "left" || dir == "right")allPoint.add(p.x);
			else if(dir == "top" || dir == "bottom")allPoint.add(p.y);			
		}
		if( minOrMax == "min") return Collections.min(allPoint);

		else if(minOrMax == "max")return Collections.max(allPoint);
		return 0;
	}
	
	public void collidesWithScreen() {
		if(getMostXY("left", "min") < 0 || getMostXY("right", "max")>TankClient.GAME_WIDTH || getMostXY("top", "min")<24||getMostXY("bottom", "max")>TankClient.GAME_HEIGHT) {
			if(good) new Thread(new SoundThread("sound/子弹掉落1.wav")).start();//只有友军坦克有声音		
			live = false;
			tc.animates.add(new Animate(x,y,"spark",8,tc));
		}else {//如果没发生碰撞

		}
	}
	
	public boolean collidesWithTank(Tank t) {
		Vector position = new Vector(new MyPoint(x,y));
		Vector displacement = position.subTract(lastPosition);
		if(this.isLive() && t.isLive() && collidesWith(t,displacement).axis!=null &&  collidesWith(t,displacement).overlap !=0 && spi == !t.spi) {//
			t.setLife(t.getLife()-1);
			if(t.getLife() <=0) {
				t.setLive(false);
				if(!t.good && owner == "p1")tc.p1AllKillNum++;//如果是p1打死的 ,打死数量就加1
				else if(!t.good && owner == "p2")tc.p2AllKillNum++;//如果是p2打死的,打死数量就加1
				new Thread(new SoundThread("sound/坦克爆炸.wav")).start();//打死就是爆炸声
				tc.animates.add(new Animate(t.getX(),t.getY(),"boom",19,tc));
			}else {
				new Thread(new SoundThread("sound/子弹打到坦克上.wav")).start();//没打死就发出这个声音
				tc.animates.add(new Animate(x,y,"spark",8,tc));
			}
			this.setLive(false);
			
			//我的坦克的方法
			if(owner == "p1") {
				if(t == tc.myTanks.get(0).lastHitTank ) {//如果打的这个坦克是上一次击中的坦克,连续击中就加1
					tc.myTanks.get(0).serialHitNum ++;
					//如果连续打中同一辆敌人坦克4次,敌人坦克就变成好坦克
					if(tc.myTanks.get(0).serialHitNum >= tc.myTanks.get(0).toSpiNum-1) {
						t.spi = true;
						t.timer.start();//启动计时器
					}
				}else {
					tc.myTanks.get(0).serialHitNum = 0;
				}
				tc.myTanks.get(0).lastHitTank = t;
				tc.p1AllHitNum++;
			}else if(owner == "p2") {
				if(t == tc.myTanks.get(1).lastHitTank ) {//如果打的这个坦克是上一次击中的坦克,连续击中就加1
					tc.myTanks.get(1).serialHitNum ++;
					if(tc.myTanks.get(1).serialHitNum >= tc.myTanks.get(0).toSpiNum-1) {
						t.spi = true;
						t.timer.start();//启动计时器
					}
				}else {
					tc.myTanks.get(1).serialHitNum = 0;
				}
				tc.myTanks.get(1).lastHitTank = t;			
				tc.p2AllHitNum++;		
			}
			
			
			
			return true;
		}
		return false;
	}
	/*
	 * 子弹相撞
	 */
	public boolean collidesWithMissile(Missile m) {
		Vector position = new Vector(new MyPoint(x,y));
		Vector displacement = position.subTract(lastPosition);
		if(this.isLive() && m.isLive()  &&  collidesWith(m,displacement).overlap !=0 && good != m.good  ) {//
			this.setLive(false);	
			m.setLive(false);
			if(!this.good)this.setLive(false);
			tc.animates.add(new Animate(m.x,m.y,"spark",8,tc));
			return true;
		}
		return false;
	}
	/*
	 * 子弹打树
	 */
	public boolean collidesWithTree(Tree tree) {
		Vector position = new Vector(new MyPoint(x,y));
		Vector displacement = position.subTract(lastPosition);
		if(this.isLive() && tree.live  &&  collidesWith(tree,displacement).overlap !=0) {//
			this.setLive(false);	
			tree.live = false;
			tc.animates.add(new Animate(tree.x,tree.y,"spark",8,tc));
			return true;
		}
		return false;
	}
	
	/*
	 * 子弹打墙
	 */
	public boolean collidesWithWall(Wall wall) {
		Vector position = new Vector(new MyPoint(x,y));
		Vector displacement = position.subTract(lastPosition);
		if(this.isLive() && wall.live  &&  collidesWith(wall,displacement).overlap !=0) {//
			this.setLive(false);	
			wall.live = false;
			tc.animates.add(new Animate(x,y,"spark",8,tc));
			return true;
		}
		return false;
	}
	/*
	 * 判断是否打中墙
	 */
	public boolean hitWalls(ArrayList<Wall> walls) {
		for(int j =0;j < walls.size();j++) {
			if(collidesWithWall(walls.get(j))) {
				return true;
			}
		}	
		return false;
	}
	
	/*
	 * 判断是否打中树
	 */
	public boolean hitTrees(ArrayList<Tree> trees) {
		for(int j =0;j < trees.size();j++) {
			if(collidesWithTree(trees.get(j))) {
				return true;
			}
		}	
		return false;
	}
	
	/*
	 * 判断是否打中坦克
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
	 * 子弹是圆形的反弹方法
	 */
//	public void  bounce(MininumTranslationVector mtv, MyShape shape,int bounceCoefficient) {//bounceCoefficient反弹系数
//		Vector velocityVector = new Vector(new MyPoint(speedV.x, speedV.y));
//		   Vector velocityUnitVector = velocityVector.normalize();
//		   float  velocityVectorMagnitude = velocityVector.getMagnitude();
//		   Vector reflectAxis = new Vector();
//		   Vector point;
//
//		   checkMTVAxisDirection(mtv, shape);
//		   
//
//		      if (mtv.axis != null) {
//		         reflectAxis = mtv.axis.perpendicular();
//		      }
//
//		      separate(mtv);
//
//		      point = velocityUnitVector.reflect(reflectAxis);
//		      
//		      speedV.x = point.x * velocityVectorMagnitude * bounceCoefficient;
//		      speedV.y = point.y * velocityVectorMagnitude * bounceCoefficient;
//	}
	
}
