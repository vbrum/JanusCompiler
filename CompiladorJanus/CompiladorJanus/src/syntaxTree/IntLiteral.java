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
public class IntLiteral extends Exp1 implements Type {

    public Integer i;

    public IntLiteral(Integer ai) {
        i = ai;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public Type accept(TypeVisitor v) {
        return v.visit(this);
    }

    public Variable generateTAC() {
        return new Variable(Integer.toString(i), "constant");
    }
}
