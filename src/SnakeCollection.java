import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;


public class SnakeCollection extends Collection{
	
	Boolean anyDead = false;
	
	long timeOfDeath;
	
	ArrayList<Snake> sList = new ArrayList<Snake>();
	
	SnakeCollection() {
		
	}
	
	public void createPieces(String str, int num){
		getSnake(str).createNewPieces(num);
	}
	
	public void add(int x, int y, String str){// never call this from another class, only overridden by children
		sList.add(new Snake(x, y, str));
	}
	
	public void remove(String name){// removes first snake pbject found with the string
		
		Snake s = getSnake(name);
		
		if(s != null){
			sList.remove(s);
		}
	}
	
	public void removeAll(){
		while(!sList.isEmpty()){
			sList.remove(0);
		}
	}
	
	public void resetSnakes(){
		for(Snake s: sList){
			s.resetSnake();
		}
	}
	
	public void removeSnakePieces(Snake s){
		while(!s.list.isEmpty()){
			s.list.remove(0);
		}
	}
	
	public void removeAllPieces(){
		for(Snake s: sList){
			s.removeAllSnakePieces();
		}
	}
	
	public void activateSnakes(){
		for(Snake s: sList){
			s.start();
		}
	}
	
	public void drawSnakes(Graphics g){
		for(int a = 0; a < sList.size(); a++){
			for(int b = sList.get(a).list.size() - 1; b >= 0; b--){
				g.setColor(sList.get(a).list.get(b).sColor);
				g.fillRect(sList.get(a).list.get(b).p.x * Square.length, sList.get(a).list.get(b).p.y * Square.length, Square.length * sList.get(a).list.get(b).sWidth, Square.length * sList.get(a).list.get(b).sHeight);
			}
		}
	}
	
	public void drawStrings(Graphics g){
		
		for(int a = 0; a < sList.size(); a++){
			g.setColor(Color.CYAN);
			g.drawString(sList.get(a).name, sList.get(a).list.get(0).p.x * Square.length - Square.length, sList.get(a).list.get(0).p.y * Square.length - Square.length);
		}
	}
	
	public void drawAll(Graphics g){
		drawSnakes(g);
		drawStrings(g);
	}
	
	public void intersectOtherSnake(Snake s1, Snake s2){//checks if s1 collides with s2 Note: not vise versa
		if(!s1.colOff && !s1.dead){
			for(int i = 0; i < s2.list.size(); i++){
				if(s1.list.get(0).p.x == s2.list.get(i).p.x && s1.list.get(0).p.y == s2.list.get(i).p.y){
					s1.timeOfDeath = (int) System.currentTimeMillis();
					s1.dead = true;
					s1.labelDeadSquare(s1.list.get(0));
				}
			}
		}
	}
	
	public void areSnakesDead(){
		
		anyDead = false;
		
		for(Snake s: sList){
			if(s.dead){
				anyDead = true;
				timeOfDeath = s.timeOfDeath;
				break;
			}
		}
	}
	
	public void SnakeCollisions(){
		for(int i = 0; i < sList.size(); i++){
			for(int j = sList.size() - 1; j > i; j--){
				intersectOtherSnake(sList.get(i), sList.get(j));
				intersectOtherSnake(sList.get(j), sList.get(i));
			}
		}
	}
	
	public void resetSnakeCollection(){
		removeAllPieces();
		resetSnakes();
		anyDead = false;
	}
	
	public void giveInvincibilityToUndead(){
		if(anyDead){
			for(Snake s: sList){
				if(!s.dead){
					s.giveInvincibility();
				}
			}
		}
	}
	
	public Snake getSnake(String name){
		
		for(Snake s: sList){
			
			if(s.name.equals(name)){
				return s;
			}
		}
		
		return null;
	}
}
