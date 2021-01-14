/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

/**
 *
 * @author omari
 */

import java.net.*;
import java.io.*;

public class SArchTCPB {
    public static void main(String[] args){
        try{
            // Creamos el socket
            ServerSocket s = new ServerSocket(7000);
            System.out.println("OK");
            File f = new File("./ArchivosRecibidos");
            if(f.mkdir())
                System.out.println("Directorio creado!");
            
            // Iniciamos el ciclo infinito del servidor
            for(;;){
               // Esperamos una conexión 
                Socket cl = s.accept();
                System.out.println("Conexión establecida desde"+cl.getInetAddress()+":"+cl.getPort());
                DataInputStream dis = new DataInputStream(cl.getInputStream());
                DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
                
                int buffer_size = dis.readInt();
                System.out.println("Tamaño del buffer " + buffer_size);
                boolean nagle = dis.readBoolean();
                System.out.println("Usar nagle? " + nagle);
                if(nagle)
                    cl.setTcpNoDelay(nagle);
                int file_num = dis.readInt();
                System.out.println("Numero de archivos " + file_num);
                System.out.println("----------------------------------------------");
                
                byte[] b = new byte[buffer_size];
                
                for(int i = 0; i < file_num; i++){
                    String nombre = dis.readUTF();
                    long tam = dis.readLong();
                    System.out.println("Recibiremos: " + nombre + " con " + tam + " bytes");
                    
                    nombre = "./ArchivosRecibidos/" + nombre;
                    DataOutputStream dos_file = new DataOutputStream(new FileOutputStream(nombre));
                    long recibidos = 0;
                    int n, porcentaje;
                    while (recibidos < tam) {
                        n = dis.read(b);
                        dos_file.write(b, 0, n);
                        dos_file.flush();
                        recibidos = recibidos + n;
                        porcentaje = (int) (recibidos * 100 / tam);
                        System.out.println("Recibido: " + porcentaje + "%\r");
                    }//While
                    System.out.println("\nArchivo recibido.\n");
                    dos.writeBoolean(true);
                    dos_file.close();
                    System.out.println("----------------------------------------------");
                }
           
                dis.close();
                cl.close();
            }
        }catch(Exception e){
                e.printStackTrace();
        }//catch
    }
}
