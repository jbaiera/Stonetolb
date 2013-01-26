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

import com.stonetolb.engine.Entity;
import com.stonetolb.graphics.Animation;
import com.stonetolb.graphics.NullDrawable;
import com.stonetolb.graphics.StatefulDrawable;

@Deprecated
public class AnimationRenderComponent extends RenderComponentOld {

	StatefulDrawable animation;
	
	public AnimationRenderComponent(String pId, StatefulDrawable pAnimation) {
		super(pId);
		if (pAnimation != null) {
			animation = pAnimation;
		} else {
			//Create a low impact animation
			animation = Animation.builder().addFrame(NullDrawable.getInstance(), 5000).build();
		}
	}
	
	@Override
	public void render(long delta) {
		animation.draw(
				  (int)parent.getAbsolute().x.floatValue()
				, (int)parent.getAbsolute().y.floatValue()
				, 0
				, delta
			);
	}

	@Override
	public void update(long delta) {
		//nothing
	}

	@Override
	public void setOwner(Entity pParent)
	{
		parent = pParent;
		animation.dispose();
		animation.ready();
	}
}
