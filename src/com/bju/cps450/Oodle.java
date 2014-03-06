/* Oodle.java
 * Author: Ethan McGee
 * Date: 2014-01-23
 * 
 * Purpose: Main class for Oodle compiler project
 */

package com.bju.cps450;
import jargs.gnu.CmdLineParser;
import jargs.gnu.CmdLineParser.IllegalOptionValueException;
import jargs.gnu.CmdLineParser.UnknownOptionException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;

import com.bju.cps450.lexer.LexerException;
import com.bju.cps450.node.Start;
import com.bju.cps450.OodleSemanticChecker;

public class Oodle
{
	/* printHelp
	 * Arguments:
	 * 
	 * Purpose: Writes a help statement to standard out
	 */
	public static void printHelp() {
		System.out.println("Oodle Compiler");
		System.out.println("v 0.1");
		System.out.println("Author: Ethan McGee");
		System.out.println("");
		System.out.println("Usage:");
		System.out.println(" java -jar oodle.jar [options] [files]");
		System.out.println("");
		System.out.println("Options:");
		System.out.println("-ds, --print-tokens");
		System.out.println("  display a list of tokens from the listed files");
		System.out.println("-?, --help");
		System.out.println("  display this message");
	}
	
	/* main
	 * Arguments:
	 *  @args - the list of command line arguments
	 * Purpose: main execution function for compiler
	 */
    public static void main(String[] args) throws IOException, IllegalOptionValueException, UnknownOptionException, LexerException {
    	String[] files = processCommandLineOptions(args);
		
		//read and merge all files into single file
		String source = "";
		boolean first = true;
		for(String file: files) {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = null;
			int lines = 0;
			while((line = reader.readLine()) != null) {
				source += ((first ? "" : "\n") + line);
				first = false;
				++lines;
			}
			reader.close();
			Application.getFileAndLineNumbers().addFile(file, lines);
		}
		
		//begin lexing and parsing
		OodleLexer lexer = null;
		try {
			lexer = new OodleLexer(new PushbackReader(new StringReader(source)));
			OodleParser oodleParser = new OodleParser(lexer);
			Start node = oodleParser.parse();
			//perform semantic checks
			OodleSemanticChecker checker = new OodleSemanticChecker();
			node.apply(checker); //invoke SemanticChecker traversal
			
			
		} catch (LexerException ex) {
			Application.getErrors().addLexicalError();
			System.out.println(Application.getFileAndLineNumbers().getFile(lexer.peek().getLine()) + ":" + Application.getFileAndLineNumbers().getLine(lexer.peek().getLine()) + "," + lexer.peek().getPos() + ":" + ex.getMessage());
		} 
		System.out.println(Application.getErrors().getTotalErrors() + " errors detected");
		if(Application.getErrors().getTotalErrors() > 0) {
			System.exit(1);
		} else {
			System.exit(0);
		}
    }
    
    /* processCommandLineOptions
	 * Arguments:
	 *  @args - the list of command line arguments
	 * Purpose: check for command line options, print help if any are malformed, return a list of files
	 */
    private static String[] processCommandLineOptions(String[] args) throws IllegalOptionValueException, UnknownOptionException {
    	CmdLineParser parser = new CmdLineParser();
    	//command line options
		CmdLineParser.Option printToken1 = parser.addBooleanOption('d', "print-tokens");
		CmdLineParser.Option printToken2 = parser.addBooleanOption('s', "print-tokens");
		CmdLineParser.Option help = parser.addBooleanOption('?', "help");
		//parse command line arguments
		parser.parse(args);
		
		//set applicable values from options class
		if((Boolean)parser.getOptionValue(printToken1, false) && (Boolean)parser.getOptionValue(printToken2, false)) {
			Application.getOptions().printTokens();
		} else if ((Boolean)parser.getOptionValue(printToken1, false) || (Boolean)parser.getOptionValue(printToken2, false) || (Boolean)parser.getOptionValue(help, false)) {
			printHelp();
			System.exit(0);
		}
		
		return parser.getRemainingArgs();
    }

}

