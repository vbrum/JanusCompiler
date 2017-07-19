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
public class Or extends Exp2 {

    public Exp2 m;
    public Exp1 e;
    public Variable t;
    public int lineNum;
    public int charNum;

    public Or(Exp2 am, Exp1 ae, int lineNum, int charNum) {
        m = am;
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
