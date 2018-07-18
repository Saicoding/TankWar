package Main;
/**   
 * @author:        Saiyan
 * @date:          2018年7月5日 下午5:17:36  
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
 * 这是坦克游戏的主窗口 
 * saiyan
 * 2018年7月8日 下午11:24:44
 */
public class TankClient extends Frame{//通过继承Frame 可以添加自己的成员变量和方法，建议用这种方式
	
	public int enemyTankNum = 0;//敌人坦克数量
	public int myTankNum = 1;//我的坦克数量
	public int totalEnemyTankNum = 0;//库存坦克
	public int myTankSpeed = 12;//我的坦克速度
	public int enemyTankSpeed = 7;//敌人坦克速度
	public int tankName = 1;//坦克起始名字
	
	private static final long serialVersionUID = 1L;
	public static final int GAME_WIDTH = 1920 ;//游戏屏幕高度
	public static final int GAME_HEIGHT = 1050 ;//游戏屏幕宽度
	public static final int GAME_POSITION_X= 0 ;//游戏屏幕位置x
	public static final int GAME_POSITION_Y= 0 ;//游戏屏幕位置y
	public static final Color FRONT_COLOR = new Color(51,51,51);//游戏前景色	
	private static Random random = new Random();
	
	static boolean keyLeftPressed = false;//设置默认按键状态
	static boolean keyRightPressed = false;//设置默认按键状态
	static boolean keyUpPressed = false;//设置默认按键状态
	static boolean keyDownPressed = false;//设置默认按键状态
	
	public int p1AllShotNum = 0;//p1打出的子弹数量
	public int p1AllHitNum = 0;//p1命中的子弹数量
	public int p2AllShotNum = 0;//p2打出的子弹数量
	public int p2AllHitNum = 0;//p2命中的子弹数量
	public int p1AllKillNum = 0;//击杀数量
	public int p2AllKillNum = 0;//击杀数量
	
	public int time = 0;
	
	public Timer timer1 = new Timer();//生成血块计时器
	public Timer timer2 = new Timer();//刷新敌人坦克计时器
	public Timer timer3 = new Timer();//刷新动画等待时间
	
	public ArrayList<Tank> myTanks =new ArrayList<Tank>();//自己坦克数组
	public ArrayList<Tank> enemyTanks = new ArrayList<Tank>();//敌人坦克数组
	
	public ArrayList<Animate> animates =new ArrayList<Animate>();//炸弹容器
	public ArrayList<Missile> missiles = new ArrayList<Missile>();//子弹容器
	public ArrayList<Tools> tools = new ArrayList<Tools>();//所有工具
	public ArrayList<Tree> trees = new ArrayList<Tree>();//所有工具
	public ArrayList<River> rivers = new ArrayList<River>();//所有河流
	public ArrayList<Wall> walls = new ArrayList<Wall>();//所有河流
	
	Image offScreenImage = null;//声明一个虚拟图片，一次性展现，解决闪屏问题
	Image offScreenImage1 = null;//声明一个虚拟图片，一次性展现，解决闪屏问题
	
	
	/*
	 * 主函数
	 */
	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.launchFream();
	}

	public void paint(Graphics g) {
		//每隔一段时间刷新道具
		int t = random.nextInt(6000)+45000;
		float bx = random.nextInt(GAME_WIDTH-80)+40;
		float by = random.nextInt(GAME_HEIGHT-80)+80;
		
		if(timer1.getElaplseTime() > t) {
			Blood b = new Blood(bx,by,"blood","blood",7,65,65,this);
			tools.add(b);
			timer1.start();
		}

		//移除已经播放完的动画
		new Thread(new RemoveAnimateThread(this)).start();		
		
		//画河流
		for(int i = 0 ; i<rivers.size();i++) {
			River river = rivers.get(i);
			river.draw(g);
		}	
		
		//画子弹
		for(int i = 0 ;i<missiles.size();i++) {
			Missile m = missiles.get(i);
			//判断子弹打击子弹
			m.hitMissiles(missiles);
			
			//判断子弹打击树
			m.hitTrees(trees);
			
			//判断子弹打击墙
			m.hitWalls(walls);
			
			//判断撞击屏幕
			m.collidesWithScreen();
			
			//判断子弹打击坦克
			m.hitTanks(enemyTanks);//可以打敌人
			m.hitTanks(myTanks);//敌人可以打我的坦克
			//判断打击坦克的thread
//			new Thread(new HitTanksThread(m,this,g)).start();			
//			new Thread(new HitMissilesThread(m,this,g)).start();
//			m.hitTanks(myTanks);//敌人可以打我的坦克
			
//			m.hitWall(w1);
//			m.hitWall(w2);
			m.draw(g);
		}
		

		
		//每隔随机一个时间刷新敌人的坦克
		int timeNum = random.nextInt(5000)+2000;
		int pos = random.nextInt(3);
		int space =0;
		if(pos == 2) space =52;	
		if(timer2.getElaplseTime()>timeNum && totalEnemyTankNum>0) {//如果刷新坦克timer大于指定时间,就启动动画timer,并暂停刷新坦克
			timer3.start();
			timer2.reset();
			Animate a = new Animate((GAME_WIDTH-120)/2*pos-space+40,85,"born",11,this);//生成动画
			animates.add(a);
		}
		if(timer3.getElaplseTime()>500) {//如果动画时间超过4秒
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
		
		//画敌人坦克
		
		for(int i = 0;i<enemyTanks.size();i++) {
			Tank enemyTank =enemyTanks.get(i);	
			//检测敌人坦克与屏幕相撞
			enemyTank.collidesWithScreen();	
			
			//敌人和友军检测碰撞
			enemyTank.collidesWithTanks(myTanks);
			
			//检测与河碰撞
			enemyTank.collidesWithRivers(rivers);
			
			//检测与墙碰撞
			enemyTank.collidesWithWalls(walls);
			
			//与树相撞
//			enemyTank.collidesWithTrees(trees);
			
			//敌人和敌人检测碰撞
//			enemyTank.collidesWithTanks(enemyTanks);
			enemyTank.draw(g);
			Graphics2D g2 = (Graphics2D)g;
			Color c = g2.getColor();
			g2.setColor(Color.GREEN);
			
			//敌人坦克边框
//			g2.draw(enemyTank.createPath());
			g2.setColor(c);
		}
		
		//画动画
		if(animates.size()>0) {
			for(int i =0;i<animates.size();i++) {
				Animate a = animates.get(i);
				a.draw(g);
			}
		}
		
		//画自己的坦克
		for(int i = 0;i<myTanks.size();i++) {
			Tank myTank =myTanks.get(i);
			
			//判断友军与友军相撞
//			myTank.collidesWithTanks(myTanks);
			
			//友军与敌人相撞
			myTank.collidesWithTanks(enemyTanks);
			
			//检测与河碰撞
			myTank.collidesWithRivers(rivers);
			
			//检测与墙碰撞
			myTank.collidesWithWalls(walls);
			
			//与树相撞
//			myTank.collidesWithTrees(trees);
			
			//判断友军与屏幕相撞
			myTank.collidesWithScreen();
			myTank.draw(g);			
			myTank.collidesWithTools();//
			
			Graphics2D g2 = (Graphics2D)g;
			Color c = g2.getColor();
			g2.setColor(Color.GREEN);
			
			//画友军坦克边框
//			g2.draw(myTank.createPath());
			if(i==0) {
			
//			g.drawString("速度向量角度"+Math.atan(myTank.speedV.x/myTank.speedV.y)*(180/Math.PI), 10, 130);
			}
			g2.setColor(c);
		}
		
		//画血块
		for(int i = 0;i<tools.size();i++) {
			Tools bt = tools.get(i);
			if(bt.live)bt.draw(g);
			else tools.remove(bt);			
		}
				
		//画数值
		g.setColor(Color.GREEN);
		g.drawString("子弹数量:" + missiles.size(), 10, 50);//有多少炮弹在屏幕上
		g.drawString("敌人数量:" + enemyTanks.size(), 10, 70);//有多少敌人在屏幕上
		g.drawString("敌人库存:" + totalEnemyTankNum, 10, 90);//敌人库存
		g.drawString("p1发出的子弹数:" + p1AllShotNum, 10, 110);
		g.drawString("p2发出的子弹数:" + p2AllShotNum, 10, 130);
		g.drawString("p1命中的子弹数:" + p1AllHitNum, 10, 150);
		g.drawString("p2命中的子弹数:" + p2AllHitNum, 10, 170);
		g.drawString("p1摧毁坦克数量:" + p1AllKillNum, 10, 190);
		g.drawString("p2摧毁坦克数量:" + p2AllKillNum, 10, 210);
		
		//画树
		for(int i = 0 ; i<trees.size();i++) {
			Tree tree = trees.get(i);
			tree.draw(g);
		}
		//画墙
		for(int i = 0 ; i<walls.size();i++) {
			Wall wall = walls.get(i);
			wall.draw(g);
		}
		
		//画数据统计
		float num1,num2;
		if(p1AllShotNum !=0) num1 = (float)(p1AllHitNum*100/p1AllShotNum);
		else num1 = 0;
		g.drawString("p1命中率:" + num1 +"%", 10, 230);
		if(p2AllShotNum !=0) num2 = (float)(p2AllHitNum*100/p2AllShotNum);
		else num2 = 0;
		g.drawString("p2命中率:" + num2 +"%", 10, 250);
	}
	
	
	/*
	 * 截取update方法，让图片一次性显示，解决闪屏问题，update原来的方法是自动刷新
	 */	
	public void update(Graphics g) {
		if(offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH,GAME_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();//先拿到画笔
		Graphics2D gOffScreen2 =(Graphics2D)gOffScreen;
		Color c = gOffScreen2.getColor();//得到前景色
		gOffScreen2.setColor(FRONT_COLOR );	//设置前景色
		Toolkit kit = Toolkit.getDefaultToolkit();
		Image img = kit.getImage("img/背景.jpg");
		gOffScreen2.drawImage(img,0,0,null);
		gOffScreen2.setColor(c);//恢复回来颜色
		paint(gOffScreen2);//把图片画到虚拟图片
		
		g.drawImage(offScreenImage, 0, 0, null);//把虚拟图片一次性贴到画布上
	}

	/**
	 * 
	 * launchFream   
	 * 显示坦克的主窗口  
	 *               
	 * void   
	 * saiyan 
	 * 2018年7月8日 下午11:26:08   
	 * 
	 */
	public void launchFream() {
		//启动计时器
		timer1.start();
		timer2.start();
		//添加自己坦克
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
				t.name = "冶";
				t.owner = "p1";
			}
			if(i == 1) {
				t.name = "思";
				t.owner = "p2";
			}
			if(i==0)t.flag=1;//测试用
			myTanks.add(t);			
		}	
		//添加敌人坦克
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
		
		 //添加树
		 for(int i = 0 ; i < 90 ;i++) {
			 int m = random.nextInt(1920-60)+30;
			 int n = random.nextInt(1050-60)+30;
			 new CreateObstacle(m, n, true, 1, "tree",this,1);	 
		 }
		 
		 //添加河流
		 new CreateObstacle(400, 600, true, 10, "river",this,1);	
		 new CreateObstacle(200, 200, true, 15, "river",this,1);
		 
		 //添加墙体
		 new CreateObstacle(800, 580, true, 1, "wall",this,4);



		 this.setLocation(GAME_POSITION_X,GAME_POSITION_Y);
		 this.setSize(GAME_WIDTH,GAME_HEIGHT);
		 
		 //监听关闭事件
		 this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			 
		});
		//监听键盘事件
		 this.addKeyListener(new KeyMonitor());		 
    
		 this.setTitle("坦克大战");		//设置标题 	
		 this.setBackground(FRONT_COLOR );//设置背景颜色
		 this.setResizable(false);//设置不可以改变窗口大小
		 this.setVisible(true);//设置是否可见
		 
		//窗口起来后运行一个线程来刷新画布
		 new Thread(new PaintThread()).start();
	}
	
	/*
	 * 启用一个线程来刷新(重画)画布
	 */
	private class PaintThread implements Runnable {
		
		public void run() {
			while(true) {
				repaint();//直接访问包装类的成员方法
				try {
					Thread.sleep(40);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/*
	 * 键盘监听，实现键盘控制坦克
	 */
	private class KeyMonitor extends KeyAdapter{//这里选择继承的原因是，如果选择implements 就要实现这个接口里的所有方法
		//设置按键状态
	
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
