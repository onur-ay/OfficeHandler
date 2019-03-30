package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CustomizedDialog {

    public static void display(String title, String message, String imageFile){
        Stage alertDialog = new Stage();

        alertDialog.initModality(Modality.APPLICATION_MODAL);
        alertDialog.initStyle(StageStyle.UNDECORATED);
        alertDialog.setMinWidth(250);
        alertDialog.setMaxWidth(450);
        alertDialog.setMinHeight(50);
        alertDialog.setMaxHeight(200);
        alertDialog.setResizable(false);

        Label dialogMessage = new Label();
        dialogMessage.setText(message);
        dialogMessage.setMinWidth(225);
        dialogMessage.setMaxWidth(425);
        dialogMessage.setMinHeight(25);
        dialogMessage.setMaxHeight(175);
        dialogMessage.setWrapText(true);
        dialogMessage.setAlignment(Pos.CENTER);
        dialogMessage.setTextAlignment(TextAlignment.CENTER);
        Button closeButton = new Button("OK");
        closeButton.setOnAction(e -> alertDialog.close());

        Label dialogTitle = new Label();
        dialogTitle.setText(title);
        ImageView dialogIcon = new ImageView(new Image(CustomizedDialog.class.getResourceAsStream(imageFile)));
        dialogIcon.setFitWidth(20.0);
        dialogIcon.setFitHeight(20.0);

        HBox titleBar = new HBox(5);
        titleBar.getChildren().addAll(dialogIcon,dialogTitle);
        titleBar.setAlignment(Pos.TOP_LEFT);

        VBox layout = new VBox(10);
        layout.getChildren().addAll(titleBar,dialogMessage,closeButton);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-border-color: -fx-second;");
        layout.setPadding(new Insets(5,5,5,5));

        Scene scene = new Scene(layout);
        scene.getStylesheets().add("dark-theme.css");
        alertDialog.setScene(scene);
        alertDialog.showAndWait();
    }

}
