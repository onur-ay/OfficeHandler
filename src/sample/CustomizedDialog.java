package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CustomizedDialog {

    private ImageView dialogIcon;
    private Label title;
    private Label header;
    private Label content;
    private Button confirmButton;
    private HBox titleBar;
    private TextField input;
    private HBox inputField;
    private VBox layout;
    private Stage dialog;
    private String result;

    public String getResult() {
        return result;
    }

    private void setResult(String result) {
        this.result = result;
    }

    public CustomizedDialog() {

    }

    public void createAlertDialog(String titleText, String headerText, String imageFile){
        initializeDialog();
        setTitleBar(titleText, imageFile);
        setHeaderText(headerText);

        confirmButton = new Button("OK");
        confirmButton.setOnAction(e -> dialog.close());

        initializeLayout();
        layout.getChildren().addAll(titleBar, header, confirmButton);
        showDialogAndWait();
    }

    public void createTextInputDialog(String titleText, String headerText, String contentText, String imageFile){
        initializeDialog();
        setTitleBar(titleText, imageFile);
        setHeaderText(headerText);
        setInputField(contentText);

        confirmButton = new Button("OK");
        confirmButton.setOnAction(e -> {setResult(input.getText()); dialog.close();});

        initializeLayout();
        layout.getChildren().addAll(titleBar, header, inputField, confirmButton);
        showDialogAndWait();
    }

    private void showDialogAndWait(){
        Scene scene = new Scene(layout);
        scene.getStylesheets().add("dark-theme.css");
        dialog.setScene(scene);
        dialog.showAndWait();
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
        layout.setStyle("-fx-border-color: -fx-second;");
        layout.setPadding(new Insets(5,5,5,5));
    }

    private void setTitleBar(String titleText, String imageFile){
        titleBar = new HBox(5);

        setTitle(titleText);
        setDialogIcon(imageFile);

        titleBar.getChildren().addAll(dialogIcon,title);
        titleBar.setAlignment(Pos.TOP_LEFT);
    }

    private void setHeaderText(String headerText){
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
        setContentText(contentText);

        inputField.getChildren().addAll(content, input);
        inputField.setAlignment(Pos.CENTER);
    }

    private void setContentText(String contentText){
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
}
