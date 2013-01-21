package com.stonetolb;

/**
 * Main entry point for the game Stonetolb.
 * 
 * @author james.baiera
 *
 */
public class Stonetolb {

	public static void main(String[] args) {
		System.out.println("Use -fullscreen for fullscreen mode");
		Game.createGame("Stonetolb 0.0.5"
				, 800
				, 600
				, "com.stonetolb.game.module.WorldModule"
				, (args.length > 0 && "-fullscreen".equalsIgnoreCase(args[0]))
				).execute();
		System.exit(0);
	}

}
