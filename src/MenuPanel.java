import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPanel extends JPanel {
    public final int menuWidth = GlobalSettings.menuWidth;
    public final int buttonHeight = GlobalSettings.buttonHeight;
    public final int buttonWidth = GlobalSettings.buttonWidth;

    JButton [] MenuButtonBarArray = new JButton[GlobalSettings.buttonCount];

    static ClickType clickType = ClickType.WALL;
    MenuPanel(){

        this.setPreferredSize(new Dimension(menuWidth,GlobalSettings.rows_count * GlobalSettings.nodeSize));
        this.setBackground(Color.green);
        //this.setPreferredSize(new Dimension(GlobalSettings.menuWidth, GlobalSettings.));

        JPanel buttonPanel = new JPanel(new GridLayout(3,GlobalSettings.buttonCount));

        for (int i = 0; i < GlobalSettings.buttonCount; i++) {
            MenuButtonBarArray[i] = new MenuButton();
            MenuButtonBarArray[i].setSize(GlobalSettings.buttonWidth, GlobalSettings.buttonHeight);
            MenuButtonBarArray[i].setText(GlobalSettings.buttonTexts[i]);
            buttonPanel.add(MenuButtonBarArray[i]);
        }

        this.add(buttonPanel);

    }
}
