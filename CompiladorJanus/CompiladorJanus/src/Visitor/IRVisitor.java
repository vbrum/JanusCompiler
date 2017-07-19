/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Visitor;

import IR.ParameterIR;
import IR.Quadruple;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import syntaxTree.*;
import symbolTable.*;
import newpackage.*;
import IR.*;

/**
 *
 * @author VBrum
 */
public class IRVisitor implements Visitor {

    private Scope currentScope;
    private int blockNumber;
    private List<Quadruple> IRList;
    private Hashtable<Quadruple, List<Label>> labels;
    private HashMap<String, String> workList;   //A mapping of method calls to labels... i.e.  'foo()' --> 'L1:"

    public IRVisitor(Scope symbolTable) {
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

    //Helper function to create unique numbers (as strings) for the blocks
    public String nextBlockNumber() {
        blockNumber++;
        return ("" + blockNumber);
    }

    //Decllist dl
    //Procedurelist pl
    public void visit(Program p) {
        for (int i = 0; i < p.dl.size(); i++) {
            p.dl.elementAt(i).accept(this);
        }
        for (int i = 0; i < p.pl.size(); i++) {
            p.pl.elementAt(i).accept(this);
        }
    }

    public void visit(IntDecl id) {
        id.i.accept(this);
    }

    public void visit(IntArrayDecl iad) {
        iad.i.accept(this);
    }

    public void visit(Proc pr) {
        currentScope = currentScope.enterScope(pr.i.toString());
        int size = IRList.size();
        if(!pr.first()){
            IRList.add(new CallIR("initproc", "0", null));
        }
        pr.i.accept(this);
        Label L1 = new Label(true);
        String labelName = "";
        for (int i = 0; i < pr.sl.size(); i++) {
            pr.sl.elementAt(i).accept(this);
           
                
        }
        
        if(pr.first()){
            labelName = addLabel(IRList.get(0), L1);
            IRList.add(new CallIR("exit", "0", null));
        }
        else{
            labelName = addLabel(IRList.get(size), L1);
            IRList.add(new CallIR("pfinished", "0", null));
        }
        
        workList.put(pr.i.toString(), labelName);
        
        currentScope = currentScope.exitScope();

    }

    public void visit(IfStmt ifs) {
        
        Label L1 = new Label(true);

        ifs.e1.accept(this);

        IRList.add(new ConditionalJumpIR(ifs.e1.generateTAC(), L1));

        for (int i = 0; i < ifs.sl.size(); i++) {
            ifs.sl.elementAt(i).accept(this);
        }
       
        int size = IRList.size();
        ifs.e2.accept(this);
        
        Temporary t = new Temporary();
        Variable v = new Variable(t.toString(),"temporary");
        IRList.add(new AssignmentIR("=", ifs.e1.generateTAC(), ifs.e2.generateTAC(), v));
        IRList.add(new ParameterIR(v));
        IRList.add(new CallIR("evalif", "1", null));
        
        addLabel(IRList.get(size), L1);
        //addLabel(IRList.get(IRList.size() - 1), L1);
    }

    public void visit(IfElseStmt ifss) {
       
        Label L1 = new Label(true);
        Label L2 = new Label(true);

        ifss.e1.accept(this);

        IRList.add(new ConditionalJumpIR(ifss.e1.generateTAC(), L1));

        for (int i = 0; i < ifss.sl1.size(); i++) {
            ifss.sl1.elementAt(i).accept(this);
        }

        IRList.add(new UnconditionalJumpIR(L2));
        int size = IRList.size();
        
        for (int i = 0; i < ifss.sl2.size(); i++) {
            ifss.sl2.elementAt(i).accept(this);
        }
        
        addLabel(IRList.get(size), L1);
        
        
        size = IRList.size();
        ifss.e2.accept(this);
        //size = IRList.size();
        Temporary t = new Temporary();
        Variable v = new Variable(t.toString(),"temporary");
        IRList.add(new AssignmentIR("=", ifss.e1.generateTAC(), ifss.e2.generateTAC(), v));
        IRList.add(new ParameterIR(v));
        IRList.add(new CallIR("evalif", "1", null));
        
        
        addLabel(IRList.get(size), L2);
        
    }

    public void visit(DoLoopStmt dls) {
        Label L1 = new Label(true);
        Label L2 = new Label(true);
        Label L3 = new Label(false);
        Label L4 = new Label(false);

        dls.e1.accept(this);
        IRList.add(new ParameterIR(dls.e1.generateTAC()));
        IRList.add(new CallIR("evalfrom1", "1", null));

        //verificar o que fazer quando o valor lógico em from for false
        //IRList.add(new ConditionalJumpIR(dls.e1.generateTAC(), LEnd));
        //IRList.add(new CallIR("System.exit","0",null)); Em java seria assim
        int size = IRList.size();

        for (int i = 0; i < dls.sl1.size(); i++) {
            dls.sl1.elementAt(i).accept(this);
        }

        addLabel(IRList.get(size), L1);

        dls.e2.accept(this);
        addLabel(IRList.get(IRList.size() - 1), L3);
        IRList.add(new ConditionalJumpIR(dls.e2.generateTAC(), L2));
        IRList.add(new UnconditionalJumpIR(L4));
        
        //IRList.add(new UnconditionalJumpIR(L3));
        
        size = IRList.size();

        for (int i = 0; i < dls.sl2.size(); i++) {
            dls.sl2.elementAt(i).accept(this);
        }
        
        dls.e1.accept(this);
        IRList.add(new ParameterIR(dls.e1.generateTAC()));
        IRList.add(new CallIR("evalfrom2", "1", null));
        IRList.add(new UnconditionalJumpIR(L1));
        
        addLabel(IRList.get(size), L2);
        addLabel(IRList.get(IRList.size() - 1), L4);
        //verificar se e1 é true. Se for, abortar o programa.
        //IRList.add(new ConditionalJumpIR(dls.e1.generateTAC(), "exit"));
        
        
    }

    public void visit(DoStmt ds) {
        Label L1 = new Label(true);
        Label L2 = new Label(true);
        Label L3 = new Label(false);
        
        //verificar se e1 é false. Se for, abortar o programa.
        ds.e1.accept(this);
        IRList.add(new ParameterIR(ds.e1.generateTAC()));
        IRList.add(new CallIR("evalfrom1", "1", null));

        int indexDo = IRList.size();

        for (int i = 0; i < ds.sl.size(); i++) {
            ds.sl.elementAt(i).accept(this);
        }
        
        ds.e2.accept(this);
        IRList.add(new ConditionalJumpIR(ds.e2.generateTAC(), L2));
        IRList.add(new UnconditionalJumpIR(L3)); //se a expressão em until for verdadeira, sai da estrutura sem entrar em loop.

        int indexEvalFrom2 = IRList.size();
        ds.e1.accept(this);
        IRList.add(new ParameterIR(ds.e1.generateTAC()));
        IRList.add(new CallIR("evalfrom2", "1", null));
        IRList.add(new UnconditionalJumpIR(L1));
        
        int indexEnd = IRList.size() - 1;
        

        addLabel(IRList.get(indexDo), L1);
        addLabel(IRList.get(indexEvalFrom2),L2);
        addLabel(IRList.get(indexEnd), L3);
    }

    public void visit(LoopStmt ls) {
        Label L1 = new Label(true);
        Label L2 = new Label(true);
        Label L3 = new Label(false);

        //verificar se e1 é false. Se for, abortar o programa.
        ls.e1.accept(this);
        IRList.add(new ParameterIR(ls.e1.generateTAC()));
        IRList.add(new CallIR("evalfrom1", "1", null));
        int posExp = IRList.size();
        ls.e2.accept(this);
        IRList.add(new ConditionalJumpIR(ls.e2.generateTAC(), L1));
        IRList.add(new UnconditionalJumpIR(L3)); //se a expressão em until for verdadeira, sai da estrutura sem entrar em loop.

        int size = IRList.size();

        for (int i = 0; i < ls.sl.size(); i++) {
            ls.sl.elementAt(i).accept(this);
        }
        
        
        ls.e1.accept(this);
        IRList.add(new ParameterIR(ls.e1.generateTAC()));
        IRList.add(new CallIR("evalfrom2", "1", null));
        IRList.add(new UnconditionalJumpIR(L2));
        
        
        addLabel(IRList.get(size), L1);
        addLabel(IRList.get(posExp), L2);
        addLabel(IRList.get(IRList.size() - 1), L3);

    }

    public void visit(IntVar v) {
        v.t = currentScope.lookupVariable(v.i.s);
    }

    public void visit(IntArray ia) {
        ia.t = currentScope.lookupVariable(ia.i.s);
        ia.e.accept(this);                
    }

    public void visit(Plus p) {
        p.m.accept(this);
        p.e.accept(this);     
        Exp e1 = p.m;
        Exp e2 = p.e;
        if (e1 instanceof IntArray) {
            Temporary t = new Temporary();
            Variable v = new Variable(t.toString(),"temporary");
            IntArray aux = (IntArray) e1;
            IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
            if (e2 instanceof IntArray){
                Temporary t2 = new Temporary();
                Variable v2 = new Variable(t2.toString(),"temporary");
                IntArray aux2 = (IntArray) e2;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux2.i.toString()), aux2.e.generateTAC(), v2));
                IRList.add(new AssignmentIR("+", v, v2, p.generateTAC()));
            } else {
                IRList.add(new AssignmentIR("+", v, p.e.generateTAC(), p.generateTAC()));
            }
        } else if (e1 instanceof IntVar) {
            if (e2 instanceof IntArray){
                Temporary t = new Temporary();
                Variable v = new Variable(t.toString(),"temporary");
                IntArray aux = (IntArray) e2;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
                IRList.add(new AssignmentIR("+", p.m.generateTAC(), v, p.generateTAC()));
            } else {
                IRList.add(new AssignmentIR("+", p.m.generateTAC(), p.e.generateTAC(), p.generateTAC()));
            }
        } else if (e1 instanceof IntLiteral){
                if(e2 instanceof IntArray){
                    Temporary t = new Temporary();
                    Variable v = new Variable(t.toString(),"temporary");
                    IntArray aux = (IntArray) e2;
                    IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
                    IRList.add(new AssignmentIR("+", e1.generateTAC(), v, p.generateTAC()));
                } else {
                    IRList.add(new AssignmentIR("+", p.e.generateTAC(), p.m.generateTAC(), p.generateTAC()));
                }
        }else {
            if(e2 instanceof IntArray){
                Temporary t = new Temporary();
                Variable v = new Variable(t.toString(),"temporary");
                IntArray aux = (IntArray) e2;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
                IRList.add(new AssignmentIR("+", p.m.generateTAC(), v, p.generateTAC()));
            } else {
                IRList.add(new AssignmentIR("+", p.m.generateTAC(), p.e.generateTAC(), p.generateTAC()));
            }
        }
    }

    public void visit(Minus m) {
        m.m.accept(this);
        m.e.accept(this);     
        Exp e1 = m.m;
        Exp e2 = m.e;
        if (e1 instanceof IntArray) {
            Temporary t = new Temporary();
            Variable v = new Variable(t.toString(),"temporary");
            IntArray aux = (IntArray) e1;
            IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
            if (e2 instanceof IntArray){
                Temporary t2 = new Temporary();
                Variable v2 = new Variable(t2.toString(),"temporary");
                IntArray aux2 = (IntArray) e2;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux2.i.toString()), aux2.e.generateTAC(), v2));
                IRList.add(new AssignmentIR("-", v, v2, m.generateTAC()));
            } else {
                IRList.add(new AssignmentIR("-", v, m.e.generateTAC(), m.generateTAC()));
            }
        } else if (e1 instanceof IntVar) {
            if (e2 instanceof IntArray){
                Temporary t = new Temporary();
                Variable v = new Variable(t.toString(),"temporary");
                IntArray aux = (IntArray) e2;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
                IRList.add(new AssignmentIR("-", m.m.generateTAC(), v, m.generateTAC()));
            } else {
                IRList.add(new AssignmentIR("-", m.m.generateTAC(), m.e.generateTAC(), m.generateTAC()));
            }
        } else if (e1 instanceof IntLiteral){
                if(e2 instanceof IntArray){
                    Temporary t = new Temporary();
                    Variable v = new Variable(t.toString(),"temporary");
                    IntArray aux = (IntArray) e2;
                    IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
                    IRList.add(new AssignmentIR("-", e1.generateTAC(), v, m.generateTAC()));
                } else {
                    IRList.add(new AssignmentIR("-", m.e.generateTAC(), m.m.generateTAC(), m.generateTAC()));
                }
        }else {
            if(e2 instanceof IntArray){
                Temporary t = new Temporary();
                Variable v = new Variable(t.toString(),"temporary");
                IntArray aux = (IntArray) e2;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
                IRList.add(new AssignmentIR("-", m.m.generateTAC(), v, m.generateTAC()));
            } else {
                IRList.add(new AssignmentIR("-", m.m.generateTAC(), m.e.generateTAC(), m.generateTAC()));
            }
        }
    }

    public void visit(Mult m) {
        m.m.accept(this);
        m.e.accept(this);     
        Exp e1 = m.m;
        Exp e2 = m.e;
        if (e1 instanceof IntArray) {
            Temporary t = new Temporary();
            Variable v = new Variable(t.toString(),"temporary");
            IntArray aux = (IntArray) e1;
            IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
            if (e2 instanceof IntArray){
                Temporary t2 = new Temporary();
                Variable v2 = new Variable(t2.toString(),"temporary");
                IntArray aux2 = (IntArray) e2;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux2.i.toString()), aux2.e.generateTAC(), v2));
                IRList.add(new AssignmentIR("*", v, v2, m.generateTAC()));
            } else {
                IRList.add(new AssignmentIR("*", v, m.e.generateTAC(), m.generateTAC()));
            }
        } else if (e1 instanceof IntVar) {
            if (e2 instanceof IntArray){
                Temporary t = new Temporary();
                Variable v = new Variable(t.toString(),"temporary");
                IntArray aux = (IntArray) e2;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
                IRList.add(new AssignmentIR("*", m.m.generateTAC(), v, m.generateTAC()));
            } else {
                IRList.add(new AssignmentIR("*", m.m.generateTAC(), m.e.generateTAC(), m.generateTAC()));
            }
        } else if (e1 instanceof IntLiteral){
                if(e2 instanceof IntArray){
                    Temporary t = new Temporary();
                    Variable v = new Variable(t.toString(),"temporary");
                    IntArray aux = (IntArray) e2;
                    IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
                    IRList.add(new AssignmentIR("*", e1.generateTAC(), v, m.generateTAC()));
                } else {
                    IRList.add(new AssignmentIR("*", m.e.generateTAC(), m.m.generateTAC(), m.generateTAC()));
                }
        }else {
            if(e2 instanceof IntArray){
                Temporary t = new Temporary();
                Variable v = new Variable(t.toString(),"temporary");
                IntArray aux = (IntArray) e2;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
                IRList.add(new AssignmentIR("*", m.m.generateTAC(), v, m.generateTAC()));
            } else {
                IRList.add(new AssignmentIR("*", m.m.generateTAC(), m.e.generateTAC(), m.generateTAC()));
            }
        }
    }

    public void visit(Mod m) {
        m.m.accept(this);
        m.e.accept(this);     
        Exp e1 = m.m;
        Exp e2 = m.e;
        if (e1 instanceof IntArray) {
            Temporary t = new Temporary();
            Variable v = new Variable(t.toString(),"temporary");
            IntArray aux = (IntArray) e1;
            IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
            if (e2 instanceof IntArray){
                Temporary t2 = new Temporary();
                Variable v2 = new Variable(t2.toString(),"temporary");
                IntArray aux2 = (IntArray) e2;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux2.i.toString()), aux2.e.generateTAC(), v2));
                IRList.add(new AssignmentIR("\\", v, v2, m.generateTAC()));
            } else {
                IRList.add(new AssignmentIR("\\", v, m.e.generateTAC(), m.generateTAC()));
            }
        } else if (e1 instanceof IntVar) {
            if (e2 instanceof IntArray){
                Temporary t = new Temporary();
                Variable v = new Variable(t.toString(),"temporary");
                IntArray aux = (IntArray) e2;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
                IRList.add(new AssignmentIR("\\", m.m.generateTAC(), v, m.generateTAC()));
            } else {
                IRList.add(new AssignmentIR("\\", m.m.generateTAC(), m.e.generateTAC(), m.generateTAC()));
            }
        } else if (e1 instanceof IntLiteral){
                if(e2 instanceof IntArray){
                    Temporary t = new Temporary();
                    Variable v = new Variable(t.toString(),"temporary");
                    IntArray aux = (IntArray) e2;
                    IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
                    IRList.add(new AssignmentIR("\\", e1.generateTAC(), v, m.generateTAC()));
                } else {
                    IRList.add(new AssignmentIR("\\", m.e.generateTAC(), m.m.generateTAC(), m.generateTAC()));
                }
        }else {
            if(e2 instanceof IntArray){
                Temporary t = new Temporary();
                Variable v = new Variable(t.toString(),"temporary");
                IntArray aux = (IntArray) e2;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
                IRList.add(new AssignmentIR("\\", m.m.generateTAC(), v, m.generateTAC()));
            } else {
                IRList.add(new AssignmentIR("\\", m.m.generateTAC(), m.e.generateTAC(), m.generateTAC()));
            }
        }
    }

    public void visit(Div d) {
        d.m.accept(this);
        d.e.accept(this);     
        Exp e1 = d.m;
        Exp e2 = d.e;
        if (e1 instanceof IntArray) {
            Temporary t = new Temporary();
            Variable v = new Variable(t.toString(),"temporary");
            IntArray aux = (IntArray) e1;
            IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
            if (e2 instanceof IntArray){
                Temporary t2 = new Temporary();
                Variable v2 = new Variable(t2.toString(),"temporary");
                IntArray aux2 = (IntArray) e2;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux2.i.toString()), aux2.e.generateTAC(), v2));
                IRList.add(new AssignmentIR("/", v, v2, d.generateTAC()));
            } else {
                IRList.add(new AssignmentIR("/", v, d.e.generateTAC(), d.generateTAC()));
            }
        } else if (e1 instanceof IntVar) {
            if (e2 instanceof IntArray){
                Temporary t = new Temporary();
                Variable v = new Variable(t.toString(),"temporary");
                IntArray aux = (IntArray) e2;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
                IRList.add(new AssignmentIR("/", d.m.generateTAC(), v, d.generateTAC()));
            } else {
                IRList.add(new AssignmentIR("/", d.m.generateTAC(), d.e.generateTAC(), d.generateTAC()));
            }
        } else if (e1 instanceof IntLiteral){
                if(e2 instanceof IntArray){
                    Temporary t = new Temporary();
                    Variable v = new Variable(t.toString(),"temporary");
                    IntArray aux = (IntArray) e2;
                    IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
                    IRList.add(new AssignmentIR("/", e1.generateTAC(), v, d.generateTAC()));
                } else {
                    IRList.add(new AssignmentIR("/", d.e.generateTAC(), d.m.generateTAC(), d.generateTAC()));
                }
        }else {
            if(e2 instanceof IntArray){
                Temporary t = new Temporary();
                Variable v = new Variable(t.toString(),"temporary");
                IntArray aux = (IntArray) e2;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
                IRList.add(new AssignmentIR("/", d.m.generateTAC(), v, d.generateTAC()));
            } else {
                IRList.add(new AssignmentIR("/", d.m.generateTAC(), d.e.generateTAC(), d.generateTAC()));
            }
        }
    }

    public void visit(LessThan l) {
        l.m.accept(this);
        l.e.accept(this);     
        Exp e1 = l.m;
        Exp e2 = l.e;
        if (e1 instanceof IntArray) {
            Temporary t = new Temporary();
            Variable v = new Variable(t.toString(),"temporary");
            IntArray aux = (IntArray) e1;
            IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
            if (e2 instanceof IntArray){
                Temporary t2 = new Temporary();
                Variable v2 = new Variable(t2.toString(),"temporary");
                IntArray aux2 = (IntArray) e2;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux2.i.toString()), aux2.e.generateTAC(), v2));
                IRList.add(new AssignmentIR("<", v, v2, l.generateTAC()));
            } else {
                IRList.add(new AssignmentIR("<", v, l.e.generateTAC(), l.generateTAC()));
            }
        } else if (e1 instanceof IntVar) {
            if (e2 instanceof IntArray){
                Temporary t = new Temporary();
                Variable v = new Variable(t.toString(),"temporary");
                IntArray aux = (IntArray) e2;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
                IRList.add(new AssignmentIR("<", l.m.generateTAC(), v, l.generateTAC()));
            } else {
                IRList.add(new AssignmentIR("<", l.m.generateTAC(), l.e.generateTAC(), l.generateTAC()));
            }
        } else if (e1 instanceof IntLiteral){
                if(e2 instanceof IntArray){
                    Temporary t = new Temporary();
                    Variable v = new Variable(t.toString(),"temporary");
                    IntArray aux = (IntArray) e2;
                    IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
                    IRList.add(new AssignmentIR("<", e1.generateTAC(), v, l.generateTAC()));
                } else {
                    IRList.add(new AssignmentIR("<", l.e.generateTAC(), l.m.generateTAC(), l.generateTAC()));
                }
        }else {
            if(e2 instanceof IntArray){
                Temporary t = new Temporary();
                Variable v = new Variable(t.toString(),"temporary");
                IntArray aux = (IntArray) e2;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
                IRList.add(new AssignmentIR("<", l.m.generateTAC(), v, l.generateTAC()));
            } else {
                IRList.add(new AssignmentIR("<", l.m.generateTAC(), l.e.generateTAC(), l.generateTAC()));
            }
        }
    }

    public void visit(GreaterThan g) {
        g.m.accept(this);
        g.e.accept(this);     
        Exp e1 = g.m;
        Exp e2 = g.e;
        if (e1 instanceof IntArray) {
            Temporary t = new Temporary();
            Variable v = new Variable(t.toString(),"temporary");
            IntArray aux = (IntArray) e1;
            IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
            if (e2 instanceof IntArray){
                Temporary t2 = new Temporary();
                Variable v2 = new Variable(t2.toString(),"temporary");
                IntArray aux2 = (IntArray) e2;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux2.i.toString()), aux2.e.generateTAC(), v2));
                IRList.add(new AssignmentIR(">", v, v2, g.generateTAC()));
            } else {
                IRList.add(new AssignmentIR(">", v, g.e.generateTAC(), g.generateTAC()));
            }
        } else if (e1 instanceof IntVar) {
            if (e2 instanceof IntArray){
                Temporary t = new Temporary();
                Variable v = new Variable(t.toString(),"temporary");
                IntArray aux = (IntArray) e2;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
                IRList.add(new AssignmentIR(">", g.m.generateTAC(), v, g.generateTAC()));
            } else {
                IRList.add(new AssignmentIR(">", g.m.generateTAC(), g.e.generateTAC(), g.generateTAC()));
            }
        } else if (e1 instanceof IntLiteral){
                if(e2 instanceof IntArray){
                    Temporary t = new Temporary();
                    Variable v = new Variable(t.toString(),"temporary");
                    IntArray aux = (IntArray) e2;
                    IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
                    IRList.add(new AssignmentIR(">", e1.generateTAC(), v, g.generateTAC()));
                } else {
                    IRList.add(new AssignmentIR(">", g.e.generateTAC(), g.m.generateTAC(), g.generateTAC()));
                }
        }else {
            if(e2 instanceof IntArray){
                Temporary t = new Temporary();
                Variable v = new Variable(t.toString(),"temporary");
                IntArray aux = (IntArray) e2;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
                IRList.add(new AssignmentIR(">", g.m.generateTAC(), v, g.generateTAC()));
            } else {
                IRList.add(new AssignmentIR(">", g.m.generateTAC(), g.e.generateTAC(), g.generateTAC()));
            }
        }
    }

    public void visit(LessEqThan l) {
        l.m.accept(this);
        l.e.accept(this);     
        Exp e1 = l.m;
        Exp e2 = l.e;
        if (e1 instanceof IntArray) {
            Temporary t = new Temporary();
            Variable v = new Variable(t.toString(),"temporary");
            IntArray aux = (IntArray) e1;
            IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
            if (e2 instanceof IntArray){
                Temporary t2 = new Temporary();
                Variable v2 = new Variable(t2.toString(),"temporary");
                IntArray aux2 = (IntArray) e2;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux2.i.toString()), aux2.e.generateTAC(), v2));
                IRList.add(new AssignmentIR("<=", v, v2, l.generateTAC()));
            } else {
                IRList.add(new AssignmentIR("<=", v, l.e.generateTAC(), l.generateTAC()));
            }
        } else if (e1 instanceof IntVar) {
            if (e2 instanceof IntArray){
                Temporary t = new Temporary();
                Variable v = new Variable(t.toString(),"temporary");
                IntArray aux = (IntArray) e2;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
                IRList.add(new AssignmentIR("<=", l.m.generateTAC(), v, l.generateTAC()));
            } else {
                IRList.add(new AssignmentIR("<=", l.m.generateTAC(), l.e.generateTAC(), l.generateTAC()));
            }
        } else if (e1 instanceof IntLiteral){
                if(e2 instanceof IntArray){
                    Temporary t = new Temporary();
                    Variable v = new Variable(t.toString(),"temporary");
                    IntArray aux = (IntArray) e2;
                    IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
                    IRList.add(new AssignmentIR("<=", e1.generateTAC(), v, l.generateTAC()));
                } else {
                    IRList.add(new AssignmentIR("<=", l.e.generateTAC(), l.m.generateTAC(), l.generateTAC()));
                }
        }else {
            if(e2 instanceof IntArray){
                Temporary t = new Temporary();
                Variable v = new Variable(t.toString(),"temporary");
                IntArray aux = (IntArray) e2;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
                IRList.add(new AssignmentIR("<=", l.m.generateTAC(), v, l.generateTAC()));
            } else {
                IRList.add(new AssignmentIR("<=", l.m.generateTAC(), l.e.generateTAC(), l.generateTAC()));
            }
        }
    }

    public void visit(GreaterEqThan g) {
        g.m.accept(this);
        g.e.accept(this);     
        Exp e1 = g.m;
        Exp e2 = g.e;
        if (e1 instanceof IntArray) {
            Temporary t = new Temporary();
            Variable v = new Variable(t.toString(),"temporary");
            IntArray aux = (IntArray) e1;
            IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
            if (e2 instanceof IntArray){
                Temporary t2 = new Temporary();
                Variable v2 = new Variable(t2.toString(),"temporary");
                IntArray aux2 = (IntArray) e2;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux2.i.toString()), aux2.e.generateTAC(), v2));
                IRList.add(new AssignmentIR(">=", v, v2, g.generateTAC()));
            } else {
                IRList.add(new AssignmentIR(">=", v, g.e.generateTAC(), g.generateTAC()));
            }
        } else if (e1 instanceof IntVar) {
            if (e2 instanceof IntArray){
                Temporary t = new Temporary();
                Variable v = new Variable(t.toString(),"temporary");
                IntArray aux = (IntArray) e2;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
                IRList.add(new AssignmentIR(">=", g.m.generateTAC(), v, g.generateTAC()));
            } else {
                IRList.add(new AssignmentIR(">=", g.m.generateTAC(), g.e.generateTAC(), g.generateTAC()));
            }
        } else if (e1 instanceof IntLiteral){
                if(e2 instanceof IntArray){
                    Temporary t = new Temporary();
                    Variable v = new Variable(t.toString(),"temporary");
                    IntArray aux = (IntArray) e2;
                    IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
                    IRList.add(new AssignmentIR(">=", e1.generateTAC(), v, g.generateTAC()));
                } else {
                    IRList.add(new AssignmentIR(">=", g.e.generateTAC(), g.m.generateTAC(), g.generateTAC()));
                }
        }else {
            if(e2 instanceof IntArray){
                Temporary t = new Temporary();
                Variable v = new Variable(t.toString(),"temporary");
                IntArray aux = (IntArray) e2;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
                IRList.add(new AssignmentIR(">=", g.m.generateTAC(), v, g.generateTAC()));
            } else {
                IRList.add(new AssignmentIR(">=", g.m.generateTAC(), g.e.generateTAC(), g.generateTAC()));
            }
        }
    }

    public void visit(Equal e) {
        e.m.accept(this);
        e.e.accept(this);     
        Exp e1 = e.m;
        Exp e2 = e.e;
        if (e1 instanceof IntArray) {
            Temporary t = new Temporary();
            Variable v = new Variable(t.toString(),"temporary");
            IntArray aux = (IntArray) e1;
            IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
            if (e2 instanceof IntArray){
                Temporary t2 = new Temporary();
                Variable v2 = new Variable(t2.toString(),"temporary");
                IntArray aux2 = (IntArray) e2;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux2.i.toString()), aux2.e.generateTAC(), v2));
                IRList.add(new AssignmentIR("=", v, v2, e.generateTAC()));
            } else {
                IRList.add(new AssignmentIR("=", v, e.e.generateTAC(), e.generateTAC()));
            }
        } else if (e1 instanceof IntVar) {
            if (e2 instanceof IntArray){
                Temporary t = new Temporary();
                Variable v = new Variable(t.toString(),"temporary");
                IntArray aux = (IntArray) e2;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
                IRList.add(new AssignmentIR("=", e.m.generateTAC(), v, e.generateTAC()));
            } else {
                IRList.add(new AssignmentIR("=", e.m.generateTAC(), e.e.generateTAC(), e.generateTAC()));
            }
        } else if (e1 instanceof IntLiteral){
                if(e2 instanceof IntArray){
                    Temporary t = new Temporary();
                    Variable v = new Variable(t.toString(),"temporary");
                    IntArray aux = (IntArray) e2;
                    IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
                    IRList.add(new AssignmentIR("=", e1.generateTAC(), v, e.generateTAC()));
                } else {
                    IRList.add(new AssignmentIR("=", e.e.generateTAC(), e.m.generateTAC(), e.generateTAC()));
                }
        }else {
            if(e2 instanceof IntArray){
                Temporary t = new Temporary();
                Variable v = new Variable(t.toString(),"temporary");
                IntArray aux = (IntArray) e2;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
                IRList.add(new AssignmentIR("=", e.m.generateTAC(), v, e.generateTAC()));
            } else {
                IRList.add(new AssignmentIR("=", e.m.generateTAC(), e.e.generateTAC(), e.generateTAC()));
            }
        }
    }

    public void visit(NotEqual n) {
        n.m.accept(this);
        n.e.accept(this);     
        Exp e1 = n.m;
        Exp e2 = n.e;
        if (e1 instanceof IntArray) {
            Temporary t = new Temporary();
            Variable v = new Variable(t.toString(),"temporary");
            IntArray aux = (IntArray) e1;
            IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
            if (e2 instanceof IntArray){
                Temporary t2 = new Temporary();
                Variable v2 = new Variable(t2.toString(),"temporary");
                IntArray aux2 = (IntArray) e2;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux2.i.toString()), aux2.e.generateTAC(), v2));
                IRList.add(new AssignmentIR("#", v, v2, n.generateTAC()));
            } else {
                IRList.add(new AssignmentIR("#", v, n.e.generateTAC(), n.generateTAC()));
            }
        } else if (e1 instanceof IntVar) {
            if (e2 instanceof IntArray){
                Temporary t = new Temporary();
                Variable v = new Variable(t.toString(),"temporary");
                IntArray aux = (IntArray) e2;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
                IRList.add(new AssignmentIR("#", n.m.generateTAC(), v, n.generateTAC()));
            } else {
                IRList.add(new AssignmentIR("#", n.m.generateTAC(), n.e.generateTAC(), n.generateTAC()));
            }
        } else if (e1 instanceof IntLiteral){
                if(e2 instanceof IntArray){
                    Temporary t = new Temporary();
                    Variable v = new Variable(t.toString(),"temporary");
                    IntArray aux = (IntArray) e2;
                    IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
                    IRList.add(new AssignmentIR("#", e1.generateTAC(), v, n.generateTAC()));
                } else {
                    IRList.add(new AssignmentIR("#", n.e.generateTAC(), n.m.generateTAC(), n.generateTAC()));
                }
        }else {
            if(e2 instanceof IntArray){
                Temporary t = new Temporary();
                Variable v = new Variable(t.toString(),"temporary");
                IntArray aux = (IntArray) e2;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
                IRList.add(new AssignmentIR("#", n.m.generateTAC(), v, n.generateTAC()));
            } else {
                IRList.add(new AssignmentIR("#", n.m.generateTAC(), n.e.generateTAC(), n.generateTAC()));
            }
        }
    }

    public void visit(And a) {
        a.m.accept(this);
        a.e.accept(this);     
        Exp e1 = a.m;
        Exp e2 = a.e;
        if (e1 instanceof IntArray) {
            Temporary t = new Temporary();
            Variable v = new Variable(t.toString(),"temporary");
            IntArray aux = (IntArray) e1;
            IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
            if (e2 instanceof IntArray){
                Temporary t2 = new Temporary();
                Variable v2 = new Variable(t2.toString(),"temporary");
                IntArray aux2 = (IntArray) e2;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux2.i.toString()), aux2.e.generateTAC(), v2));
                IRList.add(new AssignmentIR("&", v, v2, a.generateTAC()));
            } else {
                IRList.add(new AssignmentIR("&", v, a.e.generateTAC(), a.generateTAC()));
            }
        } else if (e1 instanceof IntVar) {
            if (e2 instanceof IntArray){
                Temporary t = new Temporary();
                Variable v = new Variable(t.toString(),"temporary");
                IntArray aux = (IntArray) e2;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
                IRList.add(new AssignmentIR("&", a.m.generateTAC(), v, a.generateTAC()));
            } else {
                IRList.add(new AssignmentIR("&", a.m.generateTAC(), a.e.generateTAC(), a.generateTAC()));
            }
        } else if (e1 instanceof IntLiteral){
                if(e2 instanceof IntArray){
                    Temporary t = new Temporary();
                    Variable v = new Variable(t.toString(),"temporary");
                    IntArray aux = (IntArray) e2;
                    IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
                    IRList.add(new AssignmentIR("&", e1.generateTAC(), v, a.generateTAC()));
                } else {
                    IRList.add(new AssignmentIR("&", a.e.generateTAC(), a.m.generateTAC(), a.generateTAC()));
                }
        }else {
            if(e2 instanceof IntArray){
                Temporary t = new Temporary();
                Variable v = new Variable(t.toString(),"temporary");
                IntArray aux = (IntArray) e2;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
                IRList.add(new AssignmentIR("&", a.m.generateTAC(), v, a.generateTAC()));
            } else {
                IRList.add(new AssignmentIR("&", a.m.generateTAC(), a.e.generateTAC(), a.generateTAC()));
            }
        }
    }

    public void visit(Or o) {
        o.m.accept(this);
        o.e.accept(this);     
        Exp e1 = o.m;
        Exp e2 = o.e;
        if (e1 instanceof IntArray) {
            Temporary t = new Temporary();
            Variable v = new Variable(t.toString(),"temporary");
            IntArray aux = (IntArray) e1;
            IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
            if (e2 instanceof IntArray){
                Temporary t2 = new Temporary();
                Variable v2 = new Variable(t2.toString(),"temporary");
                IntArray aux2 = (IntArray) e2;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux2.i.toString()), aux2.e.generateTAC(), v2));
                IRList.add(new AssignmentIR("|", v, v2, o.generateTAC()));
            } else {
                IRList.add(new AssignmentIR("|", v, o.e.generateTAC(), o.generateTAC()));
            }
        } else if (e1 instanceof IntVar) {
            if (e2 instanceof IntArray){
                Temporary t = new Temporary();
                Variable v = new Variable(t.toString(),"temporary");
                IntArray aux = (IntArray) e2;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
                IRList.add(new AssignmentIR("|", o.m.generateTAC(), v, o.generateTAC()));
            } else {
                IRList.add(new AssignmentIR("|", o.m.generateTAC(), o.e.generateTAC(), o.generateTAC()));
            }
        } else if (e1 instanceof IntLiteral){
                if(e2 instanceof IntArray){
                    Temporary t = new Temporary();
                    Variable v = new Variable(t.toString(),"temporary");
                    IntArray aux = (IntArray) e2;
                    IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
                    IRList.add(new AssignmentIR("|", e1.generateTAC(), v, o.generateTAC()));
                } else {
                    IRList.add(new AssignmentIR("|", o.e.generateTAC(), o.m.generateTAC(), o.generateTAC()));
                }
        }else {
            if(e2 instanceof IntArray){
                Temporary t = new Temporary();
                Variable v = new Variable(t.toString(),"temporary");
                IntArray aux = (IntArray) e2;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
                IRList.add(new AssignmentIR("|", o.m.generateTAC(), v, o.generateTAC()));
            } else {
                IRList.add(new AssignmentIR("|", o.m.generateTAC(), o.e.generateTAC(), o.generateTAC()));
            }
        }
    }

    public void visit(Xor x) {
        x.m.accept(this);
        x.e.accept(this);     
        Exp e1 = x.m;
        Exp e2 = x.e;
        if (e1 instanceof IntArray) {
            Temporary t = new Temporary();
            Variable v = new Variable(t.toString(),"temporary");
            IntArray aux = (IntArray) e1;
            IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
            if (e2 instanceof IntArray){
                Temporary t2 = new Temporary();
                Variable v2 = new Variable(t2.toString(),"temporary");
                IntArray aux2 = (IntArray) e2;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux2.i.toString()), aux2.e.generateTAC(), v2));
                IRList.add(new AssignmentIR("!", v, v2, x.generateTAC()));
            } else {
                IRList.add(new AssignmentIR("!", v, x.e.generateTAC(), x.generateTAC()));
            }
        } else if (e1 instanceof IntVar) {
            if (e2 instanceof IntArray){
                Temporary t = new Temporary();
                Variable v = new Variable(t.toString(),"temporary");
                IntArray aux = (IntArray) e2;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
                IRList.add(new AssignmentIR("!", x.m.generateTAC(), v, x.generateTAC()));
            } else {
                IRList.add(new AssignmentIR("!", x.m.generateTAC(), x.e.generateTAC(), x.generateTAC()));
            }
        } else if (e1 instanceof IntLiteral){
                if(e2 instanceof IntArray){
                    Temporary t = new Temporary();
                    Variable v = new Variable(t.toString(),"temporary");
                    IntArray aux = (IntArray) e2;
                    IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
                    IRList.add(new AssignmentIR("!", e1.generateTAC(), v, x.generateTAC()));
                } else {
                    IRList.add(new AssignmentIR("!", x.e.generateTAC(), x.m.generateTAC(), x.generateTAC()));
                }
        }else {
            if(e2 instanceof IntArray){
                Temporary t = new Temporary();
                Variable v = new Variable(t.toString(),"temporary");
                IntArray aux = (IntArray) e2;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
                IRList.add(new AssignmentIR("!", x.m.generateTAC(), v, x.generateTAC()));
            } else {
                IRList.add(new AssignmentIR("!", x.m.generateTAC(), x.e.generateTAC(), x.generateTAC()));
            }
        }
    }

    public void visit(Not n) {
        n.e.accept(this);
        if (n.e instanceof IntArray) {
            Temporary t = new Temporary();
            Variable v = new Variable(t.toString(),"temporary");
            IntArray aux = (IntArray) n.e;
            IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
            IRList.add(new UnaryAssignmentIR("~", v, n.generateTAC()));
        } else {
            IRList.add(new UnaryAssignmentIR("~", n.e.generateTAC(), n.generateTAC()));
        }
    }

    public void visit(IntLiteral i) {
    }

    public void visit(CallStmt c) {
        c.i.accept(this);
        IRList.add(new CallIR(c.i.toString(), "0", c.generateTAC()));
    }

    public void visit(UncallStmt u) {
        u.i.accept(this);
        IRList.add(new UncallIR(u.i.toString(), "0", u.generateTAC()));
    }

    public void visit(ReadStmt r) {
        r.l.accept(this);
        if (r.l instanceof IntVar){
            IntVar v = (IntVar) r.l;
            IRList.add(new ParameterIR(currentScope.lookupVariable(v.i.toString())));
            IRList.add(new CallIR("read","1",null));
        } else {
            IntArray v = (IntArray) r.l;
            Temporary temp = new Temporary();
            Variable tempV = new Variable(temp.toString(),"temporary");
            IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(v.i.toString()),v.e.generateTAC(),tempV));
            IRList.add(new ParameterIR(tempV));
            IRList.add(new CallIR("read","1",null));
            IRList.add(new IndexedAssignmentIR2(tempV,v.e.generateTAC(),currentScope.lookupVariable(v.i.toString()))); //enviando o dado da variável temporaria p/ o vetor.
        }
    }

    public void visit(WriteStmt w) {
        w.l.accept(this);
        if (w.l instanceof IntVar){
            IntVar v = (IntVar) w.l;
            IRList.add(new ParameterIR(currentScope.lookupVariable(v.i.toString())));
            IRList.add(new CallIR("write","1",null));
        } else {
            IntArray v = (IntArray) w.l;
            Temporary temp = new Temporary();
            Variable tempV = new Variable(temp.toString(),"temporary");
            IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(v.i.toString()),v.e.generateTAC(),tempV));
            IRList.add(new ParameterIR(tempV));
            IRList.add(new CallIR("write","1",null));
        }
    }
    
    public void visit(UnaryMinus m) {
        m.e.accept(this);
        Temporary temp = new Temporary();
        Variable tempV = new Variable(temp.toString(), "temporary");
        if (m.e instanceof IntArray) {
            Temporary t = new Temporary();
            Variable v = new Variable(t.toString(),"temporary");
            IntArray aux = (IntArray) m.e;
            IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
            IRList.add(new UnaryAssignmentIR("-", v, tempV));
        } else {
            IRList.add(new UnaryAssignmentIR("-", m.e.generateTAC(), tempV));
        }
        m.t = tempV;
    }

    public void visit(LValueExp1 l) {
        l.l.accept(this);
    }

    public void visit(BracketedExp b) {
        b.e.accept(this);
        b.t = b.e.generateTAC();
    }

    public void visit(Identifier i) {
    }

    public void visit(PEModStmt pem) {
        pem.l.accept(this);
        pem.e.accept(this);
        if(pem.l instanceof IntArray){
            IntArray arrayAux = (IntArray) pem.l;
            if(pem.e instanceof IntVar){
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(arrayAux.i.toString()), arrayAux.e.generateTAC(), pem.generateTAC()));
                IRList.add(new AssignmentIR("+", pem.generateTAC(), pem.e.generateTAC(), pem.generateTAC()));
                IRList.add(new IndexedAssignmentIR2(pem.generateTAC(), arrayAux.e.generateTAC(), currentScope.lookupVariable(arrayAux.i.toString())));
            }else if(pem.e instanceof IntArray){
                Temporary t = new Temporary();
                Variable v = new Variable(t.toString(),"temporary");
                IntArray arrayAux2 = (IntArray) pem.e;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(arrayAux.i.toString()), arrayAux.e.generateTAC(), pem.generateTAC()));
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(arrayAux2.i.toString()), arrayAux2.e.generateTAC(), v));
                IRList.add(new AssignmentIR("+", pem.generateTAC(), v, pem.generateTAC()));
            }else{
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(arrayAux.i.toString()), arrayAux.e.generateTAC(), pem.generateTAC()));
                IRList.add(new AssignmentIR("+", pem.generateTAC(), pem.e.generateTAC(), pem.generateTAC()));
                IRList.add(new IndexedAssignmentIR2(pem.generateTAC(), arrayAux.e.generateTAC(), currentScope.lookupVariable(arrayAux.i.toString())));
            }
        }else if(pem.l instanceof IntVar){
            if(pem.e instanceof IntVar){
                IRList.add(new AssignmentIR("+", pem.l.generateTAC(), pem.e.generateTAC(), pem.l.generateTAC()));
            }else if(pem.e instanceof IntArray){
                IntArray arrayAux = (IntArray) pem.e;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(arrayAux.i.toString()),arrayAux.e.generateTAC(), pem.generateTAC()));
                IRList.add(new AssignmentIR("+", pem.l.generateTAC(), pem.generateTAC(), pem.l.generateTAC()));
            }else{
                IRList.add(new AssignmentIR("+", pem.l.generateTAC(), pem.e.generateTAC(), pem.l.generateTAC()));
            }
        }       
    }

    public void visit(MEModStmt mem) {
        mem.l.accept(this);
        mem.e.accept(this);
        if(mem.l instanceof IntArray){
            IntArray arrayAux = (IntArray) mem.l;
            if(mem.e instanceof IntVar){
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(arrayAux.i.toString()), arrayAux.e.generateTAC(), mem.generateTAC()));
                IRList.add(new AssignmentIR("-", mem.generateTAC(), mem.e.generateTAC(), mem.generateTAC()));
                IRList.add(new IndexedAssignmentIR2(mem.generateTAC(), arrayAux.e.generateTAC(), currentScope.lookupVariable(arrayAux.i.toString())));
            }else if(mem.e instanceof IntArray){
                Temporary t = new Temporary();
                Variable v = new Variable(t.toString(),"temporary");
                IntArray arrayAux2 = (IntArray) mem.e;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(arrayAux.i.toString()), arrayAux.e.generateTAC(), mem.generateTAC()));
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(arrayAux2.i.toString()), arrayAux2.e.generateTAC(), v));
                IRList.add(new AssignmentIR("-", mem.generateTAC(), v, mem.generateTAC()));
            }else{
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(arrayAux.i.toString()), arrayAux.e.generateTAC(), mem.generateTAC()));
                IRList.add(new AssignmentIR("-", mem.generateTAC(), mem.e.generateTAC(), mem.generateTAC()));
                IRList.add(new IndexedAssignmentIR2(mem.generateTAC(), arrayAux.e.generateTAC(), currentScope.lookupVariable(arrayAux.i.toString())));
            }
        }else if(mem.l instanceof IntVar){
            if(mem.e instanceof IntVar){
                IRList.add(new AssignmentIR("-", mem.l.generateTAC(), mem.e.generateTAC(), mem.l.generateTAC()));
            }else if(mem.e instanceof IntArray){
                IntArray arrayAux = (IntArray) mem.e;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(arrayAux.i.toString()),arrayAux.e.generateTAC(), mem.generateTAC()));
                IRList.add(new AssignmentIR("-", mem.l.generateTAC(), mem.generateTAC(), mem.l.generateTAC()));
            }else{
                IRList.add(new AssignmentIR("-", mem.l.generateTAC(), mem.e.generateTAC(), mem.l.generateTAC()));
            }
        }       
    }

    public void visit(XEModStmt xem) {
        xem.l.accept(this);
        xem.e.accept(this);
        if(xem.l instanceof IntArray){
            IntArray arrayAux = (IntArray) xem.l;
            if(xem.e instanceof IntVar){
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(arrayAux.i.toString()), arrayAux.e.generateTAC(), xem.generateTAC()));
                IRList.add(new AssignmentIR("!", xem.generateTAC(), xem.e.generateTAC(), xem.generateTAC()));
                IRList.add(new IndexedAssignmentIR2(xem.generateTAC(), arrayAux.e.generateTAC(), currentScope.lookupVariable(arrayAux.i.toString())));
            }else if(xem.e instanceof IntArray){
                Temporary t = new Temporary();
                Variable v = new Variable(t.toString(),"temporary");
                IntArray arrayAux2 = (IntArray) xem.e;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(arrayAux.i.toString()), arrayAux.e.generateTAC(), xem.generateTAC()));
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(arrayAux2.i.toString()), arrayAux2.e.generateTAC(), v));
                IRList.add(new AssignmentIR("!", xem.generateTAC(), v, xem.generateTAC()));
            }else{
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(arrayAux.i.toString()), arrayAux.e.generateTAC(), xem.generateTAC()));
                IRList.add(new AssignmentIR("!", xem.generateTAC(), xem.e.generateTAC(), xem.generateTAC()));
                IRList.add(new IndexedAssignmentIR2(xem.generateTAC(), arrayAux.e.generateTAC(), currentScope.lookupVariable(arrayAux.i.toString())));
            }
        }else if(xem.l instanceof IntVar){
            if(xem.e instanceof IntVar){
                IRList.add(new AssignmentIR("!", xem.l.generateTAC(), xem.e.generateTAC(), xem.l.generateTAC()));
            }else if(xem.e instanceof IntArray){
                IntArray arrayAux = (IntArray) xem.e;
                IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(xem.e.toString()),arrayAux.e.generateTAC(), xem.generateTAC()));
                IRList.add(new AssignmentIR("!", xem.l.generateTAC(), xem.generateTAC(), xem.l.generateTAC()));
            }else{
                IRList.add(new AssignmentIR("!", xem.l.generateTAC(), xem.e.generateTAC(), xem.l.generateTAC()));
            }
        }
    }

    public void visit(SwapStmt ss) {
        ss.l1.accept(this);
        ss.l2.accept(this);
        if (ss.l1 instanceof IntArray && ss.l2 instanceof IntVar) {
            Temporary temp = new Temporary();
            Variable v = new Variable(temp.toString(),"temporary");
            IntArray aux = (IntArray) ss.l1;
            IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
            IRList.add(new IndexedAssignmentIR2(ss.l2.generateTAC(), aux.e.generateTAC(), currentScope.lookupVariable(aux.i.toString())));
            IRList.add(new CopyIR(v, ss.l2.generateTAC()));
        } else if (ss.l1 instanceof IntVar && ss.l2 instanceof IntArray) {
            Temporary temp = new Temporary();
            Variable v = new Variable(temp.toString(),"temporary");
            IntArray aux = (IntArray) ss.l2;
            IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
            IRList.add(new IndexedAssignmentIR2(ss.l1.generateTAC(), aux.e.generateTAC(), currentScope.lookupVariable(aux.i.toString())));
            IRList.add(new CopyIR(v, ss.l1.generateTAC()));
        } else if (ss.l1 instanceof IntArray && ss.l2 instanceof IntArray) {
            Temporary temp = new Temporary();
            Variable v = new Variable(temp.toString(),"temporary");
            IntArray aux = (IntArray) ss.l1;
            IntArray aux2 = (IntArray) ss.l2;
            IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux.i.toString()), aux.e.generateTAC(), v));
            IRList.add(new IndexedAssignmentIR1(currentScope.lookupVariable(aux2.i.toString()), aux2.e.generateTAC(), ss.generateTAC()));
            IRList.add(new IndexedAssignmentIR2(ss.generateTAC(), aux.e.generateTAC(), currentScope.lookupVariable(aux.i.toString())));
            IRList.add(new IndexedAssignmentIR2(v, aux2.e.generateTAC(), currentScope.lookupVariable(aux2.i.toString())));
        } else {
            IRList.add(new CopyIR(ss.l1.generateTAC(), ss.generateTAC()));
            IRList.add(new CopyIR(ss.l2.generateTAC(), ss.l1.generateTAC()));
            IRList.add(new CopyIR(ss.generateTAC(), ss.l2.generateTAC()));
        }
    }
}
