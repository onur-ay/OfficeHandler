package Classes;

import Main.Controller;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Task;
import sun.java2d.pipe.SpanShapeRenderer;

import java.io.File;

public class FileCount extends Task {
    private DoubleProperty count = new SimpleDoubleProperty();
    private File entryDirectory;
    private File currentDirectory;
    private Main.Controller sourceClass;

    public double getCount() {
        return count.get();
    }

    public DoubleProperty countProperty() {
        return count;
    }

    public void setCount(double count) {
        this.count.set(count);
    }

    public File getEntryDirectory() {
        return entryDirectory;
    }

    public void setEntryDirectory(File entryDirectory) {
        this.entryDirectory = entryDirectory;
    }

    public File getCurrentDirectory() {
        return currentDirectory;
    }

    public void setCurrentDirectory(File currentDirectory) {
        this.currentDirectory = currentDirectory;
    }

    public FileCount(File entryDirectory, File currentDirectory, Controller sourceClass) {
        this.entryDirectory = entryDirectory;
        this.currentDirectory = currentDirectory;
        this.sourceClass = sourceClass;
    }

    @Override
    public Object call() throws InterruptedException {
        countFiles();
        return null;
    }

    private void countFiles() throws InterruptedException {
        if (currentDirectory.isDirectory() && !currentDirectory.getPath().substring(currentDirectory.getPath().lastIndexOf('\\')+1).startsWith("$"))
            if (currentDirectory.canRead()){
                File[] subPaths = currentDirectory.listFiles();
                if(subPaths != null)
                    for (File path : subPaths){
                        if (path.isDirectory()){
                            FileCount recursiveCount = new FileCount(entryDirectory,path,sourceClass);
                            Thread recursiveThread = new Thread(recursiveCount);
                            recursiveThread.start();
                            recursiveThread.join();
                        }
                        else
                            sourceClass.pbm.setTotalWorkLoad(sourceClass.pbm.getTotalWorkLoad()+1);
                    }

            }

    }
}
