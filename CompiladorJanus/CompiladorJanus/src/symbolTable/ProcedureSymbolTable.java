/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package symbolTable;

/**
 *
 * @author VBrum
 */
public class ProcedureSymbolTable implements Scope {

    private Scope parent;
    private String name;
    
    public ProcedureSymbolTable(SymbolTable parent, String name) {
        this.parent = parent;
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public Scope enterScope(String name) {
        return null;
    }
    
    public Scope exitScope() {
        return parent;
    }
    
    public Variable lookupVariable(String name) {
        return parent.lookupVariable(name);
    }
    
    public ProcedureSymbolTable lookupProcedure(String name) {
        return parent.lookupProcedure(name);
    }
    
}
