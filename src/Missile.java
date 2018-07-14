import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;

/**
 * 
 * @ClassName:     Missile   
 * @Description:   �ӵ���
 * @author:        saiyan
 * @date:          2018��7��5�� ����10:09:27
 */
public class Missile extends MyCircle{
	public int x, y;//�ӵ�λ��(����)
	public int width = 10;//�ӵ����
	public int height = 10;//�ӵ��߶�
	
	public int speed;
	public Vector speedV = new Vector();
	
	private int angle;
	private boolean good;//�ӵ���Ӫ	
	private boolean live = true;//һnew�����϶��ǻ��ŵ�
	public Vector lastPosition = new Vector();
	
	public void setLive(boolean live) {//�����ӵ��Ƿ����
		this.live = live;
	}

	//���������ɫ
	int r = (int)(Math.random()*175);
	int g = (int)(Math.random()*175);
	int b = (int)(Math.random()*175);
	Color color = new Color(r,g,b);//�ӵ���ɫ
	
	private TankClient tc;//����tc����

	
	/*
	 * ���췽��
	 */
	public Missile(int x, int y,boolean good, int angle,int speed,int width) {
		this.x = x;
		this.y = y;
		this.good = good;
		this.speed = speed;
		this.width =width;
		this.angle = angle;
	}
	/*
	 * �����õĹ��췽��
	 */
	public  Missile(int x ,int y,boolean good,int angle ,int speed,int width,TankClient tc) {
		this(x,y,good,angle,speed,width);
		this.tc = tc;
		this.radius = width ;
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
	 * ���ӵ�
	 */
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		//ʹ�߶θ�ƽ��
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
		Stroke s = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g2.setStroke(s);
		//����ӵ�����,�Ƴ�,��return ������
		if(!live) {
			tc.missiles.remove(this);
			return;
		}
		Color c = g2.getColor();
		g2.setColor(this.color);
		g2.fillOval(x-this.width/2, y-this.width/2, this.width, this.width);
		g2.setColor(c);
		
		move();
	}
	
	/*
	 * �õ��ӵ��Ƿ���ŵķ���
	 */
	public boolean isLive() {
		return live;
	}
	/*
	 * �ӵ��ƶ�����
	 */
	public void move() {
		lastPosition.x = x;
		lastPosition.y = y;
		x += speedV.x;
		y += speedV.y;
	}
	
	public void collidesWithScreen() {
		//�жϱ߽�����(���Ե�,�������Ż�)
		if(x < 0 || y < 0 || x > TankClient.GAME_WIDTH || y > TankClient.GAME_HEIGHT) {
			live = false;
		}
	}
	
	public boolean collidesWithTank(Tank t) {
		Vector position = new Vector(new MyPoint(x,y));
		Vector displacement = position.subTract(lastPosition);
		if(this.isLive() && t.isLive() && collidesWith(t,displacement).axis!=null &&  collidesWith(t,displacement).overlap !=0 && this.good !=t.isGood()) {
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
	 * �ж��Ƿ��������̹��
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
	 * ���뷽��
	 */
	/*
	 * ���뷽��
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
	 * ��������
	 */
	public void  bounce(MininumTranslationVector mtv, MyShape shape,int bounceCoefficient) {//bounceCoefficient����ϵ��
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
