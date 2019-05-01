package Model;

import Classes.Database;
import Classes.Date;
import Classes.Maths;
import Main.Controller;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.input.MouseEvent;

import java.nio.file.Path;
import java.text.SimpleDateFormat;

public class File {
    private Integer ID;
    private String Name;
    private String FileType;
    private String Author;
    private Path Directory;
    private Date CreationDate;
    private Date LastModifiedDate;
    private Double Size;
    private Integer Fav;
    private Button DeleteButton;

    public File(){

    }

    public File(int ID, String name, String fileType) {
        this.ID = ID;
        Name = name;
        FileType = fileType;
    }

    public File(String name, String fileType, String author, Path directory, Date creationDate, Date lastModifiedDate, Double size, Integer fav) {
        Name = name;
        FileType = fileType;
        Author = author;
        Directory = directory;
        CreationDate = creationDate;
        LastModifiedDate = lastModifiedDate;
        Size = Maths.round(size,2);
        Fav = fav;
    }

    public File(Integer ID, String name, String fileType, String author, Path directory, Date creationDate, Date lastModifiedDate, Double size, Integer fav) {
        this.ID = ID;
        Name = name;
        FileType = fileType;
        Author = author;
        Directory = directory;
        CreationDate = creationDate;
        LastModifiedDate = lastModifiedDate;
        Size = Maths.round(size,2);
        Fav = fav;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getFileType() {
        return FileType;
    }

    public void setFileType(String fileType) {
        FileType = fileType;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getDirectory() {
        return Directory.toString().replace("\\","/");
    }

    public Path getDirectoryObject() {
        return Directory;
    }

    public void setDirectory(Path directory) {
        Directory = directory;
    }

    public String getCreationDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(CreationDate);
    }

    public Date getCreationDateObject() {
        return CreationDate;
    }

    public void setCreationDate(Date date) {
        CreationDate = date;
    }

    public String getLastModifiedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(LastModifiedDate);
    }

    public Date getLastModifiedDateObject() {
        return LastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        LastModifiedDate = lastModifiedDate;
    }

    public double getSize() {
        return Size;
    }

    public void setSize(double size) {
        Size = size;
    }

    public Integer getFav() {
        return Fav;
    }

    public void setFav(Integer fav) {
        Fav = fav;
    }

    public Button getDeleteButton() {
        return DeleteButton;
    }

    private void setDeleteButton(Button deleteButton) {
        DeleteButton = deleteButton;
    }

    public void createUndoFavButton() {
        Button undoFav = new Button();
        undoFav.setMinWidth(25);
        undoFav.setMaxWidth(25);
        undoFav.setMinHeight(25);
        undoFav.setMaxHeight(25);
        undoFav.setId("rowDeleteButton");
        undoFav.setOnMouseClicked(event -> {
            try {
                this.setFav(0);
                Database.Update(this);
                Controller.tables.get("fav").getItems().remove(this);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        setDeleteButton(undoFav);
    }
}
