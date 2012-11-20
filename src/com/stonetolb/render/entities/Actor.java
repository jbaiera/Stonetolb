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
import com.stonetolb.render.graphics.NullAnimation;


public class Actor extends BaseEntity {

	private HashMap<String, Animation> actionbank = new HashMap<String,Animation>();
	
	/**
	 * Create an Actor with null animation at point x, y
	 * 
	 * @param x x coordinate of Actor
	 * @param y y coordinate of Actor
	 */
	public Actor(int x, int y) {
		super(x,y);
		this.actionbank.put("NULL_ACTION", new NullAnimation(1000));
		this.sprt = actionbank.get("NULL_ACTION");
		this.sprtstr = "NULL_ACTION";
	}
	
	/**
	 * Adds an Animation to the list of Animations the Actor has
	 */
	public void addAnimation(String s, Animation a){
		this.actionbank.put(s, a);
	}
	
	public void setAnimation(String s){
		//Only set the Animation if it's something different
		//   than what's already set.
		if (s != sprtstr) {
			//stop the current Animation
			this.sprt.stop();
			//Try to get the new one
			this.sprt = actionbank.get(s);
			//Set the name of the current Animation
			this.sprtstr = s;
			//Check to see if the animation was in there.
			//If not, make it a null sprite
			if (this.sprt == null) {
				//This should never recursively loop because it sets
				//  sprt to be a Null Animation Object
				this.setAnimation("NULL_ACTION");
				//Let's let people know though...
				System.err.println("ERROR: In Actor.setAnimation(\"" + s + "\") : \n" +
					"Could not find Animation mapped to \"" + s + "\" /n" +
					"Switching to NullAnimation...");
			}
			// Aaaaaaand let's make sure to start the sprite
			this.sprt.start();
		}
	}
}
