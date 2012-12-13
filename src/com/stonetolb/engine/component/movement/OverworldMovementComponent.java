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

package com.stonetolb.engine.component.movement;

import com.stonetolb.engine.component.EntityComponent;
import com.stonetolb.engine.profiles.WorldProfile.WorldDirection;

/**
 * OverworldMovementComponent takes the Entity's state and uses it to
 * extrapolate where to set the entity's new position in the world
 * 
 * @author james.baiera
 *
 */
public class OverworldMovementComponent extends EntityComponent {
	
	public OverworldMovementComponent(String pId) {
		id = pId;
	}
	
	@Override
	public void update(long delta) {
		parent.getPosition().x += (float)((((delta * (parent.getSpeed())) / 1000)) * Math.cos(Math.toRadians(parent.getDirection())));
		parent.getPosition().y += (float)((((delta * (parent.getSpeed())) / 1000)) * Math.sin(Math.toRadians(parent.getDirection())));
	}

}
