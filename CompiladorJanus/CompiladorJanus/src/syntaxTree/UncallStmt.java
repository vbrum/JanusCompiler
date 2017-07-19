/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package syntaxTree;

import Visitor.*;
import newpackage.Temporary;
import symbolTable.Variable;

/**
 *
 * @author VBrum
 */
public class UncallStmt extends Statement {

    public Identifier i;
    public Variable t;
    public int lineNum;
    public int charNum;

    public UncallStmt(Identifier ai) {
        i = ai;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public Type accept(TypeVisitor v) {
        return v.visit(this);
    }

    public Variable generateTAC() {
        if (t == null) {
            Temporary temp = new Temporary();
            t = new Variable(temp.toString(), "temporary");
        }

        return t;
    }
}
