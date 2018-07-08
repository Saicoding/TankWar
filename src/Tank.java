import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Tank {
	private int x , y;
	private int lastX,lastY;
	private Color[] colors = new Color[2];//定义坦克颜色
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
	
	private int myTankShotSpeend =15;
	
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
	}
	
	/*
	 * 利用构造函数持有对象引用
	 */
	public Tank(int x,int y ,Color[] colors,boolean good ,Direction dir, int speed,TankClient tc) {
		this(x,y,colors,good, speed);
		this.tc = tc;
		this.dir = dir;
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
				return;
			}
		}
		this.cx = this.x+ tw+mw/2;;//中心x坐标
		this.cy = this.y;//中心y坐标
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
	 * 得到坦克中心x
	 */
	public int getCX() {
		return this.cx;
	}
	/*
	 * 得到坦克中心y
	 */
	public int getCY() {
		return this.cy;
	}
	
	private void stay() {
		x = lastX;
		y = lastY;
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
			//如果按了ctrl键，就发射子弹，将坦克产生的子弹对象添加到tc.missiles中
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
	 * 得到正好包含坦克的一个矩形对象(用来检测碰撞)
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
	 * 得到是否活着
	 */
	public boolean isLive() {
		return live;
	}
	/*
	 * 设置生死
	 */
	public void setLive(boolean live) {
		this.live = live;
	}
	/*
	 * 判断与墙相撞
	 */
	public boolean collidesWithWall(Wall w) {
		System.out.println("ok");
		if(this.live && this.getRect().intersects(w.getRect())) {
			System.out.println((ptDir == Direction.R)+"||"+(cx >w.getCx()));
			if(ptDir == Direction.R && cx >w.getCx()) {
				x+=10;
				System.out.println(x);
			}
			//撞墙后就回到原来的位置
//			this.stay();
			return true;
		}
		return false;
	}
}
