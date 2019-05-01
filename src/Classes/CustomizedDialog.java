package Classes;

import Main.Controller;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CustomizedDialog {

    private String titleText;
    private String headerText;
    private String imageFile;
    private String contentText;
    private ImageView dialogIcon;
    private Label title;
    private Label header;
    private Label content;
    private Button confirmButton;
    private Button closeButton;
    private HBox titleBar;
    private TextField input;
    private HBox inputField;
    private VBox layout;
    private Stage dialog;
    private String result;

    private double xOffset = 0.0;
    private double yOffset = 0.0;

    public String getResult() {
        return result;
    }

    private void setResult(String result) {
        this.result = result;
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public String getHeaderText() {
        return headerText;
    }

    public void setHeaderText(String headerText) {
        this.headerText = headerText;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public CustomizedDialog(String titleText, String headerText, String imageFile, Stage mainStage, Boolean wait) {
        this.titleText = titleText;
        this.headerText = headerText;
        this.imageFile = imageFile;

        initializeDialog();
        setTitleBar(titleText, imageFile);
        setHeader(headerText);

        confirmButton = new Button("OK");
        confirmButton.setOnAction(e -> dialog.close());

        initializeLayout();
        layout.getChildren().addAll(titleBar, header, confirmButton);
        layout.setOnMousePressed(event -> { xOffset = event.getSceneX(); yOffset = event.getSceneY(); });
        layout.setOnMouseDragged(event -> { dialog.setX(event.getScreenX() - xOffset); dialog.setY(event.getScreenY() - yOffset); });
        showDialogAndWait(mainStage, wait);
    }

    public CustomizedDialog(String titleText, String headerText, String imageFile, String contentText, Stage mainStage, Boolean wait) {
        this.titleText = titleText;
        this.headerText = headerText;
        this.imageFile = imageFile;
        this.contentText = contentText;

        initializeDialog();
        setTitleBar(titleText, imageFile);
        setHeader(headerText);
        setInputField(contentText);

        confirmButton = new Button("OK");
        confirmButton.setOnAction(e -> {setResult(input.getText()); dialog.close();});

        initializeLayout();
        layout.getChildren().addAll(titleBar, header, inputField, confirmButton);
        layout.setOnMousePressed(event -> { xOffset = event.getSceneX(); yOffset = event.getSceneY(); });
        layout.setOnMouseDragged(event -> { dialog.setX(event.getScreenX() - xOffset); dialog.setY(event.getScreenY() - yOffset); });
        showDialogAndWait(mainStage, wait);
    }

    private void showDialogAndWait(Stage mainStage, Boolean wait){
        Screen activeScreen = Screen.getScreensForRectangle(mainStage.getX(), mainStage.getY(), mainStage.getWidth(), mainStage.getHeight()).get(0);
        Rectangle2D window = activeScreen.getBounds();

        Scene scene = new Scene(layout);
        scene.getStylesheets().add("/styles/dark-theme.css");
        dialog.setScene(scene);
        if(wait){
            dialog.setX(window.getMinX() + (window.getWidth() - 437)/2);
            dialog.setY(window.getMinY() + (window.getHeight() - 164)/2);
            dialog.showAndWait();
        }else{
            dialog.show();
            dialog.setX(window.getMinX() + (window.getWidth() - dialog.getWidth())/2);
            dialog.setY(window.getMinY() + (window.getHeight() - dialog.getHeight())/2);
        }
    }

    private void initializeDialog(){
        dialog = new Stage();

        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setMinWidth(250);
        dialog.setMaxWidth(450);
        dialog.setMinHeight(50);
        dialog.setMaxHeight(200);
        dialog.setResizable(false);
    }

    private void initializeLayout(){
        layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-border-color: " + Controller.SECOND_COLOR +"; -fx-background-color: " + Controller.FIRST_COLOR +";");
        layout.setPadding(new Insets(5,5,5,5));
    }

    private void setTitleBar(String titleText, String imageFile){
        titleBar = new HBox(5);
        HBox titleBarHBOX = new HBox(5);
        HBox closeHBOX = new HBox();

        setTitle(titleText);
        setDialogIcon(imageFile);
        setCloseButton();

        titleBar.setMinWidth(dialog.getWidth());
        HBox.setMargin(closeHBOX, new Insets(-5,0,0,0));
        titleBarHBOX.getChildren().addAll(dialogIcon,title);
        titleBarHBOX.setAlignment(Pos.TOP_LEFT);
        closeHBOX.getChildren().add(closeButton);
        closeHBOX.setAlignment(Pos.TOP_RIGHT);
        HBox.setHgrow(closeHBOX, Priority.ALWAYS);
        titleBar.getChildren().addAll(titleBarHBOX,closeHBOX);
        titleBar.setAlignment(Pos.TOP_LEFT);
    }

    private void setHeader(String headerText){
        this.header = new Label();
        this.header.setText(headerText);
        this.header.setMinWidth(225);
        this.header.setMaxWidth(425);
        this.header.setMinHeight(25);
        this.header.setMaxHeight(175);
        this.header.setWrapText(true);
        this.header.setAlignment(Pos.CENTER);
        this.header.setTextAlignment(TextAlignment.CENTER);
    }

    private void setInputField(String contentText){
        inputField = new HBox(5);

        input = new TextField();
        setContent(contentText);

        inputField.getChildren().addAll(content, input);
        inputField.setAlignment(Pos.CENTER);
    }

    private void setContent(String contentText){
        content = new Label();
        content.setText(contentText);
        content.setWrapText(true);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setTextAlignment(TextAlignment.LEFT);
    }

    private void setDialogIcon(String imageFile){
        dialogIcon = new ImageView(new Image(CustomizedDialog.class.getResourceAsStream(imageFile)));
        dialogIcon.setFitWidth(20.0);
        dialogIcon.setFitHeight(20.0);
    }

    private void setTitle(String titleText){
        title = new Label();
        title.setText(titleText);
    }

    private void setCloseButton(){
        closeButton = new Button();
        closeButton.setId("dialogCloseButton");
        closeButton.setMinWidth(25);
        closeButton.setMaxWidth(25);
        closeButton.setPrefWidth(25);
        closeButton.setOnMouseClicked((MouseEvent event) -> { dialog.close(); contentText = null; });
    }

    static void setStyles(Node node, String style, String hoverStyle){
        node.setStyle(style);

        if(hoverStyle != null){
            node.setOnMouseEntered(e -> node.setStyle(hoverStyle));
            node.setOnMouseExited(e -> node.setStyle(style));
        }
    }
}
