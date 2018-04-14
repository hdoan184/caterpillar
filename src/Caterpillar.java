import uwcse.graphics.*;

import java.awt.Point;
import java.util.*;
import java.awt.Color;

/**
 * A Caterpillar is the representation and the display of a caterpillar
 */

public class Caterpillar implements CaterpillarGameConstants {
	
	private static final Color DARK_GREEN = new Color(0, 100, 0);
	
	// The body of a caterpillar is made of Points stored
	// in an ArrayList
	private ArrayList<Point> body = new ArrayList<Point>();
	
	// Store the graphical elements of the caterpillar body
	// Useful to erase the body of the caterpillar on the screen
	private ArrayList<Oval> bodyUnits = new ArrayList<Oval>();

	// The window the caterpillar belongs to
	private GWindow window;

	// Direction of motion of the caterpillar (EAST initially)
	private int dir = EAST;

	// Length of a unit of the body of the caterpillar
    // MUST be equal to the distance covered by the caterpillar
    // at every step of the animation.
    private final int bodyUnitLength = STEP;
    // Width of a unit of the body of the caterpillar
    private final int bodyUnitWidth = CATERPILLAR_WIDTH;
    
    // Initialize the cabbage status
    // It will be true if the cabbage is eaten by the caterpillar
    
    public boolean isDead = false;
    
    public int animationPeriod = ANIMATION_PERIOD;
    
    
	/**
	 * Constructs a caterpillar
	 * 
	 * @param window
	 *            the graphics where to draw the caterpillar.
	 */
	public Caterpillar(GWindow window) {
		// Initialize the graphics window for this Caterpillar
		this.window = window;

		// Create the caterpillar (10 points initially)
		// First point
		Point p = new Point();
		p.x = 5;
		p.y = WINDOW_HEIGHT / 2;
		body.add(p);

		// Other points
		for (int i = 0; i < 9; i++) {
			Point q = new Point(p);
			q.translate(STEP, 0);
			body.add(q);
			p = q;
		}

		// Other initializations (if you have more instance fields)
		// Display the caterpillar (call a private method)
		draw();
	}
	
	/**
	 * Draw the body units of the caterpillar
	 */
	public void draw(){
		Point p = (Point)body.get(0);
		
		for (int i = 1; i<body.size(); i++){
			Point q = (Point)body.get(i);
			addBodyUnit(p,q,bodyUnits.size(), DARK_GREEN);
			p=q;
		}
		body.get(body.size() - 1);
		window.doRepaint();
	}

	
    /**
     * Add a body unit to the caterpillar. The body unit
     * connects Point p and Point q.<br> 
     * Insert this body unit at position index in bodyUnits.<br>
     * e.g. 0 to insert at the tail and bodyUnits.size() to insert
     * at the head.
     */
   public void addBodyUnit(Point p, Point q, int index, Color c)
    {
        // Connect p and q with a rectangle.
        // To allow for a smooth look of the caterpillar, p and q
        // are not on the edges of the Rectangle

        // Upper left corner of the rectangle
        int x = Math.min(q.x,p.x)-bodyUnitWidth/2;
        int y = Math.min(q.y,p.y)-bodyUnitWidth/2;

        // Width and height of the rectangle (vertical or horizontal rectangle?)
        int width = ((q.y==p.y)?(bodyUnitLength+bodyUnitWidth):bodyUnitWidth);
        int height = ((q.x==p.x)?(bodyUnitLength+bodyUnitWidth):bodyUnitWidth);

        // Create the rectangle and place it in the window
        Oval r = new Oval(x, y, width, height, DARK_GREEN, true);
        window.add(r);

        // keep track of that rectangle (we will erase it at some point)
        bodyUnits.add(index,r);
    }
    
	/**
	 * Moves the caterpillar in the current direction (complete)
	 */
	public void move() {
		move(dir);
	}

	/**
	 * Move the caterpillar in the direction newDir. <br>
	 * If the new direction is illegal, select randomly a legal direction of
	 * motion and move in that direction.<br>
	 * 
	 * @param newDir
	 *            the new direction.
	 */
	public void move(int newDir){
		
		 // Is the move illegal?
        boolean isMoveNotOK;

        // newDir might not be legal
        // Before trying a random direction, try first
        // the current direction of motion (if not newDir)
        boolean isFirstTry = true;

        // move the caterpillar in direction newDir
        do{
            // new position of the head
            Point head = new Point((Point)body.get(body.size()-1));
            switch(newDir)
            {
                case NORTH:
                    head.y-=STEP;
                    break;
                case SOUTH:
                    head.y+=STEP;
                    break;
                case EAST:
                    head.x+=STEP;
                    break;
                case WEST:
                    head.x-=STEP;
                    break;
            }
            // Is the new position in the window?
            if (isPointInTheWindow(head))
            {
                isMoveNotOK=false;
                // Update the position of the caterpillar
                body.remove(0);
                body.add(head);
            }
            else
            {
                isMoveNotOK=true;
                // Select another direction
                // Try the current direction first
                if (newDir!=dir && isFirstTry)
                {
                    newDir = dir;
                    isFirstTry = false;
                }
                else // random direction
                    newDir = (int)(Math.random()*4);
            }
        }while(isMoveNotOK);

        // Update the current direction of motion
        dir = newDir;

        // Show the new location of the caterpillar
        moveCaterpillarOnScreen();
	}
	
	
    /**
     * Move the caterpillar on the screen
     */
    private void moveCaterpillarOnScreen(){
    	
        // Erase the body unit at the tail
        window.remove((Shape)bodyUnits.get(0));
        bodyUnits.remove(0);

        // Add a new body unit at the head
        Point q = (Point)body.get(body.size()-1);
        Point p = (Point)body.get(body.size()-2);
        addBodyUnit(p,q,bodyUnits.size(),DARK_GREEN);

        // show it
        window.doRepaint();
    }
    
    
    /**
     * Is Point p in the window?
     */
    private boolean isPointInTheWindow(Point p) {
        return (p.x>=0 && p.x<=WINDOW_WIDTH &&
                p.y>=0 && p.y<=WINDOW_HEIGHT  );
    }


	/**
	 * Is the caterpillar crawling over itself?
	 * 
	 * @return true if the caterpillar is crawling over itself and false
	 *         otherwise.
	 */
	public boolean isCrawlingOverItself() {
		
		for (int i =0; i < body.size()-1; i++){
			Point bodyUnitLocation = (Point)body.get(i);
			
			// Is the head point equal to any other point of the caterpillar?
			if ((getHead().x == bodyUnitLocation.x) && 
					(getHead().y == bodyUnitLocation.y)){
				return true;
			}
		}
		return false; 
	}

	/**
	 * Are all of the points of the caterpillar outside the garden
	 * 
	 * @return true if the caterpillar is outside the garden and false
	 *         otherwise.
	 */
	public boolean isOutsideGarden() {		
		// Check to see if the tail is outside the garden
		Point point = body.get(0);
		if (point.x <= 150) {
			return true;
		}
		return false;
	}

	
	/**
	 * Return the location of the head of the caterpillar (complete)
	 * 
	 * @return the location of the head of the caterpillar.
	 */
	public Point getHead() {
		return new Point((Point) body.get(body.size() - 1));
	}

	/**
	 * Increase the length of the caterpillar (by GROWTH_SPURT elements) Add the
	 * elements at the tail of the caterpillar.
	 */
	public void grow() {
		
		// Initialize the new location of growing point
		int xCoor = 0;
		int yCoor = 0;
		// Get the tail and the segment before tail of the caterpillar
		Point lastUnit = body.get(0);
		Point beforeLastUnit = body.get(1);
		
		// If they have the same X coordinate,
		// and the Y coordinate of the tail is bigger than the segment before it,
		// add the GROWTH_SPURT to the Y of the tail
		if ((lastUnit.x == beforeLastUnit.x) && (beforeLastUnit.y >= beforeLastUnit.y)){
			xCoor = lastUnit.x;
			yCoor = lastUnit.y + GROWTH_SPURT;
		} 
		// If they both have same X coordinate
		// and the Y coordinate of the tail is smaller than the segment before it,
		// subtract the GROWTH_SPURT from the Y
		else if ((lastUnit.x == beforeLastUnit.x) && (beforeLastUnit.y <= beforeLastUnit.y)){
			xCoor = lastUnit.x;
			yCoor = lastUnit.y - GROWTH_SPURT;
		}
		// If they have the same Y coordinate,
		// and the X coordinate of the tail is smaller than the segment before it,
		// then subtract the GROWTH_SPURT from the X 
		else if ((lastUnit.x <= beforeLastUnit.x) && (lastUnit.y == beforeLastUnit.y)){
			xCoor = lastUnit.x - GROWTH_SPURT;
			yCoor = lastUnit.y;
		}
		// If they have the same Y coordinate,
		// and the X coordinate of the tail is bigger than the segment before it,
		// then add the GROWTH_SPURT from the X
		else if ((lastUnit.x >= beforeLastUnit.x) && (lastUnit.y == beforeLastUnit.y)){
			xCoor = lastUnit.x + GROWTH_SPURT;
			yCoor = lastUnit.y;
		}
		
		// Add the growing part to the caterpillar
		addBodyUnit(new Point (xCoor, yCoor), lastUnit, 0, DARK_GREEN);
	}
	
	/**
	 * Change the color of the caterpillar body
	 */
	public void changeColor() {
		// Change the color of the caterpillar for a short time
		for (int i = 0; i < bodyUnits.size(); i++){
			// Use Math.random() to return the random color
			// for each unit of the caterpillar
			int red = (int)(Math.random() * 256);
			int blue = (int)(Math.random() * 256);
			int green = (int)(Math.random() * 256);
			Color col = new Color(red, blue, green);
			// Set the random color to the caterpillar		
			bodyUnits.get(i).setColor(col);
		}
	}
	
	/**
	 * Make the caterpillar goes faster by reducing the animation period.
	 */
	public void goFaster() {
		animationPeriod -= ANIMATION_PERIOD_REDUCTION;
		window.startTimerEvents(animationPeriod);
		//window.startTimerEvents(ANIMATION_PERIOD - ANIMATION_PERIOD_REDUCTION);
	}
}