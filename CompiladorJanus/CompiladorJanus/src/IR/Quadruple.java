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
public abstract class Quadruple {

    protected Object op;
    protected Object arg1;
    protected Object arg2;
    protected Object result;

    public abstract String toString();

    public Object getOp() {
        return op;
    }

    public Object getArg1() {
        return arg1;
    }

    public Object getArg2() {
        return arg2;
    }

    public Object getResult() {
        return result;
    }

    public void setOp(Object o) {
        op = o;
    }

    public void setArg1(Object o) {
        arg1 = o;
    }

    public void setArg2(Object o) {
        arg2 = o;
    }

    public void setResult(Object o) {
        result = o;
    }

}
