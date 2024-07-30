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
        //setText("Start");
        cellTypeTerrain = CellTypeTerrain.START;
        gCost = 0;
        setText("<html>F:" + this.fCost + "<br>G:" + this.gCost + "<br>H:" + this.hCost + "</html>");

    }
    // Set a node as Target node
    public void setAsTarget(){
        setBackground(CellTypeTerrain.TARGET.color);
        //setText("Target");
        setText("<html>F:" + this.fCost + "<br>G:" + this.gCost + "<br>H:" + this.hCost + "</html>");

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
    public void setTypeAsFocus() {
        cellTypeAlgo = CellTypeAlgo.FOCUS;
        //setBackground(CellTypeAlgo.FOCUS.color);
    }
    public void setAsChecked() {
        if (cellTypeTerrain != CellTypeTerrain.START && cellTypeTerrain != CellTypeTerrain.TARGET) {
            cellTypeAlgo = CellTypeAlgo.CHECKED;
            setBackground(CellTypeAlgo.CHECKED.color);
        }
    }
    public void setTypeAsPath() {
        setBackground(CellTypeAlgo.PATH.color);
        cellTypeAlgo = CellTypeAlgo.PATH;
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
                    MapPanel.startNode = MapPanel.nodeMatrices[column][row];
                    MapPanel.currentNode = MapPanel.startNode;
                } else {
                    System.out.println("Log for action #" + GlobalSettings.logCounter++ + "\nThere is already a start cell\n--- --- --- /// --- --- --- /// --- --- --- /// --- --- ---");
                    return;
                }
                break;
            case "TARGET":
                if (GlobalSettings.targetCellCount++ == 0) {
                    setCellAs(clickTypeAsStr);
                    MapPanel.targetNode = MapPanel.nodeMatrices[column][row];
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



    // New A Star method --- --- --- /// --- --- --- /// --- --- --- /// --- --- ---

    // Calculate H Cost
    public void calculateHeuristic(Node finalNode) {
        this.hCost = Math.abs(finalNode.getRow() - getRow()) + Math.abs(finalNode.getColumn() - getColumn());
    }

    public void setNodeData(Node currentNode, int cost) {
        int gCost = currentNode.getGCost() + cost;
        setParent(currentNode);
        setGCost(gCost);
        calculateFinalCost();
    }


    public boolean checkBetterPath(Node currentNode, int cost) {
        int gCost = currentNode.getGCost() + cost;
        if (gCost < getGCost()) {
            setNodeData(currentNode, cost);
            return true;
        }
        return false;
    }

    private void calculateFinalCost() {
        int finalCost = getGCost() + getHCost();
        setFCost(finalCost);
    }



    public int getHCost() {
        return hCost;
    }

    public void setHCost(int hCost) {
        this.hCost = hCost;
    }

    public int getGCost() {
        return gCost;
    }

    public void setGCost(int gCost) {
        this.gCost = gCost;
    }

    public int getFCost() {
        return fCost;
    }

    public void setFCost(int fCost) {
        this.fCost = fCost;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public boolean isBlock() {
        return cellTypeTerrain == CellTypeTerrain.WALL;
    }

    public void setBlock(boolean isBlock) {
        this.cellTypeTerrain = CellTypeTerrain.WALL;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setCol(int column) {
        this.column = column;
    }
}
