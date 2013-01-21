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

import com.artemis.Component;
import com.stonetolb.render.Camera;

/**
 * Component used to attach a Camera object to a specific Entity
 * instance. Camera's are not moved by hand, they are attached to
 * an Entity with this component and a {@link Position} component
 * and their position is updated via system call.
 * 
 * @author james.baiera
 *
 */
public class CameraMount extends Component {
	private float xOffset;
	private float yOffset;
	
	public CameraMount(float pXOffset, float pYOffset) {
		xOffset = pXOffset;
		yOffset = pYOffset;
	}
	
	public void activate() {
		Camera.getCamera().attachTo(this);
	}
	
	public void deactivate() {
		Camera cam = Camera.getCamera();
		if (cam.isAttachedTo(this)) {
			cam.detach();
		}
	}
	
	public float getXOffset() {
		return xOffset;
	}
	
	public float getYOffset() {
		return yOffset;
	}
	
	public void setXOffset(float pXOffset) {
		xOffset = pXOffset;
	}
	
	public void setYOffset(float pYOffset) {
		yOffset = pYOffset;
	}
}
