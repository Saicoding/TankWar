import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

public class Tank {
	private int x , y;
	private int lastX,lastY,lastCX,lastCY;//��һ�ε��������������
	private int life = 0;//̹������ֵ
	private int fullLife = 10;//̹���������ֵ
	
	private BloodBar bb = new BloodBar();
	
	private Color[] colors = new Color[2];//����̹����ɫ
	private String name;//̹������
	
	public void setName(String name) {
		this.name = name;
	}
	
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
	
	private int myTankShotSpeend =15;//�ҵ�̹������ٶ�
	private int myMissileSize = 40;//�ҵ�̹���ӵ���С
	
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
		if(good) this.life=10;//������� ������ʼ��10
		else this.life =1;//����ǵ���,������ʼ��1
	}
	
	/*
	 * ���ù��캯�����ж�������
	 */
	public Tank(int x,int y ,Color[] colors,boolean good ,Direction dir, int speed,TankClient tc) {
		this(x,y,colors,good, speed);
		this.tc = tc;
		this.dir = dir;
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
		cx = x + tw+mw/2;
		return cx;
	}
	/*
	 * �õ�̹������y
	 */
	public int getCy() {
		cy = y + th/2;
		return cy;
	}
	
	public boolean isGood() {
		return good;
	}
	
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
		
		//����̹���ϵ�����
		Font f=new Font("����",1,19);
		g2.setFont(f);
		Color c = g2.getColor();
		if(good) {
			g2.setColor(new Color(random.nextInt(150),random.nextInt(150),random.nextInt(150)));
			g2.drawString(this.name, cx-11, cy+8);
		}else {
			g2.setColor(new Color(125,38,205));	
			if(Integer.parseInt(this.name)<=9) {
				g2.drawString(this.name, cx-6, cy+8);
			}
			else if(Integer.parseInt(this.name)>9) {
				g2.drawString(this.name, cx-11, cy+8);
			}
		}		
		g2.setColor(c);	
		if(good) {
			bb.draw(g2);
		}
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
//				tc.myTanks.remove(this);
				return;
			}
		}
		this.cx = this.x+ tw+mw/2;//����x����
		this.cy = this.y+th/2;//����y����
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
		this.cx = x + tw+mw/2;//����x����
		this.cy = y + th/2;//����y����
		this.lastX = x;
		this.lastY = y;
		this.lastCX = cx;
		this.lastCY = cy;
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
	
	/*
	 * ����fire����
	 */
	public Missile fire(int speed,int width,Direction dir) {
		Missile m =new Missile(cx, cy, this.good,dir, speed,width,this.tc);//��Ͳ�������ĸ����ӵ���������ĸ�
		return m;
	}
	/*
	 * �����ڵ�
	 */
	public void superFire() {
		Direction[] dirs =Direction.values();
		for(int i=0 ;i<dirs.length-1 ;i++) {
			Missile m = fire(2,100,dirs[i]);
			tc.missiles.add(m);
		}		
	}

	/*
	 * �õ���ײ��stay�����������ײ״̬,��Ҫ�ƶ��ľ���x
	 */
	public int getMoveX(Wall w) {
		if(w.getCx()<cx) {
			if(ptDir == Direction.L || ptDir == Direction.R) {
				return (w.getCx()+w.getW()/2)-(cx-height/2);				
			}else if(ptDir == Direction.U || ptDir == Direction.D) {
				return (w.getCx()+w.getW()/2)-(cx-width/2);
			}
		}else if(w.getCx()>cx) {
			if(ptDir == Direction.L || ptDir == Direction.R) {
				return (w.getCx()-w.getW()/2)-(cx+height/2);
			}else if(ptDir == Direction.U || ptDir == Direction.D) {
				return (w.getCx()-w.getW()/2)-(cx+width/2);
			}
		}
		return 1;
	}
	/*
	 * �õ���ײ��stay�����������ײ״̬,��Ҫ�ƶ��ľ���y
	 */
	public int getMoveY(Wall w) {
		if(w.getCy()<cy) {
			if(ptDir == Direction.L || ptDir == Direction.R) {
				return (w.getCy()+w.getH()/2)-(cy-width/2);				
			}else if(ptDir == Direction.U || ptDir == Direction.D) {
				return (w.getCy()+w.getH()/2)-(cy-height/2);
			}
		}else if(w.getCy()>cy) {
			if(ptDir == Direction.L || ptDir == Direction.R) {
				return (w.getCy()-w.getH()/2)-(cy+width/2);
			}else if(ptDir == Direction.U || ptDir == Direction.D) {
				return (w.getCy()-w.getH()/2)-(cy+height/2);
			}
		}
		return 1;
	}
	/*
	 * �õ���ײ��stay�����������ײ״̬,��Ҫ�ƶ��ľ���x
	 */
	public int getTankMoveX(Tank t) {
		if(t.getCx()<this.getCx()) {
			if(ptDir == Direction.L || ptDir == Direction.R) {
				return (t.getCx()+t.getWidth()/2)-(this.getCx()-this.height/2);				
			}else if(ptDir == Direction.U || ptDir == Direction.D) {
				return (t.getCx()+t.getWidth()/2)-(this.getCx()-this.width/2);
			}
		}else if(t.getCx()>this.getCx()) {
			if(ptDir == Direction.L || ptDir == Direction.R) {
				return (t.getCx()-t.getWidth()/2)-(this.getCx()+this.height/2);
			}else if(ptDir == Direction.U || ptDir == Direction.D) {
				return (t.getCx()-t.getWidth()/2)-(this.getCx()+this.width/2);
			}
		}
		return 1;
	}
	
	/*
	 * �õ�̹�˻�����ײ��stay�����������ײ״̬,��Ҫ�ƶ��ľ���y
	 */
	public int getTankMoveY(Tank t) {
		if(t.getCy()<cy) {
			if(ptDir == Direction.L || ptDir == Direction.R) {
				return (t.getCy()+t.getHeight()/2)-(this.getCy()-this.width/2);				
			}else if(ptDir == Direction.U || ptDir == Direction.D) {
				return (t.getCy()+t.getHeight()/2)-(this.getCy()-this.height/2);
			}
		}else if(t.getCy()>cy) {
			if(ptDir == Direction.L || ptDir == Direction.R) {
				return (t.getCy()-t.getHeight()/2)-(this.getCy()+this.width/2);
			}else if(ptDir == Direction.U || ptDir == Direction.D) {
				return (t.getCy()-t.getHeight()/2)-(this.getCy()+this.height/2);
			}
		}
		return 1;
	}
	/*
	 * ��̹�˻ص���һ�ε�λ��
	 */
	private void stay() {
		x = lastX;
		y = lastY;
		this.cx = lastCX;//����x����
		this.cy = lastCY;//����y����
	}
	
	/*
	 * �õ����ð���̹�˵�һ�����ζ���(���������ײ)
	 */
	public Rectangle getRect() {
		//ʱ�̱���cx��cy�ĸ���,��Ϊcx��cy��������x,y�ı仯���仯
		cx = this.x+ tw+mw/2;//����x����
		cy = this.y+th/2;//����y����
		if(this.ptDir == Direction.U || this.ptDir == Direction.D) {
			return new Rectangle(cx-width/2,cy-height/2,width,height);
		}else if(this.ptDir == Direction.L || this.ptDir == Direction.R){
			return new Rectangle(cx-height/2,cy-width/2,height,width);
		}
		return new Rectangle(cx-height/2,cy-width/2,height,width);	
	}
	
	/*
	 * �ж���ǽ��ײ
	 */
	public boolean collidesWithWall(Wall w) {
		//�ж��������ײ״̬,�ͻָ�����һ��״̬,�����ʱ������ײ״̬,�͸��ݷ����������ƶ�
		if(this.live && this.getRect().intersects(w.getRect())) {
			this.stay();	
			while(this.getRect().intersects(w.getRect())) {
				System.out.println("ok1");	
				if(Math.abs(getMoveX(w))>Math.abs(getMoveY(w))) {
					if(getMoveY(w)>0)y = y+1;
					else if(getMoveY(w)<0) y =y-1;

				}else {
					if(getMoveX(w)>0)x = x+1;
					else if(getMoveX(w)<0) x =x-1;				
				}
			}
			return true;
		}
		return false;
	}
	/*
	 * �жϵ���̹����ײ
	 */
	private boolean collidesWithTank(Tank t) {
		//�ж��������ײ״̬,�ͻָ�����һ��״̬	
		if(this.live && this.getRect().intersects(t.getRect())) {
			this.stay();	
//			while(this.getRect().intersects(t.getRect())) {
//				System.out.println("ok1");	
//				if(Math.abs(getTankMoveX(t))>Math.abs(getTankMoveY(t))) {
//					if(getTankMoveY(t)>0)y = y+1;
//					else if(getTankMoveY(t)<0) y =y-1;
//
//				}else {
//					if(getTankMoveX(t)>0)x = x+1;
//					else if(getTankMoveX(t)<0) x =x-1;			
//				}
//			}
			return true;
		}
		return false;
	}
	/*
	 * �ж�̹��֮����ײ
	 */
	public void collidesWithTanks(ArrayList<Tank> tanks) {
		//�ж��������ײ״̬,�ͻָ�����һ��״̬
		for(int i = 0; i < tanks.size(); i++) {
			Tank t = tanks.get(i);
			if(this != t) {
				collidesWithTank(t);
			}	
		}
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
	/*
	 * ��Ѫ��
	 */
	public boolean eat(Blood b) {
		if(this.live && b.isLive()&& this.getRect().intersects(b.getRect())) {
			this.life =fullLife;
			b.setLive(false);
			return true;
		}
		return false;
	}
}
