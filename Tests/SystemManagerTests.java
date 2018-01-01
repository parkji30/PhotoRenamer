import org.junit.jupiter.api.*;
import phase2.Directory;
import phase2.Image;
import phase2.SystemManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for the class SystemManager.
 */
public class SystemManagerTests {
    private static SystemManager manager = new SystemManager();


    /**
     * This is called before all tests to add a directory to the manager.
     */
    @BeforeAll
    static void setUp(){
        Directory directory = new Directory("ImagesForUnitTests/");
        manager.addDirectory(directory);
    }

    /**
     * This is called before each test to reset the directory used for testing.
     */
    @BeforeEach
    void cleanManager(){
        manager.cleanDirectories();
        Directory directory = new Directory("ImagesForUnitTests/");
        manager.addDirectory(directory);

    }


    /**
     * Testing the getTags method by comparing the list returned when it is called and a list of expected values.
     */
    @Test
    void getTagsTest(){
        ArrayList<String> tags = new ArrayList<>();
        tags.add("@Tag");
        tags.add("@Test");
        tags.add("@tag1");
        tags.add("@tag2");
        tags.add("@tag3");
        tags.add("@testertag");

        Collections.sort(tags);

        ArrayList<String> actual = manager.getTags();

        Collections.sort(actual);

        assertLinesMatch(tags, actual);
        assertEquals(6, manager.getTags().size());
    }

    /**
     * Tests the searchImageByTag method by comparing it's returned Images with expected Images
     */
    @Test
    void searchImagesByTagTest(){
        Image image1 = new Image("Soccer @Test.jpeg", manager.getDirectories().get(0),
                new File("ImagesForUnitTests/Soccer @Test.jpeg"));
        Image image2 = new Image("pizza @tag1.jpg", manager.getDirectories().get(0),
                new File("ImagesForUnitTests/pizza @tag1.jpg"));
        assertEquals(2, SystemManager.searchImagesByTag("@tag1").size());
        assertEquals(image1.getImageName(), SystemManager.searchImagesByTag("@Test").get(0));
        assertEquals(image2.getImageName(), SystemManager.searchImagesByTag("@tag1").get(1));
    }


    /**
     * Testing the getDirectories method by comparing the list returned when it is called and a list of expected values.
     */
    @Test
    void getDictionariesTest(){
        Directory directory = new Directory("ImagesForUnitTests/");
        assertEquals(1, manager.getDirectories().size());
        assertEquals(directory.getDirectoryPath(), manager.getDirectories().get(0).getDirectoryPath());
    }

    /**
     * Tests the searchImageByName method by comparing it's returned Image with a expected Image
     */
    @Test
    void searchImageByNameTest(){
        Image image1 = new Image("Soccer @Test.jpeg", manager.getDirectories().get(0),
                new File("ImagesForUnitTests/Soccer @Test.jpeg"));
        assertEquals(image1, SystemManager.searchImageByName("Soccer @Test.jpeg"));
    }

    /**
     * Testing the getDirectoriesByPath method by comparing the Directory returned when it is called and a expected identical
     * Directory.
     */
    @Test
    void getDirectoryByPathTest(){
        Directory directory = new Directory("ImagesForUnitTests/");
        assertEquals(directory, manager.getDirectoryByPath(directory.getDirectoryFile().getAbsolutePath()));
    }

    /**
     * Testing produceTagsStatsMap by comparing it's returned Map with a Map of expected values
     */
    @Test
    void produceTagStatsMapTest() {
        manager.addDirectory(new Directory("test_images/"));
        Map<String, Float> expected = new HashMap<>();
        expected.put("@tag1", ((float) 3 * 100.0f) / (float) 8);
        expected.put("@tag2", ((float) 1 * 100.0f) / (float) 8);
        expected.put("@tag3", ((float) 1 * 100.0f) / (float) 8);
        expected.put("@Test", ((float) 1 * 100.0f) / (float) 8);
        expected.put("@Tag", ((float) 1 * 100.0f) / (float) 8);
        expected.put("@testertag", ((float) 1 * 100.0f) / (float) 8);

        assertEquals(expected, manager.produceTagStatsMap());

    }
}
