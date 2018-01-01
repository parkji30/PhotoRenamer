package phase2;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.net.URL;
import java.util.Objects;

public class Image implements Serializable {

    // The directory that contains this image.
    private Directory imageDirectory;

    // An ArrayList to store all past tags of the phase1.Image.
    private ArrayList<String> tagList = new ArrayList<>();

    // An ArrayList of all of this phase1.Image's names.
    private ArrayList<String> nameList = new ArrayList<>();

    // The current phase1.Image file name.
    private String imageName;

    // The previously used name of the phase1.Image.
    private String lastUsedName;

    // The file that this phase1.Image object will refer to.
    private File file;

    // The log object that contains all log for this phase1.Image.
    private LogFile imageLog;

    // The url used for this image when viewing it in the GUI
    private URL url;

    /**
     * Constructs a new phase1.Image object which stores the current name
     * of the image along with the directory that the phase1.Image is in.
     *
     * @param imageName: The current name of the phase1.Image.
     * @param directory: The file path of the phase1.Image.
     */
    public Image(String imageName, Directory directory, File file) {
        this.imageName = imageName;
        this.lastUsedName = imageName;
        this.nameList.add(imageName);
        this.imageDirectory = directory;
        this.file = file;
        try {
            this.url = this.file.toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        extractTagsFromName(this.file.getName());
    }

    /**
     * Adds all the given tags to the images list of tags. Each tag is
     * starts with an @ symbol and should be separated with a comma.
     *
     * @param newTags: A new string tag identifier for the image.
     */
    public void addTags(String newTags) {
        String[] tags = newTags.split(",");
        for (String obj : tags) {
            if  (obj.trim().length() > 0 && obj.trim().charAt(0) == '@'){
                if (!tagList.contains(obj.trim()) && !obj.trim().equals("")){
                    tagList.add(obj.trim());
                    if(!SystemManager.tagList.contains(obj.trim())){
                        SystemManager.tagList.add(obj.trim());
                    }
                }
            } else if (obj.trim().length() > 0 &&  obj.trim().charAt(0) != '@') {
                if (!tagList.contains("@" + obj.trim()) && !obj.trim().equals("")) {
                    tagList.add("@" + obj.trim());
                    if(!SystemManager.tagList.contains("@" + obj.trim())){
                        SystemManager.tagList.add("@" + obj.trim());
                    }
                }
            }

        }
        updateImageName();
        if (!this.nameList.contains(imageName)) {
            this.nameList.add(imageName);
        }
    }

    /**
     * Deletes an old tag of the phase1.Image from its current list of tags.
     *
     * @param oldTag: An old tag to be removed from the list of tags.
     */
    public void deleteTag(String oldTag) {
        if (tagList.contains(oldTag)) {
            tagList.remove(oldTag);
        }
        updateImageName();
        if (!this.nameList.contains(imageName)) {
            this.nameList.add(imageName);
        }
        if(SystemManager.searchImagesByTag(oldTag).size() == 0){
            SystemManager.tagList.remove(oldTag);
        }
    }

    /**
     * Returns this Image object's current list of tags.
     *
     * @return ArrayList<String>
     */
    public ArrayList<String> getTagList() {
        return tagList;
    }

    /**
     * Returns the url of this phase1.Image object.
     *
     * @return URL
     */
    public URL getUrl() {
        try {
            this.url = this.file.toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return this.url;
    }

    /**
     * Sets the images name to the given image name.
     */
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    /**
     * Returns the directory that contains this phase1.Image
     * object.
     *
     * @return phase1.Directory
     */
    public Directory getDirectory() {
        return imageDirectory;
    }

    /**
     * Sets the directory of this Image to the given directory name
     */
    public void setDirectory(Directory directory){
        this.imageDirectory = directory;
    }

    /**
     * Returns the current file name of this phase1.Image object with
     * its extension
     *
     * @return String
     */
    public String getImageName() {
        return this.imageName;
    }

    /**
     * Returns the current file name of this phase1.Image object without
     * its extension.
     *
     * @return String
     */
    public String getImageNameNoExtension() {
        int i = 0;
        StringBuilder noExtensionName = new StringBuilder();

        // what if image name doesn't have an extension?
        while (this.imageName.charAt(i) != '.') {
            noExtensionName.append(this.imageName.charAt(i));
            i++;
        }
        return noExtensionName.toString();
    }

    /**
     * Returns the previous phase1.Image name of this phase1.Image object.
     * If this image object does not have a previously used name,
     * then oldImageName will refer to null.
     *
     * @return String || null
     */
    public String getLastUsedName() {
        return lastUsedName;
    }

    /**
     * Returns an ArrayList of all past names of this phase1.Image.
     *
     * @return ArrayList<String>
     */
    public ArrayList<String> getNameList(){
        return this.nameList;
    }

    /**
     * Renames the phase1.Image name to any of the previous names that
     * the phase1.Image once had.
     *
     * @param oldName: An old name of the phase1.Image.
     */
    public void renameToOldImage(String oldName){
        if (this.nameList.contains(oldName)){
            extractTagsFromName(oldName);
            this.imageDirectory.updateTags();
            this.lastUsedName = this.imageName;
            this.imageName = oldName;
            this.file.renameTo(new File(oldName));
            if (this.imageLog == null){
                this.imageLog = new LogFile(new File(this.getImageNameNoExtension()
                        + " log.txt"), this);
            } else{
                try {
                    this.imageLog.writeLogToText();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

    }

    /**
     * Removes the tags from an phase1.Image's name and adds them to the
     * phase1.Image's list of tags.
     *
     * @param imageName: the name of the phase1.Image
     */
    private void extractTagsFromName(String imageName){
        final ArrayList<String> prevTags = new ArrayList<>();
        prevTags.addAll(tagList);
        tagList.clear();
        StringBuilder tagsToAdd = new StringBuilder();
        String[] imageNameSplit = imageName.split(" ");
        for(int i = 0; i < imageNameSplit.length ; i++){
            if(i + 1 < imageNameSplit.length && imageNameSplit[i].charAt(0) == '@' &&  imageNameSplit[i + 1].charAt(0) != '@'){
                String tag;
                if(imageNameSplit[i+1].contains(".")){
                    tag = imageNameSplit[i] + " " + imageNameSplit[i+1].substring(0, imageNameSplit[i+1].indexOf('.'));
                }
                else{
                    tag = imageNameSplit[i] + " " + imageNameSplit[i+1];
                }
                tagsToAdd.append(tag);
                tagsToAdd.append(", ");
            }
            else if (imageNameSplit[i].charAt(0) == '@' && !imageNameSplit[i].contains(".")){
                String tag = imageNameSplit[i];
                tagsToAdd.append(tag);
                tagsToAdd.append(", ");
            }
            else if (imageNameSplit[i].charAt(0) == '@'){
                String tag = imageNameSplit[i].substring(0, imageNameSplit[i].indexOf('.'));
                tagsToAdd.append(tag);
                tagsToAdd.append(", ");
            }
        }
        String tagsToAddString = tagsToAdd.toString();
        this.addTags(tagsToAddString);

        for (String oldTag:prevTags) {
            if(SystemManager.searchImagesByTag(oldTag).size() == 0){
                SystemManager.tagList.remove(oldTag);
            }
        }
    }

    /**
     * Returns the image file this phase1.Image object is referring to.
     *
     * @return File
     */
    public File getFile() {
        return file;
    }

    /**
     * Sets the lastUsedName of this phase1.Image object to the current name.
     * Loops through the phase1.Image object's current list of tags and updates
     * the phase1.Image object's name by adding all the tags in chronological order
     * behind the phase1.Image name. The image file which this phase1.Image object refers
     * to will also have its file name updated accordingly.
     */
    void updateImageName() {
        this.lastUsedName = this.imageName;
        StringBuilder newName = new StringBuilder();
        Integer endOfNameIndex = this.imageName.contains("@") ?
                this.imageName.indexOf('@') : this.imageName.indexOf('.');
        newName.append(imageName.substring(0, endOfNameIndex).trim());
        for (String tag : tagList) {
            newName.append(" ");
            newName.append(tag);
        }
        newName.append(imageName.substring(this.imageName.indexOf('.')));
        String logFileName = this.getImageNameNoExtension();
        this.imageName = newName.toString();

        String fullName = imageDirectory.getDirectoryPath() + "/" + newName;
        File newFile = new File(fullName);
        boolean renameSuccess = this.file.renameTo(newFile);
        if (!this.lastUsedName.equals(this.imageName)) {
            if (this.imageLog == null & renameSuccess) {
                this.imageLog = new LogFile(new File(logFileName
                        + " log.txt"), this);
            } else {
                try {
                    if(this.imageLog != null) {
                        this.imageLog.writeLogToText();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            this.url = this.file.toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the file of this phase1.Image object with a argument file
     *
     * @param file the file to be set
     */
    public void setFile(File file){
        this.file = file;
    }

    /**
     * Opens the Log text file of this phase1.Image Object
     */
    void openLogFile(){
        if(this.imageLog != null) {
            this.imageLog.openLogFile();
        }
    }

    /**
     * Moves this phase1.Image Object to a new destinationPath
     *
     * @param destinationPath The destination for this phase1.Image
     */
    public void move(String destinationPath){
        File newFile = new File(destinationPath + "/" + this.imageName);
        String newPath = newFile.getAbsolutePath();
        boolean renameSuccess = this.file.renameTo(new File(newPath));
        // Check if this directory exists
        boolean directoryExists = false;
        for (Directory dict : SystemManager.directories){
            if (dict.getDirectoryFile().getAbsolutePath().equals(destinationPath)){
                this.imageDirectory.removeImage(this);
                dict.addImage(this);
                this.imageDirectory = dict;
                directoryExists = true;
            }
        }
        if(!directoryExists){
            Directory directory = new Directory(destinationPath + "/");
            this.imageDirectory.removeImage(this);
            SystemManager.directories.add(directory);
            directory.addImage(this);
            this.imageDirectory = directory;
        }

        try {
            if (renameSuccess) {
                this.url = this.file.toURI().toURL();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Open's this phase1.Image's directory in the OS's file explorer
     */
    void open(){
        File directory = this.imageDirectory.getDirectoryFile();
        if(Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (directory.exists()) try {
                desktop.open(directory);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Compares this Image with another Object and returns if they are equal
     * @param other : The other Object to compare with
     * @return boolean : Whether or not this Image is equal to the Object other
     */
    @Override
    public boolean equals(Object other) {
        return other instanceof Image && Objects.equals(this.getFile().getAbsolutePath(),
                ((Image) other).getFile().getAbsolutePath()) && Objects.equals(this.imageName, ((Image) other).imageName);
    }
}