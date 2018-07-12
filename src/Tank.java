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
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Random;


public class Tank extends MyPolygon{
	private int lastAngle=0;//��һ�ε��������������
	private int life = 0;//̹������ֵ
	public int angle = 0;//̹�˽Ƕ�
	
	private int fullLife = 10;//̹���������ֵ
	
	private BloodBar bb = new BloodBar();
	
	private ArrayList<Color> colorList = null;//����̹����ɫ
	private String name;//̹������	
	
	private boolean live = true;
	private int speed;//�ٶ�
	public Vector speedV = new Vector();
	
	private int tw = 11;//�Ĵ����
	private int th = 80;//�Ĵ��߶�
	private int mw = 30;//�жϿ��
	private int mh = 63;//�м�߶�
	private int pw = 4;//��Ͳ���
	private int ph = 50;//��Ͳ���
	
	private int width = tw*2 +mw+2*this.fengxi;//̹�˿��
	private int height = this.th;//̹�˸߶�
	
	private int cx ;//̹������x
	private int cy ;//̹������y
	
	private static Random random =new Random();//�����������
	
	public int missileWidth = 10;//��̹�˵��ܿ��
	public int missileHeight = 10;//��̹�˵��ܸ߶�
	public int missileSpeed = 5;//�ӵ���̹�˿������

	private int lvdaiPosition = 5;//�Ĵ�λ��
	private int lvdaiSpace = 6;//�Ĵ����Ƽ�϶
	private int fengxi = 1;//�����϶
	
	private int myTankShotSpeend =15;//�ҵ�̹������ٶ�
	private int myMissileSize = 40;//�ҵ�̹���ӵ���С
	
	private boolean good =false;//�û�̹��
	private boolean bL = false,bR = false,bF = false,bB = false;//�����ʼ�ĸ����������״̬
	private boolean stop = false;//̹���Ƿ�ͣ��
	
	//�Ʋ���
	private int moveStep = 40;//̹�˼Ʋ���
	private int fireStep = 40;//����Ʋ���
	private int randomTurnAngle =0;//�����ת��Ƕ�
	private boolean turning = false;//�Ƿ���ת��
	
	
	
	TankClient tc;	//���ù��캯�����ж�������	
	

	/*
	 * ���캯��
	 */
	
	
	public Tank(int cx, int cy, ArrayList<Color> colorList,boolean good,int speed) {//�û�̹����good����
		this.cx = cx;
		this.cy = cy;
		this.colorList = colorList;
		this.good = good;
		this.speed = speed;
		
		if(good) {
			this.life=10;//������� ������ʼ��10
			this.stop = true;//�Ѿ���ʼֹͣ
		}
		else {
			this.life =1;//����ǵ���,������ʼ��1
			this.stop =false;//���˳�ʼ��ֹͣ
			this.bF = true;//����ǵ���,̹�˳�ʼ��ǰ
		}

	}
	
	/*
	 * ���ù��캯�����ж�������
	 */
	public Tank(int cx,int cy ,ArrayList<Color> colorList,boolean good ,int speed,int angle, TankClient tc) {
		this(cx,cy,colorList,good, speed);
		this.tc = tc;
		this.angle = angle;
		this.speedV.x = speed;//��ʼ�����ķ����̹�˵ĳ�ʼ����һ��
		this.speedV.y = 0;
		this.initPoints();
		this.initSpeedV();//��ʼ���ٶ�����
	}
	public void initSpeedV() {
	    float l = (float) ((angle * Math.PI) / 180);        
	    float cosv = (float) Math.cos(l);  
	    float sinv = (float) Math.sin(l);  
		speedV.x = cosv*speed;
		speedV.y = sinv*speed;
	}
	public void initPoints() {
		this.points.add(clockWiseTurn(new Point(cx-height/2,cy-width/2),new Point(cx,cy)));
		this.points.add(clockWiseTurn(new Point(cx+height/2,cy-width/2),new Point(cx,cy)));
		this.points.add(clockWiseTurn(new Point(cx+height/2,cy+width/2),new Point(cx,cy)));
		this.points.add(clockWiseTurn(new Point(cx-height/2,cy+width/2),new Point(cx,cy)));
	}
	/*
	 * ���ݲ�̹ͬ������̹�˵��ƶ���ʽ
	 */
	private void initMove() {
		if(good) {
			
		}else {
			if(moveStep <=0) {//���ǰ������û����,�����һ���Ƕ�,��ʼת��
				randomTurnAngle = random.nextInt(360)-180;//���һ���Ƕ�
				moveStep =random.nextInt(50)+20;
				turning = true;
			}
		}
	}
	
	/*
	 * ̹��ת��
	 */
	private void turn() {	
		if(good) {//�Ѿ�
			if(bL) angle--;
			if(bR) angle++;
		}else {//�о�
			if(!turning)return;//�������ת��״̬��return
			if(randomTurnAngle >0) {//���randomAngle������,˵��������ת
				angle++;
				randomTurnAngle--;
			}else if(randomTurnAngle <0) {//���randomAngle�Ǹ�,˵��������ת
				angle--;
				randomTurnAngle++;
			}else if(randomTurnAngle ==0) {
				turning =false;//���randomAngle����,˵��ת�����
				moveStep = random.nextInt(200)+50;
			}
		}
		if(angle!=lastAngle){
			changePointTurned();//ÿ�θı�ǶȺ󶼸�ָ��̹����ײģ�͵ĵ�	
		}
		
	}
	/*
	 * ��ǰ�ƶ�
	 */
	public void moveForword() {
		if(turning) return;//turning�ǵ���ת������,�Լ�̹��turning��ʼֵ��false,�����Ѿ�������Զ��ִ��
		update(speedV.x,speedV.y);
		if(!good) {
			moveStep--;//�о�ÿ�ƶ�һ��,�Ʋ���һ
			fireStep--;//�о�ÿ�ƶ�һ��,����Ʋ���һ
		}
		
	}
	/*
	 * ����ƶ�
	 */
	public void moveBack() {
		if(turning) return;
		update(-speedV.x,-speedV.y);
	}
	public void enemyFire() {
		if(good)return;
		if(fireStep <=0) {
			tc.missiles.add(fire(2,5));
			fireStep = random.nextInt(50)+10;
		}
	}
	/*
	 * �õ�ĳ���������Ƶ���ǰǰ�ǶȺ�ĵ�
	 */	
	public Point clockWiseTurn(Point p,Point cp) {
	    // calc arc   
		float l = (float) ((angle * Math.PI) / 180);  
	      
	    //sin/cos value  
		float cosv = (float)Math.cos(l);  
		float sinv = (float) Math.sin(l);  
	  
	    // calc new Point  
		float newX = (p.x - cp.x) * cosv - (p.y - cp.y) * sinv + cp.x;  
		float newY = (p.x - cp.x) * sinv + (p.y - cp.y) * cosv + cp.y;  
	    return new Point((int) newX, (int) newY);  
	}

	/*
	 * 
	 */
	public void changePointTurned() {
		ArrayList<Point> mypoints =  new ArrayList<Point>();
		mypoints.add(new Point(cx-height/2,cy-width/2));
		mypoints.add(new Point(cx+height/2,cy-width/2));
		mypoints.add(new Point(cx+height/2,cy+width/2));
		mypoints.add(new Point(cx-height/2,cy+width/2));
		
		for(int i = 0 ; i< mypoints.size();i++) {
			Point p = points.get(i);	
			Point mp = mypoints.get(i);
			p.x = clockWiseTurn(mp,new Point(cx,cy)).x;
			p.y = clockWiseTurn(mp,new Point(cx,cy)).y;
		}	
	}
	
	/*
	 * ̹���ƶ�����
	 */
	public void move() {
		initMove() ;
		turn();//�ȴ���ת��,������˱���ת�������ƶ�
		setTurnedSpeedV();//ÿ��ת��Ҫ�����ٶ�����
		
		if(!stop) {//�������ֹͣ״̬,�������ֿ���,һ����ǰ,һ�����
			if(bF) {
				moveForword();				
			}else {
				moveBack();
			}
			//̹����Ҫ���˶�,�϶��͸����Ĵ�
			this.lvdaiPosition -=2;
			if(this.lvdaiPosition <0)
				this.lvdaiPosition =6;
		}
		enemyFire();

	}


	/*
	 * �������е�
	 */
	private void update(double dx,double dy) {
		cx += dx;
		cy += dy;
		for(int i = 0 ; i< points.size();i++) {
			Point p = points.get(i);
			p.x += dx;
			p.y += dy;
		}
	}
	/*
	 * ����̹�˺��˷���
	 */
	public void enemyTankback() {
		cx -= myCos(speedV.x);
		cy -= mySin(speedV.y);
	}

/*************************��̹��****************************************/	
	/*
	 * ��̹��
	 */
	public void draw(Graphics g) {
		if(!live) {//���̹������,�Ͳ���
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
	 * ̹��ģ��
	 */
	public void tankModal(Graphics g,int angel) {
		int pw = 2;//��Ͳ���
		int ph = th/2;

		Graphics2D g2 = (Graphics2D) g;
		Color c = g2.getColor();
		//ʹ�߶θ�ƽ��
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
		Stroke s = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		Stroke s1 = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g2.setStroke(s);
		
		g2.rotate(Math.toRadians(angel),cx,cy);//��cx,cyΪ������ת����
			
		//�������Ĵ�
		RoundRectangle2D rectRound1 = new RoundRectangle2D.Double(cx-tw-mw/2, cy-th/2, tw, th,9,3);
		RoundRectangle2D rectRound2 = new RoundRectangle2D.Double(cx+mw/2, cy-th/2, tw, th,9,3);
		GradientPaint gdp1 = new GradientPaint(cx-tw-mw/2, cy-th/2,colorList.get(0),cx-tw-mw/2,cy-th/2+th,colorList.get(1),true);
		g2.setPaint(gdp1);
		g2.fill(rectRound1 );
		g2.fill(rectRound2);

		//���м䳵��
		GradientPaint g1 = new GradientPaint(cx-mw/2+fengxi,cy-mh/2,colorList.get(2),cx-mw/2+fengxi+mw-2*fengxi,cy-mh/2+mh,colorList.get(3));
		g2.setPaint(g1);
		g2.fillRect(cx-mw/2+fengxi, cy-mh/2, mw-2*fengxi, mh);

		GradientPaint g3 = new GradientPaint(cx,cy-mh/2-20,colorList.get(4),cx,cy+20,colorList.get(5));
		g2.setPaint(g3);
		g2.fill(tankTopDraw());
		
		//���м�ĺ��
		g2.setColor(colorList.get(6));
		Rectangle2D rect1 = new Rectangle2D.Double(cx-2,cy-26,4,2);
		g2.fill(rect1);
		
		//���м��Բ�Ǿ���
		g2.setColor(colorList.get(7));
		RoundRectangle2D rectRound3 = new RoundRectangle2D.Double(cx-13,cy-5,26,10,10,10);		
		g2.fill(rectRound3);
		g2.setColor(colorList.get(8));
		Rectangle2D rect2 = new Rectangle2D.Double(cx-1,cy-5,2,10);
		g2.fill(rect2);
		//��Բ�Ǿ����������Բ
		if(good) {
			colorList.set(9, new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
			g2.setColor(colorList.get(9));
		}else {
			g2.setColor(colorList.get(9));
		}
		Ellipse2D ellipse1 = new Ellipse2D.Double(cx-11,cy-3,6,6);
		g2.fill(ellipse1);
		g2.setColor(colorList.get(10));
		Ellipse2D ellipse2 = new Ellipse2D.Double(cx+5,cy-3,6,6);
		g2.fill(ellipse2);
		//���Ϸ�����������
		g2.setColor(colorList.get(11));
		Rectangle2D rect3 = new Rectangle2D.Double(cx-13,cy-27,8,2);
		g2.fill(rect3);
		Rectangle2D rect4 = new Rectangle2D.Double(cx+5,cy-27,8,2);
		g2.fill(rect4);
		//����������
		g2.setColor(colorList.get(12));
		g2.fillArc(cx-10, cy-17, 8, 8, 0, -180);
		g2.setColor(colorList.get(13));
		g2.fillArc(cx+2, cy-17, 8, 8, 0, -180);
		g2.setColor(colorList.get(14));
		g2.fillArc(cx-5, cy-25, 10, 8, 0, -180);
		//������һ��ľ���
		g2.setColor(colorList.get(15));
		RoundRectangle2D rectRound4 = new RoundRectangle2D.Double(cx-10,cy+10,20,5,3,3);	
		g2.fill(rectRound4);
		g2.setColor(colorList.get(16));
		RoundRectangle2D rectRound5 = new RoundRectangle2D.Double(cx-7,cy+10,14,5,3,3);	
		g2.fill(rectRound5);
		g2.setColor(colorList.get(17));
		RoundRectangle2D rectRound6 = new RoundRectangle2D.Double(cx-6,cy+10,12,5,3,3);	
		g2.fill(rectRound6);
		//�������դ��
		g2.setStroke(s1);
		g2.setColor(colorList.get(18));
		RoundRectangle2D rectRound7 = new RoundRectangle2D.Double(cx-12,cy+20,24,10,3,3);	

		g2.draw(rectRound7);
		Line2D line3 = new Line2D.Double(cx-8,cy+20,cx-8,cy+30);
		Line2D line4 = new Line2D.Double(cx-4,cy+20,cx-4,cy+30);
		Line2D line5 = new Line2D.Double(cx,cy+20,cx,cy+30);
		Line2D line6 = new Line2D.Double(cx+4,cy+20,cx+4,cy+30);
		Line2D line7 = new Line2D.Double(cx+8,cy+20,cx+8,cy+30);
		g2.draw(line3);
		g2.draw(line4);
		g2.draw(line5);
		g2.draw(line6);
		g2.draw(line7);
		g2.setColor(c);
		

		g2.setStroke(s);
		g2.setColor(colorList.get(19));
		//���Ĵ�����
		for(int i = 0 ;this.lvdaiPosition+i*lvdaiSpace < th;i++) {
			Point p1 = new Point(cx-tw-mw/2+fengxi,cy-th/2+i*lvdaiSpace +this.lvdaiPosition);
			Point p2 = new Point(cx-mw/2,cy-th/2+i*lvdaiSpace +this.lvdaiPosition);
			Point p3 = new Point(cx+mw/2+fengxi, cy-th/2+i*lvdaiSpace +this.lvdaiPosition);
			Point p4 = new Point(cx+mw/2+tw, cy-th/2+i*lvdaiSpace +this.lvdaiPosition);
			Line2D line1 = new Line2D.Double(p1.x,p1.y,p2.x,p2.y);
			Line2D line2 = new Line2D.Double(p3.x,p3.y,p4.x,p4.y);
			g2.draw(line1);
			g2.draw(line2);
		}
		g2.rotate(Math.toRadians(-angel),cx,cy);//�ָ���ת
		
		if(good) {
			//bb.draw(g2);
		}		
	}
	/*
	 * ̹�˸�·��
	 */
	private Shape tankTopDraw() {
	    Point p1=new Point(cx-10,cy+10);
	    Point p2=new Point(cx+10,cy+10);
	    
	    Point p3=new Point(cx+20,cy-6);
	    Point p4=new Point(cx+5,cy-21);
	    Point p5=new Point(cx+2,cy-26);
	   
	    Point p6=new Point(cx+pw/2,cy-ph);
	    
	    Point p7=new Point(cx-pw/2,cy-ph);
	    Point p8=new Point(cx-2,cy-26);
	    Point p9=new Point(cx-5,cy-21);
	    Point p10=new Point(cx-20,cy-6);
	    Point p11=new Point(cx-10,cy+10);
	    
	    GeneralPath gp=new GeneralPath();    //shape�����࣬��ʾһ����״
	    gp.append(new Line2D.Double(p1.x,p1.y,p2.x,p2.y),true);   //����״�����һ���ߣ���Line2D
	    gp.lineTo(p3.x,p3.y);   //���һ����,����֮ǰ���߶�����
	    gp.lineTo(p4.x,p4.y);  
	    gp.lineTo(p5.x,p5.y);  
	    gp.lineTo(p6.x,p6.y);  
	    gp.lineTo(p7.x,p7.y);  
	    gp.lineTo(p8.x,p8.y);  
	    gp.lineTo(p9.x,p9.y);  
	    gp.lineTo(p10.x,p10.y);  
	    gp.lineTo(p11.x,p11.y);  
	    gp.closePath();  //�ر���״����
	    
	    return gp;    //���ظ���״
	}
	/*
	 * ̹��Ѫ���ڲ���
	 */
	private class BloodBar{
		public void draw(Graphics2D g2) {
			Color c = g2.getColor();
			g2.setColor(new Color(139,0,0));
			if(life <=3) {//���Ѫ��С��3�˾Ϳ�ʼ��
				g2.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
			}
			g2.drawRect(getCx()-height/2,getCy()-height/2-23, height,20 );
			g2.fillRect(getCx()-height/2,getCy()-height/2-23, height/fullLife*life,20);
			g2.setColor(c);
		}
	}

// ******************************************************��ײ���
	/*
	 * ��ײ���
	 */
	public boolean collidesWithWall(Wall w) {
		//�ж��������ײ״̬,�ͻָ�����һ��״̬,�����ʱ������ײ״̬,�͸��ݷ����������ƶ�
//		if(this.live && this.getRect().intersects(w.getRect())) {
//			this.stay();	
//			return true;
//		}
		return false;
	}

	/*
	 * �ж�̹��֮����ײ
	 */
	public void collidesWithTanks(ArrayList<Tank> tanks) {
		//�ж��������ײ״̬,�ͻָ�����һ��״̬
		for(int i = 1; i < tanks.size(); i++) {
			Tank t = tanks.get(i);
			if(this != t) {
//				System.out.println(collidesWith(t).axis.x);
				separate(collidesWith(t));
			}	
		}
	}	
	
	public void separate(MininumTranslationVector mtv) {
		double dx =0,dy=0;
		double theta=0;
		System.out.println(mtv.axis!=null || mtv.overlap!=0);
//		if(mtv.axis !=null) {
//			
//			if(mtv.axis.x ==0) {
//				theta = Math.PI/2;
//			}else {
//				theta = Math.atan(mtv.axis.y/mtv.axis.x);
//			}
//			dx = (int)(mtv.overlap*Math.cos(theta));
//			dy = (int)(mtv.overlap*Math.sin(theta));
//			System.out.println(dx+"||"+dy);
//		}
//		update(-dx,-dy);
		
	}
//****************************************************************************************

	/*
	 * ��Ѫ��
	 */
	public boolean eat(Blood b) {
//		if(this.live && b.isLive()&& this.getRect().intersects(b.getRect())) {
//			this.life =fullLife;
//			b.setLive(false);
//			return true;
//		}
		return false;
	}
//���------------------------------------------------------
	/*
	 * ���,����������˼�룬��̹�����ʱ�����һ���ӵ�
	 */
	public Missile fire(int speed,int width) {
		Missile m =new Missile(cx, cy, this.good,this.angle, speed,width,this.tc);//��Ͳ�������ĸ����ӵ���������ĸ�
		return m;
	}
	/*
	 * ����fire����
	 */
	public Missile fire(int speed,int width,int angle) {
		Missile m =new Missile(cx, cy, this.good,angle, speed,width,this.tc);//��Ͳ�������ĸ����ӵ���������ĸ�
		return m;
	}

	/*
	 * �����ڵ�
	 */
	public void superFire() {
		int[] dirs = {0,45,90,135,180,225,270,315};
		for(int i=0 ;i<dirs.length ;i++) {
			Missile m = fire(2,100,dirs[i]);
			tc.missiles.add(m);
		}		
	}

//��������------------------------------------------------------------
	/*
	 * ��������״̬
	 */
	public void keyPressed(KeyEvent e,String p) {
		if(!live)return;//������˾�ʧЧ
		int key = e.getKeyCode();
		if(p == "P1") {
			switch(key) {
				//�������ctrl�����ͷ����ӵ�����̹�˲������ӵ�������ӵ�tc.missiles��
				case KeyEvent.VK_F:	
					superFire();
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
			//�������ctrl�����ͷ����ӵ�����̹�˲������ӵ�������ӵ�tc.missiles��
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
	 * �����ͷ�״̬
	 */
	public void keyRelease(KeyEvent e,String p) {
		int key = e.getKeyCode();
		if(p == "P1") {//���1
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
		}else if(p =="P2") {//���2
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
	 * ȷ��̹�˷���dir
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

//��ݷ���-------------------------------------------------------------
	public void setTurnedSpeedV() {		
//	    Point p = clockWiseTurn(new Point(5,0), new Point(0,0));
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
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	/*
	 * �õ�̹������x
	 */
	public int getCx() {
		return cx;
	}
	/*
	 * �õ�̹������y
	 */
	public int getCy() {
		return cy;
	}
	/*
	 * �õ��Ƿ����Ѿ�
	 */
	public boolean isGood() {
		return good;
	}
	/*
	 * �õ�����ֵ
	 */
	public int getLife() {
		return life;
	}
		
	public void setLife(int life) {
		this.life = life;
	}

	/*
	 * ��������
	 */
	public void setLive(boolean live) {
		this.live = live;
	}
	/*
	 * �õ��Ƿ����
	 */
	public boolean isLive() {
		return live;
	}
	/*
	 * ����̹������
	 */
	public void setName(String name) {
		this.name = name;
	}
	public void setStop(boolean stop) {
		this.stop = stop;
	}
	/*
	 * �õ���Ͳ����x
	 */
	public int getPtcx() {
		return (int)(getCx() +  ph * Math.cos(angle * 3.14 / 180));
	}
	/*
	 * �õ���Ͳ����y
	 */
	public int getPtcy() {
		return (int)(getCy() + ph* Math.sin(angle *3.14 /180));
	}

}
