package Classes;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.concurrent.Task;
import javafx.scene.control.Label;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.ArrayList;

public class FileSystemSearch extends Task {
    private File entryDirectory;
    private File currentDirectory;
    private String keyword;
    private Integer isMatchCase;
    private ArrayList<String> extensions;
    private Main.Controller sourceClass;
    private Boolean DFS;

    public FileSystemSearch(File entryDirectory, File currentDirectory, String keyword, Main.Controller sourceClass, ArrayList<String> extensions, Boolean DFS) {
        this.entryDirectory = entryDirectory;
        this.currentDirectory = currentDirectory;
        this.keyword = keyword;
        this.sourceClass = sourceClass;
        this.extensions = extensions;
        this.DFS = DFS;
    }

    public File getEntryDirectory() {
        return entryDirectory;
    }

    public void setEntryDirectory(File entryDirectory) {
        this.entryDirectory = entryDirectory;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public ArrayList<String> getExtensions() {
        return extensions;
    }

    public void setExtensions(ArrayList<String> extensions) {
        this.extensions = extensions;
    }

    private Integer IsMatchCase() {
        return isMatchCase;
    }

    public void setIsMatchCase(Integer isMatchCase) {
        this.isMatchCase = isMatchCase;
    }

    @Override
    public Object call() throws InterruptedException, IOException {
        if(DFS)
            searchWithDFS();
        else
            searchWithBFS();
        return null;
    }

    private void searchWithDFS() throws InterruptedException, IOException {
        if (currentDirectory.isDirectory() && !currentDirectory.getPath().substring(currentDirectory.getPath().lastIndexOf('\\')+1).startsWith("$")){
            if (currentDirectory.canRead()){
                File[] subPaths = currentDirectory.listFiles();
                if(subPaths != null){
                    for (File path : subPaths){
                        if (path.isDirectory()){
                            FileSystemSearch recursiveSearch = new FileSystemSearch(entryDirectory,path,keyword,sourceClass,extensions, true);
                            recursiveSearch.setIsMatchCase(isMatchCase);
                            Thread recursiveThread = new Thread(recursiveSearch);
                            recursiveThread.start();
                            recursiveThread.join();
                        }
                        else{
                            sourceClass.pbm.setWorkFinished(sourceClass.pbm.getWorkFinished()+1);
                            if(sourceClass.pbm.getTotalWorkLoad() > 0){
                                if(sourceClass.pbm.getWorkFinished()/sourceClass.pbm.getTotalWorkLoad() <= 1.0 && sourceClass.pbm.getProgress() < sourceClass.pbm.getWorkFinished()/sourceClass.pbm.getTotalWorkLoad())
                                    sourceClass.pbm.setProgress(sourceClass.pbm.getWorkFinished()/sourceClass.pbm.getTotalWorkLoad());
                            }
                            if(path.getName().contains(".")){
                                int i = path.getName().lastIndexOf('.');
                                String fileName = path.getName().substring(0,i);
                                String extension = path.getName().substring(i+1);
                                for(i=0; i < extensions.size() && !extensions.get(i).equals(extension); i++ );
                                Boolean[] matchCase = new Boolean[]{
                                        fileName.toLowerCase().contains(keyword.toLowerCase()),
                                        fileName.contains(keyword)
                                };
                                if((i<extensions.size()) && (matchCase[IsMatchCase()]) && (!Files.getOwner(path.toPath()).toString().contains("BUILTIN"))){
                                    try {
                                        ArrayList<String> foundedFile = new ArrayList<>();
                                        foundedFile.add(path.getAbsoluteFile().toString().replace('\\', '/'));
                                        sourceClass.getClass().getMethod("createScannedFiles", ArrayList.class).invoke(sourceClass, foundedFile);
                                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                } else
                    System.out.println(currentDirectory.getAbsoluteFile() + " is an empty directory!");
            } else
                System.out.println(currentDirectory.getAbsoluteFile() + " is not a readable directory!");
        } else
            System.out.println(currentDirectory.getAbsoluteFile() + " is not a appropriate directory!");
    }

    private void searchWithBFS() {

    }
}
