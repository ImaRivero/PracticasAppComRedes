/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ecoserver;

/**
 *
 * @author imanol
 */
import java.net.*;
import java.io.*;

public class Cliente {
    
    public static void main(String[] args){
        try{
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Escriba la dirección del servidor: ");
            System.out.print("\t> ");
            String host = stdIn.readLine();
            System.out.println("Escriba el puerto:");
            System.out.print("\t> ");
            int pto = Integer.parseInt(stdIn.readLine());
            // Creamos el socket y nos conectamos
            Socket cl = new Socket(host,pto);
            System.out.println("Conectado con exito!");
            
            /* Solicitamos mensaje a enviar al servidor */
            System.out.println("Ingresa un mensaje.");
            System.out.print("\t> ");
            String mensaje = stdIn.readLine();
            PrintWriter out = new PrintWriter(new OutputStreamWriter(cl.getOutputStream()), true);
            out.println(mensaje);
            
            /* Creamos el buffer de entrada y lo leemos */
            BufferedReader in = new BufferedReader(new InputStreamReader(cl.getInputStream()));
            mensaje = in.readLine();
            System.out.println("Servidor:" );
            System.out.println("\t> " + mensaje);
            // Cerramos flujos y socket
            stdIn.close();
            in.close();
            out.close();
            cl.close();
        }catch(Exception e){ //Manejo de excepciones
            e.printStackTrace();
        }
    }
}