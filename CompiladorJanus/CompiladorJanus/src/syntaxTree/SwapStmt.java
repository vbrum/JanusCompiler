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
public class SwapStmt extends Statement {

    public LValue l1, l2;
    public Variable t;
    public int lineNum;
    public int charNum;

    public SwapStmt(LValue al1, LValue al2, int lineNum, int charNum) {
        l1 = al1;
        l2 = al2;
        this.lineNum = lineNum;
        this.charNum = charNum;
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
