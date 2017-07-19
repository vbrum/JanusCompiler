/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package symbolTable;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import newpackage.Helper;

/**
 *
 * @author VBrum
 */
public class SymbolTable implements Scope {

    private Hashtable<String, Variable> vars;
    private Hashtable<String, ProcedureSymbolTable> procedures;

    public SymbolTable() {
        procedures = new Hashtable<String, ProcedureSymbolTable>();
        vars = new Hashtable<String, Variable>();
    }

    public Scope enterScope(String name) {
        return procedures.get(name);
    }

    public Scope exitScope() {
        return null;
    }

    public void addProcedure(String name) {
        procedures.put(name, new ProcedureSymbolTable(this, name));
    }

    public void addVariable(String name, String type) {
        vars.put(name, new Variable(name, type));
    }
    
    public void addVariable(String name, String type, int tam) {
        vars.put(name, new Variable(name, type, tam));
    }

    public ProcedureSymbolTable getProcedure(String name) {
        if (isProcedure(name)) {
            return (ProcedureSymbolTable) procedures.get(name);
        } else {
            return null;
        }
    }

    public boolean isProcedure(String name) {
        return procedures.containsKey(name);
    }

    public Hashtable<String, ProcedureSymbolTable> getProcedures() {
        return procedures;
    }
    
    public Hashtable<String, Variable> getVars(){
        return vars;
    }

    public Variable lookupVariable(String name) {
        return vars.get(name);
    }
    
    public ProcedureSymbolTable lookupProcedure(String name){
        return procedures.get(name);
    }

    public void calculateVarOffsets() {
        int localOffset = 0;

        List<String> keys = Helper.keysToSortedList(vars.keySet());

        for (int i = 0; i < keys.size(); i++) {
            Variable v = vars.get(keys.get(i));
            v.setOffset(localOffset);

            localOffset += 4;
        }
    }
   

    /*public void print(int indentLevel) {
        System.out.println("~~~~~~Symbol Table~~~~~~");

        List<String> keys = Helper.keysToSortedList(classes.keySet());

        for (int i = 0; i < keys.size(); i++) {
            classes.get(keys.get(i)).print(indentLevel + 1);
            System.out.println();
        }
    }*/
}
