import java.awt.event.*;
public class KeyHandler implements KeyListener {

    MapPanel mapPanel;
    Robot robot;

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
        System.out.println("Pressed a key 1");

        if (keyCode == KeyEvent.VK_ENTER) {
            mapPanel.autoSearch();
            System.out.println("Pressed Enter 2");
        }

        if (keyCode == KeyEvent.VK_CAPS_LOCK) {
            System.out.println("Pressed Caps Lock 3");
            //mapPanel.startRobot();
            if (robot.currentCell.cellTypeTerrain != CellTypeTerrain.TARGET) {
                robot.goNextOnPath();
            }


        }

        if (keyCode == KeyEvent.VK_0) {
            System.out.println("Pressed 0 4");
            robot = new Robot(MapPanel.startNode);
            //mapPanel.startRobot();
            if (robot.currentCell.cellTypeTerrain != CellTypeTerrain.TARGET) {
                robot.goNextOnPath();
            }


        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Nothing
    }
}
