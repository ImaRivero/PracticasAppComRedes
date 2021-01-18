/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.util.List;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import shared.Constantes;

/**
 *
 * @author imanol
 */
public class Server extends Thread{
    
    private final List<String> lista_usuarios; 
    private String msj_out;
    private InetAddress grupo = null;
    private MulticastSocket socket;

    public Server() {
        this.lista_usuarios = new ArrayList<>();
    }
    
    public static void main(String[] args) {
        new Server().start();
    }
    
    @Override
    public void run() {
        try {
            grupo = InetAddress.getByName(Constantes.MULTICAST_IP);
        } catch (UnknownHostException ex) {
            System.out.println("Error al inicializar grupo: " + ex.getMessage());
        }
        
        System.out.println("Server en linea!");
        
        for(;;){
            byte[] buffer = new byte[Constantes.BUFFER_SIZE];
            try {
                socket = new MulticastSocket(Constantes.MULTICAST_PTO);
                socket.joinGroup(grupo);
                DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
                socket.receive(dp);
                String entrada = new String(dp.getData());
                System.out.println(entrada);
                if(entrada.contains("<inicio>"))
                    agregarUser(entrada);
                if(entrada.contains("fin"))
                    borrarUser(entrada);
                socket.close();
            } catch (IOException ex) {
                System.out.println("Error en ciclo del run: " + ex.getMessage());
            }
            
        }
    }

    private void agregarUser(String entrada) {
        String[] data = entrada.split(">");
        lista_usuarios.add(data[1].trim());
        updateLista();
        sendMsjOut();
    }

    private void borrarUser(String entrada) {
        String[] data = entrada.split(">");
        lista_usuarios.remove(data[1].trim());
        updateLista();
        sendMsjOut();
    }

    private void updateLista() {
        msj_out = "<lista>";
        for(int i = 0; i < lista_usuarios.size(); i++){
            msj_out += lista_usuarios.get(i) + ",";
        }
        msj_out = msj_out.replace(" ", "");
    }
    
    private void sendMsjOut() {
        DatagramPacket dp = new DatagramPacket(
                msj_out.getBytes(), msj_out.length(), grupo, Constantes.MULTICAST_PTO);
        try {
            socket.send(dp);
            System.out.println("Enviado: " + msj_out + "\tTTL= " + socket.getTimeToLive());
        } catch (IOException ex) {
            System.out.println("Error en sendMsj: " + ex.getMessage());
        }
    }
}
