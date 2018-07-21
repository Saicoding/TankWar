package Obstacle;

import java.util.ArrayList;

import Main.TankClient;
import shape.MyPoint;

public class Screens {
	public ArrayList<MyScreen> screens = new ArrayList<MyScreen>();
	public TankClient tc;

	public Screens() {
	}
	
	public void getScreens(){
		MyScreen pLeft = new MyScreen(-200,TankClient.GAME_HEIGHT/2);
		pLeft.points.add(new MyPoint(-400,-200));
		pLeft.points.add(new MyPoint(0,-200));
		pLeft.points.add(new MyPoint(0,TankClient.GAME_HEIGHT+200));
		pLeft.points.add(new MyPoint(-400,TankClient.GAME_HEIGHT+200));
		
		MyScreen pTop = new MyScreen(TankClient.GAME_WIDTH/2,-200);
		pTop.points.add(new MyPoint(-200,-400));
		pTop.points.add(new MyPoint(TankClient.GAME_WIDTH+200,-400));
		pTop.points.add(new MyPoint(TankClient.GAME_WIDTH+200,20));
		pTop.points.add(new MyPoint(-200,20));
		
		MyScreen pRight = new MyScreen(TankClient.GAME_WIDTH+200,TankClient.GAME_HEIGHT/2);
		pRight.points.add(new MyPoint(TankClient.GAME_WIDTH,-200));
		pRight.points.add(new MyPoint(TankClient.GAME_WIDTH+400,-200));
		pRight.points.add(new MyPoint(TankClient.GAME_WIDTH+400,TankClient.GAME_HEIGHT+200));
		pRight.points.add(new MyPoint(TankClient.GAME_WIDTH,TankClient.GAME_HEIGHT+200));
		
		MyScreen pBottom = new MyScreen(TankClient.GAME_WIDTH/2,TankClient.GAME_HEIGHT+200);
		pBottom.points.add(new MyPoint(-200,TankClient.GAME_HEIGHT));
		pBottom.points.add(new MyPoint(TankClient.GAME_WIDTH+200,TankClient.GAME_HEIGHT));
		pBottom.points.add(new MyPoint(TankClient.GAME_WIDTH+200,TankClient.GAME_HEIGHT+400));
		pBottom.points.add(new MyPoint(-200,TankClient.GAME_HEIGHT+400));
		
		screens.add(pLeft);
		screens.add(pRight);
		screens.add(pTop);
		screens.add(pBottom);
	}
}
