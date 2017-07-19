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
public class BackPatcherCall {

    private List<Quadruple> IRList;
    private HashMap<String, String> workList;

    public BackPatcherCall(List<Quadruple> instructions, HashMap<String, String> work) {
        IRList = instructions;
        workList = work;
    }

    public void patch() {
        for (Quadruple q : IRList) {
            if (q instanceof CallIR) {
                String methodName = (String) q.getArg1();

                if (methodName.equals("write")) {
                    q.setArg1("write");
                } else if (methodName.equals("exit")) {
                    q.setArg1("exit");
                } else if (methodName.equals("read")) {
                    q.setArg1("read");
                } else if (methodName.equals("initproc")){
                    q.setArg1("initproc");
                } else if(methodName.equals("pfinished")){
                    q.setArg1("pfinished");
                } else if(methodName.equals("evalif")){
                    q.setArg1("evalif");
                } else if(methodName.equals("evalfrom1")){
                    q.setArg1("evalfrom1");
                } else if(methodName.equals("evalfrom2")){
                    q.setArg1("evalfrom2");
                } else {
                    q.setArg1(workList.get(methodName));
                }
            } 
        }
    }
}
