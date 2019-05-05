package Classes;

import Main.Controller;
import javafx.concurrent.Task;
import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

public class FileCount extends Task {
    private File entryDirectory;
    private File currentDirectory;
    private Main.Controller sourceClass;
    private Boolean isDFS;

    public FileCount(File entryDirectory, File currentDirectory, Controller sourceClass, Boolean algorithm) {
        this.entryDirectory = entryDirectory;
        this.currentDirectory = currentDirectory;
        this.sourceClass = sourceClass;
        this.isDFS = algorithm;
    }

    @Override
    public Object call() throws InterruptedException {
        if(isDFS)
            countFilesWithDFS();
        else
            countFilesWithBFS();
        return null;
    }

    private void countFilesWithDFS() throws InterruptedException {
        if (currentDirectory.isDirectory() && !currentDirectory.getPath().substring(currentDirectory.getPath().lastIndexOf('\\')+1).startsWith("$"))
            if (currentDirectory.canRead()){
                File[] subPaths = currentDirectory.listFiles();
                if(subPaths != null)
                    for (File path : subPaths){
                        if (path.isDirectory()){
                            FileCount recursiveCount = new FileCount(entryDirectory,path,sourceClass,isDFS);
                            Thread recursiveThread = new Thread(recursiveCount);
                            recursiveThread.start();
                            recursiveThread.join();
                        }
                        else
                            sourceClass.pbm.setTotalWorkLoad(sourceClass.pbm.getTotalWorkLoad()+1);
                    }
            }
    }

    private void countFilesWithBFS(){
        Queue<File> queue = new LinkedList<>();
        queue.add(currentDirectory);

        while (!queue.isEmpty()) {
            currentDirectory = queue.poll();
            if (currentDirectory.isDirectory() && !currentDirectory.getPath().substring(currentDirectory.getPath().lastIndexOf('\\') + 1).startsWith("$"))
                if (currentDirectory.canRead()) {
                    File[] subPaths = currentDirectory.listFiles();
                    if (subPaths != null)
                        for (File path : subPaths) {
                            if (path.isDirectory())
                                queue.add(path);
                            else
                                sourceClass.pbm.setTotalWorkLoad(sourceClass.pbm.getTotalWorkLoad() + 1);
                        }
                }
        }
    }
}
