import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.*;
import phase2.Directory;
import phase2.Image;
import phase2.StartViewController;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;

/**
 * Test cases for the StartViewController class.
 */
class StartViewControllerTests {
    // The controller used for the tests
    private static StartViewController controller = new StartViewController();
    // A directory used for testing
    private static Directory directory = new Directory("ImagesForUnitTests/");
    //
    private static Image image = new Image("Soccer.jpeg", directory, new File("ImagesForUnitTests/Soccer.jpeg"));

    /**
     * This is called before all tests to set the image and directory of controller
     */
    @BeforeAll
    static void setUp(){
        controller.setImage(image);
        controller.setDirectory(directory);
    }

    /**
     * Called before each test to set the image of controller to image
     */
    @BeforeEach
    void setImage(){
        image.getTagList().clear();
        controller.setImage(image);
    }

    /**
     * Testing getImage method by checking it's returned value with a expected identical File.
     */
    @Test
    void getImageTest(){
        assertEquals(image, controller.getImage());
    }

    /**
     * Testing setImage method by calling it via 'controller' and checking if it's Image has changed.
     */
    @Test
    void setImageTest(){
        Image img = new Image("pizza @tag1.jpg", directory, new File("ImagesForUnitTests/pizza @tag1.jpg"));
        assertEquals(image, controller.getImage());
        controller.setImage(img);
        assertEquals(img, controller.getImage());
    }

    /**
     * Testing how the method addTags behaves by adding multiple tags to controller and checking its Image's tagList for
     * expected values.
     */
    @Test
    void addTagsTest(){
        controller.addTags("Tag1, @Tag2, Tag3");
        ArrayList<String> tags = new ArrayList<>();
        tags.add("@Tag1");
        tags.add("@Tag2");
        tags.add("@Tag3");
        assertEquals(tags, controller.getImage().getTagList());
    }

    /**
     * Testing how the method deleteTags behaves by deleting multiple tags and checking for expected values.
     */
    @Test
    void deleteTagsTest(){
        controller.addTags("Tag");
        assertTrue(controller.getImage().getImageName().contains("Tag"));
        assertEquals("Soccer @Tag.jpeg", controller.getImage().getImageName());
        ObservableList<String> tagToBeDeleted = FXCollections.observableArrayList();
        tagToBeDeleted.add("@Tag");
        controller.deleteTags(tagToBeDeleted);
        assertFalse(controller.getImage().getImageName().contains("Tag"));
        assertEquals("Soccer.jpeg", controller.getImage().getImageName());
    }

    /**
     * Testing how the methods getDirectory and setDirectory behave by calling them via 'controller' and checking expected
     * values.
     */
    @Test
    void getAndSetDirectory(){
        assertEquals(directory, controller.getDirectory());
        Directory directory2 = new Directory("test_images/");
        controller.setDirectory(directory2);
        assertEquals(directory2, controller.getDirectory());
        controller.setDirectory(directory);
        assertEquals(directory, controller.getDirectory());
    }

}
