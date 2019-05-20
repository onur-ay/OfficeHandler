package Classes;

import Main.Controller;
import Main.Main;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import static Classes.Constants.*;

public class ColorPickerDialog {

    private String backgroundColorHex;
    private String borderColorHex;
    private String textColorHex;
    private ColorPicker backgroundPicker;
    private ColorPicker borderPicker;
    private ColorPicker textPicker;
    private GridPane layout;
    private Stage dialog;

    public String getBackgroundColorHex() { return backgroundColorHex; }

    private void setBackgroundColorHex(String backgroundColorHex) { this.backgroundColorHex = backgroundColorHex; }

    public String getBorderColorHex() { return borderColorHex; }

    private void setBorderColorHex(String borderColorHex) { this.borderColorHex = borderColorHex; }

    public String getTextColorHex() { return textColorHex; }

    private void setTextColorHex(String textColorHex) { this.textColorHex = textColorHex; }

    public ColorPickerDialog(MouseEvent event, String currentBackground, String currentBorder, String currentText) {
        initializeDialog(event);
        initializeLayout(currentBackground,currentBorder,currentText);
        showDialogAndWait();
    }

    private void showDialogAndWait(){
        Scene scene = new Scene(layout);
        scene.getStylesheets().add(STYLE_SHEET_PATH);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private void initializeDialog(MouseEvent event){
        double posX = event.getScreenX();
        double posY = event.getScreenY();

        dialog = new Stage();
        dialog.setX(posX-270);
        dialog.setY(posY+5);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setMinWidth(250);
        dialog.setMaxWidth(450);
        dialog.setMinHeight(50);
        dialog.setMaxHeight(200);
        dialog.setResizable(false);
    }

    private void initializeLayout(String currentBackground, String currentBorder, String currentText){
        layout = new GridPane();
        layout.addColumn(0);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-border-color:"+ Controller.CURRENT_SECOND_COLOR + ";" + "-fx-background-color:" + Controller.CURRENT_FIRST_COLOR + ";");
        layout.setPadding(new Insets(5,5,5,5));
        for(int i=0; i<5; i++)
            layout.getRowConstraints().add(new RowConstraints(30));
        for(int i=0; i<3; i++)
            layout.getColumnConstraints().add(new ColumnConstraints());
        layout.getColumnConstraints().get(0).setMinWidth(105);
        layout.getColumnConstraints().get(0).setMaxWidth(105);
        layout.getColumnConstraints().get(0).setPrefWidth(105);
        layout.getColumnConstraints().get(1).setMinWidth(10);
        layout.getColumnConstraints().get(1).setMaxWidth(10);
        layout.getColumnConstraints().get(1).setPrefWidth(10);
        layout.getRowConstraints().get(0).setMinHeight(35);
        layout.getRowConstraints().get(0).setMaxHeight(35);
        layout.getRowConstraints().get(0).setPrefHeight(35);
        layout.getRowConstraints().get(4).setMinHeight(40);
        layout.getRowConstraints().get(4).setMaxHeight(40);
        layout.getRowConstraints().get(4).setPrefHeight(40);
        setLabels();
        setColorPickers(currentBackground,currentBorder,currentText);
        setButtons();
    }

    private void setLabels(){
        String textStyle = "-fx-text-fill:"+ Controller.CURRENT_FIRST_TEXT_COLOR +";";
        Label dod1 = new Label(":");
        Label dod2 = new Label(":");
        Label dod3 = new Label(":");
        Label backgroundColor = new Label(BACKGROUND_COLOR_TEXT);
        Label borderColor = new Label(BORDER_COLOR_TEXT);
        Label textColor = new Label(TEXT_COLOR_TEXT);
        dod1.setStyle(textStyle);
        dod2.setStyle(textStyle);
        dod3.setStyle(textStyle);
        backgroundColor.setStyle(textStyle);
        borderColor.setStyle(textStyle);
        textColor.setStyle(textStyle);
        layout.add(backgroundColor,0,1);
        layout.add(borderColor,0,2);
        layout.add(textColor,0,3);
        layout.add(dod1,1,1);
        layout.add(dod2,1,2);
        layout.add(dod3,1,3);
    }

    private void setColorPickers(String currentBackground, String currentBorder, String currentText){
        String colorPickerStyle = "-fx-border-color:" + Controller.CURRENT_SECOND_COLOR + "; -fx-border-radius: 20px; -fx-background-radius: 20px; -fx-background-color:" + Controller.CURRENT_FIRST_COLOR + "; -fx-text-fill: " + Controller.CURRENT_FIRST_TEXT_COLOR + ";";
        String colorPickerStyleHover = "-fx-cursor: hand; -fx-border-radius: 20px; -fx-background-radius: 20px; -fx-background-color:" + Controller.CURRENT_SECOND_COLOR + "; -fx-text-fill: " + Controller.CURRENT_FIRST_COLOR + ";";
        backgroundPicker = new ColorPicker(Color.web(currentBackground));
        borderPicker = new ColorPicker(Color.web(currentBorder));
        textPicker = new ColorPicker(Color.web(currentText));
        CustomizedDialog.setStyles(backgroundPicker, colorPickerStyle, colorPickerStyleHover);
        CustomizedDialog.setStyles(borderPicker, colorPickerStyle, colorPickerStyleHover);
        CustomizedDialog.setStyles(textPicker, colorPickerStyle, colorPickerStyleHover);
        layout.add(backgroundPicker,2,1);
        layout.add(borderPicker,2,2);
        layout.add(textPicker,2,3);
    }

    private void setButtons(){
        String buttonStyle = "-fx-border-color:" + Controller.CURRENT_SECOND_COLOR + "; -fx-border-radius: 20px; -fx-background-radius: 20px; -fx-background-color:" + Controller.CURRENT_FIRST_COLOR + "; -fx-text-fill: " + Controller.CURRENT_FIRST_TEXT_COLOR + ";";
        String buttonStyleHover = "-fx-cursor: hand; -fx-border-radius: 20px; -fx-background-radius: 20px; -fx-background-color:" + Controller.CURRENT_SECOND_COLOR + "; -fx-text-fill: " + Controller.CURRENT_FIRST_COLOR + ";";
        Button confirmButton = new Button(OK_BUTTON_TEXT);
        confirmButton.setId("confirmColorsButton");
        confirmButton.setOnMouseClicked((MouseEvent event) -> { setBackgroundColorHex(colorToHex(backgroundPicker.getValue())); setBorderColorHex(colorToHex(borderPicker.getValue())); setTextColorHex(colorToHex(textPicker.getValue())); dialog.close();});
        Button resetButton = new Button(RESET_BUTTON_TEXT);
        resetButton.setId("resetColorsButton");
        resetButton.setOnMouseClicked(event -> { setBackgroundColorHex(DEFAULT_FIRST_COLOR); setBorderColorHex(DEFAULT_SECOND_COLOR); setTextColorHex(DEFAULT_FIRST_TEXT_COLOR); dialog.close(); });
        Button closeButton = new Button();
        closeButton.setId("cancelColorsButton");
        closeButton.setMinWidth(25);
        closeButton.setMaxWidth(25);
        closeButton.setPrefWidth(25);
        closeButton.setOnMouseClicked((MouseEvent event) -> { dialog.close(); setBackgroundColorHex(null); setBorderColorHex(null); setTextColorHex(null); });
        CustomizedDialog.setStyles(confirmButton, buttonStyle, buttonStyleHover);
        CustomizedDialog.setStyles(resetButton, buttonStyle, buttonStyleHover);
        CustomizedDialog.setStyles(closeButton, buttonStyle.replace("-fx-border-color:" + Controller.CURRENT_SECOND_COLOR + ";", ""), buttonStyleHover.replace(Controller.CURRENT_SECOND_COLOR, "#EE5253"));
        layout.add(confirmButton,1,4);
        layout.add(resetButton,2,4);
        layout.add(closeButton, 2,0);
        GridPane.setHalignment(closeButton, HPos.RIGHT);
        GridPane.setValignment(closeButton, VPos.TOP);
        GridPane.setHalignment(confirmButton, HPos.RIGHT);
        GridPane.setValignment(confirmButton, VPos.CENTER);
        GridPane.setColumnSpan(confirmButton,2);
        GridPane.setMargin(confirmButton, new Insets(0,50,0,0));
        GridPane.setHalignment(resetButton, HPos.RIGHT);
        GridPane.setValignment(resetButton, VPos.CENTER);
    }

    private String colorToHex(Color color) {
        String hex1;
        String hex2;

        hex1 = Integer.toHexString(color.hashCode()).toUpperCase();

        switch (hex1.length()) {
            case 2:
                hex2 = "000000";
                break;
            case 3:
                hex2 = String.format("00000%s", hex1.substring(0,1));
                break;
            case 4:
                hex2 = String.format("0000%s", hex1.substring(0,2));
                break;
            case 5:
                hex2 = String.format("000%s", hex1.substring(0,3));
                break;
            case 6:
                hex2 = String.format("00%s", hex1.substring(0,4));
                break;
            case 7:
                hex2 = String.format("0%s", hex1.substring(0,5));
                break;
            default:
                hex2 = hex1.substring(0, 6);
        }
        return "#" + hex2;
    }
}
