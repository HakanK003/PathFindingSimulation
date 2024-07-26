import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuButton extends JButton implements ActionListener {
    static ClickType clickType = ClickType.CLOSE;
    MenuButton(){
        addActionListener(this);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // Get the button that clicked
        JButton clickedButton = (JButton) e.getSource();

        // Get the text of that button
        String buttonText = clickedButton.getText();
        clickType = ClickType.valueOf(clickedButton.getText().toUpperCase());

        // Print the log of the action
        System.out.println("Clicked to " + buttonText);
    }
}
