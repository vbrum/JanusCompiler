/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaceGrafica;

import IR.Quadruple;
import Visitor.BuildSymbolTableVisitor;
import Visitor.IRVisitor;
import Visitor.RCodGenVisitor;
import Visitor.ReversibleIRVisitor;
import Visitor.TypeCheckingVisitor;
import Visitor.UndefinedVariableVisitor;
import backpatching.BackPatcherCall;
import backpatching.BackPatcherUncall;
import codegen.CodG;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java_cup.runtime.Symbol;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import linker.Linker;
import newpackage.Label;
import symbolTable.Scope;
import symbolTable.SymbolTable;
import syntaxTree.Proc;
import syntaxTree.Program;
import teste.LexicalAnalyzer;
import teste.Parser;

/**
 *
 * @author VBrum
 */
public class TelaInicial extends javax.swing.JFrame {

    String reversedCode;
    String rootPath = Paths.get("").toAbsolutePath().toString();
    String sourcecode = rootPath + "\\example.janus";
    private FileWriter fw;
    private final int tabSize = 3;
    boolean rcommand = false;

    /**
     * Creates new form TelaInicial
     */
    public TelaInicial() {
        initComponents();

        jTextArea1.setTabSize(tabSize);
        jTextArea3.setTabSize(tabSize);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Compilador");

        jButton1.setText("Run");
        jButton1.setPreferredSize(new java.awt.Dimension(70, 23));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Invert");
        jButton2.setPreferredSize(new java.awt.Dimension(70, 23));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);
        try{
            fw = new FileWriter(sourcecode, true);
            jTextArea1.write(fw);
            fw.close();
        }catch(IOException e){
            e.printStackTrace();
        }

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane2.setViewportView(jTextArea2);

        jLabel1.setText("Output:");

        jTextArea3.setColumns(20);
        jTextArea3.setRows(5);
        jScrollPane3.setViewportView(jTextArea3);
        try{
            fw = new FileWriter(sourcecode, true);
            jTextArea1.write(fw);
            fw.close();
        }catch(IOException e){
            e.printStackTrace();
        }

        jLabel2.setText("Forward");

        jLabel3.setText("Reverse");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Fibonacci", "Factorization", "Square Root", "Perm-to-code" }));
        jComboBox1.setSelectedIndex(-1);
        if(jComboBox1.getSelectedIndex() == -1)
        jComboBox1.setName("Examples");
        jComboBox1.setPreferredSize(new java.awt.Dimension(90, 23));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel4.setText("Examples");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(458, 458, 458)
                        .addComponent(jLabel3))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 488, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 488, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4)))
                    .addComponent(jLabel1)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 986, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(35, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 407, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 407, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(6, 6, 6)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(35, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    
    
    
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        jTextArea3.setText(reversedCode);
    }//GEN-LAST:event_jButton2ActionPerformed
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            fw = new FileWriter(sourcecode, false);
            fw.write(jTextArea1.getText());
            fw.close();
            String s = "program.janus";
            Symbol parse_tree = null;
            String msg = "";
                  
            
            Parser p = new Parser(new LexicalAnalyzer(new FileReader(sourcecode)));

            try{
                
                parse_tree = p.parse();
                
            }catch(Exception e){
                jTextArea2.setText(p.errorMessage);
              return;
            }

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
                    String str = "Error: \n";
                    if(bstVisitor.errorDetected)
                        str += bstVisitor.errorMessage + "\n";
                    if(undefinedVisitor.errorDetected)
                        str += undefinedVisitor.errorMessage + "\n"; 
                    if(typeCheckVisitor.errorDetected)
                        str += typeCheckVisitor.errorMessage + "\n";
                    jTextArea2.setText(str);                    
                    return;
                }

                //Generate IR
                IRVisitor intermediateVisitor = new IRVisitor(symbolTable);
                intermediateVisitor.visit(program);

                RCodGenVisitor rcgVisitor = new RCodGenVisitor(symbolTable);
                rcgVisitor.visit(program);

                String output = "reversed" + s.substring(0, s.lastIndexOf(".")) + ".janus";

                FileWriter fw = new FileWriter(output);

                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(rcgVisitor.getReversedCode());

                reversedCode = rcgVisitor.getReversedCode();

                if (bw != null) {
                    bw.close();
                }

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
                String rootPath = Paths.get("").toAbsolutePath().toString();
                Linker linker1 = new Linker(rootPath + "\\" + fileName2, fileName);
                linker1.link();
                Linker linker = new Linker(rootPath + "\\runtime.asm", fileName);
                linker.link();

                String pathAsm = "java -jar " + rootPath + "\\src\\tools\\Mars4_5.jar " + rootPath + "\\program.asm";
                String str = "";
                str = execCommand(pathAsm);
                jTextArea2.setText(str);

            }

        } catch (Exception e) {
            e.printStackTrace();
            jTextArea2.setText(e.getMessage());
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        BufferedReader input;
        String str;
        String code = "";
        String path = "C:\\Users\\Brum\\Downloads\\CompiladorJanus\\CompiladorJanus\\src\\samples";
        String selectedItem = (String) jComboBox1.getSelectedItem();
        if (selectedItem.equals("Fibonacci")){             
            code = readCode(path + "\\fibonacci.janus");                
            jTextArea1.setText(code);
            jTextArea1.setCaretPosition(0);            
        } else if (selectedItem.equals("Factorization")){
            code = readCode(path + "\\factorization.janus");
            jTextArea1.setText(code);
            jTextArea1.setCaretPosition(0);
        } else if (selectedItem.equals("Square Root")){
            code = readCode(path + "\\squareroot.janus");
            jTextArea1.setText(code);
            jTextArea1.setCaretPosition(0);
        } else if (selectedItem.equals("Perm-to-code")){
            code = readCode(path + "\\permToCode.janus");
            jTextArea1.setText(code);
            jTextArea1.setCaretPosition(0);
        }
    }//GEN-LAST:event_jComboBox1ActionPerformed

    public String readCode(String path){
        BufferedReader input;
        String str = "";
        String code = "";
        try{
            input = new BufferedReader(new FileReader(path));
            while ((str = input.readLine()) != null){
                code += str + "\n";
            }
            input.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        return code;
    }
    
    public synchronized static String execCommand(final String commandLine) throws IOException {
        boolean success = false;
        String result;
        Process p;
        BufferedReader input;
        StringBuffer cmdOut = new StringBuffer();
        String lineOut = null;
        int numberOfOutline = 0;
        try {
            p = Runtime.getRuntime().exec(commandLine);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((lineOut = input.readLine()) != null) {
                if (numberOfOutline > 0) {
                    cmdOut.append("\n");
                }
                                if(!lineOut.equals("MARS 4.5  Copyright 2003-2014 Pete Sanderson and Kenneth Vollmar")){
                                    cmdOut.append(lineOut);
                                    numberOfOutline++;
                                }
            }
            result = cmdOut.toString();
            success = true;
            input.close();
        } catch (IOException e) {
            result = String.format("Falha ao executar comando %s. Erro: %s", commandLine, e.toString());
        }
        // Se não executou com sucesso, lança a falha
        if (!success) {
            throw new IOException(result);
        }
        return result;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaInicial().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextArea jTextArea3;
    // End of variables declaration//GEN-END:variables

}
