import javax.swing.*;
import java.awt.*;
public class GlobalFrame extends JFrame {

    public final int screenHeight = GlobalSettings.nodeSize * GlobalSettings.rows_count;
    public final int screenWidth = GlobalSettings.nodeSize * GlobalSettings.columns_count + GlobalSettings.menuWidth;

    public static MapPanel mapPanel;
    GlobalFrame(){
        // Main window settings
        JFrame frame = new JFrame();
        frame.setTitle("Path Finding Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        frame.setSize(screenWidth,screenHeight);

        // Setting main panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Adding map panel to mainPanel
        mapPanel = new MapPanel();
        mainPanel.add(mapPanel, BorderLayout.WEST);

        // Adding menu panel to mainPanel
        mainPanel.add(new MenuPanel(), BorderLayout.EAST);

        // Add the main panel to the frame
        frame.add(mainPanel);

        frame.pack();

        frame.setLocation(null);
    }

}
