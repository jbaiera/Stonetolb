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

package com.stonetolb;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

import com.stonetolb.game.Game;

/**
 * Main entry point for the game Stonetolb.
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
	
	public static void main(String[] args) {
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
	
	public static void usage(String message, Options opts) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(USAGE, message, opts, "");
	}
	
	public static void printHelp(Options opts) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(USAGE, opts);
	}

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
