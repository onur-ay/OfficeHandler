package Main;

import Classes.*;
import Classes.Date;
import Model.FileType;
import com.sun.javafx.css.Size;
import com.sun.javafx.scene.control.skin.TableHeaderRow;
import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.controlsfx.control.spreadsheet.Grid;

import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *  DEFAULT OLAN FİLETYPE'LAR SETUP ESNASINDA DB'YE BASILACAK VERİTABANINDAN SİLMEYİ UNUTMA, FİLETYPE'LARI DB'YE BASMAYI UNUTMA !
 *  BFS İLE DE TARAMA YAPIP DFS İLE BFS İ KARŞILAŞTIRMAYI UNUTMA !
 *
 */

public class Controller implements Initializable {

    private static final int DOUBLE_CLICK = 2;
    private static boolean MAXIMIZED = false;

    private static double PREVIOUS_HEIGHT;
    private static double PREVIOUS_WIDTH;

    public static String FIRST_COLOR = "#2D2D2D";
    public static String SECOND_COLOR = "#FFFF8D";
    public static String FIRST_TEXT_COLOR = "#B2B2B2";

    @FXML
    public ComboBox<String> driveComboBox;
    public ComboBox<String> fileTypeComboBox;
    public TabPane tabs;
    public Button maximizeButton;
    public Button scanDrivesButton;
    public Button scanTablesButton;
    public CheckBox matchCaseCheck;
    public TextField keywordText;
    public Label totalFilesLabel;
    public Label filesDoneLabel;
    public Label totalFilesNumber;
    public Label filesDoneNumber;
    public TableView<Model.File> pdfTable = new TableView<>();
    public TableView<Model.File> docTable = new TableView<>();
    public TableView<Model.File> pptTable = new TableView<>();
    public TableView<Model.File> xlsTable = new TableView<>();
    public TableView<Model.File> otherTable = new TableView<>();
    public TableView<Model.File> favTable = new TableView<>();
    public GridPane outerGridPane = new GridPane();
    public GridPane innerGridPane = new GridPane();
    public ProgressIndicator scanDrivesLoading = new ProgressIndicator();
    public ProgressIndicator scanTablesLoading = new ProgressIndicator();
    public ProgressBar progressBar = new ProgressBar();
    public ProgressBarManager pbm = new ProgressBarManager(0.0, 0.0, 0.0, 0.0);
    private HashMap<String, FileType> fileTypes = new HashMap<>();
    private HashMap<String, String> drives = new HashMap<>();
    public static HashMap<String, TableView<Model.File>> tables = new HashMap<>();
    private IntegerProperty threadPoolFinishedListener = new SimpleIntegerProperty();
    private Text indicator = new Text();

    @Override
    public void initialize(URL location, ResourceBundle resources){
        try {
            ScanAndSetDrives();
            SetFileTypes();
            InitializeTables();
            LoadDataFromDB();
            GridPane.setHalignment(indicator, HPos.RIGHT);
            indicator.setStyle("-fx-fill: -fx-second; -fx-font-size: 14px;");
            indicator.setVisible(false);
            innerGridPane.add(indicator,3,11);
            progressBar.progressProperty().bind(pbm.progressProperty());
            pbm.totalWorkLoadProperty().addListener(e -> Platform.runLater(() -> totalFilesNumber.setText(String.format("%,.0f", pbm.getTotalWorkLoad()))));
            pbm.workFinishedProperty().addListener(e -> Platform.runLater(() -> {filesDoneNumber.setText(String.format("%,.0f",pbm.getWorkFinished())); indicator.setText(pbm.getProgressText());}));
        } catch (SQLException | InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException | ParseException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void closeWindow(MouseEvent event){
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void maximizeWindow(MouseEvent event){
        if(event.getSource().getClass() == HBox.class)
            if(event.getClickCount() != DOUBLE_CLICK)
                return;
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Screen activeScreen = Screen.getScreensForRectangle(stage.getX(), stage.getY(), stage.getWidth(), stage.getHeight()).get(0);
        Rectangle2D window = activeScreen.getBounds();
        int taskBarHeight = (int)window.getHeight() - GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;
        if(MAXIMIZED){
            maximizeButton.setId("maximizeButton");
            stage.setX(window.getMinX() + (window.getWidth() - PREVIOUS_WIDTH)/2);
            stage.setY(window.getMinY() + (window.getHeight() - PREVIOUS_HEIGHT)/2);
            stage.setHeight(PREVIOUS_HEIGHT);
            stage.setWidth(PREVIOUS_WIDTH);
            MAXIMIZED = false;
        }else{
            maximizeButton.setId("windowedButton");
            PREVIOUS_HEIGHT = stage.getHeight();
            PREVIOUS_WIDTH = stage.getWidth();

            stage.setX(window.getMinX());
            stage.setY(window.getMinY());
            stage.setHeight(window.getHeight() - taskBarHeight);
            stage.setWidth(window.getWidth());
            MAXIMIZED = true;
        }

    }

    @FXML
    private void minimizeWindow(MouseEvent event){
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void scanTables() throws IllegalAccessException, InvocationTargetException, ParseException, InstantiationException, SQLException, NoSuchMethodException {
        String drive = driveComboBox.getSelectionModel().getSelectedItem();
        String fileType = fileTypeComboBox.getSelectionModel().getSelectedItem();
        String keyword = keywordText.getText();
        int matchCase = matchCaseCheck.isSelected() ? 1 : 0;
        boolean driveNotSet = (drive == null || drive.isEmpty() || drive.equals("All"));
        boolean fileTypeNotSet = (fileType == null || fileType.isEmpty() || fileType.equals("All"));
        boolean keywordNotSet = (keyword == null || keyword.isEmpty());

        if(!driveNotSet)
            drive = drive.replace('\\', '/');

        for(TableView<Model.File> table : tables.values())
            table.getItems().remove(0,table.getItems().size());

        ObservableList<Model.File> files = Database.Select(new Model.File(), null);
        if(files!=null){
            setProgressBar(true);
            pbm.setTotalWorkLoad(files.size());
            for(Model.File file : files){
                boolean drivePathCondition = driveNotSet || file.getDirectory().contains(drive);
                boolean extensionCondition = fileTypeNotSet || file.getFileType().equals(fileType);
                Boolean[] keywordCondition = new Boolean[]{
                        (keywordNotSet || file.getName().toLowerCase().contains(keyword.toLowerCase())),
                        (keywordNotSet || file.getName().contains(keyword))
                };
                boolean isAccurateFile = drivePathCondition && extensionCondition && keywordCondition[matchCase];
                if(isAccurateFile)
                    putToTable(file);
                pbm.setWorkFinished(pbm.getWorkFinished()+1);
                pbm.setProgress(pbm.getWorkFinished()/pbm.getTotalWorkLoad());
            }
            new CustomizedDialog("Office Handler", "Scan is completed successfully !", "/resources/desktop-icon.png", Main.getPrimaryStage(), false);
            resetProgressBar(true);
        }

        if(driveComboBox.getSelectionModel().getSelectedItem() != null && driveComboBox.getSelectionModel().getSelectedItem().length() > 3)
            driveComboBox.getItems().remove(driveComboBox.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void scanDrives(){
        ArrayList<FileSystemSearch> searches = new ArrayList<>();
        ArrayList<FileCount> fileCounts = new ArrayList<>();
        ArrayList<String> extensions = new ArrayList<>();
        String keyword = keywordText.getText();
        ArrayList<File> directories = new ArrayList<>();

        if(driveComboBox.getSelectionModel().getSelectedItem() == null || driveComboBox.getSelectionModel().getSelectedItem().equals("All"))
            for(String drive : drives.values())
                directories.add(new File(drive));
        else
            directories.add(new File(driveComboBox.getSelectionModel().getSelectedItem()));

        if(fileTypeComboBox.getSelectionModel().getSelectedItem() == null || fileTypeComboBox.getSelectionModel().getSelectedItem().equals("All"))
            for(FileType fileType : fileTypes.values())
                extensions.add(fileType.getName());
        else
            extensions.add(fileTypeComboBox.getSelectionModel().getSelectedItem());

        for(File directory : directories){
            FileSystemSearch filteredSearch = new FileSystemSearch(directory, directory, keyword, this, extensions,true);
            filteredSearch.setIsMatchCase(matchCaseCheck.isSelected() ? 1 : 0);
            filteredSearch.setOnSucceeded(event -> threadPoolFinishedListener.set(threadPoolFinishedListener.get() + 1));
            FileCount countFiles = new FileCount(directory, directory, this);
            searches.add(filteredSearch);
            fileCounts.add(countFiles);
        }

        threadPoolFinishedListener.addListener((observable, oldValue, newValue) -> {
            if(newValue.intValue() == searches.size()){
                new CustomizedDialog("Office Handler", "Scan is completed successfully !", "/resources/desktop-icon.png", Main.getPrimaryStage(), true);
                resetProgressBar(false);
                threadPoolFinishedListener.set(0);
            }
        });

        setProgressBar(false);
        for(FileCount countTask : fileCounts)
            new Thread(countTask).start();
        ScheduledThreadPoolExecutor threadPool = new ScheduledThreadPoolExecutor(fileCounts.size());
        for(FileSystemSearch filteredSearch : searches){
            threadPool.schedule(filteredSearch,20, TimeUnit.SECONDS);
        }

        clearFilters();
    }

    @FXML
    private void colorSettings(MouseEvent event){
        ColorPickerDialog colorSettings = new ColorPickerDialog(event, FIRST_COLOR, SECOND_COLOR, FIRST_TEXT_COLOR);
        if(colorSettings.getBackgroundColorHex()!=null && colorSettings.getBorderColorHex()!=null && colorSettings.getTextColorHex()!=null){
            Parent primaryStageRoot = Main.getPrimaryStage().getScene().getRoot();
            FIRST_COLOR = colorSettings.getBackgroundColorHex();
            SECOND_COLOR = colorSettings.getBorderColorHex();
            FIRST_TEXT_COLOR = colorSettings.getTextColorHex();
            primaryStageRoot.setStyle("-fx-first: " + FIRST_COLOR + ";" + "-fx-second: " + SECOND_COLOR + ";" + "-fx-first-text: " + FIRST_TEXT_COLOR + ";");
        }
    }

    @FXML
    private void addToFavorites() throws IllegalAccessException {
        String selectedTab = tabs.getSelectionModel().getSelectedItem().getId().substring(0,3);
        TableView<Model.File> focusedTable = tables.get(selectedTab);
        Model.File selectedFile = focusedTable.getSelectionModel().getSelectedItem();
        focusedTable.getSelectionModel().select(null);

        if(selectedFile != null){
            if(selectedFile.getFav()== 0){
                selectedFile.setFav(1);
                Database.Update(selectedFile);
                selectedFile.createUndoFavButton();
                favTable.getItems().add(selectedFile);
                new CustomizedDialog("Office Handler", "Selected file is added to the Favorites !", "/resources/confirmbox.png", Main.getPrimaryStage(), false);
            }
            else
                new CustomizedDialog("Office Handler", "You can not add a file which has already exist in the Favorites !", "/resources/alertbox.png", Main.getPrimaryStage(), false);
        }else
            new CustomizedDialog("Office Handler", "Please select a file to add to Favorites !", "/resources/alertbox.png", Main.getPrimaryStage(), false);
    }

    @FXML
    private void clearFiltersLoadAgain() throws IllegalAccessException, ParseException, InstantiationException, SQLException, NoSuchMethodException, InvocationTargetException {
        if(pbm.getProgress()==0.0){
            clearFilters();
            if(!scanDrivesButton.isDisabled())
                scanTables();
        }
    }

    @FXML
    private void displayHelpDialog() {
        new CustomizedDialog("Office Handler", "This program was created in the scope of term project of Yildiz Technical University Computer Engineering Department. All rights are reserved by YTU CE. Mustafa ULUKAYA | Onur AY | 2019®", "/resources/desktop-icon.png", Main.getPrimaryStage(), false);
    }

    private void clearFilters() {
        if(driveComboBox.getSelectionModel().getSelectedItem() != null && driveComboBox.getSelectionModel().getSelectedItem().length() > 3)
            driveComboBox.getItems().remove(driveComboBox.getSelectionModel().getSelectedItem());
        driveComboBox.getSelectionModel().clearSelection();
        fileTypeComboBox.getSelectionModel().clearSelection();
        keywordText.setText("");
        matchCaseCheck.setSelected(false);
    }

    private void LoadDataFromDB() throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, ParseException {
        ObservableList<Model.File> files = Database.Select(new Model.File(), null);
        if(files!=null)
            for(Model.File file : files)
                putToTable(file);
    }

    private void SetFileTypes() throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, ParseException {
        ObservableList<Model.FileType> result = Database.Select(new FileType(), null);
        fileTypeComboBox.getItems().add("All");
        if(result != null){
            for(FileType fileType : result){
                fileTypes.put(fileType.getName(), fileType);
                fileTypeComboBox.getItems().add(fileTypes.get(fileType.getName()).getName());
            }
        }
        fileTypeComboBox.getItems().add("Add new file type...");
        fileTypeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> { if(newValue != null && newValue.equals("Add new file type...")) {
            try {
                if(!addNewFileType())
                    Platform.runLater(() -> fileTypeComboBox.getSelectionModel().clearSelection());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        });
    }

    public void createScannedFiles(ArrayList<String> fileNames) throws IOException, ParseException, IllegalAccessException {
        File scannedFile;
        Model.File scannedFileModel;
        Path directory;
        String author;
        Date creationDate;
        Date lastModifiedDate;
        String fileType;
        Double size;
        DateFormat creationDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        BasicFileAttributes attributes;
        for(String fileName : fileNames){
            scannedFile = new File(fileName);
            directory = scannedFile.toPath();
            attributes = Files.readAttributes(directory, BasicFileAttributes.class);
            creationDate = new Date(creationDateFormat.parse(creationDateFormat.format(attributes.creationTime().toMillis())));
            size = Maths.round((double)scannedFile.length()/(1024),2);
            fileType = fileName.substring(fileName.lastIndexOf('.')+1);
            author = Files.getOwner(directory).toString().replace('\\','/');
            lastModifiedDate = new Date(creationDateFormat.parse(creationDateFormat.format(Files.getLastModifiedTime(directory).toMillis())));
            scannedFileModel = new Model.File(fileName.substring(fileName.lastIndexOf('/')+1,fileName.lastIndexOf('.')), fileType, author, directory, creationDate, lastModifiedDate, size, 0);
            if(Database.Insert(scannedFileModel)){
                fileType = fileType.toLowerCase();
                if(fileType.contains("pdf") || fileType.contains("xls") || fileType.contains("ppt") || fileType.contains("doc")){
                    if(fileType.equals("xlsx") || fileType.equals("pptx") || fileType.equals("docx"))
                        fileType = fileType.substring(0,3);
                    tables.get(fileType).getItems().add(scannedFileModel);
                }else
                    tables.get("oth").getItems().add(scannedFileModel);
            }
        }
    }

    private boolean addNewFileType() throws IllegalAccessException {
        CustomizedDialog addNewFileTypeDialog = new CustomizedDialog("New File Type", "Please write the file type without comma before it. (Ex.: exe, txt etc.)", "/resources/desktop-icon.png", "File Type:", Main.getPrimaryStage(), true);
        String result = addNewFileTypeDialog.getResult();
        if(result != null){
            if(result.isEmpty()){
                new CustomizedDialog("New File Type", "You can not add an empty file type !", "/resources/alertbox.png", Main.getPrimaryStage(), false);
                return false;
            }
            else {
                FileType newFileType = new FileType(result.toLowerCase());
                newFileType.setDefault(0);
                if (!Database.Insert(newFileType)) {
                    new CustomizedDialog("New File Type", "You can not add a file type which has already exists in the list !", "/resources/alertbox.png", Main.getPrimaryStage(), false);
                    return false;
                }
                fileTypeComboBox.getItems().add(fileTypeComboBox.getItems().size() - 1, newFileType.getName());
                fileTypeComboBox.getSelectionModel().select(fileTypeComboBox.getItems().size() - 2);
                return true;
            }
        }
        return false;
    }

    private boolean isExistDirectory(String newDirectory){
        for(String directory : drives.values())
            if(directory.equals(newDirectory.replace('/','\\')))
                return true;
        return false;
    }

    private boolean selectSpecificDirectory(Stage primaryStage){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(primaryStage);
        if(selectedDirectory != null){
            if(!isExistDirectory(selectedDirectory.getPath())){
                driveComboBox.getItems().add(driveComboBox.getItems().size()-1,selectedDirectory.getAbsolutePath());
                driveComboBox.getSelectionModel().select(selectedDirectory.getAbsolutePath());
                return true;
            } else{
                new CustomizedDialog("Select Specific Directory...", "You can not add an existing directory !", "/resources/alertbox.png", Main.getPrimaryStage(), false);
                return false;
            }
        } else
            return false;
    }

    private void setProgressBar(Boolean isTableSearch){
        if(isTableSearch)
            scanTablesLoading.setVisible(true);
        else{
            totalFilesLabel.setVisible(true);
            totalFilesNumber.setVisible(true);
            filesDoneLabel.setVisible(true);
            filesDoneNumber.setVisible(true);
            scanDrivesLoading.setVisible(true);
        }
        scanDrivesButton.setDisable(true);
        scanTablesButton.setDisable(true);
        indicator.setVisible(true);
    }

    private void resetProgressBar(Boolean isTableSearch){
        if(isTableSearch)
            scanTablesLoading.setVisible(false);
        else{
            totalFilesLabel.setVisible(false);
            totalFilesNumber.setVisible(false);
            filesDoneLabel.setVisible(false);
            filesDoneNumber.setVisible(false);
            scanDrivesLoading.setVisible(false);
        }
        scanDrivesButton.setDisable(false);
        scanTablesButton.setDisable(false);
        pbm.setTotalWorkLoad(0.0);
        pbm.setWorkFinished(0.0);
        pbm.setProgress(0.0);
        indicator.setVisible(false);
    }

    private void putToTable(Model.File file){
        if(fileTypes.get(file.getFileType()).getDefault()==1){
            String fileType = file.getFileType().length()>3 ? file.getFileType().substring(0,3) : file.getFileType();
            tables.get(fileType).getItems().add(file);
        }else{
            tables.get("oth").getItems().add(file);
        }
        if(file.getFav()==1){
            file.createUndoFavButton();
            tables.get("fav").getItems().add(file);
        }
    }

    private void ScanAndSetDrives(){
        File[] paths;
        FileSystemView fsv = FileSystemView.getFileSystemView();
        paths = File.listRoots();

        driveComboBox.getItems().add("All");

        for(File path:paths)
            if(fsv.getSystemTypeDescription(path).contains("Local")){
                driveComboBox.getItems().add(path.toString());
                drives.put(path.toString().replaceAll("[/:]", "").replace("\\", ""), path.toString());
            }

        driveComboBox.getItems().add("Select specific directory...");
        driveComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.equals(driveComboBox.getItems().get(driveComboBox.getItems().size() - 1)))
                if(!Controller.this.selectSpecificDirectory(Main.getPrimaryStage()))
                    Platform.runLater(() -> driveComboBox.getSelectionModel().clearSelection());
        });
    }

    private void InitializeTables(){
        tables.put("pdf", pdfTable);
        tables.put("doc", docTable);
        tables.put("ppt", pptTable);
        tables.put("xls", xlsTable);
        tables.put("oth", otherTable);
        tables.put("fav", favTable);

        for(TableView<Model.File> table : tables.values()) {
            TableColumn deleteCol = table.getColumns().get(0);
            deleteCol.setCellValueFactory(new PropertyValueFactory<Model.File, Button>("DeleteButton"));

            TableColumn fileNameCol = table.getColumns().get(1);
            fileNameCol.setCellValueFactory(new PropertyValueFactory<Model.File, String>("Name"));

            TableColumn creationDateCol = table.getColumns().get(2);
            creationDateCol.setStyle("-fx-alignment: CENTER;");
            creationDateCol.setCellValueFactory(new PropertyValueFactory<Model.File, String>("CreationDate"));

            TableColumn lastModifiedDateCol = table.getColumns().get(3);
            lastModifiedDateCol.setStyle("-fx-alignment: CENTER;");
            lastModifiedDateCol.setCellValueFactory(new PropertyValueFactory<Model.File, String>("LastModifiedDate"));

            TableColumn authorCol = table.getColumns().get(4);
            authorCol.setCellValueFactory(new PropertyValueFactory<Model.File, String>("Author"));

            TableColumn sizeCol = table.getColumns().get(5);
            sizeCol.setStyle("-fx-alignment: CENTER-RIGHT;");
            sizeCol.setCellValueFactory(new PropertyValueFactory<Model.File, String>("Size"));

            TableColumn typeCol = table.getColumns().get(6);
            typeCol.setStyle("-fx-alignment: CENTER;");
            typeCol.setCellValueFactory(new PropertyValueFactory<Model.File, String>("FileType"));

            TableColumn pathCol = table.getColumns().get(7);
            pathCol.setCellValueFactory(new PropertyValueFactory<Model.File, String>("Directory"));

            table.setRowFactory( tableView -> {
                TableRow<Model.File> selectedRow = new TableRow<>();
                selectedRow.setOnMouseClicked(event -> {
                    if (event.getClickCount() == DOUBLE_CLICK && (! selectedRow.isEmpty()) ) {
                        try {
                            Model.File selectedFile = selectedRow.getItem();
                            File openFile = new File(selectedFile.getDirectory());
                            if(openFile.exists())
                                Desktop.getDesktop().open(openFile);
                            else{
                                Database.Delete(selectedFile);
                                tables.get(selectedFile.getFileType()).getItems().remove(selectedFile);
                                new CustomizedDialog("Office Handler", "File not found. The file you are trying to open may be deleted or moved.", "/resources/alertbox.png", Main.getPrimaryStage(), false);
                            }
                        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                });
                return selectedRow;
            });

            table.widthProperty().addListener((source, oldWidth, newWidth) -> {
                TableHeaderRow header = (TableHeaderRow) table.lookup("TableHeaderRow");
                header.reorderingProperty().addListener((observable, oldValue, newValue) -> header.setReordering(false));
            });
        }

        tables.get("fav").getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(oldValue != null)
                oldValue.getDeleteButton().setId("rowDeleteButton");
            newValue.getDeleteButton().setId("selectedRowDeleteButton");
        });
    }
}