/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package syntaxTree;

import Visitor.*;

/**
 *
 * @author VBrum
 */
public class Proc {
    public static int nextNum;
    public final int id;
    public Identifier i;
    public StatementList sl;

    public Proc(Identifier ai, StatementList asl) {
        nextNum++;
        id = nextNum;
        i = ai;
        sl = asl;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
    
    public Type accept(TypeVisitor v) {
        return v.visit(this);
    }
    
    public boolean first(){
        if(id == 1){
            return true;
        }else
            return false;
    }
    
    public static void restart(){
        nextNum = 0;
    }
}
