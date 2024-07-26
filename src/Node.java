import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

public class Node extends JButton implements ActionListener {
    Node parent;
    int column;
    int row;
    int gCost;
    int hCost;
    int fCost;
    CellType cellType;




    public Node(int column, int row) {
        this.column = column;
        this.row = row;
        this.cellType = CellType.EMPTY;

        setBackground(Color.white);
        setForeground(Color.black);

        addActionListener(this);
    }

    // To Set What happens after clicking on the node
//    public void setClickType(ClickType clickType){
//        Node.clickType = clickType;
//    }

    // Set a node as Start node
    public void setAsStart(){
        setBackground(CellType.START.color);
        setText("Start");
        cellType = CellType.START;
    }
    // Set a node as Target node
    public void setAsTarget(){
        setBackground(CellType.TARGET.color);
        setText("Target");
        cellType = CellType.TARGET;
    }

    public void setCellAs (String providedCellType){
        setBackground(CellType.valueOf(providedCellType).color);
        setText(providedCellType);
        cellType = CellType.valueOf(providedCellType);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String clickTypeAsStr = String.valueOf(MenuButton.clickType);
        if (clickTypeAsStr.equals("CLOSE")){
            return;
        }

        setCellAs(clickTypeAsStr);
//        this.cellType = CellType.valueOf(clickTypeAsStr);
//
//        setBackground(cellType.color);

        // Print the log of the action
        System.out.println("Tried to set cell value to " + cellType);// + clickTypeAsStr);
        System.out.println("Click type was " + MenuButton.clickType);// + clickTypeAsStr);
    }
}
