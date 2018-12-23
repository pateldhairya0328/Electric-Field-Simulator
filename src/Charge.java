import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Color;

import javax.swing.JPanel;

public class Charge{
    private final int rectSize = 30;
    private double charge = 0;
    private double x = 0;
    private double y = 0;
    private Rectangle rect;
    private Graphics2D g2;

    /**
     * 
     * @param x x-position of charge
     * @param y y-position of charge
     * @param charge the charge value
     * @param JPanel panel holding the charge
     */
    public Charge(double x, double y, double charge, JPanel panel){
        this.x = x;
        this.y = y;
        this.charge = charge;
        this.g2 = (Graphics2D)panel.getGraphics();

        rect = new Rectangle();
        rect.x = (int)(x*Window.intervalX/Window.xStep+Window.xOffset+Window.frameWidth/2)-rectSize/2;
        rect.y = (int)(-y*Window.intervalY/Window.yStep+Window.yOffset+Window.frameHeight/2)-rectSize/2;
        rect.setSize(rectSize, rectSize);
    }

    public String getx(){
        if (this.x >= 0.5){
            return this.x*Window.VAL[Window.BASE]+" "+Window.UNIT[Window.BASE];
        }
        else if (this.x >= 0.0005){
            return this.x*Window.VAL[Window.MILLI]+" "+Window.UNIT[Window.MILLI];
        }
        else if (this.x >= 0.0000005){
            return this.x*Window.VAL[Window.MICRO]+" "+Window.UNIT[Window.MICRO];
        }
        else{
            return this.x*Window.VAL[Window.NANO]+" "+Window.UNIT[Window.NANO];
        }
    }

    public double getXNum(){
        return x;
    }

    public String gety(){
        if (this.y >= 0.5){
            return this.y*Window.VAL[Window.BASE]+" "+Window.UNIT[Window.BASE];
        }
        else if (this.y >= 0.0005){
            return this.y*Window.VAL[Window.MILLI]+" "+Window.UNIT[Window.MILLI];
        }
        else if (this.y >= 0.0000005){
            return this.y*Window.VAL[Window.MICRO]+" "+Window.UNIT[Window.MICRO];
        }
        else{
            return this.y*Window.VAL[Window.NANO]+" "+Window.UNIT[Window.NANO];
        }
    }

    public double getYNum(){
        return y;
    }

    public String getCharge(){
        if (this.charge >= 0.5){
            return this.charge*Window.VAL[Window.BASE]+" "+Window.CUNIT[Window.BASE];
        }
        else if (this.charge >= 0.0005){
            return this.charge*Window.VAL[Window.MILLI]+" "+Window.CUNIT[Window.MILLI];
        }
        else if (this.charge >= 0.0000005){
            return this.charge*Window.VAL[Window.MICRO]+" "+Window.CUNIT[Window.MICRO];
        }
        else{
            return this.charge*Window.VAL[Window.NANO]+" "+Window.CUNIT[Window.NANO];
        }
    }

    public double getChargeNum(){
        return charge;
    }

    public Rectangle getRect(){
        rect.x = (int)(x*Window.intervalX/Window.xStep+Window.xOffset+Window.frameWidth/2)-rectSize/2;
        rect.y = (int)(-y*Window.intervalY/Window.yStep+Window.yOffset+Window.frameHeight/2)-rectSize/2;
        return rect;
    }

    public void setx(double x){
        rect.x = (int)(x*Window.intervalX/Window.xStep+Window.xOffset+Window.frameWidth/2)-rectSize/2;
        this.x = x;
    }

    public void sety(double y){
        rect.y = (int)(-y*Window.intervalY/Window.yStep+Window.yOffset+Window.frameHeight/2)-rectSize/2;
        this.y = y;
    }

    public void setCharge(double charge){
        this.charge = charge;
    }

    public void drawCharge(){
        rect.x = (int)(x*Window.intervalX/Window.xStep+Window.xOffset+Window.frameWidth/2)-rectSize/2;
        rect.y = (int)(-y*Window.intervalY/Window.yStep+Window.yOffset+Window.frameHeight/2)-rectSize/2;
        
        //ratio of charge to maximum charge
        double r = 0;

        //sets the color of the charge based on the ratio
        int red = 240, green = 240, blue = 240;
        if (charge != 0){
            r = charge/Window.maxCharge;
            red = (int)(111.333*r*r*r*r-0.666667*r*r*r-223.833*r*r+128.167*r+240);
            green = (int)(184*r*r*r*r-1.1146e-1*r*r*r-350*r*r-74*r+240);
            blue = (int)(111.333*r*r*r*r+0.666667*r*r*r-223.833*r*r-128.167*r+240);
            
            if (red>255){
                red = 255;
            }
            if (blue>255){
                blue = 255;
            }
        }
        
        g2.setColor(new Color(red, green, blue));
        g2.fillOval(rect.x, rect.y, rectSize, rectSize);
        g2.setColor((Color.lightGray));
        g2.drawOval(rect.x, rect.y, rectSize, rectSize);
    }
}