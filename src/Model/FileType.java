package Model;

public class FileType {

    private Integer ID;
    private String Name;
    private Integer Default;

    public FileType(){    }

    public FileType(String name) { Name = name; }

    public FileType(Integer ID, String name) { this.ID = ID; Name = name; }

    public FileType(Integer ID, String name, Integer aDefault) { this.ID = ID; Name = name; Default = aDefault; }

    public String getName() { return Name; }

    public Integer getDefault() { return Default; }

    public void setDefault(Integer aDefault) { Default = aDefault; }
}
