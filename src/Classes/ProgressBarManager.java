package Classes;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import static Classes.Constants.*;

public class ProgressBarManager {

    private DoubleProperty workFinished = new SimpleDoubleProperty();
    private DoubleProperty totalWorkLoad = new SimpleDoubleProperty();
    private DoubleProperty progress = new SimpleDoubleProperty();
    private StringProperty progressText = new SimpleStringProperty();

    public double getWorkFinished() {
        return workFinished.get();
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
        setProgressText(String.format(PROGRESS_TEXT_PATTERN,progress*100));
    }

    public String getProgressText() {
        return progressText.get();
    }

    private void setProgressText(String progressText) {
        this.progressText.set(progressText);
    }

    public ProgressBarManager(double workFinished, double totalWorkLoad, double progress, double progressText) {
        this.workFinished.set(workFinished);
        this.totalWorkLoad.set(totalWorkLoad);
        this.progress.set(progress);
        this.progressText.set(String.format(PROGRESS_INDICATOR_PATTERN,progressText));
    }
}