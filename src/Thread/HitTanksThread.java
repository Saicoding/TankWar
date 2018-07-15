package Thread;

import java.awt.Graphics;

import Main.Missile;
import Main.TankClient;



public class HitTanksThread  implements Runnable{
	TankClient tc;
	Missile m  ;
	Graphics g;
	public HitTanksThread(Missile m,TankClient tc,Graphics g) {
		this.m = m;
		this.g= g;
		this.tc = tc;
	}

	@Override
	public void run() {
		//画子弹
		m.hitTanks(tc.enemyTanks);//可以打敌人
	}
}
