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
public class DeclList {
    public Vector list;
    
    public DeclList(){
        list = new Vector();
    }
    
    public void addElement(Decl d){
        list.add(0, d);
    }
    
    public Decl elementAt(int i){
        return (Decl)list.elementAt(i);
    }
    
    public int size(){
        return list.size();
    }
}
