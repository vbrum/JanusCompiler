/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package syntaxTree;

import Visitor.*;
import symbolTable.Variable;

/**
 *
 * @author VBrum
 */
public class IntArray extends LValue implements Type{

    public Identifier i;
    public Exp e;
    public Variable t;

    public IntArray(Identifier ai, Exp ae) {
        i = ai;
        e = ae;

    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public Type accept(TypeVisitor v) {
        return v.visit(this);
    }
    
    public Variable generateTAC() {
        if (t == null) {
            return new Variable(i.s, "id");
        } else {
            return t;
        }
    }
}
