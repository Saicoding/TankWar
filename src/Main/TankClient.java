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
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Random;

import Obstacle.River;
import Obstacle.Tree;
import Obstacle.Wall;
import Thread.RemoveAnimateThread;


/**
 * 
 * TankClient
 * ����̹����Ϸ�������� 
 * saiyan
 * 2018��7��8�� ����11:24:44
 */
public class TankClient extends Frame{//ͨ���̳�Frame ��������Լ��ĳ�Ա�����ͷ��������������ַ�ʽ
	
	public int enemyTankNum = 0;//����̹������
	public int myTankNum = 1;//�ҵ�̹������
	public int totalEnemyTankNum = 0;//���̹��
	public int myTankSpeed = 12;//�ҵ�̹���ٶ�
	public int enemyTankSpeed = 7;//����̹���ٶ�
	public int tankName = 1;//̹����ʼ����
	
	private static final long serialVersionUID = 1L;
	public static final int GAME_WIDTH = 1920 ;//��Ϸ��Ļ�߶�
	public static final int GAME_HEIGHT = 1050 ;//��Ϸ��Ļ���
	public static final int GAME_POSITION_X= 0 ;//��Ϸ��Ļλ��x
	public static final int GAME_POSITION_Y= 0 ;//��Ϸ��Ļλ��y
	public static final Color FRONT_COLOR = new Color(51,51,51);//��Ϸǰ��ɫ	
	private static Random random = new Random();
	
	static boolean keyLeftPressed = false;//����Ĭ�ϰ���״̬
	static boolean keyRightPressed = false;//����Ĭ�ϰ���״̬
	static boolean keyUpPressed = false;//����Ĭ�ϰ���״̬
	static boolean keyDownPressed = false;//����Ĭ�ϰ���״̬
	
	public int p1AllShotNum = 0;//p1������ӵ�����
	public int p1AllHitNum = 0;//p1���е��ӵ�����
	public int p2AllShotNum = 0;//p2������ӵ�����
	public int p2AllHitNum = 0;//p2���е��ӵ�����
	public int p1AllKillNum = 0;//��ɱ����
	public int p2AllKillNum = 0;//��ɱ����
	
	public int time = 0;
	
	public Timer timer1 = new Timer();//����Ѫ���ʱ��
	public Timer timer2 = new Timer();//ˢ�µ���̹�˼�ʱ��
	public Timer timer3 = new Timer();//ˢ�¶����ȴ�ʱ��
	
	public ArrayList<Tank> myTanks =new ArrayList<Tank>();//�Լ�̹������
	public ArrayList<Tank> enemyTanks = new ArrayList<Tank>();//����̹������
	
	public ArrayList<Animate> animates =new ArrayList<Animate>();//ը������
	public ArrayList<Missile> missiles = new ArrayList<Missile>();//�ӵ�����
	public ArrayList<Tools> tools = new ArrayList<Tools>();//���й���
	public ArrayList<Tree> trees = new ArrayList<Tree>();//���й���
	public ArrayList<River> rivers = new ArrayList<River>();//���к���
	public ArrayList<Wall> walls = new ArrayList<Wall>();//���к���
	
	Image offScreenImage = null;//����һ������ͼƬ��һ����չ�֣������������
	Image offScreenImage1 = null;//����һ������ͼƬ��һ����չ�֣������������
	
	
	/*
	 * ������
	 */
	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.launchFream();
	}

	public void paint(Graphics g) {
		//ÿ��һ��ʱ��ˢ�µ���
		int t = random.nextInt(6000)+45000;
		float bx = random.nextInt(GAME_WIDTH-80)+40;
		float by = random.nextInt(GAME_HEIGHT-80)+80;
		
		if(timer1.getElaplseTime() > t) {
			Blood b = new Blood(bx,by,"blood","blood",7,65,65,this);
			tools.add(b);
			timer1.start();
		}

		//�Ƴ��Ѿ�������Ķ���
		new Thread(new RemoveAnimateThread(this)).start();		
		
		//������
		for(int i = 0 ; i<rivers.size();i++) {
			River river = rivers.get(i);
			river.draw(g);
		}	
		
		//���ӵ�
		for(int i = 0 ;i<missiles.size();i++) {
			Missile m = missiles.get(i);
			//�ж��ӵ�����ӵ�
			m.hitMissiles(missiles);
			
			//�ж��ӵ������
			m.hitTrees(trees);
			
			//�ж��ӵ����ǽ
			m.hitWalls(walls);
			
			//�ж�ײ����Ļ
			m.collidesWithScreen();
			
			//�ж��ӵ����̹��
			m.hitTanks(enemyTanks);//���Դ����
			m.hitTanks(myTanks);//���˿��Դ��ҵ�̹��
			//�жϴ��̹�˵�thread
//			new Thread(new HitTanksThread(m,this,g)).start();			
//			new Thread(new HitMissilesThread(m,this,g)).start();
//			m.hitTanks(myTanks);//���˿��Դ��ҵ�̹��
			
//			m.hitWall(w1);
//			m.hitWall(w2);
			m.draw(g);
		}
		

		
		//ÿ�����һ��ʱ��ˢ�µ��˵�̹��
		int timeNum = random.nextInt(5000)+2000;
		int pos = random.nextInt(3);
		int space =0;
		if(pos == 2) space =52;	
		if(timer2.getElaplseTime()>timeNum && totalEnemyTankNum>0) {//���ˢ��̹��timer����ָ��ʱ��,����������timer,����ͣˢ��̹��
			timer3.start();
			timer2.reset();
			Animate a = new Animate((GAME_WIDTH-120)/2*pos-space+40,85,"born",11,this);//���ɶ���
			animates.add(a);
		}
		if(timer3.getElaplseTime()>500) {//�������ʱ�䳬��4��
			timer3.reset();
			timer2.start();
			ArrayList<Color> colorList = new ArrayList<Color>();
			for(int i =0 ;i<25;i++) {
				colorList.add(new Color(random.nextInt(150),random.nextInt(150),random.nextInt(150)));
			}
			Tank et =new Tank((GAME_WIDTH-120)/2*pos-space+40,85,colorList,false,enemyTankSpeed,90,this);
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
			
			//��������ײ
			enemyTank.collidesWithRivers(rivers);
			
			//�����ǽ��ײ
			enemyTank.collidesWithWalls(walls);
			
			//������ײ
//			enemyTank.collidesWithTrees(trees);
			
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
		
		//���Լ���̹��
		for(int i = 0;i<myTanks.size();i++) {
			Tank myTank =myTanks.get(i);
			
			//�ж��Ѿ����Ѿ���ײ
//			myTank.collidesWithTanks(myTanks);
			
			//�Ѿ��������ײ
			myTank.collidesWithTanks(enemyTanks);
			
			//��������ײ
			myTank.collidesWithRivers(rivers);
			
			//�����ǽ��ײ
			myTank.collidesWithWalls(walls);
			
			//������ײ
//			myTank.collidesWithTrees(trees);
			
			//�ж��Ѿ�����Ļ��ײ
			myTank.collidesWithScreen();
			myTank.draw(g);			
			myTank.collidesWithTools();//
			
			Graphics2D g2 = (Graphics2D)g;
			Color c = g2.getColor();
			g2.setColor(Color.GREEN);
			
			//���Ѿ�̹�˱߿�
//			g2.draw(myTank.createPath());
			if(i==0) {
			
//			g.drawString("�ٶ������Ƕ�"+Math.atan(myTank.speedV.x/myTank.speedV.y)*(180/Math.PI), 10, 130);
			}
			g2.setColor(c);
		}
		
		//��Ѫ��
		for(int i = 0;i<tools.size();i++) {
			Tools bt = tools.get(i);
			if(bt.live)bt.draw(g);
			else tools.remove(bt);			
		}
				
		//����ֵ
		g.setColor(Color.GREEN);
		g.drawString("�ӵ�����:" + missiles.size(), 10, 50);//�ж����ڵ�����Ļ��
		g.drawString("��������:" + enemyTanks.size(), 10, 70);//�ж��ٵ�������Ļ��
		g.drawString("���˿��:" + totalEnemyTankNum, 10, 90);//���˿��
		g.drawString("p1�������ӵ���:" + p1AllShotNum, 10, 110);
		g.drawString("p2�������ӵ���:" + p2AllShotNum, 10, 130);
		g.drawString("p1���е��ӵ���:" + p1AllHitNum, 10, 150);
		g.drawString("p2���е��ӵ���:" + p2AllHitNum, 10, 170);
		g.drawString("p1�ݻ�̹������:" + p1AllKillNum, 10, 190);
		g.drawString("p2�ݻ�̹������:" + p2AllKillNum, 10, 210);
		
		//����
		for(int i = 0 ; i<trees.size();i++) {
			Tree tree = trees.get(i);
			tree.draw(g);
		}
		//��ǽ
		for(int i = 0 ; i<walls.size();i++) {
			Wall wall = walls.get(i);
			wall.draw(g);
		}
		
		//������ͳ��
		float num1,num2;
		if(p1AllShotNum !=0) num1 = (float)(p1AllHitNum*100/p1AllShotNum);
		else num1 = 0;
		g.drawString("p1������:" + num1 +"%", 10, 230);
		if(p2AllShotNum !=0) num2 = (float)(p2AllHitNum*100/p2AllShotNum);
		else num2 = 0;
		g.drawString("p2������:" + num2 +"%", 10, 250);
	}
	
	
	/*
	 * ��ȡupdate��������ͼƬһ������ʾ������������⣬updateԭ���ķ������Զ�ˢ��
	 */	
	public void update(Graphics g) {
		if(offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH,GAME_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();//���õ�����
		Graphics2D gOffScreen2 =(Graphics2D)gOffScreen;
		Color c = gOffScreen2.getColor();//�õ�ǰ��ɫ
		gOffScreen2.setColor(FRONT_COLOR );	//����ǰ��ɫ
		Toolkit kit = Toolkit.getDefaultToolkit();
		Image img = kit.getImage("img/����.jpg");
		gOffScreen2.drawImage(img,0,0,null);
		gOffScreen2.setColor(c);//�ָ�������ɫ
		paint(gOffScreen2);//��ͼƬ��������ͼƬ
		
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
		//������ʱ��
		timer1.start();
		timer2.start();
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
			animates.add(new Animate(t.x,t.y,"born",11,this));
			if(i == 0 ) {
				t.name = "ұ";
				t.owner = "p1";
			}
			if(i == 1) {
				t.name = "˼";
				t.owner = "p2";
			}
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
			Tank et =new Tank((GAME_WIDTH-120)/2*i-space+40,85,colorList,false,enemyTankSpeed,90,this);
			animates.add(new Animate(et.x,et.y,"born",11,this));
			et.name = tankName+"";
			enemyTanks.add(et);
			tankName++;
		}
		
		 //�����
		 for(int i = 0 ; i < 90 ;i++) {
			 int m = random.nextInt(1920-60)+30;
			 int n = random.nextInt(1050-60)+30;
			 new CreateObstacle(m, n, true, 1, "tree",this,1);	 
		 }
		 
		 //��Ӻ���
		 new CreateObstacle(400, 600, true, 10, "river",this,1);	
		 new CreateObstacle(200, 200, true, 15, "river",this,1);
		 
		 //���ǽ��
		 new CreateObstacle(800, 580, true, 1, "wall",this,4);



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
