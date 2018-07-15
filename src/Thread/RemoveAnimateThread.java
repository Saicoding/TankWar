package Thread;

import Main.Animate;
import Main.TankClient;

public class RemoveAnimateThread implements Runnable{
	TankClient tc ;

	public RemoveAnimateThread(TankClient tc) {
		this.tc = tc;
	}

	@Override
	public void run() {
		for(int i = 0 ;i < tc.animates.size();i++) {
			Animate a = tc.animates.get(i);
			if(a.over) tc.animates.remove(a);
			
		}
		
	}
}
