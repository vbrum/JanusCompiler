/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Visitor;

import syntaxTree.*;
import symbolTable.*;

/**
 *
 * @author VBrum
 */
public class UndefinedVariableVisitor implements Visitor {

    private Scope currentScope;
    public boolean errorDetected;
    private int blockNumber;
    public String errorMessage;

    public UndefinedVariableVisitor(Scope s) {
        currentScope = s;
        errorDetected = false;
        blockNumber = 0;
        errorMessage = "";
    }
    //create numbers (as strings) for the blocks

    public String nextBlockNumber() {
        blockNumber++;
        return ("" + blockNumber);
    }

    //Helper function to turn Type into String containing the type:  i.e. "int", "boolean", etc.
    /*public String getTypeStr(Type t) {
        String type;

        if (t instanceof IntegerType) {
            type = "int";
        } else if (t instanceof IntArrayType) {
            type = "int[]";
        } else if (t instanceof BooleanType) {
            type = "boolean";
        } else {
            IdentifierType t1 = (IdentifierType) t;
            type = t1.s;
        }

        return type;
    }*/
    //Helper function to report Redefinition Errors
    private void undefError(String name, int line, int character) {
        System.err.println("Use of undefined identifier " + name + " at line " + line + ", character " + character);
        errorMessage = "Use of undefined identifier " + name + " at line " + line + ", character " + character;
        errorDetected = true;
    }

    public void visit(Program p) {
        for (int i = 0; i < p.dl.size(); i++) {
            p.dl.elementAt(i).accept(this);
        }
        for (int i = 0; i < p.pl.size(); i++) {
            p.pl.elementAt(i).accept(this);
        }
    }

    public void visit(Proc pr) {
        currentScope = currentScope.enterScope(pr.i.toString());
        pr.i.accept(this);
        for (int i = 0; i < pr.sl.size(); i++) {
            pr.sl.elementAt(i).accept(this);

        }
        currentScope = currentScope.exitScope();
    }

    public void visit(IntArrayDecl iad) {
        iad.i.accept(this);
    }

    public void visit(IntDecl id) {
        id.i.accept(this);
    }

    public void visit(IfStmt ifs) {
        ifs.e1.accept(this);
        for (int i = 0; i < ifs.sl.size(); i++) {
            ifs.sl.elementAt(i).accept(this);
        }
        ifs.e2.accept(this);
    }

    public void visit(IfElseStmt ifss) {
        ifss.e1.accept(this);
        for (int i = 0; i < ifss.sl1.size(); i++) {
            ifss.sl1.elementAt(i).accept(this);
        }
        for (int i = 0; i < ifss.sl2.size(); i++) {
            ifss.sl2.elementAt(i).accept(this);
        }
        ifss.e2.accept(this);
    }

    public void visit(DoLoopStmt dls) {
        dls.e1.accept(this);
        for (int i = 0; i < dls.sl1.size(); i++) {
            dls.sl1.elementAt(i).accept(this);
        }
        for (int i = 0; i < dls.sl2.size(); i++) {
            dls.sl2.elementAt(i).accept(this);
        }
        dls.e2.accept(this);
    }

    public void visit(DoStmt ds) {
        ds.e1.accept(this);
        for (int i = 0; i < ds.sl.size(); i++) {
            ds.sl.elementAt(i).accept(this);
        }
        ds.e2.accept(this);
    }

    public void visit(LoopStmt ls) {
        ls.e1.accept(this);
        for (int i = 0; i < ls.sl.size(); i++) {
            ls.sl.elementAt(i).accept(this);
        }
        ls.e2.accept(this);
    }

    public void visit(IntVar v) {
        v.i.accept(this);
        if (currentScope.lookupVariable(v.i.toString()) == null) {
            undefError(v.i.s, v.i.l, v.i.c);
        }
    }

    public void visit(IntArray ia) {
        ia.i.accept(this);
        if (currentScope.lookupVariable(ia.i.toString()) == null) {
            undefError(ia.i.s, ia.i.l, ia.i.c);
        }
        ia.e.accept(this);
    }

    public void visit(PEModStmt pem) {
        pem.e.accept(this);
        pem.l.accept(this);
    }

    public void visit(MEModStmt mem) {
        mem.e.accept(this);
        mem.l.accept(this);
    }

    public void visit(XEModStmt xem) {
        xem.e.accept(this);
        xem.l.accept(this);
    }

    public void visit(SwapStmt ss) {
        ss.l1.accept(this);
        ss.l2.accept(this);
    }

    public void visit(Plus p) {
        p.e.accept(this);
        p.m.accept(this);
    }

    public void visit(Minus m) {
        m.e.accept(this);
        m.m.accept(this);
    }

    public void visit(Mult m) {
        m.e.accept(this);
        m.m.accept(this);
    }

    public void visit(Mod m) {
        m.e.accept(this);
        m.m.accept(this);
    }

    public void visit(Div d) {
        d.e.accept(this);
        d.m.accept(this);
    }

    public void visit(LessThan l) {
        l.e.accept(this);
        l.m.accept(this);
    }

    public void visit(GreaterThan g) {
        g.e.accept(this);
        g.m.accept(this);
    }

    public void visit(LessEqThan l) {
        l.e.accept(this);
        l.m.accept(this);
    }

    public void visit(GreaterEqThan g) {
        g.e.accept(this);
        g.m.accept(this);
    }

    public void visit(Equal e) {
        e.e.accept(this);
        e.m.accept(this);
    }

    public void visit(NotEqual n) {
        n.e.accept(this);
        n.m.accept(this);
    }

    public void visit(And a) {
        a.e.accept(this);
        a.m.accept(this);
    }

    public void visit(Or o) {
        o.e.accept(this);
        o.m.accept(this);
    }

    public void visit(Xor x) {
        x.e.accept(this);
        x.m.accept(this);
    }

    public void visit(Not n) {
        n.e.accept(this);
    }

    public void visit(IntLiteral i) {
    }

    public void visit(CallStmt c) {
        c.i.accept(this);
        if (currentScope.lookupProcedure(c.i.toString()) == null) {
            undefError(c.i.s, c.i.l, c.i.c);
            
        }
    }

    public void visit(UncallStmt u) {
        u.i.accept(this);
        if (currentScope.lookupProcedure(u.i.toString()) == null) {
            undefError(u.i.s, u.i.l, u.i.c);
        }
    }

    public void visit(ReadStmt r) {
        r.l.accept(this);
        if(r.l instanceof IntVar){
            IntVar aux = (IntVar) r.l;
            if (currentScope.lookupVariable(aux.i.toString()) == null) {
                undefError(aux.i.s, aux.i.l, aux.i.c);
            } 
        } else {
            IntArray aux = (IntArray) r.l;
            if (currentScope.lookupVariable(aux.i.toString()) == null) {
                undefError(aux.i.s, aux.i.l, aux.i.c);
            }
        }
    }

    public void visit(WriteStmt w) {
        w.l.accept(this);
        if(w.l instanceof IntVar){
            IntVar aux = (IntVar) w.l;
            if (currentScope.lookupVariable(aux.i.toString()) == null) {
                undefError(aux.i.s, aux.i.l, aux.i.c);
            } 
        } else {
            IntArray aux = (IntArray) w.l;
            if (currentScope.lookupVariable(aux.i.toString()) == null) {
                undefError(aux.i.s, aux.i.l, aux.i.c);
            }
        }
        
    }

    public void visit(UnaryMinus m) {
        m.e.accept(this);
    }

    public void visit(LValueExp1 l) {
        l.l.accept(this);
    }

    public void visit(BracketedExp b) {
        b.e.accept(this);
    }

    public void visit(Identifier i) {
    }
}
