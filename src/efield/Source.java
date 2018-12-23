/**
 * @author Dhairya Patel
 * Program to simulate electric fields
 */

package efield;

import java.awt.event.MouseListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

/**
 * Start of the program.
 * Makes the JFrame for the entire program and adds the program window
 * to it (the JPanel).
 */
public class Source{
    public static void main(String []args){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               //Create JPanel for program
                Window window = new Window();
                
                //Add listeners
                InputHandling inputHandling = new InputHandling();
                MouseListener listener = inputHandling.new MouseInputHandler();
                window.addMouseListener(listener);
                MouseWheelListener wheelListener = inputHandling.new MouseWheelHandler();
                window.addMouseWheelListener(wheelListener);
                MouseMotionListener moveListener = inputHandling.new MouseMoveHandler();
                window.addMouseMotionListener(moveListener);

                //Create a new JFrame for the GUI and adds the program's window to it
                JFrame frame = new JFrame("Electric Field Simulator");
                frame.add(window);
                frame.setVisible(true);
                frame.setExtendedState (JFrame.MAXIMIZED_BOTH);
                frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
                frame.setResizable(false);
                frame.setIconImage(new ImageIcon("Assets\\eFieldSimIcon.png").getImage());
            }
        });        
    }
}