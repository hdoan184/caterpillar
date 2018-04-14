import uwcse.graphics.*;

import java.util.*;
import java.awt.Color;
import java.awt.Point;

import javax.swing.JOptionPane;

/**
 * A CaterpillarGame displays a garden that contains good and bad cabbages and a
 * constantly moving caterpillar. The player directs the moves of the
 * caterpillar. Every time the caterpillar eats a cabbage, the caterpillar
 * grows. The player wins when all of the good cabbages are eaten and the
 * caterpillar has left the garden. The player loses if the caterpillar eats a
 * bad cabbage or crawls over itself.
 */

public class CaterpillarGame extends GWindowEventAdapter implements
		CaterpillarGameConstants
// The class inherits from GWindowEventAdapter so that it can handle key events
// (in the method keyPressed), and timer events.
// All of the code to make this class able to handle key events and perform
// some animation is already written.
{
	
	private final static Color GARDEN = new Color(124, 252, 0);
	
	// Game window
	private GWindow window;

	// The caterpillar
	private Caterpillar cp;

	// Direction of motion given by the player
	private int dirFromKeyboard;

	// Do we have a keyboard event
	private boolean isKeyboardEventNew = false;

	// The list of all the cabbages
	private ArrayList<Cabbage> cabbages;
	
	// List of the fences
	private ArrayList<Rectangle> fence;

	// is the current game over?
	private boolean gameOver;
	private String messageGameOver;

	
	/**
	 * Constructs a CaterpillarGame
	 */
	public CaterpillarGame() {
		// Create the graphics window
		window = new GWindow("Caterpillar game", WINDOW_WIDTH, WINDOW_HEIGHT);

		// Any key or timer event while the window is active is sent to this
		// CaterpillarGame
		window.addEventHandler(this);
		
		// Set up the game (fence, cabbages, caterpillar)
		initializeGame();
		
		// Display the game rules
		String gameRules = "Eat all of the NON RED food,\n"
				+ "and exit the garden.\n\n"
				+ "Do not eat the red poisonous food.\n"
				+ "Do not crawl over yourself.\n\n"
				+ "Multicolor food makes you grow\nand change color.\n"
				+ "Mushroom makes you grow \nand move faster.\n"
				+ "Red poisonous food makes you die.\n\n"
				+ "To move left, press 'J' or '4',\n"
				+ "To move right, press 'K' or '6',\n"
				+ "To move up, press 'I' or '8',\n"
				+ "To move down, press 'M' or '2'.";
		JOptionPane.showMessageDialog(null, gameRules, "Caterpillar Rules",
				JOptionPane.INFORMATION_MESSAGE);
				
		// start timer events (to do the animation)
		this.window.startTimerEvents(ANIMATION_PERIOD);
		
	}

	/**
	 * Initializes the game (draw the garden, garden fence, cabbages,
	 * caterpillar)
	 */
	private void initializeGame() {
		// Clear the window
		window.erase();

		// New game
		gameOver = false;

		// No keyboard event yet
		isKeyboardEventNew = false;

		// Background (the garden)
		window.add(new Rectangle(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, GARDEN, true));

		// Create the fence around the garden using Rectangle shape
		fence = new ArrayList<Rectangle>(5);
		fence.add(new Rectangle(FENCE_START_POINT, 0, FENCE_WIDTH, 
									WINDOW_WIDTH - FENCE_START_POINT * 2, Color.BLACK, true));
		fence.add(new Rectangle(FENCE_START_POINT, 0, WINDOW_WIDTH - FENCE_START_POINT, 
															FENCE_WIDTH, Color.BLACK, true));
		fence.add(new Rectangle(WINDOW_WIDTH - FENCE_WIDTH, 0, FENCE_WIDTH,
															WINDOW_WIDTH, Color.BLACK,true));
		fence.add(new Rectangle(FENCE_START_POINT, WINDOW_WIDTH - FENCE_WIDTH, 
								WINDOW_WIDTH - FENCE_START_POINT, FENCE_WIDTH, Color.BLACK, true));
		fence.add(new Rectangle(FENCE_START_POINT, FENCE_START_POINT * 2, FENCE_WIDTH,
										WINDOW_WIDTH - FENCE_START_POINT * 2, Color.BLACK, true));

		// Display the fences
		for(int i = 0; i < fence.size(); i++){
			window.add(fence.get(i));
		}

		// Cabbages
		cabbages = new ArrayList<Cabbage>(N_GOOD_CABBAGES + N_BAD_CABBAGES);

		// Initialize the elements of the ArrayList = cabbages
		// (they should not overlap and be in the garden) ....
		Point center = new Point();
		boolean hasCabbage = false;
		for (int i = 0; i< N_GOOD_CABBAGES + N_BAD_CABBAGES; i++) {
			center.setLocation((int) (Math.random()* 220) + 200, 
					(int) (Math.random() * 450) + 20);
			hasCabbage = true;
			while (hasCabbage) {
				center.setLocation((int) (Math.random()* 250) + 200, 
						(int) (Math.random() * 445) + 30);
				hasCabbage = hasCabbageAtThisPoint(center);
			}
			
			// Display the cabbages
			if (i < N_BAD_CABBAGES) {
				cabbages.add(new RottenCabbage(window, center));
			} else if (i < N_GOOD_CABBAGES / 2 + N_BAD_CABBAGES) {
				cabbages.add(new Mushroom(window, center));
			} else if (i < N_GOOD_CABBAGES + N_BAD_CABBAGES) {
				cabbages.add(new MultiColorCabbage(window, center));
			} 
		}

		// Create the caterpillar
		cp = new Caterpillar(window);

	}
	
	/**
	 * Check the overlapping cabbages
	 * @param p is the point 
	 * @return true if there is already a cabbage
	 */
	private boolean hasCabbageAtThisPoint(Point p) {
		
		for (Cabbage ca : cabbages) {
			Point cabbagePoint = ca.getLocation();
			
			int distance = (int) Math.sqrt(Math.pow(p.x - cabbagePoint.x, 2)
										+ Math.pow(p.y - cabbagePoint.y, 2));
			if (distance < (CABBAGE_RADIUS * 3)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Moves the caterpillar within the graphics window every ANIMATION_PERIOD
	 * milliseconds.
	 * 
	 * @param e
	 *            the timer event
	 */
	public void timerExpired(GWindowEvent e) {
		// Did we get a new direction from the user?
		// Use isKeyboardEventNew to take the event into account
		// only once
		if (isKeyboardEventNew) {
			isKeyboardEventNew = false;
			cp.move(dirFromKeyboard);}
		else 
			cp.move();

		// Is the caterpillar eating a cabbage? Is it crawling over itself?
		// Is the game over? etc...
		// (do all of these checks in a private method)...
		checkAlongGame();
		
	}
	
	/**
	 * Method to track the status of the game
	 */
	public void checkAlongGame(){
		// The caterpillar eats the cabbages
		for (int i=0; i < cabbages.size(); i++){
			Cabbage cab = (Cabbage)cabbages.get(i);
			if (cab.isPointInCabbage(cp.getHead())){
				cab.isEatenBy(cp);
				cabbages.remove(cab);
			}
		}
		
		// The caterpillar is crawling over itself
		if (cp.isCrawlingOverItself()){
			gameOver = true;
			messageGameOver = "Don't crawl over yourself!";
		}
		
		// The caterpillar is hitting the fence
		if (hitTheFence()){
			gameOver = true;
			messageGameOver = "Ouch! Don't hit the fence!";
		}
		
		// The caterpillar is eating the bad cabbage
		if(cp.isDead){
			gameOver = true;
			messageGameOver = "Don't eat the poisonous food!";
		}
		
		// If the caterpillar escapes the garden successfully
		// When all points of the caterpillar is outside the garden 
		// and it eats all good cabbages
		if((cp.isOutsideGarden()) && (cabbages.size() == N_BAD_CABBAGES)){
			gameOver = true;
			messageGameOver = "Congratulations! You win!!!";
		}
		
		// If the game is over, call the method to end the game
		if (gameOver){
			endTheGame(); 
		}
	}

	/**
	 * Check to know whether the caterpillar hit the fence or not
	 */
	public boolean hitTheFence(){
		
		// Use a loop to scan through the ArrayList of fence
		for(int i =0; i < fence.size(); i++){	
			// Scan for each fence
			Rectangle fence = (Rectangle)this.fence.get(i);
			// If the head of the caterpillar is on the fence,
			// return true
			if (cp.getHead().x >= fence.getX() &&  cp.getHead().y >= fence.getY()
					&& cp.getHead().x <= fence.getX() + fence.getWidth() 
					&& cp.getHead().y <= fence.getY() + fence.getHeight()){
				return true;
			}
		}
		return false;
	}

	/**
	 * Moves the caterpillar according to the selection of the user i: NORTH, j:
	 * WEST, k: EAST, m: SOUTH
	 * 
	 * @param e
	 *            the keyboard event
	 */
	public void keyPressed(GWindowEvent e) {
		switch (Character.toLowerCase(e.getKey())) {
		case 'i':
			dirFromKeyboard = NORTH;
			break;
		case 'j':
			dirFromKeyboard = WEST;
			break;
		case 'k':
			dirFromKeyboard = EAST;
			break;
		case 'm':
			dirFromKeyboard = SOUTH;
			break;	
		case '8':
			dirFromKeyboard = NORTH;
			break;
		case '4':
			dirFromKeyboard = WEST;
			break;
		case '6':
			dirFromKeyboard = EAST;
			break;
		case '2':
			dirFromKeyboard = SOUTH;
			break;	
		default:
			return;
		}

		// new keyboard event
		isKeyboardEventNew = true;
	}

	/**
	 * The game is over. Starts a new game or ends the application
	 */
	private void endTheGame() {
		window.stopTimerEvents();
		// messageGameOver is an instance String that
		// describes the outcome of the game that just ended
		// (e.g. congratulations! you win)
		boolean again = anotherGame(messageGameOver);
		if (again) {
			initializeGame();
			// start timer events (to do the animation)
			this.window.startTimerEvents(ANIMATION_PERIOD);
		} else {
			System.exit(0);
		}
	}
	
	
	/**
	 * Does the player want to play again?
	 */
	private boolean anotherGame(String s) {
		int choice = JOptionPane.showConfirmDialog(null, s
				+ "\nDo you want to play again?", "Game over!",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (choice == JOptionPane.YES_OPTION)
			return true;
		else
			return false;
	}
	
	
	/**
	 * Starts the application
	 */
	public static void main(String[] args) {
		new CaterpillarGame();
	}
}