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

package com.stonetolb.engine.component.render;

import java.util.HashMap;
import java.util.Map;

import com.artemis.Component;
import com.stonetolb.engine.profiles.WorldProfile.MovementContext;
import com.stonetolb.graphics.Drawable;
import com.stonetolb.graphics.NullDrawable;

public class SpriteControl extends Component {
	private final Map<MovementContext, Drawable> actionMapping;
	private Drawable noOp;
	
	/**
	 * Creates a SpriteControl using a null drawable as the defaul NoOp
	 */
	public SpriteControl() {
		this(NullDrawable.getInstance());
	}
	
	/**
	 * Creates a new SpriteControl using the designated drawable as the
	 * NoOp (no operation) drawable
	 * @param pNoOp
	 */
	public SpriteControl(Drawable pNoOp) {
		noOp = pNoOp;
		actionMapping = new HashMap<MovementContext, Drawable>();
	}
	
	/**
	 * Adds a mapping of movement to drawable
	 * 
	 * @param pMovement
	 * @param pDrawable
	 * @return this SpriteControl for command chaining
	 */
	public SpriteControl addAction(MovementContext pMovement, Drawable pDrawable) {
		actionMapping.put(pMovement, pDrawable);
		return this;
	}
	
	/**
	 * Returns the drawable keyed by the movement context. If the 
	 * map does not contain the key, then which ever drawable is 
	 * set as the noOp will be returned.
	 * @param pMovement
	 * @return
	 */
	public Drawable getDrawable(MovementContext pMovement) {
		Drawable returnVal;
		returnVal = actionMapping.get(pMovement);
		if (returnVal == null) {
			returnVal = noOp;
		}
		
		return returnVal;
	}
	
	/**
	 * Sets the NoOp (no operation) sprite for this control
	 * @param pDrawable
	 * @return this SpriteControl for command chaining
	 */
	public SpriteControl setNoOp(Drawable pDrawable) {
		noOp = pDrawable;
		return this;
	}
}
