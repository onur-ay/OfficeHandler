package Classes;

import javafx.beans.property.DoubleProperty;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class ProgressIndicatorBar extends StackPane {
    private DoubleProperty workDone;
    private DoubleProperty totalWork;

    private ProgressBar progressBar;
    private Text text = new Text();
    private String percentageStringFormat;

    public ProgressIndicatorBar(){

    }

    public ProgressIndicatorBar(DoubleProperty workDone, DoubleProperty totalWork, ProgressBar progressBar, String percentageStringFormat) {
        this.workDone = workDone;
        this.totalWork = totalWork;
        this.progressBar = progressBar;
        this.percentageStringFormat = percentageStringFormat;

        this.progressBar.setVisible(true);
        this.text.setVisible(true);
        updateProgress();
        workDone.addListener((observable, oldValue, newValue) -> updateProgress());

        getChildren().setAll(progressBar, text);
    }

    private void updateProgress() {
        if(workDone == null || totalWork.get() == 0){
            text.setText("");
            progressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
        } else {
            text.setText(String.format(percentageStringFormat, Maths.round(workDone.get()/totalWork.get(), 2)));
            progressBar.setProgress(workDone.get() / totalWork.get());
        }
    }
}
