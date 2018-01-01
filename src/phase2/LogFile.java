package phase2;

import java.io.*;
import java.io.File;
import java.sql.Timestamp;
import java.awt.*;

/**
 * A new Log class which keeps track of the past and new tags of
 * an phase1.Image file. Each Log also keeps track of the time stamp of the
 * moment when the phase1.Image name changed.
 */
public class LogFile implements Serializable {

    // The phase1.Image object this log refers to.
    private Image image;
    // The logText file for the referred phase1.Image.
    private File logFileName;
    // The number of log inputs that the phase1.Image object has.
    private Integer numLogs = 1;

    /**
     * Constructs a new log which indicates when the phase1.Image's name
     * was last changed and previous name of the phase1.Image.
     *
     * @param image: The phase1.Image object this log refers to.
     */
    public LogFile(File logFileName, Image image) {
        this.logFileName = logFileName;
        this.image = image;
        createLogFile();
    }

    /**
     * Creates the log text file if it doesn't exist.
     */
    private void createLogFile() {
        if (!logFileName.exists()) {
            try {
                boolean success = logFileName.createNewFile();
                Writer buffW = new BufferedWriter(new FileWriter(logFileName, true));
                if (success) {
                    buffW.write("Log Entries of Image: " + image.getLastUsedName());
                }
                buffW.close();
                writeLogToText();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Writes a log entry onto the existing text file. If the text file
     * doesn't exist, the method will create a new text file.
     *
     * @throws Exception: Any exception is ignored.
     */
    void writeLogToText() throws Exception {
        Writer buffW;
        try {
            buffW = new BufferedWriter(new FileWriter(logFileName, true));
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            buffW.write("\n\n" + numLogs.toString() + ") " + timestamp);
            buffW.write("\nOld image name: " + image.getLastUsedName());
            buffW.write("\nNew image name: " + image.getImageName());
            buffW.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        numLogs++;
    }


    /**
     * Opens this phase1.LogFile's txt file
     */
    void openLogFile(){
        if(Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (logFileName.exists()) try {
                desktop.open(logFileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}