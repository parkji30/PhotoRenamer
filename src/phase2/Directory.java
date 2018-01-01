package phase2;

import java.io.Serializable;
import java.util.ArrayList;
import java.io.File;

public class Directory implements Serializable {

    // The list of Images in this phase1.Directory.
    private ArrayList<Image> imageList = new ArrayList<>();

    // The path to this phase1.Directory
    private String directoryPath;

    // The file that this phase1.Directory object refers to.
    private File directoryFile;

    // A list of all tags within this phase1.Directory
    private ArrayList<String> tags = new ArrayList<>();


    /**
     * Constructs a new phase1.Directory object which stores an image
     * object along with the file path to this directory as a
     * String.
     * A File object is also represents the directory file.
     *
     * @param directoryPath: The file path of the phase1.Image.
     */
    public Directory(String directoryPath) {
        this.directoryPath = directoryPath;
        this.directoryFile = new File(directoryPath);
        this.imageList = extractImages();
        updateTags();
    }


    /**
     * Returns the path of this phase1.Directory located in the users
     * computer.
     *
     * @return String
     */
    public String getDirectoryPath() {
        return this.directoryPath;
    }


    /**
     * Updates this directory's current list of tags by going through
     * the directory's image list and filtering out the tags from each
     * image.
     */
    void updateTags() {
        this.tags.clear();
        for (Image i : this.imageList) {
            for (String tag : i.getTagList()) {
                if (!this.tags.contains(tag)) {
                    this.tags.add(tag);
                }
            }
        }
    }


    /**
     * Updates the directory's current list of tags with the method
     * updateTags. Returns the tags present within this phase1.Directory's
     * current list of images.
     *
     * @return ArrayList
     */
    public ArrayList<String> getTags() {
        updateTags();
        return tags;
    }


    /**
     * Returns the directory file that this phase1.Directory object refers
     * to.
     *
     * @return File
     */
    public File getDirectoryFile() {
        return directoryFile;
    }


    /**
     * Extracts all the images contained in this directory file,
     * creating an phase1.Image object that will refer to each one. All
     * the newly created image objects is then added to the directory's
     * current list of images.
     */
    private ArrayList<Image> extractImages() {
        File[] directoryFiles = this.directoryFile.listFiles();
        ArrayList<Image> resultImages = new ArrayList<>();
        if (directoryFiles != null){
            for (File f : directoryFiles) {
                if (isImage(f)) {
                    if(SystemManager.searchImageByName(f.getName()) == null) {
                        Image image = new Image(f.getName(), this, f);
                        resultImages.add(image);
                    }
                    else{
                        resultImages.add(SystemManager.searchImageByName(f.getName()));
                    }
                }
                else if(f.isDirectory()){
                    Directory newDirectory = new Directory(f.getAbsolutePath());
                    resultImages.addAll(newDirectory.extractImages());
                }
            }
        }
        return resultImages;
    }

    /**
     * Checks to see if the given file is a valid image file by checking the
     * extension of the image file.
     * <p>
     * The following extensions are valid image extensions:
     * .tif, .jpeg, .jpg, .png, .gif, .jpgs
     *
     * @param file: the file to be checked.
     * @return boolean
     */
    public boolean isImage(File file) {
        String[] imageFileTypes = new String[]{".tif", ".jpeg", ".jpg", ".png", ".gif", ".JPG", ".jpgs"};
        if (file.isDirectory()) {
            return false;
        }
        else {
            for (String extension : imageFileTypes) {
                if (file.getName().contains(".") && file.getName().substring(file.getName().lastIndexOf('.')).contains(extension)) {
                    return true;
                }
            }
            return false;
        }
    }


    /**
     * Returns the ArrayList containing all Image objects that
     * this phase1.Directory holds.
     *
     * @return ArrayList<Image>
     */
    public ArrayList<Image> getImageList() {
        return this.imageList;
    }

    /**
     * Returns the names of all Images in this Directory's ImageList
     * @return ArrayList All the Images names.
     */
    public ArrayList<String> getImagesNames(){
        ArrayList<String> names = new ArrayList<>();
        for (Image img : this.imageList){
         names.add(img.getImageName());
        }
        return names;
    }

    /**
     * Compares this Directory with another Object and returns if they are equal
     * @param other : The other Object to compare with
     * @return boolean : Whether or not this Directory is equal to the Object other
     */
    @Override
    public boolean equals(Object other) {
        return other instanceof Directory && this.directoryPath.equals(((Directory) other).directoryPath) &&
                this.getDirectoryFile().getAbsolutePath().equals(((Directory) other).getDirectoryFile().getAbsolutePath());
    }

    /**
     * Removes the Image from the Directory's ImageList
     * @param image: the image to be removed.
     */
    public void removeImage(Image image) {
        if (this.imageList.contains(image)) {
            this.imageList.remove(image);
        }
    }

    /**
     * Adds the Image to the phase1.Directory's ImageList
     * @param image: the image to be added.
     */
    public void addImage(Image image) {
        if (!this.imageList.contains(image)) {
            this.imageList.add(image);
        }
    }

    /**
     * Removes the passed in tag from this directory's tagList
     * @param tag : the tag to be removed
     */
    void deleteTag(String tag){
        for (Image img: this.imageList) {
            img.deleteTag(tag);
        }
        updateTags();
    }
}
