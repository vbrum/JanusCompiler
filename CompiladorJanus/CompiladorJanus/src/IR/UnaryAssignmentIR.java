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
public class UnaryAssignmentIR extends Quadruple {

    //x := op y
    public UnaryAssignmentIR(Object operator, Object y, Object x) {
        op = operator;   //op
        arg1 = y;        //y
        arg2 = null;
        result = x;      //x
    }

    public String toString() {
        return result + " := " + op + " " + arg1;
    }
}
