import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.Cursor;

import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.lang.Math;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Window extends JPanel{
    static final long serialVersionUID = 1L;
    static DecimalFormat df = new DecimalFormat("#.#");

    //variables for grid size
    static double ratio = Toolkit.getDefaultToolkit().getScreenSize().getWidth()/Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    static double gridWidth = 20;
    static double gridHeight = 20/ratio;
    static double xStep = 1;
    static double yStep = 1;
    static double intervalX = 1;
    static double intervalY = 1;
    static int frameWidth = 1;
    static int frameHeight = 1;
    
    //offset of screen in pixels
    static double xOffset = 0;
    static double yOffset = 0;

    //variables for representing units
    static final int BASE = 0;
    static final int MILLI = 1;
    static final int MICRO = 2;
    static final int NANO = 3;
    static final int[] VAL = {1, 1000, 1000000, 1000000000};
    static final String[] UNIT = {"m", "mm", "um", "nm"};
    static final String[] CUNIT = {"C", "mC", "uC", "nC"};
    static int curunit = BASE;

    static int cursor = Cursor.DEFAULT_CURSOR;
    static int mouseX = 0;
    static int mouseY = 0;
    static ArrayList<Charge> charges = new ArrayList<Charge>(0);
    static double maxCharge = 0;
    static boolean showGrid = true;
    static Graphics2D g2;   
    static Window window;

    /**
     * Set look and feel of the Window to match platform
     */
    public Window(){
        try{
            //Changes the look and feel from the default JAVA look and feel to the platform's look and feel
            UIManager.setLookAndFeel (UIManager.getSystemLookAndFeelClassName ());
        }catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e){}
        window = this;
        setFocusable(true);
    }

    /**
     * Main event loop for program
     */
    @Override
    protected void paintComponent(Graphics g){   
        super.paintComponent(g);
        g2 = (Graphics2D)g;

        this.setCursor(new Cursor(cursor));
        
        frameWidth = getWidth();
        frameHeight = getHeight();
        drawGrid();
        drawField();
        for (Charge c: charges){
            c.drawCharge();
        }
    }

    void drawField(){
        double x = 0, y = 0, r = 0, theta = 0, eField = 0, eFieldY = 0, eFieldX = 0;
        double arrowX = 0, arrowY = 0, arrowMidX = 0, arrowMidY = 0;
        double xDiff = gridWidth+xStep*(xOffset-frameWidth/2)/intervalX;
        double yDiff = gridHeight+yStep*(yOffset-frameHeight/2)/intervalY;

        for (double i = 0; i < gridWidth/xStep; i += 0.5){
            x = i*xStep-xDiff;
            arrowMidX = i*intervalX;
            for (double j = 0; j < gridHeight/xStep; j += 0.5){
                y = -j*yStep+yDiff;
                arrowMidY = j*intervalY;

                eFieldY = 0;
                eFieldX = 0;
                for (Charge c: charges){
                    r = Math.sqrt((x-c.getXNum())*(x-c.getXNum())+(y-c.getYNum())*(y-c.getYNum()));
                    theta = Math.atan2(y-c.getYNum(), x-c.getXNum());
                    eField = c.getChargeNum()/(r*r);
                    eFieldX += eField*Math.cos(theta);
                    eFieldY += eField*Math.sin(theta);
                }

                theta = Math.atan2(eFieldY, eFieldX);
                arrowX = 5*Math.cos(theta);
                arrowY = 5*Math.sin(theta);
                g2.drawLine((int)(arrowMidX+arrowX), (int)(arrowMidY-arrowY), (int)(arrowMidX-arrowX), (int)(arrowMidY+arrowY));
                //g2.drawString(df.format(eFieldX)+"; "+df.format(eFieldY), (int)(i*intervalX), (int)(j*intervalY));
            }
        }
    }

    /**
     * Makes the grid on the screen
     * 
     * @param g2 Needed to draw to JFrame
     */
    void drawGrid(){
        double i;

        //Find Step size and Unit
        {
            xStep = findStep(gridWidth/20);
            intervalX = frameWidth/(gridWidth/xStep);
            if (xStep >= 0.5){
                curunit = BASE;
            }
            else if (xStep >= 0.0005){
                curunit = MILLI;
            }
            else if (xStep >= 0.0000005){
                curunit = MICRO;
            }
            else{
                curunit = NANO;
            }

            ratio = gridWidth/gridHeight;
            yStep = findStep(gridHeight/(20/ratio));
            intervalY = frameHeight/(gridHeight/yStep);
            if (yStep >= 0.5){
                curunit = BASE;
            }
            else if (yStep >= 0.0005){
                curunit = MILLI;
            }
            else if (yStep >= 0.0000005){
                curunit = MICRO;
            }
            else{
                curunit = NANO;
            }
        }

        if (showGrid){
            g2.setColor(Color.LIGHT_GRAY);
            double k = 0;
            if (0.5*frameHeight + yOffset < 0.02*frameHeight){
                for (i = frameWidth/2+(int)xOffset; i < frameWidth; i += intervalX){
                    g2.drawLine((int)i, 0, (int)i, frameHeight);
                    g2.drawString(df.format(k*VAL[curunit]), (int)i, (int)(0.02*frameHeight));
                    k += xStep;
                }
                k = 0;
                for (i = frameWidth/2+(int)xOffset; i > 0; i -= intervalX){
                    g2.drawLine((int)i, 0, (int)i, frameHeight);
                    g2.drawString(df.format(k*VAL[curunit]), (int)i, (int)(0.02*frameHeight));
                    k -= xStep;
                }
                g2.setColor(Color.BLACK);
                g2.drawString(UNIT[curunit], (int)(0.98*frameWidth), (int)(0.02*frameHeight));
            }
            else if (0.5*frameHeight + yOffset > 0.99*frameHeight){
                for (i = frameWidth/2+(int)xOffset; i < frameWidth; i += intervalX){
                    g2.drawLine((int)i, 0, (int)i, frameHeight);
                    g2.drawString(df.format(k*VAL[curunit]), (int)i, (int)(0.99*frameHeight));
                    k += xStep;
                }
                k = 0;
                for (i = frameWidth/2+(int)xOffset; i > 0; i -= intervalX){
                    g2.drawLine((int)i, 0, (int)i, frameHeight);
                    g2.drawString(df.format(k*VAL[curunit]), (int)i, (int)(0.99*frameHeight));
                    k -= xStep;
                }
                g2.setColor(Color.BLACK);
                g2.drawString(UNIT[curunit], (int)(0.98*frameWidth), (int)(0.99*frameHeight));
            }
            else{
                for (i = frameWidth/2+(int)xOffset; i < frameWidth; i += intervalX){
                    g2.drawLine((int)i, 0, (int)i, frameHeight);
                    g2.drawString(df.format(k*VAL[curunit]), (int)i, frameHeight/2+(int)yOffset);
                    k += xStep;
                }
                k = 0;
                for (i = frameWidth/2+(int)xOffset; i > 0; i -= intervalX){
                    g2.drawLine((int)i, 0, (int)i, frameHeight);
                    g2.drawString(df.format(k*VAL[curunit]), (int)i, frameHeight/2+(int)yOffset);
                    k -= xStep;
                }
                g2.setColor(Color.BLACK);
                g2.drawString(UNIT[curunit], (int)(0.98*frameWidth), frameHeight/2+(int)yOffset);
            }

            g2.setColor(Color.LIGHT_GRAY);
            k = 0;
            if (0.5*frameWidth + xOffset < 0.01*frameWidth){
                for (i = frameHeight/2+(int)yOffset; i < frameHeight; i += intervalY){
                    g2.drawLine(0, (int)i, frameWidth, (int)i);
                    g2.drawString(df.format(k*VAL[curunit]), (int)(0.01*frameWidth), (int)i);
                    k -= yStep;
                }
                k = 0;
                for (i = frameHeight/2+(int)yOffset; i > 0; i -= intervalY){
                    g2.drawLine(0, (int)i, frameWidth, (int)i);
                    g2.drawString(df.format(k*VAL[curunit]), (int)(0.01*frameWidth), (int)i);
                    k += yStep;
                }
                g2.setColor(Color.BLACK);
                g2.drawString(UNIT[curunit], (int)(0.01*frameWidth), (int)(0.02*frameHeight));
            }
            else if (0.5*frameWidth + xOffset > 0.99*frameWidth){
                for (i = frameHeight/2+(int)yOffset; i < frameHeight; i += intervalY){
                    g2.drawLine(0, (int)i, frameWidth, (int)i);
                    g2.drawString(df.format(k*VAL[curunit]), (int)(0.99*frameWidth), (int)i);
                    k -= yStep;
                }
                k = 0;
                for (i = frameHeight/2+(int)yOffset; i > 0; i -= intervalY){
                    g2.drawLine(0, (int)i, frameWidth, (int)i);
                    g2.drawString(df.format(k*VAL[curunit]), (int)(0.99*frameWidth), (int)i);
                    k += yStep;
                }
                g2.setColor(Color.BLACK);
                g2.drawString(UNIT[curunit], (int)(0.99*frameWidth), (int)(0.02*frameHeight));
            }
            else{
                for (i = frameHeight/2+(int)yOffset; i < frameHeight; i += intervalY){
                    g2.drawLine(0, (int)i, frameWidth, (int)i);
                    g2.drawString(df.format(k*VAL[curunit]), frameWidth/2+(int)xOffset, (int)i);
                    k -= yStep;
                }
                k = 0;
                for (i = frameHeight/2+(int)yOffset; i > 0; i -= intervalY){
                    g2.drawLine(0, (int)i, frameWidth, (int)i);
                    g2.drawString(df.format(k*VAL[curunit]), frameWidth/2+(int)xOffset, (int)i);
                    k += yStep;
                }
                g2.setColor(Color.BLACK);
                g2.drawString(UNIT[curunit], frameWidth/2 + (int)xOffset, (int)(0.02*frameHeight));
            }
            
            g2.drawLine(0, frameHeight/2+(int)yOffset, frameWidth, frameHeight/2+(int)yOffset);
            g2.drawLine(frameWidth/2+(int)xOffset, 0, frameWidth/2+(int)xOffset, frameHeight);
        }
    }

    /**
     * Finds an step value that starts with either 1, 2 or 5 and is one 
     * significant only. This makes the step sizes on the grid simpler to
     * follow.
     * 
     * @param val Value to round
     * @return double the rounded to sig. fig. value
     */
    double findStep(double val){
        int exponent = getExp(val);
        val = val/(Math.pow(10, exponent));

        if (val < 1.5){
            val = 1;
        }
        else if (val < 3.5){
            val = 2;
        }
        else if (val < 7.5){
            val = 5;
        }
        else{
            val = 10;
        }
        return val*(Math.pow(10, exponent));
    }

    /**
     * Gets the exponent on 10 needed to put a double
     * into scientific notation.
     * 
     * @param val double to get exponent for
     * @return int The exponent on 10 for a double in scientific notation
     */
    int getExp(double val){
        if (val < 0){
            val = -val;
        }
        if (val < 10 && val >= 1){
            return 0;
        }
        else if (val < 1){
            int i = 0;
            while (val < 1){
                val = val*10;
                ++i;
            }
            return -i;
        }
        else{
            int i = 0;
            while (val > 10){
                val = val/10;
                ++i;
            }
            return i;
        }
    }

    static void findMax(){
        for (Charge c: charges){
            if (Math.abs(c.getChargeNum())>maxCharge){
                maxCharge = Math.abs(c.getChargeNum());
            }
        }
    }
}