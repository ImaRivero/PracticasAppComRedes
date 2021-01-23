/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.File;
import javax.swing.JOptionPane;
import model.Paquete;
import model.Usuario;
import net.Comunicacion;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;
import view.VisorTransChat;

/**
 *
 * @author omari
 */
public class CtrlVisorTrans extends Thread{
    
    VisorTransChat visorTrans;
    boolean bool_trans; // Transmisor(true) o receptor(false)
    Comunicacion comun;
    String serverAddress;
    int serverPort;
    File file;
    Controlador control;
    String nom_user;
    
    public CtrlVisorTrans(boolean bool_trans, Comunicacion comun,
            Controlador control, String nom_user, File file, String serverAddress, int serverPort) {
        this.bool_trans = bool_trans;
        this.comun = comun;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.file = file;
        this.control = control;
        this.nom_user = nom_user;
        initCtrlVisor();
    }
    
    private void initCtrlVisor(){
        visorTrans = new VisorTransChat();
        visorTrans.getBtnLeaveSala().addActionListener(e -> leaveRoom());
        visorTrans.getBtnEnviar().addActionListener(e -> enviarMensaje());
        visorTrans.getBtnCrear().addActionListener(e -> crearPrivChat());
        visorTrans.getBtnUnirse().addActionListener(e -> unirsePrivChat());
        visorTrans.setTitle("Visor de Transmición - MoviePeers - Usuario: " + nom_user);
        EmbeddedMediaPlayerComponent mediaPlayer = new EmbeddedMediaPlayerComponent();
        visorTrans.getVideoFrame().setContentPane(mediaPlayer);
        mediaPlayer.mediaPlayer().submit(() -> {
            if(bool_trans)
                mediaPlayer.mediaPlayer().media().play(file.getAbsolutePath(), formatRtpStream(serverAddress, serverPort));
            else
                mediaPlayer.mediaPlayer().media().play(formatRtpReceiver(serverAddress, serverPort));
        });
        visorTrans.setVisible(true); 
    }
    
    private void leaveRoom() {
        // Avisar a controlador de arriba;
        //Thread.currentThread().interrupt();
        control.leaveRoom();
    }

    private void enviarMensaje() {
        String linea = "<" + nom_user+ "> " + visorTrans.getTxtMsgEntry().getText();
        visorTrans.getTxtMsgEntry().setText("");
        Usuario user = new Usuario(nom_user, null, 5222);
        Paquete paquete = new Paquete(2, linea, user);
        comun.enviarPaquete(paquete);
    }

    private void crearPrivChat() {
        JOptionPane.showMessageDialog(visorTrans, "Operación no implementada", "Alerta", 0);
    }

    private void unirsePrivChat() {
        JOptionPane.showMessageDialog(visorTrans, "Operación no implementada", "Alerta", 0);
    }
    
    public String formatRtpStream(String serverAddress, int serverPort){
        StringBuilder sb = new StringBuilder(60);
        sb.append(":sout=#duplicate{dst=display,dst=rtp{dst=");
        sb.append(serverAddress);
        sb.append(",port=");
        sb.append(serverPort);
        sb.append(",mux=ts}}");
        return sb.toString();
    }
    
    public String formatRtpReceiver(String serverAddress, int serverPort){
        StringBuilder sb = new StringBuilder(60);
        sb.append("rtp://@");
        sb.append(serverAddress);
        sb.append(":");
        sb.append(serverPort);
        return sb.toString();
    }

    public VisorTransChat getVisorTrans() {
        return visorTrans;
    }
    
    
}
