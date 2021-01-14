/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared;

import java.net.InetAddress;

/**
 *
 * @author omari
 */
public class Mensaje {
    InetAddress sender;
    String texto;

    public Mensaje(InetAddress sender, String texto) {
        this.sender = sender;
        this.texto = texto;
    }

    public InetAddress getSender() {
        return sender;
    }

    public String getTexto() {
        return texto;
    }
}
