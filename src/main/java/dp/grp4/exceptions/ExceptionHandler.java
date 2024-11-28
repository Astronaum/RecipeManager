package dp.grp4.exceptions;

import dp.grp4.helpers.Helper;
import javafx.scene.control.Alert;


public class ExceptionHandler {
    public static void handle(Exception exception){
        System.err.println("ExceptionHandler:");
        System.err.println("\t"+exception.getClass().getName());
        System.err.println("\t"+exception.getMessage());
         exception.printStackTrace();
        System.out.println(exception.getCause());
        Helper.showAlert(Alert.AlertType.ERROR,"Error",exception.getMessage());
    }
    public static void context(RunnableWithException f){
        try {
            f.run();
        }catch(Exception exception){
            ExceptionHandler.handle(exception);
        }
    }
    @FunctionalInterface
    public interface RunnableWithException {
        void run() throws Exception;
    }

}
