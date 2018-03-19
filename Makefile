CUP=lib/java-cup-11b.jar
CUPRUNTIME=lib/java-cup-11b-runtime.jar
JFLEX=lib/jflex-1.6.1.jar

all:	vsopc

.PHONY: all clean install-tools vsopc flex parse

vsopc:	clean	parse
	javac -cp $(CUPRUNTIME) lex/*.java parse/ast/*.java parse/*.java parser.java sym.java Main.java
	jar -cvfm vsopcompiler.jar manifest.txt lex/*.class parse/ast/*.class parse/*.class Main.class
	chmod u+x vsopcompiler.jar
	chmod u+x vsopc

flex:
	java -jar $(JFLEX) lex/lexer.flex

parse:
	java -jar $(CUP) parse/parser.cup

install-tools:
	sudo apt update -y
	sudo apt full-upgrade -y
	sudo apt install -y ant bison cup curl default-jdk

clean:
	rm -rf *.class
	rm -f parser.java
	rm -f sym.java