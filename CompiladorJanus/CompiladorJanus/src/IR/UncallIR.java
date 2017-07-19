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
public class UncallIR extends Quadruple{
    //x := uncall f, NUMPARAMS
    public UncallIR(Object f, Object NUMPARAMS, Object x) {
        op = "uncall";         //call
        arg1 = f;            //f
        arg2 = NUMPARAMS;    //NUMPARAMS
        result = x;          //x
    }

    public String toString() {
        if (result != null) {
            return result + " := " + op + " " + arg1 + ", " + arg2;
        } else {
            return op + " " + arg1 + ", " + arg2;
        }
    }
}
