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
	private Animation 		test;
	private Actor			test2;
	private Entity			test3;
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

		Texture oneone = sheet.getSubTexture(1, 1);
		test3 = new Entity(200,100);
		
		test = new Animation(800);
		test.addFrame(new Sprite(oneone));
		
		test3.addAnimation("herp", test);
		
		
		/*test = new Animation(800);
		test.addFrame(new Sprite("Vaughn/world/Vaughn1.png"));
		test.addFrame(new Sprite("Vaughn/world/Vaughn2.png"));
		test.addFrame(new Sprite("Vaughn/world/Vaughn1.png"));
		test.addFrame(new Sprite("Vaughn/world/Vaughn3.png"));
		
		test2 = new Actor(200,100);
		test2.addAnimation("walk",test);
		test2.setAnimation("walk");
		test2.setVerticalMovement(65);*/
	}

	@Override
	public void step() {
		/*
		if (test2.getY() < 100) {
			test2.setVerticalMovement(65);
		} else if (test2.getY() > 300) {
			test2.setVerticalMovement(-65);
		}
		*/
	}

	@Override
	public void render(long delta) {
		test3.move(delta);
		test3.draw();
	}
}
