package sample;

import javafx.beans.property.SimpleStringProperty;

public class TableFile {
    private final SimpleStringProperty fileName;
    private final SimpleStringProperty creationDate;
    private final SimpleStringProperty type;
    private final SimpleStringProperty size;
    private final SimpleStringProperty tags;
    private final SimpleStringProperty author;
    private final SimpleStringProperty directory;

    public TableFile(String fileName, String creationDate, String type, String size, String tags, String author, String directory){
        this.fileName = new SimpleStringProperty(fileName);
        this.creationDate = new SimpleStringProperty(creationDate);
        this.type = new SimpleStringProperty(type);
        this.size = new SimpleStringProperty(size);
        this.tags = new SimpleStringProperty(tags);
        this.author = new SimpleStringProperty(author);
        this.directory = new SimpleStringProperty(directory);
}

    public String getFileName(){ return fileName.get();}
    public void setFileName(String fileName){ this.fileName.set(fileName);}

    public String getCreationDate(){ return creationDate.get();}
    public void setCreationDate(String creationDate){ this.creationDate.set(creationDate);}

    public String getType(){ return type.get();}
    public void setType(String type){ this.type.set(type);}

    public String getSize(){ return size.get();}
    public void setSize(String size){ this.size.set(size);}

    public String getTags(){ return tags.get();}
    public void setTags(String tags){ this.tags.set(tags);}

    public String getAuthor(){ return author.get();}
    public void setAuthor(String author){ this.author.set(author);}

    public String getDirectory(){ return directory.get();}
    public void setDirectory(String directory){ this.directory.set(directory);}
}