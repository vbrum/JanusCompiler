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
public class LoopStmt extends Statement {

    public Exp e1, e2;
    public StatementList sl;
    public int lineNumE1;
    public int charNumE1;
    public int lineNumE2;
    public int charNumE2;

    public LoopStmt(Exp ae1, Exp ae2, StatementList asl, int lineNum1, int charNum1, int lineNum2, int charNum2) {
        e1 = ae1;
        e2 = ae2;
        sl = asl;
        this.lineNumE1 = lineNum1;
	this.charNumE1 = charNum1;
        this.lineNumE2 = lineNum2;
        this.charNumE2 = charNum2;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public Type accept(TypeVisitor v) {
        return v.visit(this);
    }
}
