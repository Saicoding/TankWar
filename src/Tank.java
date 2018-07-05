import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class Tank {
	private int x , y;
	private int speedX;//x方向速度
	private int speedY;//y方向速度	
	
	public int width = 30;//坦克宽度
	public int height = 30;//坦克高度
	public int missileWidth = 10;//该坦克弹管宽度
	public int missileHeight = 10;//该坦克弹管高度
	public int missileSpeed = 5;//子弹比坦克快的数字
	
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
	//坦克形状
	public void draw(Graphics g) {
		//画坦克
		Color c = g.getColor();//得到前景色
		g.setColor(Color.RED);//设置坦克的颜色是红色
		g.fillOval(x,y,this.width,this.height);
		g.setColor(c);//恢复前景色
		
		//画炮筒
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
	
	//根据真实按键设置坦克方向状态
	public void setKeyStatus(KeyEvent e,boolean b) {


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
		//移动时随时调整炮筒方向
		if(this.dir != Direction.STOP) {
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
		int x = this.x + this.width/2 - this.missileWidth/2;
		int y = this.y + this.height/2 - this.missileHeight/2;
		Missile m =new Missile(x, y, this.ptDir, this.speedX+this.missileSpeed);//炮筒方向是哪个，子弹方向就是哪个
		return m;
	}
}
