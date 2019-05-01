package Classes;

import javafx.beans.binding.NumberBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.naming.Binding;

public class ProgressBarManager {

    private DoubleProperty workFinished = new SimpleDoubleProperty();
    private DoubleProperty totalWorkLoad = new SimpleDoubleProperty();
    private DoubleProperty progress = new SimpleDoubleProperty();
    private StringProperty progressText = new SimpleStringProperty();
    private DoubleProperty progressPercentage = new SimpleDoubleProperty();
    private int count=0;

    public double getWorkFinished() {
        return workFinished.get();
    }

    public double getProgressPercentage(){
        return workFinished.get()*100;
    }

    public DoubleProperty workFinishedProperty() {
        return workFinished;
    }

    public void setWorkFinished(double workFinished) {
        this.workFinished.set(workFinished);
    }

    public double getTotalWorkLoad() {
        return totalWorkLoad.get();
    }

    public DoubleProperty totalWorkLoadProperty() {
        return totalWorkLoad;
    }

    public void setTotalWorkLoad(double totalWorkLoad) {
        this.totalWorkLoad.set(totalWorkLoad);
    }

    public double getProgress() {
        return progress.get();
    }

    public DoubleProperty progressProperty() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress.set(progress);
        setProgressText(String.format("%.2f%%",progress*100));
    }

    public String getProgressText() {
        return progressText.get();
    }

    public StringProperty progressTextProperty() {
        return progressText;
    }

    public void setProgressText(String progressText) {
        this.progressText.set(progressText);
    }

    public DoubleProperty progressPercentageProperty() {
        return progressPercentage;
    }

    public void setProgressPercentage(double workFinishedPercentage) {
        this.progressPercentage.set(workFinishedPercentage);
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ProgressBarManager(double workFinished, double totalWorkLoad, double progress, double progressText) {
        this.workFinished.set(workFinished);
        this.totalWorkLoad.set(totalWorkLoad);
        this.progress.set(progress);
        this.progressText.set(String.format("%%%.0f",progressText));
    }
}