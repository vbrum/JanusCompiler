/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teste;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 *
 * @author VBrum
 */
public class Prompt {
    public static void main(String args[]) throws IOException, InterruptedException{
        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "cd Documents");
        BufferedReader input;
        
        Process p = builder.start();
        System.out.println(output(p.getInputStream()));
        /*String lineOut;
        try{
            input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            
            while ((lineOut = input.readLine()) != null) {
                System.out.println(lineOut);
            }
            input.close();
        }catch(IOException e){
            e.printStackTrace();
        }*/
    }
    
    private static String output(InputStream inputStream) throws IOException {

	        StringBuilder sb = new StringBuilder();

	        BufferedReader br = null;

	        try {

	            br = new BufferedReader(new InputStreamReader(inputStream));

	            String line = null;

	            while ((line = br.readLine()) != null) {

	                sb.append(line + System.getProperty("line.separator"));

	            }

	        } finally {

	            br.close();

	        }

	        return sb.toString();

	    }
}
