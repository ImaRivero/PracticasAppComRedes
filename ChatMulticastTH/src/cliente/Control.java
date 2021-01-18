/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import shared.Constantes;

/**
 *
 * @author imanol
 */
public class Control{
    
    private final Modelo mod;
    private final Vista view;
    private InetAddress grupo = null;
    private MulticastSocket ms = null;
    private  Receptor rec;
    private  Thread rec_t;
    private String linea ="";
    
    public Control(Modelo mod, Vista view) {
        this.mod = mod;
        this.view = view;
        view.getChatEntry().setText(linea);
    }

    void initControl() {
        view.getBtn_conectar().addActionListener(e -> conectar());
        view.getBtn_dsct().addActionListener(e -> desconectar());
        view.getBtn_snd().addActionListener(e -> sendMsgBtn());
        mod.setNom_user(view.getNom_user());
    }
    
    private void conectar(){
        try {
            grupo = InetAddress.getByName(Constantes.MULTICAST_IP);
            ms = new MulticastSocket(Constantes.MULTICAST_PTO);
            ms.joinGroup(grupo);
            System.out.println("Cliente conectado");
            mod.setConectado(true);
            enviarMensaje("<inicio>" + mod.getNom_user());
            rec = new Receptor(mod, this, ms);
            rec_t = new Thread(rec);
            rec_t.start();
        } catch (UnknownHostException ex) {
            System.out.println("Error al resolver ip: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Error al conectar cliente: " + ex.getMessage());
        }
    }
    
    private void desconectar(){
        if(!mod.isConectado())
            JOptionPane.showMessageDialog(null, "El cliente no esta conectado", "Alerta", 0);
        else{
            try {
                enviarMensaje("<fin>" + mod.getNom_user());
                linea += "<b>Has salido de la sala de chat!</b><br>";
                view.updateChatEntry(linea);
                view.getLst_users().removeAll();
                mod.setConectado(false);
                rec.setMod(mod);
                rec_t.interrupt();
                ms.leaveGroup(grupo);
            } catch (IOException ex) {
                System.out.println("Error al dejar el grupo: " + ex.getMessage());
            }
        } 
    }
    
    private void sendMsgBtn(){
        
        String msg = view.getMsjEntry().getText();
        if(msg.contains("<privado>")){
            String dest = msg.split(">",3)[1].replace("<", "");
            msg = msg.replace("<privado>", "");
            msg = "<privado><" + mod.getNom_user() + "><" + dest + ">" + msg;
        }else{
            msg = "<msg><"+ mod.getNom_user() + ">"+msg;
        }
        enviarMensaje(msg);
        view.getMsjEntry().setText("");
    }
    
    public void handleMsg(String msg){
        String dest;
        String remit;
        //String chat = view.getChatEntry().getText();
        
        
        System.out.println(msg);

        //Mensaje simple
        if(msg.contains("<msg>")){
            //sender = msg.split("<")[2].split(">")[0];
            remit = msg.split(">", 3)[1].replace("<", "").trim();
            msg = cleanMsg(msg);
            msg = replaceEmojis(msg);
            if(remit.equals(mod.getNom_user()))
                linea += "<b>Tu:</b><br>" + msg + "<br>";
            else
                linea += "<b>" + remit + " dice:</b><br>" + msg + "<br>";
            //view.getChatEntry().setText(linea);
            view.updateChatEntry(linea);
        //Mensaje de entrada
        }else if(msg.contains("<inicio>")){
            remit = msg.split(">", 2)[1].replace("<", "").trim();
            if(remit.equalsIgnoreCase(mod.getNom_user()))
                linea += "<b>Has entrado a la sala de chat!</b><br>";
            else
                linea += "<b>El usuario " + remit + " ha entrado a la sala</b><br>";
            //view.getChatEntry().setText(linea);
            view.updateChatEntry(linea);
        // Mensaje de salida
        }else if(msg.contains("<fin>")){
            remit = msg.split(">", 2)[1].replace("<", "").trim();
            if(remit.equals(mod.getNom_user()))
                linea += "<b>Has salido de la sala de chat!</b><br>";
            else
                linea += "<b>El usuario " + remit + " ha salido de la sala</b><br>";
            //view.getChatEntry().setText(linea);
            view.updateChatEntry(linea);
        // Mensaje privado
        }else if(msg.contains("<privado>")){
            remit = msg.split(">", 3)[1].replace("<", "").trim();
            dest = msg.split(">", 3)[2].replace("<", "").trim();
            msg = cleanMsg(msg);
            msg = replaceEmojis(msg);
            if(remit.equals(mod.getNom_user()))
                linea += "<b>Mensaje para " + dest + ":</b><br>" + msg + "<br>";
            else if(dest.equals(mod.getNom_user()))
                linea += "<b>Has recibido un mensaje privado de " + remit + "</b><br>" + msg + "<br>";
            //view.getChatEntry().setText(linea);
            view.updateChatEntry(linea);
        }
        // Mensaje de actualizacion de lista de usuarios
        else if(msg.contains("<lista>"))
            updateUsersList(msg);
        System.out.println(linea);
    }

    private void updateUsersList(String msg){
        DefaultListModel<String> list_model = new DefaultListModel<>();
        msg = cleanMsg(msg);
        String[] usuarios = msg.split(",");
        for(String user: usuarios){
            if(!(user.isEmpty() || user.isBlank() || user.length()==0))
                list_model.addElement(user);
        }
        view.updateUsersList(list_model);
    }

    public void enviarMensaje(String mensaje){
        if(!mod.isConectado())
            JOptionPane.showMessageDialog(null, "El cliente no esta conectado", "Alerta", 0);
        else{
            DatagramPacket dp = new DatagramPacket(mensaje.getBytes(), mensaje.length(), grupo, Constantes.MULTICAST_PTO);
            try {
                ms.send(dp);
                System.out.println("Enviando mensaje: " + mensaje + "\tCon TTL=" + ms.getTimeToLive());
            } catch (IOException ex) {
                System.out.println("Error al enviar mensaje: " + ex.getMessage());
            }
        }
    }
   
    
    private String cleanMsg(String msg){
        int index = msg.lastIndexOf(">");
        return msg.substring(index+1);
    }
    
    private String replaceEmojis(String msg){
        msg = msg.replaceAll(":\\)", "<img width=\"80\" height=\"50\" src=\"file:./imgs/risa.png\"></img>");
        msg = msg.replaceAll(":o", "<img width=\"80\" height=\"50\" src=\"file:./imgs/amor.png\"></img>");
        msg = msg.replaceAll(":S", "<img width=\"80\" height=\"50\" src=\"file:./imgs/enojo.png\"></img>");
        msg = msg.replaceAll("_loco_", "<img width=\"80\" height=\"50\" src=\"file:./imgs/loco.gif\"></img>");
        msg = msg.replaceAll("_Homero_", "<img width=\"80\" height=\"50\" src=\"file:./imgs/homero.gif\"></img>");
        return msg;
    }
}
