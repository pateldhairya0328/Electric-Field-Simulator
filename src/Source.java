import javax.swing.JFrame;

public class Source{
    public static void main(String []args){

        //Creates a new JFrame for the GUI and adds the program's
        //window to it
        Window window = new Window();
        JFrame frame = new JFrame("Electric Field Simulator");
        frame.add(window);
        frame.setVisible(true);
        frame.setExtendedState (JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
    }
}