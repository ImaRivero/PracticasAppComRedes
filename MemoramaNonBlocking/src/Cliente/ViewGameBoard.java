/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants; 

/**
 *
 * @author imanol
 */
public class ViewGameBoard extends JFrame{
    
    Carta cartas[] = new Carta[40];
    ImageIcon imas[] = new ImageIcon[21];
    
    private final JPanel panelBotones, panelInfo, masterPanel;
    private final JLabel partida, j1score, j2score;
    
    int vector[];
    
    public ViewGameBoard(int vector[], int idCliente, int idPartida, boolean solitario, ActionListener action){
        this.vector = vector;
        
        masterPanel = new JPanel();
        masterPanel.setLayout(new BorderLayout());
        panelInfo = new JPanel();
        panelBotones = new JPanel();
        
        
        panelInfo.setLayout(new FlowLayout());
        panelInfo.setBackground(Color.darkGray);
        panelInfo.setOpaque(true);
        
        partida = new JLabel("ID Partida: " + idPartida + " ------ ", SwingConstants.CENTER);
        j1score = new JLabel("Score Jugador 1: 0" + " ------ ", SwingConstants.CENTER);
        j2score = new JLabel("Score Jugador 2: 0", SwingConstants.CENTER);
                
        partida.setForeground(Color.WHITE);
        j1score.setForeground(Color.WHITE);
        j2score.setForeground(Color.WHITE);
        
        panelInfo.add(partida, BorderLayout.WEST);
        
        panelInfo.add(j1score, BorderLayout.CENTER);
        if(!solitario)
            panelInfo.add(j2score, BorderLayout.EAST);
        
        masterPanel.add(panelInfo, BorderLayout.SOUTH);
        
        panelBotones.setLayout(new GridLayout(8,5));
        panelBotones.setBackground(Color.darkGray);
        panelBotones.setOpaque(true);
        
        imas[20] = new ImageIcon("./src/Cliente/img/fondo.jpg");
        
        for (int i = 0; i < 20; i++){
            imas[i] = new ImageIcon("./src/Cliente/img/bob" + (i + 1) + ".jpg");
        }
            
        
        for(int i = 0; i < 40; i++){
            cartas[i] = new Carta(imas[20]);
            cartas[i].addActionListener(action);
            cartas[i].setEstado(false);
            cartas[i].setId(vector[i]);
            panelBotones.add(cartas[i]);
        }
        
        masterPanel.add(panelBotones, BorderLayout.CENTER);
        
        add(masterPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 570);
        setTitle("Memoraba Cliente " + idCliente);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

