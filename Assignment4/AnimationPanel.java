import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JPanel;

/**
 * This class created a panel that plays a ball animation
 * 
 *
 */
public class AnimationPanel extends JPanel implements Runnable  {
	// Class Vars
	private static final long serialVersionUID = 3L;
	private ArrayList<Ball> ball = new ArrayList<Ball>();
	private Random rand = new Random();
	// Setup this class as a panel
	Thread t;
	
	/*
	 * Setup the animation panel
	 */
	public AnimationPanel() {
		//create the animation thread
		t = new Thread(this);
		//give the balls a ref to this
		Ball.panel = this;
		// Create balls
		for(int i = 0; i < 5; i ++) {
			ball.add(new Ball(rand.nextInt(100),rand.nextInt(100),rand.nextInt(10) + 30,rand.nextInt(360),new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat())));
		}
		setBackground(Color.WHITE);
		//Start animation thread
		t.start();
	}
	
	/*
	 * Start the animation thread
	 */
	public void start() {
		t.resume();
	}
	
	/*
	 * Stop the animation thread
	 */
	public void stop() {
		t.suspend();
	}
	
	/*
	 * This function is called on repaint() and defines what should be shown on the screen
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics g) {
		//set the component to be painted to
		super.paintComponent(g);
		
		// place and color all the balls
		for(int i = 0; i < ball.size(); i ++) {
			g.setColor(ball.get(i).color);
			g.fillOval(ball.get(i).x, ball.get(i).y, ball.get(i).radius, ball.get(i).radius);
		}
	}
	
	/*
	 * Updates this panel 
	 * @param delta the time multiplier
	 */
	public void update(float delta) {
		// Check for ball collisions
		for(int i = 0; i < ball.size(); i ++) {
			for(int j = i; j < ball.size(); j ++) {
				if(i != j) {
					ball.get(i).collide(ball.get(j));
				}
			}
		}
		//update ball positions
		for(int i = 0; i < ball.size(); i ++) {
			ball.get(i).update(1);
		}
		//update the frame
		repaint();
	}
	
	/*
	 * This acts as a main method for a separate thread
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		// 60 updates a second
		int tick = 1000/60;
		long oldMs;
		oldMs=System.currentTimeMillis();
		// While we are in the correct thread
		while (Thread.currentThread() == t)
		{
			// If an acceptable time has pased
			if (oldMs + tick < System.currentTimeMillis() )
			{
			oldMs = System.currentTimeMillis();	
				// Update the panel
				this.update(1);
			}
		}
	}
}


