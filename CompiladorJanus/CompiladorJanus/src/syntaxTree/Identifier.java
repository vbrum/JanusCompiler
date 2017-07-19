/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package syntaxTree;

import Visitor.*;

/**
 *
 * @author VBrum
 */
public class Identifier {

    public String s;
    public int l;
    public int c;

    public Identifier(String as, int al, int ac) {
        s = as;
        l = al;
        c = ac;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public Type accept(TypeVisitor v) {
        return v.visit(this);
    }

    public String toString() {
        return s;
    }

}
