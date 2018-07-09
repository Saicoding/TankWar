/**   
 * @author:        Saiyan
 * @date:          2018年7月5日 下午5:17:36  
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
 * 这是坦克游戏的主窗口 
 * saiyan
 * 2018年7月8日 下午11:24:44
 */
public class TankClient extends Frame{//通过继承Frame 可以添加自己的成员变量和方法，建议用这种方式

	private static final long serialVersionUID = 1L;
	public static final int GAME_WIDTH = 1400 ;//游戏屏幕高度
	public static final int GAME_HEIGHT = 900 ;//游戏屏幕宽度
	public static final int GAME_POSITION_X= 100 ;//游戏屏幕位置x
	public static final int GAME_POSITION_Y= 100 ;//游戏屏幕位置y
	public static final Color FRONT_COLOR = new Color(51,51,51);//游戏前景色	
	private static Random random = new Random();
	
	static boolean keyLeftPressed = false;//设置默认按键状态
	static boolean keyRightPressed = false;//设置默认按键状态
	static boolean keyUpPressed = false;//设置默认按键状态
	static boolean keyDownPressed = false;//设置默认按键状态
	
	

	
	Wall w1 = new Wall(300,200,20,150,this),w2 = new Wall(500,100,300,20,this);
	
	Blood bb = new Blood();
	
	public int totalEnemyTankNum = 11;
	public int time = 0;
	private int enemyTankName = 1;
	
	ArrayList<Tank> myTanks =new ArrayList<Tank>();//自己坦克数组
	ArrayList<Tank> enemyTanks = new ArrayList<Tank>();//敌人坦克数组
	
	ArrayList<Boom> booms =new ArrayList<Boom>();//炸弹容器
	ArrayList<Missile> missiles = new ArrayList<Missile>();//子弹容器
	
	Image offScreenImage = null;//声明一个虚拟图片，一次性展现，解决闪屏问题
	
	/*
	 * 主函数
	 */
	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.launchFream();
	}
	

	public void paint(Graphics g) {
		
		//画子弹
		for(int i = 0 ;i<missiles.size();i++) {
			Missile m = missiles.get(i);
			m.hitTanks(enemyTanks);//可以打敌人
//			m.hitTanks(myTanks);//敌人可以打我的坦克
			m.hitWall(w1);
			m.hitWall(w2);
			m.draw(g);
		}
		
		//每隔随机一个时间刷新敌人的坦克
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
		
		//画敌人坦克
		
		for(int i = 0;i<enemyTanks.size();i++) {
			Tank enemyTank =enemyTanks.get(i);
			enemyTank.draw(g);
			enemyTank.collidesWithTanks(myTanks);
			//enemyTank.collidesWithTanks(enemyTanks);
		}
		
		//画炸弹
		if(booms.size()>0) {
			for(int i =0;i<booms.size();i++) {
				Boom b = booms.get(i);
				b.draw(g);
			}
		}
		
		//画自己的坦克
		for(int i = 0;i<myTanks.size();i++) {
			Tank myTank =myTanks.get(i);
			myTank.collidesWithWall(w1);
			myTank.collidesWithWall(w2);
//			myTank.collidesWithTanks(myTanks);
			myTank.collidesWithTanks(enemyTanks);
			myTank.draw(g);			
			myTank.eat(bb);//吃血块
		}
		//画墙
		w1.draw(g);
		w2.draw(g);
		
		//画血块
		bb.draw(g);
		
		//画数值
		g.setColor(Color.GREEN);
		g.drawString("子弹数量:" + missiles.size(), 10, 50);//有多少炮弹在屏幕上
		g.drawString("敌人数量:" + enemyTanks.size(), 10, 70);//有多少敌人在屏幕上
		g.drawString("敌人库存:" + totalEnemyTankNum, 10, 90);//敌人库存
	}
	
	
	/*
	 * 截取update方法，让图片一次性显示，解决闪屏问题，update原来的方法是自动刷新
	 */	
	public void update(Graphics g) {
		if(offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH,GAME_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();//先拿到画笔
		Color c = gOffScreen.getColor();//得到前景色
		gOffScreen.setColor(FRONT_COLOR );	//设置前景色
		gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);//填充画布
		gOffScreen.setColor(c);//恢复回来颜色
		paint(gOffScreen);//把图片画到虚拟图片
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
		//添加自己坦克
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
		//添加敌人坦克
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
					Thread.sleep(20);
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
