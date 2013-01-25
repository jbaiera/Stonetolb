package com.stonetolb.gui.test;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import de.matthiasmann.twl.theme.ThemeManager;

public class GameUIDemo extends Widget {

	public boolean quit = false;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.create();
			Display.setTitle("TWL Game UI Demo");
			Display.setVSyncEnabled(true);
			
			LWJGLRenderer renderer = new LWJGLRenderer();
			GameUIDemo gameUI = new GameUIDemo();
			GUI gui = new GUI(gameUI, renderer);
			
			ThemeManager theme = ThemeManager.createThemeManager(
					GameUIDemo.class.getResource("gameui.xml"), renderer);
			gui.applyTheme(theme);
			
			while(!Display.isCloseRequested() && !gameUI.quit) {
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
				
				gui.update();
				Display.update();
				
				GL11.glGetError();          // this call will burn the time between vsyncs
		        Display.processMessages();  // process new native messages since Display.update();
		        Mouse.poll();               // now update Mouse events
		        Keyboard.poll();            // and Keyboard too
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Display.destroy();
	}

}
