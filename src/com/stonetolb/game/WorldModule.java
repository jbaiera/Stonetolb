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

import com.stonetolb.render.engine.TextureLoader;
import com.stonetolb.render.entities.Actor;
import com.stonetolb.render.graphics.Animation;
import com.stonetolb.render.graphics.Sprite;
import com.stonetolb.render.graphics.Texture;

public class WorldModule extends Module {
	private Animation 		animation;
	private Actor			vaughn;
	private Texture 		sheet;
	private static int 		WIDTH = 32;
	private static int 		HEIGHT = 48;
	
	@Override
	public void init() {
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
		
		animation = new Animation(walkInterval);
		for(int i = 1; i < 4; i++) {
			animation.addFrame(new Sprite(sheet.getSubTexture(i*WIDTH, 0*HEIGHT, WIDTH, HEIGHT)));
		}
		animation.addFrame(new Sprite(sheet.getSubTexture(0*WIDTH, 0*HEIGHT, WIDTH, HEIGHT)));
		vaughn.addAnimation("toward",animation);
		
		animation = new Animation(walkInterval);
		for(int i = 1; i < 4; i++) {
			animation.addFrame(new Sprite(sheet.getSubTexture(i*WIDTH, 3*HEIGHT, WIDTH, HEIGHT)));
		}
		animation.addFrame(new Sprite(sheet.getSubTexture(0*WIDTH, 3*HEIGHT, WIDTH, HEIGHT)));
		vaughn.addAnimation("away", animation);
		
		animation = new Animation(walkInterval);
		for(int i = 1; i < 4; i++) {
			animation.addFrame(new Sprite(sheet.getSubTexture(i*WIDTH, 1*HEIGHT, WIDTH, HEIGHT)));
		}
		animation.addFrame(new Sprite(sheet.getSubTexture(0*WIDTH, 1*HEIGHT, WIDTH, HEIGHT)));
		vaughn.addAnimation("left",animation);
		
		animation = new Animation(walkInterval);
		for(int i = 1; i < 4; i++) {
			animation.addFrame(new Sprite(sheet.getSubTexture(i*WIDTH, 2*HEIGHT, WIDTH, HEIGHT)));
		}
		animation.addFrame(new Sprite(sheet.getSubTexture(0*WIDTH, 2*HEIGHT, WIDTH, HEIGHT)));
		vaughn.addAnimation("right",animation);
		
		animation = new Animation(walkInterval);
		animation.addFrame(new Sprite(sheet.getSubTexture(0*WIDTH, 0*HEIGHT, WIDTH, HEIGHT)));
		vaughn.addAnimation("standingtoward", animation);
		
		animation = new Animation(walkInterval);
		animation.addFrame(new Sprite(sheet.getSubTexture(0*WIDTH, 3*HEIGHT, WIDTH, HEIGHT)));
		vaughn.addAnimation("standingaway", animation);
		
		animation = new Animation(walkInterval);
		animation.addFrame(new Sprite(sheet.getSubTexture(0*WIDTH, 1*HEIGHT, WIDTH, HEIGHT)));
		vaughn.addAnimation("standingleft", animation);
		
		animation = new Animation(walkInterval);
		animation.addFrame(new Sprite(sheet.getSubTexture(0*WIDTH, 2*HEIGHT, WIDTH, HEIGHT)));
		vaughn.addAnimation("standingright", animation);
		
		vaughn.setAnimation("standingtoward");
		vaughn.setVerticalMovement(0);
		vaughn.setHorizontalMovement(0);
	}

	@Override
	public void step() {
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			vaughn.setVerticalMovement(-75);
			vaughn.setHorizontalMovement(0);
			vaughn.setAnimation("away");
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			vaughn.setVerticalMovement(75);
			vaughn.setHorizontalMovement(0);
			vaughn.setAnimation("toward");
		} else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			vaughn.setVerticalMovement(0);
			vaughn.setHorizontalMovement(-75);
			vaughn.setAnimation("left");
		} else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			vaughn.setVerticalMovement(0);
			vaughn.setHorizontalMovement(75);
			vaughn.setAnimation("right");
		} else {
			vaughn.setVerticalMovement(0);
			vaughn.setHorizontalMovement(0);
			if(vaughn.getAnimation() == "toward") {
				vaughn.setAnimation("standingtoward");
			} else if (vaughn.getAnimation() == "away") {
				vaughn.setAnimation("standingaway");
			} else if (vaughn.getAnimation() == "left") {
				vaughn.setAnimation("standingleft");
			} else if (vaughn.getAnimation() == "right") {
				vaughn.setAnimation("standingright");
			}
		}
	}

	@Override
	public void render(long delta) {
		vaughn.move(delta);
		vaughn.draw(0,0,0,delta);
	}
}
