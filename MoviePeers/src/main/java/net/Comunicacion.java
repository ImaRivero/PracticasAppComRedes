/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net;

import controller.Controlador;
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
import model.Usuario;

/**
 *
 * @author omari
 */
public class Comunicacion extends Thread{
    
    public static final String MULTICAST_IP = "230.1.1.1";
    public static final int MULTICAST_PTO = 4000;
    public static final int BUFFER_SIZE = 512;
    
    InetAddress grupo = null;
    MulticastSocket ms = null;
    Controlador control;
    
    public Comunicacion(Controlador control) {
        this.control = control;
        conectar();
    }
    
    private void conectar(){
        try {
            grupo = InetAddress.getByName(MULTICAST_IP);
            ms = new MulticastSocket(MULTICAST_PTO);
            ms.joinGroup(grupo);
            System.out.println("Cliente conectado");
        } catch (UnknownHostException ex) {
            System.out.println("Error al resolver ip: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Error al conectar cliente: " + ex.getMessage());
        }
        this.start();
    }
    
    @Override
    public void run(){
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
                    System.out.println("Error al leer objeto en Comunicacion");
                }
                if(paquete instanceof Paquete){
                    handlePaquete((Paquete) paquete);
                }
                else
                    System.out.println("No se que fue lo que recibi");
            } catch (IOException ex) {
                System.out.println("Error en receive: " + ex.getMessage());
                ex.printStackTrace();
            }
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

    private void handlePaquete(Paquete paquete) {
        switch(paquete.getTipo()){
            case Paquete.NUEVA_TRANSMICION -> {
                // Nueva transmicion
                //Sala sala = (Sala)paquete.getContent();
                //control.recibirSala(sala);
            }
            case Paquete.MENSAJE -> {
                // Mensaje
                String msg = (String)paquete.getContent();
                control.recibirMsg(msg);
            }
            case Paquete.LISTA_TRANSMICION -> {
                // Lista de salas
                control.recibirSalas((ArrayList<Sala>)paquete.getContent());
            }
        }
    }
}
