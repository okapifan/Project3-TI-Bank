import mypackage.*;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.*;
import java.awt.Component;

import javax.swing.*;
//import javax.swing.SwingUtilities;

import javafx.scene.layout.Pane;

/**
 *
 * @author danie
 */
public class App {
    //public static MainScreen ms;
	
    public static void main(String[] args) {
        
        JFrame frame = new JFrame("My First GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000,600);
        JPanel00 panel = new JPanel00();
        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }
}
