package Main;

import Obstacle.River;
import Obstacle.Tree;
import Obstacle.Wall;

public class CreateObstacle {
	public float x;
	public float y;
	public boolean isHorizontal;
	public int num;
	public String type;
	public TankClient tc;
	public int imgType;
	public CreateObstacle(float x, float y, boolean isHorizontal, int num, String type,TankClient tc) {
		super();
		this.x = x;
		this.y = y;
		this.num = num;
		this.type = type;
		this.tc =tc;
		this.isHorizontal = isHorizontal;
//		create();
	}
	
	public CreateObstacle(float x, float y, boolean isHorizontal, int num, String type,TankClient tc,int imgType) {
		this(x,y,isHorizontal,num,type,tc);
		this.imgType = imgType;
		create();
	}
	
	private void create() {
		int n = isHorizontal ? 1 : 0;
		int m = isHorizontal ? 0 : 1;
		switch (type){
			case "tree":
				for(int i = 0;i < num;i++) {
					Tree tree = new Tree(x+i*60*n,y+i*60*m,"img/Ê÷", tc);
					tc.trees.add(tree);
				}
				break;
			case "river":
				for(int i = 0;i < num;i++) {
					River river = new River(x+i*101*n,y+i*68*m,"river",11, tc);
					tc.rivers.add(river);
				}
				break;
			case "wall":
				for(int i =0;i < num;i++) {
					//Wall(float x, float y, String imageSource, TankClient tc)
					Wall wall = new Wall(x+i*60*n,y+i*60*m,"img/wall/"+imgType, tc);
					tc.walls.add(wall);
				}
				break;
		}
	}
	
}
