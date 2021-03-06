package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import static Classes.Constants.*;

public class Main extends Application {

    private static Stage primaryStage;
    private double xOffset = 0.0;
    private double yOffset = 0.0;

    static Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void start(Stage stage) throws Exception{
        primaryStage = stage;
        Parent root = FXMLLoader.load(getClass().getResource(DESIGN_FILE));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        root.setOnMousePressed(event -> { xOffset = event.getSceneX(); yOffset = event.getSceneY(); });
        root.setOnMouseDragged(event -> { primaryStage.setX(event.getScreenX() - xOffset); primaryStage.setY(event.getScreenY() - yOffset); });

        Scene myScene  = new Scene(root);
        myScene.getStylesheets().add(STYLE_SHEET_PATH);
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream(PROGRAM_ICON)));
        primaryStage.setTitle(PROGRAM_NAME);
        primaryStage.setScene(myScene);
        primaryStage.show();

        Screen activeScreen = Screen.getScreensForRectangle(primaryStage.getX(), primaryStage.getY(), primaryStage.getWidth(), primaryStage.getHeight()).get(0);
        Rectangle2D window = activeScreen.getBounds();
        primaryStage.setX(window.getMinX() + (window.getWidth() - primaryStage.getWidth())/2);
        primaryStage.setY(window.getMinY() + (window.getHeight() - primaryStage.getHeight())/2);
    }

    public static void main(String[] args) { launch(args); }
}
