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
public abstract class Decl {

    public abstract void accept(Visitor v);

    public abstract Type accept(TypeVisitor v);
}
