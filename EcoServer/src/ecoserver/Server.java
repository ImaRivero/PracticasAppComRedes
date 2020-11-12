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

public class Server {
    public static void main(String[] args){
        try{
            // Se crea el socket
            ServerSocket s = new ServerSocket(4444);
            System.out.println("Esperando cliente ...");
            // Iniciamos el ciclo infinito 
            for(;;){
                // Tenemos un bloqueo, en el momento que llegue una conexión continua el programa
                Socket cl = s.accept();
                System.out.println("Conexión establecida desde "+  cl.getInetAddress()+":" + cl.getPort());
                /* Recibimos mensaje del cliente */
                BufferedReader in = new BufferedReader(new InputStreamReader(cl.getInputStream()));
                String mensaje = in.readLine();
                System.out.println("Cliente: " + cl.getInetAddress());
                System.out.print("\t> " + mensaje + "\n");
                /* Hacemos eco */
                PrintWriter out = new PrintWriter(new OutputStreamWriter(cl.getOutputStream()));
                out.println(mensaje);
                // Se limpia el flujo
                out.flush();
                out.close();
                in.close();
                cl.close();
            }//for
        }catch(Exception e){ // Manejo de excepciones
            e.printStackTrace();
        }//catch
    }//main
}
