public class GlobalSettings {
    // Log info
    public static int logCounter = 1;

    // Cell general information
    public static final int columns_count = 15;
    public static final int rows_count = 10;
    public static final int nodeSize = 90;

    // Cell limitations info
    public static int robotCount = 0;
    public static int startCellCount = 0;
    public static int targetCellCount = 0;

    // Menu information
    public static final int menuWidth = 500;
    public static int buttonHeight = 350;
    public static int buttonWidth = 450;
    public static int buttonCount = 9;

    public static String [] buttonTexts = new String[] {"Close", "Wall", "Empty",
                                                        "Start", "Target", "Unknown",
                                                        "Robot", "Save", "Reset"
                                                        };
    // Show cell info
    // Show cell costs
    // Robot Display On/Off
    // Sensor count

    public static int robotStepSpeed = 1; // 1 second
}
