import javax.swing.*;
import java.awt.*;
import java.awt.color.*;

public class MapPanel extends JPanel {

    public final int columns = GlobalSettings.columns_count;
    public final int rows = GlobalSettings.rows_count;
    public final int nodeSize = GlobalSettings.nodeSize;
    public final int screenWidth = nodeSize * columns;
    public final int screenHeight = nodeSize * rows;

    Node [][] nodeMatrix = new Node[columns][rows];
    MapPanel(){

        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.blue);
        this.setLayout(new GridLayout(rows,columns));

        for (int c = 0; c < columns; c++) {
            for (int r = 0; r < rows; r++) {
                nodeMatrix[c][r] = new Node (c,r);
                this.add(nodeMatrix[c][r]);
            }
        }

//        // set star and end node manually
//        setStartNode(9,9);
//        setTargetNode(0, 0);

    }

    private void setStartNode(int column, int row){
        nodeMatrix[column][row].setAsStart();
    }
    private void setTargetNode(int column, int row){
        nodeMatrix[column][row].setAsTarget();
    }
}
