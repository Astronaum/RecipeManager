package dp.grp4.exceptions;

import dp.grp4.orders.OrderType;
import dp.grp4.views.ModalView;
import dp.grp4.views.ViewsManager;


public class ExceptionHandler {
    public static void handle(Exception exception){
        System.out.println("ExceptionHandler:");
        System.out.println("\t"+exception.getMessage());
        try {
            ViewsManager viewsManager = ViewsManager.getInstance();
            ModalView modalView = (ModalView) viewsManager.getViewInstance(ModalView.class);
            modalView.setModalText(exception.getMessage());
            viewsManager.processOrder(OrderType.SHOW_MODAL);
        }catch(Exception exception1){
            ExceptionHandler.handle(exception1);
        }
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
