package Classes;

import javafx.concurrent.Task;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class FileSystemSearch extends Task {
    private File entryDirectory;
    private File currentDirectory;
    private String keyword;
    private Integer isMatchCase;
    private ArrayList<String> extensions;
    private Main.Controller sourceClass;
    private Boolean isDFS;
    private Boolean createFile;

    public FileSystemSearch(File entryDirectory, File currentDirectory, String keyword, Main.Controller sourceClass, ArrayList<String> extensions, Boolean searchAlgorithm, Boolean createFile) {
        this.entryDirectory = entryDirectory;
        this.currentDirectory = currentDirectory;
        this.keyword = keyword;
        this.sourceClass = sourceClass;
        this.extensions = extensions;
        this.isDFS = searchAlgorithm;
        this.createFile = createFile;
    }

    private Integer IsMatchCase() {
        return isMatchCase;
    }

    public void setIsMatchCase(Integer isMatchCase) {
        this.isMatchCase = isMatchCase;
    }

    @Override
    public Object call() throws InterruptedException, IOException {
        if(isDFS)
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
                            FileSystemSearch recursiveSearch = new FileSystemSearch(entryDirectory,path,keyword,sourceClass,extensions,isDFS,createFile);
                            recursiveSearch.setIsMatchCase(isMatchCase);
                            Thread recursiveThread = new Thread(recursiveSearch);
                            recursiveThread.start();
                            recursiveThread.join();
                        }
                        else{
                            if(createFile)
                                updateProgressBar();
                            if(hasExtension(path))
                                addFileIfAccurate(path,createFile);
                        }
                    }
                } else
                    System.out.println(currentDirectory.getAbsoluteFile() + " is an empty directory!");
            } else
                System.out.println(currentDirectory.getAbsoluteFile() + " is not a readable directory!");
        } else
            System.out.println(currentDirectory.getAbsoluteFile() + " is not a appropriate directory!");
    }

    private void searchWithBFS() throws IOException {
        Queue<File> queue = new LinkedList<>();
        queue.add(currentDirectory);

        while (!queue.isEmpty()) {
            currentDirectory = queue.poll();
            if (currentDirectory.isDirectory() && !currentDirectory.getPath().substring(currentDirectory.getPath().lastIndexOf('\\')+1).startsWith("$")){
                if (currentDirectory.canRead()) {
                    File[] subPaths = currentDirectory.listFiles();
                    if (subPaths != null) {
                        for (File path : subPaths) {
                            if (path.isDirectory())
                                queue.add(path);
                            else{
                                if(createFile)
                                    updateProgressBar();
                                if(hasExtension(path))
                                    addFileIfAccurate(path,createFile);
                            }
                        }
                    } else
                        System.out.println(currentDirectory.getAbsoluteFile() + " is an empty directory!");
                } else
                    System.out.println(currentDirectory.getAbsoluteFile() + " is not a readable directory!");
            } else
                System.out.println(currentDirectory.getAbsoluteFile() + " is not a appropriate directory!");
        }
    }

    private void updateProgressBar(){
        sourceClass.pbm.setWorkFinished(sourceClass.pbm.getWorkFinished()+1);
        if(sourceClass.pbm.getTotalWorkLoad() > 0){
            if(sourceClass.pbm.getWorkFinished()/sourceClass.pbm.getTotalWorkLoad() <= 1.0 && sourceClass.pbm.getProgress() < sourceClass.pbm.getWorkFinished()/sourceClass.pbm.getTotalWorkLoad())
                sourceClass.pbm.setProgress(sourceClass.pbm.getWorkFinished()/sourceClass.pbm.getTotalWorkLoad());
        }
    }

    private void addFileIfAccurate(File path, boolean createFile) throws IOException {
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
                if(createFile){
                    ArrayList<String> foundedFile = new ArrayList<>();
                    foundedFile.add(path.getAbsoluteFile().toString().replace('\\', '/'));
                    sourceClass.getClass().getMethod("createScannedFiles", ArrayList.class).invoke(sourceClass, foundedFile);
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean hasExtension(File path){
        return path.getName().contains(".");
    }
}
