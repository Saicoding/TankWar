package mytest;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MyTest extends Frame{
	public static final int GAME_WIDTH = 838 ;//游戏屏幕高度
	public static final int GAME_HEIGHT = 864 ;//游戏屏幕宽度
	public static final int GAME_POSITION_X= 1075 ;//游戏屏幕位置x
	public static final int GAME_POSITION_Y= 249 ;//游戏屏幕位置y
	public static final Color FRONT_COLOR = new Color(51,51,51);//游戏前景色
	
	Image offScreenImage = null;//声明一个虚拟图片，一次性展现，解决闪屏问题
	
	TestPolygon testPolygon = new TestPolygon(200,200,5,this);
	TestPolygon testPolygon1 = new TestPolygon(200,500,5,this);
	TestCircle testCircle = new TestCircle(300,400,50,5,this);
	TestCircle testCircle1 = new TestCircle(300,600,50,5,this);

	public static void main(String[] args) {
		MyTest mt = new MyTest();
		mt.launchFream();
	}
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		Color c = g2.getColor();
		g2.setColor(Color.RED);
		
		testCircle.collidesWithPolygon();
		testCircle.collidesWithCircle();
		testPolygon.collidesWithPolygon();
		testPolygon.collidesWithCircle();
		
		g2.draw(testPolygon.createPath());
		g2.draw(testCircle.createPath());
		g2.draw(testCircle1.createPath());
		g2.draw(testPolygon1.createPath());
		g.drawString("circle x = "+testCircle.x+" y = "+testCircle.y, 10, 50);//有多少炮弹在屏幕上	
		
		g2.setColor(Color.GREEN);
		g.drawString("polygon x = "+testPolygon.x+" y = "+testPolygon.y, 10, 70);//有多少炮弹在屏幕上	
		
		g2.setColor(Color.GREEN);
		//圆与矩形
		if(testCircle.mtv1.axis != null) {
			g.drawString("圆与矩形mtv axis.x= "+testCircle.mtv1.axis.x+" y = "+testCircle.mtv1.axis.y, 10, 90);//有多少炮弹在屏幕上	
		}
		g.drawString("圆与矩形mtv overlap = "+testCircle.mtv1.overlap, 10, 110);//有多少炮弹在屏幕上	
		
		//圆与圆
		if(testCircle.mtv2.axis != null) {
			g.drawString("圆与圆mtv axis.x= "+testCircle.mtv2.axis.x+" y = "+testCircle.mtv2.axis.y, 10, 130);//有多少炮弹在屏幕上	
		}
		g.drawString("圆与圆mtv overlap = "+testCircle.mtv2.overlap, 10, 150);//有多少炮弹在屏幕上	
		
		//矩形与圆
		if(testPolygon.mtv2.axis != null) {
			g.drawString("矩形与圆mtv axis.x= "+testPolygon.mtv2.axis.x+" y = "+testPolygon.mtv2.axis.y, 10, 170);//有多少炮弹在屏幕上	
		}
		g.drawString("矩形与圆mtv overlap = "+testPolygon.mtv2.overlap, 10, 190);//有多少炮弹在屏幕上	
		
		//矩形与矩形
		if(testPolygon.mtv1.axis != null) {
			g.drawString("矩形与矩形mtv axis.x= "+testPolygon.mtv1.axis.x+" y = "+testPolygon.mtv1.axis.y, 10, 210);//有多少炮弹在屏幕上	
		}
		g.drawString("矩形与矩形mtv overlap = "+testPolygon.mtv1.overlap, 10, 230);//有多少炮弹在屏幕上	
		g2.setColor(c);
	}
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
	public void launchFream() {
		
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
			testCircle.keyPressed(e);	
			testPolygon.keyPressed(e);
		}
	}
}

