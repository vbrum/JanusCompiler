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
public class IfElseStmt extends Statement {

    public Exp e1, e2;
    public StatementList sl1, sl2;
    public int lineNumE1;
    public int charNumE1;
    public int lineNumE2;
    public int charNumE2;

    public IfElseStmt(Exp ae1, Exp ae2, StatementList asl1, StatementList asl2, int lineNum1, int charNum1, int lineNum2, int charNum2) {
        e1 = ae1;
        e2 = ae2;
        sl1 = asl1;
        sl2 = asl2;
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
