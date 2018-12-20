import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.lang.Math;

public class Window extends JPanel{
    static final long serialVersionUID = 1L;

    static double gridWidth = 20;
    static final int BASE = 0;
    static final int MILLI = 1;
    static final int MICRO = 2;
    static final int NANO = 3;
    static final int[] VAL = {1, 1000, 1000000, 1000000000};
    static final String[] UNIT = {"m", "mm", "um", "nm"};
    int curunit = BASE;

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
        
        drawGrid(g2);

        repaint();
    }

    /**
     * Makes the grid on the screen
     * 
     * @param g2 Needed to draw to JFrame
     */
    void drawGrid(Graphics2D g2){
        
        double step = roundToSigfig(gridWidth/20);
        double interval = getWidth()/(gridWidth/step);

        g2.setColor(Color.LIGHT_GRAY);
        double i;
        double k = 0;

        if (step >= 0.5){
            curunit = BASE;
        }
        else if (step >= 0.0005){
            curunit = MILLI;
        }
        else if (step >= 0.0000005){
            curunit = MICRO;
        }
        else{
            curunit = NANO;
        }

        for (i = getWidth()/2; i < getWidth(); i += interval){
            g2.drawLine((int)i, 0, (int)i, getHeight());
            g2.drawString(Integer.toString((int)(k*VAL[curunit]))+" "+UNIT[curunit], (int)i, getHeight()/2);
            k += step;
        }
        for (i = getWidth()/2; i > 0; i -= interval){
            g2.drawLine((int)i, 0, (int)i, getHeight());
        }
        for (i = getHeight()/2; i < getHeight(); i += interval){
            g2.drawLine(0, (int)i, getWidth(), (int)i);
        }
        for (i = getHeight()/2; i > 0; i -= interval){
            g2.drawLine(0, (int)i, getWidth(), (int)i);
        }
        g2.setColor(Color.black);
        g2.drawLine(0, getHeight()/2, getWidth(), getHeight()/2);
        g2.drawLine(getWidth()/2, 0, getWidth()/2, getHeight());
    }

    /**
     * 
     * @param val
     * @return
     */
    double roundToSigfig(double val){
        int exponent = getExp(val);
        val = val/(Math.pow(10, exponent));
        val = 2*Math.round(val/2);
        val = val == 0 ? 0.1 : val;
        return val*(Math.pow(10, exponent));
    }

    /**
     * Gets the exponent on 10 needed to put a double
     * into scientific notation.
     * 
     * @param val double to get exponent for
     * @return The exponent on 10 for a double in scientific notation
     */
    int getExp(double val){
        if (val < 0){
            val = -val;
        }
        if (val < 10 && val >= 1){
            return 0;
        }
        if (val < 1){
            int i = 0;
            while (val < 1){
                val = val*10;
                ++i;
            }
            return -i;
        }
        else if (val > 10){
            int i = 0;
            while (val > 10){
                val = val/10;
                ++i;
            }
            return i;
        }
        return 0;
    }
}