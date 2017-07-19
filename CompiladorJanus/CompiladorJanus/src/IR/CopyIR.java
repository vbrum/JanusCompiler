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
public class CopyIR extends Quadruple {

    //x := y
    public CopyIR(Object argument1, Object result) {
        op = null;
        arg1 = argument1;     //y
        arg2 = null;
        this.result = result; //x
    }

    public String toString() {
        return result + " := " + arg1;
    }
}
