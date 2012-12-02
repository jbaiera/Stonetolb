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

import com.stonetolb.engine.profiles.WorldProfile.MovementContext;
import com.stonetolb.graphics.NullDrawable;

/**
 * OverworldActorComponent is a rendering component used for 
 * representing characters on the overworld
 * 
 * @author james.baiera
 *
 */
public class OverworldActorComponent extends RenderComponent {

	public OverworldActorComponent(String pId){
		super(pId);
		actionMapping = new HashMap<MovementContext, ImageRenderComponent>();
		noOpAction = new ImageRenderComponent("Drawing Missing", new NullDrawable());
		currentAction = noOpAction;
	}
	
	private Map<MovementContext,ImageRenderComponent> actionMapping;
	private ImageRenderComponent currentAction;
	private ImageRenderComponent noOpAction;
	
	public void addAction(MovementContext pMovement, ImageRenderComponent pComponent)
	{
		actionMapping.put(pMovement, pComponent);
	}
	
	@Override
	public void render(long delta) {
		currentAction.render(delta);
	}

	@Override
	public void update(long delta) {
		MovementContext context = new MovementContext(parent.getDirection(), parent.getSpeed());
		ImageRenderComponent newAction;
		
		newAction = actionMapping.get(context);
		if(newAction == null)
		{
			newAction = noOpAction;
		}
		
		if(newAction != currentAction) {
			currentAction.dispose();
			newAction.ready();
			currentAction = newAction;
			currentAction.setOwner(parent);
		}
	}

}
