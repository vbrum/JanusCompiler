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
public class PrintTree implements Visitor {

    //DeclList dl
    //ProcedureList pl
    public void visit(Program p) {
        for (int i = 0; i < p.dl.size(); i++) {
            p.dl.elementAt(i).accept(this);
            System.out.print(" ");
        }
        System.out.println();
        for (int i = 0; i < p.pl.size(); i++) {
            p.pl.elementAt(i).accept(this);
        }
    }

    //Identifier i
    //StatementList sl
    public void visit(Proc pr) {
        System.out.print("procedure ");
        pr.i.accept(this);
        System.out.println();
        for (int i = 0; i < pr.sl.size(); i++) {
            pr.sl.elementAt(i).accept(this);
            System.out.println();
        }
    }

    //Identifier i
    //int tam
    public void visit(IntArrayDecl iad) {
        iad.i.accept(this);
        System.out.print("[");
        System.out.print(iad.tam);
        System.out.print("]");
    }

    //Identifier i
    public void visit(IntDecl id) {
        id.i.accept(this);
    }

    //Exp e1,e2
    //StatementList sl
    public void visit(IfStmt ifs) {
        System.out.print("if ");
        ifs.e1.accept(this);
        System.out.println(" then");
        for (int i = 0; i < ifs.sl.size(); i++) {
            ifs.sl.elementAt(i).accept(this);
            System.out.println();
        }
        System.out.print("fi ");
        ifs.e2.accept(this);
    }

    //Exp e1,e2
    //StatementList sl1,sl2
    public void visit(IfElseStmt ifss) {
        System.out.print("if ");
        ifss.e1.accept(this);
        System.out.println("then");
        for (int i = 0; i < ifss.sl1.size(); i++) {
            ifss.sl1.elementAt(i).accept(this);
            System.out.println();
        }
        System.out.println("else");
        for (int i = 0; i < ifss.sl2.size(); i++) {
            ifss.sl2.elementAt(i).accept(this);
            System.out.println();
        }
        System.out.print("fi ");
        ifss.e2.accept(this);
    }

    //Exp e1,e2
    //StatementList sl1,sl2
    public void visit(DoLoopStmt dls) {
        System.out.print("from ");
        dls.e1.accept(this);
        System.out.println();
        System.out.println("do");
        for (int i = 0; i < dls.sl1.size(); i++) {
            dls.sl1.elementAt(i).accept(this);
            System.out.println();
        }
        System.out.println("loop");
        for (int i = 0; i < dls.sl2.size(); i++) {
            dls.sl2.elementAt(i).accept(this);
        }
        System.out.println();
        System.out.print("until ");
        dls.e2.accept(this);
    }

    //Exp e1,e2
    //StatementList sl
    public void visit(DoStmt ds) {
        System.out.print("from ");
        ds.e1.accept(this);
        System.out.println();
        System.out.println("do ");
        for (int i = 0; i < ds.sl.size(); i++) {
            ds.sl.elementAt(i).accept(this);
            System.out.println();
        }
        System.out.print("until ");
        ds.e2.accept(this);
    }

    //Exp e1,e2
    //StatementList sl
    public void visit(LoopStmt ls) {
        System.out.print("from ");
        ls.e1.accept(this);
        System.out.println();
        System.out.println("loop");
        for (int i = 0; i < ls.sl.size(); i++) {
            ls.sl.elementAt(i).accept(this);
            System.out.println();
        }
        System.out.print("until ");
        ls.e2.accept(this);
    }

    //Identifier i
    public void visit(IntVar v) {
        v.i.accept(this);
    }

    //Exp e
    //Identifier i
    public void visit(IntArray ia) {
        ia.i.accept(this);
        System.out.print("[");
        ia.e.accept(this);
        System.out.print("]");
    }

    //Exp e
    //LValue l
    public void visit(PEModStmt pem) {
        pem.l.accept(this);
        System.out.print("+=");
        pem.e.accept(this);
    }

    //Exp e
    //LValue l
    public void visit(MEModStmt mem) {
        mem.l.accept(this);
        System.out.print("-=");
        mem.e.accept(this);
    }

    //Exp e
    //LValue l
    public void visit(XEModStmt xem) {
        xem.l.accept(this);
        System.out.print("!=");
        xem.e.accept(this);
    }

    //LValue l1,l2
    public void visit(SwapStmt ss) {
        ss.l1.accept(this);
        System.out.print(":");
        ss.l2.accept(this);
    }

    //MinExp m
    //Exp e
    public void visit(Plus p) {
        p.m.accept(this);
        System.out.print("+");
        p.e.accept(this);
    }

    //MinExp m
    //Exp e
    public void visit(Minus m) {
        m.m.accept(this);
        System.out.print("-");
        m.e.accept(this);
    }

    //MinExp m
    //Exp e
    public void visit(Mult m) {
        m.m.accept(this);
        System.out.print("*");
        m.e.accept(this);
    }

    //MinExp m
    //Exp e
    public void visit(Mod m) {
        m.m.accept(this);
        System.out.print("\\");
        m.e.accept(this);
    }

    //MinExp m
    //Exp e
    public void visit(Div d) {
        d.m.accept(this);
        System.out.print("/");
        d.e.accept(this);
    }

    //MinExp m
    //Exp e
    public void visit(LessThan l) {
        l.m.accept(this);
        System.out.print("<");
        l.e.accept(this);
    }

    //MinExp m
    //Exp e
    public void visit(GreaterThan g) {
        g.m.accept(this);
        System.out.print(">");
        g.e.accept(this);
    }

    //MinExp m
    //Exp e
    public void visit(LessEqThan l) {
        l.m.accept(this);
        System.out.print("<=");
        l.e.accept(this);
    }

    //MinExp m
    //Exp e
    public void visit(GreaterEqThan g) {
        g.m.accept(this);
        System.out.print(">=");
        g.e.accept(this);
    }

    //MinExp m
    //Exp e
    public void visit(Equal e) {
        e.m.accept(this);
        System.out.print("=");
        e.e.accept(this);
    }

    //MinExp m
    //Exp e
    public void visit(NotEqual n) {
        n.m.accept(this);
        System.out.print("#");
        n.e.accept(this);
    }

    //MinExp m
    //Exp e
    public void visit(And a) {
        a.m.accept(this);
        System.out.print("&");
        a.e.accept(this);
    }

    //MinExp m
    //Exp e
    public void visit(Or o) {
        o.m.accept(this);
        System.out.print("|");
        o.e.accept(this);
    }

    //MinExp m
    //Exp e
    public void visit(Xor x) {
        x.m.accept(this);
        System.out.print("!");
        x.e.accept(this);
    }

    //Exp e
    public void visit(Not n) {
        System.out.print("~");
        n.e.accept(this);
    }

    //Integer i
    public void visit(IntLiteral i) {
        System.out.print(i.i.intValue());
    }

    //Identifier i
    public void visit(CallStmt c) {
        System.out.print("call ");
        c.i.accept(this);
    }

    //Identifier i
    public void visit(UncallStmt u) {
        System.out.print("uncall ");
        u.i.accept(this);
    }

    //Identifier i
    public void visit(ReadStmt r) {
        System.out.print("read ");
        r.l.accept(this);
    }

    //Identifier i
    public void visit(WriteStmt w) {
        System.out.print("write ");
        w.l.accept(this);
    }

    //Exp e
    public void visit(UnaryMinus m) {
        System.out.print("-");
        m.e.accept(this);
    }

    //LValue l
    public void visit(LValueExp1 l) {
        l.l.accept(this);
    }

    //Exp e
    public void visit(BracketedExp b) {
        System.out.print("(");
        b.e.accept(this);
        System.out.print(")");
    }

    //String s
    public void visit(Identifier i) {
        System.out.print(i.s);
    }
}
