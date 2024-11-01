package dp.grp4.views;

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
            RecipesView.class
    };
    private Stage stage;
    private Collection<InteractiveView> views;

    private ViewsManager(){}
    public static ViewsManager getInstance(Stage stage){
        ViewsManager viewsManager=new ViewsManager();
        viewsManager.stage = stage;
        viewsManager.views = new HashSet<>();
        for(Class<?> viewClass:classesOfManagedViews)
            try {
                viewClass.getMethod("create", ViewsManager.class).invoke(null,viewsManager);
            } catch (Exception ignored) {}
        return viewsManager;
    }

    @Override
    public void setSubscription(OrderFirer orderFirer) {
        orderFirer.subscription(
                this,
                OrderType.SHOW_INGREDIENTS,OrderType.SHOW_RECIPES,OrderType.SHOW_HOME
        );
    }
    @Override
    public void processOrder(OrderType orderType) {
        Class<?> viewClass= switch (orderType) {
            case SHOW_INGREDIENTS ->IngredientsView.class;
            case SHOW_RECIPES->RecipesView.class;
            case SHOW_HOME->HomeView.class;
        };
        InteractiveView view=this.views.stream().filter(v-> v.getClass()==viewClass).findFirst().orElseThrow();
        this.getStage().setScene(view.getScene());
        this.getStage().show();
    }
    public Stage getStage() {
        return this.stage;
    }
    public void addView(InteractiveView view) {
        this.views.add(view);
    }

}
