package Model;

import Classes.Database;
import Classes.Date;
import Classes.Maths;
import Main.Controller;
import javafx.scene.control.Button;
import java.nio.file.Path;

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

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public String getFileType() {
        return FileType;
    }

    public String getDirectory() {
        return Directory.toString().replace("\\","/");
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

    public Date getCreationDate() {
        return CreationDate;
    }

    public void setCreationDate(Date creationDate) {
        CreationDate = creationDate;
    }

    public Date getLastModifiedDate() {
        return LastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        LastModifiedDate = lastModifiedDate;
    }

    public Double getSize() {
        return Size;
    }

    public void setSize(Double size) {
        Size = size;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
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
