import java.awt.Color;
import java.util.ArrayList;


public class SnakePiece extends Square{
	
	

	SnakePiece(int x, int y, Color c, String s) {
		super(x, y, c, s);
		
		this.sWidth = 1;
		this.sHeight = 1;
	}
}
