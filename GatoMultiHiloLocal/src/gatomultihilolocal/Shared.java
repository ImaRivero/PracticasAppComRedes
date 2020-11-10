/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gatomultihilolocal;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author imanol
 */
public class Shared {
    
    private final Lock lock;
    private Condition condT, condLect;
    private volatile BotonGato sh_button;
    private volatile boolean available;
    private volatile boolean condTurno;
    private HiloGato hg;
    private int turno;
    boolean esperando;
    
    public Shared(){
        lock = new ReentrantLock();
        condLect = lock.newCondition();
        condT = lock.newCondition();
        available = false;
        condTurno = false;
        esperando = false;
        hg = new HiloGato(0, 0, this);
        sh_button = new BotonGato();
    }
    
    public void esperaLectura(){
        lock.lock();
        try {
            try {
                condLect.await();
            } catch (InterruptedException ex) {
                Logger.getLogger(Shared.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        finally{
            lock.unlock();
        }
    }

    public BotonGato getSh_button() {
        lock.lock();
        try {
            available = false;/*
            try {
                condLect.awaitNanos(500000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Shared.class.getName()).log(Level.SEVERE, null, ex);
            }*/
        
        } finally {
            lock.unlock();
        }
        System.out.println("Get " + sh_button.getName());
        return sh_button;
    }

    public void setSh_button(BotonGato sh_button) {
        lock.lock();
        try{
            while (available) {
                try {
                    condLect.await();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Shared.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            available = true;
            condTurno = true;
            condLect.signalAll();
        }
        finally{
            System.out.println("Thread: " + Thread.currentThread().getId());
            System.out.println("ID Boton: " + sh_button.getName());
            System.out.println("Owner: " + sh_button.getOwner());
            this.sh_button = sh_button;
            lock.unlock();
        }
    }

    public void alternaTurno(BotonGato[] bg){
        lock.lock();
        //condTurno = true;
        esperando = true;
        try {
            try {
                while(condTurno){
                    //condT.awaitNanos(5000);
                    hg.bloquearBotones(bg);
                    condT.await();
                    hg.activaBotones(bg);
                    //condT.awaitNanos(50000);
                    condTurno = false;
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Shared.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        finally {
            lock.unlock();
        }
    }
    
    public void avisaTurno(){
        lock.lock();
        try {
            if(esperando){
                //condT.awaitNanos(5000);
                /*
                try {
                    condT.awaitNanos(50);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Shared.class.getName()).log(Level.SEVERE, null, ex);
                }*/
                condT.signal();
                condTurno = false;
            }
            condTurno = true;
            //esperando = true;
        } finally {
            lock.unlock();
        }
    }
    
    public int getTurno() {
        return turno;
    }

    public void setTurno(int turno) {
        this.turno = turno;
    }
    
    public Lock getLock() {
        return lock;
    }

    public Condition getCondT() {
        return condT;
    }

    public void setCondT(Condition condT) {
        this.condT = condT;
    }
    
    
}
