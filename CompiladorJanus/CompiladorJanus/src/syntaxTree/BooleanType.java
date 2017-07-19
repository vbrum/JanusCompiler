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
public class BooleanType implements Type {

    public Type accept(TypeVisitor v) {
        return v.visit(this);
    }

}
