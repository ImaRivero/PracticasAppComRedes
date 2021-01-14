/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

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
public class ViewGameType extends JFrame implements ActionListener{
    private final JPanel panel;
    private final JButton btnSolGame, btnParGame;
    private final JLabel txtName;
    private final JTextField tf;
    private final GridBagConstraints gbc;
    private int cantHilos;
    private Controlador cont;
    
    public ViewGameType() {
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setTitle("Examen 2 - Seleccion de juego");
        setLocationRelativeTo(null);
        
        cont = new Controlador();
        
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.darkGray);
        panel.setOpaque(true);
        add(panel);

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        txtName = new JLabel("Ingresa tu nombre", SwingConstants.CENTER);
        txtName.setForeground(Color.white);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(txtName, gbc);

        tf = new JTextField(50);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = 2;
        panel.add(tf, gbc);

        btnSolGame = new JButton("Juego En Solitario");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        btnSolGame.addActionListener(this);
        panel.add(btnSolGame, gbc);
        
        btnParGame = new JButton("Juego En Pareja");
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        btnParGame.addActionListener(this);
        panel.add(btnParGame, gbc);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton sauce = (JButton) e.getSource();
        if(txtName.getText().isBlank()){
            JOptionPane.showMessageDialog(null, "Es necesario ingresar un nombre");
        }
        else{
            if(sauce == btnSolGame){
                cont.connect();
            }
        }
    }
}
