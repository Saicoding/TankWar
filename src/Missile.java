import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 * 
 * @ClassName:     Missile   
 * @Description:   �ӵ���
 * @author:        saiyan
 * @date:          2018��7��5�� ����10:09:27
 */
public class Missile {
	public int speedX;//x������ٶ�
	public int speedY;//y������ٶ�
	
	public int width = 10;//�ӵ����
	public int height = 10;//�ӵ��߶�
	
	private boolean good;//�ӵ���Ӫ
	
	private boolean live = true;//һnew�����϶��ǻ��ŵ�
	
	public void setLive(boolean live) {//�����ӵ��Ƿ����
		this.live = live;
	}

	int x, y;//�ӵ�λ��(����)
	
	Tank.Direction dir;//�ӵ�����
	
	//���������ɫ
	int r = (int)(Math.random()*175);
	int g = (int)(Math.random()*175);
	int b = (int)(Math.random()*175);
	Color color = new Color(r,g,b);//�ӵ���ɫ
	
	private TankClient tc;//����tc����
	
	/*
	 * ���췽��
	 */
	public Missile(int x, int y,boolean good, Tank.Direction dir,int speed,int width) {
		this.x = x;
		this.y = y;
		this.good = good;
		this.speedX =speed;
		this.speedY =speed;
		this.width =width;
		this.dir = dir;
	}
	/*
	 * �����õĹ��췽��
	 */
	public  Missile(int x ,int y,boolean good, Tank.Direction dir ,int speed,int width,TankClient tc) {
		this(x,y,good,dir,speed,width);
		this.tc = tc;
	}
	
	/*
	 * ���ӵ�
	 */
	public void draw(Graphics g) {
		//����ӵ�����,�Ƴ�,��return ������
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
	 * �õ��ӵ��Ƿ���ŵķ���
	 */
	public boolean isLive() {
		return live;
	}
	/*
	 * �ӵ��ƶ�����
	 */
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
		//�жϱ߽�����(���Ե�,�������Ż�)
		if(x < 0 || y < 0 || x > TankClient.GAME_WIDTH || y > TankClient.GAME_HEIGHT) {
			live = false;
		}
	}
	
	/*
	 * �õ����ð����ӵ���һ�����ζ���(���������ײ)
	 */
	public Rectangle getRect() {
		return new Rectangle(x-width/2,y-width/2,width,width);//����x,y���ӵ�������,����Ҫ���������
	}
	
	/*
	 * �ж��Ƿ�����ҵ�̹��
	 */
	public boolean hitMyTank(Tank t) {
		if(this.live && this.getRect().intersects(t.getRect()) && t.isLive()&& !t.isGood() == this.good) {
			t.setLive(false);
			//this.setLive(false);
			tc.booms.add(new Boom(t.getCX(),t.getCY(),tc));
			return true;
		}
		return false;
	}
	
	/*
	 * �ж��Ƿ��������̹��
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
	 * �ж��Ƿ����ǽ
	 */
	public boolean hitWall(Wall w) {
		if(this.live && this.getRect().intersects(w.getRect())) {
			this.live =false;
			return true;
		}
		return false;
	}
}
