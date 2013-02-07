package com.stonetolb;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

import com.stonetolb.game.Game;
import com.stonetolb.game.GeneralGameException;

/**
 * Main entry point for the Stonetolb application.
 * Parses command line arguments, constructs the Game
 * object, then executes it.
 * 
 * @author james.baiera
 *
 */
public class Stonetolb {
	private static final char OPT_SCREEN_WIDTH_CODE = 'x';
	private static final char OPT_SCREEN_HEIGHT_CODE = 'y';
	private static final char OPT_MODULE_CODE = 'm';
	private static final char OPT_FULLSCREEN_CODE = 'f';
	private static final char OPT_HELP_CODE = 'h';
	
	private static final String APP_NAME = "Stonetolb";
	private static final String VERSION = "0.0.5";
	private static final String USAGE = "stonetolb -m <module> [-xyhf]";
	
	/**
	 * Main entry point.
	 * @param args - Command line arguments.
	 * @throws GeneralGameException On irrecoverable application failure.
	 */
	public static void main(String[] args)
	throws GeneralGameException
	{
		Options options = getOptions();
		CommandLineParser optionParser = new PosixParser();
		CommandLine cmd = null;
		
		try {
			cmd = optionParser.parse(options, args);
		} catch (org.apache.commons.cli.ParseException pe) {
			usage(pe.getMessage(), options);
			System.exit(1);
		}
		
		int screenWidth = 800;
		int screenHeight = 600;
		String module = null;
		boolean fullscreen = false;
		
		try {
			if(cmd.hasOption(OPT_HELP_CODE)) {
				printHelp(options);
				System.exit(0);
			}
			
			if(cmd.hasOption(OPT_SCREEN_WIDTH_CODE)) {
				try {
					screenWidth = Integer.parseInt(cmd.getOptionValue(OPT_SCREEN_WIDTH_CODE));
				} catch (NumberFormatException nfe) {
					usage(nfe.getMessage(), options);
					System.exit(1);
				}
			}
			
			if(cmd.hasOption(OPT_SCREEN_HEIGHT_CODE)) {
				try {
					screenHeight = Integer.parseInt(cmd.getOptionValue(OPT_SCREEN_HEIGHT_CODE));
				} catch (NumberFormatException nfe) {
					usage(nfe.getMessage(), options);
					System.exit(1);
				}
			}
			
			if(cmd.hasOption(OPT_MODULE_CODE)) {
				module = cmd.getOptionValue(OPT_MODULE_CODE);
			} else {
				usage("No module specified!", options);
				System.exit(1);
			}
			
			if(cmd.hasOption(OPT_FULLSCREEN_CODE)) {
				fullscreen = true;
			}
			
		} catch (IllegalArgumentException iae) {
			usage(iae.getMessage(), options);
			throw iae;
		}
		
		Game.createGame(APP_NAME + " " + VERSION
				, screenWidth
				, screenHeight
				, module
				, fullscreen
				).execute();
		System.exit(0);
	}
	
	/**
	 * Prints the usage text to system out.
	 * @param message - Message to display.
	 * @param opts - {@link Options} object to base help on.
	 */
	public static void usage(String message, Options opts) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(USAGE, message, opts, "");
	}
	
	/**
	 * Prints the help text to system out.
	 * @param opts - {@link Options} object to base help on.
	 */
	public static void printHelp(Options opts) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(USAGE, opts);
	}

	/**
	 * Constructs the {@link Options} for this application.
	 * @return {@link Options} for the Stonetolb application.
	 */
	public static Options getOptions() {
		Options options = new Options();
		
		options.addOption(new Option(OPT_SCREEN_WIDTH_CODE+"", "screenWidth", true, "Sets the width of the screen in pixels"));
		options.addOption(new Option(OPT_SCREEN_HEIGHT_CODE+"", "screenHeight", true, "Sets the height of the screen in pixels"));
		options.addOption(new Option(OPT_MODULE_CODE+"", "module", true, "Sets the module object to load on game execution"));
		options.addOption(new Option(OPT_FULLSCREEN_CODE+"", "fullscreen", false, "Toggles fullscreen if capable"));
		options.addOption(new Option(OPT_HELP_CODE+"", "help", false, "Displays this help screen"));
		
		return options;
	}
}
