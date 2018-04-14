import java.awt.Color;
import java.awt.Point;
import uwcse.graphics.*;

/**
 * A class that represents the  cabbage.
 * 
 * @author Hien Doan 
 */
public class Mushroom extends Cabbage {

	private static final Color PURPLE = new Color(148, 0, 211);
	
	// The circle that made up mushroom
	private Oval head;
	
	private Oval body;
		
	// Display the cabbage in the graphic window
	public Mushroom(GWindow window, Point center) {
		super(window, center);
		draw();
	}
	
	/** 
	 * A method to draw white cabbage
	 */
	public void draw(){
		// Use Oval shape to draw the mushroom
		head = new Oval(center.x - CABBAGE_RADIUS, center.y - CABBAGE_RADIUS,
				CABBAGE_RADIUS * 2, CABBAGE_RADIUS, PURPLE, true);		
		
		body = new Oval(center.x - CABBAGE_RADIUS/2, center.y - CABBAGE_RADIUS,
				CABBAGE_RADIUS, CABBAGE_RADIUS * 2, Color.PINK, true);		
		
		window.add(body);
		window.add(head);
	}
	
	
	/**
	 * This cabbage is eaten by a caterpillar
	 * 
	 * @param cp
	 *            the caterpillar that is eating this cabbage
	 */
	public void isEatenBy(Caterpillar cp){
		// Remove the mushroom
		window.remove(head);
		window.remove(body);
		
		// Make caterpillar goes faster
		cp.grow();
		cp.goFaster();
		
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