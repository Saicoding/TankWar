/*
 * ����һ������
 */
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TankClient extends Frame{//ͨ���̳�Frame ��������Լ��ĳ�Ա�����ͷ��������������ַ�ʽ
	
	public void launchFream() {
		 this.setLocation(0,455);
		 this.setSize(800,600);
		 this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			 
		});
		 this.setTitle("̹�˴�ս");//���ñ���
		 this.setBackground(new Color(127,255,0));//���ñ�����ɫ
		 this.setResizable(false);//���ò����Ըı䴰�ڴ�С
		 this.setVisible(true);//�����Ƿ�ɼ�
		 
	}

	@Override
	public void paint(Graphics g) {//����window��paint����
		Color c = g.getColor();//�õ�ǰ��ɫ
		g.setColor(Color.RED);//����̹�˵���ɫ�Ǻ�ɫ
		g.fillOval(50,50,30,30);//��̹��
		g.setColor(c);//
	}

	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.launchFream();
	}

}
