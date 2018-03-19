package lex;

public interface sym
{
    final int EOF = 0;
    final int AND = 1;
    final int BOOL = 2;
    final int CLASS = 3;
    final int DO = 4;
    final int ELSE = 5;
    final int EXTENDS = 6;
    final int FALSE = 7;
    final int IF = 8;
    final int IN = 9;
    final int INT32 = 10;
    final int ISNULL = 11;
    final int LET = 12;
    final int NEW = 13;
    final int NOT = 14;
    final int STRING = 15;
    final int THEN = 16;
    final int TRUE = 17;
    final int UNIT = 18;
    final int WHILE = 19;
    final int LBRACE = 20;
    final int RBRACE = 21;
    final int LPAR = 22;
    final int RPAR = 23;
    final int COLON = 24;
    final int SEMICOLON = 25;
    final int COMMA = 26;
    final int PLUS = 27;
    final int MINUS = 28;
    final int TIMES = 29;
    final int DIV = 30;
    final int POW = 31;
    final int DOT = 32;
    final int EQUAL = 33;
    final int LOWER = 34;
    final int LOWEREQUAL = 35;
    final int ASSIGN = 36;
    final int OBJECTIDENTIFIER = 37;
    final int INTEGERLITERAL = 38;
    final int STRINGLITERAL = 39;
    final int TYPEIDENTIFIER = 40;


    final String[] stringTab = new String[] {
            "EOF",
            "and",
            "bool",
            "class",
            "do",
            "else",
            "extends",
            "false",
            "if",
            "in",
            "int32",
            "isnull",
            "let",
            "new",
            "not",
            "string",
            "then",
            "true",
            "unit",
            "while",
            "lbrace",
            "rbrace",
            "lpar",
            "rpar",
            "colon",
            "semicolon",
            "comma",
            "plus",
            "minus",
            "times",
            "div",
            "pow",
            "dot",
            "equal",
            "lower",
            "lower-equal",
            "assign",
            "object-identifier",
            "integer-literal",
            "string-literal",
            "type-identifier"
    };
}