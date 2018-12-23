package efield;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.Cursor;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.lang.Math;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Window extends JPanel{
    static final long serialVersionUID = 1L;
    static DecimalFormat df = new DecimalFormat("#.#");
    static DecimalFormat df2 = new DecimalFormat("#.###");

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

    //mouse variables
    static int cursor = Cursor.DEFAULT_CURSOR;
    static int mouseX = 0;
    static int mouseY = 0;
    
    //charge variables
    static ArrayList<Charge> charges = new ArrayList<Charge>(0);
    static double maxCharge = 0;

    //miscellaneous
    static boolean showGrid = true;
    static Graphics2D g2;   
    static Window window;
    static String display = "";

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
        g2.setFont(new Font("Times New Roman", Font.PLAIN, 20));

        //changes cursor if needed
        this.setCursor(new Cursor(cursor));
        
        //gets dimensions of frame (for use in other places)
        frameWidth = getWidth();
        frameHeight = getHeight();
        
        //draws the grid and electric field
        drawGrid();
        drawField();
        
        //draws the charges
        for (Charge c: charges){
            c.drawCharge();
        }
        
        //draws the value of the electric field at a point
        //the user clicked at
        if (!display.equals("")){
            g2.setColor(new Color(240, 240, 240));
            g2.fillRect(15, 0, 10 + g2.getFontMetrics().stringWidth(display), 30);
            g2.setColor(Color.LIGHT_GRAY);
            g2.drawRect(15, 0, 10 + g2.getFontMetrics().stringWidth(display), 30);
            g2.setColor(Color.BLACK);
            g2.drawString(display, 20, 20);
        }
        
        int [] xs = {(int)(0.95*frameWidth), (int)(0.9525*frameWidth), (int)(0.9475*frameWidth)};
        int [] ys = {(int)(0.15*frameHeight-0.0375*frameWidth), (int)(0.15*frameHeight - 0.0325*frameWidth), (int)(0.15*frameHeight - 0.0325*frameWidth)};
        g2.fillPolygon(xs, ys, 3);
        int []xss = {(int)(0.9875*frameWidth), (int)(0.9825*frameWidth), (int)(0.9825*frameWidth)};
        int []yss = {(int)(0.15*frameHeight), (int)(0.15*frameHeight - 0.0025*frameWidth), (int)(0.15*frameHeight + 0.0025*frameWidth)};
        g2.fillPolygon(xss, yss, 3);
        g2.drawLine((int)(0.95*frameWidth), (int)(0.15*frameHeight), (int)(0.9875*frameWidth), (int)(0.15*frameHeight));
        g2.drawLine((int)(0.95*frameWidth), (int)(0.15*frameHeight), (int)(0.95*frameWidth), (int)(0.15*frameHeight-0.0375*frameWidth));
        g2.drawString("N", (int)(0.95*frameWidth)-7, (int)(0.15*frameHeight-0.0375*frameWidth));
        g2.drawString("E", (int)(0.9875*frameWidth), (int)(0.15*frameHeight)+7);
    }

    /**
     * Draws the electric field itself
     */
    void drawField(){
        double x = 0, y = 0, r = 0, theta = 0, delta = 0.5, eField = 0, eFieldY = 0, eFieldX = 0;
        double arrowX = 0, arrowY = 0, arrowMidX = 0, arrowMidY = 0, sizeX, sizeY;
        double xDiff = gridWidth+xStep*(xOffset-frameWidth/2)/intervalX;
        double yDiff = gridHeight+yStep*(yOffset-frameHeight/2)/intervalY;
        int xm, xn, ym, yn;

        double [][] vals = new double[1+(int)(2*gridWidth/xStep)][1+(int)(2*gridHeight/xStep)];
        double [][] ang = new double[1+(int)(2*gridWidth/xStep)][1+(int)(2*gridHeight/xStep)];
        double max = 0;

        //finds and stores electric field values (and angle) for every point to draw at
        //also finds the maximum charge
        for (double i = 0; i < gridWidth/xStep; i += 0.5){
            x = i*xStep-xDiff;
            for (double j = 0; j < gridHeight/xStep; j += 0.5){
                y = -j*yStep+yDiff;
                eFieldY = 0;
                eFieldX = 0;
                //calculate x and y components of efield first, then the overall efield strength and angle
                for (Charge c: charges){
                    r = Math.sqrt((x-c.getXNum())*(x-c.getXNum())+(y-c.getYNum())*(y-c.getYNum()));
                    theta = Math.atan2(y-c.getYNum(), x-c.getXNum());
                    eField = c.getChargeNum()/(r*r);
                    eFieldX += eField*Math.cos(theta);
                    eFieldY += eField*Math.sin(theta);
                }
                eField = Math.sqrt(eFieldX*eFieldX+eFieldY*eFieldY);
                if (eField > max){
                    max = eField;
                }
                vals[(int)(2*i)][(int)(2*j)] = eField;
                ang[(int)(2*i)][(int)(2*j)] = Math.atan2(eFieldY, eFieldX);
            }
        }

        //draws the electric field values found before as arrows, with their darkness 
        //representing their magnitude.
        for (int i = 0; i < 2*gridWidth/xStep; i++){
            arrowMidX = 0.5*i*intervalX;
            for (int j = 0; j < 2*gridHeight/xStep; j++){
                if (vals[i][j] != 0){
                    theta = ang[i][j];
                    arrowMidY = 0.5*j*intervalY;
                    sizeX = 0.15*intervalX;
                    sizeY = 0.15*intervalY;
                    arrowX = sizeX*Math.cos(theta);
                    arrowY = sizeY*Math.sin(theta);
                    
                    //arrow head calculations
                    xm = (int)(.75*sizeX*Math.cos(Math.PI+theta+delta)+arrowMidX+arrowX);
                    xn = (int)(.75*sizeX*Math.cos(Math.PI+theta-delta)+arrowMidX+arrowX);
                    ym = (int)(-.75*sizeY*Math.sin(Math.PI+theta+delta)+arrowMidY-arrowY);
                    yn = (int)(-.75*sizeY*Math.sin(Math.PI+theta-delta)+arrowMidY-arrowY);
                    int[] xs = {xm, xn, (int)(arrowMidX+arrowX)};
                    int[] ys = {ym, yn, (int)(arrowMidY-arrowY)};

                    //makes color of charges a gradient overall based on electric field strength at that point
                    int color = (int)(200*Math.pow(100, -100*vals[i][j]/max));
                    g2.setColor(new Color(color, color, color));
                    g2.drawLine((int)(arrowMidX+arrowX), (int)(arrowMidY-arrowY), (int)(arrowMidX-arrowX), (int)(arrowMidY+arrowY));
                    g2.fillPolygon(xs, ys, 3);
                }
            }
        }
    }

    //gets the electric field strength at one point
    //also changes the display string representing the efield strength at a point
    //if the user clicked there
    /**
     * gets the electric field strength at one point
     * also changes the display string representing the efield strength at a point
     * if the user clicked there
     * 
     * @param x x coordinate to get efield strength at
     * @param y y coordinate to get efield strength at
     * @param changeDisplay check to change the display or not
     * @return String efield strength and it's angle
     */
    public String getEFieldValue(double x, double y, boolean changeDisplay){
        double eFieldY, eFieldX, eField, r, theta;
        eFieldY = 0;
        eFieldX = 0;
        for (Charge c: charges){
            r = Math.sqrt((x-c.getXNum())*(x-c.getXNum())+(y-c.getYNum())*(y-c.getYNum()));
            theta = Math.atan2(y-c.getYNum(), x-c.getXNum());
            eField = c.getChargeNum()/(r*r);
            eFieldX += eField*Math.cos(theta);
            eFieldY += eField*Math.sin(theta);
        }

        //8987551787.3681764 = Coulomb's constant
        eField = 8987551787.3681764*Math.sqrt(eFieldX*eFieldX+eFieldY*eFieldY);
        theta = Math.toDegrees(Math.atan2(eFieldY, eFieldX));
        String display1;

        if (theta > 0 && theta < 90){
            display1 = df2.format(eField)+" N/C [E"+df2.format(theta)+"\u00b0N]";
        }
        else if (theta > 90 && theta < 180){
            display1 = df2.format(eField)+" N/C [W"+df2.format(180-theta)+"\u00b0N]";
        }
        else if (theta < 0 && theta > -90){
            display1 = df2.format(eField)+" N/C [E"+df2.format(-theta)+"\u00b0S]";
        }
        else if (theta < -90 && theta > -180){
            display1 = df2.format(eField)+" N/C [W"+df2.format(180+theta)+"\u00b0S]";
        }
        else if (theta == 0){
            display1 = df2.format(eField)+" N/C [E]";
        }
        else if (theta == 90){
            display1 = df2.format(eField)+" N/C [N]";
        }
        else if (theta == -90){
            display1 = df2.format(eField)+" N/C [S]";
        }
        else{
            display1 = df2.format(eField)+" N/C [W]";
        }
        if (changeDisplay){
            display = display1;
        }
        return display1;
    }

    /**
     * Makes the grid on the screen
     * 
     * @param g2 Needed to draw to JFrame
     */
    void drawGrid(){
        double i;

        //Find Step size and Unit
        //This is done even if the user has selected to hide the grid
        //because the values calculated here are needed in other 
        //places
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

        //draws the grid if user hasn't selected to hide the grid
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
     * significant figure only. This makes the step sizes on the grid simpler to
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

    /**
     * Finds the maximum magnitude charge
     */
    static void findMax(){
        for (Charge c: charges){
            if (Math.abs(c.getChargeNum())>maxCharge){
                maxCharge = Math.abs(c.getChargeNum());
            }
        }
    }
}