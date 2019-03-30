package Model;

import java.sql.Date;

public class File {
    public int ID;

    public File(int ID, String name, Model.FileType fileType) {
        this.ID = ID;
        Name = name;
        FileType = fileType;
    }

    public String Name;

    public FileType FileType;

    public String Author;

    public String Directory;

    public Date Date;

    public double Size;

    public boolean IsFav;

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

    public FileType getFileType() {
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

    public java.sql.Date getDate() {
        return Date;
    }

    public void setDate(java.sql.Date date) {
        Date = date;
    }

    public double getSize() {
        return Size;
    }

    public void setSize(double size) {
        Size = size;
    }

    public boolean isFav() {
        return IsFav;
    }

    public void setFav(boolean fav) {
        IsFav = fav;
    }
}
