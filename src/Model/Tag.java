package Model;

public class Tag {
    public Tag(int ID, String name) {
        this.ID = ID;
        Name = name;
    }

    public int ID;

    public String Name;

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
}
