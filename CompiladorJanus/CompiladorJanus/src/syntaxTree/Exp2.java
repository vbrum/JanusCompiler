/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package syntaxTree;

import Visitor.TypeVisitor;
import Visitor.Visitor;
import symbolTable.Variable;

/**
 *
 * @author VBrum
 */
public abstract class Exp2 extends Exp3{

    public abstract void accept(Visitor v);

    public abstract Type accept(TypeVisitor v);

    public abstract Variable generateTAC();
}
