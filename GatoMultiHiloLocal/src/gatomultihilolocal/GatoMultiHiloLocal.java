/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gatomultihilolocal;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author imanol
 */
public class GatoMultiHiloLocal{
    public static void main(String[] args) {
        PantallaInicial inicio = new PantallaInicial();
    }
}

class PantallaInicial extends JFrame implements ActionListener{
    
    private final JPanel panel;
    private final JButton btn;
    private final JLabel encab;
    private final JTextField tf;
    private final GridBagConstraints gbc;
    private int cantHilos;
    
    
    public PantallaInicial(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setTitle("Practica-JuegoGatoHilos");
        setLocationRelativeTo(null);
        
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.darkGray);
        panel.setOpaque(true);
        add(panel);
        
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        
        encab = new JLabel("Ingresa la cantidad de partidas de Gato", SwingConstants.CENTER);
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
                Shared sh[] = new Shared[cantHilos];
                HiloGato hg[] = new HiloGato[cantHilos*2];
                setVisible(false);
                
                for(int i = 0; i < cantHilos; i++){
                    sh[i] = new Shared();
                    for(int j = 1; j < 3; j++){
                        hg[i] = new HiloGato(i, j, sh[i]);
                        hg[i].start();
                    }
                }
            }
        }catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(null, "Ingresa un valor numerico");
        }
    }
}

