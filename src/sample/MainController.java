package sample;

import API.Database;
import Model.FileType;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;


/**
 *  JENERİK OLARAK OBJE TİPİ TANIYIP VERİTABANINA INSERT EDİLEN FONKSİYONDA İYİLEŞTİRME YAPILACAK.
 *  (METOT İSİMLERİNİ TARAMAK YERİNE DOĞRUDAN PROPERTY'NİN VALUE'SUNA ERİŞMEYE ÇALIŞILACAK)
 *
 *  FİLETYPE'LAR SETUP ESNASINDA DB'YE BASILACAK VERİTABANINDAN SİLMEYİ UNUTMA, FİLETYPE'LARI DB'YE BASMAYI UNUTMA.
 *
 *
 */

public class MainController implements Initializable {

    @FXML
    public ComboBox<String> driveComboBox;
    public ComboBox<String> fileTypeComboBox;
    public TableView<Model.File> pdfTable = new TableView<>();
    public TableView<Model.File> docTable = new TableView<>();
    public TableView<Model.File> pptTable = new TableView<>();
    public TableView<Model.File> xlsTable = new TableView<>();
    public TableView<Model.File> otherTable = new TableView<>();
    public TableView<Model.File> favTable = new TableView<>();
    private List<TableView<Model.File>> tables = new ArrayList<>();
    public GridPane outerGridPane = new GridPane();
    public ProgressBar progressBar = new ProgressBar();
    private ProgressIndicator progressIndicator = new ProgressIndicator();

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
        try {
            ScanAndSetDrives();
            SetFileTypes();
            InitializeTables();
            FillProgressBar();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    private void SetFileTypes() throws SQLException {
        ResultSet fileTypes = Database.Select(new FileType(), null);
        String fileType;
        while(fileTypes.next()){
            fileType = fileTypes.getString("Name").replaceAll("[{}]","");
            fileTypeComboBox.getItems().add(fileType);
        }
        fileTypeComboBox.getItems().add("Add new file type...");
        fileTypeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> { if(newValue.equals(fileTypeComboBox.getItems().get(fileTypeComboBox.getItems().size()-1))) addNewFileType(); });
    }

    private void InitializeTables(){
        tables.add(pdfTable);
        tables.add(docTable);
        tables.add(pptTable);
        tables.add(xlsTable);
        tables.add(otherTable);
        tables.add(favTable);

        for(TableView<Model.File> table : tables){
            table.setEditable(true);
            TableColumn fileNameCol = table.getColumns().get(0);
            fileNameCol.setCellValueFactory(new PropertyValueFactory<Model.File, String>("Name"));
            TableColumn creationDateCol = table.getColumns().get(1);
            creationDateCol.setCellValueFactory(new PropertyValueFactory<Model.File, String>("CreationDate"));
            TableColumn typeCol = table.getColumns().get(2);
            typeCol.setCellValueFactory(new PropertyValueFactory<Model.File, String>("FileType"));
            TableColumn sizeCol = table.getColumns().get(3);
            sizeCol.setCellValueFactory(new PropertyValueFactory<Model.File, String>("Size"));
            TableColumn tagsCol = table.getColumns().get(4);
            tagsCol.setCellValueFactory(new PropertyValueFactory<Model.File, String>("Tags"));
            TableColumn authorCol = table.getColumns().get(5);
            authorCol.setCellValueFactory(new PropertyValueFactory<Model.File, String>("Author"));
        }

        ObservableList<Model.File> files = FXCollections.observableArrayList();
        Model.File tempFile = new Model.File(1,"schedule.pdf", new FileType(1,"pdf"));
        tempFile.setAuthor("OnurAY");
        tempFile.setCreationDate(new Date());
        tempFile.setSize(1654.24);
        tempFile.setDirectory("C:/Users/OnurAy/Documents/Personal/School/schedule.pdf");
        tempFile.setFav(true);
        files.add(tempFile);

        pdfTable.setItems(files);
        for(Model.File file : files)
            Database.Insert(file);
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
        CustomizedDialog alertDialog = new CustomizedDialog();
        task.setOnSucceeded(e -> alertDialog.createAlertDialog("Office Handler", "Scan completed successfully !", "alertbox.png"));

        Thread thread = new Thread(task);
        thread.start();
    }

    @FXML
    private void displayHelpDialog(MouseEvent event){
        CustomizedDialog alertDialog = new CustomizedDialog();
        alertDialog.createAlertDialog("Office Handler", "This program was created in the scope of term project of Yildiz Technical University Computer Engineering Department. All rights are reserved by YTU CE. Mustafa ULUKAYA | Onur AY | 2019®", "desktop-icon.png");
    }

    private void addNewFileType() {
        CustomizedDialog addNewFileTypeDialog = new CustomizedDialog();
        addNewFileTypeDialog.createTextInputDialog("New File Type", "Please write the file type with comma before it. (Ex.: .exe, .txt etc.)", "File Type:", "desktop-icon.png");
        FileType newFileType = new FileType(addNewFileTypeDialog.getResult());
        fileTypeComboBox.getItems().add(fileTypeComboBox.getItems().size()-1, newFileType.getName());
        fileTypeComboBox.getSelectionModel().select(fileTypeComboBox.getItems().size()-2);
        Database.Insert(newFileType);
    }

    private void activateProgressBar(final Task<?> task)  {
        progressBar.progressProperty().bind(task.progressProperty());
        progressIndicator.progressProperty().bind(task.progressProperty());
    }
}