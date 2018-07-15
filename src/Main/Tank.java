package Main;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import Thread.SoundThread;
import shape.MininumTranslationVector;
import shape.MyPoint;
import shape.MyPolygon;
import shape.Vector;


public class Tank extends MyPolygon{
	private float lastX = 700,lastY=600,lastAngle=0;//上一次的坐标和中心坐标
	private Vector lastPosition = new Vector();
	private int life = 0;//坦克生命值
	public float angle = 0;//坦克角度
	
	public int flag;//测试用flag
	
//	private int fullLife = 10;//坦克最大生命值
	
//	private BloodBar bb = new BloodBar();
	
	private ArrayList<Color> colorList = null;//定义坦克颜色
	
	private boolean live = true;
	private int speed;//速度
	public Vector speedV = new Vector();
	
	private float tw = 11;//履带宽度
	private float th = 80;//履带高度
	private float mw = 30;//中断宽度
	private float mh = 63;//中间高度
	private float pw = 4;//炮筒宽度
	private float ph = 50;//炮筒长度
	
	private float width = tw*2 +mw+2*this.fengxi;//坦克宽度
	private float height = this.th;//坦克高度
	
	private static Random random =new Random();//随机数产生器
	
	public int missileWidth = 10;//该坦克弹管宽度
	public int missileHeight = 10;//该坦克弹管高度

	private int leftTrackPosition = 5;//履带位置
	private int rightTrackPosition = 5;//履带位置
	private int lvdaiSpace = 6;//履带条纹间隙
	private int trackSpeed =2;//履带条纹变化频率
	private int fengxi = 1;//车身缝隙
	
	private int myTankShotSpeend = 20;//我的坦克射击速度
	private int myMissileSize = 20;//我的坦克子弹大小
	private int myTankTurnSpeed = 4;//我的坦克转弯速度

	private int enemyTankMissileSpeed = 7;//敌人坦克子弹速度
	private int enemyTankMissileSize = 10;//敌人坦克子弹大小
	private int enemyTankTurnSpeed = 1;//敌人坦克转弯速度
	
	private boolean good =false;//好坏坦克
	private boolean bL = false,bR = false,bF = false,bB = false;//定义初始四个方向键按下状态
	private boolean stop = false;//坦克是否停下
	
	//计步器
	private int moveStep = 40;//坦克计步器
	private int fireStep = 40;//射击计步步
	private int randomTurnAngle =0;//随机的转向角度
	private boolean turning = false;//是否在转弯

	TankClient tc;	//利用构造函数持有对象引用	
	

	/*
	 * 构造函数
	 */
	

	public Tank(float x, float y, ArrayList<Color> colorList,boolean good,int speed) {//好坏坦克用good区分
		this.x = x;
		this.y = y;
		this.colorList = colorList;
		this.good = good;
		this.speed = speed;
		
		if(good) {
			this.life=10;//如果是友 生命初始是10
			this.stop = true;//友军初始停止
		}
		else {
			this.life =1;//如果是敌人,生命初始是1
			this.stop =false;//敌人初始不停止
			this.bF = true;//如果是敌人,坦克初始向前
		}

	}
	
	/*
	 * 利用构造函数持有对象引用
	 */
	public Tank(float x,float y ,ArrayList<Color> colorList,boolean good ,int speed,float angle, TankClient tc) {
		this(x,y,colorList,good, speed);
		this.tc = tc;
		this.angle = angle;
		this.speedV.x = speed;//初始向量的方向和坦克的初始方向一样
		this.speedV.y = 0;
		this.initPoints();
		this.initSpeedV();//初始化速度向量
	}
	public void initSpeedV() {
	    float l = (float) ((angle * Math.PI) / 180);        
	    float cosv = (float) Math.cos(l);  
	    float sinv = (float) Math.sin(l);  
		speedV.x = cosv*speed;
		speedV.y = sinv*speed;
	}
	public void initPoints() {
		this.points.add(clockWiseTurn(new MyPoint(x-height/2,y-width/2),new MyPoint(x,y)));
		this.points.add(clockWiseTurn(new MyPoint(x+height/2,y-width/2),new MyPoint(x,y)));
		this.points.add(clockWiseTurn(new MyPoint(x+height/2,y+width/2),new MyPoint(x,y)));
		this.points.add(clockWiseTurn(new MyPoint(x-height/2,y+width/2),new MyPoint(x,y)));
	}
	/*
	 * 根据不同坦克设置坦克的移动方式
	 */
	private void initMove() {
		if(good) {
			
		}else {
			if(moveStep <=0) {//如果前进步数没有了,就随机一个角度,开始转向
				if(bB) {//倒退一次后将后退状态改为前进状态
					bB = false;
					bF = true;
				}
				randomTurnAngle = random.nextInt(360)-180;//随机一个角度
				moveStep =random.nextInt(50)+20;
				turning = true;
			}
		}
	}
	
	/*
	 * 坦克转向
	 */
	private void turn() {	
		if(good) {//友军
			if(bL) angle -= myTankTurnSpeed;
			if(bR) {
				angle += myTankTurnSpeed;
				initSpeedV();
			}

		}else {//敌军
			if(!turning)return;//如果不是转弯状态就return
			if(randomTurnAngle >0) {//如果randomAngle是正数,说明是向右转
				angle += enemyTankTurnSpeed;
				randomTurnAngle -= enemyTankTurnSpeed;
			}else if(randomTurnAngle <0) {//如果randomAngle是负,说明是向左转
				angle -= enemyTankTurnSpeed;
				randomTurnAngle+= enemyTankTurnSpeed;
			}else if(Math.abs(randomTurnAngle) ==0) {
				turning =false;//如果randomAngle是零,说明转弯结束
				moveStep = random.nextInt(100)+20;
			}
		}
		if(angle!=lastAngle){
			changePointTurned();//每次改变角度后都改指定坦克碰撞模型的点	
			if(angle >lastAngle) {//右转
				this.leftTrackPosition -=trackSpeed;
				this.rightTrackPosition +=trackSpeed;
				if(this.leftTrackPosition <0)this.leftTrackPosition =6;					
				if(this.rightTrackPosition >6)this.rightTrackPosition =0;
			}else {
				this.leftTrackPosition +=trackSpeed;
				this.rightTrackPosition -=trackSpeed;
				if(this.leftTrackPosition >6)this.leftTrackPosition =0;					
				if(this.rightTrackPosition <0)this.rightTrackPosition =6;
			}
		}
		
	}
	/*
	 * 向前移动
	 */
	public void moveForword() {
		if(turning) return;//turning是敌人转悠属性,自己坦克turning初始值是false,所以友军这里永远不执行
		setSpeedV(speedV.x,speedV.y);//每次前后移动都设置下速度方向
		update(speedV.x,speedV.y);
		if(!good) {
			moveStep--;//敌军每移动一次,计步减一
			fireStep--;//敌军每移动一次,发火计步减一
		}
		
	}
	/*
	 * 向后移动
	 */
	public void moveBack() {
		if(turning) return;
		setSpeedV(-speedV.x,-speedV.y);//每次前后移动都设置下速度方向

		update(speedV.x,speedV.y);
		if(!good) {
			moveStep--;//敌军每移动一次,计步减一
			fireStep--;//敌军每移动一次,发火计步减一
		}
	}
	public void enemyFire() {
		if(good)return;
		if(fireStep <=0) {
			tc.missiles.add(fire(enemyTankMissileSpeed,enemyTankMissileSize));
			fireStep = random.nextInt(50)+10;
		}
	}
	/*
	 * 得到某个点从零度绕到当前前角度后的点
	 */	
	public MyPoint clockWiseTurn(MyPoint p,MyPoint cp) {
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
	 * 
	 */
	public void changePointTurned() {
		ArrayList<MyPoint> mypoints =  new ArrayList<MyPoint>();
		mypoints.add(new MyPoint(x-height/2,y-width/2));
		mypoints.add(new MyPoint(x+height/2,y-width/2));
		mypoints.add(new MyPoint(x+height/2,y+width/2));
		mypoints.add(new MyPoint(x-height/2,y+width/2));
		
		for(int i = 0 ; i< mypoints.size();i++) {
			MyPoint p = points.get(i);	
			MyPoint mp = mypoints.get(i);
			p.x = clockWiseTurn(mp,new MyPoint(x,y)).x;
			p.y = clockWiseTurn(mp,new MyPoint(x,y)).y;
		}	
	}
	
	/*
	 * 坦克移动方法
	 */
	public void move() {
		lastX = x;
		lastY = y;
		lastAngle = angle;
		lastPosition.x = x;
		lastPosition.y = y;
		
		initMove() ;
		turn();//先处理转弯,这里敌人必须转弯后才能移动
		setTurnedSpeedV();//每次转向都要设置速度向量
		
		if(!stop) {//如果不是停止状态,就有两种可能,一是向前,一是向后
			if(bF) {
				moveForword();	
				this.leftTrackPosition -=trackSpeed;
				this.rightTrackPosition -=trackSpeed;
				if(this.leftTrackPosition <0)this.leftTrackPosition =6;					
				if(this.rightTrackPosition <0)this.rightTrackPosition =6;
					
			}else if(bB){
				moveBack();
				this.leftTrackPosition +=trackSpeed;
				this.rightTrackPosition +=trackSpeed;
				if(this.leftTrackPosition >6)this.leftTrackPosition =0;					
				if(this.rightTrackPosition >6)this.rightTrackPosition =0;
			}
			//坦克是要是运动,肯定就更新履带
		}
		enemyFire();

	}

	
	
	/*
	 * 回退上一帧状态
	 */
	public void goBackLastFrame() {
		x = lastX;
		y = lastY;
		angle = lastAngle;
		changePointTurned();
	}

	/*
	 * 更新所有点
	 */
 	private void update(float dx,float dy) {	
		x += dx ;
		y += dy ;
		for(int i = 0 ; i< points.size();i++) {
			MyPoint p = points.get(i);
			p.x += dx;
			p.y += dy;
		}
	}
	/*
	 * 敌人坦克后退方法
	 */
	public void enemyTankback() {
		x -= myCos(speedV.x);
		y -= mySin(speedV.y);
	}

/*************************画坦克****************************************/	
	/*
	 * 画坦克
	 */
	public void draw(Graphics g) {
		if(!live) {//如果坦克死了,就不画
			if(!good) {
				tc.enemyTanks.remove(this);
				return;
			}else {
//				tc.myTanks.remove(this);
				return;
			}
		}
		tankModal(g,angle+90);
	
		move();
	}
	
	/*
	 * 坦克模型
	 */
	public void tankModal(Graphics g,float angel) {

		Graphics2D g2 = (Graphics2D) g;
		Color c = g2.getColor();
		//使线段更平滑
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
		Stroke s = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		Stroke s1 = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g2.setStroke(s);
		
		g2.rotate(Math.toRadians(angel),x,y);//以cx,cy为中心旋转画布
			
		//画两个履带
		RoundRectangle2D rectRound1 = new RoundRectangle2D.Double(x-tw-mw/2, y-th/2, tw, th,9,3);
		RoundRectangle2D rectRound2 = new RoundRectangle2D.Double(x+mw/2, y-th/2, tw, th,9,3);
		GradientPaint gdp1 = new GradientPaint(x-tw-mw/2, y-th/2,colorList.get(0),x-tw-mw/2,y-th/2+th,colorList.get(1),true);
		g2.setPaint(gdp1);
		g2.fill(rectRound1 );
		g2.fill(rectRound2);

		//画中间车身
		GradientPaint g1 = new GradientPaint(x-mw/2+fengxi,y-mh/2,colorList.get(2),x-mw/2+fengxi+mw-2*fengxi,y-mh/2+mh,colorList.get(3));
		g2.setPaint(g1);
		RoundRectangle2D rectRound3 = new RoundRectangle2D.Double(x-mw/2+fengxi, y-mh/2, mw-2*fengxi, mh,5,5);
		g2.fill(rectRound3);

		GradientPaint g3 = new GradientPaint(x,y-mh/2-20,colorList.get(4),x,y+20,colorList.get(5));
		g2.setPaint(g3);
		g2.fill(tankTopDraw());
		
		//画中间的横杠
		g2.setColor(colorList.get(6));
		Rectangle2D rect1 = new Rectangle2D.Double(x-2,y-26,4,2);
		g2.fill(rect1);
		
		//画中间的圆角矩形
		g2.setColor(colorList.get(7));
		RoundRectangle2D rectRound4 = new RoundRectangle2D.Double(x-13,y-5,26,10,10,10);		
		g2.fill(rectRound4);
		g2.setColor(colorList.get(8));
		Rectangle2D rect2 = new Rectangle2D.Double(x-1,y-5,2,10);
		g2.fill(rect2);
		//画圆角矩形里的两个圆
		if(good) {
			colorList.set(9, new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
			g2.setColor(colorList.get(9));
		}else {
			g2.setColor(colorList.get(9));
		}
		Ellipse2D ellipse1 = new Ellipse2D.Double(x-11,y-3,6,6);
		g2.fill(ellipse1);
		g2.setColor(colorList.get(10));
		Ellipse2D ellipse2 = new Ellipse2D.Double(x+5,y-3,6,6);
		g2.fill(ellipse2);
		//画上方的两个横条
		g2.setColor(colorList.get(11));
		Rectangle2D rect3 = new Rectangle2D.Double(x-13,y-27,8,2);
		g2.fill(rect3);
		Rectangle2D rect4 = new Rectangle2D.Double(x+5,y-27,8,2);
		g2.fill(rect4);
		//画两个把手
		g2.setColor(colorList.get(12));
		Arc2D arc1 = new Arc2D.Double(x-10, y-17, 8, 8, 0, -180,Arc2D.OPEN);	
		g2.fill(arc1);
		g2.setColor(colorList.get(13));
		Arc2D arc2 = new Arc2D.Double(x+2, y-17, 8, 8, 0, -180,Arc2D.OPEN);
		g2.fill(arc2);
		g2.setColor(colorList.get(14));
		Arc2D arc3 = new Arc2D.Double(x-5, y-25, 10, 8, 0, -180,Arc2D.OPEN);
		g2.fill(arc3);
		//画下面一点的矩形
		g2.setColor(colorList.get(15));
		RoundRectangle2D rectRound5 = new RoundRectangle2D.Double(x-10,y+10,20,5,3,3);	
		g2.fill(rectRound5);
		g2.setColor(colorList.get(16));
		RoundRectangle2D rectRound6 = new RoundRectangle2D.Double(x-7,y+10,14,5,3,3);	
		g2.fill(rectRound6);
		g2.setColor(colorList.get(17));
		RoundRectangle2D rectRound7 = new RoundRectangle2D.Double(x-6,y+10,12,5,3,3);	
		g2.fill(rectRound7);
		//画下面的栅栏
		g2.setStroke(s1);
		g2.setColor(colorList.get(18));
		RoundRectangle2D rectRound8 = new RoundRectangle2D.Double(x-12,y+20,24,10,3,3);	
		g2.draw(rectRound8);
		Line2D line3 = new Line2D.Double(x-8,y+20,x-8,y+30);
		Line2D line4 = new Line2D.Double(x-4,y+20,x-4,y+30);
		Line2D line5 = new Line2D.Double(x,y+20,x,y+30);
		Line2D line6 = new Line2D.Double(x+4,y+20,x+4,y+30);
		Line2D line7 = new Line2D.Double(x+8,y+20,x+8,y+30);
		g2.draw(line3);
		g2.draw(line4);
		g2.draw(line5);
		g2.draw(line6);
		g2.draw(line7);
		g2.setColor(c);
		

		g2.setStroke(s);
		g2.setColor(colorList.get(19));
		//画履带格子
		for(int i = 0 ;this.leftTrackPosition+i*lvdaiSpace < th;i++) {
			MyPoint p1 = new MyPoint(x-tw-mw/2+fengxi,y-th/2+i*lvdaiSpace +this.leftTrackPosition);
			MyPoint p2 = new MyPoint(x-mw/2,y-th/2+i*lvdaiSpace +this.leftTrackPosition);
			Line2D line1 = new Line2D.Double(p1.x,p1.y,p2.x,p2.y);
			g2.draw(line1);
		}
		for(int i = 0 ;this.rightTrackPosition+i*lvdaiSpace < th;i++) {
			MyPoint p3 = new MyPoint(x+mw/2+fengxi, y-th/2+i*lvdaiSpace +this.rightTrackPosition);
			MyPoint p4 = new MyPoint(x+mw/2+tw, y-th/2+i*lvdaiSpace +this.rightTrackPosition);
			Line2D line2 = new Line2D.Double(p3.x,p3.y,p4.x,p4.y);
			g2.draw(line2);
		}
		g2.rotate(Math.toRadians(-angel),x,y);//恢复旋转
		
		if(good) {
			//bb.draw(g2);
		}		
	}
	/*
	 * 坦克盖路径
	 */
	private Shape tankTopDraw() {
		MyPoint p1=new MyPoint(x-10,y+10);
		MyPoint p2=new MyPoint(x+10,y+10);
	    
		MyPoint p3=new MyPoint(x+20,y-6);
		MyPoint p4=new MyPoint(x+5,y-21);
		MyPoint p5=new MyPoint(x+2,y-26);
	   
		MyPoint p6=new MyPoint(x+pw/2,y-ph);
	    
		MyPoint p7=new MyPoint(x-pw/2,y-ph);
		MyPoint p8=new MyPoint(x-2,y-26);
		MyPoint p9=new MyPoint(x-5,y-21);
		MyPoint p10=new MyPoint(x-20,y-6);
		MyPoint p11=new MyPoint(x-10,y+10);
	    
	    GeneralPath gp=new GeneralPath();    //shape的子类，表示一个形状
	    gp.append(new Line2D.Double(p1.x,p1.y,p2.x,p2.y),true);   //在形状中添加一条线，即Line2D
	    gp.lineTo(p3.x,p3.y);   //添加一个点,并和之前的线段相连
	    gp.lineTo(p4.x,p4.y);  
	    gp.lineTo(p5.x,p5.y);  
	    gp.lineTo(p6.x,p6.y);  
	    gp.lineTo(p7.x,p7.y);  
	    gp.lineTo(p8.x,p8.y);  
	    gp.lineTo(p9.x,p9.y);  
	    gp.lineTo(p10.x,p10.y);  
	    gp.lineTo(p11.x,p11.y);  
	    gp.closePath();  //关闭形状创建
	    
	    return gp;    //返回该形状
	}
	/*
	 * 坦克血条内部类
	 */
//	private class BloodBar{
//		public void draw(Graphics2D g2) {
//			Color c = g2.getColor();
//			g2.setColor(new Color(139,0,0));
//			if(life <=3) {//如果血量小于3了就开始闪
//				g2.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
//			}
//			g2.drawRect(getCx()-height/2,getCy()-height/2-23, height,20 );
//			g2.fillRect(getCx()-height/2,getCy()-height/2-23, height/fullLife*life,20);
//			g2.setColor(c);
//		}
//	}

// ******************************************************碰撞检测
	public void enemyCollidesWithWallAndTank() {
		if(!good) {//如果是敌人坦克
			turning = false;
			if(bB) {//如果是后退状态
				bB = false;
				bF = true;
				moveStep = random.nextInt(5)+3;
			}else {
				bB = true;
				bF = false;
				moveStep = random.nextInt(5)+3;
			}
		}
	}
	/*
	 * 碰撞检测
	 */
	public void collidesWithWall(Wall w) {
		Vector position = new Vector(new MyPoint(x,y));
		Vector displacement = position.subTract(lastPosition);
		MininumTranslationVector mtv=collidesWith(w,displacement);
		
		if(mtv.axis !=null && mtv.overlap !=0) {
			goBackLastFrame();
			enemyCollidesWithWallAndTank();
		}
	}
	
	public void collidesWithScreen() {
		if(getMostXY("left", "min") < 0 || getMostXY("right", "max")>TankClient.GAME_WIDTH || getMostXY("top", "min")<24||getMostXY("bottom", "max")>TankClient.GAME_HEIGHT) {
			goBackLastFrame();
			enemyCollidesWithWallAndTank();
		}else {//如果没发生碰撞

		}
	}

	
	public MininumTranslationVector collidesWithTank(Tank t) {
		Vector position = new Vector(new MyPoint(x,y));
		Vector displacement = position.subTract(lastPosition);
		MininumTranslationVector mtv=collidesWith(t,displacement );

		return mtv;
	}
	/*
	 * 判断坦克之间相撞
	 */
	public void collidesWithTanks(ArrayList<Tank> tanks) {
		//判断如果是碰撞状态,就恢复到上一次状态
		for(int i = 0; i < tanks.size(); i++) {
			Tank t = tanks.get(i);
			if(this != t) {

				MininumTranslationVector mtv = collidesWithTank(t);
				if(mtv.axis!=null && mtv.overlap!=0) {					
					goBackLastFrame();	
//					separate(mtv);
					enemyCollidesWithWallAndTank();
				}
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
		Float dot = mtv.axis.dotProduct(speedV);
		if(dot < 0){
			dy = (float)(mtv.overlap * Math.sin(theta));
			dx = (float)(mtv.overlap * Math.cos(theta));
		}else {
			dy = 0;
			dx = 0;
		}

		if (mtv.axis.x < 0 && dx > 0 || mtv.axis.x > 0 && dx < 0) dx = -dx; // account for negative angle
		if (mtv.axis.y < 0 && dy > 0 || mtv.axis.y > 0 && dy < 0) dy = -dy;
		setSpeedV(speedV.x,speedV.y);//每次前后移动都设置下速度方向
		update(dx,dy);
	}

//****************************************************************************************

	/*
	 * 吃血块
	 */
	public boolean eat(Blood b) {
//		if(this.live && b.isLive()&& this.getRect().intersects(b.getRect())) {
//			this.life =fullLife;
//			b.setLive(false);
//			return true;
//		}
		return false;
	}
//射击------------------------------------------------------
	/*
	 * 射击,用面向对象的思想，当坦克射击时会射出一个子弹
	 */
	public Missile fire(int speed,int radius) {
		Missile m =new Missile((int)getPtPoint().x, (int)getPtPoint().y, radius,good,angle, speed,tc);//炮筒方向是哪个，子弹方向就是哪个
		if(good) {
			new Thread(new SoundThread("sound/发炮.wav")).start();//启用新进程
		}
		return m;
	}
	/*
	 * 重载fire方法
	 */
	public Missile fire(int speed,int radius,int angle) {
		Missile m =new Missile((int)getPtPoint().x, (int)getPtPoint().y,radius, good,angle, speed,tc);//炮筒方向是哪个，子弹方向就是哪个
		return m;
	}

	/*
	 * 超级炮弹
	 */
	public void superFire() {
		int[] dirs = {0,45,90,135,180,225,270,315};
		for(int i=0 ;i<dirs.length ;i++) {
			Missile m = fire(2,100,dirs[i]);
			tc.missiles.add(m);
		}		
	}

//按键控制------------------------------------------------------------
	/*
	 * 按键按下状态
	 */
	public void keyPressed(KeyEvent e,String p) {
		if(!live)return;//如果死了就失效
		int key = e.getKeyCode();
		if(p == "P1") {
			switch(key) {
				//如果按了ctrl键，就发射子弹，将坦克产生的子弹对象添加到tc.missiles中
				case KeyEvent.VK_F:	
					superFire();
					break;
				case KeyEvent.VK_G:	
					tc.missiles.add(fire(myTankShotSpeend,myMissileSize));
					break;
				case KeyEvent.VK_A:
					bL =true;
					break;
				case KeyEvent.VK_D:	
					bR =true;
					break;
				case KeyEvent.VK_W:		
					bF= true;
					break;
				case KeyEvent.VK_S:	
					bB = true;
					break;
			}
		}else if(p =="P2") {
			switch(key) {
			//如果按了ctrl键，就发射子弹，将坦克产生的子弹对象添加到tc.missiles中
				case KeyEvent.VK_M:	
					superFire();
					break;
				case KeyEvent.VK_SPACE:	
					tc.missiles.add(fire(myTankShotSpeend,myMissileSize));
					break;
				case KeyEvent.VK_LEFT:
					bL = true;
					break;
				case KeyEvent.VK_RIGHT:
					bR = true;
					break;
				case KeyEvent.VK_UP:
					bF = true;
					break;
				case KeyEvent.VK_DOWN:
					bB = true;
					break;
			}
		}
				
		locateDirection();
	}

	/*
	 * 按键释放状态
	 */
	public void keyRelease(KeyEvent e,String p) {
		int key = e.getKeyCode();
		if(p == "P1") {//玩家1
			switch(key) {
				case KeyEvent.VK_A:
					bL = false;
					break;
				case KeyEvent.VK_D:	
					bR = false;
					break;
				case KeyEvent.VK_W:		
					bF = false;
					break;
				case KeyEvent.VK_S:	
					bB = false;
					break;
			}
		}else if(p =="P2") {//玩家2
			switch(key) {
				case KeyEvent.VK_LEFT:
					bL = false;
					break;
				case KeyEvent.VK_RIGHT:
					bR = false;
					break;
				case KeyEvent.VK_UP:
					bF = false;
					break;
				case KeyEvent.VK_DOWN:
					bB = false;
					break;
			}
		}			
		locateDirection();

	}	


	/*
	 * 确定坦克方向dir
	 */
	private void locateDirection() {
		if(good) {
			if(!bF && !bB ) {
				setStop(true);
			}
			if(bF || bB) {
				setStop(false);
			}		
		}
	}

//快捷方法-------------------------------------------------------------
	public void setTurnedSpeedV() {		
		float l = (float)((angle * Math.PI)/ 180);
		speedV.x = (float)(Math.cos(l)*speed);
		speedV.y = (float)(Math.sin(l)*speed);
		
	}
	public float myCos(float data) {
		float l = (float)((angle * Math.PI) / 180);  	      
		float cosv = (float) Math.cos(l);  
		return  cosv*data;
	}
	public float mySin(float data) {
		float l = (float) ((angle * Math.PI) / 180);  	      
		float sinv = (float) Math.sin(l);  
		return  sinv*data;
	}
	
	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}
	/*
	 * 得到坦克中心x
	 */
	public float getX() {
		return x;
	}
	/*
	 * 得到坦克中心y
	 */
	public float getY() {
		return y;
	}
	/*
	 * 得到是否是友军
	 */
	public boolean isGood() {
		return good;
	}
	/*
	 * 得到生命值
	 */
	public int getLife() {
		return life;
	}
		
	public void setLife(int life) {
		this.life = life;
	}

	/*
	 * 设置生死
	 */
	public void setLive(boolean live) {
		this.live = live;
	}
	/*
	 * 得到是否活着
	 */
	public boolean isLive() {
		return live;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}
	/*
	 * 得到炮筒中心点
	 */
	public MyPoint getPtPoint() {
		MyPoint pt = clockWiseTurn(new MyPoint(x+ph,y), new MyPoint(x,y));
		return pt;
	}
	
	public void setSpeedV(float x,float y) {
		speedV.x = x;
		speedV.y = y;
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
}
