import java.awt.Color;
import java.awt.Point;
import uwcse.graphics.*;

/**
 * A class that represents the psychedelic cabbage.
 * 
 * @author Hien Doan 
 */
public class MultiColorCabbage extends Cabbage{

	// The 4 circles that made up psychedelic cabbage
	private Oval multiColCab1;
	private Oval multiColCab2;
	private Oval multiColCab3;
	private Oval multiColCab4;
	
	// Display the cabbage in the graphic window
	public MultiColorCabbage(GWindow window, Point center) {
		super(window, center);
		draw();
	}
	
	/**
	 * Method to draw the multicolor cabbage
	 */
	public void draw(){

		// draw the first circle
		multiColCab1 = new Oval(center.x - CABBAGE_RADIUS, center.y - CABBAGE_RADIUS,
				CABBAGE_RADIUS * 2, CABBAGE_RADIUS * 2, Color.BLUE, true);	
		
		// draw the second circle that smaller than the first one
		multiColCab2 = new Oval(center.x - CABBAGE_RADIUS + 3, center.y - CABBAGE_RADIUS + 3,
				CABBAGE_RADIUS + 4, CABBAGE_RADIUS + 4, Color.YELLOW, true);
		
		// draw the third circle that smaller than BL second one
		multiColCab3 = new Oval(center.x - CABBAGE_RADIUS + 5, center.y - CABBAGE_RADIUS + 4,
				CABBAGE_RADIUS, CABBAGE_RADIUS + 2, Color.RED, true);
		
		// draw the 4th circle that smaller than BL second one
		multiColCab4 = new Oval(center.x - CABBAGE_RADIUS / 4, center.y - CABBAGE_RADIUS / 4,
				CABBAGE_RADIUS / 2, CABBAGE_RADIUS / 2, Color.BLACK, true);
		
		// display the 4 circles
		window.add(multiColCab1);
		window.add(multiColCab2);
		window.add(multiColCab3);
		window.add(multiColCab4);
	}
	

	/**
	 * This cabbage is eaten by a caterpillar
	 * 
	 * @param cp
	 *            the caterpillar that is eating this cabbage
	 */
	public void isEatenBy(Caterpillar cp){
		// Remove all the circle made of cabbage
		window.remove(multiColCab1);
		window.remove(multiColCab2);
		window.remove(multiColCab3);	
		window.remove(multiColCab4);
		
		// Add more segments for the caterpillar
		cp.grow();
		cp.changeColor();
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