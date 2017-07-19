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
public class IntArrayDecl extends Decl {

    public Identifier i;
    public int tam;

    public IntArrayDecl(Identifier ai, int atam) {
        i = ai;
        tam = atam;
    }

    
    public void accept(Visitor v) {
        v.visit(this);
    }

    public Type accept(TypeVisitor v) {
        return v.visit(this);
    }

}
