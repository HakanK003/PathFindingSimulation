import javax.swing.*;
import java.awt.*;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class MapPanel extends JPanel {

    // Global Configuration --- --- --- ||| --- --- --- ||| --- --- --- ||| --- --- ---
    public final int columns = GlobalSettings.columns_count;
    public final int rows = GlobalSettings.rows_count;
    public final int nodeSize = GlobalSettings.nodeSize;
    public final int screenWidth = nodeSize * columns;
    public final int screenHeight = nodeSize * rows;

    // Map Configuration --- --- --- ||| --- --- --- ||| --- --- --- ||| --- --- ---
    // Whole Map
    public static Node [][] nodeMatrix = new Node[GlobalSettings.columns_count][GlobalSettings.rows_count];

    // Node Configuration --- --- --- ||| --- --- --- ||| --- --- --- ||| --- --- ---
    // Start, Target, Current, Robot Node
    public static Node startNode, targetNode, currentNode, robotNode;

    // Algorithm Configuration --- --- --- ||| --- --- --- ||| --- --- --- ||| --- --- ---
    // A Star Algorithm Setup
    static ArrayList<Node> focusList = new ArrayList<>();
    ArrayList<Node> checkedList = new ArrayList<>();
    int step = 0;
    static boolean targetReached = false;



    // MapPanel Constructor --- --- --- ||| --- --- --- ||| --- --- --- ||| --- --- ---
    MapPanel(){

        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.blue);
        this.setLayout(new GridLayout(rows,columns));

        // Set Key Listener
        this.addKeyListener(new KeyHandler(this));
        this.setFocusable(true);

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                nodeMatrix[c][r] = new Node (c,r);
                this.add(nodeMatrix[c][r]);
            }
        }

        // set start and end node manually
        setStartNode(14,9);
        setTargetNode(2, 2);

        setInitialCostOnNodes();

    }




    // Setting start and end nodes manually method --- --- --- ||| --- --- --- ||| --- --- --- ||| --- --- ---
    private void setStartNode(int column, int row){
        nodeMatrix[column][row].setAsStart();
        startNode = nodeMatrix[column][row];
        currentNode = startNode;
    }
    private void setTargetNode(int column, int row){
        nodeMatrix[column][row].setAsTarget();
        targetNode = nodeMatrix[column][row];
    }



    // Calculating costs of the cells method --- --- --- ||| --- --- --- ||| --- --- --- ||| --- --- ---
    private void getInitialCost(Node node){

        // Calculate H Cost
        int xDistance = Math.abs(node.column - targetNode.column);
        int yDistance = Math.abs(node.row - targetNode.row);
        node.hCost = (int) (10 * (Math.max(xDistance, yDistance) + Math.min(xDistance, yDistance) * (Math.sqrt(2) - 1)));
        // Calculate G Cost
        node.gCost = 0;
        // Calculate F Cost
        node.fCost = Integer.MAX_VALUE;

        if (node.cellTypeTerrain != CellTypeTerrain.START && node.cellTypeTerrain != CellTypeTerrain.TARGET) {
            node.setText("<html>F:" + node.fCost + "<br>G:" + node.gCost + "<br>H:" + node.hCost + "</html>");
        }
    }
    // Writes costs of the cell on the cell
    public void setInitialCostOnNodes(){
        int c = 0;
        int r = 0;

        while (c < columns && r < rows) {
            getInitialCost(nodeMatrix[c][r]);
            c++;
            if (c == columns) {
                c = 0;
                r++;
            }
        }
    }

    // Change cell type to FOCUS (aka open) --- --- --- ||| --- --- --- ||| --- --- --- ||| --- --- ---

    // focused on an empty cell open = FOCUS
    private void focusNode (Node currentNode, Node newFocusNode, int possibleNewGCost) {
        if (newFocusNode.cellTypeAlgo != CellTypeAlgo.FOCUS && newFocusNode.cellTypeAlgo != CellTypeAlgo.CHECKED && newFocusNode.cellTypeTerrain != CellTypeTerrain.WALL) {

            newFocusNode.setAsFocus();

            //int newGCost = getNewGCost(newFocusNode);
            if (possibleNewGCost < newFocusNode.gCost || newFocusNode.gCost == 0) {
                newFocusNode.gCost = possibleNewGCost;
                newFocusNode.fCost = newFocusNode.gCost + newFocusNode.hCost;
                newFocusNode.setText("<html>F:" + newFocusNode.fCost + "<br>G:" + newFocusNode.gCost + "<br>H:" + newFocusNode.hCost + "</html>");
            }

            newFocusNode.parent = currentNode;
            focusList.add(newFocusNode);
        }
    }

    private static int getNewGCost(Node node) {
        int parentColumn;
        int parentRow;
        int parentGCost;

        if (node.parent == null) {
            parentColumn = startNode.column;
            parentRow = startNode.row;
            parentGCost = 0;
        } else {
            parentColumn = node.parent.column;
            parentRow = node.parent.row;
            parentGCost = node.parent.gCost;
        }

        int xDistance = Math.abs(node.column - parentColumn);
        int yDistance = Math.abs(node.row - parentRow);

        return parentGCost + (int) (10 * (Math.max(xDistance, yDistance) + Math.min(xDistance, yDistance) * (Math.sqrt(2) - 1)));
    }


    // Auto Search for A Star Algo --- --- --- ||| --- --- --- ||| --- --- --- ||| --- --- ---
    public void autoSearch() {
        while (!targetReached) {// && step < 10000
            int c = currentNode.column;
            int r = currentNode.row;

            currentNode.setAsChecked();
            //checkedList.add(currentNode);
            focusList.remove(currentNode);

            // 4 Direction Check
            // Focus the upper cell if there is one
            if (r - 1 > -1)
                focusNode(currentNode, nodeMatrix[c][r - 1], currentNode.gCost + 10);
            // Focus the left cell if there is one
            if (c - 1 > -1)
                focusNode(currentNode, nodeMatrix[c - 1][r], currentNode.gCost + 10);
            // Focus the down cell if there is one
            if (r + 1 < rows)
                focusNode(currentNode, nodeMatrix[c][r + 1], currentNode.gCost + 10);
            // Focus the right cell if there is one
            if (c + 1 < columns)
                focusNode(currentNode, nodeMatrix[c + 1][r], currentNode.gCost + 10);

//            // * Direction Check (+4)
//            // Focus the upper-left cell if there is one
//            if (r - 1 > -1 && c - 1 > -1 && !(nodeMatrix[c - 1][r].cellTypeTerrain == CellTypeTerrain.WALL && nodeMatrix[c][r - 1].cellTypeTerrain == CellTypeTerrain.WALL))
//                focusNode(currentNode, nodeMatrix[c - 1][r - 1], currentNode.gCost + 14);
//            // Focus the down-left cell if there is one
//            if (r + 1 < rows && c - 1 > -1 && !(nodeMatrix[c - 1][r].cellTypeTerrain == CellTypeTerrain.WALL && nodeMatrix[c][r + 1].cellTypeTerrain == CellTypeTerrain.WALL))
//                focusNode(currentNode, nodeMatrix[c - 1][r + 1], currentNode.gCost + 14);
//            // Focus the upper-right cell if there is one
//            if (r - 1 > -1 && c + 1 < columns && !(nodeMatrix[c + 1][r].cellTypeTerrain == CellTypeTerrain.WALL && nodeMatrix[c][r - 1].cellTypeTerrain == CellTypeTerrain.WALL))
//                focusNode(currentNode, nodeMatrix[c + 1][r - 1], currentNode.gCost + 14);
//            // Focus the down-right cell if there is one
//            if (r + 1 < rows && c + 1 < columns && !(nodeMatrix[c + 1][r].cellTypeTerrain == CellTypeTerrain.WALL && nodeMatrix[c][r + 1].cellTypeTerrain == CellTypeTerrain.WALL))
//                focusNode(currentNode, nodeMatrix[c + 1][r + 1], currentNode.gCost + 14);

            // Find the best cell
            int bestNodeIndex = 0;
            int bestNodeFCost = Integer.MAX_VALUE;

            for (int i = 0; i < focusList.size(); i++) {
                // Check if this node's F cost is lower
                if (focusList.get(i).fCost < bestNodeFCost) {
                    bestNodeIndex = i;
                    bestNodeFCost = focusList.get(i).fCost;
                } else if (focusList.get(i).fCost == bestNodeFCost) {
                    // IF F cost are same check min G cost
                    if (focusList.get(i).gCost < focusList.get(bestNodeIndex).gCost) {
                        bestNodeIndex = i;
                    }
                }
            }
            System.out.println("Evaluated another node " + step++);
            // After the loop we have next step cell
            currentNode = focusList.get(bestNodeIndex);
            //currentNode.cellType == CellType.TARGET
            if (currentNode.targetNode) {
                targetReached = true;
                trackThePath();
            }
            //step++;
        }

        System.out.println("Searched and found a new path");


//        Robot robot = new Robot(startNode);
//
//        while (robot.currentCell.cellTypeTerrain != CellTypeTerrain.TARGET) {
//            robot.goNextOnPath();
//        }

    }

    // Backtrack the best path --- --- --- ||| --- --- --- ||| --- --- --- ||| --- --- ---

    private void trackThePath () {
        Node current = targetNode;

        while (current != startNode) {
            current = current.parent;
            if (current != startNode) {
                current.setAsPath();
            }
        }
    }

//    // Start robot first version
//    public void startRobot(){
//        Robot robot = new Robot(startNode);
//
//        while (robot.currentCell.cellTypeTerrain != CellTypeTerrain.TARGET) {
//            robot.goNextOnPath();
//        }
//    }

    // Start robot second (easy version)
    public void startRobot(){
        Robot robot = new Robot(startNode);
        if (robot.currentCell.cellTypeTerrain != CellTypeTerrain.TARGET) {
            return;
        } else {
            robot.goNextOnPath();
        }
    }

    static public void resetAlgoCellType (Node newStartNode) {
        startNode = newStartNode;
        for (int r = 0; r < GlobalSettings.rows_count; r++) {
            for (int c = 0; c < GlobalSettings.columns_count; c++) {
                nodeMatrix[c][r].parent = null;
                if (nodeMatrix[c][r].cellTypeAlgo == CellTypeAlgo.FOCUS || nodeMatrix[c][r].cellTypeAlgo == CellTypeAlgo.CHECKED ||nodeMatrix[c][r].cellTypeAlgo == CellTypeAlgo.PATH){
                    nodeMatrix[c][r].cellTypeAlgo = CellTypeAlgo.NOTHING;
                    nodeMatrix[c][r].setBackground(CellTypeAlgo.NOTHING.color);
                }

                if (nodeMatrix[c][r].cellTypeTerrain == CellTypeTerrain.WALL){
                    nodeMatrix[c][r].setBackground(CellTypeTerrain.WALL.color);
                }
            }
        }
        startNode.setBackground(CellTypeTerrain.START.color);
        targetNode.setBackground(CellTypeTerrain.TARGET.color);
        System.out.println("Reset map\n--------------------------------------------------------------------------");
    }

//    // Manual version
//    public void search() {
//        if (!targetReached) {
//            int c = currentNode.column;
//            int r = currentNode.row;
//
//            currentNode.setAsChecked();
//            checkedList.add(currentNode);
//            focusList.remove(currentNode);
//
//            // Focus the upper cell if there is one
//            if (r - 1 > -1)
//                focusNode(nodeMatrix[c][r - 1]);
//            // Focus the left cell if there is one
//            if (c - 1 > -1)
//                focusNode(nodeMatrix[c - 1][r]);
//            // Focus the down cell if there is one
//            if (r + 1 < rows)
//                focusNode(nodeMatrix[c][r + 1]);
//            // Focus the right cell if there is one
//            if (c + 1 < columns)
//                focusNode(nodeMatrix[c + 1][r]);
//
//
//            // Find the best cell
//            int bestNodeIndex = 0;
//            int bestNodeFCost = Integer.MAX_VALUE;
//
//            for (int i = 0; i < focusList.size(); i++) {
//                // Check if this node's F cost is lower
//                if (focusList.get(i).fCost < bestNodeFCost) {
//                    bestNodeIndex = i;
//                    bestNodeFCost = focusList.get(i).fCost;
//                } else if (focusList.get(i).fCost == bestNodeFCost) {
//                    // IF F cost are same check min G cost
//                    if (focusList.get(i).gCost < focusList.get(bestNodeIndex).gCost) {
//                        bestNodeIndex = i;
//                    }
//                }
//            }
//
//            // After the loop we have next step cell
//            currentNode = focusList.get(bestNodeIndex);
//            //currentNode.cellType == CellType.TARGET
//            if (currentNode.targetNode) {
//                targetReached = true;
//            }
//        }
//    }



}
