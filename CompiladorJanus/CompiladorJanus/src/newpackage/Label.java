/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newpackage;

/**
 *
 * @author VBrum
 */
public class Label {

    private static int nextNumber;
    private final int num;
    public boolean printBefore;

    public Label(boolean printBefore) {
        num = nextNumber++;
        this.printBefore = printBefore;
    }

    public String getName() {
        return "L" + num;
    }

    public String toString() {
        return "L" + num + ":";
    }

    public int getNum() {
        return num;
    }

    public void reset() {
        nextNumber = -1;
    }
    
    public static void restart(){
        nextNumber = 0;
    }
}
