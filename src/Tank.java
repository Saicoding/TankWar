import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class Tank {
	private int x , y;
	private int speedX;//x方向速度
	private int speedY;//y方向速度	
	
	private int tw = 10;//履带宽度
	private int th = 80;//履带高度
	private int mw = 30;//中断宽度
	private int mh = 60;//中间高度
	private int cx ;
	private int cy ;
	
	public int missileWidth = 10;//该坦克弹管宽度
	public int missileHeight = 10;//该坦克弹管高度
	public int missileSpeed = 5;//子弹比坦克快的数字
	private int lvdaiPosition = 5;//履带位置
	
	
	
	private int px;//子弹出膛中心x
	private int py;//子弹出膛中心y
	
	private boolean bL = false,bR = false,bU = false,bD = false;//定义初始四个方向键按下状态
	
	TankClient tc;	//利用构造函数持有对象引用
	
	enum Direction {L,R,U,D,LD,DR,RU,UL,STOP};//枚举9个方向，其中STOP是停止状态
	
	private Direction dir = Direction.STOP;//设置默认方向是停止状态
	private Direction ptDir = Direction.D;

	public Tank(int x, int y, int speed) {
		this.x = x;
		this.y = y;
		this.speedX = speed;
		this.speedY = speed;
	}
	
	//利用构造函数持有对象引用
	public Tank(int x,int y ,int speed,TankClient tc) {
		this(x,y, speed);
		this.tc = tc;
	}
	
	public void tankModal(Graphics g,int angel) {

		this.cx = x + tw+mw/2;//中心x坐标
		this.cy = y + th/2;//中心y坐标
		int pw = 2;//炮筒宽度
		int ph = th/2;
		int fx = 1;//缝隙

		Graphics2D g2 = (Graphics2D) g;
		g2.rotate(Math.toRadians(angel),cx,cy);

		//画坦克
//		Color c = g2.getColor();//得到前景色
		g2.setColor(new Color(178, 178, 0));
//		g.fillOval(x,y,this.width,this.height);
//		g.setColor(c);//恢复前景色
				
		//画两个履带
		g2.fillRect(cx-tw-mw/2, cy-th/2, tw, th);
		g2.fillRect(cx+mw/2, cy-th/2, tw, th);

		//画中间车身
		g2.setColor(new Color(178, 178, 0));
		g2.fillRect(cx-mw/2+fx+1, cy-mh/2, mw-fx-fx-fx, mh);
		
		//画炮筒
		g2.setColor(new Color(255, 255, 0));//设置炮筒颜色
		g2.fillRect(cx-pw/2, cy-ph, pw, ph);
		g2.fillOval(cx-mw/2,cy-mw/2,mw,mw);
		 
		g2.drawRect(cx-tw-mw/2, cy-th/2, tw, th);
		g2.drawRect(cx+mw/2, cy-th/2, tw, th);
		
		//画履带格子
		for(int i = 0 ;this.lvdaiPosition+i*5 < th;i++) {
			g2.drawLine(cx-tw-mw/2, cy-th/2+i*5+this.lvdaiPosition, cx-mw/2, cy-th/2+i*5+this.lvdaiPosition);
			g2.drawLine(cx+tw+mw/2, cy-th/2+i*5+this.lvdaiPosition, cx+mw/2, cy-th/2+i*5+this.lvdaiPosition);
		}

	}
	//画坦克
	public void draw(Graphics g) {
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

	//按键按下状态
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
			//如果按了ctrl键，就发射子弹，将坦克产生的子弹对象添加到tc.missiles中
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
	
	//按键释放状态
	public void keyRelease(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
		//如果松开了ctrl键，就发射子弹，将坦克产生的子弹对象添加到tc.missiles中
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
	
	//坦克移动方法
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
		
		//移动时随时调整炮筒方向
		if(this.dir != Direction.STOP) {
			//更新履带状态
			this.lvdaiPosition -=1;
			if(this.lvdaiPosition ==0)
				this.lvdaiPosition =5;
			//更新子弹方向
			this.ptDir = this.dir;
		}
	}
	
	//确定坦克方向dir
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

	//射击,用面向对象的思想，当坦克射击时会射出一个子弹
	public Missile fire() {
		Missile m =new Missile(cx, cy, this.ptDir, this.speedX+this.missileSpeed);//炮筒方向是哪个，子弹方向就是哪个
		return m;
	}
}
