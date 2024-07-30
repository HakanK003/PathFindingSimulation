import java.awt.*;

public enum CellTypeAlgo {

//    // Type of cell
//    EMPTY(Color.white),
//    WALL(Color.black),
//    UNKNOWN(Color.gray),
//    START(Color.green),
//    TARGET(Color.orange),

    // Highlighting the cell after evaluating
    NOTHING(Color.white),
    FOCUS(Color.red),
    CHECKED(Color.cyan),
    PATH(Color.blue),
    PASSEDPATH(Color.orange),

    // Robot or sensors are on this cell
    ROBOT(Color.magenta),
    SENSORS(Color.yellow);

    final Color color;

    CellTypeAlgo(Color color){
        this.color = color;
    }
}
