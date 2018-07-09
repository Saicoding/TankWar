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


/**
 * 
 * TankClient
 * ����̹����Ϸ�������� 
 * saiyan
 * 2018��7��8�� ����11:24:44
 */
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
	
	

	
	Wall w1 = new Wall(300,200,20,150,this),w2 = new Wall(500,100,300,20,this);
	
	Blood bb = new Blood();
	
	public int totalEnemyTankNum = 11;
	public int time = 0;
	private int enemyTankName = 1;
	
	ArrayList<Tank> myTanks =new ArrayList<Tank>();//�Լ�̹������
	ArrayList<Tank> enemyTanks = new ArrayList<Tank>();//����̹������
	
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
		
		//ÿ�����һ��ʱ��ˢ�µ��˵�̹��
		int timeNum = random.nextInt(200)+100;
		int pos = random.nextInt(3);
		time++;
		if(time>timeNum && totalEnemyTankNum>0) {
			time =0;
			ArrayList<Color> colorList = new ArrayList<Color>();
			for(int i =0 ;i<25;i++) {
				colorList.add(new Color(random.nextInt(150),random.nextInt(150),random.nextInt(150)));
			}
			int space =0;
			if(pos == 2) space =52;	
			Tank et =new Tank(GAME_WIDTH/2*pos-space,0,colorList,false,Tank.Direction.D,5,this);
			String name =enemyTankName+"";
			enemyTankName++;
			totalEnemyTankNum--;
			et.setName(name);
			enemyTanks.add(et);
		}
		
		//������̹��
		
		for(int i = 0;i<enemyTanks.size();i++) {
			Tank enemyTank =enemyTanks.get(i);
			enemyTank.draw(g);
			enemyTank.collidesWithTanks(myTanks);
			//enemyTank.collidesWithTanks(enemyTanks);
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
//			myTank.collidesWithTanks(myTanks);
			myTank.collidesWithTanks(enemyTanks);
			myTank.draw(g);			
			myTank.eat(bb);//��Ѫ��
		}
		//��ǽ
		w1.draw(g);
		w2.draw(g);
		
		//��Ѫ��
		bb.draw(g);
		
		//����ֵ
		g.setColor(Color.GREEN);
		g.drawString("�ӵ�����:" + missiles.size(), 10, 50);//�ж����ڵ�����Ļ��
		g.drawString("��������:" + enemyTanks.size(), 10, 70);//�ж��ٵ�������Ļ��
		g.drawString("���˿��:" + totalEnemyTankNum, 10, 90);//���˿��
	}
	
	
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

	/**
	 * 
	 * launchFream   
	 * ��ʾ̹�˵�������  
	 *               
	 * void   
	 * saiyan 
	 * 2018��7��8�� ����11:26:08   
	 * 
	 */
	public void launchFream() {
		//����Լ�̹��
		for(int i=0;i<2;i++) {
			ArrayList<Color> colorList = new ArrayList<Color>();
			if(i == 0) {
				for(int j =0 ;j<25;j++) {
					colorList.add(new Color(random.nextInt(150),random.nextInt(150),random.nextInt(150)));
				}
			}else if(i == 1) {
				colorList.add(new Color(0,255,255));//0
				colorList.add(new Color(0,255,255));//1
				colorList.add(new Color(255,250,0));//2
				colorList.add(new Color(255,255,0));//3
				colorList.add(new Color(255,250,250));//4
				colorList.add(new Color(0,255,0));//5
				colorList.add(new Color(139,37,0));//6
				colorList.add(new Color(0,0,0));//7
				colorList.add(new Color(105,105,105));//8
				colorList.add(new Color(0,0,255));//9
				colorList.add(new Color(255,0,0));//10
				colorList.add(new Color(255,165,0));//11
				colorList.add(new Color(148,0,211));//12
				colorList.add(new Color(148,0,211));//13
				colorList.add(new Color(255,0,255));//14
				colorList.add(new Color(131,111,255));//15
				colorList.add(new Color(0,191,255));//16
				colorList.add(new Color(139,0,0));//17
				colorList.add(new Color(0,139,0));//18
				colorList.add(new Color(255,0,0));//19
			}
			Tank t = new Tank(300*(i+1),GAME_HEIGHT-100,colorList,true,Tank.Direction.STOP,7,this);
			myTanks.add(t);			
		}	
		//��ӵ���̹��
		for(int i=0;i<3;i++) {
			ArrayList<Color> colorList = new ArrayList<Color>();
			for(int j =0 ;j<25;j++) {
				colorList.add(new Color(random.nextInt(150),random.nextInt(150),random.nextInt(150)));
			}
			int space =0;
			if(i == 2) space =52;	
			Tank et =new Tank(GAME_WIDTH/2*i-space,0,colorList,false,Tank.Direction.D,5,this);
			totalEnemyTankNum--;
			enemyTanks.add(et);
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
