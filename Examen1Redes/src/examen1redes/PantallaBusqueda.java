/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examen1redes;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @author imanol
 */
public class PantallaBusqueda{
        
    private final JFrame thisframe;
    private final JPanel panel;
    private final JLabel encab, hiloAnt, hiloSig, nombreLabel, msjlabel;
    private JTextArea mensajes;
    private JTextField nombreField;
    private JButton buscar;
    private GridBagConstraints gbc;
    private int hilosActivos;
    private long idAct, idAnt, idSig;
    private ActionListener al;
    
    public PantallaBusqueda(int hilosActivos, long idAct, long idAnt, long idSig, ActionListener al){
        this.hilosActivos = hilosActivos;
        this.idAct = idAct;
        this.idAnt = idAnt;
        this.idSig = idSig;
        this.al = al;
        thisframe = new JFrame();
        thisframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        thisframe.setSize(300, 300);
        thisframe.setTitle("Examen 1 - Aplicaciones Red");
        thisframe.setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.darkGray);
        panel.setOpaque(true);
        thisframe.add(panel);

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        encab = new JLabel("Soy el hilo: " + idAct + ". Hilos activos: " + hilosActivos, SwingConstants.CENTER);
        encab.setForeground(Color.white);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(encab, gbc);

        hiloAnt = new JLabel("Hilo anterior: " + idAnt, SwingConstants.CENTER);
        hiloAnt.setForeground(Color.white);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(hiloAnt, gbc);

        hiloSig = new JLabel("Hilo siguiente: " + idSig, SwingConstants.CENTER);
        hiloSig.setForeground(Color.white);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(hiloSig, gbc);

        nombreLabel = new JLabel("Ingresa el nombre del archivo:", SwingConstants.CENTER);
        nombreLabel.setForeground(Color.white);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        panel.add(nombreLabel, gbc);

        nombreField = new JTextField(40);
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(nombreField, gbc);

        buscar = new JButton("Buscar");
        buscar.addActionListener(al);
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(buscar, gbc);

        msjlabel = new JLabel("Mensajes:", SwingConstants.CENTER);
        msjlabel.setForeground(Color.white);
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(msjlabel, gbc);

        mensajes = new JTextArea();
        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(mensajes, gbc);

        thisframe.setVisible(true);
    }

    public JTextField getNombreField() {
        return nombreField;
    }

    public JTextArea getMensajes() {
        return mensajes;
    }
    
    
}
