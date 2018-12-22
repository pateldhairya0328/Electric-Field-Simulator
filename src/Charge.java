import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JPanel;

public class Charge{
    private double charge = 0;
    private double x = 0;
    private double y = 0;
    private Rectangle rect;
    private JPanel panel;
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
        this.panel = panel;
        this.g2 = (Graphics2D)panel.getGraphics();

        rect = new Rectangle();
        rect.x = (int)x-15;
        rect.y = (int)y-15;
        rect.setSize(30, 30);
    }

    public double getx(){
        return x;
    }

    public double gety(){
        return y;
    }

    public double getCharge(){
        return charge;
    }

    public Rectangle getRect(){
        return rect;
    }

    public void setx(double x){
        this.x = x;
    }

    public void sety(double y){
        this.y = y;
    }

    public void setCharge(double charge){
        this.charge = charge;
    }

    public void drawCharge(){
        g2.draw(rect);
    }
}