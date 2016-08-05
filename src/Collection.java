import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;


public abstract class Collection {
	
	ArrayList<Square> list = new ArrayList<Square>();
	
	String name = " ";
	
	Collection(){
		
	}
	
	Collection(Square s){
		
	}
	
	public void resetCollection(){
		removeAll();
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void add(int num){
		for(int i = 0; i < num; i++){
			create();
		}
	}
	
	abstract void create();
	
	public void remove(Square s){
		list.remove(s);
	}
	
	public void removeAll(){
		while(!list.isEmpty()){
			list.remove(0);
		}
	}
	
	public void drawString(Graphics g, Square square){
		g.setColor(Color.CYAN);
		g.drawString(square.s, square.p.x * Square.length - Square.length, square.p.y * Square.length - Square.length);
	}
	
	public void drawSquare(Graphics g, Square square){
		g.setColor(square.sColor);
		g.fillRect(square.p.x * Square.length, square.p.y * Square.length, Square.length * square.sWidth, Square.length * square.sHeight);
	}
	
	public void drawAll(Graphics g){
		for(int i = list.size() - 1; i >= 0; i--){
			drawSquare(g, list.get(i));
			drawString(g, list.get(i));
		}
	}
	
	public Square getHitSquare(Snake snake){
		for(Square s: list){
			if(isHit(s, snake)){
				return s;
			}
		}
		return null;
	}
	
	public Boolean isHit(Square s, Snake snake){

		for(int a = 0; a < s.sWidth; a++){
			for(int b = 0; b < s.sHeight; b++){
				if(snake.list.get(0).p.x == s.p.x + a && snake.list.get(0).p.y == s.p.y + b){
					return true;
				}
			}
		}
		
		return false;
	}
	
	public void handleCollisions(Snake snake){
		
		Square s = getHitSquare(snake);
		
		if(s != null){
			doSomething(s, snake);
		}
	}
	
	abstract void doSomething(Square s, Snake snake);
}
