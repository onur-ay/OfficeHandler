package sample;

import API.Database;
import Model.FileType;
import ReqFunc.Maths;
import com.sun.javafx.scene.control.skin.TableHeaderRow;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


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
    public TabPane tabs;
    public TableView<Model.File> pdfTable = new TableView<>();
    public TableView<Model.File> docTable = new TableView<>();
    public TableView<Model.File> pptTable = new TableView<>();
    public TableView<Model.File> xlsTable = new TableView<>();
    public TableView<Model.File> otherTable = new TableView<>();
    public TableView<Model.File> favTable = new TableView<>();
    public GridPane outerGridPane = new GridPane();
    public ProgressBar progressBar = new ProgressBar();
    private HashMap<String, TableView<Model.File>> tables = new HashMap<>();
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
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
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

    private void InitializeTables() throws SQLException, ParseException {
        tables.put("pdf", pdfTable);
        tables.put("doc", docTable);
        tables.put("ppt", pptTable);
        tables.put("xls", xlsTable);

        /*String[] fileNames = {"D:/Personal/School/Weekly Schedule 2017 Spring.pdf",
                "D:/Downloads/IDM/2018-2019-bahar-ders-programi-LİSANS-V1.pdf",
                "D:/Downloads/IDM/2019-2020-erasmus-ogrenim-hareketliligi-basvuru-metni0117100155.pdf",
                "D:/Downloads/IDM/15304683774_Ogrenci.pdf",
                "D:/Downloads/IDM/adli-sicil-kaydi.pdf",
                "D:/Downloads/IDM/BLM201819GuzButProgLisans.pdf",
                "D:/Downloads/IDM/nvi-yerlesim-yeri-ve-diger-adres-belgesi-sorgulama.pdf",
                "D:/Downloads/IDM/OBSS Burs Programı.pdf",
                "D:/Downloads/IDM/output_2.pdf",
                "D:/Downloads/IDM/output_3.pdf",
                "D:/Downloads/IDM/output_4.pdf",
                "D:/Downloads/IDM/output_5.pdf",
                "D:/Downloads/IDM/output_6.pdf",
                "D:/Downloads/IDM/output_7.pdf",
                "D:/Downloads/IDM/output_8.pdf",
                "D:/Downloads/IDM/output_9.pdf",
                "D:/Downloads/IDM/output_10.pdf",
                "D:/Downloads/IDM/output_11.pdf",
                "D:/Downloads/IDM/output_12.pdf",
                "D:/Downloads/IDM/yok-ogrenci-belgesi-sorgulama.pdf",
                "D:/Personal/School/Lectures/8th Semester (2018-2)/Computer Project/Reports/1st Report/Attachments/Project_Work_Plan_Gantt_Diagram.xlsx",
                "D:/Personal/School/Lectures/8th Semester (2018-2)/Final Project/TUBITAK/Report/Term Project Report.docx",
                "D:/Personal/School/Lectures/8th Semester (2018-2)/Final Project/TUBITAK/Report/TUBITAK Term Project Report.pdf",
                "D:/Personal/School/Lectures/8th Semester (2018-2)/Final Project/TUBITAK/TUBITAK Project Paperworks/Project Work Plan.xlsx",
                "D:/Personal/School/Lectures/8th Semester (2018-2)/Introduction to Mobile Programming/Assignments/Assignment-1/Report/Assignment-1.doc",
                "D:/Personal/School/Lectures/8th Semester (2018-2)/Introduction to Mobile Programming/Assignments/Assignment-2/Report/Report.docx",
                "D:/Personal/School/Lectures/8th Semester (2018-2)/Network Technologies/Assignments/Assignment-3/Ağtek ODEV.docx",
                "D:/Personal/School/Lectures/8th Semester (2018-2)/Network Technologies/Assignments/Assignment-3/AgTekHW3.doc",
                "D:/Personal/School/Lectures/8th Semester (2018-2)/Network Technologies/Exams/Final_Exam_3.docx",
                "D:/Personal/School/Lectures/8th Semester (2018-2)/Network Technologies/Exams/Questions.docx",
                "D:/Personal/School/Lectures/7th Semester (2018-1)/Computer Systems Security/Assignments/Assignment-1/Report/Assignment-1.docx",
                "D:/Personal/School/Lectures/7th Semester (2018-1)/General Internship/Application/INTERN_1_Apply_Doc_1(SGK_3 copy).doc",
                "D:/Personal/School/Lectures/7th Semester (2018-1)/General Internship/Application/INTERN_1_Apply_Doc_2(CONTRACT_1 copy).doc",
                "D:/Personal/School/Lectures/7th Semester (2018-1)/General Internship/Application/INTERN_1_Apply_Doc_3(SALARY_1 copy).docx",
                "D:/Personal/School/Lectures/7th Semester (2018-1)/General Internship/Application/INTERN_1_Apply_Doc_4(CALENDAR_1 copy).docx",
                "D:/Personal/School/Lectures/7th Semester (2018-1)/General Internship/Application/INTERN_1_Apply_Doc_5(REQUEST_1 copy).docx",
                "D:/Personal/School/Lectures/7th Semester (2018-1)/General Internship/Application/INTERN_1_Apply_Doc_6(INTERN_EVALUATION_1 copy).doc",
                "D:/Personal/School/Lectures/7th Semester (2018-1)/General Internship/Application/INTERN_1_Apply_Doc_7(INTERN_BOOK_EVALUATION_1 copy).doc",
                "D:/Personal/School/Lectures/All Lectures/Veri Yapıları ve Algoritmalar/2012 Lab/Veri Yapıları 26Mart v2.xls",
                "D:/Personal/School/Lectures/All Lectures/Veri Yapıları ve Algoritmalar/2012 Lab/Veri Yapıları 26Mart.xls",
                "D:/Personal/School/Lectures/All Lectures/Veri Yapıları ve Algoritmalar/2013 Lab/Veri Yapıları 131106 Lab programı.xlsx",
                "D:/Personal/School/Lectures/All Lectures/Veri Yapıları ve Algoritmalar/data yapıları lab.xls",
                "D:/Personal/School/Lectures/All Lectures/Veri Yapıları ve Algoritmalar/ödev/data yapıları lab.xls",
                "D:/Personal/School/Lectures/All Lectures/Veri Yapıları ve Algoritmalar/ödev/DataLabWebDuyuru.xls",
                "D:/Personal/School/Lectures/All Lectures/Veri Yapıları ve Algoritmalar/veriyapılarınot.xlsx",
                "D:/Personal/School/Lectures/All Lectures/Veritabanı Yönetimi/LABGroups.xls",
                "D:/Personal/School/Lectures/All Lectures/Veritabanı Yönetimi/SL-1 (2).xls",
                "D:/Personal/School/Lectures/All Lectures/Veritabanı Yönetimi/SL-1.xls",
                "D:/Personal/School/Lectures/All Lectures/Veritabanı Yönetimi/SL-1as2.xls"};
        try {
            createScannedFiles(fileNames);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }*/

        for(TableView<Model.File> table : tables.values()){
            table.widthProperty().addListener((source, oldWidth, newWidth) -> {
                TableHeaderRow header = (TableHeaderRow) table.lookup("TableHeaderRow");
                header.reorderingProperty().addListener((observable, oldValue, newValue) -> header.setReordering(false));
            });

            String[][] conditions = {{"string", "FileType", table.getId().substring(0,3), "1"}};
            ResultSet rs = Database.Select(new Model.File(), conditions);
            addFromDBToTable(rs, table);
            if(!table.getId().equals("pdf")){
                conditions = new String[][]{{"string", "FileType", table.getId().substring(0,3) + "x", "1"}};
                rs = Database.Select(new Model.File(), conditions);
                addFromDBToTable(rs, table);
            }
        }

        tables.put("oth", otherTable);
        String[][] conditions = {{"string", "FileType", "pdf", "0"},{"string", "FileType", "doc", "0"},{"string", "FileType", "docx", "0"},{"string", "FileType", "xls", "0"},{"string", "FileType", "xlsx", "0"},{"string", "FileType", "ppt", "0"},{"string", "FileType", "pptx", "0"}};
        ResultSet rs = Database.Select(new Model.File(), conditions);
        addFromDBToTable(rs, otherTable);

        tables.put("fav", favTable);
        conditions = new String[][]{{"boolean", "Fav", "true", "1"}};
        rs = Database.Select(new Model.File(), conditions);
        addFromDBToTable(rs, favTable);

        for(TableView<Model.File> table : tables.values()){
            table.setEditable(true);
            TableColumn fileNameCol = table.getColumns().get(0);
            fileNameCol.setCellValueFactory(new PropertyValueFactory<Model.File, String>("Name"));
            TableColumn creationDateCol = table.getColumns().get(1);
            creationDateCol.setCellValueFactory(new PropertyValueFactory<Model.File, String>("CreationDate"));
            TableColumn lastModifiedDateCol = table.getColumns().get(2);
            lastModifiedDateCol.setCellValueFactory(new PropertyValueFactory<Model.File, String>("LastModifiedDate"));
            TableColumn typeCol = table.getColumns().get(3);
            typeCol.setCellValueFactory(new PropertyValueFactory<Model.File, String>("FileType"));
            TableColumn sizeCol = table.getColumns().get(4);
            sizeCol.setCellValueFactory(new PropertyValueFactory<Model.File, String>("Size"));
            TableColumn authorCol = table.getColumns().get(5);
            authorCol.setCellValueFactory(new PropertyValueFactory<Model.File, String>("Author"));
        }
    }

    private void addFromDBToTable(ResultSet rs, TableView<Model.File> table) throws SQLException, ParseException {
        Model.File dbFile;
        while(rs.next()){
            dbFile = new Model.File();
            dbFile.setID(rs.getInt("ID"));
            dbFile.setName(rs.getString("Name").replaceAll("[{\"}]",""));
            dbFile.setFileType(new FileType(rs.getString("FileType").replaceAll("[{\"}]","")));
            dbFile.setAuthor(rs.getString("Author").replaceAll("[{\"}]",""));
            dbFile.setDirectory(Paths.get(rs.getString("Directory").replaceAll("[{\"}]","")));
            dbFile.setLastModifiedDate(new SimpleDateFormat("dd/MM/yyyy").parse(rs.getString("LastModifiedDate").replaceAll("[{\"}]","")));
            dbFile.setCreationDate(new SimpleDateFormat("dd/MM/yyyy").parse(rs.getString("CreationDate").replaceAll("[{\"}]","")));
            dbFile.setFav(Boolean.valueOf(rs.getString("Fav")));
            dbFile.setSize(Maths.round(rs.getDouble("Size"), 4));
            table.getItems().add(dbFile);
        }
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

    @FXML
    private void addToFavorites(MouseEvent event) {
        String selectedTab = tabs.getSelectionModel().getSelectedItem().getId().substring(0,3);
        TableView<Model.File> focusedTable = tables.get(selectedTab);
        Model.File selectedFile = focusedTable.getSelectionModel().getSelectedItem();
        favTable.getItems().add(selectedFile);

        focusedTable.getSelectionModel().select(null);

        String[][] conditions = {{"string", "Name", selectedFile.getName(), "1"}, {"string", "Directory", selectedFile.getDirectory(), "1"}};
        String[][] updatedFields = {{"boolean","Fav","true"}};
        Database.Update(selectedFile, updatedFields, conditions);
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

    private void createScannedFiles(String[] fileNames) throws IOException, ParseException {
        File scannedFile;
        Model.File scannedFileModel = new Model.File();
        Path directory;
        String author;
        Date creationDate;
        Date lastModifiedDate;
        FileType fileType;
        double size;
        DateFormat creationDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        BasicFileAttributes attributes;
        for(String fileName : fileNames){
            scannedFile = new File(fileName);
            directory = scannedFile.toPath();
            attributes = Files.readAttributes(directory, BasicFileAttributes.class);
            creationDate = creationDateFormat.parse(creationDateFormat.format(attributes.creationTime().toMillis()));
            size = Maths.round((double)scannedFile.length()/(1024*1024),2);
            fileType = new FileType(fileName.substring(fileName.lastIndexOf('.')+1));
            author = Files.getOwner(directory).toString().replace('\\','/');
            lastModifiedDate = creationDateFormat.parse(creationDateFormat.format(Files.getLastModifiedTime(directory).toMillis()));
            scannedFileModel = new Model.File(fileName.substring(fileName.lastIndexOf('/')+1), fileType, author, directory, creationDate, lastModifiedDate, size, false);
            Database.Insert(scannedFileModel);
        }

        ObservableList<Model.File> files = FXCollections.observableArrayList();
        files.add(scannedFileModel);

        pdfTable.setItems(files);
    }
}