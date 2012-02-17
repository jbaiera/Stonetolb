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

package STEngine;

public class WorldModule extends Module {
	private Animation 		animation;
	private Actor			vaughn;
	private Texture 		sheet;
	
	@Override
	public void init() {
		try {
			this.sheet = TextureLoader.getInstance().getTexture("sprites/Vaughn/world/Vaughn.png");
		} catch(Exception e) {
			System.out.println("BAD THINGS HAPPENED");
			e.printStackTrace();
			System.exit(1);
		}
		
		sheet.setXSections(4);
		sheet.setYSections(4);
		vaughn = new Actor(200,100);
		int walkInterval = 800;
		
		animation = new Animation(walkInterval);
		for(int i = 0; i < 4; i++) {
			animation.addFrame(new Sprite(sheet.getSubTexture(i, 0)));
		}
		vaughn.addAnimation("toward",animation);
		
		animation = new Animation(walkInterval);
		for(int i = 0; i < 4; i++) {
			animation.addFrame(new Sprite(sheet.getSubTexture(i, 3)));
		}
		vaughn.addAnimation("away", animation);
		
		vaughn.setAnimation("toward");
		vaughn.setVerticalMovement(65);
	}

	@Override
	public void step() {
		if (vaughn.getY() < 100) {
			vaughn.setVerticalMovement(65);
			vaughn.setAnimation("toward");
		} else if (vaughn.getY() > 300) {
			vaughn.setVerticalMovement(-65);
			vaughn.setAnimation("away");
		}
	}

	@Override
	public void render(long delta) {
		vaughn.move(delta);
		vaughn.draw();
	}
}
