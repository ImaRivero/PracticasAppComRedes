package ecodatagrama;

import java.net.*;
import java.util.ArrayList;

public class EcoDatagramaServer {
    public static void main(String[] args){
        try{
            DatagramSocket s = new DatagramSocket(2000);
            System.out.println("Servidor iniciado, esperando cliente");
            String mensajeRec = "";
            
            for(;;){
                DatagramPacket p = new DatagramPacket(new byte[20],20);
                s.receive(p);
                System.out.println("Datagrama recibido desde"+p.getAddress()+":"+p.getPort());
                String msj = new String(p.getData(),0,p.getLength());
                
                if(msj.equals("/end")){
                    System.out.println("Mensaje reconstruido del cliente:");
                    System.out.println("\t> " + mensajeRec);
                    ArrayList<String> split = splitArray(mensajeRec, 20);
                    mensajeRec = "";
                    for (String cad : split) {
                        byte b[] = cad.getBytes();
                        DatagramPacket respuesta = new DatagramPacket(b, b.length, p.getAddress(), p.getPort());
                        s.send(respuesta);
                    }
                    //s.close();
                }
                else
                    mensajeRec += msj;
            }//for
            
            
        }catch(Exception e){
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

