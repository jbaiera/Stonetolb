package com.stonetolb.graphics;

/**
 * Interface that should be implemented by anything that may need to determine
 * the difference between a drawable and a stateful drawable for reasons.
 * 
 * Interface name is a play on words, since it takes a critic to really understand
 * art...
 * @author james.baiera
 *
 */
public interface Critic {
	public void analyze(Drawable image);
	
	public void analyze(StatefulDrawable image);
}
