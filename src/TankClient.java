/**   
 * @author:        Saiyan
 * @date:          2018��7��5�� ����5:17:36  
 */   
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Random;

public class TankClient extends Frame{//ͨ���̳�Frame ��������Լ��ĳ�Ա�����ͷ��������������ַ�ʽ

	private static final long serialVersionUID = 1L;
	public static final int GAME_WIDTH = 1400 ;//��Ϸ��Ļ�߶�
	public static final int GAME_HEIGHT = 900 ;//��Ϸ��Ļ���
	public static final int GAME_POSITION_X= 100 ;//��Ϸ��Ļλ��x
	public static final int GAME_POSITION_Y= 100 ;//��Ϸ��Ļλ��y
	public static final Color FRONT_COLOR = new Color(51,51,51);//��Ϸǰ��ɫ	
	private static Random random = new Random();
	
	static boolean keyLeftPressed = false;//����Ĭ�ϰ���״̬
	static boolean keyRightPressed = false;//����Ĭ�ϰ���״̬
	static boolean keyUpPressed = false;//����Ĭ�ϰ���״̬
	static boolean keyDownPressed = false;//����Ĭ�ϰ���״̬
	
	Color[] p1Color = {new Color(255,48,48),new Color(205,38,38)};//P1̹����ɫ
	Color[] p2Color = {new Color(255,255,0),new Color(0,255,0)};//P2̹����ɫ
	Color[][] myTankColors = {p1Color,p2Color};//��ɫ����
	
	Wall w1 = new Wall(300,200,20,150,this),w2 = new Wall(500,100,300,20,this);
	
	ArrayList<Tank> myTanks =new ArrayList<Tank>();//�Լ�̹������
	ArrayList<Tank> enemyTanks = new ArrayList<Tank>();
	ArrayList<Boom> booms =new ArrayList<Boom>();//ը������
	ArrayList<Missile> missiles = new ArrayList<Missile>();//�ӵ�����
	
	
	
	Image offScreenImage = null;//����һ������ͼƬ��һ����չ�֣������������
	
	/*
	 * ������
	 */
	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.launchFream();
	}
	

	@Override
	public void paint(Graphics g) {
		
		//���ӵ�
		for(int i = 0 ;i<missiles.size();i++) {
			Missile m = missiles.get(i);
			m.hitTanks(enemyTanks);//���Դ����
//			m.hitTanks(myTanks);//���˿��Դ��ҵ�̹��
			m.hitWall(w1);
			m.hitWall(w2);
			m.draw(g);
		}
		
		//�����˵�̹��
		for(int i = 0;i<enemyTanks.size();i++) {
			Tank enemyTank =enemyTanks.get(i);
			enemyTank.draw(g);
		}
		
		//��ը��
		if(booms.size()>0) {
			for(int i =0;i<booms.size();i++) {
				Boom b = booms.get(i);
				b.draw(g);
			}
		}
		
		//���Լ���̹��
		for(int i = 0;i<myTanks.size();i++) {
			Tank myTank =myTanks.get(i);
			myTank.collidesWithWall(w1);
			myTank.collidesWithWall(w2);
			myTank.draw(g);
			myTank.drawRect(g);
		}
		//����ֵ
		g.drawString("�ӵ�����:" + missiles.size(), 10, 50);//�ж����ڵ�����Ļ��
		g.drawString("��������:" + enemyTanks.size(), 10, 70);//�ж��ٵ�������Ļ��
		
		//��ǽ
		w1.drawWallRect(g);
		w2.drawWallRect(g);
		w1.draw(g);
		w2.draw(g);
		
	}
	
	@Override
	/*
	 * ��ȡupdate��������ͼƬһ������ʾ������������⣬updateԭ���ķ������Զ�ˢ��
	 */	
	public void update(Graphics g) {
		if(offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH,GAME_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();//���õ�����
		Color c = gOffScreen.getColor();//�õ�ǰ��ɫ
		gOffScreen.setColor(FRONT_COLOR );	//����ǰ��ɫ
		gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);//��仭��
		gOffScreen.setColor(c);//�ָ�������ɫ
		paint(gOffScreen);//��ͼƬ��������ͼƬ
		g.drawImage(offScreenImage, 0, 0, null);//������ͼƬһ��������������
	}

	/*
	 * �������� 
	 */
	public void launchFream() {
		//����Լ�̹��
		for(int i=0;i<2;i++) {
			//��������
			String name = "";
			if(i == 0) {
				name = "ұ";
			}else if(i ==1) {
				name ="��";
			}
			Tank t = new Tank(90*(i+1),50,myTankColors[i],true,Tank.Direction.STOP,7,this);
			t.setName(name);
			myTanks.add(t);
		}		
		//��ӵ���̹��
		for(int i=0;i<10;i++) {
			for(int j = 0;j < 2;j++) {
				Color c1 = new Color(random.nextInt(150),random.nextInt(150),random.nextInt(150));
				Color c2 = new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255));
				Color[] colors = {c1,c2};
				enemyTanks.add(new Tank(50+90*(i+1),50+100*(j),colors,false,Tank.Direction.D,5,this));
			}
		}
		
		 this.setLocation(GAME_POSITION_X,GAME_POSITION_Y);
		 this.setSize(GAME_WIDTH,GAME_HEIGHT);
		 
		 //�����ر��¼�
		 this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			 
		});
		//���������¼�
		 this.addKeyListener(new KeyMonitor());		 
    
		 this.setTitle("̹�˴�ս");		//���ñ��� 	
		 this.setBackground(FRONT_COLOR );//���ñ�����ɫ
		 this.setResizable(false);//���ò����Ըı䴰�ڴ�С
		 this.setVisible(true);//�����Ƿ�ɼ�
		 
		//��������������һ���߳���ˢ�»���
		 new Thread(new PaintThread()).start();
	}
	
	/*
	 * ����һ���߳���ˢ��(�ػ�)����
	 */
	private class PaintThread implements Runnable {
		
		public void run() {
			while(true) {
				repaint();//ֱ�ӷ��ʰ�װ��ĳ�Ա����
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/*
	 * ���̼�����ʵ�ּ��̿���̹��
	 */
	private class KeyMonitor extends KeyAdapter{//����ѡ��̳е�ԭ���ǣ����ѡ��implements ��Ҫʵ������ӿ�������з���
		//���ð���״̬

		@Override
		public void keyPressed(KeyEvent e) {
			for(int i=0;i<myTanks.size();i++) {
				Tank myTank = myTanks.get(i);
				if(i == 0) myTank.keyPressed(e,"P1");	
				if(i == 1) myTank.keyPressed(e,"P2");			
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			for(int i=0;i<myTanks.size();i++) {
				Tank myTank = myTanks.get(i);
				if(i == 0) myTank.keyRelease(e,"P1");	
				if(i == 1) myTank.keyRelease(e,"P2");					
			}		
		}
	}
}
