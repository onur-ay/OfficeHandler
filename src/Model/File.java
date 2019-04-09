package Model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class File {
    private int ID;
    private String Name;
    private FileType FileType;
    private String Author;
    private String Directory;
    private Date CreationDate;
    private double Size;
    private boolean Fav;

    public File(int ID, String name, FileType fileType) {
        this.ID = ID;
        Name = name;
        FileType = fileType;
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
        return Directory;
    }

    public void setDirectory(String directory) {
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
