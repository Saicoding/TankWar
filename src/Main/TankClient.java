package Main;
/**   
 * @author:        Saiyan
 * @date:          2018��7��5�� ����5:17:36  
 */   
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import Thread.RemoveAnimateThread;


/**
 * 
 * TankClient
 * ����̹����Ϸ�������� 
 * saiyan
 * 2018��7��8�� ����11:24:44
 */
public class TankClient extends Frame{//ͨ���̳�Frame ��������Լ��ĳ�Ա�����ͷ��������������ַ�ʽ
	
	public int enemyTankNum = 3;//����̹������
	public int myTankNum = 2;//�ҵ�̹������
	public int totalEnemyTankNum = 11;//���̹��
	public int myTankSpeed = 12;//�ҵ�̹���ٶ�
	public int enemyTankSpeed = 7;//����̹���ٶ�
	public int tankName = 1;//̹����ʼ����
	
	public long lastTime;
	public float elapseTime;
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
	
	Wall w1 = new Wall(this),w2 = new Wall(this);
	
	Blood bb = new Blood();
	
	public int time = 0;
	
	public ArrayList<Tank> myTanks =new ArrayList<Tank>();//�Լ�̹������
	public ArrayList<Tank> enemyTanks = new ArrayList<Tank>();//����̹������
	
	public ArrayList<Animate> animates =new ArrayList<Animate>();//ը������
	public ArrayList<Missile> missiles = new ArrayList<Missile>();//�ӵ�����
	
	Image offScreenImage = null;//����һ������ͼƬ��һ����չ�֣������������
	
	/*
	 * ������
	 */
	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.launchFream();
	}

	public void paint(Graphics g) {
		long currentTime = new Date().getTime();
		if(lastTime == 0)elapseTime =40;
		else elapseTime = currentTime - lastTime;
		lastTime = currentTime;
		//�Ƴ��Ѿ�������Ķ���
		new Thread(new RemoveAnimateThread(this)).start();
		//���ӵ�
		for(int i = 0 ;i<missiles.size();i++) {
			Missile m = missiles.get(i);
			//�ж��ӵ�����ӵ�
			m.hitMissiles(missiles);
			
			//�ж�ײ����Ļ
			m.collidesWithScreen();
			
			//�ж��ӵ����̹��
			m.hitTanks(enemyTanks);//���Դ����
			//�жϴ��̹�˵�thread
//			new Thread(new HitTanksThread(m,this,g)).start();			
//			new Thread(new HitMissilesThread(m,this,g)).start();
//			m.hitTanks(myTanks);//���˿��Դ��ҵ�̹��
			
//			m.hitWall(w1);
//			m.hitWall(w2);
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
			Tank et =new Tank((GAME_WIDTH-120)/2*pos-space+40,65,colorList,false,enemyTankSpeed,90,this);
			et.name = tankName+"";		
			totalEnemyTankNum--;
			enemyTanks.add(et);
			tankName++;
		}
		
		//������̹��
		
		for(int i = 0;i<enemyTanks.size();i++) {
			Tank enemyTank =enemyTanks.get(i);
			
			//������̹������Ļ��ײ
			enemyTank.collidesWithScreen();	
			
			//���˺��Ѿ������ײ
			enemyTank.collidesWithTanks(myTanks);
			
			//���˺͵��˼����ײ
//			enemyTank.collidesWithTanks(enemyTanks);
			enemyTank.draw(g);
			Graphics2D g2 = (Graphics2D)g;
			Color c = g2.getColor();
			g2.setColor(Color.GREEN);
			
			//����̹�˱߿�
//			g2.draw(enemyTank.createPath());
			g2.setColor(c);
		}
		
		//������
		if(animates.size()>0) {
			for(int i =0;i<animates.size();i++) {
				Animate a = animates.get(i);
				a.draw(g);
			}
		}
		//��ǽ
		w1.draw(g);
		w2.draw(g);
		
		//���Լ���̹��
		for(int i = 0;i<myTanks.size();i++) {
			Tank myTank =myTanks.get(i);
			
			//�ж��Ѿ����Ѿ���ײ
//			myTank.collidesWithTanks(myTanks);
			
			//�Ѿ��������ײ
			myTank.collidesWithTanks(enemyTanks);
			
			//�ж��Ѿ�����Ļ��ײ
			myTank.collidesWithScreen();
			myTank.draw(g);			
			myTank.eat(bb);//��Ѫ��
			
			Graphics2D g2 = (Graphics2D)g;
			Color c = g2.getColor();
			g2.setColor(Color.GREEN);
			
			//���Ѿ�̹�˱߿�
//			g2.draw(myTank.createPath());
			if(i==0) {
			
			g.drawString("̹�˽Ƕ�:" + myTank.angle, 10, 110);//̹�˽Ƕ�
			
//			g.drawString("�ٶ������Ƕ�"+Math.atan(myTank.speedV.x/myTank.speedV.y)*(180/Math.PI), 10, 130);
			}
			g2.setColor(c);
		}

		
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
		for(int i=0;i<myTankNum;i++) {
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
			Tank t = new Tank(300*(i+1),GAME_HEIGHT-100,colorList,true,myTankSpeed,-90,this);
			if(i == 0 ) t.name = "ұ";
			if(i == 1) t.name = "˼";
			if(i==0)t.flag=1;//������
			myTanks.add(t);			
		}	
		//��ӵ���̹��
		for(int i=0;i<enemyTankNum;i++) {
			ArrayList<Color> colorList = new ArrayList<Color>();
			for(int j =0 ;j<25;j++) {
				colorList.add(new Color(random.nextInt(150),random.nextInt(150),random.nextInt(150)));
			}
			int space =0;
			if(i == 2) space =52;	
			Tank et =new Tank((GAME_WIDTH-120)/2*i-space+40,65,colorList,false,enemyTankSpeed,90,this);
			et.name = tankName+"";
			enemyTanks.add(et);
			tankName++;
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
					Thread.sleep(40);
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
