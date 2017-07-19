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
public interface Scope {
        
        public Scope enterScope(String name);
        
	public Variable lookupVariable(String name);
        
        public ProcedureSymbolTable lookupProcedure(String name);
	
	public Scope exitScope();
	//public void print(int indentLevel);
        
}
