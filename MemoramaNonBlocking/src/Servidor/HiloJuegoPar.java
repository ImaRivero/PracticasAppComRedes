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
public class HiloJuegoPar implements Runnable{
    
    SocketChannel sc;
    SharedPar sp;
    
    public HiloJuegoPar(SocketChannel sc, SharedPar sp){
        this.sc = sc;
        this.sp = sp;
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
