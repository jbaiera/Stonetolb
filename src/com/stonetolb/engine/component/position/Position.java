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

package com.stonetolb.engine.component.position;

import com.artemis.Component;

/**
 * Component used to represent an Entity's location in space.
 * 
 * @author james.baiera
 *
 */
public class Position extends Component{
	private float xpos;
	private float ypos;
	
	public Position(float pXpos, float pYpos) {
		xpos = pXpos;
		ypos = pYpos;
	}
	
	public float getX() {
		return xpos;
	}
	
	public void setX(float pNew) {
		xpos = pNew;
	}
	
	public float getY() {
		return ypos;
	}
	
	public void setY(float pNew) {
		ypos = pNew;
	}
	
	public void setPosition(Position other) {
		xpos = other.xpos;
		ypos = other.ypos;
	}
	
	public void setPosition(float x, float y) {
		xpos = x;
		ypos = y;
	}
}
