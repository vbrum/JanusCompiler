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
public class TypeCheckingVisitor implements TypeVisitor {

    private Scope currentScope;
    private ProcedureSymbolTable currProcedure;
    private SymbolTable symTable;
    public boolean errorDetected;
    private int blockNumber;
    public String errorMessage;

    public TypeCheckingVisitor(Scope s) {
        currentScope = s;
        errorDetected = false;
        symTable = (SymbolTable) currentScope;
        blockNumber = 0;
        errorMessage = "";
    }

    public boolean isBoolean(Type t) {
        if (t instanceof BooleanType) {
            return true;
        }
        return false;
    }

    public boolean isInteger(Type t) {
        if (t instanceof IntVar || t instanceof IntArray || t instanceof IntegerType) {
            return true;
        }
        return false;
    }

    public Type visit(Program p) {
        for (int i = 0; i < p.dl.size(); i++) {
            p.dl.elementAt(i).accept(this);
        }
        for (int i = 0; i < p.pl.size(); i++) {
            p.pl.elementAt(i).accept(this);
        }
        return null;
    }

    public Type visit(Proc pr) {
        currentScope = currentScope.enterScope(pr.i.s);
        currProcedure = (ProcedureSymbolTable) currentScope;
        pr.i.accept(this);
        for (int i = 0; i < pr.sl.size(); i++) {
            pr.sl.elementAt(i).accept(this);
        }
        currentScope = currentScope.exitScope();
        return null;
    }

    public Type visit(IntArrayDecl iad) {
        iad.i.accept(this);
        return null;
    }

    public Type visit(IntDecl id) {
        id.i.accept(this);
        return null;
    }

    public Type visit(IfStmt ifs) {
        if (!isBoolean(ifs.e1.accept(this))) {
            System.err.println("Non-boolean expression used as the condition of if statement at line " + ifs.lineNumE1 + ", character " + ifs.charNumE1);
            errorMessage += "\n" + "Non-boolean expression used as the condition of if statement at line " + ifs.lineNumE1 + ", character " + ifs.charNumE1;
            errorDetected = true;
        }
        for (int i = 0; i < ifs.sl.size(); i++) {
            ifs.sl.elementAt(i).accept(this);
        }
        if (!isBoolean(ifs.e2.accept(this))) {
            System.err.println("Non-boolean expression used as the condition of if statement at line " + ifs.lineNumE2 + ", character " + ifs.charNumE2);
            errorMessage += "\n" + "Non-boolean expression used as the condition of if statement at line " + ifs.lineNumE2 + ", character " + ifs.charNumE2;
            errorDetected = true;
        }
        return null;
    }

    public Type visit(IfElseStmt ifss) {
        if (!isBoolean(ifss.e1.accept(this))) {
            System.err.println("Non-boolean expression used as the condition of if statement at line " + ifss.lineNumE1 + ", character " + ifss.charNumE1);
            errorMessage += "\n" + "Non-boolean expression used as the condition of if statement at line " + ifss.lineNumE1 + ", character " + ifss.charNumE1;
            errorDetected = true;
        }
        for (int i = 0; i < ifss.sl1.size(); i++) {
            ifss.sl1.elementAt(i).accept(this);
        }
        for (int i = 0; i < ifss.sl2.size(); i++) {
            ifss.sl2.elementAt(i).accept(this);
        }
        if (!isBoolean(ifss.e2.accept(this))) {
            System.err.println("Non-boolean expression used as the condition of if statement at line " + ifss.lineNumE2 + ", character " + ifss.charNumE2);
            errorMessage += "\n" + "Non-boolean expression used as the condition of if statement at line " + ifss.lineNumE2 + ", character " + ifss.charNumE2;
            errorDetected = true;
        }
        return null;
    }

    public Type visit(DoLoopStmt dls) {
        if (!isBoolean(dls.e1.accept(this))) {
            System.err.println("Non-boolean expression used as the condition of do statement at line " + dls.lineNumE1 + ", character " + dls.charNumE1);
            errorMessage += "\n" + "Non-boolean expression used as the condition of do statement at line " + dls.lineNumE1 + ", character " + dls.charNumE1;
            errorDetected = true;
        }
        for (int i = 0; i < dls.sl1.size(); i++) {
            dls.sl1.elementAt(i).accept(this);
        }
        for (int i = 0; i < dls.sl2.size(); i++) {
            dls.sl2.elementAt(i).accept(this);
        }
        if (!isBoolean(dls.e2.accept(this))) {
            System.err.println("Non-boolean expression used as the condition of do statement at line " + dls.lineNumE2 + ", character " + dls.charNumE2);
            errorMessage += "\n" + "Non-boolean expression used as the condition of do statement at line " + dls.lineNumE2 + ", character " + dls.charNumE2;
            errorDetected = true;
        }
        return null;
    }

    public Type visit(DoStmt ds) {
        if (!isBoolean(ds.e1.accept(this))) {
            System.err.println("Non-boolean expression used as the condition of do statement at line " + ds.lineNumE1 + ", character " + ds.charNumE1);
            errorMessage += "\n" + "Non-boolean expression used as the condition of do statement at line " + ds.lineNumE1 + ", character " + ds.charNumE1;
            errorDetected = true;
        }
        for (int i = 0; i < ds.sl.size(); i++) {
            ds.sl.elementAt(i).accept(this);
        }
        if (!isBoolean(ds.e2.accept(this))) {
            System.err.println("Non-boolean expression used as the condition of do statement at line " + ds.lineNumE2 + ", character " + ds.charNumE2);
            errorMessage += "\n" + "Non-boolean expression used as the condition of do statement at line " + ds.lineNumE2 + ", character " + ds.charNumE2;
            errorDetected = true;
        }
        return null;
    }

    @Override
    public Type visit(LoopStmt ls) {
        if (!isBoolean(ls.e1.accept(this))) {
            System.err.println("Non-boolean expression used as the condition of do statement at line " + ls.lineNumE1 + ", character " + ls.charNumE1);
            errorMessage += "\n" + "Non-boolean expression used as the condition of do statement at line " + ls.lineNumE1 + ", character " + ls.charNumE1;
            errorDetected = true;
        }
        for (int i = 0; i < ls.sl.size(); i++) {
            ls.sl.elementAt(i).accept(this);
        }
        if (!isBoolean(ls.e2.accept(this))) {
            System.err.println("Non-boolean expression used as the condition of do statement at line " + ls.lineNumE2 + ", character " + ls.charNumE2);
            errorMessage += "\n" + "Non-boolean expression used as the condition of do statement at line " + ls.lineNumE2 + ", character " + ls.charNumE2;
            errorDetected = true;
        }
        return null;
    }

    public Type visit(IntVar v) {
        return new IntVar(v.i);
    }

    public Type visit(IntArray ia) {
        return new IntArray(ia.i, ia.e);
    }

    //Exp e
    //LValue l
    public Type visit(PEModStmt pem) {
        Type t = pem.l.accept(this);
        boolean internalError = false;
        if (t instanceof IntVar) {
            IntVar iv = (IntVar) t;
            if (symTable.isProcedure(iv.i.s)) {
                internalError = true;
                errorDetected = true;
                System.err.println("Invalid l-value: " + iv.i.s + " is a procedure, at line " + pem.lineNum);
                errorMessage += "\n" + "Invalid l-value: " + iv.i.s + " is a procedure, at line " + pem.lineNum;
            }
        } else if (t instanceof IntArray) {
            IntArray ia = (IntArray) t;
            if (symTable.isProcedure(ia.i.s)) {
                internalError = true;
                errorDetected = true;
                System.err.println("Invalid l-value: " + ia.i.s + " is a procedure, at line " + pem.lineNum);
                errorMessage += "\n" + "Invalid l-value: " + ia.i.s + " is a procedure, at line " + pem.lineNum;
            }
        }
        Type t2 = pem.e.accept(this);
        if (t2 instanceof IntVar) {
            IntVar iv = (IntVar) t2;
            if (symTable.isProcedure(iv.i.s)) {
                internalError = true;
                errorDetected = true;
                System.err.println("Invalid r-value: " + iv.i.s + " is a procedure, at line " + pem.lineNum);
                errorMessage += "\n" + "Invalid r-value: " + iv.i.s + " is a procedure, at line " + pem.lineNum;
            }
        } else if (t2 instanceof IntArray) {
            IntArray ia = (IntArray) t2;
            if (symTable.isProcedure(ia.i.s)) {
                internalError = true;
                errorDetected = true;
                System.err.println("Invalid r-value: " + ia.i.s + " is a procedure, at line " + pem.lineNum);
                errorMessage += "\n" + "Invalid r-value: " + ia.i.s + " is a procedure, at line " + pem.lineNum;
            }
        }
        if ((!isInteger(t) || !isInteger(t2)) && !internalError) {
            errorDetected = true;
            System.err.println("Type mismatch during assignment at line " + pem.lineNum + ", character " + pem.charNum);
            errorMessage += "\n" + "Type mismatch during assignment at line " + pem.lineNum + ", character " + pem.charNum;
        }

        return null;
    }

    @Override
    public Type visit(MEModStmt mem) {
        Type t = mem.l.accept(this);
        boolean internalError = false;
        if (t instanceof IntVar) {
            IntVar iv = (IntVar) t;
            if (symTable.isProcedure(iv.i.s)) {
                internalError = true;
                errorDetected = true;
                System.err.println("Invalid l-value: " + iv.i.s + " is a procedure, at line " + mem.lineNum);
                errorMessage += "\n" + "Invalid l-value: " + iv.i.s + " is a procedure, at line " + mem.lineNum;
            }
        } else if (t instanceof IntArray) {
            IntArray ia = (IntArray) t;
            if (symTable.isProcedure(ia.i.s)) {
                internalError = true;
                errorDetected = true;
                System.err.println("Invalid l-value: " + ia.i.s + " is a procedure, at line " + mem.lineNum);
                errorMessage += "\n" + "Invalid l-value: " + ia.i.s + " is a procedure, at line " + mem.lineNum;
            }
        }
        Type t2 = mem.e.accept(this);
        if (t2 instanceof IntVar) {
            IntVar iv = (IntVar) t2;
            if (symTable.isProcedure(iv.i.s)) {
                internalError = true;
                errorDetected = true;
                System.err.println("Invalid r-value: " + iv.i.s + " is a procedure, at line " + mem.lineNum);
                errorMessage += "\n" + "Invalid r-value: " + iv.i.s + " is a procedure, at line " + mem.lineNum;
            }
        } else if (t2 instanceof IntArray) {
            IntArray ia = (IntArray) t2;
            if (symTable.isProcedure(ia.i.s)) {
                internalError = true;
                errorDetected = true;
                System.err.println("Invalid r-value: " + ia.i.s + " is a procedure, at line " + mem.lineNum);
                errorMessage += "\n" + "Invalid r-value: " + ia.i.s + " is a procedure, at line " + mem.lineNum;
            }
        }
        if ((!isInteger(t) || !isInteger(t2)) && !internalError) {
            errorDetected = true;
            System.err.println("Type mismatch during assignment at line " + mem.lineNum + ", character " + mem.charNum);
            errorMessage += "\n" + "Type mismatch during assignment at line " + mem.lineNum + ", character " + mem.charNum;
        }

        return null;
    }

    @Override
    public Type visit(XEModStmt xem) {
        Type t = xem.l.accept(this);
        boolean internalError = false;
        if (t instanceof IntVar) {
            IntVar iv = (IntVar) t;
            if (symTable.isProcedure(iv.i.s)) {
                internalError = true;
                errorDetected = true;
                System.err.println("Invalid l-value: " + iv.i.s + " is a procedure, at line " + xem.lineNum);
                errorMessage += "\n" + "Invalid l-value: " + iv.i.s + " is a procedure, at line " + xem.lineNum;
            }
        } else if (t instanceof IntArray) {
            IntArray ia = (IntArray) t;
            if (symTable.isProcedure(ia.i.s)) {
                internalError = true;
                errorDetected = true;
                System.err.println("Invalid l-value: " + ia.i.s + " is a procedure, at line " + xem.lineNum);
                errorMessage += "\n" + "Invalid l-value: " + ia.i.s + " is a procedure, at line " + xem.lineNum;
            }
        }
        Type t2 = xem.e.accept(this);
        if (t2 instanceof IntVar) {
            IntVar iv = (IntVar) t2;
            if (symTable.isProcedure(iv.i.s)) {
                internalError = true;
                errorDetected = true;
                System.err.println("Invalid r-value: " + iv.i.s + " is a procedure, at line " + xem.lineNum);
                errorMessage += "\n" + "Invalid r-value: " + iv.i.s + " is a procedure, at line " + xem.lineNum;
            }
        } else if (t2 instanceof IntArray) {
            IntArray ia = (IntArray) t2;
            if (symTable.isProcedure(ia.i.s)) {
                internalError = true;
                errorDetected = true;
                System.err.println("Invalid r-value: " + ia.i.s + " is a procedure, at line " + xem.lineNum);
                errorMessage += "\n" + "Invalid r-value: " + ia.i.s + " is a procedure, at line " + xem.lineNum;
            }
        }
        if ((!isInteger(t) || !isInteger(t2)) && !internalError) {
            errorDetected = true;
            System.err.println("Type mismatch during assignment at line " + xem.lineNum + ", character " + xem.charNum);
            errorMessage += "\n" + "Type mismatch during assignment at line " + xem.lineNum + ", character " + xem.charNum;
        }

        return null;
    }

    @Override
    public Type visit(SwapStmt ss) {
        Type t = ss.l1.accept(this);
        boolean internalError = false;
        if (t instanceof IntVar) {
            IntVar iv = (IntVar) t;
            if (symTable.isProcedure(iv.i.s)) {
                internalError = true;
                errorDetected = true;
                System.err.println("Invalid l-value: " + iv.i.s + " is a procedure, at line " + ss.lineNum);
                errorMessage += "\n" + "Invalid l-value: " + iv.i.s + " is a procedure, at line " + ss.lineNum;
            }
        } else if (t instanceof IntArray) {
            IntArray ia = (IntArray) t;
            if (symTable.isProcedure(ia.i.s)) {
                internalError = true;
                errorDetected = true;
                System.err.println("Invalid l-value: " + ia.i.s + " is a procedure, at line " + ss.lineNum);
                errorMessage += "\n" + "Invalid l-value: " + ia.i.s + " is a procedure, at line " + ss.lineNum;
            }
        }
        Type t2 = ss.l2.accept(this);
        if (t2 instanceof IntVar) {
            IntVar iv = (IntVar) t2;
            if (symTable.isProcedure(iv.i.s)) {
                internalError = true;
                errorDetected = true;
                System.err.println("Invalid r-value: " + iv.i.s + " is a procedure, at line " + ss.lineNum);
                errorMessage += "\n" + "Invalid r-value: " + iv.i.s + " is a procedure, at line " + ss.lineNum;
            }
        } else if (t2 instanceof IntArray) {
            IntArray ia = (IntArray) t2;
            if (symTable.isProcedure(ia.i.s)) {
                internalError = true;
                errorDetected = true;
                System.err.println("Invalid r-value: " + ia.i.s + " is a procedure, at line " + ss.lineNum);
                errorMessage += "\n" + "Invalid r-value: " + ia.i.s + " is a procedure, at line " + ss.lineNum;
            }
        }
        if ((!isInteger(t) || !isInteger(t2)) && !internalError) {
            errorDetected = true;
            System.err.println("Type mismatch during swapping at line " + ss.lineNum + ", character " + ss.charNum);
            errorMessage += "\n" + "Type mismatch during swapping at line " + ss.lineNum + ", character " + ss.charNum;
        }

        return null;
    }

    public Type visit(Plus p) {
        Type t = p.e.accept(this);
        Type t2 = p.m.accept(this);
        boolean invalidOperands = false;
        if (t instanceof IntVar) {
            IntVar iv = (IntVar) t;
            if (symTable.isProcedure(iv.i.s)) {
                invalidOperands = true;
                errorDetected = true;
                System.err.println("Invalid operands for + operator, at line " + p.lineNum + ", character" + p.charNum);
                errorMessage += "\n" + "Invalid operands for + operator, at line " + p.lineNum + ", character" + p.charNum;
            }
        } else if (t instanceof IntArray) {
            IntArray ia = (IntArray) t;
            if (symTable.isProcedure(ia.i.s)) {
                invalidOperands = true;
                errorDetected = true;
                System.err.println("Invalid operands for + operator, at line " + p.lineNum + ", character" + p.charNum);
                errorMessage += "\n" + "Invalid operands for + operator, at line " + p.lineNum + ", character" + p.charNum;
            }
        }
        if (t2 instanceof IntVar) {
            IntVar iv = (IntVar) t2;
            if (symTable.isProcedure(iv.i.s)) {
                invalidOperands = true;
                errorDetected = true;
                System.err.println("Invalid operands for + operator, at line " + p.lineNum + ", character" + p.charNum);
                errorMessage += "\n" + "Invalid operands for + operator, at line " + p.lineNum + ", character" + p.charNum;
            }
        } else if (t2 instanceof IntArray) {
            IntArray ia = (IntArray) t2;
            if (symTable.isProcedure(ia.i.s)) {
                invalidOperands = true;
                errorDetected = true;
                System.err.println("Invalid operands for + operator, at line " + p.lineNum + ", character" + p.charNum);
                errorMessage += "\n" + "Invalid operands for + operator, at line " + p.lineNum + ", character" + p.charNum;
            }
        }
        if (!invalidOperands) {
            if (!isInteger(p.e.accept(this)) || !isInteger(p.m.accept(this))) {
                errorDetected = true;
                System.err.println("Non-integer operand for operator +, at line " + p.lineNum + ", character " + p.charNum);
                errorMessage += "\n" + "Non-integer operand for operator +, at line " + p.lineNum + ", character " + p.charNum;
            }
        }
        return new IntegerType();
    }

    @Override
    public Type visit(Minus m) {
        Type t = m.e.accept(this);
        Type t2 = m.m.accept(this);
        boolean invalidOperands = false;
        if (t instanceof IntVar) {
            IntVar iv = (IntVar) t;
            if (symTable.isProcedure(iv.i.s)) {
                invalidOperands = true;
                errorDetected = true;
                System.err.println("Invalid operands for - operator, at line " + m.lineNum + ", character" + m.charNum);
                errorMessage += "\n" + "Invalid operands for - operator, at line " + m.lineNum + ", character" + m.charNum;
            }
        } else if (t instanceof IntArray) {
            IntArray ia = (IntArray) t;
            if (symTable.isProcedure(ia.i.s)) {
                invalidOperands = true;
                errorDetected = true;
                System.err.println("Invalid operands for - operator, at line " + m.lineNum + ", character" + m.charNum);
                errorMessage += "\n" + "Invalid operands for - operator, at line " + m.lineNum + ", character" + m.charNum;
            }
        }
        if (t2 instanceof IntVar) {
            IntVar iv = (IntVar) t2;
            if (symTable.isProcedure(iv.i.s)) {
                invalidOperands = true;
                errorDetected = true;
                System.err.println("Invalid operands for - operator, at line " + m.lineNum + ", character" + m.charNum);
                errorMessage += "\n" + "Invalid operands for - operator, at line " + m.lineNum + ", character" + m.charNum;
            }
        } else if (t2 instanceof IntArray) {
            IntArray ia = (IntArray) t2;
            if (symTable.isProcedure(ia.i.s)) {
                invalidOperands = true;
                errorDetected = true;
                System.err.println("Invalid operands for - operator, at line " + m.lineNum + ", character" + m.charNum);
                errorMessage += "\n" + "Invalid operands for - operator, at line " + m.lineNum + ", character" + m.charNum;
            }
        }
        if (!invalidOperands) {
            if (!isInteger(m.e.accept(this)) || !isInteger(m.m.accept(this))) {
                errorDetected = true;
                System.err.println("Non-integer operand for operator -, at line " + m.lineNum + ", character " + m.charNum);
                errorMessage += "\n" + "Non-integer operand for operator -, at line " + m.lineNum + ", character " + m.charNum;
            }
        }
        return new IntegerType();
    }

    @Override
    public Type visit(Mult m) {
        Type t = m.e.accept(this);
        Type t2 = m.m.accept(this);
        boolean invalidOperands = false;
        if (t instanceof IntVar) {
            IntVar iv = (IntVar) t;
            if (symTable.isProcedure(iv.i.s)) {
                invalidOperands = true;
                errorDetected = true;
                System.err.println("Invalid operands for * operator, at line " + m.lineNum + ", character" + m.charNum);
                errorMessage += "\n" + "Invalid operands for * operator, at line " + m.lineNum + ", character" + m.charNum;
            }
        } else if (t instanceof IntArray) {
            IntArray ia = (IntArray) t;
            if (symTable.isProcedure(ia.i.s)) {
                invalidOperands = true;
                errorDetected = true;
                System.err.println("Invalid operands for * operator, at line " + m.lineNum + ", character" + m.charNum);
                errorMessage += "\n" + "Invalid operands for * operator, at line " + m.lineNum + ", character" + m.charNum;
            }
        }
        if (t2 instanceof IntVar) {
            IntVar iv = (IntVar) t2;
            if (symTable.isProcedure(iv.i.s)) {
                invalidOperands = true;
                errorDetected = true;
                System.err.println("Invalid operands for * operator, at line " + m.lineNum + ", character" + m.charNum);
                errorMessage += "\n" + "Invalid operands for * operator, at line " + m.lineNum + ", character" + m.charNum;
            }
        } else if (t2 instanceof IntArray) {
            IntArray ia = (IntArray) t2;
            if (symTable.isProcedure(ia.i.s)) {
                invalidOperands = true;
                errorDetected = true;
                System.err.println("Invalid operands for * operator, at line " + m.lineNum + ", character" + m.charNum);
                errorMessage += "\n" + "Invalid operands for * operator, at line " + m.lineNum + ", character" + m.charNum;
            }
        }
        if (!invalidOperands) {
            if (!isInteger(m.e.accept(this)) || !isInteger(m.m.accept(this))) {
                errorDetected = true;
                System.err.println("Non-integer operand for operator *, at line " + m.lineNum + ", character " + m.charNum);
                errorMessage += "\n" + "Non-integer operand for operator *, at line " + m.lineNum + ", character " + m.charNum;
            }
        }
        return new IntegerType();
    }

    public Type visit(Mod m) {
        Type t = m.e.accept(this);
        Type t2 = m.m.accept(this);
        boolean invalidOperands = false;
        if (t instanceof IntVar) {
            IntVar iv = (IntVar) t;
            if (symTable.isProcedure(iv.i.s)) {
                invalidOperands = true;
                errorDetected = true;
                System.err.println("Invalid operands for \\ operator, at line " + m.lineNum + ", character" + m.charNum);
                errorMessage += "\n" + "Invalid operands for \\ operator, at line " + m.lineNum + ", character" + m.charNum;
            }
        } else if (t instanceof IntArray) {
            IntArray ia = (IntArray) t;
            if (symTable.isProcedure(ia.i.s)) {
                invalidOperands = true;
                errorDetected = true;
                System.err.println("Invalid operands for \\ operator, at line " + m.lineNum + ", character" + m.charNum);
                errorMessage += "\n" + "Invalid operands for \\ operator, at line " + m.lineNum + ", character" + m.charNum;
            }
        }
        if (t2 instanceof IntVar) {
            IntVar iv = (IntVar) t2;
            if (symTable.isProcedure(iv.i.s)) {
                invalidOperands = true;
                errorDetected = true;
                System.err.println("Invalid operands for \\ operator, at line " + m.lineNum + ", character" + m.charNum);
                errorMessage += "\n" + "Invalid operands for \\ operator, at line " + m.lineNum + ", character" + m.charNum;
            }
        } else if (t2 instanceof IntArray) {
            IntArray ia = (IntArray) t2;
            if (symTable.isProcedure(ia.i.s)) {
                invalidOperands = true;
                errorDetected = true;
                System.err.println("Invalid operands for \\ operator, at line " + m.lineNum + ", character" + m.charNum);
                errorMessage += "\n" + "Invalid operands for \\ operator, at line " + m.lineNum + ", character" + m.charNum;
            }
        }
        if (!invalidOperands) {
            if (!isInteger(m.e.accept(this)) || !isInteger(m.m.accept(this))) {
                errorDetected = true;
                System.err.println("Non-integer operand for operator \\, at line " + m.lineNum + ", character " + m.charNum);
                errorMessage += "\n" + "Non-integer operand for operator \\, at line " + m.lineNum + ", character " + m.charNum;
            }
        }
        return new IntegerType();
    }

    public Type visit(Div d) {
        Type t = d.e.accept(this);
        Type t2 = d.m.accept(this);
        boolean invalidOperands = false;
        if (t instanceof IntVar) {
            IntVar iv = (IntVar) t;
            if (symTable.isProcedure(iv.i.s)) {
                invalidOperands = true;
                errorDetected = true;
                System.err.println("Invalid operands for / operator, at line " + d.lineNum + ", character" + d.charNum);
                errorMessage += "\n" + "Invalid operands for / operator, at line " + d.lineNum + ", character" + d.charNum;
            }
        } else if (t instanceof IntArray) {
            IntArray ia = (IntArray) t;
            if (symTable.isProcedure(ia.i.s)) {
                invalidOperands = true;
                errorDetected = true;
                System.err.println("Invalid operands for / operator, at line " + d.lineNum + ", character" + d.charNum);
                errorMessage += "\n" + "Invalid operands for / operator, at line " + d.lineNum + ", character" + d.charNum;
            }
        }
        if (t2 instanceof IntVar) {
            IntVar iv = (IntVar) t2;
            if (symTable.isProcedure(iv.i.s)) {
                invalidOperands = true;
                errorDetected = true;
                System.err.println("Invalid operands for / operator, at line " + d.lineNum + ", character" + d.charNum);
                errorMessage += "\n" + "Invalid operands for / operator, at line " + d.lineNum + ", character" + d.charNum;
            }
        } else if (t2 instanceof IntArray) {
            IntArray ia = (IntArray) t2;
            if (symTable.isProcedure(ia.i.s)) {
                invalidOperands = true;
                errorDetected = true;
                System.err.println("Invalid operands for / operator, at line " + d.lineNum + ", character" + d.charNum);
                errorMessage += "\n" + "Invalid operands for / operator, at line " + d.lineNum + ", character" + d.charNum;
            }
        }
        if (!invalidOperands) {
            if (!isInteger(d.e.accept(this)) || !isInteger(d.m.accept(this))) {
                errorDetected = true;
                System.err.println("Non-integer operand for operator /, at line " + d.lineNum + ", character " + d.charNum);
                errorMessage += "\n" + "Non-integer operand for operator /, at line " + d.lineNum + ", character " + d.charNum;
            }
        }
        return new IntegerType();
    }

    public Type visit(LessThan l) {
        Type t = l.e.accept(this);
        Type t2 = l.m.accept(this);
        boolean invalidOperands = false;
        if (t instanceof IntVar) {
            IntVar iv = (IntVar) t;
            if (symTable.isProcedure(iv.i.s)) {
                invalidOperands = true;
                errorDetected = true;
                System.err.println("Invalid operands for < operator, at line " + l.lineNum + ", character" + l.charNum);
                errorMessage += "\n" + "Invalid operands for < operator, at line " + l.lineNum + ", character" + l.charNum;
            }
        } else if (t instanceof IntArray) {
            IntArray ia = (IntArray) t;
            if (symTable.isProcedure(ia.i.s)) {
                invalidOperands = true;
                errorDetected = true;
                System.err.println("Invalid operands for < operator, at line " + l.lineNum + ", character" + l.charNum);
                errorMessage += "\n" + "Invalid operands for < operator, at line " + l.lineNum + ", character" + l.charNum;
            }
        }
        if (t2 instanceof IntVar) {
            IntVar iv = (IntVar) t2;
            if (symTable.isProcedure(iv.i.s)) {
                invalidOperands = true;
                errorDetected = true;
                System.err.println("Invalid operands for < operator, at line " + l.lineNum + ", character" + l.charNum);
                errorMessage += "\n" + "Invalid operands for < operator, at line " + l.lineNum + ", character" + l.charNum;
            }
        } else if (t2 instanceof IntArray) {
            IntArray ia = (IntArray) t2;
            if (symTable.isProcedure(ia.i.s)) {
                invalidOperands = true;
                errorDetected = true;
                System.err.println("Invalid operands for < operator, at line " + l.lineNum + ", character" + l.charNum);
                errorMessage += "\n" + "Invalid operands for < operator, at line " + l.lineNum + ", character" + l.charNum;
            }
        }
        if (!invalidOperands) {
            if (!isInteger(l.e.accept(this)) || !isInteger(l.m.accept(this))) {
                errorDetected = true;
                System.err.println("Non-integer operand for operator <, at line " + l.lineNum + ", character " + l.charNum);
                errorMessage += "\n" + "Non-integer operand for operator <, at line " + l.lineNum + ", character " + l.charNum;
            }
        }
        return new BooleanType();
    }

    public Type visit(GreaterThan g) {
        Type t = g.e.accept(this);
        Type t2 = g.m.accept(this);
        boolean invalidOperands = false;
        if (t instanceof IntVar) {
            IntVar iv = (IntVar) t;
            if (symTable.isProcedure(iv.i.s)) {
                invalidOperands = true;
                errorDetected = true;
                System.err.println("Invalid operands for > operator, at line " + g.lineNum + ", character" + g.charNum);
                errorMessage += "\n" + "Invalid operands for > operator, at line " + g.lineNum + ", character" + g.charNum;
            }
        } else if (t instanceof IntArray) {
            IntArray ia = (IntArray) t;
            if (symTable.isProcedure(ia.i.s)) {
                invalidOperands = true;
                errorDetected = true;
                System.err.println("Invalid operands for > operator, at line " + g.lineNum + ", character" + g.charNum);
                errorMessage += "\n" + "Invalid operands for > operator, at line " + g.lineNum + ", character" + g.charNum;
            }
        }
        if (t2 instanceof IntVar) {
            IntVar iv = (IntVar) t2;
            if (symTable.isProcedure(iv.i.s)) {
                invalidOperands = true;
                errorDetected = true;
                System.err.println("Invalid operands for > operator, at line " + g.lineNum + ", character" + g.charNum);
                errorMessage += "\n" + "Invalid operands for > operator, at line " + g.lineNum + ", character" + g.charNum;
            }
        } else if (t2 instanceof IntArray) {
            IntArray ia = (IntArray) t2;
            if (symTable.isProcedure(ia.i.s)) {
                invalidOperands = true;
                errorDetected = true;
                System.err.println("Invalid operands for > operator, at line " + g.lineNum + ", character" + g.charNum);
                errorMessage += "\n" + "Invalid operands for > operator, at line " + g.lineNum + ", character" + g.charNum;
            }
        }
        if (!invalidOperands) {
            if (!isInteger(g.e.accept(this)) || !isInteger(g.m.accept(this))) {
                errorDetected = true;
                System.err.println("Non-integer operand for operator >, at line " + g.lineNum + ", character " + g.charNum);
                errorMessage += "\n" + "Non-integer operand for operator >, at line " + g.lineNum + ", character " + g.charNum;
            }
        }
        return new BooleanType();
    }

    public Type visit(LessEqThan l) {
        Type t = l.e.accept(this);
        Type t2 = l.m.accept(this);
        boolean invalidOperands = false;
        if (t instanceof IntVar) {
            IntVar iv = (IntVar) t;
            if (symTable.isProcedure(iv.i.s)) {
                invalidOperands = true;
                errorDetected = true;
                System.err.println("Invalid operands for <= operator, at line " + l.lineNum + ", character" + l.charNum);
                errorMessage += "\n" + "Invalid operands for <= operator, at line " + l.lineNum + ", character" + l.charNum;
            }
        } else if (t instanceof IntArray) {
            IntArray ia = (IntArray) t;
            if (symTable.isProcedure(ia.i.s)) {
                invalidOperands = true;
                errorDetected = true;
                System.err.println("Invalid operands for <= operator, at line " + l.lineNum + ", character" + l.charNum);
                errorMessage += "\n" + "Invalid operands for <= operator, at line " + l.lineNum + ", character" + l.charNum;
            }
        }
        if (t2 instanceof IntVar) {
            IntVar iv = (IntVar) t2;
            if (symTable.isProcedure(iv.i.s)) {
                invalidOperands = true;
                errorDetected = true;
                System.err.println("Invalid operands for <= operator, at line " + l.lineNum + ", character" + l.charNum);
                errorMessage += "\n" + "Invalid operands for <= operator, at line " + l.lineNum + ", character" + l.charNum;
            }
        } else if (t2 instanceof IntArray) {
            IntArray ia = (IntArray) t2;
            if (symTable.isProcedure(ia.i.s)) {
                invalidOperands = true;
                errorDetected = true;
                System.err.println("Invalid operands for <= operator, at line " + l.lineNum + ", character" + l.charNum);
                errorMessage += "\n" + "Invalid operands for <= operator, at line " + l.lineNum + ", character" + l.charNum;
            }
        }
        if (!invalidOperands) {
            if (!isInteger(l.e.accept(this)) || !isInteger(l.m.accept(this))) {
                errorDetected = true;
                System.err.println("Non-integer operand for operator <=, at line " + l.lineNum + ", character " + l.charNum);
                errorMessage += "\n" + "Non-integer operand for operator <=, at line " + l.lineNum + ", character " + l.charNum;
            }
        }
        return new BooleanType();
    }

    public Type visit(GreaterEqThan g) {
        Type t = g.e.accept(this);
        Type t2 = g.m.accept(this);
        boolean invalidOperands = false;
        if (t instanceof IntVar) {
            IntVar iv = (IntVar) t;
            if (symTable.isProcedure(iv.i.s)) {
                invalidOperands = true;
                errorDetected = true;
                System.err.println("Invalid operands for >= operator, at line " + g.lineNum + ", character" + g.charNum);
                errorMessage += "\n" + "Invalid operands for >= operator, at line " + g.lineNum + ", character" + g.charNum;
            }
        } else if (t instanceof IntArray) {
            IntArray ia = (IntArray) t;
            if (symTable.isProcedure(ia.i.s)) {
                invalidOperands = true;
                errorDetected = true;
                System.err.println("Invalid operands for >= operator, at line " + g.lineNum + ", character" + g.charNum);
                errorMessage += "\n" + "Invalid operands for >= operator, at line " + g.lineNum + ", character" + g.charNum;
            }
        }
        if (t2 instanceof IntVar) {
            IntVar iv = (IntVar) t2;
            if (symTable.isProcedure(iv.i.s)) {
                invalidOperands = true;
                errorDetected = true;
                System.err.println("Invalid operands for >= operator, at line " + g.lineNum + ", character" + g.charNum);
                errorMessage += "\n" + "Invalid operands for >= operator, at line " + g.lineNum + ", character" + g.charNum;
            }
        } else if (t2 instanceof IntArray) {
            IntArray ia = (IntArray) t2;
            if (symTable.isProcedure(ia.i.s)) {
                invalidOperands = true;
                errorDetected = true;
                System.err.println("Invalid operands for >= operator, at line " + g.lineNum + ", character" + g.charNum);
                errorMessage += "\n" + "Invalid operands for >= operator, at line " + g.lineNum + ", character" + g.charNum;
            }
        }
        if (!invalidOperands) {
            if (!isInteger(g.e.accept(this)) || !isInteger(g.m.accept(this))) {
                errorDetected = true;
                System.err.println("Non-integer operand for operator >=, at line " + g.lineNum + ", character " + g.charNum);
                errorMessage += "\n" + "Non-integer operand for operator >=, at line " + g.lineNum + ", character " + g.charNum;
            }
        }
        return new BooleanType();
    }

    public Type visit(Equal e) {
        Type t = e.e.accept(this);
        Type t2 = e.m.accept(this);
        boolean invalidOperands = false;
        if (t instanceof IntVar) {
            IntVar iv = (IntVar) t;
            if (symTable.isProcedure(iv.i.s)) {
                invalidOperands = true;
                errorDetected = true;
                System.err.println("Invalid operands for = operator, at line " + e.lineNum + ", character " + e.charNum);
                errorMessage += "\n" + "Invalid operands for = operator, at line " + e.lineNum + ", character " + e.charNum;
            }
        } else if (t instanceof IntArray) {
            IntArray ia = (IntArray) t;
            if (symTable.isProcedure(ia.i.s)) {
                invalidOperands = true;
                errorDetected = true;
                System.err.println("Invalid operands for = operator, at line " + e.lineNum + ", character " + e.charNum);
                errorMessage += "\n" + "Invalid operands for = operator, at line " + e.lineNum + ", character " + e.charNum;
            }
        }
        if (t2 instanceof IntVar) {
            IntVar iv = (IntVar) t2;
            if (symTable.isProcedure(iv.i.s)) {
                invalidOperands = true;
                errorDetected = true;
                System.err.println("Invalid operands for = operator, at line " + e.lineNum + ", character " + e.charNum);
                errorMessage += "\n" + "Invalid operands for = operator, at line " + e.lineNum + ", character " + e.charNum;
            }
        } else if (t2 instanceof IntArray) {
            IntArray ia = (IntArray) t2;
            if (symTable.isProcedure(ia.i.s)) {
                invalidOperands = true;
                errorDetected = true;
                System.err.println("Invalid operands for = operator, at line " + e.lineNum + ", character " + e.charNum);
                errorMessage += "\n" + "Invalid operands for = operator, at line " + e.lineNum + ", character " + e.charNum;
            }
        }
        if (!invalidOperands) {
            if (!isInteger(e.e.accept(this)) || !isInteger(e.m.accept(this))) {
                errorDetected = true;
                System.err.println("Non-integer operand for operator =, at line " + e.lineNum + ", character " + e.charNum);
                errorMessage += "\n" + "Non-integer operand for operator =, at line " + e.lineNum + ", character " + e.charNum;
            }
        }
        return new BooleanType();
    }

    public Type visit(NotEqual n) {
        Type t = n.e.accept(this);
        Type t2 = n.m.accept(this);
        boolean invalidOperands = false;
        if (t instanceof IntVar) {
            IntVar iv = (IntVar) t;
            if (symTable.isProcedure(iv.i.s)) {
                invalidOperands = true;
                errorDetected = true;
                System.err.println("Invalid operands for # operator, at line " + n.lineNum + ", character" + n.charNum);
                errorMessage += "\n" + "Invalid operands for # operator, at line " + n.lineNum + ", character" + n.charNum;
            }
        } else if (t instanceof IntArray) {
            IntArray ia = (IntArray) t;
            if (symTable.isProcedure(ia.i.s)) {
                invalidOperands = true;
                errorDetected = true;
                System.err.println("Invalid operands for # operator, at line " + n.lineNum + ", character" + n.charNum);
                errorMessage += "\n" + "Invalid operands for # operator, at line " + n.lineNum + ", character" + n.charNum;
            }
        }
        if (t2 instanceof IntVar) {
            IntVar iv = (IntVar) t2;
            if (symTable.isProcedure(iv.i.s)) {
                invalidOperands = true;
                errorDetected = true;
                System.err.println("Invalid operands for # operator, at line " + n.lineNum + ", character" + n.charNum);
                errorMessage += "\n" + "Invalid operands for # operator, at line " + n.lineNum + ", character" + n.charNum;
            }
        } else if (t2 instanceof IntArray) {
            IntArray ia = (IntArray) t2;
            if (symTable.isProcedure(ia.i.s)) {
                invalidOperands = true;
                errorDetected = true;
                System.err.println("Invalid operands for # operator, at line " + n.lineNum + ", character" + n.charNum);
                errorMessage += "\n" + "Invalid operands for # operator, at line " + n.lineNum + ", character" + n.charNum;
            }
        }
        if (!invalidOperands) {
            if (!isInteger(n.e.accept(this)) || !isInteger(n.m.accept(this))) {
                errorDetected = true;
                System.err.println("Non-integer operand for operator #, at line " + n.lineNum + ", character " + n.charNum);
                errorMessage += "\n" + "Non-integer operand for operator #, at line " + n.lineNum + ", character " + n.charNum;
            }
        }
        return new BooleanType();
    }

    public Type visit(And a) {
        if (!isBoolean(a.e.accept(this)) || !isBoolean(a.m.accept(this))) {
            errorDetected = true;
            System.err.println("Attempt to use boolean operator & on non-boolean operands at line " + a.lineNum + ", character " + a.charNum);
            errorMessage += "\n" + "Attempt to use boolean operator & on non-boolean operands at line " + a.lineNum + ", character " + a.charNum;
        }
        return new BooleanType();
    }

    public Type visit(Or o) {
        if (!isBoolean(o.e.accept(this)) || !isBoolean(o.m.accept(this))) {
            errorDetected = true;
            System.err.println("Attempt to use boolean operator | on non-boolean operands at line " + o.lineNum + ", character " + o.charNum);
            errorMessage += "\n" + "Attempt to use boolean operator | on non-boolean operands at line " + o.lineNum + ", character " + o.charNum;
        }
        return new BooleanType();
    }

    public Type visit(Xor x) {
        if (!isBoolean(x.e.accept(this)) || !isBoolean(x.m.accept(this))) {
            errorDetected = true;
            System.err.println("Attempt to use boolean operator ~ on non-boolean operands at line " + x.lineNum + ", character " + x.charNum);
            errorMessage += "\n" + "Attempt to use boolean operator ~ on non-boolean operands at line " + x.lineNum + ", character " + x.charNum;
        }
        return new BooleanType();
    }

    //Exp e
    public Type visit(Not n) {
        if (!isBoolean(n.e.accept(this))) {
            errorDetected = true;
            System.err.println("Attempt to use boolean operator ~ on non-boolean operand at line " + n.lineNum + ", character " + n.charNum);
            errorMessage += "\n" + "Attempt to use boolean operator ~ on non-boolean operand at line " + n.lineNum + ", character " + n.charNum;
        }
        return new BooleanType();
    }

    public Type visit(IntLiteral i) {
        return new IntegerType();
    }

    public Type visit(CallStmt c) {
        return c.i.accept(this);
    }

    public Type visit(UncallStmt u) {
        return u.i.accept(this);
    }

    public Type visit(ReadStmt r) {
        return r.l.accept(this);
    }

    public Type visit(WriteStmt w) {
        return w.l.accept(this);
    }

    public Type visit(UnaryMinus m) {
        Type t = m.e.accept(this);
        boolean invalidOperand = false;
        if (t instanceof IntVar) {
            IntVar iv = (IntVar) t;
            if (symTable.isProcedure(iv.i.s)) {
                invalidOperand = true;
                errorDetected = true;
                System.err.println("Invalid operands for - operator, at line " + m.lineNum + ", character" + m.charNum);
            }
        } else if (t instanceof IntArray) {
            IntArray ia = (IntArray) t;
            if (symTable.isProcedure(ia.i.s)) {
                invalidOperand = true;
                errorDetected = true;
                System.err.println("Invalid operands for - operator, at line " + m.lineNum + ", character" + m.charNum);
            }
        }
        if (!invalidOperand) {
            if (!isInteger(m.e.accept(this))) {
                errorDetected = true;
                System.err.println("Non-integer operand for operator -, at line " + m.lineNum + ", character " + m.charNum);
            }
        }
        return new IntegerType();
    }

    public Type visit(LValueExp1 l) {
        return null;
    }

    public Type visit(BracketedExp b) {
        //b.e.accept(this);
        return b.e.accept(this);
    }

    public Type visit(Identifier i) {
        return null;
    }

    public Type visit(IntegerType it) {
        return null;
    }

    public Type visit(BooleanType bt) {
        return null;
    }

}
