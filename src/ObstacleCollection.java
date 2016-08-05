import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;


public class ObstacleCollection extends Collection{
	
	Random r = new Random();
	
	static Obstacle o;
	
	ObstacleCollection() {
		super(o);
	}
	
	@Override
	public void doSomething(Square s, Snake snake){
		if(!snake.colOff){
			snake.setToDead();
		}
	}
	
	@Override
	public void create(){
		
		int x = r.nextInt(Board.NUM_BLOCKS);
		int y = r.nextInt(Board.NUM_BLOCKS);
		
		list.add(new Obstacle(x, y, Color.GRAY, name));
	}
}
