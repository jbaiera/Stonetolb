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
import com.stonetolb.render.graphics.Drawable;
import com.stonetolb.render.graphics.Entity;
import com.stonetolb.render.graphics.NullDrawable;
import com.stonetolb.render.graphics.StatefulDrawable;

/**
 * The BaseEntity is a simple object that stores a single Drawable object, as well as 
 * the location and movement states. 
 *  
 * @author comet
 */
public class BaseEntity implements Entity{
	protected float 	xPosition;
	protected float 	yPosition;
	protected int 	changeInX;
	protected int		changeInY;
	protected Drawable	image;
	protected long 	movementDelta;
	protected String 	imageName;
	
	/**
	 * Creates a new BaseEntity at Location x and y, at rest, with an empty drawable
	 * as it's visual.
	 * 
	 * @param x x position
	 * @param y y position
	 * @param animation Entity visual representation
	 */
	public BaseEntity(int pX, int pY) {
		this.xPosition = (float) pX;
		this.yPosition = (float) pY;
		this.changeInX = 0;
		this.changeInY = 0;
		this.movementDelta = 1;
		this.image = new NullDrawable();
		this.imageName = "NULL_ACTION";
	}
	
	/**
	 * Updates the entity's position based on how much time has
	 * passed, and the entity's velocity
	 * 
	 * @param delta number of milliseconds passed since last move
	 */
	public void move(long delta) {
		movementDelta = delta;
		xPosition += ((delta * changeInX) / 1000);
		yPosition += (float)((delta * changeInY) / 1000);
	}
	
	/**
	 * Orders the object to draw it's Drawable image where ever it's location is.
	 */
	@Override
	public void render(long delta) {
		image.draw((int)xPosition, (int)yPosition, 0, delta);
	}
	
	//Setters
	
	public void setHorizontalMovement (int toTheRight) {
		this.changeInX = toTheRight;
	}
	
	public void setVerticalMovement(int toTheBottom) {
		this.changeInY = toTheBottom;
	}

	public void setDrawable(Drawable pDrawing, String pName) {
		if (image != null && image instanceof StatefulDrawable) 
		{
			((StatefulDrawable)image).dispose();
		}
		image = pDrawing;
		imageName = pName;
		if (image instanceof StatefulDrawable)
		{
			((StatefulDrawable)image).ready();
		}
	}
	
	public void setDrawable(StatefulDrawable pAnimation, String pName) {
		if (image != null && StatefulDrawable.class.isInstance(image)) 
		{
			((StatefulDrawable)image).dispose();
		}
		image = pAnimation;
		imageName = pName;
		((StatefulDrawable) image).ready();
	}
	
	//Getters
	
	public int getX() {
		return (int) xPosition;
	}
	
	public int getY() {
		return (int) yPosition;
	}
	
	public int getHorizontalMovement() {
		return changeInX;
	}
	
	public int getVerticalMovement() {
		return changeInY;
	}
	
	public Drawable getDrawable() {
		return image;
	}
	
	public String getDrawingName()
	{
		return imageName;
	}
}
