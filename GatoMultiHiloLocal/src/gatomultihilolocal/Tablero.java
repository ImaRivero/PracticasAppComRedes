/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gatomultihilolocal;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;

/**
 *
 * @author imanol
 */
public class Tablero extends JFrame{
    private final JPanel panelBotones, panelInfo, masterPanel;
    private final JLabel partida,turno;
    private BotonGato[] botones = new BotonGato[9];
    private ArrayList<BotonGato> btn_cat;
    
    public Tablero(int idPart, int idPlayer, ActionListener action){
        this.btn_cat = new ArrayList();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(230, 230);
        setTitle("Gato Multihilos Local");
        setLocationRelativeTo(null);
        
        masterPanel = new JPanel();
        masterPanel.setLayout(new BorderLayout());
        panelInfo = new JPanel();
        panelBotones = new JPanel();
        
        panelInfo.setLayout(new BorderLayout());
        panelInfo.setBackground(Color.darkGray);
        panelInfo.setOpaque(true);
        
        partida = new JLabel("ID Partida: " + idPart + 
                            "        Player: " + idPlayer, SwingConstants.CENTER);
        partida.setForeground(Color.WHITE);
        panelInfo.add(partida, BorderLayout.WEST);
        
        turno = new JLabel("Turno", SwingConstants.CENTER);
        turno.setForeground(Color.WHITE);
        panelInfo.add(turno, BorderLayout.EAST);
        
        masterPanel.add(panelInfo, BorderLayout.SOUTH);
        
        panelBotones.setLayout(new GridLayout(3,3));
        panelBotones.setBackground(Color.darkGray);
        panelBotones.setOpaque(true);
        
        for(int i = 0; i < 9; i++){
            botones[i] = new BotonGato();
            botones[i].addActionListener(action);
            botones[i].setName("" + i);
            panelBotones.add(botones[i]);
            btn_cat.add(botones[i]);
        }
        
        masterPanel.add(panelBotones, BorderLayout.CENTER);
        add(masterPanel);
        setVisible(true);
    }

    public BotonGato[] getBotones() {
        return botones;
    }

    public void setBotones(BotonGato[] botones) {
        this.botones = botones;
    }

    public ArrayList<BotonGato> getBtn_cat() {
        return btn_cat;
    }

    public void setBtn_cat(ArrayList<BotonGato> btn_cat) {
        this.btn_cat = btn_cat;
    }
}

class BotonGato extends JButton{
    private int id;
    private boolean check;
    private int owner;
    
    public BotonGato(){
        this.owner = 0;
        this.check = false;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }  
}
