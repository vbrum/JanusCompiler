/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IR;

/**
 *
 * @author VBrum
 */
public class IndexedAssignmentIR2 extends Quadruple {

    //y[i] := x
    public IndexedAssignmentIR2(Object x, Object i, Object y) {
        op = null;
        arg1 = x;             //x
        arg2 = i;             //i
        result = y;           //y
    }

    public String toString() {
        return result + "[" + arg2 + "]" + " := " + arg1;
    }
}
