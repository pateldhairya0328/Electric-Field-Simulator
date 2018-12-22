import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.SwingUtilities;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class InputHandling{
    int mouseX = 0;
    int mouseY = 0;
    JPopupMenu defaultMenu;
    Object[] addChargeMessage = new Object[6];
    JTextField xPos, yPos, crg;
    JComboBox<String> cbx, cby, cbc;

    public InputHandling(){
        MakeDefaultPopupMenu:{
            defaultMenu = new JPopupMenu();
            JMenuItem item;
            MenuItemListener mList = new MenuItemListener();
            item = new JMenuItem("Add Charge");
            item.setIcon(new ImageIcon("Assets\\Add_Charge.png"));
            item.addActionListener(mList.new Add_Charge());
            defaultMenu.add(item);
            item = new JMenuItem("Reset Charges");
            item.setIcon(new ImageIcon("Assets\\Reset_Charges.png"));
            defaultMenu.add(item);
            item = new JMenuItem("Get Electric Field Strength");
            item.setIcon(new ImageIcon("Assets\\Get_Electric_Field_Strength.png"));
            defaultMenu.add(item);
            
            defaultMenu.add(new JSeparator(SwingConstants.HORIZONTAL));
            
            item = new JMenuItem("Zoom In");
            item.setIcon(new ImageIcon("Assets\\Zoom_In.png"));
            defaultMenu.add(item);
            item = new JMenuItem("Zoom Out");
            item.setIcon(new ImageIcon("Assets\\Zoom_Out.png"));
            defaultMenu.add(item);
            item = new JMenuItem("Set Bounds");
            item.setIcon(new ImageIcon("Assets\\Set_Bounds.png"));
            defaultMenu.add(item);

            defaultMenu.add(new JSeparator(SwingConstants.HORIZONTAL));
            
            item = new JMenuItem("Help");
            item.setIcon(new ImageIcon("Assets\\Help.png"));
            defaultMenu.add(item);
        }

        MakeAddChargeMessage:{
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

    public class MouseInputHandler implements MouseListener{
        @Override
        public void mouseClicked(MouseEvent e){
            if (SwingUtilities.isRightMouseButton(e)){
                defaultMenu.show(e.getComponent(), mouseX, mouseY);
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

        }

        @Override
        public void mouseEntered(MouseEvent e){

        }
    }

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

    public class MenuItemListener{
        public MenuItemListener(){

        }

        public class Add_Charge implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e){
                int opt = JOptionPane.showConfirmDialog(null, addChargeMessage); 
                if (opt == JOptionPane.YES_OPTION){
                    try{
                        double x, y, c;
                        x = Double.parseDouble(xPos.getText())/Window.VAL[cbx.getSelectedIndex()];
                        y = Double.parseDouble(yPos.getText())/Window.VAL[cby.getSelectedIndex()];
                        c = Double.parseDouble(crg.getText())/Window.VAL[cbc.getSelectedIndex()];
                        xPos.setText("");
                        yPos.setText("");
                        crg.setText("");
                        Window.charges.add(new Charge(x,y,c,Window.window));
                        Window.window.repaint();
                    }catch(NumberFormatException ex){};
                }
            }
        }
    }
}