import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;


public class FruitCollection extends Collection{
	
	Random r = new Random();
	
	static Fruit f;
	
	FruitCollection(){
		super(f);
	}
	
	@Override
	public void doSomething(Square s, Snake snake){
		snake.createNewPieces(3);
		this.remove(s);
		this.add(1);
	}
	
	@Override
	public void create(){
		list.add(new Fruit(r.nextInt(Board.NUM_BLOCKS), r.nextInt(Board.NUM_BLOCKS), Color.RED, name));
	}
}
