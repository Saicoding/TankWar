import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Tank {
	private int x , y;
	private int lastX,lastY;
	private Color[] colors = new Color[2];//����̹����ɫ
	private boolean live = true;
	private int speedX;//x�����ٶ�
	private int speedY;//y�����ٶ�	
	
	private int tw = 11;//�Ĵ����
	private int th = 80;//�Ĵ��߶�
	private int mw = 30;//�жϿ��
	private int mh = 60;//�м�߶�
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
	
	private int step = 0;//�Ʋ���,����̹�˵�ת�����
	private int stepShot = 0;//����Ʋ�
	
	private int myTankShotSpeend =15;
	
	private boolean good =false;//�û�̹��
	private boolean bL = false,bR = false,bU = false,bD = false;//�����ʼ�ĸ����������״̬
	
	TankClient tc;	//���ù��캯�����ж�������
	
	enum Direction {L,R,U,D,LD,DR,RU,UL,STOP};//ö��9����������STOP��ֹͣ״̬
	
	private Direction dir = Direction.STOP;//����Ĭ�Ϸ�����ֹͣ״̬
	Direction[] dirs = Direction.values();//��ö������תΪ����
	private Direction ptDir = Direction.D;//������Ͳ����
	

	/*
	 * ���캯��
	 */
	public Tank(int x, int y, Color[] colors,boolean good,int speed) {//�û�̹����good����
		this.x = x;
		this.y = y;
		this.colors = colors;
		this.good = good;
		this.speedX = speed;
		this.speedY = speed;
	}
	
	/*
	 * ���ù��캯�����ж�������
	 */
	public Tank(int x,int y ,Color[] colors,boolean good ,Direction dir, int speed,TankClient tc) {
		this(x,y,colors,good, speed);
		this.tc = tc;
		this.dir = dir;
	}
	
	/*
	 * ̹��ģ��
	 */
	public void tankModal(Graphics g,int angel) {

		this.cx = x + tw+mw/2;//����x����
		this.cy = y + th/2;//����y����

		int pw = 2;//��Ͳ���
		int ph = th/2;

		Graphics2D g2 = (Graphics2D) g;
		g2.rotate(Math.toRadians(angel),cx,cy);//��cx,cyΪ������ת����
		g2.setColor(colors[0]);//������Ͳ��ɫ
			
		//�������Ĵ�
		g2.fillRect(cx-tw-mw/2, cy-th/2, tw, th);
		g2.fillRect(cx+mw/2, cy-th/2, tw, th);

		//���м䳵��
		g2.fillRect(cx-mw/2+fengxi, cy-mh/2, mw-2*fengxi, mh);
		
		//����Ͳ
		g2.setColor(colors[1]);//������Ͳ��ɫ

		g2.fillRect(cx-pw/2, cy-ph, pw, ph);
		g2.fillOval(cx-mw/2+fengxi,cy-mw/2,mw-2,mw);
		
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
		g2.rotate(Math.toRadians(-angel),cx,cy);//�ָ���ת

	}
	/*
	 * ��̹��
	 */
	public void draw(Graphics g) {
		if(!live) {//���̹������,�Ͳ���
			if(!good) {
				tc.enemyTanks.remove(this);
				return;
			}else {
				return;
			}
		}
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
	/*
	 * �õ�̹������x
	 */
	public int getCX() {
		return this.cx;
	}
	/*
	 * �õ�̹������y
	 */
	public int getCY() {
		return this.cy;
	}
	
	private void stay() {
		x = lastX;
		y = lastY;
	}

	/*
	 * ��������״̬
	 */
	public void keyPressed(KeyEvent e,String p) {
		if(!live)return;//������˾�ʧЧ
		int key = e.getKeyCode();
		if(p == "P1") {
			switch(key) {
				//�������ctrl�����ͷ����ӵ�����̹�˲������ӵ�������ӵ�tc.missiles��		
				case KeyEvent.VK_G:	
					tc.missiles.add(fire(myTankShotSpeend,200));
					break;
				case KeyEvent.VK_A:
					bL = true;
					break;
				case KeyEvent.VK_D:	
					bR = true;
					break;
				case KeyEvent.VK_W:		
					bU = true;
					break;
				case KeyEvent.VK_S:	
					bD = true;
					break;
			}
		}else if(p =="P2") {
			switch(key) {
			//�������ctrl�����ͷ����ӵ�����̹�˲������ӵ�������ӵ�tc.missiles��
				case KeyEvent.VK_M:		
				case KeyEvent.VK_SPACE:	
					tc.missiles.add(fire(myTankShotSpeend,200));
					break;
				case KeyEvent.VK_LEFT:
					bL = true;
					break;
				case KeyEvent.VK_RIGHT:
					bR = true;
					break;
				case KeyEvent.VK_UP:
					bU = true;
					break;
				case KeyEvent.VK_DOWN:
					bD = true;
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
					bU = false;
					break;
				case KeyEvent.VK_S:	
					bD = false;
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
					bU = false;
					break;
				case KeyEvent.VK_DOWN:
					bD = false;
					break;
			}
		}			
		locateDirection();

	}		
	
	/*
	 * ̹���ƶ�����
	 */
	public void move() {
		//ÿ�ƶ�һ�ξͼ�¼һ��λ��
		this.lastX = x;
		this.lastY = y;
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
		
		if(!good) {
			step++;
			if(step > (random.nextInt(800)+50)){
				step = 0;
				int rn = random.nextInt(dirs.length);//����һ����length��Χ�ڵ�����
				dir = dirs[rn];
			}
			stepShot++;
			if(stepShot >(random.nextInt(4000)+20)) {
				tc.missiles.add(fire(2,10));
				stepShot=0;
			}
		}
	}
	
	/*
	 * ȷ��̹�˷���dir
	 */
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

	/*
	 * ���,����������˼�룬��̹�����ʱ�����һ���ӵ�
	 */
	public Missile fire(int speed,int width) {
		Missile m =new Missile(cx, cy, this.good,this.ptDir, speed,width,this.tc);//��Ͳ�������ĸ����ӵ���������ĸ�
		return m;
	}
	
	public boolean isGood() {
		return good;
	}
	
	public void drawRect(Graphics g) {
		if(this.ptDir == Direction.U || this.ptDir == Direction.D) {
			g.drawRect(cx-width/2,cy-height/2,width,height);
		}else if(this.ptDir == Direction.L || this.ptDir == Direction.R){
			g.drawRect(cx-height/2,cy-width/2,height,width);
		};
	}

	/*
	 * �õ����ð���̹�˵�һ�����ζ���(���������ײ)
	 */
	public Rectangle getRect() {
		if(this.ptDir == Direction.U || this.ptDir == Direction.D) {
			return new Rectangle(cx-width/2,cy-height/2,width,height);
		}else if(this.ptDir == Direction.L || this.ptDir == Direction.R){
			return new Rectangle(cx-height/2,cy-width/2,height,width);
		}
		return new Rectangle(cx-height/2,cy-width/2,height,width);
		
	}
	
	/*
	 * �õ��Ƿ����
	 */
	public boolean isLive() {
		return live;
	}
	/*
	 * ��������
	 */
	public void setLive(boolean live) {
		this.live = live;
	}
	/*
	 * �ж���ǽ��ײ
	 */
	public boolean collidesWithWall(Wall w) {
		System.out.println("ok");
		if(this.live && this.getRect().intersects(w.getRect())) {
			System.out.println((ptDir == Direction.R)+"||"+(cx >w.getCx()));
			if(ptDir == Direction.R && cx >w.getCx()) {
				x+=10;
				System.out.println(x);
			}
			//ײǽ��ͻص�ԭ����λ��
//			this.stay();
			return true;
		}
		return false;
	}
}
