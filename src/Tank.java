import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class Tank {
	private int x , y;
	private int speedX;//x�����ٶ�
	private int speedY;//y�����ٶ�	
	
	public int width = 30;//̹�˿��
	public int height = 30;//̹�˸߶�
	public int missileWidth = 10;//��̹�˵��ܿ��
	public int missileHeight = 10;//��̹�˵��ܸ߶�
	public int missileSpeed = 5;//�ӵ���̹�˿������
	
	private boolean bL = false,bR = false,bU = false,bD = false;//�����ʼ�ĸ����������״̬
	
	TankClient tc;	//���ù��캯�����ж�������
	
	enum Direction {L,R,U,D,LD,DR,RU,UL,STOP};//ö��9����������STOP��ֹͣ״̬
	
	private Direction dir = Direction.STOP;//����Ĭ�Ϸ�����ֹͣ״̬
	private Direction ptDir = Direction.D;

	public Tank(int x, int y, int speed) {
		this.x = x;
		this.y = y;
		this.speedX = speed;
		this.speedY = speed;
	}
	
	//���ù��캯�����ж�������
	public Tank(int x,int y ,int speed,TankClient tc) {
		this(x,y, speed);
		this.tc = tc;
	}
	//̹����״
	public void draw(Graphics g) {
		//��̹��
		Color c = g.getColor();//�õ�ǰ��ɫ
		g.setColor(Color.RED);//����̹�˵���ɫ�Ǻ�ɫ
		g.fillOval(x,y,this.width,this.height);
		g.setColor(c);//�ָ�ǰ��ɫ
		
		//����Ͳ
		switch(this.ptDir) {
			case L :
				g.drawLine(x + this.width/2, y + this.height/2, x, y + this.height/2);
				break;
			case R :
				g.drawLine(x + this.width/2, y + this.height/2, x + this.width, y + this.height/2);
				break;
			case U :
				g.drawLine(x + this.width/2, y + this.height/2, x + this.width/2, y);
				break;
			case D :
				g.drawLine(x + this.width/2, y + this.height/2, x + this.width/2, y + this.height);
				break;
			case LD :
				g.drawLine(x + this.width/2, y + this.height/2, x, y + this.height);
				break;
			case DR :
				g.drawLine(x + this.width/2, y + this.height/2, x + this.width, y + this.height);
				break;
			case RU :
				g.drawLine(x + this.width/2, y + this.height/2, x + this.width, y);
				break;
			case UL :
				g.drawLine(x + this.width/2, y + this.height/2, x, y);
				break;
		default:
			break;
		}		
		move();
	}
	
	//������ʵ��������̹�˷���״̬
	public void setKeyStatus(KeyEvent e,boolean b) {


	}
	
	//��������״̬
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
			//�������ctrl�����ͷ����ӵ�����̹�˲������ӵ�������ӵ�tc.missiles��
//			case KeyEvent.VK_CONTROL:		
//			case KeyEvent.VK_SPACE:	
//				tc.missiles.add(fire());
//				break;
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_J:
				bL = true;
				break;
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_L:	
				bR = true;
				break;
			case KeyEvent.VK_UP:
			case KeyEvent.VK_I:		
				bU = true;
				break;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_K:	
				bD = true;
				break;
		}
		locateDirection();
	}
	
	//�����ͷ�״̬
	public void keyRelease(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
		//����ɿ���ctrl�����ͷ����ӵ�����̹�˲������ӵ�������ӵ�tc.missiles��
		case KeyEvent.VK_CONTROL:		
		case KeyEvent.VK_SPACE:	
			tc.missiles.add(fire());
			break;
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_J:
			bL = false;
			break;
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_L:	
			bR = false;
			break;
		case KeyEvent.VK_UP:
		case KeyEvent.VK_I:		
			bU = false;
			break;
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_K:	
			bD = false;
			break;
		}
		locateDirection();
	}		
	
	//̹���ƶ�����
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
		//�ƶ�ʱ��ʱ������Ͳ����
		if(this.dir != Direction.STOP) {
			this.ptDir = this.dir;
		}
	}
	
	//ȷ��̹�˷���dir
	private void locateDirection() {
		if(bL && !bU && !bR && !bD) dir =Direction.L;
		else if(!bL && !bU && bR && !bD) dir =Direction.R;
		else if(!bL && bU && !bR && !bD) dir =Direction.U;
		else if(!bL && !bU && !bR && bD) dir =Direction.D;		
		else if(bL && !bU && !bR && bD) dir =Direction.LD;
		else if(!bL && !bU && bR && bD) dir =Direction.DR;
		else if(!bL && bU && bR && !bD) dir =Direction.RU;
		else if(bL && bU && !bR && !bD) dir =Direction.UL;
		else if(!bL && !bU && !bR && !bD) dir =Direction.STOP;
	}

	//���,����������˼�룬��̹�����ʱ�����һ���ӵ�
	public Missile fire() {
		int x = this.x + this.width/2 - this.missileWidth/2;
		int y = this.y + this.height/2 - this.missileHeight/2;
		Missile m =new Missile(x, y, this.ptDir, this.speedX+this.missileSpeed);//��Ͳ�������ĸ����ӵ���������ĸ�
		return m;
	}
}
