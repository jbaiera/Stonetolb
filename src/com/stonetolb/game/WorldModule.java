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

package com.stonetolb.game;


import org.lwjgl.input.Keyboard;

import com.stonetolb.engine.Entity;
import com.stonetolb.engine.component.movement.KeyboardMovementComponent;
import com.stonetolb.engine.component.render.ImageRenderComponent;
import com.stonetolb.render.engine.TextureLoader;
import com.stonetolb.render.entities.Actor;
import com.stonetolb.render.graphics.Animation;
import com.stonetolb.render.graphics.Animation.AnimationBuilder;
import com.stonetolb.render.graphics.NullDrawable;
import com.stonetolb.render.graphics.Sprite;
import com.stonetolb.render.graphics.Texture;
import com.stonetolb.util.Pair;

/**
 * Implementation of an overworld movement game state
 * 
 * @author james.baiera
 *
 */
public class WorldModule implements Module {
	private Actor			vaughn;
	private Texture 		sheet;
	private static int 		WIDTH = 32;
	private static int 		HEIGHT = 48;
	
	private Entity nada;
	
	@Override
	public void init() {
		// Create an old style actor:
		
		try {
			this.sheet = TextureLoader.getInstance().getTexture("sprites/Vaughn/world/Vaughn.png");
		} catch(Exception e) {
			System.out.println("BAD THINGS HAPPENED");
			e.printStackTrace();
			System.exit(1);
		}
		
		vaughn = new Actor(200,100);
		int walkInterval = 800;
		
		// Gotta make a way to procedurally generate this from a file input...
		AnimationBuilder builder = Animation.builder();
		
		vaughn.addAction("toward"
				, builder.setInterval(walkInterval)
					.addFrame(new Sprite(sheet.getSubTexture(1*WIDTH, 0*HEIGHT, WIDTH, HEIGHT)))
					.addFrame(new Sprite(sheet.getSubTexture(2*WIDTH, 0*HEIGHT, WIDTH, HEIGHT)))
					.addFrame(new Sprite(sheet.getSubTexture(3*WIDTH, 0*HEIGHT, WIDTH, HEIGHT)))
					.addFrame(new Sprite(sheet.getSubTexture(0*WIDTH, 0*HEIGHT, WIDTH, HEIGHT)))
					.build()
				);
		
		vaughn.addAction("left"
				, builder.setInterval(walkInterval)
					.addFrame(new Sprite(sheet.getSubTexture(1*WIDTH, 1*HEIGHT, WIDTH, HEIGHT)))
					.addFrame(new Sprite(sheet.getSubTexture(2*WIDTH, 1*HEIGHT, WIDTH, HEIGHT)))
					.addFrame(new Sprite(sheet.getSubTexture(3*WIDTH, 1*HEIGHT, WIDTH, HEIGHT)))
					.addFrame(new Sprite(sheet.getSubTexture(0*WIDTH, 1*HEIGHT, WIDTH, HEIGHT)))
					.build()
				);
		
		vaughn.addAction("right"
				, builder.setInterval(walkInterval)
					.addFrame(new Sprite(sheet.getSubTexture(1*WIDTH, 2*HEIGHT, WIDTH, HEIGHT)))
					.addFrame(new Sprite(sheet.getSubTexture(2*WIDTH, 2*HEIGHT, WIDTH, HEIGHT)))
					.addFrame(new Sprite(sheet.getSubTexture(3*WIDTH, 2*HEIGHT, WIDTH, HEIGHT)))
					.addFrame(new Sprite(sheet.getSubTexture(0*WIDTH, 2*HEIGHT, WIDTH, HEIGHT)))
					.build()
				);
		
		vaughn.addAction("away"
				, builder.setInterval(walkInterval)
					.addFrame(new Sprite(sheet.getSubTexture(1*WIDTH, 3*HEIGHT, WIDTH, HEIGHT)))
					.addFrame(new Sprite(sheet.getSubTexture(2*WIDTH, 3*HEIGHT, WIDTH, HEIGHT)))
					.addFrame(new Sprite(sheet.getSubTexture(3*WIDTH, 3*HEIGHT, WIDTH, HEIGHT)))
					.addFrame(new Sprite(sheet.getSubTexture(0*WIDTH, 3*HEIGHT, WIDTH, HEIGHT)))
					.build()
				);
		
		vaughn.addAction("standingtoward", new Sprite(sheet.getSubTexture(0*WIDTH, 0*HEIGHT, WIDTH, HEIGHT)));
		vaughn.addAction("standingleft", new Sprite(sheet.getSubTexture(0*WIDTH, 1*HEIGHT, WIDTH, HEIGHT)));
		vaughn.addAction("standingright", new Sprite(sheet.getSubTexture(0*WIDTH, 2*HEIGHT, WIDTH, HEIGHT)));
		vaughn.addAction("standingaway", new Sprite(sheet.getSubTexture(0*WIDTH, 3*HEIGHT, WIDTH, HEIGHT)));
		
		vaughn.setAction("standingtoward");
		vaughn.setVerticalMovement(0);
		vaughn.setHorizontalMovement(0);
		
		//Create an entity with new Entity Engine
		nada = new Entity("nada");
		nada.addComponent(
				new ImageRenderComponent(
						  "NullImage"
						, new NullDrawable()
						)
				);
		nada.addComponent(new KeyboardMovementComponent("Arrows"));
		nada.setPosition(new Pair<Float,Float>(300F,300F));
		
	}

	@Override
	public void step() {
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			vaughn.setVerticalMovement(-75);
			vaughn.setHorizontalMovement(0);
			vaughn.setAction("away");
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			vaughn.setVerticalMovement(75);
			vaughn.setHorizontalMovement(0);
			vaughn.setAction("toward");
		} else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			vaughn.setVerticalMovement(0);
			vaughn.setHorizontalMovement(-75);
			vaughn.setAction("left");
		} else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			vaughn.setVerticalMovement(0);
			vaughn.setHorizontalMovement(75);
			vaughn.setAction("right");
		} else if (Keyboard.isKeyDown(Keyboard.KEY_N)) {
			vaughn.setVerticalMovement(0);
			vaughn.setHorizontalMovement(0);
			vaughn.setAction(Actor.EMPTY);
		} else {
			vaughn.setVerticalMovement(0);
			vaughn.setHorizontalMovement(0);
			if(vaughn.getDrawingName() == "toward") {
				vaughn.setAction("standingtoward");
			} else if (vaughn.getDrawingName() == "away") {
				vaughn.setAction("standingaway");
			} else if (vaughn.getDrawingName() == "left") {
				vaughn.setAction("standingleft");
			} else if (vaughn.getDrawingName() == "right") {
				vaughn.setAction("standingright");
			}
		}
	}

	@Override
	public void render(long delta) {
		vaughn.move(delta);
		vaughn.render(delta);
		nada.update(delta);
		nada.render(delta);
	}
}
