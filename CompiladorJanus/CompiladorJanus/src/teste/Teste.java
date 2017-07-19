/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teste;

import IR.Quadruple;
import Visitor.*;
import syntaxTree.*;
import Visitor.PrintTree;
import backpatching.BackPatcherCall;
import backpatching.BackPatcherUncall;
import codegen.CodG;
import interfaceGrafica.TelaInicial;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java_cup.runtime.*;
import linker.Linker;
import newpackage.Helper;
import newpackage.Label;
import newpackage.Temporary;
import symbolTable.*;

/**
 *
 * @author VBrum
 */
public class Teste {

    /**
     * @param args the command line arguments
     */
//    public static void main(String[] args) {
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Windows".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(TelaInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(TelaInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(TelaInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(TelaInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new TelaInicial().setVisible(true);
//            }
//        });
    
    
    public static void main(String[] args) {
        String rootPath = Paths.get("").toAbsolutePath().toString();
        String subPath = "/src/samples/";
        String sourcecode = rootPath + subPath + "factorization.janus";
        String s = "program.janus";
        Symbol parse_tree = null;

        try {
            Parser p = new Parser(new LexicalAnalyzer(new FileReader(sourcecode)));

            //Object result = p.parse().value;
            parse_tree = p.parse();

            Program program = ((Program) parse_tree.value);
            
            
            if (program != null) {

                BuildSymbolTableVisitor bstVisitor = new BuildSymbolTableVisitor();
                bstVisitor.visit(program);

                Scope symbolTable = bstVisitor.getFirstScope();

                if (symbolTable == null) {
                    System.err.println("Symbol Table is null.");
                    return;
                }

                UndefinedVariableVisitor undefinedVisitor = new UndefinedVariableVisitor(symbolTable);
                undefinedVisitor.visit(program);

                TypeCheckingVisitor typeCheckVisitor = new TypeCheckingVisitor(symbolTable);
                typeCheckVisitor.visit(program);

                if (bstVisitor.errorDetected || undefinedVisitor.errorDetected || typeCheckVisitor.errorDetected) {
                    return;
                }

                //Generate IR
                IRVisitor intermediateVisitor = new IRVisitor(symbolTable);
                intermediateVisitor.visit(program);
                
                Temporary.restartTemp();
                
                RCodGenVisitor rcgVisitor = new RCodGenVisitor(symbolTable);
                rcgVisitor.visit(program);
                
                String output = "reversed"+ s.substring(0, s.lastIndexOf(".")) + ".janus";
                
                FileWriter fw = new FileWriter(output);
                
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(rcgVisitor.getReversedCode());
                
                if (bw != null) {
                    bw.close();
                }
                
                System.out.println("------------------");
                System.out.println(rcgVisitor.getReversedCode());
                
                ReversibleIRVisitor reversibleVisitor = new ReversibleIRVisitor(symbolTable);
                reversibleVisitor.visit(program);
                
                List<Quadruple> IRList2 = reversibleVisitor.getIR();
                Hashtable<Quadruple, List<Label>> labels2 = reversibleVisitor.getLabels();
                
                
                

                List<Quadruple> IRList = intermediateVisitor.getIR();
                Hashtable<Quadruple, List<Label>> labels = intermediateVisitor.getLabels();
                
                
                HashMap<String, String> workList = intermediateVisitor.getWorkList();

                BackPatcherCall backPatchCall = new BackPatcherCall(IRList, workList);
                backPatchCall.patch();
                
                HashMap<String, String> workList2 = reversibleVisitor.getWorkList();
                
                BackPatcherUncall backPatchUncall = new BackPatcherUncall(IRList, workList2);
                backPatchUncall.patch();
                

                BackPatcherCall backPatchCall2 = new BackPatcherCall(IRList2, workList2);
                backPatchCall2.patch();
                
                BackPatcherUncall backPatchUncall2 = new BackPatcherUncall(IRList2, workList);
                backPatchUncall2.patch();
                
                SymbolTable symTable = (SymbolTable) symbolTable;
                String fileName = s.substring(0, s.lastIndexOf(".")) + ".asm";
                symTable.calculateVarOffsets();
                CodG gen = new CodG(IRList, labels, symTable, workList2, fileName, false);
                gen.generateMips();
                String fileName2 = output + ".asm";
                CodG gen2 = new CodG(IRList2, labels2, symTable, workList, fileName2, true);
                gen2.generateMips();
                
                //Link runtime.asm file
                Linker linker1 = new Linker("C:/Users/VBrum/Documents/NetBeansProjects/CompiladorJanus/" + fileName2, fileName);
                linker1.link();
                Linker linker = new Linker("C:/Users/VBrum/Documents/NetBeansProjects/CompiladorJanus/src/linker/runtime.asm", fileName);
                linker.link();

                System.out.println("Compilacao concluida com sucesso...");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
