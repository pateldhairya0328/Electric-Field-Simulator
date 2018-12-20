import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Window extends JPanel{
    static final long serialVersionUID = 1L;

    /**
     * Set look and feel of the Window
     */
    public Window(){
        try{
            //Changes the look and feel from the default JAVA look and feel to the platform's look and feel
            UIManager.setLookAndFeel (UIManager.getSystemLookAndFeelClassName ());
        }catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e){}
        
        setFocusable(true);
    }


    @Override
    protected void paintComponent(Graphics g){   
    	
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        
    }
}