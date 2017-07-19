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
public class Temporary {

    private static int nextNumber;
    private final int num;

    public Temporary() {
        num = nextNumber++;
    }

    public String toString() {
        return "t" + num;
    }

    public int getNum() {
        return num;
    }

    public void reset() {
        nextNumber = -1;
    }
    
    public static void restartTemp(){
        nextNumber = 0;
    }
}
