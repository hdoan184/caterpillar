import java.awt.Color;
import java.awt.Point;
import uwcse.graphics.*;

/**
 * A class that represents the red poisonous cabbage.
 * 
 * @author Hien Doan 
 */
public class RottenCabbage extends Cabbage {
		
	private static final Color DARK_RED = new Color(220, 20, 60);
	
	// The circle that made up red cabbage
	private Oval redCabbage;
	
	// The leaf of the cabbage
	private Oval leaf;
	
	// Display the cabbage in the graphic window

	public RottenCabbage(GWindow window, Point center) {
		super(window, center);
		draw();
	}

	/**
	 * A method to draw the red cabbage
	 */
	public void draw(){
		
		// Use Oval shape to draw the cabbage
		redCabbage = new Oval(center.x - CABBAGE_RADIUS, center.y - CABBAGE_RADIUS,
				CABBAGE_RADIUS * 2, CABBAGE_RADIUS * 2, DARK_RED, true);
		
		leaf = new Oval(center.x - CABBAGE_RADIUS / 2, center.y - CABBAGE_RADIUS / 2,
				CABBAGE_RADIUS - 2, CABBAGE_RADIUS - 4, Color.BLACK, true);
		
		window.add(redCabbage);
		window.add(leaf);
	}
	
	
	/**
	 * This cabbage is eaten by a caterpillar
	 * 
	 * @param cp
	 *            the caterpillar that is eating this cabbage
	 */
	public void isEatenBy(Caterpillar cp) {
		// remove the cabbage from window
		window.remove(redCabbage);
		window.remove(leaf);
		
		// Because the caterpillar eats poisonous cabbage, it is dead
		cp.isDead = true;
	}

	/**
	 * Is this Point in this Cabbage?
	 * 
	 * @param p
	 *            the Point to check
	 * @return true if p in within the cabbage and false otherwise.
	 */
	public boolean isPointInCabbage(Point p) {
		return (p.distance(center) <= CABBAGE_RADIUS);
	}

	/**
	 * Returns the location of this Cabbage
	 * 
	 * @return the location of this Cabbage.
	 */
	public Point getLocation() {
		return new Point(center);
	}
}
