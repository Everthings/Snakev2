import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;


public class Obstacle extends Square{

	Obstacle(int x, int y, Color c, String s) {
		super(x, y, c, s);

		this.sWidth = 4;
		this.sHeight = 4;
	}
}
