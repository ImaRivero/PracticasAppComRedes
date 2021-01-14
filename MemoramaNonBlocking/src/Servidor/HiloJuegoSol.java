/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import java.nio.channels.SocketChannel;

/**
 *
 * @author omari
 */
public class HiloJuegoSol implements Runnable{
    
    SocketChannel sc;
    
    public HiloJuegoSol(SocketChannel sc){
        this.sc = sc;
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
