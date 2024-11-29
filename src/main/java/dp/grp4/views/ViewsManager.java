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
import java.util.Map;
import javafx.scene.image.Image;
import static dp.grp4.orders.OrderType.*;

public class ViewsManager implements OrderListener {
    private static final int WINDOW_WIDTH=1000, WINDOW_HEIGHT=600;
    private static final Image APP_ICON=new Image("/images/icon.png");
    private static final String APP_TITLE="Recipes Manager";
    private static final Map<OrderType,Class<?>> managedViews=Map.of(
            SHOW_HOME,HomeView.class,
            SHOW_INGREDIENTS,IngredientsView.class,
            SHOW_RECIPES,RecipesView.class,
            SHOW_CONFIRM_MODAL,ConfirmModalView.class,
            SHOW_INGREDIENT_MODAL, IngredientModalView.class,
            SHOW_RECIPE_MODAL,RecipeModalView.class,
            SHOW_FEASIBLE_RECIPES,FeasibleRecipesView.class,
            SHOW_FAVORITES,FavoritesRecipesView.class,
            SHOW_RECIPE_DETAILS_MODAL,RecipeDetailsModalView.class,
            SHOW_SETTINGS,SettingsView.class
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
        instance.stage.getIcons().add(APP_ICON);
        instance.stage.setTitle(APP_TITLE);
        instance.stage.setWidth(WINDOW_WIDTH);
        instance.stage.setHeight(WINDOW_HEIGHT);
        instance.views = new HashSet<>();
        for(Class<?> viewClass:managedViews.values())
            Helper.callClassMethod(viewClass,"create", instance);
    }
    public void reloadViews(){
        views.forEach(view->{
            if (view instanceof InitializableView) ((InitializableView) view).initialize();
        });
    }
    @Override
    public void setSubscription(OrderFirer orderFirer) {
        orderFirer.subscription(this, OrderType.values());
    }
    @Override
    public void processOrder(OrderType orderType) throws ViewsManagerException {
        Class<?> viewClass=managedViews.get(orderType);
        InteractiveView view = this.views.stream()
                .filter(v -> v.getClass() == viewClass)
                .findFirst()
                .orElseThrow(() -> new ViewsManagerException("View "+viewClass.getName()+" not found"));
        if(view instanceof ModalView)
            ((ModalView) view).open();
        else {
            this.stage.setScene(view.getScene());
            this.stage.show();
        }
    }
    public Stage getStage() {
        return this.stage;
    }
    public Image getAppIcon(){
        return APP_ICON;
    }
    public void addView(InteractiveView view) {
        this.views.add(view);
    }
    public InteractiveView getViewInstance(Class<?> viewClassName){
        return this.views.stream().filter(v-> v.getClass()==viewClassName).findFirst().orElseThrow();
    }
    public void launch() throws ViewsManagerException {
        this.processOrder(SHOW_HOME);
    }
}