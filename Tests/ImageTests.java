import org.junit.jupiter.api.*;
import phase2.Directory;
import phase2.Image;
import phase2.SystemManager;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Test cases for the class Image.
 */
class ImageTests {
    // The directory used for testing
    private static Directory directory = new Directory("ImagesForUnitTests/");
    // a Image which we will use to conduct tests
    private static Image img = new Image("Soccer.jpeg", directory, new File("ImagesForUnitTests/Soccer.jpeg"));

    /**
     * This is called before every test and is used to reset the variable 'img' to it's original state.
     */
    @BeforeEach
    void setUp(){
        File f = new File("ImagesForUnitTests/Soccer.jpeg");
        img.setFile(f);
        img.setImageName("Soccer.jpeg");
        img.getTagList().clear();
        img.getNameList().clear();
        img.getNameList().add("Soccer.jpeg");
        img.setDirectory(directory);
    }

    /**
     * This is called after all tests to return the variable 'img' to its original state.
     */
    @AfterAll
    static void TearDown(){
        File f = new File("ImagesForUnitTests/Soccer.jpeg");
        img.getFile().renameTo(f);
        img.setImageName("Soccer.jpeg");
        SystemManager.tagList.clear();

    }

    /**
     * Testing the getImage method by checking the value returned when it is called.
     */
    @Test
    void getImageNameTest(){

        assertEquals("Soccer.jpeg", img.getImageName());
    }

    /**
     * Testing how the methods addTags and deleteTag behave by adding/deleting single tags and checking for expected values.
     */
    @Test
    void addAndDeleteSingleTagTest(){
        //Add the tag
        assertEquals(0, img.getTagList().size());
        img.addTags("Test");
        assertEquals(1, img.getTagList().size());
        assertEquals("@Test", img.getTagList().get(0));
        assertEquals("Soccer @Test.jpeg", img.getImageName());
        //Delete the tag
        assertEquals(1, img.getTagList().size());
        img.deleteTag("@Test");
        assertEquals(0, img.getTagList().size());
        assertEquals("Soccer.jpeg", img.getImageName());

        //For some reason the file doesnt update fast enough?
    }

    /**
     * Testing how the methods addTags and deleteTag behave by adding/deleting multiple tags and checking for expected values.
     */
    @Test
    void addAndDeleteMultipleTest(){
        ArrayList<String> tags = new ArrayList<>();
        tags.add("@Test");
        tags.add("@Test1");
        tags.add("@Test2");
        tags.add("@Test3");
        img.addTags("@Test, Test1, @Test2, Test3");
        assertEquals(tags, img.getTagList());
        for(String tag:tags){
            img.deleteTag(tag);
        }
        assertEquals(new ArrayList<>(), img.getTagList());

    }

    /**
     * Testing how the method deleteTag behaves by deleting multiple tags and checking for expected values.
     */
    @Test
    void deleteTagTest(){
        if(img.getTagList().size() == 0){
            img.addTags("Test");
        }
        assertEquals(1, img.getTagList().size());
        img.deleteTag("@Test");
        assertEquals(0, img.getTagList().size());
        assertEquals("Soccer.jpeg", img.getImageName());
    }

    /**
     * Testing if the url of a expected Image is correct
     */
    @Test
    void getURLTest(){
        File file = new File("ImagesForUnitTests/Soccer.jpeg");
        try {
            URL url = file.toURI().toURL();
            assertEquals(url, img.getUrl());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests setImage by setting an image and checking if the name changes as expected.
     */
    @Test
    void setImageName(){
        assertEquals("Soccer.jpeg", img.getImageName());
        img.setImageName("Test");
        assertEquals("Test", img.getImageName());
    }

    /**
     * Testing the getDirectoryPath method by checking the value returned when it is called.
     */
    @Test
    void getDirectoryPathTest(){
        assertEquals(directory, img.getDirectory());
        assertEquals(directory.getDirectoryFile().getAbsolutePath(), img.getDirectory().getDirectoryFile().getAbsolutePath());
    }

    /**
     * Testing getImageNameNoExtension method by checking the value returned when it is called.
     */
    @Test
    void getImageNameNoExtension(){
        assertEquals("Soccer", img.getImageNameNoExtension());
    }

    /**
     * Testing getLastUsedName method by checking the value returned when it is called.
     */
    @Test
    void getLastUsedNameTest(){
        img.addTags("Test");
        assertEquals("Soccer @Test.jpeg", img.getImageName());
        assertEquals("Soccer.jpeg", img.getLastUsedName());
    }

    /**
     * Testing getNameList method by checking if the values in the returned list are the same as a list filled with the
     * expected values.
     */
    @Test
    void getNameListTest(){
        img.addTags("Test");
        assertEquals("Soccer @Test.jpeg", img.getImageName());
        ArrayList<String> names = new ArrayList<>();
        names.add("Soccer.jpeg");
        names.add("Soccer @Test.jpeg");
        //names.add("Soccer @Test @tag1.jpeg");
        assertEquals(names, img.getNameList());
    }

    /**
     * Testing renameToOldName method by renaming 'img' to one of it's old names and checking that it is updated correctly.
     * Also testing for a case where the oldName passed in is not a old name of 'img'
     */
    @Test
    void renameToOldNameTest(){
        img.addTags("Test");
        assertEquals("Soccer @Test.jpeg", img.getImageName());
        img.renameToOldImage("Soccer.jpeg");
        assertEquals("Soccer.jpeg", img.getImageName());
        img.renameToOldImage("Test! @tag.png");
        assertEquals("Soccer.jpeg", img.getImageName());
        img.renameToOldImage("Soccer @Test.jpeg");
        assertEquals("Soccer @Test.jpeg", img.getImageName());
        img.addTags("Kory McLean");
        assertEquals("Soccer @Test @Kory McLean.jpeg", img.getImageName());
    }

    /**
     * Testing getFile method by checking it's returned value with a expected identical File.
     */
    @Test
    void getFileTest(){
        File file = new File("ImagesForUnitTests/Soccer.jpeg");
        assertEquals(file.getAbsolutePath(), img.getFile().getAbsolutePath());
    }

    /**
     * Testing filtering tags from a images name when it is constructed.
     */
    @Test
    void extractTagsFromNameTest(){
        Image img = new Image("meme @tag1 @tag2 @tag3.jpg", directory, new File(directory.getDirectoryPath()
        + "meme @tag1 @tag2 @tag3.jpg"));
        ArrayList<String> tags = new ArrayList<>();
        tags.add("@tag1");
        tags.add("@tag2");
        tags.add("@tag3");
        Collections.sort(tags);

        ArrayList<String> actual = img.getTagList();
        Collections.sort(actual);

        assertEquals(tags, actual);
    }

    /**
     * Testing move method by moving 'img' and then checking it's directory path.
     */
    @Test
    void moveTest(){
        assertEquals("ImagesForUnitTests/", img.getDirectory().getDirectoryPath());
        img.move("src");
        assertEquals("src/", img.getDirectory().getDirectoryPath());
        img.move("ImagesForUnitTests");
        assertEquals("ImagesForUnitTests/", img.getDirectory().getDirectoryPath());
    }

    /**
     * Testing setFile method by calling it via 'img' and checking if it's File has changed.
     */
    @Test
    void setFileTest(){
        File original = new File("ImagesForUnitTests/Soccer.jpeg");
        assertEquals(original.getAbsolutePath(), img.getFile().getAbsolutePath());

        File newFile = new File("ImagesForUnitTests/pizza @tag1.jpeg");
        img.setFile(newFile);
        assertEquals(newFile.getAbsolutePath(), img.getFile().getAbsolutePath());

        img.setFile(original);
        assertEquals(original.getAbsolutePath(), img.getFile().getAbsolutePath());
    }

    /**
     * Testing equals method by calling it and comparing its result with the expected values.
     */
    @Test
    void equalsTest(){
        Image trueImage = new Image(img.getImageName(), img.getDirectory(), img.getFile());
        assertTrue(img.equals(trueImage));
        Image falseImage = new Image("meme @tag1 @tag2 @tag3.jpg", directory, new File(directory.getDirectoryPath()
                + "meme @tag1 @tag2 @tag3.jpg"));
        assertFalse(img.equals(falseImage));
    }
}
