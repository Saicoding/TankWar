import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class Tank {
	private int x , y;
	private int speedX;//x�����ٶ�
	private int speedY;//y�����ٶ�	
	
	private boolean bL = false,bR = false,bU = false,bD = false;//�����ʼ�ĸ����������״̬
	private Direction lastDir = Direction.D;
	
	TankClient tc;	//���ù��캯�����ж�������
	
	enum Direction {L,R,U,D,LD,DR,RU,UL,STOP};
	
	private Direction dir = Direction.STOP;

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
		Color c = g.getColor();//�õ�ǰ��ɫ
		g.setColor(Color.RED);//����̹�˵���ɫ�Ǻ�ɫ
		g.fillOval(x,y,30,30);//��̹��
		g.setColor(c);//�ָ�ǰ��ɫ
		
		move();
	}
	
	//������ʵ��������̹�˷���״̬
	public void setKeyStatus(KeyEvent e,boolean b) {
		int key = e.getKeyCode();
		switch(key) {
		//�������ctrl�����ͷ����ӵ�����̹�˲������ӵ����󴫸��ͻ���tc.m
		case KeyEvent.VK_CONTROL:		
		case KeyEvent.VK_SPACE:	
			tc.m = fire();
			break;
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_J:
			bL = b;
			break;
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_L:	
			bR = b;
			break;
		case KeyEvent.VK_UP:
		case KeyEvent.VK_I:		
			bU = b;
			break;
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_K:	
			bD = b;
			break;
		}

	}
	
	//��������״̬
	public void keyPressed(KeyEvent e) {
		setKeyStatus(e, true);
		locateDirection();
	}
	
	//�����ͷ�״̬
	public void keyRelease(KeyEvent e) {
		setKeyStatus(e, false);
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
		Missile m =new Missile(this.x+5, this.y, this.dir, this.speedX+5);	
		return m;
	}
}
