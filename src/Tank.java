import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class Tank {
	private int x , y;
	private int speedX;//x�����ٶ�
	private int speedY;//y�����ٶ�	
	
	private int tw = 11;//�Ĵ����
	private int th = 80;//�Ĵ��߶�
	private int mw = 30;//�жϿ��
	private int mh = 60;//�м�߶�
	private int width = tw*2 +mw+2*this.fengxi;//̹�˿��
	private int height = this.th;//̹�˸߶�
	
	private int cx ;
	private int cy ;
	
	public int missileWidth = 10;//��̹�˵��ܿ��
	public int missileHeight = 10;//��̹�˵��ܸ߶�
	public int missileSpeed = 5;//�ӵ���̹�˿������
	private int lvdaiPosition = 5;//�Ĵ�λ��
	private int lvdaiSpace = 6;//�Ĵ����Ƽ�϶
	private int fengxi = 1;//�����϶
	
	
	
	
	private boolean bL = false,bR = false,bU = false,bD = false;//�����ʼ�ĸ����������״̬
	
	TankClient tc;	//���ù��캯�����ж�������
	
	enum Direction {L,R,U,D,LD,DR,RU,UL,STOP};//ö��9����������STOP��ֹͣ״̬
	
	private Direction dir = Direction.STOP;//����Ĭ�Ϸ�����ֹͣ״̬
	private Direction ptDir = Direction.D;//������Ͳ����

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
	
	public void tankModal(Graphics g,int angel) {

		this.cx = x + tw+mw/2;//����x����
		this.cy = y + th/2;//����y����

		int pw = 2;//��Ͳ���
		int ph = th/2;

		Graphics2D g2 = (Graphics2D) g;
		g2.rotate(Math.toRadians(angel),cx,cy);
		g2.setColor(new Color(178, 178, 0));
			
		//�������Ĵ�
		g2.fillRect(cx-tw-mw/2, cy-th/2, tw, th);
		g2.fillRect(cx+mw/2, cy-th/2, tw, th);

		//���м䳵��
		g2.setColor(new Color(178, 178, 0));
		g2.fillRect(cx-mw/2+fengxi, cy-mh/2, mw-2*fengxi, mh);
		
		//����Ͳ
		g2.setColor(new Color(255, 255, 0));//������Ͳ��ɫ
		g2.fillRect(cx-pw/2, cy-ph, pw, ph);
		g2.fillOval(cx-mw/2+fengxi,cy-mw/2,mw-2,mw);
		 
//		g2.drawRect(cx-tw-mw/2, cy-th/2, tw, th);
//		g2.drawRect(cx+mw/2, cy-th/2, tw, th);
		
		//���Ĵ�����
		for(int i = 0 ;this.lvdaiPosition+i*lvdaiSpace < th;i++) {
			//������(��ת������Ϊ���Գ�,��Ϊ������֧��double����,ֻ����int����,��������ȡ������
			if(ptDir == Direction.D || ptDir == Direction.L) {
				g2.drawLine(cx-tw-mw/2+fengxi, cy-th/2+i*lvdaiSpace +this.lvdaiPosition, cx-mw/2, cy-th/2+i*lvdaiSpace +this.lvdaiPosition);
				g2.drawLine(cx+mw/2+fengxi, cy-th/2+i*lvdaiSpace +this.lvdaiPosition, cx+mw/2+tw, cy-th/2+i*lvdaiSpace +this.lvdaiPosition);
			}else if(ptDir == Direction.RU || ptDir ==Direction.UL || ptDir ==Direction.LD || ptDir ==Direction.DR) {
				g2.drawLine(cx-tw-mw/2+2*fengxi, cy-th/2+i*lvdaiSpace +this.lvdaiPosition, cx-mw/2-fengxi, cy-th/2+i*lvdaiSpace +this.lvdaiPosition);
				g2.drawLine(cx+mw/2+2*fengxi, cy-th/2+i*lvdaiSpace +this.lvdaiPosition, cx+mw/2+tw-fengxi, cy-th/2+i*lvdaiSpace +this.lvdaiPosition);
			}else {
				g2.drawLine(cx-tw-mw/2, cy-th/2+i*lvdaiSpace +this.lvdaiPosition, cx-mw/2-fengxi, cy-th/2+i*lvdaiSpace +this.lvdaiPosition);
				g2.drawLine(cx+mw/2, cy-th/2+i*lvdaiSpace +this.lvdaiPosition, cx+mw/2+tw-fengxi, cy-th/2+i*lvdaiSpace +this.lvdaiPosition);
			}
		}

	}
	//��̹��
	public void draw(Graphics g) {
		this.cx = this.x+ tw+mw/2;;//����x����
		this.cy = this.y;//����y����
		switch(this.ptDir) {
			case L :
				tankModal(g,-90);
				break;
			case R :
				tankModal(g,90);
				break;
			case U :
				tankModal(g,0);
				break;
			case D :
				tankModal(g,180);
				break;
			case LD :
				tankModal(g,-135);
				break;
			case DR :
				tankModal(g,135);
				break;
			case RU :
				tankModal(g,45);
				break;
			case UL :
				tankModal(g,-45);
				break;
			case STOP:
				tankModal(g,0);
				break;
		}		

		move();
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
		
		//�ƶ�ʱ��ʱ������Ͳ����
		if(this.dir != Direction.STOP) {
			//�����Ĵ�״̬
			this.lvdaiPosition -=2;
			if(this.lvdaiPosition <0)
				this.lvdaiPosition =6;
			//�����ӵ�����
			this.ptDir = this.dir;
		}
		
		//̹�˱߽��ж�
		if(x < 20) x = 20;
		if(y < 30) y = 30;
		if(x + width > TankClient.GAME_WIDTH-20) x = TankClient.GAME_WIDTH - width-20;
		if(y + height > TankClient.GAME_HEIGHT) y = TankClient.GAME_HEIGHT - height;
	}
	
	//ȷ��̹�˷���dir
	private void locateDirection() {		
		if(bL && !bU && !bR && !bD) {
			dir =Direction.L;
		}
		else if(!bL && !bU && bR && !bD) {
			dir =Direction.R;
		}
		else if(!bL && bU && !bR && !bD) {
			dir =Direction.U;
		}
		else if(!bL && !bU && !bR && bD) {
			dir =Direction.D;	
		}
		else if(bL && !bU && !bR && bD) {
			dir =Direction.LD;
		}
		else if(!bL && !bU && bR && bD) {
			dir =Direction.DR;
		}
		else if(!bL && bU && bR && !bD) {
			dir =Direction.RU;
		}
		else if(bL && bU && !bR && !bD) {
			dir =Direction.UL;
		}
		else if(!bL && !bU && !bR && !bD) {
			dir =Direction.STOP;
		}
	}

	//���,����������˼�룬��̹�����ʱ�����һ���ӵ�
	public Missile fire() {
		Missile m =new Missile(cx, cy, this.ptDir, this.speedX+this.missileSpeed,this.tc);//��Ͳ�������ĸ����ӵ���������ĸ�
		return m;
	}
}
