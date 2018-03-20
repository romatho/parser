CUP=lib/java-cup-11a.jar
CUPRUNTIME=lib/java-cup-11b-runtime.jar
JFLEX=lib/jflex-1.6.1.jar

all:	vsopc

.PHONY: all clean install-tools vsopc flex parse

vsopc:	clean	parse	flex
	javac -cp $(CUPRUNTIME) lex/*.java parserClasses/*.java parse/*.java parser.java sym.java Lexer.java Main.java
	jar -cvfm vsopcompiler.jar manifest.txt lex/*.class parserClasses/*.class parse/*.class parser.class Lexer.class Main.class
	chmod u+x vsopcompiler.jar
	chmod u+x vsopc

flex:
	java -jar $(JFLEX) -d ./ lex/lexer.flex

parse:
	java -jar $(CUP) parse/parser.cup

install-tools:
	sudo apt update -y
	sudo apt full-upgrade -y
	sudo apt install -y ant cup curl default-jdk

clean:
	find . -name '*.class' -delete
	rm -f Lexer.java
	rm -f parser.java
	rm -f sym.java
	rm -f vsopcompiler.jar