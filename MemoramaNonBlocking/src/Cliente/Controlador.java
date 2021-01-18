/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

/**
 *
 * @author imanol
 */
public class Controlador implements ActionListener{
    
    private ConexionRed conexion;

    public Controlador() {
    }
    
    public void init(){
        ViewGameType vt = new ViewGameType();
    }
    
    public void connect(){
        conexion = new ConexionRed();
        conexion.connect();
    }
    
    public void disconnect() {
        conexion.disconnect();
    }

    public void sendMessage(String msg) {
        conexion.addMessageToSend(msg);
    }
    
    public int[] randomVectors() {
        int rand1[] = new int[20];
        int rand2[] = new int[20];
        int randfinal[] = new int[40];
        
        Random rnd = new Random();
        int n, k, res;
        n = k = 19; //var k auxiliar
        int ordenado[] = new int[n];

        for (int i = 0; i < n; i++) {
            ordenado[i] = i + 1;
        }

        for (int i = 0; i < n; i++) {
            res = rnd.nextInt(k);
            rand1[i] = ordenado[res];
            ordenado[res] = ordenado[k - 1];
            k--;
        }

        k = n;
        for (int i = 0; i < n; i++) {
            ordenado[i] = i + 1;
        }

        for (int i = 0; i < n; i++) {
            res = rnd.nextInt(k);
            rand2[i] = ordenado[res];
            ordenado[res] = ordenado[k - 1];
            k--;
        }

        for (int i = 0; i < 20; i++) {
            randfinal[i] = rand1[i];
        }
        for (int i = 20; i < 40; i++) {
            randfinal[i] = rand2[i - 20];
        }
        
        return randfinal;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

