import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.SwingUtilities;

public class InputHandling{

    public InputHandling(){
        
    }

    public class MouseInputHandler extends MouseAdapter{

        public void mousePressed(MouseEvent e){
            if (SwingUtilities.isLeftMouseButton(e)){
                System.out.println("Left Pressed");
            }
            else if (SwingUtilities.isRightMouseButton(e)){
                System.out.println("Right Pressed");
            }
        }
    }

    public class MouseWheelHandler implements MouseWheelListener{

        @Override
        public void mouseWheelMoved(MouseWheelEvent e){
            int amount = e.getWheelRotation();
            if (amount > 1){
                amount = 1;
            }
            else if (amount < -1){
                amount = -1;
            }
            Window.gridWidth *= (1+0.1*amount);
            if (Window.gridWidth > 200){
                Window.gridWidth = 200;
            }
        }
    }
}