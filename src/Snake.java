import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import javax.swing.JFrame;


public class Snake implements Runnable{
	
	int boostDuration = 3000;//milliseconds
	
	int defaultBlockRate = 15;
	
	int blockRate = defaultBlockRate;
	
	ArrayList<Square> list = new ArrayList<Square>();
	
	boolean invincible = false;
	
	int invisiForMillis = 3000;
	
	String name;
	
	int timeOfDeath;
	
	int startInvisiTime;

	boolean dead = false;
	
	boolean spawnProtection = false;
	
	int startingX, startingY;
	
	Direction d = Direction.Up;
	
	boolean colOff = spawnProtection || invincible;
	
	//TimerTask task = new updateSnake();
	
	Timer t;
	
	Thread sThread = new Thread(this);
	
	int lastTime = (int)System.currentTimeMillis();
	
	Snake(int x, int y, String name) {
		t = new Timer();
		this.startingX = x;
		this.startingY = y;
		this.name = name;
	}
	
	public void start(){
		sThread.start();
	}
	
	@Override
	public void run() {
		
		startInvisiTime = (int)System.currentTimeMillis();
		
		while(true){
			if((int)System.currentTimeMillis() - lastTime > 1000/blockRate){
				lastTime = (int)System.currentTimeMillis();
				moveSnake();
			}
		}
	}
	
	public void changeBlockRate(double boostValue){
		blockRate = (int) (blockRate * (1 + (boostValue / 100.0)));
	}
	
	public void resetBlockRate(){
		blockRate = defaultBlockRate;
	}

	public void moveSnake(){
		
		checkSpawnProtection();
		
		if(!dead){
			if(list.size() > 0){
				
				Square lastSquare = list.get(list.size() - 1);
				Square firstSquare = list.get(0);
				
				setNewHead(firstSquare, lastSquare);
				
				colOff = spawnProtection || invincible;
				
				checkCollisions(lastSquare);
			}
		}
		
		Board.GameBoard.repaint();
	}
	
	public void createNewPieces(int num){
		
		for(int i = 0; i < num; i++){
		
			if(list.isEmpty()){
				list.add(new SnakePiece(startingX, startingY, Color.GREEN, name));
			}else{
				switch(d){
				 case Up:
					 list.add(new SnakePiece(list.get(list.size() - 1).p.x, list.get(list.size() - 1).p.y + 1, Color.GREEN, name));
				 	break;
				 case Down:
					 list.add(new SnakePiece(list.get(list.size() - 1).p.x, list.get(list.size() - 1).p.y - 1, Color.GREEN,  name));
					 break;
				 case Left:
					 list.add(new SnakePiece(list.get(list.size() - 1).p.x + 1, list.get(list.size() - 1).p.y, Color.GREEN, name));
					 break;
				 case Right:
					 list.add(new SnakePiece(list.get(list.size() - 1).p.x - 1, list.get(list.size() - 1).p.y, Color.GREEN, name));
					 break;
				 }
				//new SnakePiece(x, y, Color.GREEN, list);
			}
		}
	}
	
	public void setNewHead(Square firstSquare, Square lastSquare){
		
		list.remove(lastSquare);
		list.add(0, lastSquare);
		
		switch(d){
		case Up:
			//s.setX(s.p.y - 1);
			lastSquare.p.y = firstSquare.p.y - 1;
			lastSquare.p.x = firstSquare.p.x;
			break;
		case Down:
			//s.setX(s.p.y + 1);
			lastSquare.p.y = firstSquare.p.y + 1;
			lastSquare.p.x = firstSquare.p.x;
			break;
		case Left:
			//s.setX(s.p.x - 1);
			lastSquare.p.x = firstSquare.p.x - 1;
			lastSquare.p.y = firstSquare.p.y;
			break;
		case Right:
			//s.setX(s.p.x + 1);
			lastSquare.p.x = firstSquare.p.x + 1;
			lastSquare.p.y = firstSquare.p.y;
			break;
		}//last square becomes first square!		
	}
	
	public void checkCollisions(Square newFirstSquare){
		if(!colOff){
			
			for(Square square: list){
				square.setSquareColor(Color.GREEN);
			}

			checkBounds(newFirstSquare.p.x);
			checkBounds(newFirstSquare.p.y);
			//intersectOwnSnake();
			
		}else{
			for(Square square: list){
				square.setSquareColor(Color.MAGENTA);
			}
		}
	}
	
	public void checkSpawnProtection(){
		
		if((int)System.currentTimeMillis() - startInvisiTime > invisiForMillis){
			spawnProtection = false;
		}else{
			spawnProtection = true;
		}
	}
	
	public void checkBounds(int i){
		if(i < 0 || i > Board.NUM_BLOCKS - 1){
			setToDead();
		}
	}
	
	public void intersectOwnSnake(){
		for(int i = 1; i < list.size(); i++){
			if(list.get(0).p.x == list.get(i).p.x && list.get(0).p.y == list.get(i).p.y){
				setToDead();
				break;
			}
		}
	}
	
	public void setToDead(){
		timeOfDeath = (int) System.currentTimeMillis();
		dead = true;
		labelDeadSquare(list.get(0));
	}
	
	public void giveInvincibility(){
		invincible = true;
	}
	
	public void removeInvincibility(){
		invincible = false;
	}
	
	public void labelDeadSquare(Square s){
		s.sColor = Color.PINK;
	}
	
	public void removeAllSnakePieces(){
		while(list.size() > 0){
			list.remove(0);
		}
	}
	
	public void resetSnake(){
		startInvisiTime = (int)System.currentTimeMillis();
		dead = false;
		invincible = false;
		d = Direction.Up;
		boostDuration = 3000;
	}
}
