/*
 * 生成一个弹窗
 */
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TankClient extends Frame{//通过继承Frame 可以添加自己的成员变量和方法，建议用这种方式
	
	public void launchFream() {
		 this.setLocation(0,455);
		 this.setSize(800,600);
		 this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			 
		});
		 this.setTitle("坦克大战");//设置标题
		 this.setBackground(new Color(127,255,0));//设置背景颜色
		 this.setResizable(false);//设置不可以改变窗口大小
		 this.setVisible(true);//设置是否可见
		 
	}

	@Override
	public void paint(Graphics g) {//覆盖window的paint方法
		Color c = g.getColor();//得到前景色
		g.setColor(Color.RED);//设置坦克的颜色是红色
		g.fillOval(50,50,30,30);//画坦克
		g.setColor(c);//
	}

	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.launchFream();
	}

}
