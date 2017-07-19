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
public class UnconditionalJumpIR extends Quadruple {

    //goto LABEL
    public UnconditionalJumpIR(Object label) {
        op = "goto";      //goto
        arg1 = null;
        arg2 = null;
        result = label;   //LABEL
    }

    public String toString() {
        return op + " " + result;
    }
}
