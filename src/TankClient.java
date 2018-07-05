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

public class TankClient extends Frame{//ͨ���̳�Frame ��������Լ��ĳ�Ա�����ͷ��������������ַ�ʽ

	private static final long serialVersionUID = 1L;
	public static final int GAME_WIDTH = 800 ;//��Ϸ��Ļ�߶�
	public static final int GAME_HEIGHT = 600 ;//��Ϸ��Ļ���
	public static final int GAME_POSITION_X= 0 ;//��Ϸ��Ļλ��x
	public static final int GAME_POSITION_Y= 455 ;//��Ϸ��Ļλ��y
	public static final Color FRONT_COLOR = new Color(127,255,0);//��Ϸǰ��ɫ	
	
	static boolean keyLeftPressed = false;//����Ĭ�ϰ���״̬
	static boolean keyRightPressed = false;//����Ĭ�ϰ���״̬
	static boolean keyUpPressed = false;//����Ĭ�ϰ���״̬
	static boolean keyDownPressed = false;//����Ĭ�ϰ���״̬
	
	Tank myTank = new Tank(50,50,5);
	
	/*
	 * ������
	 */
	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.launchFream();
	}

	Image offScreenImage = null;//����һ������ͼƬ��һ����չ�֣������������

	@Override
	public void paint(Graphics g) {
		myTank.draw(g);
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
				myTank.move();//ÿ��һ��֮ǰ̹���ƶ�һ��
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
			myTank.keyPressed(e);
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			myTank.keyRelease(e);
		}
	}
}
