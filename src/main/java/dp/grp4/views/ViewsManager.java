package dp.grp4.views;

import dp.grp4.exceptions.ViewsManagerException;
import dp.grp4.orders.OrderFirer;
import dp.grp4.orders.OrderListener;
import dp.grp4.orders.OrderType;
import javafx.stage.Stage;

import java.util.Collection;
import java.util.HashSet;

public class ViewsManager implements OrderListener {
    private static final Class<?>[] classesOfManagedViews = new Class<?>[]{
            HomeView.class,
            IngredientsView.class,
            RecipesView.class,
            ModalView.class
    };

    private Stage stage;
    private Collection<InteractiveView> views;
    private static final ViewsManager instance = new ViewsManager();

    private ViewsManager() {}

    public static ViewsManager getInstance() throws ViewsManagerException {
        if (instance.stage == null) {
            throw new ViewsManagerException("ViewsManager is not initialized");
        }
        return instance;
    }

    public static void init(Stage stage) {
        instance.stage = stage;
        instance.views = new HashSet<>();
        for (Class<?> viewClass : classesOfManagedViews) {
            try {
                viewClass.getMethod("create", ViewsManager.class).invoke(null, instance);
            } catch (Exception e) {
                System.err.println("Failed to initialize view: " + viewClass.getSimpleName());
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setSubscription(OrderFirer orderFirer) {
        orderFirer.subscription(this, OrderType.values());
    }

    @Override
    public void processOrder(OrderType orderType) throws ViewsManagerException {
        Class<?> viewClass = switch (orderType) {
            case SHOW_INGREDIENTS -> IngredientsView.class;
            case SHOW_RECIPES -> RecipesView.class;
            case SHOW_HOME -> HomeView.class;
            case SHOW_MODAL -> ModalView.class;
        };

        InteractiveView view = this.views.stream()
                .filter(v -> v.getClass() == viewClass)
                .findFirst()
                .orElseThrow(() -> new ViewsManagerException("View not found for: " + viewClass.getSimpleName()));

        System.out.println("Navigating to: " + viewClass.getSimpleName());

        if (view instanceof ModalView) {
            ((ModalView) view).open();
        } else {
            this.stage.setScene(view.getScene());
            this.stage.show();
        }
    }

    public Stage getStage() {
        return this.stage;
    }

    public void addView(InteractiveView view) {
        this.views.add(view);
        System.out.println("View added: " + view.getClass().getSimpleName());
    }

    public InteractiveView getViewInstance(Class<?> viewClassName) throws ViewsManagerException {
        return this.views.stream()
                .filter(v -> v.getClass() == viewClassName)
                .findFirst()
                .orElseThrow(() -> new ViewsManagerException("View instance not found for: " + viewClassName.getSimpleName()));
    }

    public void launch() throws ViewsManagerException {
        this.processOrder(OrderType.SHOW_HOME);
    }
}
