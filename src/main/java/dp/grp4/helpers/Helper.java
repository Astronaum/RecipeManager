package dp.grp4.helpers;

import dp.grp4.exceptions.ExceptionHandler;
import dp.grp4.views.ViewsManager;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

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
        alert.initOwner(ViewsManager.getInstance().getStage());
        alert.show();
    }
}
