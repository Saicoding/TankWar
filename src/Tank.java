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
	private int lastX,lastY,lastCX,lastCY;//上一次的坐标和中心坐标
	private int life = 0;//坦克生命值
	private int fullLife = 10;//坦克最大生命值
	
	private BloodBar bb = new BloodBar();
	
	private Color[] colors = new Color[2];//定义坦克颜色
	private String name;//坦克名字
	
	public void setName(String name) {
		this.name = name;
	}
	
	private boolean live = true;
	private int speedX;//x方向速度
	private int speedY;//y方向速度	
	
	private int tw = 11;//履带宽度
	private int th = 80;//履带高度
	private int mw = 30;//中断宽度
	private int mh = 60;//中间高度
	private int width = tw*2 +mw+2*this.fengxi;//坦克宽度
	private int height = this.th;//坦克高度
	
	private int cx ;//坦克中心x
	private int cy ;//坦克中心y
	
	private static Random random =new Random();//随机数产生器
	
	public int missileWidth = 10;//该坦克弹管宽度
	public int missileHeight = 10;//该坦克弹管高度
	public int missileSpeed = 5;//子弹比坦克快的数字

	private int lvdaiPosition = 5;//履带位置
	private int lvdaiSpace = 6;//履带条纹间隙
	private int fengxi = 1;//车身缝隙
	
	private int step = 0;//计步数,控制坦克的转向随机
	private int stepShot = 0;//射击计步
	
	private int myTankShotSpeend =15;//我的坦克射击速度
	private int myMissileSize = 40;//我的坦克子弹大小
	
	private boolean good =false;//好坏坦克
	private boolean bL = false,bR = false,bU = false,bD = false;//定义初始四个方向键按下状态
	
	TankClient tc;	//利用构造函数持有对象引用
	
	enum Direction {L,R,U,D,LD,DR,RU,UL,STOP};//枚举9个方向，其中STOP是停止状态
	
	private Direction dir = Direction.STOP;//设置默认方向是停止状态
	Direction[] dirs = Direction.values();//将枚举类型转为数组
	private Direction ptDir = Direction.D;//设置炮筒方向
	
	

	/*
	 * 构造函数
	 */
	public Tank(int x, int y, Color[] colors,boolean good,int speed) {//好坏坦克用good区分
		this.x = x;
		this.y = y;
		this.colors = colors;
		this.good = good;
		this.speedX = speed;
		this.speedY = speed;
		if(good) this.life=10;//如果是友 生命初始是10
		else this.life =1;//如果是敌人,生命初始是1
	}
	
	/*
	 * 利用构造函数持有对象引用
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
	 * 得到坦克中心x
	 */
	public int getCx() {
		cx = x + tw+mw/2;
		return cx;
	}
	/*
	 * 得到坦克中心y
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
		
	/*
	 * 坦克模型
	 */
	public void tankModal(Graphics g,int angel) {

		this.cx = x + tw+mw/2;//中心x坐标
		this.cy = y + th/2;//中心y坐标

		int pw = 2;//炮筒宽度
		int ph = th/2;

		Graphics2D g2 = (Graphics2D) g;
		g2.rotate(Math.toRadians(angel),cx,cy);//以cx,cy为中心旋转画布
		g2.setColor(colors[0]);//设置炮筒颜色
			
		//画两个履带
		g2.fillRect(cx-tw-mw/2, cy-th/2, tw, th);
		g2.fillRect(cx+mw/2, cy-th/2, tw, th);

		//画中间车身
		g2.fillRect(cx-mw/2+fengxi, cy-mh/2, mw-2*fengxi, mh);
		
		//画炮筒
		g2.setColor(colors[1]);//设置炮筒颜色

		g2.fillRect(cx-pw/2, cy-ph, pw, ph);
		g2.fillOval(cx-mw/2+fengxi,cy-mw/2,mw-2,mw);
		
		//画履带格子
		for(int i = 0 ;this.lvdaiPosition+i*lvdaiSpace < th;i++) {
			//做修正(旋转画布因为不对称,因为画布不支持double类型,只能用int类型,产生像素取舍问题
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
		g2.rotate(Math.toRadians(-angel),cx,cy);//恢复旋转
		
		//设置坦克上的数字
		Font f=new Font("宋体",1,19);
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
		this.cx = this.x+ tw+mw/2;//中心x坐标
		this.cy = this.y+th/2;//中心y坐标
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
					bU = false;
					break;
				case KeyEvent.VK_S:	
					bD = false;
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
	 * 坦克移动方法
	 */
	public void move() {
		//每移动一次就记录一下位置
		this.cx = x + tw+mw/2;//中心x坐标
		this.cy = y + th/2;//中心y坐标
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
		
		//移动时随时调整炮筒方向
		if(this.dir != Direction.STOP) {
			//更新履带状态
			this.lvdaiPosition -=2;
			if(this.lvdaiPosition <0)
				this.lvdaiPosition =6;
			//更新子弹方向
			this.ptDir = this.dir;
		}
		
		//坦克边界判断
		if(x < 20) x = 20;
		if(y < 30) y = 30;
		if(x + width > TankClient.GAME_WIDTH-20) x = TankClient.GAME_WIDTH - width-20;
		if(y + height > TankClient.GAME_HEIGHT) y = TankClient.GAME_HEIGHT - height;
		
		if(!good) {
			step++;
			if(step > (random.nextInt(800)+50)){
				step = 0;
				int rn = random.nextInt(dirs.length);//产生一个在length范围内的整数
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
	 * 确定坦克方向dir
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
	 * 射击,用面向对象的思想，当坦克射击时会射出一个子弹
	 */
	public Missile fire(int speed,int width) {
		Missile m =new Missile(cx, cy, this.good,this.ptDir, speed,width,this.tc);//炮筒方向是哪个，子弹方向就是哪个
		return m;
	}
	
	/*
	 * 重载fire方法
	 */
	public Missile fire(int speed,int width,Direction dir) {
		Missile m =new Missile(cx, cy, this.good,dir, speed,width,this.tc);//炮筒方向是哪个，子弹方向就是哪个
		return m;
	}
	/*
	 * 超级炮弹
	 */
	public void superFire() {
		Direction[] dirs =Direction.values();
		for(int i=0 ;i<dirs.length-1 ;i++) {
			Missile m = fire(2,100,dirs[i]);
			tc.missiles.add(m);
		}		
	}

	/*
	 * 得到碰撞后stay后如果还是碰撞状态,需要移动的距离x
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
	 * 得到碰撞后stay后如果还是碰撞状态,需要移动的距离y
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
	 * 得到碰撞后stay后如果还是碰撞状态,需要移动的距离x
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
	 * 得到坦克互相碰撞后stay后如果还是碰撞状态,需要移动的距离y
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
	 * 让坦克回到上一次的位置
	 */
	private void stay() {
		x = lastX;
		y = lastY;
		this.cx = lastCX;//中心x坐标
		this.cy = lastCY;//中心y坐标
	}
	
	/*
	 * 得到正好包含坦克的一个矩形对象(用来检测碰撞)
	 */
	public Rectangle getRect() {
		//时刻保持cx和cy的更新,因为cx和cy不会随着x,y的变化而变化
		cx = this.x+ tw+mw/2;//中心x坐标
		cy = this.y+th/2;//中心y坐标
		if(this.ptDir == Direction.U || this.ptDir == Direction.D) {
			return new Rectangle(cx-width/2,cy-height/2,width,height);
		}else if(this.ptDir == Direction.L || this.ptDir == Direction.R){
			return new Rectangle(cx-height/2,cy-width/2,height,width);
		}
		return new Rectangle(cx-height/2,cy-width/2,height,width);	
	}
	
	/*
	 * 判断与墙相撞
	 */
	public boolean collidesWithWall(Wall w) {
		//判断如果是碰撞状态,就恢复到上一次状态,如果此时还是碰撞状态,就根据方向来进行移动
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
	 * 判断单个坦克相撞
	 */
	private boolean collidesWithTank(Tank t) {
		//判断如果是碰撞状态,就恢复到上一次状态	
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
	 * 判断坦克之间相撞
	 */
	public void collidesWithTanks(ArrayList<Tank> tanks) {
		//判断如果是碰撞状态,就恢复到上一次状态
		for(int i = 0; i < tanks.size(); i++) {
			Tank t = tanks.get(i);
			if(this != t) {
				collidesWithTank(t);
			}	
		}
	}	
	/*
	 * 坦克血条内部类
	 */
	private class BloodBar{
		public void draw(Graphics2D g2) {
			Color c = g2.getColor();
			g2.setColor(new Color(139,0,0));
			if(life <=3) {//如果血量小于3了就开始闪
				g2.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
			}
			g2.drawRect(getCx()-height/2,getCy()-height/2-23, height,20 );
			g2.fillRect(getCx()-height/2,getCy()-height/2-23, height/fullLife*life,20);
			g2.setColor(c);
		}
	}
	/*
	 * 吃血块
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
