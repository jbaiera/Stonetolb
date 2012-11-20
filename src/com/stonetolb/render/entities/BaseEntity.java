/* 
 * Copyleft (o) 2012 James Baiera
 * All wrongs reserved.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.stonetolb.render.entities;


import com.stonetolb.render.graphics.Animation;
import com.stonetolb.render.graphics.Entity;
import com.stonetolb.render.graphics.NullAnimation;

/**
 * Entity holds an animation or sprite and keeps track 
 * of movement, as well as most entity related game logic. 
 *  
 * @author comet
 */
public class BaseEntity implements Entity{
	protected float 	xPos;
	protected float 	yPos;
	protected int 	dx;
	protected int		dy;
	protected Animation	sprt;
	protected long 	thisDelta;
	protected String 	sprtstr;
	
	/**
	 * Creates a new Entity at x and y with animation as it's
	 * visual.
	 * 
	 * @param x x position
	 * @param y y position
	 * @param animation Entity visual representation
	 */
	public BaseEntity(int x, int y) {
		this.xPos = (float) x;
		this.yPos = (float) y;
		this.dx = 0;
		this.dy = 0;
		this.thisDelta = 1;
		this.sprt = new NullAnimation(1000);
		this.sprtstr = "NULL_ACTION";
	}
	
	public void addAnimation(String s, Animation anim) {
		this.sprt.stop();
		this.sprt = anim;
		this.sprtstr = s;
		this.sprt.start();
	}
	
	/**
	 * Updates the Entity's position based on how much time has
	 * passed, and the Entity's velocity
	 * 
	 * @param delta number of milliseconds passed since last move
	 */
	public void move(long delta) {
		this.thisDelta = delta;
		this.xPos += ((delta * dx) / 1000);
		this.yPos += (float)((delta * dy) / 1000);
	}
	
	/**
	 * Draws the animation based on when the last time the move method
	 * was called. 
	 */
	@Override
	public void draw(int x, int y, int z, long delta) {
		render(delta);
	}
	
	@Override
	public void render(long delta) {
		sprt.draw((int)xPos, (int)yPos, 0, delta);
	}
	
	//Setters
	
	/**
	 * Sets the horizontal movement 
	 * 
	 * @param toTheRight (pixels/second) to the right
	 */
	public void setHorizontalMovement (int toTheRight) {
		this.dx = toTheRight;
	}
	
	/** 
	 * Sets the vertical movement
	 * 
	 * @param toTheBottom (pixels/second) to the bottom
	 */
	public void setVerticalMovement(int toTheBottom) {
		this.dy = toTheBottom;
	}
	
	//Getters
	
	/**
	 * Returns the animation of an Entity
	 * 
	 * @return the name of the current Animation
	 */
	public String getAnimation() {
		return sprtstr;
	}
	
	/**
	 * Gets the X position
	 * 
	 * @return Entity X position
	 */
	public int getX() {
		return (int) xPos;
	}
	
	/**
	 * Gets the Y position
	 * 
	 * @return Entity Y position
	 */
	public int getY() {
		return (int) yPos;
	}
	
	/**
	 * Gets the rate of change of x
	 * 
	 * @return the rate of change of x
	 */
	public int getHorizontalMovement() {
		return dx;
	}
	
	/**
	 * Gets the rate of change of y
	 * 
	 * @return the rate of change of y
	 */
	public int getVerticalMovement() {
		return dy;
	}
}