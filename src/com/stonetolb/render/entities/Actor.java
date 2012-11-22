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

import java.util.HashMap;

import com.stonetolb.render.graphics.Animation;
import com.stonetolb.render.graphics.Drawable;
import com.stonetolb.render.graphics.NullDrawable;

/**
 * Actor is an extension of the BaseEntity object that is capable of storing 
 * a map of String to Drawable Objects. This can be used to change the image being drawn
 * on the screen by calling the setAction method.
 * 
 * @author james.baiera
 *
 */
public class Actor extends BaseEntity {
	public static String EMPTY = "NULL_ACTION";
	private HashMap<String, Drawable> actionbank = new HashMap<String,Drawable>();
	
	/**
	 * Create an Actor with null animation at point x, y
	 * 
	 * @param x x coordinate of Actor
	 * @param y y coordinate of Actor
	 */
	public Actor(int x, int y) {
		super(x,y);
		actionbank.put(EMPTY, new NullDrawable());
		setDrawable(actionbank.get(EMPTY), EMPTY);
	}
	
	/**
	 * Adds a new Drawable to the list of actions the Actor has
	 */
	public void addAction(String s, Drawable a){
		this.actionbank.put(s, a);
	}
	
	public void setAction(String actionName){
		//Only set the Animation if it's something different
		//   than what's already set.
		if (actionName != imageName) {
			Drawable newAction = actionbank.get(actionName);

			if (newAction == null) {
				this.setEmptyAnimation(actionName);

				System.err.println("ERROR: In Actor.setAction(\"" + actionName + "\") : \n" +
					"Could not find Drawable mapped to \"" + actionName + "\" \n" +
					"Switching to NullDrawable...");
			}
			else 
			{
				setDrawable(newAction, actionName);
			}
		}
	}
	
	private void setEmptyAnimation(String attemptedName)
	{
		Drawable nullImage = actionbank.get(EMPTY);
		if (nullImage == null)
		{
			String msg = "Attempted to set action to non existant \""+attemptedName+"\". Fallback image is also non existant.";
			throw new IllegalStateException(msg);
		}
		else
		{
			setDrawable(nullImage, EMPTY);
		}
	}
}
