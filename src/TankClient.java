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

public class TankClient extends Frame{//通过继承Frame 可以添加自己的成员变量和方法，建议用这种方式

	private static final long serialVersionUID = 1L;
	public static final int GAME_WIDTH = 800 ;//游戏屏幕高度
	public static final int GAME_HEIGHT = 600 ;//游戏屏幕宽度
	public static final int GAME_POSITION_X= 0 ;//游戏屏幕位置x
	public static final int GAME_POSITION_Y= 455 ;//游戏屏幕位置y
	public static final Color FRONT_COLOR = new Color(127,255,0);//游戏前景色	
	
	static boolean keyLeftPressed = false;//设置默认按键状态
	static boolean keyRightPressed = false;//设置默认按键状态
	static boolean keyUpPressed = false;//设置默认按键状态
	static boolean keyDownPressed = false;//设置默认按键状态
	
	Tank myTank = new Tank(50,50,5);
	
	/*
	 * 主函数
	 */
	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.launchFream();
	}

	Image offScreenImage = null;//声明一个虚拟图片，一次性展现，解决闪屏问题

	@Override
	public void paint(Graphics g) {
		myTank.draw(g);
	}
	
	@Override
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

	/*
	 * 启动画布 
	 */
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
				myTank.move();//每画一次之前坦克移动一次
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
			myTank.keyPressed(e);
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			myTank.keyRelease(e);
		}
	}
}
