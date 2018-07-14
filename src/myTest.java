import java.awt.Frame;
import java.awt.Point;

public class myTest extends Frame{

	public static void main(String[] args) {
		MyPoint m = new MyPoint();
		MyPoint n = new MyPoint(3,4);
		m = n;
		System.out.println(m.x);
	}
}
