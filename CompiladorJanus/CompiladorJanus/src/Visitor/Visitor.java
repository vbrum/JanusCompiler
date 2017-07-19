/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Visitor;

import syntaxTree.*;

/**
 *
 * @author VBrum
 */
public interface Visitor {

    public void visit(Program p); //x

    public void visit(Proc pr);  //x

    public void visit(IntArrayDecl iad); //x

    public void visit(IntDecl id); //x

    public void visit(IfStmt ifs); //x

    public void visit(IfElseStmt ifss); //x

    public void visit(DoLoopStmt dls); //x
 
    public void visit(DoStmt ds); //x

    public void visit(LoopStmt ls); //x

    public void visit(IntVar v); //x

    public void visit(IntArray ia);

    public void visit(PEModStmt pem); //x

    public void visit(MEModStmt mem); //x

    public void visit(XEModStmt xem); //x

    public void visit(SwapStmt ss); //x

    public void visit(Plus p); //x

    public void visit(Minus m); //x

    public void visit(Mult m); //x

    public void visit(Mod m); //x

    public void visit(Div d); //x

    public void visit(LessThan l); //x

    public void visit(GreaterThan g); //x

    public void visit(LessEqThan l); //x

    public void visit(GreaterEqThan g); //x

    public void visit(Equal e); //x

    public void visit(NotEqual n); //x

    public void visit(And a); //x

    public void visit(Or o); //x

    public void visit(Xor x); //x

    public void visit(Not n); //x

    public void visit(IntLiteral i); //x

    public void visit(CallStmt c); //x

    public void visit(UncallStmt u); //x

    public void visit(ReadStmt r); //x

    public void visit(WriteStmt w); //x

    public void visit(UnaryMinus m); //x

    public void visit(LValueExp1 l);  //x

    public void visit(BracketedExp b); //x

    public void visit(Identifier i); //x
}
