/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gatomultihilolocal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.locks.Lock;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author imanol
 */
public class HiloGato extends Thread implements ActionListener{
    
    private final int idPartida;
    private final int idPlayer;
    private final Lock lock;
    private boolean gameover;
    private final Shared sh;
    private Tablero tab;
    BotonGato[] array_bg;
    private final ImageIcon icop1, icop2;
    private BotonGato btn;
    
    public HiloGato(int idPartida, int idPlayer, Shared sh){
        this.idPartida = idPartida;
        this.idPlayer = idPlayer;
        this.sh = sh;
        icop1 = new ImageIcon("./src/img/circle.png");
        icop2 = new ImageIcon("./src/img/cross.png");
        lock = sh.getLock();
        gameover = false;
    }
    
    @Override
    public void run(){
        lock.lock();
        int win;
        
        try {
            tab = new Tablero(idPartida, idPlayer, this);
            while(!gameover){
                sh.esperaLectura();
                btn = sh.getSh_button();
                
                array_bg = tab.getBotones();
                array_bg[Integer.parseInt(btn.getName())].setCheck(btn.isCheck());
                array_bg[Integer.parseInt(btn.getName())].setOwner(btn.getOwner());
                
                if(btn.getOwner() == 1)
                    array_bg[Integer.parseInt(btn.getName())].setIcon(icop1);
                else
                    array_bg[Integer.parseInt(btn.getName())].setIcon(icop2);
                
                
                win = ganador(array_bg);
                if(win != 0){
                    if(win == idPlayer)
                        JOptionPane.showMessageDialog(null, "Felicidades has ganado player: " + idPlayer);
                    else
                        JOptionPane.showMessageDialog(null, "Mejor suerte la proxima. Ha ganado: " + win);
                    gameover = true;
                    tab.setVisible(false);
                }
                
                if(empate(array_bg)){
                    JOptionPane.showMessageDialog(null, "EMPATE. Fin del juego c:");
                    gameover = true;
                    tab.setVisible(false);
                }
                
                /*
                if(btn.getOwner() == idPlayer)
                    sh.alternaTurno(array_bg);
                */
                
            }
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent arg) {
        BotonGato bg = (BotonGato)arg.getSource();
        array_bg = tab.getBotones();
        
        if(!bg.isCheck()){
            if (idPlayer == 1) {
                bg.setIcon(icop1);
            } else {
                bg.setIcon(icop2);
            }
            bg.setCheck(true);
            bg.setOwner(idPlayer);
            //sh.avisaTurno();
            sh.setSh_button(bg);
        }
        else
            JOptionPane.showMessageDialog(null, "Casilla ocupada");
    }
    
    public void bloquearBotones(BotonGato[] lista){
        for(int i = 0; i < 9; i++){
            lista[i].setEnabled(false);
        }
    }
    
    public void activaBotones(BotonGato[] lista){
        for(int i = 0; i < 9; i++){
            lista[i].setEnabled(true);
        }
    }
    
    public int ganador(BotonGato[] lista){
        /* Horizontales */
        if(lista[0].getOwner() == lista[1].getOwner() && lista[0].getOwner() == lista[2].getOwner())
            return lista[0].getOwner();
        if(lista[3].getOwner() == lista[4].getOwner() && lista[3].getOwner() == lista[5].getOwner())
            return lista[3].getOwner();
        if(lista[6].getOwner() == lista[7].getOwner() && lista[6].getOwner() == lista[8].getOwner())
            return lista[6].getOwner();
        /* Verticales */
        if(lista[0].getOwner() == lista[3].getOwner() && lista[0].getOwner() == lista[6].getOwner())
            return lista[0].getOwner();
        if(lista[1].getOwner() == lista[4].getOwner() && lista[1].getOwner() == lista[7].getOwner())
            return lista[1].getOwner();
        if(lista[2].getOwner() == lista[5].getOwner() && lista[2].getOwner() == lista[8].getOwner())
            return lista[2].getOwner();
        /* Diagonales */
        if(lista[0].getOwner() == lista[4].getOwner() && lista[0].getOwner() == lista[8].getOwner())
            return lista[0].getOwner();
        if(lista[2].getOwner() == lista[4].getOwner() && lista[2].getOwner() == lista[6].getOwner())
            return lista[2].getOwner();
        return 0;
    }
    
    public boolean empate(BotonGato[] lista){
        for(BotonGato bg : lista){
            if(!bg.isCheck())
                return false;
        }
        return true;
    }
}
