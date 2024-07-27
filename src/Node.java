import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Node extends JButton implements ActionListener {
    Node parent;
    int column;
    int row;
    int gCost;
    int hCost;
    int fCost;
    CellTypeTerrain cellTypeTerrain;
    CellTypeAlgo cellTypeAlgo;
    boolean targetNode = false;


    // Node (aka Cell) Constructor --- --- --- /// --- --- --- /// --- --- --- /// --- --- ---
    public Node(int column, int row) {
        this.column = column;
        this.row = row;
        this.cellTypeTerrain = CellTypeTerrain.EMPTY;
        this.cellTypeAlgo = CellTypeAlgo.NOTHING;

        setBackground(Color.white);
        setForeground(Color.black);

        setText("C-" + column + "| R-" + row);
        addActionListener(this);
    }



    // Setting node type --- --- --- /// --- --- --- /// --- --- --- /// --- --- ---

    // Set a node as Start node
    public void setAsStart(){
        setBackground(CellTypeTerrain.START.color);
        setText("Start");
        cellTypeTerrain = CellTypeTerrain.START;
        gCost = 0;
    }
    // Set a node as Target node
    public void setAsTarget(){
        setBackground(CellTypeTerrain.TARGET.color);
        setText("Target");
        this.targetNode = true;
        cellTypeTerrain = CellTypeTerrain.TARGET;
    }

    public void setCellAs (String providedCellType){
        setBackground(CellTypeTerrain.valueOf(providedCellType).color);
        setText(providedCellType);
        cellTypeTerrain = CellTypeTerrain.valueOf(providedCellType);
    }


    // Setting CellTypeAlgo method --- --- --- /// --- --- --- /// --- --- --- /// --- --- ---

    // open = FOCUS
    public void setAsFocus() {
        cellTypeAlgo = CellTypeAlgo.FOCUS;
        //setBackground(CellTypeAlgo.FOCUS.color);
    }
    public void setAsChecked () {
        if (cellTypeTerrain != CellTypeTerrain.START && cellTypeTerrain != CellTypeTerrain.TARGET) {
            cellTypeAlgo = CellTypeAlgo.CHECKED;
            setBackground(CellTypeAlgo.CHECKED.color);
        }
    }

    public void setAsPath () {
        setBackground(CellTypeAlgo.PATH.color);
    }



    // Click method --- --- --- /// --- --- --- /// --- --- --- /// --- --- ---
    @Override
    public void actionPerformed(ActionEvent e) {

        String clickTypeAsStr = String.valueOf(MenuButton.clickType);
        switch (clickTypeAsStr) {
            case "CLOSE":
                return;
            case "EMPTY":
                {
                  Node clickedButton = (Node)e.getSource();
                  String buttonStr = clickedButton.getText();
                  if (buttonStr.equals("START")) {
                      GlobalSettings.startCellCount--;
                  }else if (buttonStr.equals("TARGET")) {
                      GlobalSettings.targetCellCount--;
                  }else if (buttonStr.equals("ROBOT")) {
                      GlobalSettings.robotCount--;
                  }
                }
                break;
            case "ROBOT":
                if (GlobalSettings.robotCount++ == 0) {
                    setCellAs(clickTypeAsStr);
                } else {
                    System.out.println("Log for action #" + GlobalSettings.logCounter++ + "\nThere is already a robot cell\n--- --- --- /// --- --- --- /// --- --- --- /// --- --- ---");
                    return;
                }
                break;
            case "START":
                if (GlobalSettings.startCellCount++ == 0) {
                    setCellAs(clickTypeAsStr);
                    MapPanel.startNode = MapPanel.nodeMatrix[column][row];
                    MapPanel.currentNode = MapPanel.startNode;
                } else {
                    System.out.println("Log for action #" + GlobalSettings.logCounter++ + "\nThere is already a start cell\n--- --- --- /// --- --- --- /// --- --- --- /// --- --- ---");
                    return;
                }
                break;
            case "TARGET":
                if (GlobalSettings.targetCellCount++ == 0) {
                    setCellAs(clickTypeAsStr);
                    MapPanel.targetNode = MapPanel.nodeMatrix[column][row];
                } else {
                    System.out.println("Log for action #" + GlobalSettings.logCounter++ + "\nThere is already a target cell\n--- --- --- /// --- --- --- /// --- --- --- /// --- --- ---");
                    return;
                }
                break;
        }
        setCellAs(clickTypeAsStr);

        // Print the log of the action
        System.out.println("Log for action #" + GlobalSettings.logCounter++);
        System.out.println("Clicked on cell in column " + column + " row " + row);
        System.out.println("Tried to set cell value to " + cellTypeTerrain);
        System.out.println("Click type was " + MenuButton.clickType);
        System.out.println("--- --- --- /// --- --- --- /// --- --- --- /// --- --- ---");

    }
}
