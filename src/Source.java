import java.awt.event.MouseListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.ImageIcon;

public class Source{
    public static void main(String []args){
        //Create a new JFrame for the GUI and adds the program's window to it
        Window window = new Window();
        JFrame frame = new JFrame("Electric Field Simulator");
        frame.add(window);
        frame.setVisible(true);
        frame.setExtendedState (JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        frame.setIconImage(new ImageIcon("Assets\\eFieldSimIcon.png").getImage());
        
        //Add listeners
        InputHandling inputHandling = new InputHandling();
        MouseListener listener = inputHandling.new MouseInputHandler();
        frame.addMouseListener(listener);
        MouseWheelListener wheelListener = inputHandling.new MouseWheelHandler();
        frame.addMouseWheelListener(wheelListener);
        MouseMotionListener moveListener = inputHandling.new MouseMoveHandler();
        frame.addMouseMotionListener(moveListener);
    }
}