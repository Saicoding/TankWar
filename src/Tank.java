import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class Tank {
	private int x , y,speed;
	private static final int XSPEED = 5;//x�����ٶ�
	private static final int YSPEED = 5;//y�����ٶ�	
	private boolean bL = false,bR = false,bU = false,bD = false;//�����ʼ�ĸ����������״̬

	public Tank(int x, int y, int speed) {
		this.x = x;
		this.y = y;
		this.speed = speed;
	}
	
	//̹����״
	public void draw(Graphics g) {
		Color c = g.getColor();//�õ�ǰ��ɫ
		g.setColor(Color.RED);//����̹�˵���ɫ�Ǻ�ɫ
		g.fillOval(x,y,30,30);//��̹��
		g.setColor(c);//�ָ�ǰ��ɫ
	}
	
	//������ʵ��������̹�˷���״̬
	public void setKeyStatus(KeyEvent e,boolean b) {
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_LEFT:
			bL = b;
			break;
		case KeyEvent.VK_RIGHT:
			bR = b;
			break;
		case KeyEvent.VK_UP:
			bU = b;
			break;
		case KeyEvent.VK_DOWN:
			bD = b;
			break;
		}
	}
	
	//��������״̬
	public void keyPressed(KeyEvent e) {
		setKeyStatus(e, true);
	}
	
	//�����ͷ�״̬
	public void keyRelease(KeyEvent e) {
		setKeyStatus(e, false);
	}		
	
	//̹���ƶ�����
	public void move() {
		if(bL) 
			x -=Tank.XSPEED;
		if(bR) 
			x +=Tank.XSPEED;
		if(bU) 
			y -=Tank.YSPEED;
		if(bD) 
			y +=Tank.YSPEED;	
	}
}
