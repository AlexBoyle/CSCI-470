import java.awt.Color;

import javax.swing.JPanel;

/**
 * This Class contains everything needed to create a ball
 *
 */
public class Ball {
	//Ball vars
	public Color color;
	public int radius;
	public int x;
	public int y;
	private int speed = 10;
	public int deg;
	public static JPanel panel;
	
	/*
	 * Creates a new ball object
	 * @params x X position of ball 
	 * 		   y Y position of ball
	 * 	       radius Radius of the ball
	 *		   deg The direction the ball is traveling
	 *		   color The color of the ball
	 */
	public Ball(int x, int y,int radius, int deg, Color color) {
		this.color = color;
		this.radius = radius;
		this.x = x;
		this.y = y;
		this.deg = deg;
	}
	
	/**
	 *  Checks for ball collisions
	 * @param ball ball to check
	 */
	public void collide(Ball ball) {
		// Get ball distance
		double dis = Math.sqrt(Math.pow((this.x - ball.x),2) + Math.pow((this.y - ball.y),2));
		// If balls are touching switch directions
		if(dis < (this.radius + ball.radius)/2) {
			int temp = this.deg;
			this.deg  = ball.deg;
			ball.deg = temp;
			update(1);
		}
	}
	
	/**
	 * Update the balls position
	 * @param delta Time multiplier
	 */
	public void update(int delta) {
		// X wall bounce
		if ( x <= 0){
			x = 0;
			 deg = -deg + 180;
			 
		 }
		// X wall bounce
		 if ( x >= panel.getWidth() - this.radius){
			 x = panel.getWidth()- this.radius;
			 deg = -deg + 180;
			 
		 }
		 // Y wall bounce
		 if ( y <= 0){
			 y = 0;
			 deg = -deg + 360;
			 
		 }
		 // Y wall bounce
		 if ( y >= panel.getHeight()- this.radius){
			 y = panel.getHeight()- this.radius;
			 deg = -deg + 360;
			 
		 }
		 // Update ball position off of deg
		 x += (int)(delta * speed * Math.cos(Math.toRadians(deg)));
		 y += (int)(delta * speed * Math.sin(Math.toRadians(deg)));
	}
}
