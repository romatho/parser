package lex;

import java_cup.runtime.Symbol;


public class MySymb extends Symbol {

    public String tokenClass;

    public MySymb(Symbol symbol, String pTokenClass)
    {
        super(symbol.sym, symbol.right, symbol.left, symbol.value);
        this.tokenClass = new String(pTokenClass);
    }

    public void display()
    {
        /*Il reste à modifier le dernier élément pour renvoyer une string + ajouter les values*/
        if(this.sym == 0)
            return;
        System.out.print(this.right + "," + this.left + "," + this.tokenClass);
        if(this.value != null)
            System.out.print("," + this.value);
        System.out.println();
    }
}
