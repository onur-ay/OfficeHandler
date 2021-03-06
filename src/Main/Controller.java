package Main;

import Classes.*;
import Classes.Date;
import Model.FileType;
import com.sun.javafx.scene.control.skin.TableHeaderRow;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
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
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import static Classes.Constants.*;

/**
 *  DEFAULT OLAN FİLETYPE'LAR SETUP ESNASINDA DB'YE BASILACAK VERİTABANINDAN SİLMEYİ UNUTMA, FİLETYPE'LARI DB'YE BASMAYI UNUTMA !
 *
 */

public class Controller implements Initializable {

    public static final int START_AFTER_SECOND = 10;
    private static boolean ALGORITHM = BFS;
    private static boolean MAXIMIZED = false;
    private static boolean IS_TABLE_FILTERED = false;

    private static double PREVIOUS_HEIGHT;
    private static double PREVIOUS_WIDTH;
    public static String CURRENT_FIRST_COLOR = DEFAULT_FIRST_COLOR;
    public static String CURRENT_SECOND_COLOR = DEFAULT_SECOND_COLOR;
    public static String CURRENT_FIRST_TEXT_COLOR = DEFAULT_FIRST_TEXT_COLOR;

    @FXML
    public ComboBox<String> driveComboBox;
    public ComboBox<String> fileTypeComboBox;
    public ComboBox<String> algorithmComboBox;
    public TabPane tabs;
    public Button maximizeButton;
    public Button scanDrivesButton;
    public Button scanTablesButton;
    public Button analyzeButton;
    public CheckBox matchCaseCheck;
    public TextField keywordText;
    public Label totalFilesLabel;
    public Label filesDoneLabel;
    public Label totalFilesNumber;
    public Label filesDoneNumber;
    public ImageView matchCaseTooltip;
    public ImageView algorithmTooltip;
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
    public ProgressIndicator analyzeLoading = new ProgressIndicator();
    public ProgressBar progressBar = new ProgressBar();

    public ProgressBarManager pbm = new ProgressBarManager(0.0, 0.0, 0.0, 0.0);
    private HashMap<String, FileType> fileTypes = new HashMap<>();
    private HashMap<String, String> drives = new HashMap<>();
    public static HashMap<String, TableView<Model.File>> tables = new HashMap<>();
    private IntegerProperty threadPoolFinishedListener = new SimpleIntegerProperty();
    private IntegerProperty threadPoolFinishedListenerBFS = new SimpleIntegerProperty();
    private Text indicator = new Text();
    private ArrayList<FileSystemSearch> searches;
    private ArrayList<FileSystemSearch> searchesBFS;
    private ArrayList<String> extensionsFilter;
    private ArrayList<File> directoriesFilter;
    private String keywordFilter;
    private int matchCaseFilter;
    private long DFSstartTime;
    private long DFSendTime;
    private long BFSstartTime;
    private long BFSendTime;
    private long DFSworkTime;
    private long BFSworkTime;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        try {
            matchCaseCheck.setDisable(true);
            Tooltip.install(matchCaseTooltip, MATCH_CASE_TOOLTIP);
            Tooltip.install(algorithmTooltip, ALGORITHM_TOOLTIP);
            keywordText.textProperty().addListener((observable, oldValue, newValue) -> {matchCaseCheck.setDisable(newValue.equals("")); if(newValue.equals("")) matchCaseTooltip.setVisible(true); else matchCaseTooltip.setVisible(false);});
            algorithmComboBox.getItems().addAll(DFS_TEXT, BFS_TEXT);
            algorithmComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> ALGORITHM = newValue.equals(DFS_TEXT) ? DFS : BFS);
            ScanAndSetDrives();
            SetFileTypes();
            InitializeTables();
            LoadDataFromDB();
            GridPane.setHalignment(indicator, HPos.RIGHT);
            indicator.setStyle("-fx-fill: -fx-second; -fx-font-size: 14px;");
            indicator.setVisible(false);
            innerGridPane.add(indicator,3,11);
            progressBar.progressProperty().bind(pbm.progressProperty());
            pbm.totalWorkLoadProperty().addListener(e -> Platform.runLater(() -> totalFilesNumber.setText(String.format(NUMBER_WITH_COMMA_PATTERN, pbm.getTotalWorkLoad()))));
            pbm.workFinishedProperty().addListener(e -> Platform.runLater(() -> {filesDoneNumber.setText(String.format(NUMBER_WITH_COMMA_PATTERN,pbm.getWorkFinished())); indicator.setText(pbm.getProgressText());}));
            threadPoolFinishedListener.addListener((observable, oldValue, newValue) -> {
                if(newValue.intValue() == searches.size()){
                    if(DFSstartTime != 0){
                        DFSworkTime = DFSendTime - DFSstartTime;
                        totalFilesNumber.setText(String.format(DISPLAY_TIME_PATTERN,((DFSworkTime/1000)/60)/60,(DFSworkTime/1000)/60,(DFSworkTime/1000)%60));
                        if(threadPoolFinishedListenerBFS.intValue() == searchesBFS.size()){
                            ResetAnalyzeInfo();
                            pbm.setProgress(0);
                            threadPoolFinishedListener.set(0);
                            scanTablesButton.setDisable(false);
                            scanDrivesButton.setDisable(false);
                            analyzeButton.setDisable(false);
                            analyzeLoading.setVisible(false);
                        }
                        DFSstartTime = 0;
                    }else{
                        new CustomizedDialog(PROGRAM_NAME, SCAN_IS_DONE_TEXT, PROGRAM_ICON, Main.getPrimaryStage(), true);
                        resetProgressBar(false);
                        threadPoolFinishedListener.set(0);
                    }
                }
            });
            threadPoolFinishedListenerBFS.addListener((observable, oldValue, newValue) -> {
                if(newValue.intValue() == searchesBFS.size()){
                    if(BFSstartTime != 0){
                        BFSworkTime = BFSendTime - BFSstartTime;
                        filesDoneNumber.setText(String.format(DISPLAY_TIME_PATTERN,((BFSworkTime/1000)/60)/60,(BFSworkTime/1000)/60,(BFSworkTime/1000)%60));
                        if(threadPoolFinishedListener.intValue() == searchesBFS.size()){
                            ResetAnalyzeInfo();
                            pbm.setProgress(0);
                            BFSstartTime = 0;
                            threadPoolFinishedListenerBFS.set(0);
                            scanTablesButton.setDisable(false);
                            scanDrivesButton.setDisable(false);
                            analyzeButton.setDisable(false);
                            analyzeLoading.setVisible(false);
                        }
                    }else{
                        new CustomizedDialog(PROGRAM_NAME, SCAN_IS_DONE_TEXT, PROGRAM_ICON, Main.getPrimaryStage(), true);
                        resetProgressBar(false);
                        threadPoolFinishedListenerBFS.set(0);
                    }
                }

            });
        } catch (SQLException | InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
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
    private void scanTables() throws IllegalAccessException, InvocationTargetException, InstantiationException, SQLException, NoSuchMethodException {
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
            new CustomizedDialog(PROGRAM_NAME, SCAN_IS_DONE_TEXT, PROGRAM_ICON, Main.getPrimaryStage(), false);
            resetProgressBar(true);
            IS_TABLE_FILTERED = true;
        }

        if(driveComboBox.getSelectionModel().getSelectedItem() != null && driveComboBox.getSelectionModel().getSelectedItem().length() > 3)
            driveComboBox.getItems().remove(driveComboBox.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void scanDrives() throws InvocationTargetException, SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException {
        if(IS_TABLE_FILTERED){
            IS_TABLE_FILTERED = false;
            scanTables();
        }
        searches = new ArrayList<>();
        ArrayList<FileCount> fileCounts = new ArrayList<>();

        getFilters();

        for(File directory : directoriesFilter){
            FileSystemSearch filteredSearch = new FileSystemSearch(directory, directory, keywordFilter, this, extensionsFilter, ALGORITHM, CREATEFILE);
            filteredSearch.setIsMatchCase(matchCaseFilter);
            filteredSearch.setOnSucceeded(event -> threadPoolFinishedListener.set(threadPoolFinishedListener.get() + 1));
            FileCount countFiles = new FileCount(directory, directory, this, ALGORITHM);
            searches.add(filteredSearch);
            fileCounts.add(countFiles);
        }

        setProgressBar(false);
        ScheduledThreadPoolExecutor threadPool = new ScheduledThreadPoolExecutor(searches.size());
        for(int i=0; i<searches.size(); i++){
            new Thread(fileCounts.get(i)).start();
            threadPool.schedule(searches.get(i), START_AFTER_SECOND, TimeUnit.SECONDS);
        }
    }

    @FXML
    private void analyzeAlgorithms(){
        searches = new ArrayList<>();
        searchesBFS = new ArrayList<>();
        extensionsFilter = new ArrayList<>();

        for(FileType extension : fileTypes.values())
            extensionsFilter.add(extension.getName());

        keywordFilter = keywordText.getText();
        DFSstartTime = 0;
        DFSendTime = 0;
        DFSworkTime = 0;
        BFSstartTime = 0;
        BFSendTime = 0;
        BFSworkTime = 0;

        for(String directory : drives.values()){
            FileSystemSearch filteredDFSSearch = new FileSystemSearch(new File(directory), new File(directory), keywordFilter, this, extensionsFilter, DFS, DONTCREATEFILE);
            FileSystemSearch filteredBFSSearch = new FileSystemSearch(new File(directory), new File(directory), keywordFilter, this, extensionsFilter, BFS, DONTCREATEFILE);
            filteredDFSSearch.setIsMatchCase(matchCaseFilter);
            filteredBFSSearch.setIsMatchCase(matchCaseFilter);
            filteredDFSSearch.setOnSucceeded(event -> { long now = System.currentTimeMillis(); DFSendTime = DFSendTime < now ? now : DFSendTime; threadPoolFinishedListener.set(threadPoolFinishedListener.get() + 1); });
            filteredBFSSearch.setOnSucceeded(event -> { long now = System.currentTimeMillis(); BFSendTime = BFSendTime < now ? now : BFSendTime; threadPoolFinishedListenerBFS.set(threadPoolFinishedListenerBFS.get() + 1); });
            searches.add(filteredDFSSearch);
            searchesBFS.add(filteredBFSSearch);
        }

        pbm.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
        scanTablesButton.setDisable(true);
        scanDrivesButton.setDisable(true);
        analyzeButton.setDisable(true);
        analyzeLoading.setVisible(true);
        DFSstartTime = System.currentTimeMillis();
        for(FileSystemSearch filteredDFSSearch : searches)
            new Thread(filteredDFSSearch).start();
        totalFilesLabel.setVisible(true);
        totalFilesLabel.setText(DFS_DURATION_TEXT);
        totalFilesNumber.setVisible(true);
        totalFilesNumber.setText(WAITING_FOR_RESULTS_TEXT);

        BFSstartTime = System.currentTimeMillis();
        for(FileSystemSearch filteredBFSSearch : searchesBFS)
            new Thread(filteredBFSSearch).start();
        filesDoneLabel.setVisible(true);
        filesDoneLabel.setText(BFS_DURATION_TEXT);
        filesDoneNumber.setVisible(true);
        filesDoneNumber.setText(WAITING_FOR_RESULTS_TEXT);
    }

    @FXML
    private void colorSettings(MouseEvent event){
        ColorPickerDialog colorSettings = new ColorPickerDialog(event, CURRENT_FIRST_COLOR, CURRENT_SECOND_COLOR, CURRENT_FIRST_TEXT_COLOR);
        if(colorSettings.getBackgroundColorHex()!=null && colorSettings.getBorderColorHex()!=null && colorSettings.getTextColorHex()!=null){
            Parent primaryStageRoot = Main.getPrimaryStage().getScene().getRoot();
            CURRENT_FIRST_COLOR = colorSettings.getBackgroundColorHex();
            CURRENT_SECOND_COLOR = colorSettings.getBorderColorHex();
            CURRENT_FIRST_TEXT_COLOR = colorSettings.getTextColorHex();
            primaryStageRoot.setStyle("-fx-first: " + CURRENT_FIRST_COLOR + ";" + "-fx-second: " + CURRENT_SECOND_COLOR + ";" + "-fx-first-text: " + CURRENT_FIRST_TEXT_COLOR + ";");
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
                if(Database.Update(selectedFile)){
                    selectedFile.createUndoFavButton();
                    favTable.getItems().add(selectedFile);
                    new CustomizedDialog(PROGRAM_NAME, ADD_FAV_TEXT, CONFIRM_BOX_ICON, Main.getPrimaryStage(), false);
                } else{
                    System.out.println(LOG_UPDATE_FILE_DOES_NOT_EXIST_TEXT);
                    selectedFile.setFav(0);
                }
            }
            else
                new CustomizedDialog(PROGRAM_NAME, CANT_ADD_FAV_TEXT, ALERT_BOX_ICON, Main.getPrimaryStage(), false);
        }else
            new CustomizedDialog(PROGRAM_NAME, SELECT_FAV_TEXT, ALERT_BOX_ICON, Main.getPrimaryStage(), false);
    }

    @FXML
    private void clearFilters() throws IllegalAccessException, InstantiationException, SQLException, NoSuchMethodException, InvocationTargetException {
        if(pbm.getProgress()==0.0){
            if(driveComboBox.getSelectionModel().getSelectedItem() != null && driveComboBox.getSelectionModel().getSelectedItem().length() > 3)
                driveComboBox.getItems().remove(driveComboBox.getSelectionModel().getSelectedItem());
            driveComboBox.getSelectionModel().clearSelection();
            fileTypeComboBox.getSelectionModel().clearSelection();
            keywordText.setText("");
            matchCaseCheck.setSelected(false);
            if(IS_TABLE_FILTERED){
                scanTables();
                IS_TABLE_FILTERED = false;
            }
        }
    }

    @FXML
    private void displayHelpDialog() {
        new CustomizedDialog(PROGRAM_NAME, HELP_DIALOG_TEXT, PROGRAM_ICON, Main.getPrimaryStage(), false);
    }

    private void getFilters(){
        extensionsFilter = new ArrayList<>();
        keywordFilter = keywordText.getText();
        directoriesFilter = new ArrayList<>();
        matchCaseFilter = matchCaseCheck.isSelected() ? 1 : 0;

        if(driveComboBox.getSelectionModel().getSelectedItem() == null || driveComboBox.getSelectionModel().getSelectedItem().equals(ITEM_ALL_TEXT))
            for(String drive : drives.values())
                directoriesFilter.add(new File(drive));
        else
            directoriesFilter.add(new File(driveComboBox.getSelectionModel().getSelectedItem()));

        if(fileTypeComboBox.getSelectionModel().getSelectedItem() == null || fileTypeComboBox.getSelectionModel().getSelectedItem().equals(ITEM_ALL_TEXT))
            for(FileType fileType : fileTypes.values())
                extensionsFilter.add(fileType.getName());
        else
            extensionsFilter.add(fileTypeComboBox.getSelectionModel().getSelectedItem());
    }

    private void LoadDataFromDB() throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        ObservableList<Model.File> files = Database.Select(new Model.File(), null);
        if(files!=null)
            for(Model.File file : files)
                putToTable(file);
    }

    private void SetFileTypes() throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ObservableList<Model.FileType> result = Database.Select(new FileType(), null);
        fileTypeComboBox.getItems().add(ITEM_ALL_TEXT);
        if(result != null){
            for(FileType fileType : result){
                fileTypes.put(fileType.getName(), fileType);
                fileTypeComboBox.getItems().add(fileTypes.get(fileType.getName()).getName());
            }
        }
        fileTypeComboBox.getItems().add(ADD_NEW_FILE_TYPE_TEXT);
        fileTypeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> { if(newValue != null && newValue.equals(ADD_NEW_FILE_TYPE_TEXT)) {
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
        DateFormat creationDateFormat = new SimpleDateFormat(DATE_PATTERN);
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
            Integer fileID = Database.Insert(scannedFileModel);
            if(fileID > -1){
                scannedFileModel.setID(fileID);
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
        CustomizedDialog addNewFileTypeDialog = new CustomizedDialog(NEW_FILE_TYPE_TEXT, FILE_TYPE_DESCRIPTION_TEXT, PROGRAM_ICON, NEW_FILE_TYPE_TEXT+":", Main.getPrimaryStage(), true);
        String result = addNewFileTypeDialog.getResult();
        if(result != null){
            if(result.isEmpty()){
                new CustomizedDialog(NEW_FILE_TYPE_TEXT, EMPTY_FILE_TYPE_TEXT, ALERT_BOX_ICON, Main.getPrimaryStage(), false);
                return false;
            }
            else {
                FileType newFileType = new FileType(result.toLowerCase());
                newFileType.setDefault(0);
                Integer fileTypeID = Database.Insert(newFileType);
                if (fileTypeID == -1) {
                    new CustomizedDialog(NEW_FILE_TYPE_TEXT, EXISTING_FILE_TYPE_TEXT, ALERT_BOX_ICON, Main.getPrimaryStage(), false);
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
                new CustomizedDialog(SELECT_SPECIFIC_DIRECTORY_TEXT, EXISTING_DIRECTORY_TEXT, ALERT_BOX_ICON, Main.getPrimaryStage(), false);
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
        analyzeButton.setDisable(true);
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
        analyzeButton.setDisable(false);
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

        driveComboBox.getItems().add(SELECT_SPECIFIC_DIRECTORY_TEXT);
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
                                if(!Database.Delete(selectedFile))
                                    System.out.println(FILE_DOES_NOT_EXISTS_IN_DB_TEXT);
                                tables.get(selectedFile.getFileType()).getItems().remove(selectedFile);
                                new CustomizedDialog(PROGRAM_NAME, FILE_NOT_FOUND_TEXT, ALERT_BOX_ICON, Main.getPrimaryStage(), false);
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

    private void ResetAnalyzeInfo(){
        new CustomizedDialog(PROGRAM_NAME, ANALYZE_FINISH_TEXT, CONFIRM_BOX_ICON, Main.getPrimaryStage(), true);
        if(DFSworkTime<BFSworkTime)
            algorithmComboBox.setValue(DFS_TEXT);
        else
            algorithmComboBox.setValue(BFS_TEXT);
        filesDoneLabel.setVisible(false);
        filesDoneNumber.setVisible(false);
        totalFilesLabel.setVisible(false);
        totalFilesNumber.setVisible(false);
        totalFilesLabel.setText(TOTAL_FILES_TEXT);
        totalFilesNumber.setText("");
        filesDoneLabel.setText(FILES_DONE_TEXT);
        filesDoneNumber.setText("");
    }
}