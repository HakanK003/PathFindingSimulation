// Cell enum is group of constants that behave similarly to objects
// In this case all types of cells are cells, but they will have different purposes
import java.awt.*;
public enum CellTypeTerrain {
    // Type of cell
    EMPTY(Color.white),
    WALL(Color.black),
    UNKNOWN(Color.gray),
    START(Color.red),
    TARGET(Color.green),

//    // Highlighting the cell after evaluating
//    FOCUS(Color.red),
//    CHECKED(Color.cyan),
//    PATH(Color.blue),

    // Robot or sensors are on this cell
    ROBOT(Color.magenta),
    SENSORS(Color.yellow);

    final Color color;

    CellTypeTerrain(Color color){
        this.color = color;
    }

}
