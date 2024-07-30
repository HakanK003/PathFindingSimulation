import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class MapPanel extends JPanel {

    // Global Configuration --- --- --- ||| --- --- --- ||| --- --- --- ||| --- --- ---
    public final int columns = GlobalSettings.columns_count;
    public final int rows = GlobalSettings.rows_count;
    public final int nodeSize = GlobalSettings.nodeSize;
    public final int screenWidth = nodeSize * columns;
    public final int screenHeight = nodeSize * rows;

    // Map Configuration --- --- --- ||| --- --- --- ||| --- --- --- ||| --- --- ---
    // Whole Map
    public static Node[][] nodeMatrices = new Node[GlobalSettings.columns_count][GlobalSettings.rows_count];

    // Node Configuration --- --- --- ||| --- --- --- ||| --- --- --- ||| --- --- ---
    // Start, Target, Current, Robot Node
    public static Node startNode, targetNode, currentNode, robotNode;

    // Algorithm Configuration --- --- --- ||| --- --- --- ||| --- --- --- ||| --- --- ---
    // A Star Algorithm Setup
    //ArrayList<Node> focusList = new ArrayList<>();
    PriorityQueue<Node> focusList = new PriorityQueue<Node>(new Comparator<Node>() {
        @Override
        public int compare(Node node0, Node node1) {
            return Integer.compare(node0.getFCost(), node1.getFCost());
        }
    });

    private Set<Node> closedSet = new HashSet<>();
    int step = 0;
    boolean targetReached = false;



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
                nodeMatrices[c][r] = new Node(c,r);
                this.add(nodeMatrices[c][r]);
            }
        }

        // set start and end node manually
        setStartNode(14,9);
        setTargetNode(2, 2);


//        AStar aStar = new AStar(rows, columns, nodeMatrices[14][9], nodeMatrices[2][2]);
//
//        List<Node> path = aStar.findPath();
//        for (Node node : path) {
//            System.out.println(node);
//        }

        startNode.gCost = 0;
        setCostOnNodes();

    }




    // Setting start and end nodes manually method --- --- --- ||| --- --- --- ||| --- --- --- ||| --- --- ---
    private void setStartNode(int column, int row){
        nodeMatrices[column][row].setAsStart();
        startNode = nodeMatrices[column][row];
        currentNode = startNode;
        startNode.gCost = 0;
        startNode.fCost = 0;
    }
    private void setTargetNode(int column, int row){
        nodeMatrices[column][row].setAsTarget();
        targetNode = nodeMatrices[column][row];
    }



    // Calculating costs of the cells method --- --- --- ||| --- --- --- ||| --- --- --- ||| --- --- ---
    private void setInitialCosts(Node node){
//        // Calculate G Cost
//        int xDistance = Math.abs(node.column - startNode.column);
//        int yDistance = Math.abs(node.row - startNode.row);
//        //node.gCost = (int) (10 * Math.sqrt(xDistance*xDistance + yDistance*yDistance));
//        node.gCost = (int) (10 * (Math.max(xDistance, yDistance) + Math.min(xDistance, yDistance) * (Math.sqrt(2) - 1)));
        // Default G cost
        node.gCost = 1;

        //Calculate H cost new
//        public static double calculateHCost(Node currentNode, Node targetNode) {
//            int distanceX = Math.abs(targetNode.column - currentNode.column);
//            int distanceY = Math.abs(targetNode.row - currentNode.row);
//            return Math.max(distanceX, distanceY) + Math.min(distanceX, distanceY) * (Math.sqrt(2) - 1);
//        }

        // Calculate H Cost
        //Calculate H cost
        int xDistance = Math.abs(node.column - targetNode.column);
        int yDistance = Math.abs(node.row - targetNode.row);
        node.hCost = (int) (10 * (Math.max(xDistance, yDistance) + Math.min(xDistance, yDistance) * (Math.sqrt(2) - 1)));

        // Default F Cost
        node.fCost = Integer.MAX_VALUE;

        if (node.cellTypeTerrain != CellTypeTerrain.START && node.cellTypeTerrain != CellTypeTerrain.TARGET) {
            node.setText("<html>F:" + node.fCost + "<br>G:" + node.gCost + "<br>H:" + node.hCost + "</html>");
        }
    }
    // Writes costs of the cell on the cell
    private void setCostOnNodes(){
        int c = 0;
        int r = 0;

        while (c < columns && r < rows) {
            setInitialCosts(nodeMatrices[c][r]);
            c++;
            if (c == columns) {
                c = 0;
                r++;
            }
        }
    }

    // Change cell type to FOCUS (aka open) --- --- --- ||| --- --- --- ||| --- --- --- ||| --- --- ---

    // focused on an empty cell open = FOCUS
    private void focusNode (Node node, int potentialNewGCost) {
        if (node.cellTypeAlgo != CellTypeAlgo.FOCUS && node.cellTypeAlgo != CellTypeAlgo.CHECKED && node.cellTypeTerrain != CellTypeTerrain.WALL) {

            node.setTypeAsFocus();

            //int newGCost = getNewGCost(node);
            if (node.gCost == 1 || potentialNewGCost < node.gCost) {
                node.gCost = potentialNewGCost;

                node.fCost = node.gCost + node.hCost;
                node.setText("<html>F:" + node.fCost + "<br>G:" + node.gCost + "<br>H:" + node.hCost + "</html>");
                node.parent = currentNode;
            }

            focusList.add(node);
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
        //node.gCost = (int) (10 * Math.sqrt(xDistance*xDistance + yDistance*yDistance));
        //
        return parentGCost + (int) (10 * (Math.max(xDistance, yDistance) + Math.min(xDistance, yDistance) * (Math.sqrt(2) - 1)));
    }


    // Auto Search for A Star Algo --- --- --- ||| --- --- --- ||| --- --- --- ||| --- --- ---

    public void autoSearch(Node startNode) {

        while (!targetReached) {
            int c = currentNode.column;
            int r = currentNode.row;

            currentNode.setAsChecked();
            focusList.remove(currentNode);

//            // 4 Direction Check
//            // Focus the upper cell if there is one
//            if (r - 1 > -1)
//                focusNode(nodeMatrices[c][r - 1], currentNode.gCost + 10);
//            // Focus the left cell if there is one
//            if (c - 1 > -1)
//                focusNode(nodeMatrices[c - 1][r], currentNode.gCost + 10);
//            // Focus the down cell if there is one
//            if (r + 1 < rows)
//                focusNode(nodeMatrices[c][r + 1], currentNode.gCost + 10);
//            // Focus the right cell if there is one
//            if (c + 1 < columns)
//                focusNode(nodeMatrices[c + 1][r], currentNode.gCost + 10);
//
//            // * Direction Check (+4)
//            // Focus the upper-left cell if there is one
//            if (r - 1 > -1 && c - 1 > -1 && !(nodeMatrices[c - 1][r].cellTypeTerrain == CellTypeTerrain.WALL && nodeMatrices[c][r - 1].cellTypeTerrain == CellTypeTerrain.WALL))
//                focusNode(nodeMatrices[c - 1][r - 1], currentNode.gCost + 14);
//            // Focus the down-left cell if there is one
//            if (r + 1 < rows && c - 1 > -1 && !(nodeMatrices[c - 1][r].cellTypeTerrain == CellTypeTerrain.WALL && nodeMatrices[c][r + 1].cellTypeTerrain == CellTypeTerrain.WALL))
//                focusNode(nodeMatrices[c - 1][r + 1], currentNode.gCost + 14);
//            // Focus the upper-right cell if there is one
//            if (r - 1 > -1 && c + 1 < columns && !(nodeMatrices[c + 1][r].cellTypeTerrain == CellTypeTerrain.WALL && nodeMatrices[c][r - 1].cellTypeTerrain == CellTypeTerrain.WALL))
//                focusNode(nodeMatrices[c + 1][r - 1], currentNode.gCost + 14);
//            // Focus the down-right cell if there is one
//            if (r + 1 < rows && c + 1 < columns && !(nodeMatrices[c + 1][r].cellTypeTerrain == CellTypeTerrain.WALL && nodeMatrices[c][r + 1].cellTypeTerrain == CellTypeTerrain.WALL))
//                focusNode(nodeMatrices[c + 1][r + 1], currentNode.gCost + 14);

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
//                    if (focusList.get(i).hCost < focusList.get(bestNodeIndex).hCost) {
//                        bestNodeIndex = i;
//                    } else if (focusList.get(i).hCost == focusList.get(bestNodeIndex).hCost) {
//                        if (focusList.get(i).gCost < focusList.get(bestNodeIndex).gCost) {
//                            bestNodeIndex = i;
//                        }
//                    }
//
//                }
//            }

            //Find Path?? --- --- --- /// --- /// --- /// --- --- ---
            findPath();

//            for (int i = 0; i < checkedList.size(); i++) {
//                // Check if this node's F cost is lower
//                if (checkedList.get(i).fCost < bestNodeFCost) {
//                    bestNodeIndex = i;
//                    bestNodeFCost = checkedList.get(i).fCost;
//                } else if (checkedList.get(i).fCost == bestNodeFCost) {
//                    // IF F cost are same check min G cost
//                    if (checkedList.get(i).hCost < checkedList.get(bestNodeIndex).hCost) {
//                        bestNodeIndex = i;
//                    }
//                }
//            }


            //currentNode = focusList.get(bestNodeIndex);
            //currentNode.cellType == CellType.TARGET
            if (currentNode.targetNode) {
                targetReached = true;
                trackThePath();
            }
            step++;
        }

    }

    //Find Path?? --- --- --- /// --- /// --- /// --- --- ---

    public void findPath() {
        focusList.add(startNode);
        while (!focusList.isEmpty()) {
            Node currentNode = focusList.poll();
            closedSet.add(currentNode);
            if (isFinalNode(currentNode)) {
                trackThePath();
            } else {
                addAdjacentNodes(currentNode);
            }
        }
    }

public void addAdjacentNodes(Node currentNode){

    int c = currentNode.column;
    int r = currentNode.row;

    // 4 Direction Check
    // Focus the upper cell if there is one
    if (r - 1 > -1)
        focusNode(nodeMatrices[c][r - 1], currentNode.gCost + 10);
    // Focus the left cell if there is one
    if (c - 1 > -1)
        focusNode(nodeMatrices[c - 1][r], currentNode.gCost + 10);
    // Focus the down cell if there is one
    if (r + 1 < rows)
        focusNode(nodeMatrices[c][r + 1], currentNode.gCost + 10);
    // Focus the right cell if there is one
    if (c + 1 < columns)
        focusNode(nodeMatrices[c + 1][r], currentNode.gCost + 10);

    // * Direction Check (+4)
    // Focus the upper-left cell if there is one
    if (r - 1 > -1 && c - 1 > -1 && !(nodeMatrices[c - 1][r].cellTypeTerrain == CellTypeTerrain.WALL && nodeMatrices[c][r - 1].cellTypeTerrain == CellTypeTerrain.WALL))
        focusNode(nodeMatrices[c - 1][r - 1], currentNode.gCost + 14);
    // Focus the down-left cell if there is one
    if (r + 1 < rows && c - 1 > -1 && !(nodeMatrices[c - 1][r].cellTypeTerrain == CellTypeTerrain.WALL && nodeMatrices[c][r + 1].cellTypeTerrain == CellTypeTerrain.WALL))
        focusNode(nodeMatrices[c - 1][r + 1], currentNode.gCost + 14);
    // Focus the upper-right cell if there is one
    if (r - 1 > -1 && c + 1 < columns && !(nodeMatrices[c + 1][r].cellTypeTerrain == CellTypeTerrain.WALL && nodeMatrices[c][r - 1].cellTypeTerrain == CellTypeTerrain.WALL))
        focusNode(nodeMatrices[c + 1][r - 1], currentNode.gCost + 14);
    // Focus the down-right cell if there is one
    if (r + 1 < rows && c + 1 < columns && !(nodeMatrices[c + 1][r].cellTypeTerrain == CellTypeTerrain.WALL && nodeMatrices[c][r + 1].cellTypeTerrain == CellTypeTerrain.WALL))
        focusNode(nodeMatrices[c + 1][r + 1], currentNode.gCost + 14);

}

    private boolean isFinalNode (Node currentNode) {
        return currentNode.cellTypeTerrain == CellTypeTerrain.TARGET;
    }
//    public void autoSearch(Node startNode) {
////        currentNode = startNode;
////        currentNode.gCost = 0;
//
//        if (!targetReached) {
//            int c = currentNode.column;
//            int r = currentNode.row;
//
//            currentNode.setAsChecked();
//            //checkedList.add(currentNode);
//            focusList.remove(currentNode);
//
//            // 4 Direction Check
//            // Focus the upper cell if there is one
//            if (r - 1 > -1)
//                focusNode(nodeMatrix[c][r - 1], currentNode.gCost + 10);
//            // Focus the left cell if there is one
//            if (c - 1 > -1)
//                focusNode(nodeMatrix[c - 1][r], currentNode.gCost + 10);
//            // Focus the down cell if there is one
//            if (r + 1 < rows)
//                focusNode(nodeMatrix[c][r + 1], currentNode.gCost + 10);
//            // Focus the right cell if there is one
//            if (c + 1 < columns)
//                focusNode(nodeMatrix[c + 1][r], currentNode.gCost + 10);
//
//            // * Direction Check (+4)
//            // Focus the upper-left cell if there is one
//            if (r - 1 > -1 && c - 1 > -1 && !(nodeMatrix[c - 1][r].cellTypeTerrain == CellTypeTerrain.WALL && nodeMatrix[c][r - 1].cellTypeTerrain == CellTypeTerrain.WALL))
//                focusNode(nodeMatrix[c - 1][r - 1], currentNode.gCost + 14);
//            // Focus the down-left cell if there is one
//            if (r + 1 < rows && c - 1 > -1 && !(nodeMatrix[c - 1][r].cellTypeTerrain == CellTypeTerrain.WALL && nodeMatrix[c][r + 1].cellTypeTerrain == CellTypeTerrain.WALL))
//                focusNode(nodeMatrix[c - 1][r + 1], currentNode.gCost + 14);
//            // Focus the upper-right cell if there is one
//            if (r - 1 > -1 && c + 1 < columns && !(nodeMatrix[c + 1][r].cellTypeTerrain == CellTypeTerrain.WALL && nodeMatrix[c][r - 1].cellTypeTerrain == CellTypeTerrain.WALL))
//                focusNode(nodeMatrix[c + 1][r - 1], currentNode.gCost + 14);
//            // Focus the down-right cell if there is one
//            if (r + 1 < rows && c + 1 < columns && !(nodeMatrix[c + 1][r].cellTypeTerrain == CellTypeTerrain.WALL && nodeMatrix[c][r + 1].cellTypeTerrain == CellTypeTerrain.WALL))
//                focusNode(nodeMatrix[c + 1][r + 1], currentNode.gCost + 14);
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
////            try
////            {
////                Thread.sleep(250);
////            }
////            catch(InterruptedException ex)
////            {
////                Thread.currentThread().interrupt();
////            }
//            // After the loop we have next step cell
//            currentNode = focusList.get(bestNodeIndex);
//            //currentNode.cellType == CellType.TARGET
//            if (currentNode.targetNode) {
//                targetReached = true;
//                trackThePath();
//            }
//            step++;
//        }
//
//    }

    // Backtrack the best path --- --- --- ||| --- --- --- ||| --- --- --- ||| --- --- ---

    private void trackThePath () {
        Node current = targetNode;

        while (current != startNode) {
            current = current.parent;
            if (current != startNode) {
                current.setTypeAsPath();
            }
        }
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
