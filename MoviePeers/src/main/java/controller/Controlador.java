/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import model.Paquete;
import model.Sala;
import model.Usuario;
import net.Comunicacion;
import view.Cartelera;
import view.CrearSala;
import view.MovieCard;
import view.VisorTransChat;


/**
 *
 * @author omari
 */
public class Controlador {

    String nom_user;
    
    Cartelera cartelera;
    CrearSala crearSala;
    VisorTransChat visorTrans;
    Comunicacion comun;
    Sala newSala;
    CtrlVisorTrans ctrlVisor;
    String chat = "";
    
    public void initControlador(){
        
        solicitarNombre();
        iniciarVistas();
        
        comun = new Comunicacion(this);
        
        cartelera.getBtnCrearTrans().addActionListener(e -> crearTransmicion());
        cartelera.getBtnUnirsePrivado().addActionListener(e -> unirsePrivado());
        
        crearSala.getBtnTransmicion().addActionListener(e -> iniciarTransmicion());
        crearSala.getBtnCancel().addActionListener(e -> cancelCrearTrans());
        
        visorTrans.getChatEntry().setText(chat);
    }
    
    private void iniciarVistas(){
        cartelera = new Cartelera();
        cartelera.setTitle("Cartelera - MoviePeers - Usuario: " + nom_user);
        crearSala = new CrearSala();
        crearSala.setTitle("Crear Sala - MoviePeers - Usuario: " + nom_user);
        
        cartelera.setVisible(true); 
    }
    
    private void solicitarNombre(){
        nom_user = JOptionPane.showInputDialog("Ingresa un nombre de usuario");
        if(nom_user == null)
            System.exit(1);
    }
    
    
    // Operaciones de Cartelera

    private void crearTransmicion() {
        cartelera.setVisible(false);
        crearSala.setVisible(true);
    }

    private void unirsePrivado() {
        JOptionPane.showMessageDialog(visorTrans, "Operación no implementada", "Alerta", 0);
    }
    
    private void unirseAPublica(Sala s){
        cartelera.setVisible(false);
        ctrlVisor = new CtrlVisorTrans(false, comun, this, nom_user, null, "230.0.0.1", 2000);
        ctrlVisor.getVisorTrans().getLabelNombrePel().setText(newSala.getNombre());
        ctrlVisor.getVisorTrans().getLabelDescrip().setText(newSala.getDescripcion());
    }
    
    // Operaciones de CrearSala

    private void iniciarTransmicion() {
        if(crearSala.validarInfo()){
            cartelera.setVisible(false); 
            newSala = crearSala.generarSala();
            Usuario user = new Usuario(nom_user, Comunicacion.MULTICAST_IP, Comunicacion.MULTICAST_PTO);
            comun.enviarPaquete(new Paquete(1, newSala, user));
            ctrlVisor = new CtrlVisorTrans(true, comun, this, nom_user, crearSala.getFichero(), "230.0.0.1", 2000);
            ctrlVisor.getVisorTrans().getLabelNombrePel().setText(newSala.getNombre());
            ctrlVisor.getVisorTrans().getLabelDescrip().setText(newSala.getDescripcion());
        }
    }
    
    private void cancelCrearTrans() {
        crearSala.setVisible(false);
        cartelera.setVisible(true);
    }
     
    // Operaciones de Visor de Transmición
    
    public void leaveRoom() {
        ctrlVisor.getVisorTrans().setVisible(false);
        ctrlVisor.getVisorTrans().dispatchEvent(new WindowEvent(ctrlVisor.getVisorTrans(), WindowEvent.WINDOW_CLOSING));
        cartelera.setVisible(true);
        chat = "";
    }
    
    public void recibirMsg(String linea){
        chat +=  linea + "<br>";
        ctrlVisor.getVisorTrans().getChatEntry().setText(chat); 
    }
    
    public void recibirSalas(ArrayList<Sala> salas){
        for(Sala s: salas){
            MovieCard card = new MovieCard(s.movieCardFormat(), s);
            cartelera.getMovieCardPanel().add(card);
            card.getBtnUnirse().addActionListener(e -> unirseAPublica(s));
        }
        cartelera.getMovieCardPanel().repaint();
        cartelera.getMovieCardPanel().revalidate();
        cartelera.repaint();
        cartelera.revalidate();
    }
}
