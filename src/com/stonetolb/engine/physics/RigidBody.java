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

package com.stonetolb.engine.physics;

import java.awt.Rectangle;

/**
 * RigidBody is an object that contains a location and a bounding box.
 * @author james.baiera
 * @deprecated Use {@link AxisAlignedBoundingBox} instead.
 */
@Deprecated
class RigidBody {
	protected CollisionEvent event;
	protected Rectangle bounds;
	protected boolean inUse;
	
	public RigidBody(Rectangle pBounds, CollisionEvent pEvent) {
		event = pEvent;
		bounds = pBounds;
		inUse = true;
	}
	
	public RigidBody() {
		event = null;
		bounds = null;
		inUse = false;
	}
	
	public void init(Rectangle pBounds, CollisionEvent pEvent) {
		if(!inUse && pBounds != null && pEvent != null) {
			event = pEvent;
			bounds = pBounds;
			inUse = true;
		}
	}
	
	public void clear() {
		inUse = false;
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	public void updatePosition(int x, int y) {
		if (inUse) {
			bounds.x = x;
			bounds.y = y;
		}
	}
	
	public CollisionEvent collidesWith(RigidBody pOther) {
		if(inUse && pOther.inUse) {
			if (bounds.intersects(pOther.bounds)) {
				return event;
			}
		}
		
		return null;
	}
}
