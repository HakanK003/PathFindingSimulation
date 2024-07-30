import javax.swing.*;
import java.util.ArrayList;

public class Robot extends JPanel {
    Node currentCell;
    Node previousCell;
    int robotSize = GlobalSettings.robotSize;
    int robotStep = 0;

    Robot(Node currentCell){
        this.currentCell = currentCell;

//        JPanel robot = new JPanel();
//        robot.setSize(robotSize, robotSize);
//        robot.setBackground(CellTypeAlgo.ROBOT.color);
//        currentCell.add(robot);
    }

//    public void goNextOnPath(Robot robot){
//        //currentCell.remove(robot);
//        currentCell = checkNextPathNode();
//        currentCell.add(robot);
//        //currentCell.getLayout();robot.setVisible(true);
//        robot.setBackground(CellTypeAlgo.ROBOT.color);
//        System.out.println("New robot at R " + currentCell.row+ " C " +currentCell.column);
//    }

    public void goNextOnPath(){
        //currentCell.remove(robot);
//        try {
//            Thread.sleep(250);
//        } catch (InterruptedException ie) {
//            System.out.println("Scanning...");
//        }
        currentCell.setText("<html>Passed"+ "<br>" +"Step " + robotStep++ + "</html>");
        currentCell.cellTypeAlgo = CellTypeAlgo.PASSEDPATH;
        currentCell.setBackground(CellTypeAlgo.PASSEDPATH.color);
        previousCell = currentCell.parent;
        currentCell = checkNextPathNode();
        currentCell.setText("ROBOT");
        //currentCell.getLayout();robot.setVisible(true);
        //robot.setBackground(CellTypeAlgo.ROBOT.color);
        System.out.println("New robot at R " + currentCell.row+ " C " +currentCell.column);
    }

    public Node checkNextPathNode (){

        int r = currentCell.row;
        int c = currentCell.column;

        if (sensorCheckNextPathNodeIsAWall()){
            System.out.println("Set current cell R " + currentCell.row + " C " + currentCell.column + " to");

//            if (currentCell == currentCell.parent){
//                currentCell = checkPreviousPathNode();
//                System.out.println("Previous cell R " + currentCell.row + " C " + currentCell.column);
//            } else {
                currentCell = currentCell.parent;
                System.out.println("Previous cell R " + previousCell.row + " C " + previousCell.column);
            //}
            recalculateThePath();
        }



        ArrayList<Node> possibleNodes = new ArrayList<>();

        // Right Cell
        if (r - 1 > -1 && MapPanel.nodeMatrix[c][r - 1].cellTypeAlgo != CellTypeAlgo.PASSEDPATH && (MapPanel.nodeMatrix[c][r - 1].cellTypeAlgo == CellTypeAlgo.PATH || MapPanel.nodeMatrix[c][r - 1].cellTypeTerrain == CellTypeTerrain.TARGET))
            possibleNodes.add(MapPanel.nodeMatrix[c][r - 1]);
        // Focus the left cell if there is one
        if (c - 1 > -1 && MapPanel.nodeMatrix[c - 1][r].cellTypeAlgo != CellTypeAlgo.PASSEDPATH && (MapPanel.nodeMatrix[c - 1][r].cellTypeAlgo == CellTypeAlgo.PATH || MapPanel.nodeMatrix[c - 1][r].cellTypeTerrain == CellTypeTerrain.TARGET))
            possibleNodes.add(MapPanel.nodeMatrix[c - 1][r]);
        // Focus the down cell if there is one
        if (r + 1 < GlobalSettings.rows_count && MapPanel.nodeMatrix[c][r + 1].cellTypeAlgo != CellTypeAlgo.PASSEDPATH && (MapPanel.nodeMatrix[c][r + 1].cellTypeAlgo == CellTypeAlgo.PATH || MapPanel.nodeMatrix[c][r + 1].cellTypeTerrain == CellTypeTerrain.TARGET))
            possibleNodes.add(MapPanel.nodeMatrix[c][r + 1]);
        // Focus the right cell if there is one
        if (c + 1 < GlobalSettings.columns_count && MapPanel.nodeMatrix[c + 1][r].cellTypeAlgo != CellTypeAlgo.PASSEDPATH && (MapPanel.nodeMatrix[c + 1][r].cellTypeAlgo == CellTypeAlgo.PATH || MapPanel.nodeMatrix[c + 1][r].cellTypeTerrain == CellTypeTerrain.TARGET))
            possibleNodes.add(MapPanel.nodeMatrix[c + 1][r]);

//        // * Direction Check (+4)
//        // Focus the upper-left cell if there is one
//        if (r - 1 > -1 && c - 1 > -1 && MapPanel.nodeMatrix[c - 1][r - 1].cellTypeAlgo == CellTypeAlgo.PATH)
//            possibleNodes.add(MapPanel.nodeMatrix[c - 1][r - 1]);
//        // Focus the down-left cell if there is one
//        if (r + 1 < GlobalSettings.rows_count && c - 1 > -1 && MapPanel.nodeMatrix[c - 1][r + 1].cellTypeAlgo == CellTypeAlgo.PATH)
//            possibleNodes.add(MapPanel.nodeMatrix[c - 1][r + 1]);
//        // Focus the upper-right cell if there is one
//        if (r - 1 > -1 && c + 1 < GlobalSettings.columns_count && MapPanel.nodeMatrix[c + 1][r - 1].cellTypeAlgo == CellTypeAlgo.PATH)
//            possibleNodes.add(MapPanel.nodeMatrix[c + 1][r - 1]);
//        // Focus the down-right cell if there is one
//        if (r + 1 < GlobalSettings.rows_count && c + 1 < GlobalSettings.columns_count && MapPanel.nodeMatrix[c + 1][r + 1].cellTypeAlgo == CellTypeAlgo.PATH)
//            possibleNodes.add(MapPanel.nodeMatrix[c + 1][r + 1]);

        if (!possibleNodes.isEmpty()) {
            return possibleNodes.get(0);
        } else {
            return MapPanel.startNode;
        }
    }

    private void recalculateThePath() {

        //mapPanel.autoSearch();
        System.out.println("------------------------------------------------------------------");
        System.out.println("Recalculating the path");
        MapPanel.resetAlgoCellType(currentCell);

        //Reset F G H costs
        GlobalFrame.mapPanel.setInitialCostOnNodes();
        System.out.println("Initial Cost Reset-----------------------------------------------");


        //Set Current node as start node in mapPanel
        MapPanel.startNode = currentCell;
        MapPanel.currentNode = currentCell;
        MapPanel.targetReached = false;

        //Empty the checked and focus arraylists
        MapPanel.focusList.clear();

        System.out.println("Start Target Focus reset---------------------------------------");

        //Recalculate the path
        GlobalFrame.mapPanel.autoSearch();
        System.out.println("End of Recalculation ------------------------------------------------------------------");


    }

    public boolean sensorCheckNextPathNodeIsAWall (){

        int r = currentCell.row;
        int c = currentCell.column;

        ArrayList<Node> newWalls = new ArrayList<>();

        // Right Cell
        if (r - 1 > -1 && (MapPanel.nodeMatrix[c][r - 1].cellTypeTerrain == CellTypeTerrain.UNKNOWN && MapPanel.nodeMatrix[c][r - 1].cellTypeAlgo == CellTypeAlgo.PATH))
            newWalls.add(MapPanel.nodeMatrix[c][r - 1]);
        // Focus the left cell if there is one
        if (c - 1 > -1 && (MapPanel.nodeMatrix[c - 1][r].cellTypeTerrain == CellTypeTerrain.UNKNOWN && MapPanel.nodeMatrix[c - 1][r].cellTypeAlgo == CellTypeAlgo.PATH))
            newWalls.add(MapPanel.nodeMatrix[c - 1][r]);
        // Focus the down cell if there is one
        if (r + 1 < GlobalSettings.rows_count && (MapPanel.nodeMatrix[c][r + 1].cellTypeTerrain == CellTypeTerrain.UNKNOWN && MapPanel.nodeMatrix[c][r + 1].cellTypeAlgo == CellTypeAlgo.PATH))
            newWalls.add(MapPanel.nodeMatrix[c][r + 1]);
        // Focus the right cell if there is one
        if (c + 1 < GlobalSettings.columns_count && (MapPanel.nodeMatrix[c + 1][r].cellTypeTerrain == CellTypeTerrain.UNKNOWN && MapPanel.nodeMatrix[c + 1][r].cellTypeAlgo == CellTypeAlgo.PATH))
            newWalls.add(MapPanel.nodeMatrix[c + 1][r]);

//        // * Direction Check (+4)
//        // Focus the upper-left cell if there is one
//        if (r - 1 > -1 && c - 1 > -1 && MapPanel.nodeMatrix[c - 1][r - 1].cellTypeAlgo == CellTypeAlgo.PATH)
//            possibleNodes.add(MapPanel.nodeMatrix[c - 1][r - 1]);
//        // Focus the down-left cell if there is one
//        if (r + 1 < GlobalSettings.rows_count && c - 1 > -1 && MapPanel.nodeMatrix[c - 1][r + 1].cellTypeAlgo == CellTypeAlgo.PATH)
//            possibleNodes.add(MapPanel.nodeMatrix[c - 1][r + 1]);
//        // Focus the upper-right cell if there is one
//        if (r - 1 > -1 && c + 1 < GlobalSettings.columns_count && MapPanel.nodeMatrix[c + 1][r - 1].cellTypeAlgo == CellTypeAlgo.PATH)
//            possibleNodes.add(MapPanel.nodeMatrix[c + 1][r - 1]);
//        // Focus the down-right cell if there is one
//        if (r + 1 < GlobalSettings.rows_count && c + 1 < GlobalSettings.columns_count && MapPanel.nodeMatrix[c + 1][r + 1].cellTypeAlgo == CellTypeAlgo.PATH)
//            possibleNodes.add(MapPanel.nodeMatrix[c + 1][r + 1]);

        if (!newWalls.isEmpty()) {
            for (Node newWall : newWalls) {
                newWall.cellTypeTerrain = CellTypeTerrain.WALL;
                newWall.setBackground(CellTypeTerrain.WALL.color);
                System.out.println("New wall found on R " + newWall.row + " C " + newWall.column);
                System.out.println("------------------------------------------------------------------");
            }
            //MapPanel.nodeMatrix.autoSearch();
            return true;
        } else {
            return false;
        }
    }


//    public Node checkPreviousPathNode (){
//
//        int r = currentCell.row;
//        int c = currentCell.column;
//
////        if (sensorCheckNextPathNodeIsAWall()){
////            System.out.println("Set current cell R " + currentCell.row + " C " + currentCell.column + " to");
////            currentCell = currentCell.parent;
////            System.out.println("Previous cell R " + previousCell.row + " C " + previousCell.column);
////            recalculateThePath();
////        }
//
//
//
//        ArrayList<Node> possibleNodes = new ArrayList<>();
//
//        // Right Cell
//        if (r - 1 > -1 && MapPanel.nodeMatrix[c][r - 1].cellTypeAlgo == CellTypeAlgo.PASSEDPATH)
//            possibleNodes.add(MapPanel.nodeMatrix[c][r - 1]);
//        // Focus the left cell if there is one
//        if (c - 1 > -1 && MapPanel.nodeMatrix[c - 1][r].cellTypeAlgo == CellTypeAlgo.PASSEDPATH)
//            possibleNodes.add(MapPanel.nodeMatrix[c - 1][r]);
//        // Focus the down cell if there is one
//        if (r + 1 < GlobalSettings.rows_count && MapPanel.nodeMatrix[c][r + 1].cellTypeAlgo == CellTypeAlgo.PASSEDPATH)
//            possibleNodes.add(MapPanel.nodeMatrix[c][r + 1]);
//        // Focus the right cell if there is one
//        if (c + 1 < GlobalSettings.columns_count && MapPanel.nodeMatrix[c + 1][r].cellTypeAlgo == CellTypeAlgo.PASSEDPATH)
//            possibleNodes.add(MapPanel.nodeMatrix[c + 1][r]);
//
////        // * Direction Check (+4)
////        // Focus the upper-left cell if there is one
////        if (r - 1 > -1 && c - 1 > -1 && MapPanel.nodeMatrix[c - 1][r - 1].cellTypeAlgo == CellTypeAlgo.PATH)
////            possibleNodes.add(MapPanel.nodeMatrix[c - 1][r - 1]);
////        // Focus the down-left cell if there is one
////        if (r + 1 < GlobalSettings.rows_count && c - 1 > -1 && MapPanel.nodeMatrix[c - 1][r + 1].cellTypeAlgo == CellTypeAlgo.PATH)
////            possibleNodes.add(MapPanel.nodeMatrix[c - 1][r + 1]);
////        // Focus the upper-right cell if there is one
////        if (r - 1 > -1 && c + 1 < GlobalSettings.columns_count && MapPanel.nodeMatrix[c + 1][r - 1].cellTypeAlgo == CellTypeAlgo.PATH)
////            possibleNodes.add(MapPanel.nodeMatrix[c + 1][r - 1]);
////        // Focus the down-right cell if there is one
////        if (r + 1 < GlobalSettings.rows_count && c + 1 < GlobalSettings.columns_count && MapPanel.nodeMatrix[c + 1][r + 1].cellTypeAlgo == CellTypeAlgo.PATH)
////            possibleNodes.add(MapPanel.nodeMatrix[c + 1][r + 1]);
//
//        if (!possibleNodes.isEmpty()) {
//            return possibleNodes.get(0);
//        } else {
//            return MapPanel.startNode;
//        }
//    }

}
