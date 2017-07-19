/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package syntaxTree;

import Visitor.*;
import symbolTable.Variable;
import newpackage.*;

/**
 *
 * @author VBrum
 */
public class Not extends Exp1 {

    public Exp e;
    public Variable t;
    public int lineNum;
    public int charNum;

    public Not(Exp ae, int lineNum, int charNum) {
        e = ae;
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
