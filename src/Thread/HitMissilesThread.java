package Thread;

import java.awt.Graphics;

import Main.Missile;
import Main.TankClient;

public class HitMissilesThread implements Runnable{
	TankClient tc;
	Missile m  ;
	Graphics g;
	public HitMissilesThread (Missile m,TankClient tc,Graphics g) {
		this.m = m;
		this.g= g;
		this.tc = tc;
	}

	@Override
	public void run() {
		//×Óµ¯´ò×Óµ¯ÅÐ¶Ï
		m.hitMissiles(tc.missiles);
	}
}
