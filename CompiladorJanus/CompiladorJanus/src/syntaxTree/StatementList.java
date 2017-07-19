/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package syntaxTree;

import java.util.Vector;

/**
 *
 * @author VBrum
 */
public class StatementList {
    public Vector list;
    
    public StatementList(){
        list = new Vector();
    }
    
    public void addElement(Statement n){
        list.add(0,n);
    }
    
    public Statement elementAt(int i){
        return (Statement)list.elementAt(i);
    }
    
    public int size(){
        return list.size();
    }
}
