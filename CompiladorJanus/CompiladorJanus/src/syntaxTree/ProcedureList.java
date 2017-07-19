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
public class ProcedureList {
    public Vector list;
    
    public ProcedureList(){
        list = new Vector();
    }
    
    public void addElement(Proc p){
        list.add(0, p);
    }
    
    public Proc elementAt(int i){
        return (Proc)list.elementAt(i);
    }
    
    public int size(){
        return list.size();
    }
}
