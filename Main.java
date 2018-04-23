import java_cup.runtime.*;
import parserClasses.Program;
import parserClasses.*;
import check.Checker;
//import lex.MySymb;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class Main {
	static public void main(String arg[])
	{
		if(arg.length != 2) {
			System.err.println("Usage 1: vsopc -lex <SOURCE-FILE> or  vsopc -parse <SOURCE-FILE> " +
								"or vspoc -check <SOURCE-FILE>");
			System.exit(1);
		}
		final String path = arg[1];
	    if(arg[0].equals("-lex"))
		{
		    
    		Lexer lexer;
    		Symbol retrievedSym;
    		MySymb currentSym;
    		try {
    			lexer = new Lexer( new java.io.FileReader(path), path);
    			while (!lexer.getZzAtEOF())
    			{



    				retrievedSym = lexer.next_token();
					currentSym = new MySymb(retrievedSym,sym.terminalNames[retrievedSym.sym].replace("_", "-").toLowerCase());
    				currentSym.display();
    			}
    		}
    		catch (java.io.FileNotFoundException e) {
    			System.out.println("File not found");
    		}
    		catch (java.io.IOException e) {
    			System.out.println("IO error scanning file");
    			System.out.println(e);
    		}
    		catch (Exception e) {
    			System.out.println("Unexpected exception:");
    			e.printStackTrace();
    		}
        }
        else if(arg[0].equals("-parse"))
		{
			parser p = null;
			try {
				p = new parser(new Lexer(new FileReader(path), path), path);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			Symbol parsed = null;
			try {
				parsed = p.parse();
				Program program = (Program) parsed.value;
				System.out.println(program.toString(false));

				//System.out.println(p.program.toString());
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
			System.exit(p.toReturn);
        }
		else if(arg[0].equals("-check"))
		{
			parser p = null;
			try {
				p = new parser(new Lexer(new FileReader(path), path), path);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			Symbol parsed = null;
			Checker c = null;
			try {

				parsed = p.parse();
				Program program = (Program) parsed.value;
//				System.out.println(program.toString());
				c = new Checker(program,path);
				System.out.println(program.toString(true));
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(c.toReturn);
			}
		}
        else
        {
            System.err.println("Usage2: vsopc -lex <SOURCE-FILE> or  vsopc -parse <SOURCE-FILE> ");
			System.exit(1);
        }
	}


}
