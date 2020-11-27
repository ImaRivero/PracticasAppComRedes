package ecodatagrama;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class EcoDatagramaCliente {
    
    static String dst = "127.0.0.1";
    static int pto = 2000;
    
    public static void main(String[] args) {
        
        String msjRec = "";
        
        try {
            DatagramSocket cl = new DatagramSocket();
            System.out.println("Cliente iniciado, escriba un mensaje de saludo:");
            System.out.print("\t> ");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String mensaje = br.readLine();
            ArrayList<String> split = splitArray(mensaje, 20);
            
            /*Enviamos los datagramas al servidor*/
            for(String cad: split){
                byte[] b = cad.getBytes();
                DatagramPacket p = new DatagramPacket(b, b.length, InetAddress.getByName(dst), pto);
                cl.send(p);
            }
            
            /*Recibimos los datagramas del servidor*/
            for(;;){
                DatagramPacket res = new DatagramPacket(new byte[20],20);
                cl.receive(res);
                System.out.println("Datagrama recibido desde: " + res.getAddress()+":" + res.getPort());
                String msj = new String(res.getData(), 0, res.getLength());
                if(msj.equals("/end")){
                    break;
                }
                else
                    msjRec += msj;
            }
            System.out.println("Mensaje reconstruido del servidor:");
            System.out.println("\t> " + msjRec);
            //cl.close();
        } catch (Exception e) {
            e.printStackTrace();
        }//catch
    }//main
    
    /*Devuelve arraylist de strings con un max de caracteres del original para cada entrada*/
    public static ArrayList<String> splitArray(String original, int max){
        ArrayList<String> salida = new ArrayList();
        int num = original.length()/max;
        
        for(int i = 0; i < num+1; i++){
            if(original.substring((i)*max, original.length()).length() < max){
                salida.add(new String(original.substring(i*max, original.length())));
                break;
            }
            else{
                salida.add(new String(original.substring(i*max, (i+1)*max)));
            }
        }
        salida.add("/end");
        return salida;
    }
}
