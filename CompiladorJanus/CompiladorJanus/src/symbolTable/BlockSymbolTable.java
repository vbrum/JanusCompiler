/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package symbolTable;

import java.util.Hashtable;
import java.util.List;

/**
 *
 * @author VBrum
 */
public class BlockSymbolTable implements Scope {
    
    protected Scope parent;
    protected Hashtable<String, Variable> vars;
    protected Hashtable<String, BlockSymbolTable> blocks;
    
    public BlockSymbolTable(Scope parent) {
        this.parent = parent;
        vars = new Hashtable<String, Variable>();
        blocks = new Hashtable<String, BlockSymbolTable>();
    }
    
    public Scope enterScope(String name) {
        return blocks.get(name);
    }
    
    public Scope exitScope() {
        return parent;
    }
    
    public void addBlock(String name) {
        blocks.put(name, new BlockSymbolTable(this));
    }
    
    public void addVariable(String name, String type) {
        vars.put(name, new Variable(name, type));
    }
    
    public Variable lookupVariable(String name) {
        return parent.lookupVariable(name);
        
    }
    
    public ProcedureSymbolTable lookupProcedure(String name) {
        return parent.lookupProcedure(name);
    }

    /*public void printIndentation(int indentLevel) {
        System.out.println("");
        for (int i = 0; i < indentLevel; i++) {
            System.out.print("\t");
        }
    }

    public void print(int indentLevel)
	{
		List<String> keys = Helper.keysToSortedList(vars.keySet());
		
		for(int i = 0; i < keys.size(); i++)
		{
			printIndentation(indentLevel);
			System.out.print(vars.get(keys.get(i)).getType() + " " + vars.get(keys.get(i)).getName() + ";");
		}
		
		keys = Helper.keysToSortedList(blocks.keySet());
		
		for(int i = 0; i < keys.size(); i++)
		{
			blocks.get(keys.get(i)).print(indentLevel+1);
		}
	}*/
}
