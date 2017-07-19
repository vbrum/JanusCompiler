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
public class Token {
    Object value;
    int line;
    int column;
    
    public Token(Object value, int line, int column){
        this.value = value;
        this.line = line;
        this.column = column;
    }
    
    public Object getValue(){
        return value;
    }
    
    public int getLine(){
        return line;
    }
    
    public int getColumn(){
        return column;
    }
    
    public String toString(){
        return String.valueOf(value);
    }
    
    
}
