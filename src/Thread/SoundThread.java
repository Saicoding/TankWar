package Thread;
import Main.MySound;

public class SoundThread implements Runnable{
	String path;
		
	public SoundThread(String path) {
		this.path = path;
	}


	public void run() {
		new MySound(path).play();
	}
}
