/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatmulticastnb;

import java.net.InetAddress;
import java.util.EventObject;

/**
 *
 * @author omari
 */
public class NewMsjEvent extends EventObject{
    
    private int puerto;
    private InetAddress sender;
    private String texto;
    boolean privado;

    public NewMsjEvent(Object source, int puerto, InetAddress sender, String texto) {
        super(source);
        this.puerto = puerto;
        this.sender = sender;
        this.texto = texto;
    }

    public int getPuerto() {
        return puerto;
    }

    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }

    public InetAddress getSender() {
        return sender;
    }

    public void setSender(InetAddress sender) {
        this.sender = sender;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public boolean isPrivado() {
        return privado;
    }

    public void setPrivado(boolean privado) {
        this.privado = privado; 
    }
    
}
