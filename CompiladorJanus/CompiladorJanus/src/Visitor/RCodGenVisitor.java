/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Visitor;

import IR.AssignmentIR;
import IR.CallIR;
import IR.ConditionalJumpIR;
import IR.CopyIR;
import IR.IndexedAssignmentIR1;
import IR.IndexedAssignmentIR2;
import IR.ParameterIR;
import IR.Quadruple;
import IR.UnaryAssignmentIR;
import IR.UnconditionalJumpIR;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import newpackage.Label;
import newpackage.Temporary;
import symbolTable.Scope;
import symbolTable.Variable;
import syntaxTree.And;
import syntaxTree.BracketedExp;
import syntaxTree.CallStmt;
import syntaxTree.Div;
import syntaxTree.DoLoopStmt;
import syntaxTree.DoStmt;
import syntaxTree.Equal;
import syntaxTree.GreaterEqThan;
import syntaxTree.GreaterThan;
import syntaxTree.Identifier;
import syntaxTree.IfElseStmt;
import syntaxTree.IfStmt;
import syntaxTree.IntArray;
import syntaxTree.IntArrayDecl;
import syntaxTree.IntDecl;
import syntaxTree.IntLiteral;
import syntaxTree.IntVar;
import syntaxTree.LValueExp1;
import syntaxTree.LessEqThan;
import syntaxTree.LessThan;
import syntaxTree.LoopStmt;
import syntaxTree.MEModStmt;
import syntaxTree.Minus;
import syntaxTree.Mod;
import syntaxTree.Mult;
import syntaxTree.Not;
import syntaxTree.NotEqual;
import syntaxTree.Or;
import syntaxTree.PEModStmt;
import syntaxTree.Plus;
import syntaxTree.Proc;
import syntaxTree.Program;
import syntaxTree.ReadStmt;
import syntaxTree.SwapStmt;
import syntaxTree.UnaryMinus;
import syntaxTree.UncallStmt;
import syntaxTree.WriteStmt;
import syntaxTree.XEModStmt;
import syntaxTree.Xor;

/**
 *
 * @author VBrum
 */
public class RCodGenVisitor implements Visitor {
    private Scope currentScope;
    private int blockNumber;
    private List<Quadruple> IRList;
    private Hashtable<Quadruple, List<Label>> labels;
    private HashMap<String, String> workList;   //A mapping of method calls to labels... i.e.  'foo()' --> 'L1:"
    private String reversedCode = "";
    private String tab = "";
    
    public RCodGenVisitor(Scope symbolTable) {
        labels = new Hashtable<Quadruple, List<Label>>();
        IRList = new ArrayList<Quadruple>();
        workList = new HashMap<String, String>();
        currentScope = symbolTable;
        blockNumber = 0;
    }
    
    public Hashtable<Quadruple, List<Label>> getLabels() {
        return labels;
    }

    public HashMap<String, String> getWorkList() {
        return workList;
    }

    public List<Quadruple> getIR() {
        return IRList;
    }
    
    public String getReversedCode(){
        return reversedCode;
    }

    //Helper function to add a new Label to a certain IR
    public String addLabel(Quadruple q, boolean printBefore) {
        List<Label> temp = labels.get(q);

        if (temp == null) {
            temp = new ArrayList<Label>();
        }

        Label l = new Label(printBefore);

        temp.add(l);
        labels.put(q, temp);

        return l.getName();
    }

    public String addLabel(Quadruple q, Label l) {
        List<Label> temp = labels.get(q);

        if (temp == null) {
            temp = new ArrayList<Label>();
        }

        temp.add(l);
        labels.put(q, temp);

        return l.getName();
    }
    //Decllist dl
    //Procedurelist pl
    public void visit(Program p) {
        for (int i = 0; i < p.dl.size(); i++) {
            p.dl.elementAt(i).accept(this);
        }
        reversedCode += "\n";
        for (int i = 0; i < p.pl.size(); i++) {
            p.pl.elementAt(i).accept(this);
        }
    }

    public void visit(IntDecl id) {
        id.i.accept(this);
        reversedCode += " ";
    }

    public void visit(IntArrayDecl iad) {
        iad.i.accept(this);
        reversedCode +="["+ iad.tam + "] ";
    }

    public void visit(Proc pr) {
        
        reversedCode += "procedure ";
        pr.i.accept(this);
        reversedCode += "\n";
        tab += "\t";
        for (int i = pr.sl.size() - 1; i >= 0; i--) {
            reversedCode += tab;
            pr.sl.elementAt(i).accept(this);
            reversedCode += "\n";
        }
        tab = tab.substring(0, tab.length()-1);
       
    }

    public void visit(IfStmt ifs) {              
        reversedCode += "if ";
        ifs.e2.accept(this);
        reversedCode += "then\n";
        tab += "\t";

        for (int i = ifs.sl.size() - 1; i >= 0 ; i--) {
            reversedCode += tab;
            ifs.sl.elementAt(i).accept(this);
            reversedCode += "\n";
        }
        
        tab = tab.substring(0,tab.length() - 1);        
        reversedCode += tab;
        reversedCode += "fi ";
        ifs.e1.accept(this);       
    }

    public void visit(IfElseStmt ifss) {
        reversedCode += "if ";
        ifss.e2.accept(this);       
        reversedCode += "then\n";
        tab += "\t";
        
        for (int i = ifss.sl1.size() - 1; i >= 0; i--) {
            reversedCode += tab;
            ifss.sl1.elementAt(i).accept(this);
            reversedCode += "\n";
        }
        
        tab = tab.substring(tab.length()-1);
        reversedCode += tab;
        reversedCode += "else\n";
        tab += "\t";
             
        for (int i = ifss.sl2.size() - 1; i >= 0; i--) {
            reversedCode += tab;
            ifss.sl2.elementAt(i).accept(this);
            reversedCode += "\n";
        }
        tab = tab.substring(0,tab.length()-1);
        reversedCode += tab;
        reversedCode += "fi ";
        ifss.e1.accept(this);
    }

    public void visit(DoLoopStmt dls) {        
        reversedCode += "from ";
        dls.e2.accept(this);
        reversedCode += "\n";
        reversedCode += tab;
        reversedCode += "do\n";
        tab += "\t";
        
        for (int i = dls.sl1.size() - 1; i >= 0 ; i--) {
            reversedCode += tab;
            dls.sl1.elementAt(i).accept(this);
            reversedCode += "\n";
        }
        
        tab = tab.substring(0,tab.length()-1);    
        reversedCode += tab;
        reversedCode += "loop\n";
        tab += "\t";
        
        for (int i = dls.sl2.size() - 1; i >= 0; i--) {
            reversedCode += tab;
            dls.sl2.elementAt(i).accept(this);
            reversedCode += "\n";
        }
        
        tab = tab.substring(0,tab.length()-1);        
        reversedCode += tab;
        reversedCode += "until ";
        dls.e1.accept(this);        
    }

    public void visit(DoStmt ds) {       
        reversedCode += "from ";
        ds.e2.accept(this);
        reversedCode += "\n";
        reversedCode += tab;
        reversedCode += "do\n";        
        tab += "\t";
        
        for (int i = ds.sl.size() - 1; i >= 0; i--) {
            reversedCode += tab;
            ds.sl.elementAt(i).accept(this);
            reversedCode += "\n";
        }
        
        tab = tab.substring(0,tab.length()-1);
        reversedCode += tab;
        reversedCode += "until ";
        ds.e1.accept(this);
    }

    public void visit(LoopStmt ls) {      
        reversedCode += "from ";
        ls.e2.accept(this);
        reversedCode += "\n";
        reversedCode += tab;
        reversedCode += "loop\n";
        tab += "\t";
        
        for (int i = ls.sl.size() - 1; i >= 0; i--) {
            reversedCode += tab;
            ls.sl.elementAt(i).accept(this);
            reversedCode += "\n";
        }
        
        tab = tab.substring(0,tab.length()-1);
        reversedCode += tab;
        reversedCode += "until ";
        ls.e1.accept(this);
    }

    public void visit(IntVar v) {
        reversedCode += v.i.s + " ";
    }

    public void visit(IntArray ia) {
        reversedCode += ia.i.s + "[";
        ia.e.accept(this);
        reversedCode +="] ";
    }

    public void visit(Plus p) {
        p.m.accept(this);
        reversedCode += "+ ";
        p.e.accept(this);
    }

    public void visit(Minus m) {
        m.m.accept(this);
        reversedCode += "- ";
        m.e.accept(this);
    }

    public void visit(Mult m) {
        m.m.accept(this);
        reversedCode += "* ";
        m.e.accept(this);  
    }

    public void visit(Mod m) {
        m.m.accept(this);
        reversedCode += "\\ ";
        m.e.accept(this);
    }

    public void visit(Div d) {
        d.m.accept(this);
        reversedCode += "/ ";
        d.e.accept(this);
        
    }

    public void visit(LessThan l) {
        l.m.accept(this);
        reversedCode += "< ";
        l.e.accept(this);
    }

    public void visit(GreaterThan g) {
        g.m.accept(this);
        reversedCode += "> ";
        g.e.accept(this);
    }

    public void visit(LessEqThan l) {
        l.m.accept(this);
        reversedCode += "<= ";
        l.e.accept(this);
    }

    public void visit(GreaterEqThan g) {
        g.m.accept(this);
        reversedCode += ">= ";
        g.e.accept(this);
    }

    public void visit(Equal e) {
        e.m.accept(this);
        reversedCode += "= ";
        e.e.accept(this);     
    }

    public void visit(NotEqual n) {
        n.m.accept(this);
        reversedCode += "# ";
        n.e.accept(this);
    }

    public void visit(And a) {
        a.m.accept(this);
        reversedCode += "& ";
        a.e.accept(this);
    }

    public void visit(Or o) {
        o.m.accept(this);
        reversedCode += "| ";
        o.e.accept(this);
    }

    public void visit(Xor x) {
        x.m.accept(this);
        reversedCode += "! ";
        x.e.accept(this);
    }

    public void visit(Not n) {
        reversedCode += "~";
        n.e.accept(this);
    }

    public void visit(IntLiteral i) {
        reversedCode += i.i + " ";
    }

    public void visit(CallStmt c) {
        reversedCode += "call ";
        c.i.accept(this);
    }

    public void visit(UncallStmt u) {
        reversedCode += "uncall ";
        u.i.accept(this);
    }

    public void visit(ReadStmt r) {
        reversedCode += "write ";
        r.l.accept(this);
    }

    public void visit(WriteStmt w) {
        reversedCode += "read ";
        w.l.accept(this);
    }

    public void visit(UnaryMinus m) {
        reversedCode += "-";
        m.e.accept(this);
    }

    public void visit(LValueExp1 l) {
        l.l.accept(this);
    }

    public void visit(BracketedExp b) {
        reversedCode += "( ";
        b.e.accept(this);
        reversedCode += ") ";
    }

    public void visit(Identifier i) {
        reversedCode += i.s;
    }

    public void visit(PEModStmt pem) {
        pem.l.accept(this);
        reversedCode += "-= ";
        pem.e.accept(this);       
    }

    public void visit(MEModStmt mem) {
        mem.l.accept(this);
        reversedCode += "+= ";
        mem.e.accept(this);
    }

    public void visit(XEModStmt xem) {
        xem.l.accept(this);
        reversedCode += "!= ";
        xem.e.accept(this);
    }

    public void visit(SwapStmt ss) {
        ss.l1.accept(this);
        reversedCode += ": ";
        ss.l2.accept(this);
    }    
}
