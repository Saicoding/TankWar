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
		//���ӵ�
		m.hitTanks(tc.enemyTanks);//���Դ����
	}
}
