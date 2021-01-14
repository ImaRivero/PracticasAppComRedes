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
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CArchTCPB {
    
    static String host;
    static int pto;
    static int buffer_size;
    static boolean nagle;

    public static void main(String[] args) {
        new CArchTCPB().MyJOptionPane();
        
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            /*
            System.out.printf("Escriba la dirección del servidor:");
            String host = br.readLine();
            
            System.out.printf("Escriba el puerto:");
            int pto = Integer.parseInt(br.readLine());
            */
            Socket cl = new Socket(host, pto);
            if(nagle)
                cl.setTcpNoDelay(nagle);
            DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
            DataInputStream dis = new DataInputStream(cl.getInputStream());
            /*
            System.out.printf("Indique el tamaño del buffer:");
            int buffer_size = Integer.parseInt(br.readLine());
            */
            dos.writeInt(buffer_size); // enviamos tamaño del buffer
            dos.writeBoolean(nagle); // enviamos nagle
            
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
    
    public void MyJOptionPane(){
        JTextField host_field = new JTextField(15);
        JTextField pto_field = new JTextField(5);
        JTextField buff_field = new JTextField(8);
        JCheckBox nagle_box = new JCheckBox("Usar Nagle");
        JPanel panel = new JPanel();
        panel.add(new JLabel("Direccion IP:"));
        panel.add(host_field);
        panel.add(Box.createHorizontalStrut(15));
        panel.add(new JLabel("Puerto:"));
        panel.add(pto_field);
        panel.add(Box.createHorizontalStrut(15));
        panel.add(new JLabel("Tamaño de Buffer:"));
        panel.add(Box.createHorizontalStrut(15));
        panel.add(buff_field);
        panel.add(Box.createHorizontalStrut(15));
        panel.add(nagle_box);
        
        int res = JOptionPane.showConfirmDialog(
                null, panel, "Indica la informacion", JOptionPane.OK_CANCEL_OPTION);
        if(res == JOptionPane.OK_OPTION){
            host = host_field.getText();
            pto = Integer.parseInt(pto_field.getText());
            buffer_size = Integer.parseInt(buff_field.getText());
            nagle = nagle_box.isSelected();
        }
    }
}
