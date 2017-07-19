/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IR;

import IR.Quadruple;

/**
 *
 * @author VBrum
 */
public class ParameterIR extends Quadruple {

    //param x
    public ParameterIR(Object x) {
        op = "param";   //param
        arg1 = x;       //x
        arg2 = null;
        result = null;
    }

    public String toString() {
        return op + " " + arg1;
    }
}
