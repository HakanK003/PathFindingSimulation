// Cell enum is group of constants that behave similarly to objects
// In this case all types of cells are cells, but they will have different purposes
import java.awt.*;
public enum CellType {
    // Type of cell
    EMPTY(Color.white),
    WALL(Color.black),
    UNKNOWN(Color.gray),
    START(Color.green),
    TARGET(Color.orange),

    // Highlighting the cell after evaluating
    INVALID(Color.red),
    VISITED(Color.cyan),
    PATH(Color.blue),

    // Robot or sensors are on this cell
    ROBOT(Color.magenta),
    SENSORS(Color.yellow);

    final Color color;

    CellType(Color color){
        this.color = color;
    }

}