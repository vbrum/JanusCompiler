/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backpatching;

import IR.CallIR;
import IR.Quadruple;
import IR.UncallIR;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author VBrum
 */
public class BackPatcherUncall {
    private List<Quadruple> IRList;
    private HashMap<String, String> workList;
    
    public BackPatcherUncall(List<Quadruple> instructions, HashMap<String, String> work){
        IRList = instructions;
        workList = work;
    }
    
    public void patch() {
        for (Quadruple q : IRList) {
            if (q instanceof UncallIR) {
                String methodName = (String) q.getArg1();        
                    q.setArg1(workList.get(methodName));               
            } 
        }
    }
}
