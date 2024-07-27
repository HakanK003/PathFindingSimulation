import java.awt.event.*;
public class KeyHandler implements KeyListener {

    MapPanel mapPanel;

    public KeyHandler (MapPanel mapPanel) {
        this.mapPanel = mapPanel;
    }
    @Override
    public void keyTyped(KeyEvent e) {
        // Nothing
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        System.out.println("Pressed Enter 1");

        if (keyCode == KeyEvent.VK_ENTER) {
            mapPanel.autoSearch();
            System.out.println("Pressed Enter 2");
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Nothing
    }
}
