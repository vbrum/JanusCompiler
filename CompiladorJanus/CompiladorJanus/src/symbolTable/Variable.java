/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package symbolTable;

/**
 *
 * @author VBrum
 */
public class Variable {

    private String name;
    private String type;
    private int offset; //Used by class member variables for an offset
    private String register; //Used by local variables to store the register string mapped to it by the register allocator
    private int arrayRange = 1;
    
    public Variable(String name, String type) {
        this.name = name;
        this.type = type;
        offset = -1;
        register = null;
    }

    public Variable(String name, String type, int x) {
        this(name, type);
        offset = x;
    }

    public Variable(String name, String type, String reg) {
        this(name, type);
        register = reg;
    }
 
    public void setRegister(String reg) {
        register = reg;
    }

    public void setOffset(int x) {
        offset = x;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return getName();
    }

    public String getType() {
        return type;
    }

    public int getOffset() {
        return offset;
    }

    public String getRegister() {
        return register;
    }
    
    public int getArrayRange(){
        return arrayRange;
    }
    
    public void setArrayRange(int range){
        arrayRange = range;
    }
    
    public void setName(String n){
        name = n;
    }
}
