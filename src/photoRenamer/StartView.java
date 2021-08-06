package phase2;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.layout.HBox;

import java.io.File;


public class StartView extends Application implements EventHandler<ActionEvent> {

    // path of our directory
    private String directoryPath;

    // Buttons to be added onto the GUI.
    private Button exit;
    private Button searchByTag = new Button("Search By Selected Tag");
    private Button renameToOldName = new Button("Rename to selected Old Name");
    private Button syncButton;
    private Button addToTagList;
    private Button deleteFromTagList;
    private Button addTagsFromTagList;
    private Button addImageTagButton;
    private Button deleteImageTagButton;
    private Button getImageLogButton;
    private Button getMasterLogButton;
    private Button moveImageButton;
    private Button openImageFileButton;

    // Boxes which divide the GUI into different sections.
    private VBox tagListBox;
    private VBox rightBox;
    private VBox rightCenterBox;
    private VBox leftBox;
    private VBox oldNamePanel;


    // Viewable list objects that will hold tags and images..
    private ObservableList<String> imageTags = FXCollections.observableArrayList();
    private ObservableList<String> imagePanel = FXCollections.observableArrayList();
    private ListView<String> imageTagView = new ListView<>(imageTags);
    private ListView<String> imageListView;
    private ListView<String> tagListView;
    private ImageView imageViewPanel;
    private TextField tagInputField = new TextField();
    private TextField imageTagInputField = new TextField();

    // Labels to be added to the GUI.
    private Label fileNameLabel;
    private Label currentDir;
    private Label allTagsList;
    private Label tagListLabel;
    private Label statsLabel = new Label("");
    private Label imageAbsPath;

    // Creates a new controller for the application.
    private StartViewController startViewController = new StartViewController();

    // Launches the JavaFX
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage ps) throws Exception {

        // Deserialize any old Image, Directory or LogFiles that were used.
        startViewController.getSystemManager().deserializeConfig();
        ps.setTitle("CSC207 Phase 2");
        oldNamePanel = renameToOldNamePanel();

        // Creates new buttons onto the GUI.
        exit = new Button("Exit");
        syncButton = new Button("Sync");
        Button findImageDirectory = new Button("Find Image Directory");
        Button selectImage = new Button("Select Image");
        // TagList Buttons
        addToTagList = new Button("Add Tag(s) to Tag List");
        deleteFromTagList = new Button("Delete Selected Tag(s)");
        addTagsFromTagList = new Button("Add Selected Tags to Image");
        // ImageTag Buttons
        addImageTagButton = new Button("Add Tag(s) to Image");
        deleteImageTagButton = new Button("Delete Selected Tag(s)");
        getImageLogButton = new Button("Open Image Tag Log");
        moveImageButton = new Button("Move Image Location");
        openImageFileButton = new Button("Open Image Directory");
        addTagsFromTagList = new Button("Add Selected Tags to Image");
        getMasterLogButton = new Button("Open Master Log");

    // Special Feature Button
        Button openPieChartButton = new Button("Open Stats.");

        // Creates new Labels onto the GUI.
        tagListLabel = new Label("Tags:");
        Label results = new Label("Results:");
        fileNameLabel = new Label(startViewController.getImage().getImageName());
        fileNameLabel.setAlignment(Pos.TOP_CENTER);
        imageAbsPath = new Label(this.startViewController.getImage().getFile().getAbsolutePath());
        currentDir = new Label();
        allTagsList = new Label();
        allTagsList.setText("All Existing Tags:");

        // Sets the preferred width of the buttons
        findImageDirectory.setPrefWidth(150);
        exit.setPrefWidth(150);
        searchByTag.setPrefWidth(150);
        syncButton.setPrefWidth(150);
        searchByTag.setPrefWidth(250);
        selectImage.setPrefWidth(150);
        openPieChartButton.setPrefWidth(150);
        renameToOldName.setPrefWidth(180);
        searchByTag.setPrefWidth(200);
        addToTagList.setPrefWidth(200);
        addTagsFromTagList.setPrefWidth(200);
        deleteFromTagList.setPrefWidth(200);
        addImageTagButton.setPrefWidth(180);
        deleteImageTagButton.setPrefWidth(180);
        getImageLogButton.setPrefWidth(180);
        moveImageButton.setPrefWidth(180);
        openImageFileButton.setPrefWidth(180);
        getMasterLogButton.setPrefWidth(180);

        // Creates new boxes to add the GUI objects into.
        HBox windowBox = new HBox();
        VBox leftCenterBox = new VBox();
        VBox lowerLeftCenterBox = new VBox();
        VBox upperLeftCenterBox = new VBox();
        rightBox = new VBox();
        rightCenterBox = new VBox();
        leftBox = new VBox();

        // Creates a viewing panel for the Image and the list of tags.
        tagListView = new ListView<>();
        tagListView.setPrefSize(100, 500);
        tagListView.setEditable(false);
        tagListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tagListBox = tagListPanel();
        imageListView = new ListView<>(imagePanel);
        imageListView.setPrefSize(200, 250);
        imageListView.setEditable(false);
        imageListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        imageViewPanel = imageViewPanel(startViewController.getImage());
        imageViewPanel.setFitWidth(300);
        imageViewPanel.setFitHeight(300);
        imageTagView.setPrefSize(200, 250);
        imageTagView.setEditable(false);
        imageTagView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Sets the action of these buttons
        exit.setOnAction(this);
        syncButton.setOnAction(e -> sync());
        findImageDirectory.setOnAction(e -> findDirectory());
        selectImage.setOnAction(e -> selectImage());
        addToTagList.setOnAction(e -> startViewController.addNewExistingTag(tagInputField.getText()));
        moveImageButton.setOnAction(e -> startViewController.moveImage());
        getImageLogButton.setOnAction(e -> startViewController.openImageLogFile());
        openImageFileButton.setOnAction(e -> startViewController.getImage().open());
        addImageTagButton.setOnAction(e -> startViewController.addTags(imageTagInputField.getText()));
        getMasterLogButton.setOnAction(e -> startViewController.openMasterLogFile());


        // Our unique special feature for our phase2.
        openPieChartButton.setOnAction(
                event -> {
                    final Stage dialog = new Stage();
                    dialog.initModality(Modality.APPLICATION_MODAL);
                    dialog.initOwner(ps);
                    VBox dialogVbox = new VBox(20);
                    dialogVbox.getChildren().add(tagPopularityChart());
                    dialogVbox.getChildren().add(statsLabel);
                    Scene dialogScene = new Scene(dialogVbox, 800, 400);
                    dialog.setScene(dialogScene);
                    dialog.show();
                });

        // Adds all the GUI objects to different boxes and sets their alignment.
        lowerLeftCenterBox.getChildren().addAll(findImageDirectory, results, imageListView, selectImage, openPieChartButton, exit);
        lowerLeftCenterBox.setAlignment(Pos.BOTTOM_CENTER);
        lowerLeftCenterBox.setSpacing(5);
        upperLeftCenterBox.getChildren().add(currentDir);
        upperLeftCenterBox.setAlignment(Pos.TOP_CENTER);
        upperLeftCenterBox.setSpacing(5);
        leftCenterBox.getChildren().addAll(upperLeftCenterBox, lowerLeftCenterBox);
        leftCenterBox.setAlignment(Pos.CENTER_LEFT);
        rightCenterBox.getChildren().addAll(imageAbsPath, fileNameLabel, imageViewPanel, syncButton, oldNamePanel);
        rightCenterBox.setAlignment(Pos.CENTER);
        rightCenterBox.setSpacing(5);
        rightBox.getChildren().add(imageTagPanel());
        leftBox.getChildren().add(tagListBox);
        leftBox.setAlignment(Pos.CENTER);
        windowBox.getChildren().addAll(leftBox, leftCenterBox, rightCenterBox, rightBox);
        windowBox.setAlignment(Pos.CENTER);
        windowBox.setSpacing(10);

        // Creates the entire GUI with all its objects and displays it.
        Scene startView = new Scene(windowBox, 850, 600);
        ps.setScene(startView);
        ps.show();
    }

    /**
     * Handles the event where the user clicks the exit button.
     * Serializes any change made in objects created for referral in the
     * future.
     *
     * @param event: The event when the user clicks the exit button.
     */
    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == exit) {
            startViewController.getSystemManager().serializeToConfig();
            System.exit(0);
        }
    }

    /**
     * Lets the user select an image from the viewable list of
     * images in order to modify tags for the image.
     * The image that is selected in displayed onto the GUI.
     */
    private void selectImage() {
        String selectedImage;
        selectedImage = imageListView.getSelectionModel().getSelectedItem();
        fileNameLabel.setText(selectedImage);
        Image thisImage = SystemManager.searchImageByName(selectedImage);
        if (thisImage != null) {
            startViewController.setImage(thisImage);
            String imgDirectory = this.startViewController.getImage().getDirectory().getDirectoryFile().getAbsolutePath();
            this.startViewController.getImage().setFile(new File(imgDirectory +
                    "/" + this.startViewController.getImage().getImageName()));
            fileNameLabel.setText(this.startViewController.getImage().getImageName());
            imageViewPanel = imageViewPanel(this.startViewController.getImage());
            imageViewPanel.setFitWidth(300);
            imageViewPanel.setFitHeight(300);
            while (rightCenterBox.getChildren().size() > 0) {
                rightCenterBox.getChildren().remove(0);
            }
            imageAbsPath = new Label(this.startViewController.getImage().getFile().getAbsolutePath());
            oldNamePanel = renameToOldNamePanel();
            rightCenterBox.getChildren().addAll(imageAbsPath, fileNameLabel, imageViewPanel, syncButton, oldNamePanel);
        }
    }


    /**
     * Calls the findDirectoryPath method from the StartViewController
     * which allows the user to open a directory chooser and select a
     * directory of his/her choice.
     * A list of all images contained within that directory is displayed
     * in the imagePanel.
     */
    private void findDirectory() {
        directoryPath = startViewController.findDirectoryPath();
        Directory newDir;
        if (directoryPath != null) {
            if (startViewController.getSystemManager().getDirectoryByPath(directoryPath) == null) {
                newDir = new Directory(directoryPath);
                this.startViewController.getSystemManager().addDirectory(newDir);
            } else {
                newDir = startViewController.getSystemManager().getDirectoryByPath(directoryPath);
            }
            this.startViewController.setDirectory(newDir);
            currentDir.setText(directoryPath);
            imagePanel.clear();
            imagePanel.addAll(this.startViewController.getDirectory().getImagesNames());
            imageListView.setItems(imagePanel);
        }
    }

    /**
     * Creates a viewable list of tags onto the GUI by searching for
     * all existing tags of the selected Image.
     *
     * @return VBox
     */
    private VBox tagListPanel() {
        VBox box = new VBox();
        tagInputField.clear();
        tagInputField.setPromptText("E.g.: tag1, tag2, tag3... ");
        ObservableList<String> tags = FXCollections.observableArrayList();
        tags.addAll(startViewController.getSystemManager().getTags());
        tagListView.setItems(tags);
        tagListView.getSelectionModel().selectedItemProperty().addListener(
                (ov, old_val, new_val) -> searchByTag.setOnAction(e -> searchByTagList(new_val)));
        deleteFromTagList.setOnAction(e -> startViewController.deleteExistingTag(tagListView.getSelectionModel().getSelectedItems()));
        addTagsFromTagList.setOnAction(e -> startViewController.addTagList(tagListView.getSelectionModel().getSelectedItems()));
        box.getChildren().addAll(allTagsList, tagListView, tagInputField, addToTagList,
                deleteFromTagList, searchByTag, addTagsFromTagList);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    /**
     * Displays the given image object onto GUI.
     *
     * @param img: The image object to be displayed.
     * @return ImageView
     */
    private ImageView imageViewPanel(Image img) {
        javafx.scene.image.Image image = new javafx.scene.image.Image(String.valueOf(img.getUrl()));
        ImageView iv = new ImageView();
        iv.setImage(image);
        return iv;
    }

    /**
     * Creates the right panel of the GUI which allows the User
     * to modify tags of the Image.
     *
     * @return VBox
     */
    private VBox imageTagPanel() {
        VBox panel = new VBox();
        imageTagInputField.setPromptText("E.g.: tag1, tag2, tag3... ");
        imageTags.clear();
        imageTags.addAll(startViewController.getImage().getTagList());
        imageTagView.setItems(imageTags);
        deleteImageTagButton.setOnAction(e -> startViewController.deleteTags(imageTagView.getSelectionModel().getSelectedItems()));
        panel.getChildren().addAll(tagListLabel, imageTagView, imageTagInputField,
                addImageTagButton, deleteImageTagButton, moveImageButton, openImageFileButton, getImageLogButton, getMasterLogButton);
        panel.setAlignment(Pos.BOTTOM_CENTER);
        panel.setSpacing(5);
        return panel;
    }


    /**
     * Allows us to update a Image's name and update the GUI to display the changes made.
     */
    private void sync() {
        fileNameLabel.setText(this.startViewController.getImage().getImageName());
        if (rightBox.getChildren().size() > 0) { rightBox.getChildren().remove(0); }
        rightBox.getChildren().add(imageTagPanel());
        startViewController.getImage().updateImageName();
        String imgDirectory = this.startViewController.getImage().getDirectory().getDirectoryFile().getAbsolutePath();
        this.startViewController.getImage().setFile(new File(imgDirectory +
                "/" + this.startViewController.getImage().getImageName()));
        this.tagListBox = tagListPanel();
        this.leftBox.getChildren().clear();
        this.leftBox.getChildren().add(tagListBox);
        updateDirectoryImageFiles();
        updateOldNamePanel();
    }

    /**
     * Looks for all Images in SystemManager's stored Image objects
     * with the given tag.
     *
     * @param tag: The tag to be searched
     */
    private void searchByTagList(String tag) {
        imagePanel.clear();
        imagePanel.addAll(SystemManager.searchImagesByTag(tag));
        imageListView.setItems(imagePanel);
    }

    /**
     * Displays all the old Image names of this object onto the GUI's
     * oldImageName panel.
     *
     * @return VBox
     */
    private VBox renameToOldNamePanel() {
        VBox box = new VBox();
        ObservableList<String> oldNames = FXCollections.observableArrayList();
        oldNames.addAll(startViewController.getImage().getNameList());
        ListView<String> oldNameList = new ListView<>();
        oldNameList.setItems(oldNames);
        oldNameList.setPrefSize(100, 150);
        oldNameList.setEditable(false);
        oldNameList.getSelectionModel().selectedItemProperty().addListener(
                 (ov, old_val, new_val) -> renameToOldName.setOnAction
                         (e -> startViewController.getImage().renameToOldImage(new_val)));
        box.getChildren().addAll(oldNameList, renameToOldName);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private PieChart tagPopularityChart() {
        PieChart pieChart = new PieChart();
        pieChart.setData(startViewController.produceTagStatsMap());
        pieChart.setTitle("Percentage Usage of Tags");
        pieChart.setLabelLineLength(10);
        pieChart.setLegendSide(Side.LEFT);
        statsLabel.setTextFill(Color.BLACK);
        statsLabel.setAlignment(Pos.BOTTOM_CENTER);
        pieChart.getData().forEach(data -> data.getNode().addEventHandler(MouseEvent.ANY, e->{
            statsLabel.setText("                       " + data.getName() + " tags make up " + (data.getPieValue()) +
            "% of all tags");
        }));
        return pieChart;
    }

    /**
     * Updates the Image names in the directory's list of Images.
     */
    private void updateDirectoryImageFiles(){
        currentDir.setText(directoryPath);
        imagePanel.clear();
        imagePanel.addAll(this.startViewController.getDirectory().getImagesNames());
        imageListView.setItems(imagePanel);
    }

    /**
     * Updates the list of old names in the old Names Panel.
     */
    private void updateOldNamePanel(){
        oldNamePanel.getChildren().clear();
        oldNamePanel = renameToOldNamePanel();
        rightCenterBox.getChildren().clear();
        rightCenterBox.getChildren().addAll(fileNameLabel, imageViewPanel, syncButton, oldNamePanel);
    }
}