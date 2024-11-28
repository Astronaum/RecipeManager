package dp.grp4.views;

import dp.grp4.exceptions.ExceptionHandler;
import dp.grp4.exceptions.ViewsManagerException;
import dp.grp4.helpers.Helper;
import dp.grp4.orders.OrderFirer;
import dp.grp4.orders.OrderListener;
import dp.grp4.orders.OrderType;
import javafx.stage.Stage;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class ViewsManager implements OrderListener {
    private static final List<Class<?>> managedViews= List.of(
            HomeView.class,
            IngredientsView.class,
            RecipesView.class,
            ConfirmModalView.class,
            IngredientModalView.class,
            RecipeModalView.class,
            FeasibleRecipesView.class,
            FavoritesRecipesView.class,
            RecipeDetailsModalView.class
    );
    private Stage stage;
    private  Collection<InteractiveView> views;
    private ViewsManager(){}
    private static final ViewsManager instance=new ViewsManager();
    public static ViewsManager getInstance()  {
        ExceptionHandler.context(()->{
            if(instance.stage==null) throw new ViewsManagerException("The ViewsManager is not initialized");
        });
        return instance;
    }
    public static void init(Stage stage){
        instance.stage = stage;
        instance.views = new HashSet<>();
        for(Class<?> viewClass:managedViews)
            Helper.callClassMethod(viewClass,"create", instance);
    }

    @Override
    public void setSubscription(OrderFirer orderFirer) {
        orderFirer.subscription(
                this,
                OrderType.values()
        );
    }
    @Override
    public void processOrder(OrderType orderType) throws ViewsManagerException {
        Class<?> viewClass= switch (orderType) {
            case SHOW_INGREDIENTS ->IngredientsView.class;
            case SHOW_RECIPES->RecipesView.class;
            case SHOW_HOME->HomeView.class;
            case SHOW_CONFIRM_MODAL-> ConfirmModalView.class;
            case SHOW_INGREDIENT_MODAL -> IngredientModalView.class;
            case SHOW_RECIPE_MODAL -> RecipeModalView.class;
            case SHOW_RECIPE_DETAILS_MODAL -> RecipeDetailsModalView.class;
            case SHOW_FEASIBLE_RECIPES -> FeasibleRecipesView.class;
            case SHOW_FAVORITES -> FavoritesRecipesView.class;
        };
        InteractiveView view = this.views.stream()
                .filter(v -> v.getClass() == viewClass)
                .findFirst()
                .orElseThrow(() -> new ViewsManagerException("View not found: " + viewClass.getName()));
        if(view instanceof ModalView){
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
    public void launch() throws ViewsManagerException {
        this.processOrder(OrderType.SHOW_HOME);
    }

}