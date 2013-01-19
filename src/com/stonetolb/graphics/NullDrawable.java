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

package com.stonetolb.graphics;

import java.io.IOException;

/**
 * Null object that implements the {@link Drawable} interface.
 * Used to represent non existant resources.
 * @author james.baiera
 *
 */
public class NullDrawable implements Drawable {
	
	private static final NullDrawable INSTANCE = new NullDrawable();
	public static NullDrawable getInstance() {
		return INSTANCE;
	}
	
	private Drawable img;
	
	private NullDrawable() {
		try
		{
			img = new Sprite("null.gif");
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
			System.exit(-1);
		}
	}

	@Override
	public void draw(int x, int y, int z, long delta) {
		img.draw(x, y, z, delta);
	}
	
	@Override
	public void accept(Critic critic) {
		critic.analyze(this);
	}
}