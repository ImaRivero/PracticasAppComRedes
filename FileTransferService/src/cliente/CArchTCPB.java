/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

/**
 *
 * @author omari
 */
import javax.swing.JFileChooser;
import java.net.*;
import java.io.*;

public class CArchTCPB {

    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            
            System.out.printf("Escriba la dirección del servidor:");
            String host = br.readLine();
            
            System.out.printf("Escriba el puerto:");
            int pto = Integer.parseInt(br.readLine());
            
            Socket cl = new Socket(host, pto);
            DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
            DataInputStream dis = new DataInputStream(cl.getInputStream());
            
            System.out.printf("Indique el tamaño del buffer:");
            int buffer_size = Integer.parseInt(br.readLine());
           
            dos.writeInt(buffer_size); // enviamos tamaño del buffer
            
            JFileChooser jf = new JFileChooser();
            jf.setMultiSelectionEnabled(true);
            int r = jf.showOpenDialog(null);
            
            if (r == JFileChooser.APPROVE_OPTION) {
                File[] file_array = jf.getSelectedFiles();  //Manejador
                dos.writeInt(file_array.length); // enviamos numero de archivos
                System.out.println("---------------------------------------------");
                for(File f: file_array){
                    String archivo = f.getAbsolutePath(); //Dirección
                    String nombre = f.getName(); //Nombre
                    long tam = f.length();  //Tamaño
                    System.out.println("Enviando: " + f.getName());
                    DataInputStream dis_file = new DataInputStream(new FileInputStream(archivo));
                    dos.writeUTF(nombre);
                    dos.flush();
                    dos.writeLong(tam);
                    dos.flush();
                    byte[] b = new byte[buffer_size];
                    long enviados = 0;
                    int porcentaje, n;
                    while (enviados < tam) {
                        n = dis_file.read(b);
                        dos.write(b, 0, n);
                        dos.flush();
                        enviados = enviados + n;
                        porcentaje = (int) (enviados * 100 / tam);
                        System.out.println("Enviado: " + porcentaje + "%\r");
                    }//While
                    System.out.println("\n\nArchivo enviado");
                    System.out.println("-------------------------------------------");
                    dis_file.close();
                    dis.readBoolean();
                }
                dos.close();
                cl.close();
            }//if
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
