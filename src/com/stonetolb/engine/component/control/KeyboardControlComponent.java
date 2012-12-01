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

package com.stonetolb.engine.component.control;

import org.lwjgl.input.Keyboard;

import com.stonetolb.engine.component.EntityComponent;

/**
 * Component that reads Keyboard data and updates the Entity's 
 * state accordingly. 
 * 
 * @author james.baiera
 *
 */
public class KeyboardControlComponent extends EntityComponent {

	private static int SPEED = 75;
	private static int STOP = 0;
	
	public KeyboardControlComponent(String pId) {
		id = pId;
	}
	
	@Override
	public void update(long delta) {
		
		if(Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			parent.setDirection(0);
			parent.setSpeed(SPEED);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			parent.setDirection(2);
			parent.setSpeed(SPEED);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			parent.setDirection(3);
			parent.setSpeed(SPEED);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			parent.setDirection(1);
			parent.setSpeed(SPEED);
		} else {
			parent.setSpeed(STOP);
		}
	}
}
