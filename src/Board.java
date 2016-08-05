import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.KeyStroke;


public class Board extends JLabel implements KeyListener{
	
	final static int WIDTH = 700;
	final static int HEIGHT = 700;
	final static int NUM_BLOCKS = 70;
	
	int numPieces = 4;
	
	double boostValue = 200;
	
	Random r = new Random();
	
	static JFrame GameBoard;
	
	SnakeCollection sc = new SnakeCollection();// special collection(does not extend Collection)
	static FruitCollection f = new FruitCollection();
	static ObstacleCollection o = new ObstacleCollection();
	
	ArrayList<Collection> compiledList = new ArrayList<Collection>();
	
	long lastP1Press;
	long lastP2Press;
	long elapsedTime;
	
	boolean p1KeyPressed = false;//player 1 wasd, player 2 arrows
	boolean p2KeyPressed = false;
	
	public Board(){
		
		GameBoard = new JFrame("Snake");
		GameBoard.setLocationRelativeTo(null);
		GameBoard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GameBoard.pack();
        GameBoard.setResizable(false);
        GameBoard.setLocationByPlatform(true);
        GameBoard.setSize(WIDTH, HEIGHT + 22);
        GameBoard.setVisible(true);  
        GameBoard.add(this);
        
        compiledList.add(f);
		compiledList.add(o);
		//compiledList.add(s);
        
        this.requestFocus();
        
        sc.add(NUM_BLOCKS / 3, NUM_BLOCKS * 2 / 3, "1");
		sc.add(NUM_BLOCKS * 2 / 3, NUM_BLOCKS * 2 / 3, "2");
        
		setupKeyBindings();
		
		setupBoard();
		
		addKeyListener(this);
		
		sc.activateSnakes();
	}
	
	public void setupBoard(){
		
		sc.createPieces("1", 5);
		sc.createPieces("2", 5);
		
		f.add(4);

		o.add(r.nextInt(7) + 3);
	}
	
	public void setupKeyBindings(){
		
		InputMap inMap = this.getInputMap(JComponent.WHEN_FOCUSED);
		
		KeyStroke wKey = KeyStroke.getKeyStroke('w');
		KeyStroke aKey = KeyStroke.getKeyStroke('a');
		KeyStroke sKey = KeyStroke.getKeyStroke('s');
		KeyStroke dKey = KeyStroke.getKeyStroke('d');
		
		KeyStroke zKey = KeyStroke.getKeyStroke('z');
		
		KeyStroke slashKey = KeyStroke.getKeyStroke('/');
		
		KeyStroke upKey = KeyStroke.getKeyStroke("UP");
		KeyStroke downKey = KeyStroke.getKeyStroke("DOWN");
		KeyStroke leftKey = KeyStroke.getKeyStroke("LEFT");
		KeyStroke rightKey = KeyStroke.getKeyStroke("RIGHT");
			
		inMap.put(zKey, "changeSOneSpeed");
		inMap.put(slashKey, "changeSTwoSpeed");
		
		inMap.put(wKey, "moveSOneUp");
		inMap.put(aKey, "moveSOneLeft");
		inMap.put(sKey, "moveSOneDown");
		inMap.put(dKey, "moveSOneRight");
		
		inMap.put(upKey, "moveSTwoUp");
		inMap.put(leftKey, "moveSTwoLeft");
		inMap.put(downKey, "moveSTwoDown");
		inMap.put(rightKey, "moveSTwoRight");
		
		ActionMap actMap = this.getActionMap();
		
		actMap.put("moveSOneUp", new setDirection(Direction.Up, sc.getSnake("1")));
		actMap.put("moveSOneDown", new setDirection(Direction.Down, sc.getSnake("1")));
		actMap.put("moveSOneLeft", new setDirection(Direction.Left, sc.getSnake("1")));
		actMap.put("moveSOneRight", new setDirection(Direction.Right, sc.getSnake("1")));
		
		actMap.put("moveSTwoUp", new setDirection(Direction.Up, sc.getSnake("2")));
		actMap.put("moveSTwoDown", new setDirection(Direction.Down, sc.getSnake("2")));
		actMap.put("moveSTwoLeft", new setDirection(Direction.Left, sc.getSnake("2")));
		actMap.put("moveSTwoRight", new setDirection(Direction.Right, sc.getSnake("2")));
	}
	
	public void resetAllCollections(){
		sc.resetSnakeCollection();
		
		for(Collection c: compiledList){
			c.resetCollection();
			c.resetCollection();
		}
	}
	
	public void paintComponent(Graphics g){
		
		 g.setColor(Color.BLACK);
		 g.fillRect(0, 0, WIDTH, HEIGHT);
		 
		 if(sc.anyDead){
			 if((int)System.currentTimeMillis() - sc.timeOfDeath > 3000){
				 resetAllCollections();
				 setupBoard();
			 }
		 }
		
		for(Snake s: sc.sList){
			for(Collection c: compiledList){
				c.handleCollisions(s);
			}
		}
		
		sc.SnakeCollisions();
		sc.areSnakesDead();
		sc.giveInvincibilityToUndead();
		
		for(Collection element: compiledList){
			if(compiledList.size() > 0){
				element.drawAll(g);
			}
		}
		
		sc.drawAll(g);
		
		if(p1KeyPressed){
			if(sc.getSnake("1").boostDuration > 0){
				elapsedTime = (long)System.currentTimeMillis() - lastP1Press;
				sc.getSnake("1").boostDuration -= elapsedTime;
				lastP1Press = (long)System.currentTimeMillis();
			}else{
				p1KeyPressed = false;
				sc.getSnake("1").resetBlockRate();
			}
		}
		
		if(p2KeyPressed){
			if(sc.getSnake("2").boostDuration > 0){
				elapsedTime = (long)System.currentTimeMillis() - lastP2Press;
				sc.getSnake("2").boostDuration -= elapsedTime;
				lastP2Press = (long)System.currentTimeMillis();
			}else{
				p2KeyPressed = false;
				sc.getSnake("2").resetBlockRate();
			}
		}
	}
	
	public class setDirection extends AbstractAction{
		
		Direction d;
		
		Snake s;
		
		public setDirection(Direction d, Snake s){
			this.d = d;
			this.s = s;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			switch(d){
			case Up:
				if(s.d != Direction.Down)
					s.d = Direction.Up;
				break;
			case Down:
				if(s.d != Direction.Up)
					s.d = Direction.Down;
				break;
			case Left:
				if(s.d != Direction.Right)
					s.d = Direction.Left;
				break;
			case Right:
				if(s.d != Direction.Left)
					s.d = Direction.Right;
				break;
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		if(e.getKeyCode() == KeyEvent.VK_F){
			sc.getSnake("1").changeBlockRate(boostValue);
			p1KeyPressed = true;
			lastP1Press = System.currentTimeMillis();
		}else if(e.getKeyCode() == KeyEvent.VK_M){
			sc.getSnake("2").changeBlockRate(boostValue);
			p2KeyPressed = true;
			lastP2Press = System.currentTimeMillis();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_F){
			sc.getSnake("1").resetBlockRate();
			p1KeyPressed = false;
		}else if(e.getKeyCode() == KeyEvent.VK_M){
			sc.getSnake("2").resetBlockRate();
			p2KeyPressed = false;
		}
	}
}
