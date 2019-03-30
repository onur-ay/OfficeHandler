package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    public ComboBox<String> driveComboBox;
    public ComboBox<String> fileTypeComboBox;
    public TableView<TableFile> pdfTable = new TableView<>();
    public TableView<TableFile> docTable = new TableView<>();
    public TableView<TableFile> pptTable = new TableView<>();
    public TableView<TableFile> xlsTable = new TableView<>();
    public TableView<TableFile> otherTable = new TableView<>();
    public TableView<TableFile> favTable = new TableView<>();
    public GridPane outerGridPane = new GridPane();
    public ProgressBar progressBar = new ProgressBar();
    public ProgressIndicator progressIndicator = new ProgressIndicator();

    @FXML
    private void closeProgram(MouseEvent event){
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void minimizeProgram(MouseEvent event){
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        ScanAndSetDrives();
        SetFileTypes();
        FillTables();
        FillProgressBar();
    }

    private void ScanAndSetDrives(){
        File[] paths;
        FileSystemView fsv = FileSystemView.getFileSystemView();

        paths = File.listRoots();

        for(File path:paths)
        {
            if(fsv.getSystemTypeDescription(path).contains("Local"))
                driveComboBox.getItems().add(path.toString());
        }
        driveComboBox.getItems().add("All");
    }

    private void SetFileTypes(){
        fileTypeComboBox.getItems().addAll(".pdf", ".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx", "All", "Add new file type...");
    }

    private void FillTables(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date today = new Date();
        pdfTable.setEditable(true);

        ObservableList<TableFile> data = FXCollections.observableArrayList(
                new TableFile("schedule.pdf", sdf.format(today), ".pdf", "1654.24","school,lecture,program", "OnurAY", "C:/Users/OnurAy/Documents/Personal/School/schedule.pdf"),
                new TableFile("weekend.pdf", sdf.format(today), ".pdf", "1654.24","weekend,holiday,personal", "OnurAY", "C:/Users/OnurAy/Documents/Personal/weekend.pdf"),
                new TableFile("holiday.pdf", sdf.format(today), ".pdf", "1654.24","weekend,holiday,personal", "OnurAY", "C:/Users/OnurAy/Documents/Personal/holiday.pdf"),
                new TableFile("work.pdf", sdf.format(today), ".pdf", "1654.24","work,business", "OnurAY", "C:/Users/OnurAy/Documents/Personal/Work/work.pdf"),
                new TableFile("school.pdf", sdf.format(today), ".pdf", "1654.24","school,lecture,program", "OnurAY", "C:/Users/OnurAy/Documents/Personal/School/school.pdf"),
                new TableFile("schedule.pdf", sdf.format(today), ".pdf", "1654.24","school,lecture,program", "OnurAY", "C:/Users/OnurAy/Documents/Personal/School/schedule.pdf"),
                new TableFile("weekend.pdf", sdf.format(today), ".pdf", "1654.24","weekend,holiday,personal", "OnurAY", "C:/Users/OnurAy/Documents/Personal/weekend.pdf"),
                new TableFile("holiday.pdf", sdf.format(today), ".pdf", "1654.24","weekend,holiday,personal", "OnurAY", "C:/Users/OnurAy/Documents/Personal/holiday.pdf"),
                new TableFile("work.pdf", sdf.format(today), ".pdf", "1654.24","work,business", "OnurAY", "C:/Users/OnurAy/Documents/Personal/Work/work.pdf"),
                new TableFile("school.pdf", sdf.format(today), ".pdf", "1654.24","school,lecture,program", "OnurAY", "C:/Users/OnurAy/Documents/Personal/School/school.pdf"),
                new TableFile("schedule.pdf", sdf.format(today), ".pdf", "1654.24","school,lecture,program", "OnurAY", "C:/Users/OnurAy/Documents/Personal/School/schedule.pdf"),
                new TableFile("weekend.pdf", sdf.format(today), ".pdf", "1654.24","weekend,holiday,personal", "OnurAY", "C:/Users/OnurAy/Documents/Personal/weekend.pdf"),
                new TableFile("holiday.pdf", sdf.format(today), ".pdf", "1654.24","weekend,holiday,personal", "OnurAY", "C:/Users/OnurAy/Documents/Personal/holiday.pdf"),
                new TableFile("work.pdf", sdf.format(today), ".pdf", "1654.24","work,business", "OnurAY", "C:/Users/OnurAy/Documents/Personal/Work/work.pdf"),
                new TableFile("school.pdf", sdf.format(today), ".pdf", "1654.24","school,lecture,program", "OnurAY", "C:/Users/OnurAy/Documents/Personal/School/school.pdf"),
                new TableFile("schedule.pdf", sdf.format(today), ".pdf", "1654.24","school,lecture,program", "OnurAY", "C:/Users/OnurAy/Documents/Personal/School/schedule.pdf"),
                new TableFile("weekend.pdf", sdf.format(today), ".pdf", "1654.24","weekend,holiday,personal", "OnurAY", "C:/Users/OnurAy/Documents/Personal/weekend.pdf"),
                new TableFile("holiday.pdf", sdf.format(today), ".pdf", "1654.24","weekend,holiday,personal", "OnurAY", "C:/Users/OnurAy/Documents/Personal/holiday.pdf"),
                new TableFile("work.pdf", sdf.format(today), ".pdf", "1654.24","work,business", "OnurAY", "C:/Users/OnurAy/Documents/Personal/Work/work.pdf"),
                new TableFile("school.pdf", sdf.format(today), ".pdf", "1654.24","school,lecture,program", "OnurAY", "C:/Users/OnurAy/Documents/Personal/School/school.pdf"),
                new TableFile("schedule.pdf", sdf.format(today), ".pdf", "1654.24","school,lecture,program", "OnurAY", "C:/Users/OnurAy/Documents/Personal/School/schedule.pdf"),
                new TableFile("weekend.pdf", sdf.format(today), ".pdf", "1654.24","weekend,holiday,personal", "OnurAY", "C:/Users/OnurAy/Documents/Personal/weekend.pdf"),
                new TableFile("holiday.pdf", sdf.format(today), ".pdf", "1654.24","weekend,holiday,personal", "OnurAY", "C:/Users/OnurAy/Documents/Personal/holiday.pdf"),
                new TableFile("work.pdf", sdf.format(today), ".pdf", "1654.24","work,business", "OnurAY", "C:/Users/OnurAy/Documents/Personal/Work/work.pdf"),
                new TableFile("school.pdf", sdf.format(today), ".pdf", "1654.24","school,lecture,program", "OnurAY", "C:/Users/OnurAy/Documents/Personal/School/school.pdf")
        );

        TableColumn fileNameCol = pdfTable.getColumns().get(0);
        fileNameCol.setCellValueFactory(new PropertyValueFactory<TableFile, String>("fileName"));

        TableColumn creationDateCol = pdfTable.getColumns().get(1);
        creationDateCol.setCellValueFactory(new PropertyValueFactory<TableFile, String>("creationDate"));

        TableColumn typeCol = pdfTable.getColumns().get(2);
        typeCol.setCellValueFactory(new PropertyValueFactory<TableFile, String>("type"));

        TableColumn sizeCol = pdfTable.getColumns().get(3);
        sizeCol.setCellValueFactory(new PropertyValueFactory<TableFile, String>("size"));

        TableColumn tagsCol = pdfTable.getColumns().get(4);
        tagsCol.setCellValueFactory(new PropertyValueFactory<TableFile, String>("tags"));

        TableColumn authorCol = pdfTable.getColumns().get(5);
        authorCol.setCellValueFactory(new PropertyValueFactory<TableFile, String>("author"));

        pdfTable.setItems(data);
    }

    private void FillProgressBar(){
        Task<Void> task = new Task<Void>() {
            @Override
            public Void call() throws InterruptedException {
                for (int i = 0; i < 50; i++) {
                    updateProgress(i, 50);
                    Thread.sleep(200);
                }
                updateProgress(50, 50);
                return null ;
            }
        };

        // binds progress of progress bars to progress of task:
        activateProgressBar(task);

        // in real life this method would get the result of the task
        // and update the UI based on its value:
        task.setOnSucceeded(e -> CustomizedDialog.display("Office Handler", "Scan completed successfully !", "alertbox.png"));

        Thread thread = new Thread(task);
        thread.start();
    }

    @FXML
    private void displayHelpDialog(MouseEvent event){
        CustomizedDialog.display("Office Handler", "This program was created in the scope of term project of Yildiz Technical University Computer Engineering Department. All rights are reserved by YTU CE. Mustafa ULUKAYA | Onur AY | 2019®", "desktop-icon.png");
    }

    public void activateProgressBar(final Task<?> task)  {
        progressBar.progressProperty().bind(task.progressProperty());
        progressIndicator.progressProperty().bind(task.progressProperty());
    }
}