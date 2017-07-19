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
public interface TypeVisitor {

    public Type visit(Program p);

    public Type visit(Proc pr);

    public Type visit(IntArrayDecl iad);

    public Type visit(IntDecl id);

    public Type visit(IfStmt ifs);

    public Type visit(IfElseStmt ifss);

    public Type visit(DoLoopStmt dls);

    public Type visit(DoStmt ds);

    public Type visit(LoopStmt ls);

    public Type visit(IntVar v);

    public Type visit(IntArray ia);

    public Type visit(PEModStmt pem);

    public Type visit(MEModStmt mem);

    public Type visit(XEModStmt xem);

    public Type visit(SwapStmt ss);

    public Type visit(Plus p);

    public Type visit(Minus m);

    public Type visit(Mult m);

    public Type visit(Mod m);

    public Type visit(Div d);

    public Type visit(LessThan l);

    public Type visit(GreaterThan g);

    public Type visit(LessEqThan l);

    public Type visit(GreaterEqThan g);

    public Type visit(Equal e);

    public Type visit(NotEqual n);

    public Type visit(And a);

    public Type visit(Or o);

    public Type visit(Xor x);

    public Type visit(Not n);

    public Type visit(IntLiteral i);

    public Type visit(CallStmt c);

    public Type visit(UncallStmt u);

    public Type visit(ReadStmt r);

    public Type visit(WriteStmt w);

    public Type visit(UnaryMinus m);

    public Type visit(LValueExp1 l);

    public Type visit(BracketedExp b);
    
    public Type visit(Identifier i);

    public Type visit(IntegerType it);

    public Type visit(BooleanType bt);
}
