package Main;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
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

import Obstacle.MyScreen;
import Obstacle.River;
import Obstacle.Tree;
import Obstacle.Wall;
import Thread.SoundThread;
import shape.MininumTranslationVector;
import shape.MyPoint;
import shape.MyPolygon;
import shape.MyShape;
import shape.Vector;


public class Tank extends MyPolygon{
	private float lastAngle=0;//上一次的坐标和中心坐标
	private Vector lastPosition = new Vector();
	public  Timer timer = new Timer();
	
	public String owner;
	public boolean spi = false;//是否是间谍坦克
	public int serialHitNum =0;//连续击中坦克数量
	public int toSpiNum = 0;//变成自己坦克需要打击的数量
	public Tank lastHitTank;//上一次击中的坦克

	public float angle = 0;//坦克角度
	public float ptAngle =0;//炮筒角度
	public String name;
	public int flag;//测试用flag
	
	private int life = 0;//坦克生命值
	private int fullLife ;//坦克最大生命值
	private int missilesNum;//坦克弹药
	private int fullMissilesNum ;//坦克弹药库存
	
	private BloodBar bb = new BloodBar();
	
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
	private int myTankTurnSpeed = 7;//我的坦克转弯速度

	private int enemyTankMissileSpeed = 8;//敌人坦克子弹速度
	private int enemyTankTurnSpeed = 1;//敌人坦克转弯速度
	
	public boolean good =false;//好坏坦克
	private boolean bL = false,bR = false,bF = false,bB = false;//定义初始四个方向键按下状态
	private boolean stop = false;//坦克是否停下
	
	//计步器
	private int moveStep = 20;//坦克计步器
	private int fireStep = 20;//射击计步步
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
			this.fullLife = life;//最大生命;
			this.missilesNum = 5000;//初始弹药数量
			this.fullMissilesNum = missilesNum;//初始弹药库
			this.stop = true;//友军初始停止
			this.spi = true;	
		}
		else {
			this.life =1;//如果是敌人,生命初始是1
			this.fullLife = life;//最大生命
			this.missilesNum = 500;//初始弹药数量
			this.fullMissilesNum = missilesNum;//初始弹药库
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
		this.ptAngle = angle;
		this.speedV.x = speed;//初始向量的方向和坦克的初始方向一样
		this.speedV.y = 0;
		this.initPoints();
		this.initSpeedV();//初始化速度向量
	}
	/*
	 * 根据坦克角度求速度向量
	 */
	public void initSpeedV() {
	    float l = (float) ((angle * Math.PI) / 180);        
	    float cosv = (float) Math.cos(l);  
	    float sinv = (float) Math.sin(l);    
		speedV.x = cosv*speed;
		speedV.y = sinv*speed;
	}
	/*
	 * 根据向量求角度
	 */
	public float getAngleByVector(Vector v) {
		float theta = 0;
		if(v.x == 0) {
			theta = (float)(Math.PI/2);
		}else {
			theta = (float)(Math.atan(v.y / v.x));
		}
		
		return (float)(180*theta/Math.PI);
	}
	
	/*
	 * 初始化坦克模型点
	 */
	public void initPoints() {
		this.points.add(clockWiseTurn(new MyPoint(x-height/2,y-width/2),new MyPoint(x,y),angle));
		this.points.add(clockWiseTurn(new MyPoint(x+height/2,y-width/2),new MyPoint(x,y),angle));
		this.points.add(clockWiseTurn(new MyPoint(x+height/2,y+width/2),new MyPoint(x,y),angle));
		this.points.add(clockWiseTurn(new MyPoint(x-height/2,y+width/2),new MyPoint(x,y),angle));
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
				float shotAngle = enemyTankGetMypos();
				
				float turnAngle = (shotAngle - angle)%360;
				
				if(turnAngle > 180 )randomTurnAngle =(int)(360 - turnAngle);
				else if(turnAngle < -180 )randomTurnAngle =(int)(360 + turnAngle);
				else randomTurnAngle = (int)turnAngle;
				
//				randomTurnAngle = random.nextInt(360)-180;//随机一个角度
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
			if(bR) angle += myTankTurnSpeed; 
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
	/*
	 * 敌人坦克射击方法
	 */
	public void enemyFire() {
		if(good)return;
		if(fireStep <=0) {
			if(missilesNum > 0 )tc.missiles.add(fire(enemyTankMissileSpeed));
			fireStep = random.nextInt(10)+10;
		}
	}
	/*
	 * 得到某个点从零度绕到指定角度后的点
	 */	
	public MyPoint clockWiseTurn(MyPoint p,MyPoint cp,float angle) {
	    // calc arc   
		float l = (float) ((angle * Math.PI) / 180);  
	      
	    //sin/cos value  
		float cosv = (float)Math.cos(l);  
		float sinv = (float)Math.sin(l);  
	  
	    // calc new Point  
		float newX = (p.x - cp.x) * cosv - (p.y - cp.y) * sinv + cp.x;  
		float newY = (p.x - cp.x) * sinv + (p.y - cp.y) * cosv + cp.y;  
	    return new MyPoint((int) newX, (int) newY);  
	}

	/*
	 * 每次转弯更新坦克模型点
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
			p.x = clockWiseTurn(mp,new MyPoint(x,y),angle).x;
			p.y = clockWiseTurn(mp,new MyPoint(x,y),angle).y;
		}	
	}
	
	/*
	 * 坦克移动方法
	 */
	public void move() {
		lastAngle = angle;
		if(timer.getElaplseTime() > 1000) {
			spi = false;
			timer.reset();
		}
		
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
	 * 敌人坦克得到我的坦克方向向量
	 */
	private float enemyTankGetMypos() {
		float theta = 0;
		//得到要打击的玩家坦克
		if(tc.myTanks.size() == 0) return 0;
		int num = random.nextInt(tc.myTanks.size());
		System.out.println(num);
		Tank myTank = tc.myTanks.get(num);
		MyPoint enemyCenter = centroid();
		MyPoint myTankCenter = myTank.centroid();
		
		Vector findMe = new Vector(myTankCenter).subTract(new Vector(enemyCenter));
		
		if(findMe.x == 0) {
			theta = (float)(Math.PI/2);
		}else {
			theta = (float)(Math.atan(findMe.y / findMe.x));
		}
		float shotAngle = (float)(180*theta/Math.PI);
		
		//两种情况	
		if((myTankCenter.x < enemyCenter.x && myTankCenter.y > enemyCenter.y) ||
			(myTankCenter.x < enemyCenter.x && myTankCenter.y < enemyCenter.y)	
				) {
			shotAngle = 180+shotAngle;
		}else if((myTankCenter.x > enemyCenter.x && myTankCenter.y < enemyCenter.y)) {
			shotAngle = 360+shotAngle;
		}
		
		return shotAngle;
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
				tc.myTanks.remove(this);
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
		//画中间车身
		GradientPaint g1 = new GradientPaint(x-mw/2+fengxi,y-mh/2,colorList.get(2),x-mw/2+fengxi+mw-2*fengxi,y-mh/2+mh,colorList.get(3));
		g2.setPaint(g1);
		RoundRectangle2D rectRound3 = new RoundRectangle2D.Double(x-mw/2+fengxi, y-mh/2, mw-2*fengxi, mh,5,5);
		g2.fill(rectRound3);
		
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
		
		//画上方的两个横条
		g2.setColor(colorList.get(11));
		Rectangle2D rect3 = new Rectangle2D.Double(x-13,y-27,8,2);
		g2.fill(rect3);
		Rectangle2D rect4 = new Rectangle2D.Double(x+5,y-27,8,2);
		g2.fill(rect4);
		
		//***************************画炮盖**********************************************************************
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
		g2.setColor(c);
		
		g2.rotate(Math.toRadians(-angel),x,y);//恢复旋转
		//画血条
		bb.draw(g2);
	
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
	private class BloodBar{
		public void draw(Graphics2D g2) {
			Color c = g2.getColor();
			g2.setColor(new Color(180, 179, 148));
			if(life <=3) {//如果血量小于3了就开始闪
				g2.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
			}
			//画右的圆
			g2.setColor(new Color(180, 179, 148));
			Ellipse2D ellipse1 = new Ellipse2D.Double(x+height/2,y-width/2-46,20,20);
			g2.draw(ellipse1);
			//最外面的方框
			RoundRectangle2D rectRound8 = new RoundRectangle2D.Double(x-height/2-5,y-width/2-43, height+10,12,8,8);
			g2.fill(rectRound8);			
			//绿血条
			if(good && life < 0.3 * fullLife) g2.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
			else if(good || spi) g2.setColor(new Color(254, 1, 1));
			else g2.setColor(new Color(111, 145, 32));
			
			RoundRectangle2D rectRound9 = new RoundRectangle2D.Double(x-height/2-4,y-width/2-42, (height+8) / fullLife * life,5,3,3);
			g2.fill(rectRound9);
			
			//蓝血条
			g2.setColor(new Color(53, 66, 136));
			RoundRectangle2D rectRound10 = new RoundRectangle2D.Double(x-height/2-4,y-width/2-37, (height+8) /fullMissilesNum * missilesNum,5,3,3);
			g2.fill(rectRound10);
			
			//右侧里圆
			g2.setColor(new Color(236, 46, 5));
			Ellipse2D ellipse2 = new Ellipse2D.Double(x+height/2+1,y-width/2-46,19,19);
			Color arcolor1,arcolor2;
			if(good || spi) {
				arcolor1 = new Color(11, 174, 0);
				arcolor2 = new Color(6, 99, 0);
			}else {
				arcolor1 = new Color(236, 46, 5);
				arcolor2 = new Color(245, 106, 76);
			}
			GradientPaint g1 = new GradientPaint(x+height/2+1,y-width/2-43,arcolor1,x+height/2+1,y-width/2-30,arcolor2);
			g2.setPaint(g1);
			g2.fill(ellipse2);
			
			//画字
			Font f=new Font("宋体",1,14);
			g2.setFont(f);
			if(good) {
				g2.setColor(new Color(206, 255, 254));
				g2.drawString(name, x+height/2+2,y-width/2-31);
			}else {
				g2.setColor(new Color(194, 249, 145));
				int num = Integer.parseInt(name);
				if(num < 10) g2.drawString(name, x+height/2+6,y-width/2-31);
				else if(num >=10 && num <100) g2.drawString(name, x+height/2+2,y-width/2-31);
			}
			
			g2.setColor(c);
		}
	}

// ******************************************************碰撞检测
	/*
	 * 检测与血块碰撞
	 */
	public boolean collidesWithTools() {
		Vector position = new Vector(new MyPoint(x,y));
		Vector displacement = position.subTract(lastPosition);
		for(int i = 0;i < tc.tools.size();i++) {
			Tools t = tc.tools.get(i);
			MininumTranslationVector mtv = collidesWith(t,displacement);
			if(live && t.live && mtv.overlap > 0) {
				if(t.name == "blood") {//判断工具类型为血块
					this.life =fullLife;
					t.live = false;
					return true;
				}
			}
		}
		return false;
	}
	
	/*
	 * 碰撞检测
	 */
	public void collidesWithTrees(ArrayList<Tree> trees) {
		Vector position = new Vector(new MyPoint(x,y));
		Vector displacement = position.subTract(lastPosition);
		
		for(int i = 0 ;i<trees.size();i ++) {
			Tree tree = trees.get(i);
			MininumTranslationVector mtv=collidesWith(tree,displacement);
			if(mtv.axis !=null && mtv.overlap !=0) {
				selide(mtv,tree);
			}
		}
	}
	/*
	 * 与河碰撞
	 */
	public void collidesWithRivers(ArrayList<River> rivers) {		
		for(int i = 0 ;i<rivers.size();i ++) {
			River river = rivers.get(i);
			MininumTranslationVector mtv=collidesWith(river,speedV);
			if(mtv.axis !=null && mtv.overlap !=0) {
				selide(mtv,river);
			}
		}
	}
	
	/*
	 * 与墙碰撞
	 */
	public void collidesWithWalls(ArrayList<Wall> walls) {
		for(int i = 0 ;i<walls.size();i ++) {
			Wall wall = walls.get(i);
			MininumTranslationVector mtv=collidesWith(wall,speedV);
			if(mtv.axis !=null && mtv.overlap !=0) {
				selide(mtv,wall);
			}
		}
	}
	
	public void collidesWithScreens(ArrayList<MyScreen> screens) {
		for(int i = 0; i < screens.size(); i++) {
			MyPolygon screen = screens.get(i);
			MininumTranslationVector mtv = collidesWith(screen,speedV);			
			if(mtv.axis!=null && mtv.overlap!=0) {					
				selide(mtv,screen);
			}

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
					selide(mtv,t);
				}
			}
		}
	}	
	/*
	 * 滑动方法
	 */
	public void selide(MininumTranslationVector mtv,MyShape shape) {
		float dx,dy;
		float theta = 0;
		MyPoint center1 = centroid();
		MyPoint center2 = shape.centroid();	
		
		if(mtv.axis.x == 0) {
			theta = (float)(Math.PI/2);
		}else {
			theta = (float)(Math.atan(mtv.axis.y / mtv.axis.x));
		}
		
		dy = (float)(mtv.overlap * Math.sin(theta));
		dx = (float)(mtv.overlap * Math.cos(theta));
		
		//根据速度方向和中心位置,可以判断坦克和障碍物或者坦克如何碰撞的
		if(speedV.x <0 && center1.x < center2.x || speedV.x >0 && center1.x < center2.x)dx = -Math.round(Math.abs(dx));//如果速度x方向向左,并且运动物体中心x小于被撞物体中心x,那么 dx就是负数
		else if(speedV.x <0 && center1.x > center2.x ||speedV.x >0 && center1.x > center2.x)dx = Math.round(Math.abs(dx));//如果速度x方向向左,并且运动物体中心x大于被撞物体中心x,那么 dx就是正数
		
		if(speedV.y <0 && center1.y < center2.y || speedV.y >0 && center1.y < center2.y)dy = -Math.round(Math.abs(dy));
		else if(speedV.y <0 && center1.y > center2.y || speedV.y >0 && center1.y > center2.y)dy = Math.round(Math.abs(dy));
		
		update(dx,dy);
	}

//****************************************************************************************

//射击------------------------------------------------------
	/*
	 * 射击,用面向对象的思想，当坦克射击时会射出一个子弹
	 */
	public Missile fire(int speed) {
		Missile m =new Missile(getPtPoint().x, getPtPoint().y, "img/wissile",tc,good,spi,angle, speed);//炮筒方向是哪个，子弹方向就是哪个
		if(owner == "p1")tc.p1AllShotNum++;//如果子弹是p1的,就加载到统计中
		else if(owner == "p2") tc.p2AllShotNum++;//如果子弹是p2的,就加载到统计中
		m.owner = owner;
		missilesNum -- ;
		if(good) {
			new Thread(new SoundThread("sound/发炮.wav")).start();//启用新进程
		}
		return m;
	}
	/*
	 * 重载fire方法
	 */
	public Missile fire(int speed,int radius,int angle) {
		Missile m =new Missile(getPtPoint().x, getPtPoint().y, "img/wissile",tc, good,spi,angle, speed);//炮筒方向是哪个，子弹方向就是哪个
		if(owner == "p1")tc.p1AllShotNum++;//如果子弹是p1的,就加载到统计中
		else if(owner == "p2") tc.p2AllShotNum++;//如果子弹是p2的,就加载到统计中
		m.owner = owner;
		if(good) {
			new Thread(new SoundThread("sound/发炮.wav")).start();//启用新进程
		}
		missilesNum -- ;
		return m;
	}

	/*
	 * 超级炮弹
	 */
	public void superFire() {
		int[] dirs = {0,45,90,135,180,225,270,315};
		for(int i=0 ;i<dirs.length ;i++) {
			Missile m = fire(10,100,dirs[i]);
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
					if(missilesNum > 0) superFire();
					break;
				case KeyEvent.VK_G:	
					if(missilesNum > 0)tc.missiles.add(fire(myTankShotSpeend));
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
//				case KeyEvent.VK_Q:	
//					ptL = true;	
//					break;
//				case KeyEvent.VK_E:	
//					ptR = true;	
//					break;
			}
		}else if(p =="P2") {
			switch(key) {
			//如果按了ctrl键，就发射子弹，将坦克产生的子弹对象添加到tc.missiles中
				case KeyEvent.VK_M:	
					if(missilesNum > 0) superFire();
					break;
				case KeyEvent.VK_SPACE:	
					if(missilesNum > 0) tc.missiles.add(fire(myTankShotSpeend));
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
//				case KeyEvent.VK_DELETE:	
//					ptL = true;	
//					break;
//				case KeyEvent.VK_PAGE_DOWN:	
//					ptR = true;	
//					break;	
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
//				case KeyEvent.VK_Q:	
//					ptL = false;	
//					break;
//				case KeyEvent.VK_E:	
//					ptR = false;	
//					break;
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
//				case KeyEvent.VK_DELETE:	
//					ptL = false;	
//					break;
//				case KeyEvent.VK_PAGE_DOWN:	
//					ptR = false;	
//					break;	
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
		MyPoint pt = clockWiseTurn(new MyPoint(x+ph,y), new MyPoint(x,y),angle);
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
