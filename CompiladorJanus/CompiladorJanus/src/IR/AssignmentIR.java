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
public class AssignmentIR extends Quadruple{

    //x := y op z
    public AssignmentIR(Object operator, Object y, Object z, Object x) {
        op = operator;    //op
        arg1 = y;         //y
        arg2 = z;         //z
        result = x;       //x
    }

    public String toString() {
        return result + " := " + arg1 + " " + op + " " + arg2;
    }
}
