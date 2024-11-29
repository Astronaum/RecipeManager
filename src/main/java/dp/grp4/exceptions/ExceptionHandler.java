package dp.grp4.exceptions;

import dp.grp4.helpers.Helper;
import javafx.scene.control.Alert;

public class ExceptionHandler {
    public static void handle(Exception exception){
        System.err.println("ExceptionHandler:");
        System.err.println("\t"+exception.getClass().getName());
        System.err.println("\t"+exception.getMessage());
        Throwable cause = exception.getCause();
        System.err.println(cause!=null?cause.toString():"\tNo cause available.");
        if(exception.getClass()== DBException.class && ((DBException) exception).getCode()==0)
            Helper.showAlert(Alert.AlertType.INFORMATION,"","Please set a valid database directory to store your files.");
        else
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
