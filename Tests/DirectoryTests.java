import org.junit.jupiter.api.*;
import phase2.Directory;
import phase2.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for the class Directory
 */
class DirectoryTests {
    // The directory used for testing
    private Directory directory = new Directory("ImagesForUnitTests/");

    /**
     * Testing the getDirectoryPath method by comparing the value returned when it is called.
     */
    @Test
    void getDirectoryPathTest() {
        assertEquals("ImagesForUnitTests/", directory.getDirectoryPath());
    }

    /**
     * Testing the getTags method by comparing the list returned when it is called and a list of expected values.
     */
    @Test
    void getTagsTest() {
        ArrayList<String> expected = new ArrayList<>();
        expected.add("@tag1");
        expected.add("@tag2");
        expected.add("@tag3");
        expected.add("@Test");
        expected.add("@Tag");
        Collections.sort(expected);

        ArrayList<String> actual = directory.getTags();
        Collections.sort(actual);
        assertEquals(expected, actual);
        assertEquals(5, directory.getTags().size());
    }

    /**
     * Testing the getDirectoryFile method by comparing the File returned and a identical expected File.
     */
    @Test
    void getDirectoryFileTest() {
        File expected = new File("ImagesForUnitTests/");
        File actual = directory.getDirectoryFile();
        assertEquals(expected.getAbsolutePath(), actual.getAbsolutePath());
    }

    /**
     * Testing extractImages method by comparing the amount of images found when constructing a new directory as extractImages
     * is called in the constructor.
     */
    @Test
    void extractImagesTest() {
        //This method is called when a new directory is constructed
        int expected = 6;
        assertEquals(expected, directory.getImageList().size());
    }

    /**
     * Testing isImage by passing in different types of files as parameters and asserting expected values.
     */
    @Test
    void isImageTest() {
        //False
        File testNoneImage = new File("ImagesForUnitTests/Text.txt");
        assertFalse(directory.isImage(testNoneImage));

        //False
        File testNoneImageWithWrongExtension = new File("ImagesForUnitTests/ImbeddedImages/Test.png.meta");
        assertFalse(directory.isImage(testNoneImageWithWrongExtension));

        File trueTest = new File("ImagesForUnitTests/Soccer.jpg");
        assertTrue(directory.isImage(trueTest));
    }

    /**
     * Testing getImageNames method by comparing the returned list with a list that has the expected values in it.
     */
    @Test
    void getImageNamesTest() {
        ArrayList<String> expected = new ArrayList<>();
        expected.add("theoffice @Tag.jpg");
        expected.add("pizza @tag1.jpg");
        expected.add("Soccer @Test.jpeg");
        expected.add("meme @tag1 @tag2 @tag3.jpg");
        expected.add("cow.png");
        expected.add("watermelon.jpg");
        //Sort expected
        Collections.sort(expected);
        ArrayList<String> actual = directory.getImagesNames();
        //Sort actual
        Collections.sort(actual);
        assertEquals(expected, actual);
    }

    /**
     *  Testing removeImage method by analyzing the contents of directory's imageList.
     */
    @Test
    void removeImageTest(){
        Image img = directory.getImageList().get(0);
        directory.removeImage(img);
        assertFalse(directory.getImageList().contains(img));
    }

    /**
     *  Testing addImage method by analyzing the contents of directory's imageList.
     */
    @Test
    void addImageTest(){

        Image img = new Image("test.jpg", directory, new File(directory.getDirectoryPath()
                + "test.jpg"));
        assertFalse(directory.getImageList().contains(img));
        directory.addImage(img);
        assertTrue(directory.getImageList().contains(img));
    }

    /**
     *  Testing deleteTag method by analyzing the contents of directory's tags.
     */
    @Test
    void deleteTagTest(){
        Image img = directory.getImageList().get(0);
        directory.removeImage(img);
        assertFalse(directory.getImageList().contains(img));
    }

}