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

package com.stonetolb.render;

import org.lwjgl.opengl.GL11;

import com.stonetolb.game.Game;
import com.stonetolb.util.Pair;
import com.stonetolb.util.Vector2f;


public final class FixedVantage implements Vantage{
	
	private static volatile FixedVantage INSTANCE = null;
	private static Pair<Float, Float> ORIGIN = new Pair<Float, Float>(0F, 0F);
	
	private Pair<Float, Float> position;
	private int screenWidth;
	private int screenHeight;
	
	public static FixedVantage create() {
		if (INSTANCE == null) {
			synchronized(FixedVantage.class) {
				if (INSTANCE == null) {
					INSTANCE = new FixedVantage(
							  Game.getGame().getWindowWidth()
							, Game.getGame().getWindowHeight()
							);
				}
			}
		}
		return INSTANCE;
	}
	
	private FixedVantage(int pWidth, int pHeight) {
		screenWidth = pWidth;
		screenHeight = pHeight;
		position = ORIGIN;
	}
	
	@Override
	public void updatePosition(Vector2f target) {
		position.x = target.getX() - ((float) screenWidth / 2F);
		position.y = target.getY() - ((float) screenHeight / 2F);
	}
	
	@Override
	public void setPosition(Vector2f target) {
		updatePosition(target);
	}
	
	@Override
	public void update(long delta) {
		//TODO : Switch to Vector2f positioning and use Camera move command.
		moveCamera();
	}
	
	private void moveCamera() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(
				  position.x.doubleValue()
				, position.x.doubleValue() + (double)screenWidth
				, position.y.doubleValue() + (double)screenHeight
				, position.y.doubleValue()
				, 2000
				, 2000 * -1
			);
		
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
}
