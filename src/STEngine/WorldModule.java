package STEngine;

public class WorldModule extends Module {
	private Animation 		test;
	private Entity			test2;
	private int				testx;
	private int 			testy;
	
	@Override
	public void init() {
		test = new Animation(600);
		test.addFrame(new Sprite("test.gif"));
		test.addFrame(new Sprite("test2.gif"));
		test.addFrame(new Sprite("test.gif"));
		test.addFrame(new Sprite("test3.gif"));
		
		test2 = new Entity(70, 190, test);
		test2.setHorizontalMovement(75);
		
		testx = 200;
		testy = 200;
	}

	@Override
	public void step() {
		if (test2.getX() < 100) {
			test2.setHorizontalMovement(75);
		} else if (test2.getX() > 300) {
			test2.setHorizontalMovement(-75);
		}
	}

	@Override
	public void render(long delta) {
		test.draw(testx, testy, delta);
		test2.move(delta);
		test2.draw();
	}
}
