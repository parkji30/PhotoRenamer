package phase2;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SystemManager implements Serializable{

    // An ArrayList of all from across all Images.
    public static ArrayList<String> tagList  = new ArrayList<>();
    private ArrayList<String> configTagList = new ArrayList<>();

    // The String representation of a path to our config file.
    //private String configFilePath = "src/phase2/config.ser";
    private String configFilePath = "src/phase2/config.ser";

    // An ArrayList of all directories containing Images with at least one tag.
    static ArrayList<Directory> directories = new ArrayList<>();
    private ArrayList<Directory> configDirectories = new ArrayList<>();


    /**
     * Constructs a new SystemManager object which initializes a tagList ArrayList,
     * directories ArrayList and configures any current images according the
     * the configuration file.
     */
    public SystemManager(){
    }

    /**
     * Searches through an ArrayList of Images for all phase1.Image objects that
     * contain the given tag.
     * Return these phase1.Image objects in an ArrayList of phase1.Image objects.
     *
     * @param tag: A String object that represents a tag.
     * @return ArrayList<Image>
     */
    public static ArrayList<String> searchImagesByTag(String tag) {
        ArrayList<String> foundImages = new ArrayList<>();
        for (Directory dir : directories) {
            for (Image img : dir.getImageList()) {
                if (img.getTagList().contains(tag)) {
                    foundImages.add(img.getImageName());
                }
            }
        }

        return foundImages;
    }


    /**
     * Adds a new Directory to the current list of directories.
     *
     * @param directory: A directory object to be added to the list of directories.
     */
    public void addDirectory(Directory directory) {
        directories.add(directory);
        for (String tag : directory.getTags()){
            if (!tagList.contains(tag)){
                tagList.add(tag);
            }
        }
    }

    /**
     * Returns the appropriate directory by a specified path.
     *
     * @param path: the path of the directory in the user's computer.
     * @return Directory
     */
    public Directory getDirectoryByPath(String path) {
        for (Directory d : directories) {
            if (d.getDirectoryFile().getAbsolutePath().equals(path)) {
                return d;
            }
        }
        return null;
    }

    /**
     * Returns an ArrayList of the current directories.
     *
     * @return ArrayList<Directory>
     */
    public ArrayList<Directory> getDirectories() {
        return directories;
    }


    /**
     * Writes this SystemManager to a serialized config file to be used in the future.
     */
    void serializeToConfig(){
        this.configDirectories = directories;
        this.configTagList = tagList;
        try {
            FileOutputStream fileOut =
                    new FileOutputStream(this.configFilePath);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    /**
     * Configures the program to the appropriate settings specified in our
     * configuration files. Any Images that were tagged or directories used
     * will have appropriate Image and phase1.Directory objects that contain the appropriate
     * tags and Image objects. The system's directories ArrayList will also
     * be updated accordingly.
     */
    void deserializeConfig(){
        try {
            FileInputStream fileIn = new FileInputStream(this.configFilePath);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            SystemManager manager = (SystemManager) in.readObject();
            in.close();
            fileIn.close();
            SystemManager.tagList = manager.configTagList;
            SystemManager.directories = manager.configDirectories;

        } catch (EOFException ignored){
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("SystemManager class not found");
            c.printStackTrace();
        }

    }

    /**
     * Updates the current tagList and then returns it
     * @return this SystemManagers tag list
     */
    public ArrayList<String> getTags(){
        // Figure out a way to change updating tags.
        return tagList;
    }

    /**
     * Searches for a Image with the name 'name'. If there is not a image it returns null
     * @param name the search criteria
     * @return The searched for Image
     */
    public static Image searchImageByName(String name){
        ArrayList<Image> list = new ArrayList<>();
        for (Directory dir: directories) {
            for (Image img : dir.getImageList()) {
                if (img.getImageName().equals(name)){
                    list.add(img);
                }
            }
        }
        if (list.size() == 0){
            return null;
        }
        return list.get(list.size()-1);
    }

    /**
     * Takes the given tag(s) and adds it to this SxystemManager's
     * current list of tags. If there are multiple tags, then each
     * tag must be separated by a comma.
     *
     * @param newTags: The new tag(s) to be added.
     */
    void addNewTag(String newTags){
        String[] tags = newTags.split(",");
        for (String obj : tags) {
            if  (obj.trim().charAt(0) == '@'){
                if (!tagList.contains(obj.trim()) && !obj.trim().equals("")){
                    tagList.add(obj.trim());
                }
            } else if ( obj.trim().charAt(0) != '@') {
                if (!tagList.contains("@" + obj.trim()) && !obj.trim().equals("")) {
                    tagList.add("@" + obj.trim());
                }
            }
        }
    }

    /**
     * Removes the specified tag from this SystemManager's current
     * list of tags.
     *
     * @param tag: String
     */
    void deleteTagFromSystem(String tag){
        for (Directory directory : directories) {
            directory.deleteTag(tag);
        }
        tagList.remove(tag);
    }

    /**
     * Used in Testing to Clean the static variable
     */
    public void cleanDirectories(){
        directories = new ArrayList<>();
    }


    /**
     * Compares this SystemManager with another Object and returns if they are equal
     * @param other : The other Object to compare with
     * @return boolean : Whether or not this SystemManager is equal to the Object other
     */
    @Override
    public boolean equals(Object other) {
        return other instanceof SystemManager && directories.equals(((SystemManager) other).getDirectories()) &&
                this.configFilePath.equals(((SystemManager) other).configFilePath);
    }

    /**
     * Creates a pie chart which represents the amount of occurrences of tags in relation to total occurrences
     * @return Map : a Map who's keys are tags found in the system and its values are the percentage of total occurrences
     * of tags it's responsible for.
     */
    public Map<String,Float> produceTagStatsMap() {
        float fullCount = 0;
        Map<String, Float> result = new HashMap<>();
        for (String tag:tagList) {
            float count = 0;
            for(Directory directory : directories){
                for (Image img : directory.getImageList()){
                    if(img.getTagList().contains(tag)){
                        fullCount++;
                        count++;
                    }
                }
            }
            result.put(tag, count);
        }
        for (Map.Entry<String, Float> pair :result.entrySet()){
            String key = pair.getKey();
            Float value = pair.getValue();
            float percent = (value * 100.0f) / fullCount;
            result.put(key, percent);
        }
        return result;
    }
}
