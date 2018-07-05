/**   
 * @author:        Saiyan
 * @date:          2018��7��5�� ����5:17:36  
 */   
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TankClient extends Frame{//ͨ���̳�Frame ��������Լ��ĳ�Ա�����ͷ��������������ַ�ʽ
	public static final int GAME_WIDTH = 800 ;//��Ϸ��Ļ�߶�
	public static final int GAME_HEIGHT = 600 ;//��Ϸ��Ļ���
	public static final int GAME_POSITION_X= 0 ;//��Ϸ��Ļλ��x
	public static final int GAME_POSITION_Y= 455 ;//��Ϸ��Ļλ��y
	public static final Color FRONT_COLOR = new Color(127,255,0);//��Ϸǰ��ɫ

	int x = 50,y =50;
	
	Image offScreenImage = null;//����һ������ͼƬ��һ����չ�֣������������

	@Override
	public void paint(Graphics g) {//����window��paint����
		Color c = g.getColor();//�õ�ǰ��ɫ
		g.setColor(Color.RED);//����̹�˵���ɫ�Ǻ�ɫ
		g.fillOval(x,y,30,30);//��̹��
		g.setColor(c);//�ָ�ǰ��ɫ
		
		y += 5;
	}
	
	@Override
	//��ȡupdate��������ͼƬһ������ʾ������������⣬updateԭ���ķ�����ˢ
	public void update(Graphics g) {
		if(offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH,GAME_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();//���õ�����
		Color c = gOffScreen.getColor();//�õ�ǰ��ɫ
		gOffScreen.setColor(FRONT_COLOR );	//����ǰ��ɫ
		gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		gOffScreen.setColor(c);//�ָ�������ɫ
		paint(gOffScreen);//��ͼƬ��������ͼƬ
		g.drawImage(offScreenImage, 0, 0, null);//������ͼƬһ��������������
	}

	public void launchFream() {
		 this.setLocation(GAME_POSITION_X,GAME_POSITION_Y);
		 this.setSize(GAME_WIDTH,GAME_HEIGHT);
		 this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			 
		});
		 this.setTitle("̹�˴�ս");//���ñ���
		 this.setBackground(FRONT_COLOR );//���ñ�����ɫ
		 this.setResizable(false);//���ò����Ըı䴰�ڴ�С
		 this.setVisible(true);//�����Ƿ�ɼ�
		 
		 new Thread(new PaintThread()).start();//��������������һ���߳���ˢ�»���
	}

	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.launchFream();
	}
	
	//����һ���߳���ˢ��(�ػ�)����
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
}
