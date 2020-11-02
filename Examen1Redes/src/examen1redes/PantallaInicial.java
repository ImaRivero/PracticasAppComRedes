/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examen1redes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 *
 * @author imanol
 */
public class PantallaInicial extends JFrame implements ActionListener{
    
    protected JPanel panel;
    protected JButton btn, btn2;
    protected JLabel encab;
    protected JTextField tf;
    protected GridBagConstraints gbc;
    protected int cantHilos;
    
    
    public PantallaInicial(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setTitle("Examen 1 - Aplicaciones Red");
        setLocationRelativeTo(null);
        
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.darkGray);
        panel.setOpaque(true);
        add(panel);
        
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        
        encab = new JLabel("Ingresa la cantidad de hilos a ejecutar.", SwingConstants.CENTER);
        encab.setForeground(Color.white);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(encab, gbc);
        
        tf = new JTextField(2);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(tf, gbc);
        
        btn = new JButton("Aceptar");
        gbc.gridx = 0;
        gbc.gridy = 2;
        btn.addActionListener(this);
        panel.add(btn, gbc);
        
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) { //To change body of generated methods, choose Tools | Templates.
        JButton sauce = (JButton) e.getSource();
        try{
            if(sauce == btn){
                cantHilos = Integer.parseInt(tf.getText());
                PantallaBusqueda arrayhilos[] = new PantallaBusqueda[cantHilos];
                setVisible(false);
                
                for(int i = 0; i < cantHilos; i++)
                    arrayhilos[i] = new PantallaBusqueda(cantHilos, "/test");
                
                for(int i = 0; i < cantHilos; i++){
                    if(i == 0){ // El primero
                        arrayhilos[i].setAnt(arrayhilos[cantHilos-1]);
                        arrayhilos[i].setSig(arrayhilos[i+1]);
                    }
                    else if(i == cantHilos-1){ // El ultimo
                        arrayhilos[i].setAnt(arrayhilos[i-1]);
                        arrayhilos[i].setSig(arrayhilos[0]);
                    }
                    else{ // Cualquier otro caso
                        arrayhilos[i].setAnt(arrayhilos[i-1]);
                        arrayhilos[i].setSig(arrayhilos[i+1]);
                    }
                }
                
                for(int i = 0; i < cantHilos; i++)
                    arrayhilos[i].start();
            }
        }catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(null, "Siguele de pinche chistosito");
        }
    }
}
