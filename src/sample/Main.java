package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.sql.*;

import API.GeneralOperations;

public class Main extends Application {

    private double xOffset = 0.0;
    private double yOffset = 0.0;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });

        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setX(event.getScreenX() - xOffset);
                primaryStage.setY(event.getScreenY() - yOffset);
            }
        });

        Scene myScene  = new Scene(root);
        myScene.getStylesheets().add("dark-theme.css");
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("desktop-icon.png")));
        primaryStage.setTitle("Office Handler");
        primaryStage.setScene(myScene);
        primaryStage.show();

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/OfficeHandlerDB", "postgres", "Sher76796775ed.")) {


            System.out.println("Java JDBC PostgreSQL Example");

            System.out.println("Connected to PostgreSQL database!");

            Statement statement = connection.createStatement();

            System.out.println("Reading car records...");

            System.out.printf("%-30.30s  %-30.30s%n", "ID", "Name");

            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + GeneralOperations.GetTableName("OFFICE","FileType"));
            while (resultSet.next()) {
                System.out.printf("%-30.30s  %-30.30s%n", resultSet.getString("ID"), resultSet.getString("NAME"));
            }

        } /*catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC driver not found.");
            e.printStackTrace();
        }*/ catch (SQLException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }

    }

    public static void main(String[] args) { launch(args); }

}
