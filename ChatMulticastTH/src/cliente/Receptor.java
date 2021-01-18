/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.Constantes;

/**
 *
 * @author imanol
 */
public class Receptor implements Runnable{
   
    private MulticastSocket ms;
    private Modelo mod;
    private Control c;

    public Receptor(Modelo mod, Control c, MulticastSocket ms) {
        this.mod = mod;
        this.c = c;
        this.ms = ms;
    }

    public void setMod(Modelo mod) {
        this.mod = mod;
    }
    
    @Override
    public void run() {
        while(mod.isConectado()){
            byte[] buffer = new byte[Constantes.BUFFER_SIZE];
            DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
            try {
                ms.receive(dp);
                byte[] data = dp.getData();
                String entrada = new String(data);
                c.handleMsg(entrada);
            } catch (IOException ex) {
                System.out.println("Error en receive: " + ex.getMessage());
            }
        }
    }
}
