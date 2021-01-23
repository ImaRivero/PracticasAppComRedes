/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package supernodo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import model.Paquete;
import model.Sala;
import static net.Comunicacion.MULTICAST_IP;
import static net.Comunicacion.MULTICAST_PTO;

/**
 *
 * @author omari
 */
public class SuperNodo extends Thread{
    
    private ArrayList<Sala> list;
    private InetAddress grupo = null;
    private MulticastSocket ms;
    
    public static final String MULTICAST_IP = "230.1.1.1";
    public static final int MULTICAST_PTO = 4000;
    public static final int BUFFER_SIZE = 512;
    
    public SuperNodo() {
        this.list = new ArrayList<>();
    }
    
    public static void main(String[] args) {
        new SuperNodo().start();
    }
    
    @Override
    public void run(){
        conectar();
        try {
            grupo = InetAddress.getByName(MULTICAST_IP);
        } catch (UnknownHostException ex) {
            System.out.println("Error al inicializar grupo: " + ex.getMessage());
        }
        
        Object paquete = new Object();
        while(true){
            byte[] buffer = new byte[BUFFER_SIZE];
            DatagramPacket dp = new DatagramPacket(buffer, buffer.length, grupo, MULTICAST_PTO);
            try {
                ms.receive(dp);
                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(buffer));
                try {
                    paquete = ois.readObject();
                } catch (ClassNotFoundException ex) {
                    System.out.println("Error al leer objeto en SuperNodo");
                }
                if(paquete instanceof Paquete){
                    handlePaquete((Paquete) paquete);
                }
                else
                    System.out.println("No se que fue lo que recibi");
            } catch (IOException ex) {
                System.out.println("Error en receive: " + ex.getMessage());
            }
        }
    }
    
    private void handlePaquete(Paquete paquete) {
        if(paquete.getTipo() == 1){
            list.add((Sala)paquete.getContent());
            enviarPaquete(new Paquete(3, list, null));
        }
    }
    
    private void conectar(){
        try {
            grupo = InetAddress.getByName(MULTICAST_IP);
            ms = new MulticastSocket(MULTICAST_PTO);
            ms.joinGroup(grupo);
            System.out.println("Supernodo conectado!");
        } catch (UnknownHostException ex) {
            System.out.println("Error al resolver ip: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Error al conectar el supernodo: " + ex.getMessage());
        }
    }
    
    public void enviarPaquete(Paquete paquete){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(paquete);
            byte[] data = baos.toByteArray();
            ms.send(new DatagramPacket(data, data.length, grupo, MULTICAST_PTO));
        } catch (IOException ex) {
            System.out.println("Error al crear el flujo");
            ex.printStackTrace();
        }
    }
}
