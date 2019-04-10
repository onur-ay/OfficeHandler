package Model;

import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

public class File {
    private int ID;
    private String Name;
    private FileType FileType;
    private String Author;
    private Path Directory;
    private Date CreationDate;
    private Date LastModifiedDate;
    private double Size;
    private boolean Fav;

    public File(){

    }

    public File(int ID, String name, FileType fileType) {
        this.ID = ID;
        Name = name;
        FileType = fileType;
    }

    public File(String name, Model.FileType fileType, String author, Path directory, Date creationDate, Date lastModifiedDate, double size, boolean fav) {
        Name = name;
        FileType = fileType;
        Author = author;
        Directory = directory;
        CreationDate = creationDate;
        LastModifiedDate = lastModifiedDate;
        Size = size;
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
        return FileType.getName();
    }

    public FileType getFileTypeObject() {
        return FileType;
    }

    public void setFileType(FileType fileType) {
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

    public boolean getFav() {
        return Fav;
    }

    public void setFav(boolean fav) {
        Fav = fav;
    }
}
