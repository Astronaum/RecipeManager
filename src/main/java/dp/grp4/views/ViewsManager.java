package dp.grp4.views;

import dp.grp4.exceptions.ViewsManagerException;
import dp.grp4.orders.OrderFirer;
import dp.grp4.orders.OrderListener;
import dp.grp4.orders.OrderType;
import javafx.stage.Stage;
import java.util.Collection;
import java.util.HashSet;

public class ViewsManager implements OrderListener {
    private static final Class<?>[] classesOfManagedViews=new Class<?>[]{
            HomeView.class,
            IngredientsView.class,
            RecipesView.class,
            ModalView.class
    };
    private Stage stage;
    private  Collection<InteractiveView> views;
    private ViewsManager(){}
    private static final ViewsManager instance=new ViewsManager();
    public static ViewsManager getInstance() throws Exception {
        if(instance.stage==null) throw new ViewsManagerException("ViewsManager is not initialized");
        return instance;
    }
    public static void init(Stage stage){
        instance.stage = stage;
        instance.views = new HashSet<>();
        for(Class<?> viewClass:classesOfManagedViews)
            try {
                viewClass.getMethod("create", ViewsManager.class).invoke(null,instance);
            } catch (Exception ignored) {}
    }

    @Override
    public void setSubscription(OrderFirer orderFirer) {
        orderFirer.subscription(
                this,
                OrderType.values()
        );
    }
    @Override
    public void processOrder(OrderType orderType) {
        Class<?> viewClass= switch (orderType) {
            case SHOW_INGREDIENTS ->IngredientsView.class;
            case SHOW_RECIPES->RecipesView.class;
            case SHOW_HOME->HomeView.class;
            case SHOW_MODAL -> ModalView.class;
        };
        InteractiveView view=this.views.stream().filter(v-> v.getClass()==viewClass).findFirst().orElseThrow();
        if(view instanceof  ModalView){
            ((ModalView) view).open();
            return;
        }
        this.stage.setScene(view.getScene());
        this.stage.show();
    }
    public Stage getStage() {
        return this.stage;
    }
    public void addView(InteractiveView view) {
        this.views.add(view);
    }
    public InteractiveView getViewInstance(Class<?> viewClassName){
        return this.views.stream().filter(v-> v.getClass()==viewClassName).findFirst().orElseThrow();
    }
    public void launch(){
        this.processOrder(OrderType.SHOW_HOME);
    }

}
