/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Visitor;

import Visitor.Visitor;
import symbolTable.Scope;
import symbolTable.SymbolTable;
import symbolTable.Variable;
import syntaxTree.*;

/**
 *
 * @author VBrum
 */
public class BuildSymbolTableVisitor implements Visitor {

    private SymbolTable symbolTable;
    private Scope currentScope;
    private int blockNumber;
    public boolean errorDetected;
    private String lvalue = "";
    private String lvalueR = "";
    public String errorMessage;
    
    public BuildSymbolTableVisitor() {
        symbolTable = new SymbolTable();
        currentScope = symbolTable;
        blockNumber = 0;
        errorDetected = false;
        errorMessage = "";
    }

    public Scope getFirstScope() {
        return symbolTable;
    }

    private void redefError(String name, int line, int character) {
        System.err.println("Multiply defined identifier " + name + " at line " + line + ", character " + character);
        errorMessage = "Multiply defined identifier " + name + " at line " + line + ", character " + character;
        errorDetected = true;
    }

    //DeclList dl
    //ProcedureList pl
    public void visit(Program p) {
        for (int i = 0; i < p.dl.size(); i++) {
            p.dl.elementAt(i).accept(this);
        }
        for (int i = 0; i < p.pl.size(); i++) {
            p.pl.elementAt(i).accept(this);
        }
    }

    //Identifier i
    //StatementList sl
    public void visit(Proc pr) {
        symbolTable.addProcedure(pr.i.toString());
        currentScope = symbolTable.enterScope(pr.i.toString());

        pr.i.accept(this);

        for (int i = 0; i < pr.sl.size(); i++) {
            pr.sl.elementAt(i).accept(this);
        }

        currentScope = currentScope.exitScope();

    }

    @Override
    public void visit(IntArrayDecl iad) {
        if (symbolTable.lookupVariable(iad.i.toString()) != null) {
            redefError(iad.i.toString(), iad.i.l, iad.i.c);
        }
        symbolTable.addVariable(iad.i.toString(), "int []");
        symbolTable.lookupVariable(iad.i.toString()).setArrayRange(iad.tam);
        iad.i.accept(this);
    }

    @Override
    public void visit(IntDecl id) {
        if (symbolTable.lookupVariable(id.i.toString()) != null) {
            redefError(id.i.toString(), id.i.l, id.i.c);
        }
        symbolTable.addVariable(id.i.toString(), "int");
        id.i.accept(this);
    }

    @Override
    public void visit(IfStmt ifs) {
        ifs.e1.accept(this);
        ifs.e2.accept(this);

        for (int i = 0; i < ifs.sl.size(); i++) {
            ifs.sl.elementAt(i).accept(this);
        }

    }

    @Override
    public void visit(IfElseStmt ifss) {
        ifss.e1.accept(this);
        ifss.e2.accept(this);

        for (int i = 0; i < ifss.sl1.size(); i++) {
            ifss.sl1.elementAt(i).accept(this);
        }

        for (int i = 0; i < ifss.sl2.size(); i++) {
            ifss.sl2.elementAt(i).accept(this);
        }

    }

    @Override
    public void visit(DoLoopStmt dls) {
        dls.e1.accept(this);
        dls.e2.accept(this);

        for (int i = 0; i < dls.sl1.size(); i++) {
            dls.sl1.elementAt(i).accept(this);
        }

        for (int i = 0; i < dls.sl2.size(); i++) {
            dls.sl2.elementAt(i).accept(this);
        }
    }

    @Override
    public void visit(DoStmt ds) {
        ds.e1.accept(this);
        ds.e2.accept(this);

        for (int i = 0; i < ds.sl.size(); i++) {
            ds.sl.elementAt(i).accept(this);
        }

    }

    @Override
    public void visit(LoopStmt ls) {
        ls.e1.accept(this);
        ls.e2.accept(this);

        for (int i = 0; i < ls.sl.size(); i++) {
            ls.sl.elementAt(i).accept(this);
        }
    }

    @Override
    public void visit(IntVar v) {
        v.i.accept(this);
        if(lvalueR.equals(v.i.s)){
            System.err.println("Identifiers " + lvalueR + " and " + v.i.s + " are aliases");
            errorDetected = true;
        }
        else
            lvalue = v.i.s;
        
    }

    @Override
    public void visit(IntArray ia) {
        ia.e.accept(this);
        /*String posVet = ia.e.generateTAC().getName();
        int pos = Integer.parseInt(posVet);
        Variable v = currentScope.lookupVariable(ia.i.s);
        if (pos < 0 || pos >= v.getArrayRange()){
            errorDetected = true;
            System.err.println("Index out of bounds");
        }*/
        ia.i.accept(this);
        if(lvalueR.equals(ia.i.s)){
            System.err.println("Identifiers " + lvalueR + " and " + ia.i.s + " are aliases");
            errorDetected = true;
        }
        else
            lvalue = ia.i.s;
        
        
    }

    @Override
    public void visit(PEModStmt pem) {
        pem.l.accept(this);
        lvalueR = lvalue;
        pem.e.accept(this);
        lvalueR = "";
        lvalue = "";
        
    }

    @Override
    public void visit(MEModStmt mem) {
        mem.l.accept(this);
        lvalueR = lvalue;
        mem.e.accept(this);
        lvalueR = "";
        lvalue = "";
        
    }

    @Override
    public void visit(XEModStmt xem) {
        xem.l.accept(this);
        lvalueR = lvalue;
        xem.e.accept(this);
        lvalueR = "";
        lvalue = "";
    }

    @Override
    public void visit(SwapStmt ss) {
        ss.l1.accept(this);
        ss.l2.accept(this);
    }

    @Override
    public void visit(Plus p) {
        p.m.accept(this);
        p.e.accept(this);
        lvalue = "";
    }

    @Override
    public void visit(Minus m) {
        m.m.accept(this);
        m.e.accept(this);
        lvalue = "";
    }

    @Override
    public void visit(Mult m) {
        m.m.accept(this);
        m.e.accept(this);
        lvalue = "";
    }

    @Override
    public void visit(Mod m) {
        m.m.accept(this);
        m.e.accept(this);
        lvalue = "";
        
    }

    @Override
    public void visit(Div d) {
        d.m.accept(this);
        d.e.accept(this);
        lvalue = "";
        
    }

    @Override
    public void visit(LessThan l) {
        l.m.accept(this);
        l.e.accept(this);
        lvalue = "";     
    }

    @Override
    public void visit(GreaterThan g) {
        g.m.accept(this);
        g.e.accept(this);
        lvalue = "";      
    }

    @Override
    public void visit(LessEqThan l) {
        l.m.accept(this);
        l.e.accept(this);
        lvalue = "";       
    }

    @Override
    public void visit(GreaterEqThan g) {
        g.m.accept(this);
        g.e.accept(this);
        lvalue = "";       
    }

    @Override
    public void visit(Equal e) {
        e.m.accept(this);
        e.e.accept(this);
        lvalue = "";        
    }

    @Override
    public void visit(NotEqual n) {
        n.m.accept(this);
        n.e.accept(this);
        lvalue = "";
    }

    @Override
    public void visit(And a) {
        a.m.accept(this);
        a.e.accept(this);
        lvalue = "";  
    }

    @Override
    public void visit(Or o) {
        o.m.accept(this);
        o.e.accept(this);
        lvalue = "";
    }

    @Override
    public void visit(Xor x) {
        x.m.accept(this);
        x.e.accept(this);
        lvalue = "";
    }

    @Override
    public void visit(Not n) {
        n.e.accept(this);
        lvalue = "";
    }

    @Override
    public void visit(IntLiteral i) {
        i.generateTAC();
    }

    @Override
    public void visit(CallStmt c) {
        c.i.accept(this);
    }

    @Override
    public void visit(UncallStmt u) {
        u.i.accept(this);
    }

    @Override
    public void visit(ReadStmt r) {
        r.l.accept(this);
    }

    @Override
    public void visit(WriteStmt w) {
        w.l.accept(this);
    }

    @Override
    public void visit(UnaryMinus m) {
        m.e.accept(this);
        m.t = m.e.generateTAC();
        //m.t.setName("-" + m.t.getName());
    }

    @Override
    public void visit(LValueExp1 l) {
        l.l.accept(this);
    }

    @Override
    public void visit(BracketedExp b) {
        b.e.accept(this);
    }

    @Override
    public void visit(Identifier i) {
    }
}
