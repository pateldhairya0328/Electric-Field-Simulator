package efield;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.util.Scanner;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class InputHandling{
    int mouseX = 0;
    int mouseY = 0;
    JPopupMenu defaultMenu;
    Object[] addChargeMessage = new Object[6];
    JTextField xPos, yPos, crg;
    JComboBox<String> cbx, cby, cbc;
    int chargeToChange;

    /**
     * Makes the Popup Menu and Message for adding charge
     * Done here because it would be slower for the program to 
     * Create the menu every time the user right clicked. Instead,
     * the menu is only made visible or not for speed (but also
     * uses more RAM consitently). Same reason for why adding charge
     * message is also made here.
     */
    public InputHandling(){
        //Make Default Charge Popup Menu
        {
            defaultMenu = new JPopupMenu();
            JMenuItem item;
            MenuItemListener mList = new MenuItemListener();
            item = new JMenuItem("Add Charge");
            item.setIcon(new ImageIcon("Assets\\Add_Charge.png"));
            item.addActionListener(mList.new Add_Charge());
            defaultMenu.add(item);
            item = new JMenuItem("Reset Charges");
            item.setIcon(new ImageIcon("Assets\\Reset_Charges.png"));
            item.addActionListener(mList.new Reset_Charges());
            defaultMenu.add(item);
            item = new JMenuItem("Get Electric Field Strength");
            item.setIcon(new ImageIcon("Assets\\Get_Electric_Field_Strength.png"));
            item.addActionListener(mList.new Get_Electric_Field_Strength());
            defaultMenu.add(item);
            
            defaultMenu.add(new JSeparator(SwingConstants.HORIZONTAL));
            
            item = new JMenuItem("Zoom In");
            item.setIcon(new ImageIcon("Assets\\Zoom_In.png"));
            item.addActionListener(mList.new Zoom_In());
            defaultMenu.add(item);
            item = new JMenuItem("Zoom Out");
            item.setIcon(new ImageIcon("Assets\\Zoom_Out.png"));
            item.addActionListener(mList.new Zoom_Out());
            defaultMenu.add(item);
            item = new JMenuItem("Set Bounds");
            item.setIcon(new ImageIcon("Assets\\Set_Bounds.png"));
            item.addActionListener(mList.new Set_Bounds());
            defaultMenu.add(item);            
            item = new JMenuItem("Hide Grid");
            item.setIcon(new ImageIcon("Assets\\Hide_Grid.png"));
            item.addActionListener(mList.new Change_Grid());
            defaultMenu.add(item);
            
            defaultMenu.add(new JSeparator(SwingConstants.HORIZONTAL));

            item = new JMenuItem("Set Precision");
            item.setIcon(new ImageIcon("Assets\\Set_Precision.png"));
            item.addActionListener(mList.new Set_Precision());
            defaultMenu.add(item);
            
            defaultMenu.add(new JSeparator(SwingConstants.HORIZONTAL));

            item = new JMenuItem("Help");
            item.setIcon(new ImageIcon("Assets\\Help.png"));
            item.addActionListener(mList.new Help());
            defaultMenu.add(item);

            JButton b = new JButton("Help");
            b.addActionListener(mList.new Help());
            Window.window.setLayout(new FlowLayout(FlowLayout.RIGHT));
            Window.window.add(b);
        }

        //Make Message to display for Adding Charge
        {
            Insets insets = new Insets(5,5,0,0);
            JPanel message = new JPanel(new GridBagLayout());
            GridBagConstraints c;

            xPos = new JTextField();
            c = new GridBagConstraints(0, 0, 1, 1, 10, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0);
            message.add(xPos, c);
            cbx = new JComboBox<String>(Window.UNIT);
            c = new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0);
            message.add(cbx, c);
            addChargeMessage[0] = "Enter x position and select unit";
            addChargeMessage[1] = message;

            message = new JPanel(new GridBagLayout());
            yPos = new JTextField();
            c = new GridBagConstraints(0, 0, 1, 1, 10, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0);
            message.add(yPos, c);
            cby = new JComboBox<String>(Window.UNIT);
            c = new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0);
            message.add(cby, c);
            addChargeMessage[2] = "Enter y position and select unit";
            addChargeMessage[3] = message;
            
            message = new JPanel(new GridBagLayout());
            crg = new JTextField();
            c = new GridBagConstraints(0, 0, 1, 1, 10, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0);
            message.add(crg, c);
            cbc = new JComboBox<String>(Window.CUNIT);
            c = new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0);
            message.add(cbc, c);
            addChargeMessage[4] = "Charge and select unit";
            addChargeMessage[5] = message;
        }

    }

    /**
     * Listener for the mouse buttons
     */
    public class MouseInputHandler implements MouseListener{
        @Override
        public void mouseClicked(MouseEvent e){
            if (SwingUtilities.isRightMouseButton(e)){
                boolean flag = true;
                for (int i = 0; i < Window.charges.size(); ++i){
                    chargeToChange = i;
                    Charge c = Window.charges.get(i);
                    if (c.getRect().contains(mouseX, mouseY)){
                        JPopupMenu chargeInfo = new JPopupMenu();
                        chargeInfo.add(new JMenuItem("Charge: "+c.getCharge()));
                        chargeInfo.add(new JMenuItem("(x, y) = ("+c.getx()+", "+c.gety()+")"));

                        chargeInfo.add(new JSeparator(JSeparator.HORIZONTAL));
                        
                        JMenuItem item = new JMenuItem("Delete Charge");
                        item.addActionListener(new MenuItemListener().new Delete_Charge());
                        chargeInfo.add(item);
                        item = new JMenuItem("Modify Charge");
                        item.addActionListener(new MenuItemListener().new Modify_Charge());
                        chargeInfo.add(item);
                        
                        chargeInfo.show(e.getComponent(), mouseX, mouseY);
                        flag = false;
                        break;
                    }
                }
                if (flag){
                    defaultMenu.show(e.getComponent(), mouseX, mouseY);
                }
            }
            else if (SwingUtilities.isLeftMouseButton(e)){
                Window.window.getEFieldValue(
                    Window.xStep*(e.getX()-Window.xOffset-Window.frameWidth/2)/Window.intervalX,
                    Window.yStep*(-e.getY()+Window.yOffset+Window.frameHeight/2)/Window.intervalY,
                    true);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e){
            Window.cursor = Cursor.DEFAULT_CURSOR;
            e.getComponent().repaint();
        }

        @Override
        public void mouseExited(MouseEvent e){
        }

        @Override
        public void mousePressed(MouseEvent e){
            for (Charge c: Window.charges){
                if (c.getRect().contains(mouseX, mouseY)){
                    Graphics2D g2 = (Graphics2D)Window.window.getGraphics();
                    g2.setFont(new Font("Times New Roman", Font.PLAIN, 20));
                    g2.setColor(new Color(240, 240, 240));
                    g2.fillRect(mouseX+15, mouseY - 20, 10 + g2.getFontMetrics().stringWidth("(x, y) = ("+c.getx()+", "+c.gety()+")"), 50);
                    g2.setColor(Color.LIGHT_GRAY);
                    g2.drawRect(mouseX+15, mouseY - 20, 10 + g2.getFontMetrics().stringWidth("(x, y) = ("+c.getx()+", "+c.gety()+")"), 50);
                    g2.setColor(Color.BLACK);
                    g2.drawString("Charge: "+c.getCharge(), mouseX + 20, mouseY);
                    g2.drawString("(x, y) = ("+c.getx()+", "+c.gety()+")", mouseX + 20, mouseY + 20);
                    break;
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e){
        }
    }

    /**
     * Listener for when the mouse wheel is moved (scrolling)
     */
    public class MouseWheelHandler implements MouseWheelListener{
        @Override
        public void mouseWheelMoved(MouseWheelEvent e){
            int amount = 0;
            if (e.getWheelRotation() < 0){
                amount = 1;
            }
            else{
                amount = -1;
            }
            
            double ini, fin;

            //makes the window zoom into or out of the place the cursor is hovering
            //this is done by zooming the window on either side of the cursor proportionally
            //to how much of the window is on that side of the cursor. This way, even when 
            //the window zooms in or out, the place the cursor is hovering stays unchanged
            //effectively making that the focus of the zoom
            if ((Window.xStep >= 20 || Window.yStep >= 20) && amount < 0){
                amount = 0;
            }
            if ((Window.xStep <= 1e-10 || Window.yStep <= 1e-10) && amount > 0){
                amount = 0;
            }

            ini = Window.xStep*(0.1*amount*e.getX()-Window.xOffset-Window.frameWidth/2)/Window.intervalX;
            fin = Window.xStep*(-0.1*amount*(Window.frameWidth-e.getX())-Window.xOffset+Window.frameWidth/2)/Window.intervalX;
            Window.xOffset = (ini*Window.frameWidth)/(ini-fin)-Window.frameWidth/2;
            Window.gridWidth = fin-ini;

            ini = Window.yStep*(0.1*amount*e.getY()-Window.yOffset-Window.frameHeight/2)/Window.intervalY;
            fin = Window.yStep*(-0.1*amount*(Window.frameHeight-e.getY())-Window.yOffset+Window.frameHeight/2)/Window.intervalY;
            Window.yOffset = (ini*Window.frameHeight)/(ini-fin)-Window.frameHeight/2;
            Window.gridHeight = fin-ini;

            e.getComponent().repaint();
        }
    }

    /**
     * Listener for when the mouse is moved
     */
    public class MouseMoveHandler implements MouseMotionListener{
        @Override
        public void mouseDragged(MouseEvent e){
            Window.xOffset += (e.getX()-mouseX);
            Window.yOffset += (e.getY()-mouseY);
            mouseX = e.getX();
            mouseY = e.getY();
            Window.cursor = Cursor.MOVE_CURSOR;
            
            e.getComponent().repaint();
        }

        @Override
        public void mouseMoved(MouseEvent e){
            mouseX = e.getX();
            mouseY = e.getY();
            Window.mouseX = mouseX;
            Window.mouseY = mouseY;
        }
    }

    /**
     * Action listeners for each option in the menu
     */
    public class MenuItemListener{
        public MenuItemListener(){
        }

        public class Add_Charge implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e){
                Object[] options = {"Add Charge", "Cancel"};
                int opt = JOptionPane.showOptionDialog(null, addChargeMessage, "Add Charge", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]); 
                if (opt == JOptionPane.YES_OPTION){
                    try{
                        double x, y, c;
                        x = Double.parseDouble(xPos.getText())/Window.VAL[cbx.getSelectedIndex()];
                        y = Double.parseDouble(yPos.getText())/Window.VAL[cby.getSelectedIndex()];
                        c = Double.parseDouble(crg.getText())/Window.VAL[cbc.getSelectedIndex()];
                        if (Math.abs(c)>Window.maxCharge){
                            Window.maxCharge = Math.abs(c);
                        }
                        xPos.setText("");
                        yPos.setText("");
                        crg.setText("");
                        Window.charges.add(new Charge(x,y,c,Window.window));
                        Window.window.repaint();
                    }catch(NumberFormatException ex){
                        JOptionPane.showMessageDialog(null, "Error:\n"+ex.getMessage()+"\nTry entering a valid number.");
                    };
                }
            }
        }

        public class Reset_Charges implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e){
                Window.charges.clear();
                Window.maxCharge = 0;
                Window.window.repaint();
            }
        }

        public class Delete_Charge implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e){
                Window.charges.remove(chargeToChange);
                Window.findMax();
                Window.window.repaint();
            }
        }

        public class Modify_Charge implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e){
                Object[] options = {"Modify Charge", "Cancel"};
                int opt = JOptionPane.showOptionDialog(null, addChargeMessage, "Modify Charge", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]); 
                if (opt == JOptionPane.YES_OPTION){
                    try{
                        double x, y, c;
                        x = Double.parseDouble(xPos.getText())/Window.VAL[cbx.getSelectedIndex()];
                        y = Double.parseDouble(yPos.getText())/Window.VAL[cby.getSelectedIndex()];
                        c = Double.parseDouble(crg.getText())/Window.VAL[cbc.getSelectedIndex()];
                        if (Math.abs(c)>Window.maxCharge){
                            Window.maxCharge = Math.abs(c);
                        }
                        xPos.setText("");
                        yPos.setText("");
                        crg.setText("");
                        Window.charges.set(chargeToChange, new Charge(x,y,c,Window.window));
                        Window.findMax();
                        Window.window.repaint();
                    }catch(NumberFormatException ex){
                        JOptionPane.showMessageDialog(null, "Error:\n"+ex.getMessage()+"\nTry entering a valid number.");
                    };
                }
            }
        }

        public class Zoom_In implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e){
                double ini, fin;

                //makes the window zoom into the center of the screen
                if (Window.xStep <= 1e-10 || Window.yStep <= 1e-10){
                    return;
                }

                double w = Window.frameWidth/2;
                double h = Window.frameHeight/2;
                ini = Window.xStep*(-0.75*w-Window.xOffset)/Window.intervalX;
                fin = Window.xStep*(0.75*w-Window.xOffset)/Window.intervalX;
                Window.xOffset = (ini*2*w)/(ini-fin)-w;
                Window.gridWidth = fin-ini;

                ini = Window.yStep*(-0.75*h-Window.yOffset)/Window.intervalY;
                fin = Window.yStep*(0.75*h-Window.yOffset)/Window.intervalY;
                Window.yOffset = (ini*2*h)/(ini-fin)-h;
                Window.gridHeight = fin-ini;

                Window.window.repaint();
            }
        }

        public class Zoom_Out implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e){
                double ini, fin;

                //makes the window zoom into the center of the screen
                if (Window.xStep <= 1e-10 || Window.yStep <= 1e-10){
                    return;
                }

                double w = Window.frameWidth/2;
                double h = Window.frameHeight/2;
                ini = Window.xStep*(-1.25*w-Window.xOffset)/Window.intervalX;
                fin = Window.xStep*(1.25*w-Window.xOffset)/Window.intervalX;
                Window.xOffset = (ini*2*w)/(ini-fin)-w;
                Window.gridWidth = fin-ini;

                ini = Window.yStep*(-1.25*h-Window.yOffset)/Window.intervalY;
                fin = Window.yStep*(1.25*h-Window.yOffset)/Window.intervalY;
                Window.yOffset = (ini*2*h)/(ini-fin)-h;
                Window.gridHeight = fin-ini;

                Window.window.repaint();
            }
        }

        public class Set_Bounds implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e){
                Object[] arr = new Object[8];
                JTextField xLeft, xRight, yTop, yBot;
                JComboBox<String> uLeft, uRight, uTop, uBot;
                //Make message to display
                {
                    Insets insets = new Insets(5,5,0,0);
                    JPanel mess = new JPanel(new GridBagLayout());
                    GridBagConstraints c;

                    xLeft = new JTextField();
                    c = new GridBagConstraints(0, 0, 1, 1, 10, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0);
                    mess.add(xLeft, c);
                    uLeft = new JComboBox<String>(Window.UNIT);
                    c = new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0);
                    mess.add(uLeft, c);
                    arr[0] = "Enter Left x bound and unit";
                    arr[1] = mess;
                    
                    mess = new JPanel(new GridBagLayout());
                    xRight = new JTextField();
                    c = new GridBagConstraints(0, 0, 1, 1, 10, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0);
                    mess.add(xRight, c);
                    uRight = new JComboBox<String>(Window.UNIT);
                    c = new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0);
                    mess.add(uRight, c);
                    arr[2] = "Enter Right x bound and unit";
                    arr[3] = mess;
                    
                    mess = new JPanel(new GridBagLayout());
                    yTop = new JTextField();
                    c = new GridBagConstraints(0, 0, 1, 1, 10, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0);
                    mess.add(yTop, c);
                    uTop = new JComboBox<String>(Window.UNIT);
                    c = new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0);
                    mess.add(uTop, c);
                    arr[4] = "Enter Top y bound and unit";
                    arr[5] = mess;
                    
                    mess = new JPanel(new GridBagLayout());
                    yBot = new JTextField();
                    c = new GridBagConstraints(0, 0, 1, 1, 10, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0);
                    mess.add(yBot, c);
                    uBot = new JComboBox<String>(Window.UNIT);
                    c = new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0);
                    mess.add(uBot, c);
                    arr[6] = "Enter Bottom y bound and unit";
                    arr[7] = mess;
                }

                Object[] options = {"Set Bounds", "Cancel"};
                int opt = JOptionPane.showOptionDialog(null, arr, "Set Bounds", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]); 
                
                if (opt == JOptionPane.YES_OPTION){
                    try{
                        double iniX = Double.parseDouble(xLeft.getText());
                        double iniY = Double.parseDouble(yTop.getText());
                        double finX = Double.parseDouble(xRight.getText());
                        double finY = Double.parseDouble(yBot.getText());
                        if (finX < iniX){
                            JOptionPane.showMessageDialog(null, "Error:\nThe right x bound cannot be less than the left x bound.");
                            return;
                        }
                        else if (finY > iniY){
                            JOptionPane.showMessageDialog(null, "Error:\nThe top x bound cannot be less than the botton x bound.");
                            return;
                        }

                        Window.xOffset = (iniX*Window.frameWidth)/(iniX-finX)-Window.frameWidth/2;
                        Window.yOffset = (iniY*Window.frameHeight)/(iniY-finY)-Window.frameHeight/2;
                        Window.gridWidth = finX-iniX;
                        Window.gridHeight = iniY-finY;
                    }catch(NumberFormatException ex){
                        JOptionPane.showMessageDialog(null, "Error:\n"+ex.getMessage()+"\nTry entering a valid number.");
                    }
                }

                Window.window.repaint();
            }
        }

        public class Change_Grid implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e){
                defaultMenu.remove(7);
                if (Window.showGrid){
                    Window.showGrid = false;
                    JMenuItem item = new JMenuItem("Show Grid");
                    item.setIcon(new ImageIcon("Assets\\Show_Grid.png"));
                    item.addActionListener(new Change_Grid());
                    defaultMenu.add(item, 7);
                }
                else{
                    Window.showGrid = true;
                    JMenuItem item = new JMenuItem("Hide Grid");
                    item.setIcon(new ImageIcon("Assets\\Hide_Grid.png"));
                    item.addActionListener(new Change_Grid());
                    defaultMenu.add(item, 7);
                }
                Window.window.repaint();
            }
        }

        public class Get_Electric_Field_Strength implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e){
                JTextField xPos = new JTextField();
                JTextField yPos = new JTextField();
                Object[] m = {"Enter x position of location", xPos, "Enter y position of location", yPos};
                Object[] options = {"Yes", "Cancel"};
                int opt = JOptionPane.showOptionDialog(null, m, "Coordinates for electric field strength", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (opt == JOptionPane.YES_OPTION){
                    try{
                        String ef = Window.window.getEFieldValue(Double.parseDouble(xPos.getText()), Double.parseDouble(yPos.getText()), false);
                        JOptionPane.showMessageDialog(null, ef);
                    }catch (NumberFormatException ex){
                        JOptionPane.showMessageDialog(null, "Error:\n"+ex.getMessage()+"\nTry entering a valid number.");                        
                    }
                }
            }
        }

        public class Set_Precision implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e){
                double precision = 0;
                try{
                    precision = Double.parseDouble(JOptionPane.showInputDialog(null, "Enter Number of digits after decimal", "Set Precision", JOptionPane.QUESTION_MESSAGE));
                }catch(NumberFormatException ex){
                    JOptionPane.showMessageDialog(null, "Error:\n"+ex.getMessage()+"\nTry entering a valid number.");
                }
                if (precision > 0){
                    Window.df2.setMaximumFractionDigits(0);
                }
                else{
                    JOptionPane.showMessageDialog(null, "Error:\nTry entering a non-negative number.");                    
                }
            }
        }

        public class Help implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e){                
                JEditorPane ep = new JEditorPane();
                String helpText = "";
                try{
                    Scanner scn = new Scanner(new File("Assets\\helpText.txt"));
                    helpText = scn.useDelimiter("\\Z").next();
                    scn.close();
                }catch(IOException ex){
                    System.err.println(ex.getMessage());
                }
                ep.setContentType("text/html");
                ep.setText(helpText);
                ep.setBackground(null);
                ep.setEditable(false);
                ep.setCaretPosition(0);
                JScrollPane sp = new JScrollPane(ep);
                sp.setPreferredSize(new Dimension(500, 500));
                sp.setBorder(null);
                JOptionPane.showMessageDialog(null, sp);
            }
        }
    }
}