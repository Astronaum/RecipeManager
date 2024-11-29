package dp.grp4.helpers;

import dp.grp4.exceptions.ExceptionHandler;
import dp.grp4.views.ViewsManager;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Helper {
    private Helper(){}
    @SuppressWarnings("unchecked")
    public static <T> T getViewInstance(Class<T> clazz){
        ViewsManager viewsManager = ViewsManager.getInstance();
        return (T) viewsManager.getViewInstance(clazz);
    }
    public static void callClassMethod(Class<?> clazz,String methodName, Object ...args){
        ExceptionHandler.context(()->{
            clazz.getMethod(methodName, ViewsManager.class).invoke(null,args);
        });
    }
    public static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        //wait for the stage to be initialized
        final Timeline[] timelineRef = new Timeline[1];
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(500), event -> {
                    Stage stage = ViewsManager.getInstance().getStage();
                    if (stage != null) {
                        if (alert.getOwner() == null) {
                            alert.initOwner(stage);
                        }
                        alert.show();
                        timelineRef[0].stop();
                    }
                })
        );
        timelineRef[0] = timeline;
        timeline.setCycleCount(3);
        timeline.play();
    }
    public static <T> void setTableViewProperties(TableView<T> t){
        t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        t.setSelectionModel(null);
        t.getColumns().forEach(column -> {
            column.setReorderable(false);
            column.setSortable(false);
        });
    }
}
