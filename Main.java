import java_cup.runtime.*;

public class Main {
	static public void main(String arg[])
	{
		if(arg.length != 2) {
			System.err.println("Usage: vsopc -lex <SOURCE-FILE> or  vsopc -parse <SOURCE-FILE> ");
			System.exit(1);
		}
		final String path = arg[1];
	    if(arg[0]=="-lex")
		{
		    
    		Lexer lexer;
    		Symbol retrievedSym;
    		MySymb currentSym;
    		try {
    			lexer = new Lexer( new java.io.FileReader(path), path);
    			while (!lexer.getZzAtEOF())
    			{
    				retrievedSym = lexer.next_token();
    				currentSym = new MySymb(retrievedSym, sym.stringTab[retrievedSym.sym]);
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
        else if(arg[0]=="-parse")
		{
    		Parser p = new Parser(new lexer(new FileReader(path), path), filename);
            java_cup.runtime.Symbol parsed = p.parse();
            Program prog = (Program) parsed.value;
            System.out.println(prog.toString());
        }
        else
        {
            System.err.println("Usage: vsopc -lex <SOURCE-FILE> or  vsopc -parse <SOURCE-FILE> ");
			System.exit(1);
        }
	}


}
