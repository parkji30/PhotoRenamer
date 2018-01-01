package phase2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.stage.DirectoryChooser;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Map;

public class StartViewController {

    // The Image object that is currently displayed and being modified.
    private Image image;
    // The directory that the application is currently linked to.
    private Directory directory;
    // The master log file which keeps track of all logs from all images.
    private File masterLogFile;
    /**
     * A SystemManager object which will keep track of all Images, Directories
     * and Log objects.
     */
    private SystemManager systemManager;

    public StartViewController(){
        this.directory = new Directory("phase2/");
        this.image = new Image("Welcome.png", this.directory,
                new File("src/phase2/Welcome.png"));
        this.systemManager = new SystemManager();
        this.masterLogFile = new File ("src/phase2/MasterLogFile.txt");
    }

    /**
     * Returns the Image object that is currently being shown on the
     * GUI.
     *
     * @return Image
     */
    public Image getImage() {
        return image;
    }

    /**
     * Sets a new Image object to be displayed on the GUI.
     *
     * @param image: The new Image object to be displayed.
     */
    public void setImage(Image image){
        this.image = image;
    }

    /**
     * Adds the given tags to the Image's name. A tag is preceded by
     * an @ and multiple tags can be added at once.
     *
     * @param tags: The tags to be added to the Image's name
     */
    public void addTags(String tags){
        BufferedWriter buffW;
        if (tags.length() != 0) {
            this.image.addTags(tags);
            this.systemManager.addNewTag(tags);
            try {
                buffW = new BufferedWriter(new FileWriter(masterLogFile, true));
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                buffW.write("\nTime Stamp: " + timestamp);
                buffW.write("\nOld image name: " + image.getLastUsedName());
                buffW.write("\nNew image name: " + image.getImageName() + "\n");
                buffW.close();
            } catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    /**
     * Takes an ArrayList of tags and adds each tag to the Image object
     * referred by this SystemManager.
     *
     * @param tagList: ArrayList<String>
     */
    void addTagList(ObservableList tagList){
        if (tagList != null && tagList.size() > 0){
            StringBuilder newTags = new StringBuilder();
            for (Object obj : tagList) {
                if (((String) obj).charAt(0) == '@') {
                    newTags.append((String) obj);
                    newTags.append(",");
                }
            }
            newTags.deleteCharAt(newTags.length() - 1);
            this.addTags(newTags.toString());
        }
    }

    /**
     * Deletes the specified tag from the Image name. A tag is preceded by
     * by an @ and only one tag can be deleted at a time.
     *
     * @param tagList: the tag to be deleted from this Image.
     */

    public void deleteTags(ObservableList tagList) {
        if (tagList != null && tagList.size() > 0){
            for (Object tag : tagList) {
                this.image.deleteTag((String)tag);
                if (SystemManager.searchImagesByTag((String)tag) == null){
                    this.systemManager.deleteTagFromSystem((String)tag);
                }
            }
        }
    }

    /**
     * Open's the Image object's current log file. The log file displays
     * all name changes that the Image went through along with the
     * time-stamp of when the change occurred.
     */
    void openImageLogFile(){
        this.image.openLogFile();
    }

    /**
     * Opens the master log file which contains the name changes of across
     * all the images including its timestamp.
     */
    void openMasterLogFile(){
        if(Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (masterLogFile.exists()) try {
                desktop.open(masterLogFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns the current directory that the application is in.
     *
     * @return Directory
     */
    public Directory getDirectory(){
        return this.directory;
    }

    /**
     * Sets the new directory to be displayed on the GUI.
     *
     * @param directory: The new directory to be displayed on the GUI.
     */
    public void setDirectory(Directory directory) {
        this.directory = directory;
    }

    /**
     * Opens a DirectoryChooser which will let the user find and select
     * a desired directory.
     *
     * @return String || null
     */
    String findDirectoryPath(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Open Image's Directory");
        File selectedFile = directoryChooser.showDialog(null);
        if (selectedFile == null){
            return null;
        }
        return selectedFile.getAbsolutePath();
    }

    /**
     * Returns the SystemManager object that the phase1 application is
     * currently referring to.
     *
     * @return SystemManager
     */

    SystemManager getSystemManager() {
        return systemManager;
    }

    /**
     * Moves the Image file that this StartViewController currently refers to
     * to the desired directory choice of the user.
     */
    void moveImage(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Open Image's New Directory");
        File selectedFile = directoryChooser.showDialog(null);
        if (selectedFile != null){
            this.image.move(selectedFile.getAbsolutePath());
        }
    }

    /**
     * Passes a string of tag(s) to be added into the SystemManager's
     * list of tags.
     *
     * @param newTags: The new tag(s) to be added.
     */
    void addNewExistingTag(String newTags) {
        if (newTags != null && !newTags.trim().equals("")) {
            this.systemManager.addNewTag(newTags);
        }
    }

    /**
     * Deletes an existing tag from the existing list of tags in
     * SystemManager.
     *
     * @param tagList: ObservableList
     */
    void deleteExistingTag(ObservableList tagList){
        if (tagList != null && tagList.size() > 0){
            for (Object tag : tagList) {
                if (this.systemManager.getTags().contains(tag.toString())) {
                    this.systemManager.deleteTagFromSystem((String)tag);
                }
            }
        }
    }

    /**
     * Creates a pie chart to represent the each tags percentage of occurrences over total tag occurrence.
     * @return ObservableList<PieChart.Data>
     */
    ObservableList<PieChart.Data> produceTagStatsMap() {
        Map<String, Float> tagData = systemManager.produceTagStatsMap();
        ObservableList<PieChart.Data> result = FXCollections.observableArrayList();
        Iterator it = tagData.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            result.add(new PieChart.Data((String) pair.getKey(), (float) pair.getValue()));
            it.remove(); // avoids a ConcurrentModificationException
        }
        return result;
    }
}
