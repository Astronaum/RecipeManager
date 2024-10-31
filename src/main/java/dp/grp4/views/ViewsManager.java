package dp.grp4.views;

import dp.grp4.controllers.*;
import dp.grp4.orders.OrderFirer;
import dp.grp4.orders.OrderListener;
import dp.grp4.orders.OrderType;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class ViewsManager implements OrderListener {
    // Singleton design pattern because we need only one Views manager in the app
    private static final ViewsManager INSTANCE=new ViewsManager();
    private ViewsManager(){}

    private Stage stage;
    private Collection<OrderListener> orderListeners;
    private Collection<InteractiveView> views;
    private HomeView homeView;
    private IngredientsView ingredientsView;
    private RecipesView recipesView;

    public static ViewsManager getInstance(Stage stage) throws IOException {
        INSTANCE.stage = stage;
        INSTANCE.orderListeners = new ArrayList<>();
        INSTANCE.views = new ArrayList<>();
        // views instantiation
        INSTANCE.ingredientsView = IngredientsView.create(INSTANCE);
        INSTANCE.recipesView = RecipesView.create(INSTANCE);
        INSTANCE.homeView = HomeView.create(INSTANCE);
        return INSTANCE;
    }

    public Stage getStage() {
        return this.stage;
    }
    public void addView(InteractiveView view) {
        this.views.add(view);
    }
    public void addOrderListener(OrderListener orderListener) {
        this.orderListeners.add(orderListener);
    }
    public Collection<InteractiveView> getViews(){
        return this.views;
    }
    public void setController(Controller controller) {
        this.views.forEach(v -> v.setController(controller));
    }
    @Override
    public void setSubscription(OrderFirer orderFirer) {
        this.orderListeners.forEach(e -> e.setSubscription(orderFirer));
        orderFirer.subscription(
                this,
                OrderType.SHOW_INGREDIENTS,OrderType.SHOW_RECIPES,OrderType.SHOW_HOME
        );
    }
    @Override
    public void processOrder(OrderType e) {
        switch (e) {
            case SHOW_INGREDIENTS:
                this.getStage().setScene(INSTANCE.ingredientsView.getScene());
                this.getStage().show();
                break;
            case SHOW_RECIPES:
                this.getStage().setScene(INSTANCE.recipesView.getScene());
                this.getStage().show();
                break;
            case SHOW_HOME:
                this.getStage().setScene(INSTANCE.homeView.getScene());
                this.getStage().show();
                break;
        }
    }
}
