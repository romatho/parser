import java_cup.runtime.*;

import java.util.Stack;

%%

/**
 * Class Name
 */

%class Lexer

/**
 * Encoding
 */

%unicode

/**
 * To allow line count via (yyline) 
 */
%line

/**
 * To allow column count via (yycolumn) 
 */

%column


%cup


%{


    StringBuffer string = new StringBuffer();
    private int[] stringinfo;
    private int[] comment;
    private Stack<int[]> comments= new Stack<int[]>();
    private boolean instring= false;
    private Symbol token(int type) 
    {
        return new Symbol(type, yyline + 1, yycolumn + 1);
    }

    private Symbol token(int type, Object value)
    {
        return new Symbol(type, yyline + 1, yycolumn + 1, value);
        
    }
    private Symbol token(int type, Object value, int[] stringinfo)
    {
        return new Symbol(type, stringinfo[1], stringinfo[2], value);
        
    }
    
    
    private String filename;

    Lexer(java.io.Reader in, String path ) {
        this.zzReader = in;
        this.filename = path;
    }

    private void errorDisplay()
    {
        int line = yyline + 1;
        int column =  yycolumn + 1;
        System.err.println(this.filename + ":" + line + ":" + column + ":" + " lexical error");
        System.exit(1);        
    }



    public boolean getZzAtEOF()
    {
        return zzAtEOF;
    }
    private void comment(int state)
    {
        comments.push(Element(state, yyline+1, yycolumn +1));
    }
    private void endComment()
    {
        yybegin(comments.pop()[0]);
    }
    public  int[] Element(int state, int line ,int column)
    {
        int[] element = new int[3];
        element[0] = state;
        element[1] = line;
        element[2] = column;
        return element;
    }
    
%}    

%eof{

    


    if(instring) 
    {
        System.err.println(this.filename + ":" + stringinfo[1] + ":" + stringinfo[2] + ":" + " lexical error");
        System.exit(1);    
    }
    //  Check comments
    if(!comments.isEmpty()){
        comment=comments.pop();
        System.err.println(this.filename + ":" + comment[1] + ":" + comment[2] + ":" + " lexical error");
        System.exit(1);
    }

%eof}
/*
liste des token class
*/

/*2*/
lowercaseletter = [a-z]
uppercaseletter = [A-Z]
letter = {lowercaseletter} | {uppercaseletter}
bindigit = [0-1]
digit = {bindigit} | [2-9]
hexdigit = {digit} | [a-f]

/*2.1*/
whitespace = [ ] | {tab} | {lf} | {ff} | {cr} 
tab = \t
lf = \n
ff = \f
cr = \r

availableIntegerChar = [a-zA-Z0-9_]
digit_literal = {digit}+
hexdigit_literal = {hexdigit}+
bindigit_literal = {bindigit}+
hexnumber = 0x{hexdigit_literal}
binnumber = 0b{bindigit_literal}
error_hex = 0x{hexdigit}*{availableIntegerChar}*
error_bin = 0b{bindigit}*{availableIntegerChar}*

integer_literal = {digit_literal}  | 0x {bindigit_literal}  | 0b {bindigit_literal}
typeidentifier = {uppercaseletter} ({letter} | {digit} | _)*

/*2.6*/
objectidentifier = {lowercaseletter} ({letter} | {digit} | _ )* 

/*2.7*/
stringliteral =  ({regularchar} | {escapedchar})* 
regularchar = [^\b\t\n\r\"\\\u0000]
escapedchar = \b | \t | \n | \r | \" | \\

/*escapesequence = */
lineTerminator= \r | \n | \r\n
singleComment = "//" + [^\r\n]* {lineTerminator}?



%state COMMENT STRING 

%% 


<YYINITIAL>{
\"                  {string.setLength(0);
                        string.append('\"');
                        instring= true;
                        stringinfo= Element(STRING,yyline +1,yycolumn + 1);
                        yybegin(STRING);}
    {whitespace}        {;}

    
    /*INTEGERS*/

    {binnumber}         {string.setLength(0);
                        string.append(Integer.toString(Integer.parseInt(yytext().substring(2), 2)));
                        return token(sym.INTEGERLITERAL, string.toString());}

    {error_bin}         {errorDisplay();}



    {hexnumber}         {string.setLength(0);
                        string.append(Integer.toString(Integer.parseInt(yytext().substring(2), 16)));
                        return token(sym.INTEGERLITERAL, string.toString());}

    {error_hex}         {errorDisplay();}

                            
    {digit_literal}     {string.setLength(0);
                        string.append(Integer.parseInt(yytext()));
                        return token(sym.INTEGERLITERAL, string.toString());}
    
    /*2.8*/
    "{"                 {return token(sym.LBRACE);}
    "}"                 {return token(sym.RBRACE);}
    "("                 {return token(sym.LPAR);}
    ")"                 {return token(sym.RPAR);}
    ":"                 {return token(sym.COLON);}
    ";"                 {return token(sym.SEMICOLON);}
    ","                 {return token(sym.COMMA);}
    "+"                 {return token(sym.PLUS);}
    "-"                 {return token(sym.MINUS);}
    "*"                 {return token(sym.TIMES);}
    "/"                 {return token(sym.DIV);}
    "^"                 {return token(sym.POW);}
    "."                 {return token(sym.DOT);}
    "="                 {return token(sym.EQUAL);}
    "<"                 {return token(sym.LOWER);}
    "<="                {return token(sym.LOWEREQUAL);}
    "<-"                {return token(sym.ASSIGN);}
    "and"               {return token(sym.AND);}
    "bool"              {return token(sym.BOOL);}
    "class"             {return token(sym.CLASS);}
    "do"                {return token(sym.DO);}
    "else"              {return token(sym.ELSE);}
    "extends"           {return token(sym.EXTENDS);}
    "false"             {return token(sym.FALSE);}
    "if"                {return token(sym.IF);}
    "in"                {return token(sym.IN);}
    "int32"             {return token(sym.INT32);}
    "isnull"            {return token(sym.ISNULL);}
    "let"               {return token(sym.LET);}
    "new"               {return token(sym.NEW);}
    "not"               {return token(sym.NOT);}
    "string"            {return token(sym.STRING);}
    "then"              {return token(sym.THEN);}
    "true"              {return token(sym.TRUE);}
    "unit"              {return token(sym.UNIT);}
    "while"             {return token(sym.WHILE);}
    [^a-zA-Z0-9_]               {errorDisplay();}

    "(*"       {
                 comment(YYINITIAL);
                 yybegin(COMMENT);
                }

    {singleComment}   {;}
    
    {typeidentifier}    {string.setLength(0);
                        string.append(yytext());
                        return token(sym.TYPEIDENTIFIER, string.toString());}
    
    {objectidentifier} {string.setLength(0);
                        string.append(yytext());
                        return token(sym.OBJECTIDENTIFIER, string.toString());}
    
}
<STRING>{
        "\\u0000"                      {errorDisplay();}
    {regularchar}               {string.append(yytext());}
    _                           {string.append(yytext());}
    \"                          {yybegin(YYINITIAL);
                                string.append('"');
                                instring=false;
                                return token(sym.STRINGLITERAL, string.toString(),stringinfo);}
    \\\"                        {string.append("\\\"");}
    \\\\                        {string.append("\\\\");}
    \\b                         {string.append("\\x08");}
    \\t                         {string.append("\\x09");}
    \\n                         {string.append("\\x0a");}
    \\f                         {string.append("\\x0c");}
    \\r                         {string.append("\\x0d");}
    \\x[0-9a-fA-F][0-9a-fA-F]   {string.append(yytext());}
    \\{whitespace}*[\n|\r|\r\n]{whitespace}* {;}
    {lineTerminator}            {errorDisplay();}
    [^a-zA-Z0-9_]               {errorDisplay();}
    
}

<COMMENT>
{
    "(*"        {
                 comment(COMMENT);
                }
    [^\*\)\(]* | [\*\)\(]        {}


    "*)"        {    endComment();  }
}


<<EOF>> { return token(sym.EOF); }
