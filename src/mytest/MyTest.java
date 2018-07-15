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
	public static final int GAME_WIDTH = 838 ;//��Ϸ��Ļ�߶�
	public static final int GAME_HEIGHT = 864 ;//��Ϸ��Ļ���
	public static final int GAME_POSITION_X= 1075 ;//��Ϸ��Ļλ��x
	public static final int GAME_POSITION_Y= 249 ;//��Ϸ��Ļλ��y
	public static final Color FRONT_COLOR = new Color(51,51,51);//��Ϸǰ��ɫ
	
	Image offScreenImage = null;//����һ������ͼƬ��һ����չ�֣������������
	
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
		g.drawString("circle x = "+testCircle.x+" y = "+testCircle.y, 10, 50);//�ж����ڵ�����Ļ��	
		
		g2.setColor(Color.GREEN);
		g.drawString("polygon x = "+testPolygon.x+" y = "+testPolygon.y, 10, 70);//�ж����ڵ�����Ļ��	
		
		g2.setColor(Color.GREEN);
		//Բ�����
		if(testCircle.mtv1.axis != null) {
			g.drawString("Բ�����mtv axis.x= "+testCircle.mtv1.axis.x+" y = "+testCircle.mtv1.axis.y, 10, 90);//�ж����ڵ�����Ļ��	
		}
		g.drawString("Բ�����mtv overlap = "+testCircle.mtv1.overlap, 10, 110);//�ж����ڵ�����Ļ��	
		
		//Բ��Բ
		if(testCircle.mtv2.axis != null) {
			g.drawString("Բ��Բmtv axis.x= "+testCircle.mtv2.axis.x+" y = "+testCircle.mtv2.axis.y, 10, 130);//�ж����ڵ�����Ļ��	
		}
		g.drawString("Բ��Բmtv overlap = "+testCircle.mtv2.overlap, 10, 150);//�ж����ڵ�����Ļ��	
		
		//������Բ
		if(testPolygon.mtv2.axis != null) {
			g.drawString("������Բmtv axis.x= "+testPolygon.mtv2.axis.x+" y = "+testPolygon.mtv2.axis.y, 10, 170);//�ж����ڵ�����Ļ��	
		}
		g.drawString("������Բmtv overlap = "+testPolygon.mtv2.overlap, 10, 190);//�ж����ڵ�����Ļ��	
		
		//���������
		if(testPolygon.mtv1.axis != null) {
			g.drawString("���������mtv axis.x= "+testPolygon.mtv1.axis.x+" y = "+testPolygon.mtv1.axis.y, 10, 210);//�ж����ڵ�����Ļ��	
		}
		g.drawString("���������mtv overlap = "+testPolygon.mtv1.overlap, 10, 230);//�ж����ڵ�����Ļ��	
		g2.setColor(c);
	}
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
			testCircle.keyPressed(e);	
			testPolygon.keyPressed(e);
		}
	}
}

