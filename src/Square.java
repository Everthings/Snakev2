import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;


public abstract class Square{
	Color sColor;
	Color tColor = Color.CYAN;
	
	Pair p;
	
	String s;
	
	int sHeight = 1;
	int sWidth = 1;
	
	static int length = Board.WIDTH / Board.NUM_BLOCKS;
	
	Square(int x, int y, Color sColor, String s){
		this.s = s;
		p = new Pair(x, y);
		this.sColor = sColor;
	}
	
	public void setSquareColor(Color sColor){
		this.sColor = sColor;
	}
	
	public void setTestColor(Color tColor){
		this.tColor = tColor;
	}
	
	public void setX(int x){
		p.x = x;
	}
	
	public void setY(int y){
		p.y = y;
	}
}
