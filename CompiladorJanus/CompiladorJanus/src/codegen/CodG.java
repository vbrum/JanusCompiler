/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codegen;

import IR.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import newpackage.Label;
import symbolTable.SymbolTable;
import symbolTable.Variable;

/**
 *
 * @author VBrum
 */
public class CodG {
    private String output;
    private List<Quadruple> IRList;
    private Hashtable<Quadruple, List<Label>> labels;
    private SymbolTable symbolTable;
    private HashMap<String, String> workList;
    private Hashtable<String, Boolean> regTemp;
    private int indiceIR = 0;
    boolean reversed;
    private int nTemps;
    
    
    

    public CodG(List<Quadruple> list, Hashtable<Quadruple, List<Label>> label, SymbolTable symTable, HashMap<String, String> wl, String fileName, boolean rev) {
        IRList = list;        
        output = fileName;
        labels = label;        
        symbolTable = symTable;
        workList = wl;
        regTemp = new Hashtable<String, Boolean>();
        reversed = rev;
        initRegTemp();
        getNumTemp();
    }
    
    public int procuraPfini(List<Quadruple> list){
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i) instanceof CallIR){
                CallIR q = (CallIR)list.get(i);
                if(q.getArg1().equals("exit")){
                    return i;
                }
                
            }
        }
        return -1;
    }

    public void generateMips() {
        try {
            FileWriter fw = new FileWriter(output);
            BufferedWriter bw = new BufferedWriter(fw);
            Collection<Variable> variables = symbolTable.getVars().values();
            if(!reversed){
                bw.write(".data\n", 0, 6);
                int index = procuraPfini(IRList);
                Quadruple qExit = IRList.remove(index);
                for (Variable v : variables) {
                    if (v.getType().equals("int")) {
                        IRList.add(index++,new ParameterIR(symbolTable.lookupVariable(v.getName())));
                        IRList.add(index++,new CallIR("write","1",null));
                        
                        String s = "" + v.getName() + ": .word 0\n";
                        bw.write(s, 0, s.length());
                        
                        s = "str" + v.getName() + ": .asciiz " + "\"" + v.getName() + " = \"\n";
                        bw.write(s, 0, s.length());
                    } else {
                        IRList.add(index++,new ParameterIR(symbolTable.lookupVariable(v.getName())));
                        IRList.add(index++,new CallIR("writevet","1",null));
                        int n = v.getArrayRange();
                        String s = "" + v.getName() + ": .word 0";
                        for (int i = 1; i < n; i++) {
                            if (i == n - 1) {
                                s += ", 0\n";
                            } else {
                                s += ", 0";
                            }
                        }
                        bw.write(s, 0, s.length());
                        
                        s = "str" + v.getName() + ": .asciiz " + "\"" +  v.getName() +"["+v.getArrayRange()+"]" + " = \"\n";
                        bw.write(s, 0, s.length());
                
                        s = "tam" + v.getName() + ": .word " + v.getArrayRange() + "\n";
                        bw.write(s, 0, s.length());
                    }
                }
                
                IRList.add(index,qExit);
                
                String temp = "rc: .asciiz \"readCommand\\n\"\n";
                bw.write(temp, 0, temp.length());
                
                temp = "open" + ": .asciiz \"{\"\n";
                bw.write(temp, 0, temp.length());
                
                temp = "close" + ": .asciiz \"}\"\n";
                bw.write(temp, 0, temp.length());
                
                temp = "comma" + ": .asciiz \",\"\n";
                bw.write(temp, 0, temp.length());
                
                temp = "offset: .word 0\n";
                bw.write(temp, 0, temp.length());
                
                temp = "temporaries: .word " + nTemps*4 + "\n";
                bw.write(temp, 0, temp.length());
                
                bw.write(".text\n", 0, 6);
            }
            for (int i = 0; i < IRList.size(); i++) {
                Quadruple q = IRList.get(i);    
                indiceIR = i;
                List<Label> labelList = labels.get(q);

                if (labelList != null) {
                    for (Label l : labelList) {
                            if(l.getNum() == 4)
                            System.out.println("");
                        if (l.printBefore == true) {
                            //Print label
                            String temp = l.toString() + "\n";
                            bw.write(temp, 0, temp.length());
                        }
                    }
                }
            
                if (q instanceof UnaryAssignmentIR){
                    handleUnaryAssignment(q,bw);
                }else if (q instanceof AssignmentIR) {
                    handleAssignment(q, bw);
                } else if (q instanceof IndexedAssignmentIR1) {
                    handleArrayIndex(q, bw);
                } else if (q instanceof IndexedAssignmentIR2){
                    handleArrayAssignment(q, bw);
                } else if (q instanceof ConditionalJumpIR) {
                    handleConditionalJump(q, bw);
                } else if (q instanceof UnconditionalJumpIR) {
                    handleUnconditionalJump(q, bw);
                } else if (q instanceof CallIR) {
                    functionCall(IRList, i, bw);
                } else if (q instanceof UncallIR){
                    functionUncall(IRList, i, bw);
                } else if (q instanceof CopyIR){
                    handleCopy(q, bw);
                }

                //Print any labels after
                if (labelList != null) {
                    for (Label l : labelList) {
                        
                        if (l.printBefore == false) {
                            //Print label
                            String temp = l.toString() + "\n";
                            bw.write(temp, 0, temp.length());
                        }
                    }
                }
            }
            
            if (bw != null) {
                bw.close();
            }

            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleAssignment(Quadruple instruction, BufferedWriter bw) {
        try {
            String op = (String) instruction.getOp();
            Variable result = (Variable) instruction.getResult();
            Variable arg1 = (Variable) instruction.getArg1();
            Variable arg2 = (Variable) instruction.getArg2();
            String temp = "";
            boolean chave = false;
            String strArg1 = "";


            if(!result.getType().equals("temporary")){
                chave = true;
            }
            
            if(result.getRegister() == null){
                if (result.getType().equals("temporary")) {
                    result.setRegister(getTempReg());
                } else {
                    result.setRegister(getTempReg());
                }
            }
            
            if(arg1.getRegister() == null){
                arg1.setRegister(getTempReg());
            }
            if(arg2.getRegister() == null){
                arg2.setRegister(getTempReg());
            }
            
            if (arg1.getType().equals("constant")) {
                temp = "li " + result.getRegister() + ", " + arg1.getName() + "\n";
            } else if (arg1.getType().equals("temporary")) {
                strArg1 = arg1.getName();
                int i = getPositionTemp(strArg1);
                
                temp = "lw " + arg1.getRegister() + ", " + "temporaries + " + (i*4) + "\n";
                bw.write(temp, 0, temp.length());
                
                temp = "move " + result.getRegister() + "," + arg1.getRegister() + "\n";
            } else {
                temp = "lw " + result.getRegister() + ", " + arg1.getName() + "\n";
            }
            bw.write(temp, 0, temp.length());
            

            if (arg2.getType().equals("constant")) {
                if (op.equals("+")) {
                    temp = "addi " + result.getRegister() + ", " + result.getRegister() + ", " + arg2.getName() + "\n";
                } else if (op.equals("-")) {
                    temp = "addi " + result.getRegister() + ", " + result.getRegister() + ", " + (Integer.parseInt(arg2.getName()) * -1) + "\n";
                } else if (op.equals("*")) {
                    temp = "mul " + result.getRegister() + ", " + result.getRegister() + ", " + arg2.getName() + "\n";
                } else if (op.equals("/")) {
                    
                    temp = "la $a0, msg\n";
                    bw.write(temp, 0, temp.length());
                    
                    Label L = new Label(true);
                    String tempReg = getTempReg();
                    
                    temp = "li " + tempReg + ", " + arg2.getName() + "\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "teqi " + tempReg + ", " + "0" + "\n";
                    bw.write(temp, 0, temp.length());
                    freeTempReg(tempReg);
                    
                    temp = "j " + L.getName() + "\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "j exit\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = L.toString() + "\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "div " + result.getRegister() + ", " + result.getRegister() + ", " + arg2.getName() + "\n";
                } else if (op.equals("\\")) {
                    
                    temp = "la $a0, msg\n";
                    bw.write(temp, 0, temp.length());
                    
                    Label L = new Label(true);
                    String tempReg = getTempReg();
                    
                    temp = "li " + tempReg + ", " + arg2.getName() + "\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "teqi " + tempReg + ", " + "0" + "\n";
                    bw.write(temp, 0, temp.length());
                    freeTempReg(tempReg);
                    
                    temp = "j " + L.getName() + "\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "j exit\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = L.toString() + "\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "div " + result.getRegister() + ", " + result.getRegister() + ", " + arg2.getName() + "\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "mfhi " + result.getRegister() + "\n";
                } else if (op.equals("<")) {
                    Label L1 = new Label(false);
                    Label L2 = new Label(false);

                    //if arg1 < arg2 go to L1
                    temp = "blt " + result.getRegister() + ", " + arg2.getName() + ", " + L1.getName() + "\n";
                    bw.write(temp, 0, temp.length());

                    temp = "add " + result.getRegister() + ", $zero, $zero\n";
                    bw.write(temp, 0, temp.length());

                    temp = "j " + L2.getName() + "\n";
                    bw.write(temp, 0, temp.length());

                    temp = L1.toString() + "\n";
                    bw.write(temp, 0, temp.length());

                    temp = "addi " + result.getRegister() + ", $zero, 1\n";
                    bw.write(temp, 0, temp.length());

                    temp = L2.toString() + "\n";

                } else if (op.equals("<=")) {
                    Label L1 = new Label(false);
                    Label L2 = new Label(false);

                    //if arg1 <= arg2 go to L1
                    temp = "ble " + result.getRegister() + ", " + arg2.getName() + ", " + L1.getName() + "\n";
                    bw.write(temp, 0, temp.length());

                    temp = "add " + result.getRegister() + ", $zero, $zero\n";
                    bw.write(temp, 0, temp.length());

                    temp = "j " + L2.getName() + "\n";
                    bw.write(temp, 0, temp.length());

                    temp = L1.toString() + "\n";
                    bw.write(temp, 0, temp.length());

                    temp = "addi " + result.getRegister() + ", $zero, 1\n";
                    bw.write(temp, 0, temp.length());

                    temp = L2.toString() + "\n";
                } else if (op.equals(">")) {
                    Label L1 = new Label(false);
                    Label L2 = new Label(false);

                    //if arg1 > arg2 go to L1
                    temp = "bgt " + result.getRegister() + ", " + arg2.getName() + ", " + L1.getName() + "\n";
                    bw.write(temp, 0, temp.length());

                    temp = "add " + result.getRegister() + ", $zero, $zero\n";
                    bw.write(temp, 0, temp.length());

                    temp = "j " + L2.getName() + "\n";
                    bw.write(temp, 0, temp.length());

                    temp = L1.toString() + "\n";
                    bw.write(temp, 0, temp.length());

                    temp = "addi " + result.getRegister() + ", $zero, 1\n";
                    bw.write(temp, 0, temp.length());

                    temp = L2.toString() + "\n";
                } else if (op.equals(">=")) {
                    Label L1 = new Label(false);
                    Label L2 = new Label(false);

                    //if arg1 >= arg2 go to L1
                    temp = "bge " + result.getRegister() + ", " + arg2.getName() + ", " + L1.getName() + "\n";
                    bw.write(temp, 0, temp.length());

                    temp = "add " + result.getRegister() + ", $zero, $zero\n";
                    bw.write(temp, 0, temp.length());

                    temp = "j " + L2.getName() + "\n";
                    bw.write(temp, 0, temp.length());

                    temp = L1.toString() + "\n";
                    bw.write(temp, 0, temp.length());

                    temp = "addi " + result.getRegister() + ", $zero, 1\n";
                    bw.write(temp, 0, temp.length());

                    temp = L2.toString() + "\n";
                } else if (op.equals("=")) {
                    Label L1 = new Label(false);
                    Label L2 = new Label(false);

                    //if arg1 = arg2 go to L1
                    temp = "beq " + result.getRegister() + ", " + arg2.getName() + ", " + L1.getName() + "\n";
                    bw.write(temp, 0, temp.length());

                    temp = "add " + result.getRegister() + ", $zero, $zero\n";
                    bw.write(temp, 0, temp.length());

                    temp = "j " + L2.getName() + "\n";
                    bw.write(temp, 0, temp.length());

                    temp = L1.toString() + "\n";
                    bw.write(temp, 0, temp.length());

                    temp = "addi " + result.getRegister() + ", $zero, 1\n";
                    bw.write(temp, 0, temp.length());

                    temp = L2.toString() + "\n";
                } else if (op.equals("#")) {
                    Label L1 = new Label(false);
                    Label L2 = new Label(false);

                    //if arg1 # arg2 go to L1
                    temp = "bne " + result.getRegister() + ", " + arg2.getName() + ", " + L1.getName() + "\n";
                    bw.write(temp, 0, temp.length());

                    temp = "add " + result.getRegister() + ", $zero, $zero\n";
                    bw.write(temp, 0, temp.length());

                    temp = "j " + L2.getName() + "\n";
                    bw.write(temp, 0, temp.length());

                    temp = L1.toString() + "\n";
                    bw.write(temp, 0, temp.length());

                    temp = "addi " + result.getRegister() + ", $zero, 1\n";
                    bw.write(temp, 0, temp.length());

                    temp = L2.toString() + "\n";
                } else if (op.equals("&")) {
                    temp = "and " + result.getRegister() + ", " + result.getRegister() + ", " + arg2.getName() + "\n";
                } else if (op.equals("|")) {
                    temp = "or " + result.getRegister() + ", " + result.getRegister() + ", " + arg2.getName() + "\n";
                } else if (op.equals("!")) {
                    temp = "xor " + result.getRegister() + ", " + result.getRegister() + ", " + arg2.getName() + "\n";
                }
            } else {
                if (!arg2.getType().equals("temporary")) {
                    temp = "lw " + arg2.getRegister() + ", " + arg2.getName() + "\n";
                    bw.write(temp, 0, temp.length());
                }else{
                    strArg1 = arg2.getName();
                    int i = getPositionTemp(strArg1);
                 
                    temp = "lw " + arg2.getRegister() + ", " + "temporaries + " + (i*4) + "\n";
                    bw.write(temp, 0, temp.length());
                }
                if (op.equals("+")) {
                    temp = "add " + result.getRegister() + ", " + result.getRegister() + ", " + arg2.getRegister() + "\n";
                } else if (op.equals("-")) {
                    temp = "sub " + result.getRegister() + ", " + result.getRegister() + ", " + arg2.getRegister() + "\n";
                } else if (op.equals("*")) {
                    temp = "mul " + result.getRegister() + ", " + result.getRegister() + ", " + arg2.getRegister() + "\n";
                } else if (op.equals("/")) {
                    temp = "la $a0, msg\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "teqi " + arg2.getRegister() + ", " + "0\n";
                    Label L = new Label(true);
                    bw.write(temp, 0, temp.length());
                    
                    temp = "j " + L.getName() + "\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "j exit\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = L.toString() + "\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "div " + result.getRegister() + ", " + result.getRegister() + ", " + arg2.getRegister() + "\n";
                } else if (op.equals("\\")) {
                    temp = "la $a0, msg\n";
                    bw.write(temp, 0, temp.length());
                    
                    Label L = new Label(true);
                    
                    temp = "teqi " + arg2.getRegister() + ", " + "0\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "j " + L.getName() + "\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "j exit\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = L.toString() + "\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "div " + result.getRegister() + ", " + result.getRegister() + ", " + arg2.getRegister() + "\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "mfhi " + result.getRegister() + "\n";
                } else if (op.equals("<")) {
                    Label L1 = new Label(false);
                    Label L2 = new Label(false);

                    //if arg1 < arg2 go to L1
                    temp = "blt " + result.getRegister() + ", " + arg2.getRegister() + ", " + L1.getName() + "\n";
                    bw.write(temp, 0, temp.length());

                    temp = "add " + result.getRegister() + ", $zero, $zero\n";
                    bw.write(temp, 0, temp.length());

                    temp = "j " + L2.getName() + "\n";
                    bw.write(temp, 0, temp.length());

                    temp = L1.toString() + "\n";
                    bw.write(temp, 0, temp.length());

                    temp = "addi " + result.getRegister() + ", $zero, 1\n";
                    bw.write(temp, 0, temp.length());

                    temp = L2.toString() + "\n";
                } else if (op.equals("<=")) {
                    Label L1 = new Label(false);
                    Label L2 = new Label(false);

                    //if arg1 <= arg2 go to L1
                    temp = "ble " + result.getRegister() + ", " + arg2.getRegister() + ", " + L1.getName() + "\n";
                    bw.write(temp, 0, temp.length());

                    temp = "add " + result.getRegister() + ", $zero, $zero\n";
                    bw.write(temp, 0, temp.length());

                    temp = "j " + L2.getName() + "\n";
                    bw.write(temp, 0, temp.length());

                    temp = L1.toString() + "\n";
                    bw.write(temp, 0, temp.length());

                    temp = "addi " + result.getRegister() + ", $zero, 1\n";
                    bw.write(temp, 0, temp.length());

                    temp = L2.toString() + "\n";
                } else if (op.equals(">")) {
                    Label L1 = new Label(false);
                    Label L2 = new Label(false);

                    //if arg1 > arg2 go to L1
                    temp = "bgt " + result.getRegister() + ", " + arg2.getRegister() + ", " + L1.getName() + "\n";
                    bw.write(temp, 0, temp.length());

                    temp = "add " + result.getRegister() + ", $zero, $zero\n";
                    bw.write(temp, 0, temp.length());

                    temp = "j " + L2.getName() + "\n";
                    bw.write(temp, 0, temp.length());

                    temp = L1.toString() + "\n";
                    bw.write(temp, 0, temp.length());

                    temp = "addi " + result.getRegister() + ", $zero, 1\n";
                    bw.write(temp, 0, temp.length());

                    temp = L2.toString() + "\n";
                } else if (op.equals(">=")) {
                    Label L1 = new Label(false);
                    Label L2 = new Label(false);

                    //if arg1 >= arg2 go to L1
                    temp = "bge " + result.getRegister() + ", " + arg2.getRegister() + ", " + L1.getName() + "\n";
                    bw.write(temp, 0, temp.length());

                    temp = "add " + result.getRegister() + ", $zero, $zero\n";
                    bw.write(temp, 0, temp.length());

                    temp = "j " + L2.getName() + "\n";
                    bw.write(temp, 0, temp.length());

                    temp = L1.toString() + "\n";
                    bw.write(temp, 0, temp.length());

                    temp = "addi " + result.getRegister() + ", $zero, 1\n";
                    bw.write(temp, 0, temp.length());

                    temp = L2.toString() + "\n";
                } else if (op.equals("=")) {
                    Label L1 = new Label(false);
                    Label L2 = new Label(false);

                    //if arg1 = arg2 go to L1
                    temp = "beq " + result.getRegister() + ", " + arg2.getRegister() + ", " + L1.getName() + "\n";
                    bw.write(temp, 0, temp.length());

                    temp = "add " + result.getRegister() + ", $zero, $zero\n";
                    bw.write(temp, 0, temp.length());

                    temp = "j " + L2.getName() + "\n";
                    bw.write(temp, 0, temp.length());

                    temp = L1.toString() + "\n";
                    bw.write(temp, 0, temp.length());

                    temp = "addi " + result.getRegister() + ", $zero, 1\n";
                    bw.write(temp, 0, temp.length());

                    temp = L2.toString() + "\n";
                } else if (op.equals("#")) {
                    Label L1 = new Label(false);
                    Label L2 = new Label(false);

                    //if arg1 # arg2 go to L1
                    temp = "bne " + result.getRegister() + ", " + arg2.getRegister() + ", " + L1.getName() + "\n";
                    bw.write(temp, 0, temp.length());

                    temp = "add " + result.getRegister() + ", $zero, $zero\n";
                    bw.write(temp, 0, temp.length());

                    temp = "j " + L2.getName() + "\n";
                    bw.write(temp, 0, temp.length());

                    temp = L1.toString() + "\n";
                    bw.write(temp, 0, temp.length());

                    temp = "addi " + result.getRegister() + ", $zero, 1\n";
                    bw.write(temp, 0, temp.length());

                    temp = L2.toString() + "\n";
                } else if (op.equals("&")) {
                    temp = "and " + result.getRegister() + ", " + result.getRegister() + ", " + arg2.getRegister() + "\n";
                } else if (op.equals("|")) {
                    temp = "or " + result.getRegister() + ", " + result.getRegister() + ", " + arg2.getRegister() + "\n";
                } else if (op.equals("!")) {
                    temp = "xor " + result.getRegister() + ", " + result.getRegister() + ", " + arg2.getRegister() + "\n";
                }
            }

            bw.write(temp, 0, temp.length());

            if (chave) {
                //armazena o novo valor na variável de entrada... exemplo: x := x + 1 ... o resultado de x + 1 está sendo colocado em x com essa instrução.
                temp = "sw " + result.getRegister() + ", " + result.getName() + "\n";
                //como esse valor foi guardado em x, o registrador pode ser liberado.
                if(result.getRegister() != null){
                    freeTempReg(result.getRegister());
                    result.setRegister(null);   
                }
                
                if (arg1.getRegister() != null) {
                    freeTempReg(arg1.getRegister());
                    arg1.setRegister(null);
                }
                if (arg2.getRegister() != null) {
                    freeTempReg(arg2.getRegister());
                    arg2.setRegister(null);
                }
                bw.write(temp, 0, temp.length());
            } else {
                //libera os registradores temporarios, caso nenhuma instrução subsequente precise dos valores guardados neles
                String aux = result.getName();
                int i = Integer.parseInt(aux.substring(1, aux.length()));
                temp = "sw " + result.getRegister() + ", " + "temporaries + " + (i*4) + "\n";
                if (result.getRegister() != null) {
                    freeTempReg(result.getRegister());
                    result.setRegister(null);
                }
                if (arg1.getRegister() != null) {
                    freeTempReg(arg1.getRegister());
                    arg1.setRegister(null);
                }
                if (arg2.getRegister() != null) {
                    freeTempReg(arg2.getRegister());
                    arg2.setRegister(null);
                }
                bw.write(temp, 0, temp.length());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleConditionalJump(Quadruple instruction, BufferedWriter bw) {
        try {
            String label = ((Label) instruction.getResult()).getName();
            Variable arg1 = (Variable) instruction.getArg1();
            String temp;

            if (arg1.getType().equals("constant")) {
                if(arg1.getRegister() == null){
                    arg1.setRegister(getTempReg());
                }
                temp = "li " + arg1.getRegister() + ", " + arg1.getName() + "\n";
                bw.write(temp, 0, temp.length());

                temp = "beq " + arg1.getRegister() + ", " + "$zero" + ", " + label + "\n";            
            } else if (arg1.getType().equals("temporary")) {
                if (arg1.getRegister() == null) {
                    arg1.setRegister(getTempReg());
                }
                temp = "beq " + arg1.getRegister() + ", $zero, " + label + "\n";
            } else {
                if (arg1.getRegister() == null) {
                    arg1.setRegister(getTempReg());
                }
                temp = "beq " + arg1.getRegister() + ", $zero, " + label + "\n";
            }

            bw.write(temp, 0, temp.length());
            
            if(arg1.getRegister() != null){
                if(tempVarFinished(arg1)){
                    freeTempReg(arg1.getRegister());
                    arg1.setRegister(null);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleUnconditionalJump(Quadruple instruction, BufferedWriter bw) {
        try {
            String label = ((Label) instruction.getResult()).getName();

            String temp = "j " + label + "\n";
            bw.write(temp, 0, temp.length());
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void functionCall(List<Quadruple> IRList, int index, BufferedWriter bw) {
        try {
            CallIR instruction = (CallIR) IRList.get(index);
            int paramCount = Integer.parseInt((String) instruction.getArg2());
            String function = (String) instruction.getArg1();
            
            if (paramCount > 0) {
                int paramIndex = index - paramCount;
                ParameterIR param = (ParameterIR) IRList.get(paramIndex);
                Variable arg1 = (Variable) param.getArg1();
                if(arg1.getType().equals("temporary")){
                    if(arg1.getRegister() == null){
                        arg1.setRegister(getTempReg());
                    }
                }
                if (function.equals("read")) {
                    String temp = "jal " + function + "\n";
                    bw.write(temp, 0, temp.length());
                    if(arg1.getType().equals("temporary")){
                        temp = "move " + arg1.getRegister() + ", $v0\n";
                    } else{
                        temp = "sw " + "$v0 " + ", " + param.getArg1() + "\n";
                    }
                    bw.write(temp, 0, temp.length());
                    
                } else if (function.equals("write")) {
                    String temp;
                    
                    if(arg1.getType().equals("temporary")){
                        temp = "move $a1, " + arg1.getRegister() + "\n";
                    } else{
                        temp = "lw " + "$a1 " + ", " + param.getArg1() + "\n";
                    }
                    bw.write(temp, 0, temp.length());
                    
                    temp = "la $a0, str"+arg1.getName()+"\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "jal " + function + "\n";
                    bw.write(temp, 0, temp.length());
                    
                } else if (function.equals("writevet")){
                    String temp;
                    
                    temp = "la $a1," + arg1.getName() + "\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "lw $a2, tam"+arg1.getName() + "\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "la $a0, str" + arg1.getName() + "\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "jal wvector\n";
                    bw.write(temp, 0, temp.length());
                    
                } else if (function.equals("evalif")){
                    String temp = "";
                    Variable v = (Variable) param.getArg1();
                    Label L = new Label(true);
                    
                    String strArg1 = arg1.getName();
                    int i = getPositionTemp(strArg1);
                    
                    temp = "lw " + arg1.getRegister() + ", " + "temporaries + " + (i*4) + "\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "la $a0, msg2\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "teqi " + arg1.getRegister() + ", 0\n";
                    bw.write(temp, 0, temp.length());                    
                    
                    temp = "j " + L.getName() + "\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "j exit\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = L.toString() + "\n";
                    bw.write(temp, 0, temp.length());
                    
                } else if(function.equals("evalfrom1")){
                    String temp = "";
                    Variable v = (Variable) param.getArg1();
                    Label L = new Label(true);
                    
                    String strArg1 = arg1.getName();
                    int i = getPositionTemp(strArg1);
                    
                    temp = "lw " + arg1.getRegister() + ", " + "temporaries + " + (i*4) + "\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "la $a0, msg3\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "teqi " + arg1.getRegister() + ", 0\n";
                    bw.write(temp, 0, temp.length());                    
                    
                    temp = "j " + L.getName() + "\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "j exit\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = L.toString() + "\n";
                    bw.write(temp, 0, temp.length());
                    
                } else if(function.equals("evalfrom2")){
                    String temp = "";
                    Variable v = (Variable) param.getArg1();
                    Label L = new Label(true);
                    
                    String strArg1 = arg1.getName();
                    int i = getPositionTemp(strArg1);
                    
                    temp = "lw " + arg1.getRegister() + ", " + "temporaries + " + (i*4) + "\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "la $a0, msg4\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "teqi " + arg1.getRegister() + ", 1\n";
                    bw.write(temp, 0, temp.length());                    
                    
                    temp = "j " + L.getName() + "\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "j exit\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = L.toString() + "\n";
                    bw.write(temp, 0, temp.length());
                }
                
                if(arg1.getRegister() != null){
                    freeTempReg(arg1.getRegister());
                    arg1.setRegister(null);
                }
                
            } else {
                if (function.equals("exit")) {
                    String temp = "j " + function + "\n";
                    bw.write(temp, 0, temp.length());
                } else if (function.equals("pfinished")) {
                    String temp;
                    for (int i = 0; i < nTemps; i++) {
                        String str = getTempReg();
                        
                        temp = "lw " + str + ", offset($sp)\n";
                        bw.write(temp, 0, temp.length());
                        
                        temp = "sw " + str + ", " + "temporaries + " + (i*4) + "\n" ;
                        bw.write(temp, 0, temp.length());
                        
                        temp = "addi $a0, $a0, -4\n";
                        bw.write(temp, 0, temp.length());
                        
                        temp = "sw $a0, offset\n";
                        bw.write(temp, 0, temp.length());
                        
                        temp = "addi $sp, $sp, 4\n";
                        bw.write(temp, 0, temp.length());
                    
                        freeTempReg(str);
                    }
                    
                    temp = "lw $ra, " + "offset($sp)\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "lw $a0, offset\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "addi $a0, $a0, -4\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "sw $a0, offset\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "addi $sp, $sp, 4\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "jr $ra\n";
                    bw.write(temp, 0, temp.length());
                    
                } else if (function.equals("initproc")){
                    String temp = "addi $sp, $sp, -4\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "sw $ra" + ", " + "offset($sp)\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "lw $a0, offset\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "addi $a0, $a0, 4\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "sw $a0, offset\n";
                    bw.write(temp, 0, temp.length());
                    
                    for (int i = 0 ; i < nTemps; i++){
                        String str = getTempReg();
                        
                        temp = "addi $sp, $sp, -4\n";
                        bw.write(temp, 0, temp.length());
                        
                        temp = "lw " + str + ", " + "temporaries + " + (i*4) + "\n";
                        bw.write(temp, 0, temp.length());
                        
                        temp = "sw " + str + ", " + "offset($sp)\n";
                        bw.write(temp, 0, temp.length());
                        
                        temp = "lw $a0, offset\n";
                        bw.write(temp, 0, temp.length());
                        
                        temp = "addi $a0, $a0, 4\n";
                        bw.write(temp, 0, temp.length());
                        
                        temp = "sw $a0, offset\n";
                        bw.write(temp, 0, temp.length());
                        
                        freeTempReg(str);
                    }                    
                }else {
                    String temp = "jal " + function + "\n";
                    bw.write(temp, 0, temp.length());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void functionUncall(List<Quadruple> IRList, int index, BufferedWriter bw) {
        try {
            UncallIR instruction = (UncallIR) IRList.get(index);
            String function = (String) instruction.getArg1();
            String temp = "";
            
            temp = "jal " + function + "\n";
            bw.write(temp, 0, temp.length());
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void handleCopy(Quadruple instruction, BufferedWriter bw) {
        try{
            Variable result = (Variable)instruction.getResult();
            Variable arg1 = (Variable)instruction.getArg1();
            String temp = "";
                                  
            if(result.getType().equals("temporary")){
                if(result.getRegister() == null){
                    result.setRegister(getTempReg());
                }
                
                if(arg1.getType().equals("temporary")){
                    String strArg1 = arg1.getName();
                    int i = getPositionTemp(strArg1);
                    temp = "lw " + result.getRegister() + ", " + "temporaries + " + (i*4) + "\n";
                    bw.write(temp, 0, temp.length());
                    String strResult = result.getName();
                    i = getPositionTemp(strResult);
                    temp = "sw " + result.getRegister() + ", " + "temporaries + " + (i*4) + "\n";
                    bw.write(temp, 0, temp.length());
                }else{
                    temp = "lw " + result.getRegister() + ", " + arg1.getName() + "\n";
                    bw.write(temp, 0, temp.length());
                    String strResult = result.getName();
                    int i = getPositionTemp(strResult);
                    temp = "sw " + result.getRegister() + ", " + "temporaries + " + (i*4) + "\n";
                    bw.write(temp, 0, temp.length());
                }
                
                
                 if(result.getRegister() != null){
                    freeTempReg(result.getRegister());
                    result.setRegister(null);
                 }
                
                
                if(arg1.getRegister() != null){
                    freeTempReg(arg1.getRegister());
                    arg1.setRegister(null);
                }
                
            }else{
                if(result.getRegister() == null){
                    result.setRegister(getTempReg());
                }
                
                if(arg1.getRegister() == null){
                    arg1.setRegister(getTempReg());
                }
                
     
                
                if(arg1.getType().equals("temporary")){
                    String strArg1 = arg1.getName();
                    int i = getPositionTemp(strArg1);
                    temp = "lw " + result.getRegister() + ", " + "temporaries + " + (i*4) + "\n";
                    bw.write(temp, 0, temp.length());
                    temp = "sw " + result.getRegister() + ", " + result.getName() + "\n";
                    bw.write(temp, 0, temp.length());
                }else{
                    temp = "lw " + result.getRegister() + ", " + arg1.getName() + "\n";
                    bw.write(temp, 0, temp.length());
                    temp = "sw " + result.getRegister() + ", " + result.getName() + "\n";
                    bw.write(temp, 0, temp.length());
                }
                         
                if(result.getRegister() != null){
                        freeTempReg(result.getRegister());
                        result.setRegister(null);
                }
                if(arg1.getRegister() != null){
                        freeTempReg(arg1.getRegister());
                        arg1.setRegister(null);
                 
                }
            }
                
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    private void handleArrayIndex(Quadruple instruction, BufferedWriter bw) {
        try{
            Variable arg1 = (Variable)instruction.getArg1();
            Variable arg2 = (Variable)instruction.getArg2();
            Variable result = (Variable)instruction.getResult();
            String temp = "";
            
            if(result.getRegister() == null){
                result.setRegister(getTempReg());
            }
            if(result.getName().equals("t64")){
                System.out.println("");
            }
            //x := vet[1] result := arg1 op arg2
            if(result.getType().equals("temporary")){
                if(arg2.getType().equals("constant")){
                    temp = "lw " + result.getRegister() + ", " + arg1.getName()+ "+" + (Integer.parseInt(arg2.getName())*4) + "\n";
                    bw.write(temp, 0, temp.length());
                    String strResult = result.getName();
                    int i = getPositionTemp(strResult);
                    temp = "sw " + result.getRegister() + ", " + "temporaries + " + (i*4) + "\n";
                    bw.write(temp, 0, temp.length());                  
                } else if (arg2.getType().equals("temporary")){
                    if(arg1.getRegister() == null){
                        arg1.setRegister(getTempReg());
                    }
                    if(arg2.getRegister() == null){
                        arg2.setRegister(getTempReg());
                    }
                    temp = "la " + arg1.getRegister() + ", " + arg1.getName() + "\n";
                    bw.write(temp, 0, temp.length());
                    
                    String arg2Str = arg2.getName();
                    int i = getPositionTemp(arg2Str);
                                       
                    temp = "lw " + arg2.getRegister() + ", " + "temporaries + " + (i*4) + "\n";
                    bw.write(temp, 0, temp.length());
                                     
                    temp = "mul " + arg2.getRegister() + ", " + arg2.getRegister() + ", 4\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "add " + arg1.getRegister()+ ", " + arg1.getRegister() + ", " + arg2.getRegister() + "\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "lw " + result.getRegister() + ", " + "(" + arg1.getRegister() + ")" + "\n";                  
                    bw.write(temp, 0, temp.length());
                    
                    String strResult = result.getName();
                    i = getPositionTemp(strResult);
                    
                    temp = "sw " + result.getRegister() + ", " + "temporaries + " + (i*4) + "\n";
                    bw.write(temp, 0, temp.length());
                               
                } else {
                    if(arg1.getRegister() == null){
                        arg1.setRegister(getTempReg());
                    }
                    if(arg2.getRegister() == null){
                        arg2.setRegister(getTempReg());
                    }
                    String strResult = result.getName();
                    int i = getPositionTemp(strResult);
                    
                    temp = "la " + arg1.getRegister() + ", " + arg1.getName() + "\n";
                    bw.write(temp, 0, temp.length());
                                       
                    temp = "lw " + arg2.getRegister() + ", " + arg2.getName() + "\n";
                    bw.write(temp, 0, temp.length());
                                     
                    temp = "mul " + arg2.getRegister() + ", " + arg2.getRegister() + ", 4\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "add " + arg1.getRegister()+ ", " + arg1.getRegister() + "," + arg2.getRegister() + "\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "lw " + result.getRegister() + ", " + "(" + arg1.getRegister() + ")" + "\n";         
                    bw.write(temp, 0, temp.length());
                    
                    temp = "sw " + result.getRegister() + ", " + "temporaries + " + (i*4) + "\n";
                    bw.write(temp, 0, temp.length());
                }               
            } else {
                if(arg2.getType().equals("constant")){
                    temp = "lw " + result.getRegister() + ", " + arg1.getName()+ "+" + (Integer.parseInt(arg2.getName())*4) + "\n";
                    bw.write(temp, 0, temp.length());
                    temp = "sw " + result.getRegister() + ", " + result.getName() + "\n";
                    bw.write(temp, 0, temp.length());
                }else if (arg2.getType().equals("temporary")){
                    if(arg1.getRegister() == null){
                        arg1.setRegister(getTempReg());
                    }
                    if(arg2.getRegister() == null){
                        arg2.setRegister(getTempReg());
                    }
                    
                    temp = "la " + arg1.getRegister() + ", " + arg1.getName() + "\n";
                    bw.write(temp, 0, temp.length());
                    
                    String arg2Str = arg2.getName();
                    int i = getPositionTemp(arg2Str);
                                       
                    temp = "lw " + arg2.getRegister() + ", " + "temporaries + " + (i*4) + "\n";
                    bw.write(temp, 0, temp.length());
                                     
                    temp = "mul " + arg2.getRegister() + ", " + arg2.getRegister() + ", 4\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "add " + arg1.getRegister()+ ", " + arg1.getRegister() + ", " + arg2.getRegister() + "\n";
                    bw.write(temp, 0, temp.length());
                   
                    temp = "lw " + result.getRegister() + ", " + "(" + arg1.getRegister() + ")" + "\n";                  
                    bw.write(temp, 0, temp.length());
                                     
                    temp = "sw " + result.getRegister() + ", " + result.getName() + "\n";
                    bw.write(temp, 0, temp.length());
                   
                } else {
                    
                    if(arg1.getRegister() == null){
                        arg1.setRegister(getTempReg());
                    }
                    if(arg2.getRegister() == null){
                        arg2.setRegister(getTempReg());
                    }
                    
                    temp = "la " + arg1.getRegister() + ", " + arg1.getName() + "\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "lw " + arg2.getRegister() + ", " + arg2.getName() + "\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "mul " + arg2.getRegister() + ", " + arg2.getRegister() + ", 4\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "add " + arg1.getRegister()+ ", " + arg1.getRegister() + "," + arg2.getRegister() + "\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "lw " + result.getRegister() + ", " + "(" + arg1.getRegister() + ")" + "\n";         
                    bw.write(temp, 0, temp.length());
                                                         
                    temp = "sw " + result.getRegister() + ", " + result.getName() + "\n";
                    bw.write(temp, 0, temp.length());
                    
                }          
            }
            
            
            if(arg1.getRegister() != null){
                    freeTempReg(arg1.getRegister());
                    arg1.setRegister(null);
            }
            
            if(arg2.getRegister() != null){
                    freeTempReg(arg2.getRegister());
                    arg2.setRegister(null);
            }
            
            if(result.getRegister() != null){
                    freeTempReg(result.getRegister());
                    result.setRegister(null);
            }
            
            
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    private void handleArrayAssignment(Quadruple instruction, BufferedWriter bw) {
        try{
            Variable arg1 = (Variable)instruction.getArg1();
            Variable arg2 = (Variable)instruction.getArg2();
            Variable result = (Variable)instruction.getResult();
            String temp = "";
            
            if(result.getRegister() == null){
                result.setRegister(getTempReg());
            }
            
            if(arg1.getType().equals("temporary")){
                if(arg2.getType().equals("constant")){
                    if(arg1.getRegister() == null){
                        arg1.setRegister(getTempReg());
                    }
                    String strArg1 = arg1.getName();
                    int i = getPositionTemp(strArg1);
                    
                    temp = "lw " + result.getRegister() + ", " + "temporaries + " + (i*4) + "\n";
                    bw.write(temp, 0, temp.length());
                                                            
                    temp = "sw " + result.getRegister() + ", " + result.getName() + "+" + (Integer.parseInt(arg2.getName())*4) + "\n";
                    bw.write(temp, 0, temp.length());
                } else if (arg2.getType().equals("temporary")){
                    if(arg1.getRegister() == null){
                        arg1.setRegister(getTempReg());
                    }
                    if(arg2.getRegister() == null){
                        arg2.setRegister(getTempReg());
                    }
                    String strArg1 = arg1.getName();
                    int i = getPositionTemp(strArg1);
                    
                    temp = "lw " + arg1.getRegister() + ", " + "temporaries + " + (i*4) + "\n";
                    bw.write(temp, 0, temp.length());      
                    
                    String strArg2 = arg2.getName();
                    i = getPositionTemp(strArg2);
                    
                    temp = "lw " + arg2.getRegister() + ", " + "temporaries + " + (i*4) + "\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "mul " + arg2.getRegister() + ", " + arg2.getRegister() + ", 4\n";
                    bw.write(temp, 0, temp.length());
                                        
                    temp = "lw " + result.getRegister() + ", " + result.getName() + "\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "add " + result.getRegister() + ", " + result.getRegister() + ", " + arg2.getRegister() + "\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "sw " + arg1.getRegister() + ", (" + result.getRegister() + ")\n";
                    bw.write(temp, 0, temp.length());
                 
                } else {
                    if(arg1.getRegister() == null){
                        arg1.setRegister(getTempReg());
                    }
                    if(arg2.getRegister() == null){
                        arg2.setRegister(getTempReg());
                    }
                                
                    String strArg1 = arg1.getName();
                    int i = getPositionTemp(strArg1);
                    
                    temp = "lw " + arg1.getRegister() + ", " + "temporaries + " + (i*4) + "\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "lw " + arg2.getRegister() + ", " + arg2.getName() + "\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "mul " + arg2.getRegister() + ", " + arg2.getRegister() + ", 4\n";
                    bw.write(temp, 0, temp.length());
                   
                    temp = "la " + result.getRegister() + ", " + result.getName() + "\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "add " + result.getRegister() + ", " + result.getRegister() + ", " + arg2.getRegister() + "\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "sw " + arg1.getRegister() + ", (" + result.getRegister() + ")\n";
                    bw.write(temp, 0, temp.length());
                }
            } else{
                if (arg2.getType().equals("constant")) {
                    temp = "lw " + result.getRegister() + ", " + arg1.getName() + "\n";
                    bw.write(temp, 0, temp.length());
                    temp = "sw " + result.getRegister() + ", " + result.getName() + "+" + (Integer.parseInt(arg2.getName())*4) + "\n";
                    bw.write(temp, 0, temp.length());
                } else if(arg2.getType().equals("temporary")){
                    if(arg1.getRegister() == null){
                        arg1.setRegister(getTempReg());
                    }
                    if(arg2.getRegister() == null){
                        arg2.setRegister(getTempReg());
                    }
                    String strArg1 = arg1.getName();
                    int i = getPositionTemp(strArg1);
                    
                    temp = "lw " + arg1.getRegister() + ", " + arg1.getName() + "\n";
                    bw.write(temp, 0, temp.length());      
                    
                    String strArg2 = arg2.getName();
                    i = getPositionTemp(strArg2);
                    
                    temp = "lw " + arg2.getRegister() + ", " + "temporaries + " + (i*4) + "\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "mul " + arg2.getRegister() + ", " + arg2.getRegister() + ", 4\n";
                    bw.write(temp, 0, temp.length());
                                        
                    temp = "la " + result.getRegister() + ", " + result.getName() + "\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "add " + result.getRegister() + ", " + result.getRegister() + ", " + arg2.getRegister() + "\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "sw " + arg1.getRegister() + ", (" + result.getRegister() + ")\n";
                    bw.write(temp, 0, temp.length());
                    
                } else {
                    if(arg1.getRegister() == null){
                        arg1.setRegister(getTempReg());
                    }
                    if(arg2.getRegister() == null){
                        arg2.setRegister(getTempReg());
                    }
                    
                    temp = "lw " + arg1.getRegister() + ", " + arg1.getName() + "\n";
                    bw.write(temp, 0, temp.length());      
                                       
                    temp = "lw " + arg2.getRegister() + ", " + arg2.getName() + "\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "mul " + arg2.getRegister() + ", " + arg2.getRegister() + ", 4\n";
                    bw.write(temp, 0, temp.length());
                                        
                    temp = "la " + result.getRegister() + ", " + result.getName() + "\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "add " + result.getRegister() + ", " + result.getRegister() + ", " + arg2.getRegister() + "\n";
                    bw.write(temp, 0, temp.length());
                    
                    temp = "sw " + arg1.getRegister() + ", (" + result.getRegister() + ")\n";
                    bw.write(temp, 0, temp.length());
                }
                
            }
            
            if(arg1.getRegister() != null){
                    freeTempReg(arg1.getRegister());
                    arg1.setRegister(null);
            }
            if(arg2.getRegister() != null){
                    freeTempReg(arg2.getRegister());
                    arg2.setRegister(null);
            }
            if(result.getRegister() != null){
                    freeTempReg(result.getRegister());
                    result.setRegister(null);
            }
            
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public void initRegTemp() {
        for (int j = 0; j < 10; j++) {
            regTemp.put("$t" + j, Boolean.FALSE);
        }
        for (int j = 0; j < 8; j++) {
            regTemp.put("$s" + j, Boolean.FALSE);
        }
    }

    //pega um registrador temporário livre
    public String getTempReg() {
        String str = "";
        for (int j = 0; j < 10; j++) {
            str = "$t" + j;
            if (!regTemp.get(str).booleanValue()) {
                regTemp.replace(str, Boolean.TRUE);
                return str;
            }
        }
        for (int j = 0; j < 8; j++) {
            str = "$s" + j;
            if (!regTemp.get(str).booleanValue()) {
                regTemp.replace(str, Boolean.TRUE);
                return str;
            }
        }
        return null;
    }

    //libera um registrador temporario p/ outro
    public void freeTempReg(String reg) {
        if (regTemp.get(reg).booleanValue()) {
            regTemp.replace(reg, Boolean.FALSE);
        }
    }
    
    public int contFreeReg(){
        String str = "";
        int cont = 0;
        for (int j = 0; j < 10; j++) {
            str = "$t" + j;
            if (!regTemp.get(str).booleanValue()) {
                cont ++;
            }
        }
        for (int j = 0; j < 8; j++) {
            str = "$s" + j;
            if (!regTemp.get(str).booleanValue()) {
                cont ++;
            }
        }
        return cont;
    }

    //verifica se nenhuma instrução subsequente usa a variável v
    public boolean tempVarFinished(Variable v) {
        boolean resp = true;
        for (int j = indiceIR + 1; j < IRList.size(); j++) {
            if (IRList.get(j).getArg1() == v || IRList.get(j).getArg2() == v || IRList.get(j).getResult() == v) {
                resp = false;
            }
        }
        return resp;
    }

   public void getNumTemp(){
       Quadruple q;
       Variable v;
       int maior = 0;
       int indice = 0;
       for (int i = 0; i < IRList.size(); i++) {
           q = IRList.get(i);
           if(q.getArg1() != null && q.getArg1() instanceof Variable){
                v = (Variable) q.getArg1();
                if(v.getType().equals("temporary")){
                    String str = v.getName();
                    indice = Integer.parseInt(str.substring(1,str.length()));
                    if(indice > maior){
                        maior = indice;
                    }
                }
           }
           if(q.getArg2() != null && q.getArg2() instanceof Variable){
                v = (Variable) q.getArg2();
                if(v.getType().equals("temporary")){
                   String str = v.getName();
                    indice = Integer.parseInt(str.substring(1,str.length()));
                    if(indice > maior){
                        maior = indice;
                    }
                }
           }
           if(q.getResult() != null && q.getResult() instanceof Variable){
               v = (Variable) q.getResult();
               if(v.getType().equals("temporary")){
                   String str = v.getName();
                    indice = Integer.parseInt(str.substring(1,str.length()));
                    if(indice > maior){
                        maior = indice;
                    }
               }
           }
           
       }
       nTemps = indice + 1;
   }

   public int getPositionTemp(String s){
       int pos;
       pos = Integer.parseInt(s.substring(1, s.length()));
       return pos;
   }

    private void handleUnaryAssignment(Quadruple instruction, BufferedWriter bw) {
        try{
        String op = (String) instruction.getOp();
        Variable result = (Variable) instruction.getResult();
        Variable arg1 = (Variable) instruction.getArg1();
        String temp = "";
        boolean chave = false;
        
        if(!result.getType().equals("temporary")){
            chave = true;
        }
        
        if(result.getRegister() == null){
            result.setRegister(getTempReg());
        }
        
        if(arg1.getType().equals("temporary")){
            if(op.equals("-")){
                if(arg1.getRegister() == null)
                    arg1.setRegister(getTempReg());
                
                String strArg1 = arg1.getName();
                int i = getPositionTemp(strArg1);
                
                temp = "lw " + arg1.getRegister() + ", " + "temporaries + " + i*4 + "\n";
                bw.write(temp,0,temp.length());
                
                temp = "mul " + arg1.getRegister() + ", " + arg1.getRegister() + ", -1"  + "\n";
                bw.write(temp,0,temp.length());
                
                temp = "move " + result.getRegister() + ", " + arg1.getRegister() + "\n";
                bw.write(temp,0,temp.length());
                
            } else if(op.equals("~")){
                if(arg1.getRegister() == null)
                    arg1.setRegister(getTempReg());
                
                String strArg1 = arg1.getName();
                int i = getPositionTemp(strArg1);
                
                temp = "lw " + arg1.getRegister() + ", " + "temporaries + " + i*4 + "\n";
                bw.write(temp,0,temp.length());
                
                
                Label L1 = new Label(false);
                Label L2 = new Label(false);

                temp = "beq " + arg1.getRegister() + ", $zero" + ", " + L1.getName() + "\n";
                bw.write(temp, 0, temp.length());
                
                temp = "li " + result.getRegister() + ", 0\n";
                bw.write(temp, 0, temp.length());
                
                temp = "j " + L2.getName() + "\n";
                bw.write(temp, 0, temp.length());
                
                temp = L1.toString() + "\n";
                bw.write(temp, 0, temp.length());
                
                temp = "li " + result.getRegister() + ", 1\n";
                bw.write(temp, 0, temp.length());
                
                temp = L2.toString() + "\n";
                bw.write(temp, 0, temp.length());
                
            }
        } else if (arg1.getType().equals("constant")) {
            if(op.equals("-")){
                temp = "li " + result.getRegister() + ", " + arg1.getName() + "\n";
                bw.write(temp,0,temp.length());
                
                temp = "mul " + result.getRegister() + ", " + result.getRegister() + ", -1\n";
                bw.write(temp,0,temp.length());
                
            }
        } else {
            if(op.equals("-")){
                temp = "lw " + result.getRegister() + ", " + arg1.getName() + "\n";
                bw.write(temp,0,temp.length());
                
                temp = "mul " + result.getRegister() + ", " + result.getRegister() + ", -1\n";
                bw.write(temp,0,temp.length());
                
            }
        }
        
        if(chave){
            temp = "sw " + result.getRegister() + ", " + result.getName() + "\n";
            bw.write(temp,0,temp.length());            
        } else {
            String strResult = result.getName();
            int i = getPositionTemp(strResult);
            temp = "sw " + result.getRegister() + ", " + "temporaries + " + (i*4) + "\n";
            bw.write(temp,0,temp.length());
        }
        
        if(result.getRegister() != null){
            freeTempReg(result.getRegister());
            result.setRegister(null);   
        }
        
        if(arg1.getRegister() != null){
            freeTempReg(arg1.getRegister());
            arg1.setRegister(null);
        }
        
        }catch(IOException e){
            e.printStackTrace();
        }
        
    }
}
