import java.awt.Cursor;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.SwingUtilities;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;

public class InputHandling{
    int mouseX = 0;
    int mouseY = 0;
    JPopupMenu defaultMenu;
    
    public InputHandling(){
        defaultMenu = new JPopupMenu();
        JMenuItem item;
        item = new JMenuItem("New Charge");
        item.setIcon(new ImageIcon("Assets\\Add_Charge.png"));
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
}