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
import com.stonetolb.graphics.Drawable;
import com.stonetolb.graphics.StatefulDrawable;

/**
 * Rendering component that uses a {@link Drawable} item as it's
 * image to represent the {@link Entity} on screen.
 * <p>
 * This rendering component is for basic image rendering of any
 * {@link Drawable} object.
 *  
 * @author comet
 *
 */
public class ImageRenderComponent extends RenderComponent {
	Drawable image;
	StatefulDrawable animation;
	boolean isStateful;
	
	public ImageRenderComponent(String pId, Drawable pImage) {
		super(pId);
		image = pImage;
		if (StatefulDrawable.class.isInstance(image)) {
			isStateful = true;
			animation = (StatefulDrawable) image;
		}
	}

	@Override
	public void update(long delta) {
		// Nothing...
	}
	
	/**
	 * If the underlying drawable is a stateful drawable, this method
	 * will allow setup of the drawing
	 */
	public void ready() {
		if (isStateful)
		{
			animation.ready();
		}
	}
	
	/**
	 * If the underlying drawable is a stateful drawable, this method
	 * will allow setup of the drawing
	 */
	public void dispose() {
		if (isStateful)
		{
			animation.dispose();
		}
	}
	
	@Override
	public void render(long delta) {
		if (isStateful)
		{
			animation.draw(
					  (int)parent.getPosition().x.floatValue()
					, (int)parent.getPosition().y.floatValue()
					, 0
					, delta
				);
		} else  {
			image.draw(
					  (int)parent.getPosition().x.floatValue()
					, (int)parent.getPosition().y.floatValue()
					, 0
					, delta
				);
		}
	}
}
